/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game;

import entity.Player;
import entity.Enemy;
import entity.Character;
import entity.Warrior;
import entity.Mage;
import entity.Tank;
import entity.Archer;
import entity.Healer;
import object.Item;
import tile.TileManager;
import data.SaveLoad;

import javax.swing.JPanel;
import javax.swing.JOptionPane;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
/**
 *
 * @author nicol
 */
public class GamePanel extends JPanel implements Runnable {

    public final int tileSize = 48;
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;

    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;
    
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;
    
    Thread gameThread;

    KeyHandler keyH = new KeyHandler(this);
    public Player player = new Player(this, keyH);

    public TileManager tileM = new TileManager(this);
    public CollisionChecker cChecker = new CollisionChecker(this);

    public Enemy enemy;
    public BattleManager battleManager = new BattleManager(this);
    public SaveLoad saveLoad = new SaveLoad(this);

    public ArrayList<Character> party = new ArrayList<>();
    public ArrayList<Item> inventory = new ArrayList<>();

    public int currentWave = 1;
    public int gold = 0;
    public int enemiesDefeated = 0;
    public int turnsTaken = 0;

    public String message = "Explore the map and touch the enemy to battle.";

    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;

    public GamePanel() {

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);

        this.addKeyListener(keyH);
        this.setFocusable(true);

        gameState = titleState;
    }

    public void chooseHeroes() {

        party.clear();

        while(party.size() < 3) {

            String[] choices = {
                "Warrior",
                "Mage",
                "Tank",
                "Archer",
                "Healer"
            };

            int choice = JOptionPane.showOptionDialog(
                    null,
                    "Choose hero " + (party.size() + 1) + " of 3:",
                    "Hero Selection",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    choices,
                    choices[0]
            );

            if(choice == 0) {
                party.add(new Warrior());
            } else if(choice == 1) {
                party.add(new Mage());
            } else if(choice == 2) {
                party.add(new Tank());
            } else if(choice == 3) {
                party.add(new Archer());
            } else if(choice == 4) {
                party.add(new Healer());
            } else {
                JOptionPane.showMessageDialog(null, "Please choose a hero.");
            }
        }

        JOptionPane.showMessageDialog(
                null,
                "Your party is ready:\n" +
                party.get(0).getName() + "\n" +
                party.get(1).getName() + "\n" +
                party.get(2).getName()
        );
    }

    public void setupItems() {

        inventory.clear();

        inventory.add(new Item("Health Potion", "Restores 30 HP to one hero.", "heal"));
        inventory.add(new Item("Mana Elixir", "Boosts skill damage.", "boost"));
        inventory.add(new Item("Revive Scroll", "Revives a defeated hero.", "revive"));

        startNextWave();
    }

    public void startNextWave() {

        if(currentWave == 1) {
            enemy = new Enemy(this, "Slime", 500, 300, 50, 10);
        } else if(currentWave == 2) {
            enemy = new Enemy(this, "Goblin", 500, 300, 70, 15);
        } else if(currentWave == 3) {
            enemy = new Enemy(this,"Skeleton", 500, 300, 90, 18);
        } else if(currentWave == 4) {
            enemy = new Enemy(this,"Orc", 500, 300, 120, 22);
        } else if(currentWave == 5) {
            enemy = new Enemy(this,"Dark Mage", 500, 300, 140, 25);
        } else if(currentWave == 6) {
            enemy = new Enemy(this,"Dragon Boss", 500, 300, 200, 35);
        }

        message = "Wave " + currentWave + " started!";
    }

    public void openShop() {

        String[] choices = {
            "Buy Health Potion - 20 Gold",
            "Buy Mana Elixir - 25 Gold",
            "Buy Revive Scroll - 40 Gold",
            "Exit Shop"
        };

        boolean shopping = true;

        while(shopping) {

            int choice = JOptionPane.showOptionDialog(
                    null,
                    "SHOP\nGold: " + gold +
                    "\nInventory Items: " + inventory.size(),
                    "Shop",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    choices,
                    choices[0]
            );

            if(choice == 0) {
                if(gold >= 20) {
                    gold -= 20;
                    inventory.add(new Item("Health Potion", "Restores 30 HP.", "heal"));
                    message = "Bought Health Potion.";
                } else {
                    JOptionPane.showMessageDialog(null, "Not enough gold.");
                }
            } else if(choice == 1) {
                if(gold >= 25) {
                    gold -= 25;
                    inventory.add(new Item("Mana Elixir", "Boosts skill damage.", "boost"));
                    message = "Bought Mana Elixir.";
                } else {
                    JOptionPane.showMessageDialog(null, "Not enough gold.");
                }
            } else if(choice == 2) {
                if(gold >= 40) {
                    gold -= 40;
                    inventory.add(new Item("Revive Scroll", "Revives a hero.", "revive"));
                    message = "Bought Revive Scroll.";
                } else {
                    JOptionPane.showMessageDialog(null, "Not enough gold.");
                }
            } else {
                shopping = false;
            }
        }
    }

    public void startGameThread() {

        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        double drawInterval = 1000000000 / 60;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while(gameThread != null) {

            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if(delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }

    public void update() {

        if(gameState == playState) {
            player.update();
        }
    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        if(gameState == titleState) {

            g2.setColor(Color.white);
            g2.drawString("LEGEND BATTLE ARENA", 300, 250);
            g2.drawString("Press ENTER to Start", 320, 300);
        }

        if(gameState == playState) {

            tileM.draw(g2);

            if(enemy != null) {
                enemy.draw(g2);
            }

            player.draw(g2);

            g2.setColor(Color.white);
            g2.drawString("Wave: " + currentWave, 20, 20);
            g2.drawString("Gold: " + gold, 20, 40);
            g2.drawString("Items: " + inventory.size(), 20, 60);
            g2.drawString(message, 20, 85);
        }

        g2.dispose();
    }
}