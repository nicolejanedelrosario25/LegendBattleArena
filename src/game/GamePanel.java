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
import ui.UI;
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

    public KeyHandler keyH = new KeyHandler(this);

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

    public final int shopState = 4;
    
    public String message = "Explore the map and touch the enemy to battle.";

    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public int characterState = 2;
    public int battleState = 3;
    
    public UI ui = new UI(this);

    private long lastHeroPickTime = 0;
    public long lastStateChangeTime = 0;

    public int currentHeroIndex = 0;
    public entity.Character player;
    public MouseHandler mouseH = new MouseHandler(this);
    public Sound sound = new Sound();

    private int currentMusicIndex = -1;
    
    public GamePanel() {

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);

        this.addMouseListener(mouseH);
        this.addMouseMotionListener(mouseH);
        this.addKeyListener(keyH);
        this.setFocusable(true);

        gameState = titleState;
    }

   public void chooseHeroes() {
       
    long currentTime = System.currentTimeMillis();

    if(currentTime - lastStateChangeTime < 250){
        return;
    }

    if(party.size() >= 3) {
        return;
    }

    switch(ui.commandNum) {

        case 0:
            party.add(new Warrior(this));
            break;

        case 1:
            party.add(new Mage(this));
            break;

        case 2:
            party.add(new Tank(this));
            break;

        case 3:
            party.add(new Healer(this));
            break;

        case 4:
            party.add(new Archer(this));
            break;
    }

    lastStateChangeTime = currentTime;
    System.out.println("Hero Added! Party size: " + party.size());
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

            message = "Wave 1 started! Find the enemy.";

            enemy = new Enemy(
                    this,
                    "Slime",
                    tileSize * 18,
                    tileSize * 8,
                    120,
                    12
            );
        }

        else if(currentWave == 2) {

            message = "Wave 2 started! Find the enemy.";

            enemy = new Enemy(
                    this,
                    "Goblin",
                    tileSize * 22,
                    tileSize * 18,
                    130,
                    18
            );
        }

        else if(currentWave == 3) {

            message = "Wave 3 started! Find the enemy.";

            enemy = new Enemy(
                    this,
                    "Skeleton",
                    tileSize * 25,
                    tileSize * 42,
                    150,
                    24
            );
        }

        else if(currentWave == 4) {

            message = "Wave 4 started! Find the enemy.";

            enemy = new Enemy(
                    this,
                    "Goblin",
                    tileSize * 26,
                    tileSize * 9,
                    180,
                    30
            );
        }

        else if(currentWave == 5) {

            message = "Wave 5 started! Find the enemy.";

            enemy = new Enemy(
                    this,
                    "Skeleton",
                    tileSize * 6,
                    tileSize * 20,
                    250,
                    38
            );
        }

        else if(currentWave == 6) {

        JOptionPane.showMessageDialog(
                null,
                "WARNING!\n\n" +
                "The Dragon Lord has appeared!\n" +
                "Prepare for the final battle!",
                "FINAL BOSS",
                JOptionPane.WARNING_MESSAGE
        );

        message = "Final Wave! Defeat the Dragon Lord!";

        enemy = new Enemy(
                this,
                "Dragon",
                tileSize * 33,
                tileSize * 18,
                450,
                55
        );
    }

        else {
            enemy = null;
            message = "Victory! All waves cleared.";
        }
    }
    
    public void setUpGame(){
        if(!party.isEmpty()){
            player = party.get(0);
        }
    }

  public void openShop() {

    String[] choices = {
        "Health Potion - 20G",
        "Mana Elixir - 25G",
        "Revive Scroll - 40G",
        "Exit"
    };

    boolean shopping = true;

    while(shopping) {

        int choice = JOptionPane.showOptionDialog(
                null,
                "SHOP\n" +
                "Gold: " + gold +
                "\nItems: " + inventory.size(),
                "Shop",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
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

    repaint();
}
    public void startGameThread() {
        playMusic(0);
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

        if(gameState == playState && player != null) {
            player.update();

            if(enemy != null) {
                enemy.update();
            }
        }
        else if(gameState == battleState){
//            battleManager.update();
        }
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        if(gameState == titleState) {
            ui.draw(g2);
        }
        else if(gameState == characterState) {
            ui.draw(g2);
        }        
        else if(gameState == playState) {

            tileM.draw(g2);
            
            if(player != null){
                player.draw(g2);
            }
            if(enemy != null) {
                enemy.draw(g2);
            }

            ui.draw(g2);

            g2.setColor(Color.white);
            g2.drawString("Wave: " + currentWave, 20, 485);
            g2.drawString("Gold: " + gold, 20, 505);
            g2.drawString("Items: " + inventory.size(), 20, 525);
            g2.drawString(message, 20, 550);
        }
        else if(gameState == battleState){
            ui.draw(g2);
        }
             
        g2.dispose();
    }
    
    public void playMusic(int i){
        if(i == currentMusicIndex){
            return;
        }
        sound.stop();
        currentMusicIndex = i;
        sound.setFile(i);
        sound.play();
        sound.loop();
    }
    
    public void stopMusic(){
        if(currentMusicIndex == -1){
            return;
        }
        sound.stop();
        currentMusicIndex = -1;
    }
    
    public void playSE(int i){
        sound.playSE(i);
    }
}