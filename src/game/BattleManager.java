/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game;

import data.EmptyInventoryException;
import entity.Character;
import entity.Enemy;
import object.Item;

import javax.swing.JOptionPane;
import java.util.Random;
/**
 *
 * @author nicol
 */
public class BattleManager {

    GamePanel gp;
    Random random = new Random();

    public BattleManager(GamePanel gp) {
        this.gp = gp;
    }

  public void startBattle(Enemy enemy) {

    gp.message = "Battle started against " + enemy.name + "!";

    while(enemy.hp > 0) {

        for(Character hero : gp.party) {

            if(enemy.hp <= 0) break;

            if(!hero.isAlive()) continue;

            gp.turnsTaken++;

            String[] options = {
                "Attack",
                "Skill",
                "Item",
                "Flee"
            };

            int choice = JOptionPane.showOptionDialog(
                    null,
                    hero.getName() +
                    "\nHP: " + hero.getHp() + "/" + hero.getMaxHp() +
                    "\n\nEnemy: " + enemy.name +
                    "\nEnemy HP: " + enemy.hp +
                    "\n\nLast Action: " + gp.message +
                    "\n\nChoose your action:",
                    "Battle",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            // ATTACK
            if(choice == 0) {

                basicAttack(hero, enemy);
            }

            // SKILL
            else if(choice == 1) {

                int damage = hero.useSkill();

                enemy.hp -= damage;

                if(enemy.hp < 0) {
                    enemy.hp = 0;
                }

                gp.message = hero.getName() +
                        " used a skill and dealt " +
                        damage + " damage!";
            }

            // ITEM
            else if(choice == 2) {

                try {
                    useItem(hero);
                } catch (EmptyInventoryException e) {
                    gp.message = e.getMessage();
                }
            }

            // FLEE
            else if(choice == 3) {

                gp.message = "You fled from battle.";

                gp.player.worldX = gp.tileSize * 12;
                gp.player.worldY = gp.tileSize * 10;

                return;
            }

            else {
                gp.message = "Invalid action.";
            }

            // ENEMY DEFEATED
            if(enemy.hp <= 0) {

                enemy.hp = 0;

                gp.enemiesDefeated++;

                int reward = gp.currentWave * 25;

                gp.gold += reward;

                gp.message = enemy.name +
                        " defeated! You earned " +
                        reward + " gold.";

                JOptionPane.showMessageDialog(
                        null,
                        enemy.name + " defeated!\n\n" +
                        "+" + reward + " Gold earned!"
                );

                // FINAL WAVE
                if(gp.currentWave == 6) {

                    gp.enemy = null;

                    gp.message = "Victory! All waves cleared.";

                    String[] endOptions = {
                        "Start Over",
                        "Exit Game"
                    };

                    int endChoice = JOptionPane.showOptionDialog(
                            null,
                            "CONGRATULATIONS!\n\n" +
                            "You defeated the Goblin Lord!\n\n" +
                            "Enemies Defeated: " + gp.enemiesDefeated +
                            "\nTurns Taken: " + gp.turnsTaken +
                            "\nGold Remaining: " + gp.gold +
                            "\n\nWhat do you want to do?",
                            "VICTORY",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.INFORMATION_MESSAGE,
                            null,
                            endOptions,
                            endOptions[0]
                    );

                    // START OVER
                    if(endChoice == 0) {

                        gp.currentWave = 1;
                        gp.gold = 0;
                        gp.enemiesDefeated = 0;
                        gp.turnsTaken = 0;

                        gp.party.clear();
                        gp.inventory.clear();

                        gp.player.worldX = gp.tileSize * 12;
                        gp.player.worldY = gp.tileSize * 10;

                        gp.ui.commandNum = 0;

                        gp.gameState = gp.characterState;

                        gp.message = "New game started. Choose your heroes.";
                    }

                    // EXIT GAME
                    else {
                        System.exit(0);
                    }

                    return;
                }

                // NEXT WAVE
                gp.currentWave++;

                int shopChoice = JOptionPane.showConfirmDialog(
                        null,
                        "Wave cleared!\nGold: " + gp.gold +
                        "\nDo you want to open the shop?",
                        "Shop",
                        JOptionPane.YES_NO_OPTION
                );

                if(shopChoice == JOptionPane.YES_OPTION) {
                    gp.openShop();
                }

                gp.player.worldX = gp.tileSize * 12;
                gp.player.worldY = gp.tileSize * 10;

                gp.startNextWave();

                return;
            }
        }

        // ENEMY TURN
        if(enemy.hp > 0) {
            enemyTurn(enemy);
        }

        // ALL HEROES DEAD
        if(allHeroesDead()) {

            JOptionPane.showMessageDialog(
                    null,
                    "DEFEAT!\n\n" +
                    "Enemies Defeated: " + gp.enemiesDefeated +
                    "\nTurns Taken: " + gp.turnsTaken +
                    "\nGold Remaining: " + gp.gold
            );

            gp.enemy = null;

            gp.message = "Defeat. All heroes were defeated.";

            gp.player.worldX = gp.tileSize * 12;
            gp.player.worldY = gp.tileSize * 10;

            return;
        }
    }
}

    public void basicAttack(Character hero, Enemy enemy) {

        int chance = random.nextInt(100) + 1;

        if(chance <= 15) {

            gp.message = hero.getName() + " attacked but missed!";
        }

        else {

            int damage = hero.attack();

            if(chance >= 85) {

                damage *= 2;

                gp.message = "CRITICAL HIT! " +
                        hero.getName() +
                        " dealt " +
                        damage + " damage!";
            }

            else {

                gp.message = hero.getName() +
                        " attacked " +
                        enemy.name +
                        " for " +
                        damage + " damage.";
            }

            enemy.hp -= damage;

            if(enemy.hp < 0) {
                enemy.hp = 0;
            }
        }
    }

    public void useItem(Character hero) throws EmptyInventoryException {

    if(gp.inventory.isEmpty()) {
        throw new EmptyInventoryException("Inventory is empty.");
    }

    int potionCount = 0;
    int manaCount = 0;
    int reviveCount = 0;

    for(Item item : gp.inventory) {

        if(item.getName().equals("Health Potion")) {
            potionCount++;
        }

        else if(item.getName().equals("Mana Elixir")) {
            manaCount++;
        }

        else if(item.getName().equals("Revive Scroll")) {
            reviveCount++;
        }
    }

    java.util.ArrayList<String> choicesList = new java.util.ArrayList<>();

    if(potionCount > 0) {
        choicesList.add("Health Potion x" + potionCount);
    }

    if(manaCount > 0) {
        choicesList.add("Mana Elixir x" + manaCount);
    }

    if(reviveCount > 0) {
        choicesList.add("Revive Scroll x" + reviveCount);
    }

    String[] choices = choicesList.toArray(new String[0]);

    int choice = JOptionPane.showOptionDialog(
            null,
            "Choose Item",
            "Inventory",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            choices,
            choices[0]
    );

    if(choice < 0) {
        gp.message = "Item cancelled.";
        return;
    }

    String selected = choices[choice];

    if(selected.contains("Health Potion")) {

        hero.setHp(hero.getHp() + 30);

        gp.message = hero.getName() +
                " used Health Potion and restored 30 HP.";

        for(int i = 0; i < gp.inventory.size(); i++) {

            if(gp.inventory.get(i).getName().equals("Health Potion")) {
                gp.inventory.remove(i);
                break;
            }
        }
    }

    else if(selected.contains("Mana Elixir")) {

        gp.message = hero.getName() +
                " used Mana Elixir.";

        for(int i = 0; i < gp.inventory.size(); i++) {

            if(gp.inventory.get(i).getName().equals("Mana Elixir")) {
                gp.inventory.remove(i);
                break;
            }
        }
    }

    else if(selected.contains("Revive Scroll")) {

        hero.setHp(40);

        gp.message = hero.getName() +
                " used Revive Scroll and revived with 40 HP.";

        for(int i = 0; i < gp.inventory.size(); i++) {

            if(gp.inventory.get(i).getName().equals("Revive Scroll")) {
                gp.inventory.remove(i);
                break;
            }
        }
    }
}

    public void enemyTurn(Enemy enemy) {

        int enemyChoice = random.nextInt(3);

        if(enemyChoice == 0) {

            Character target = gp.party.get(random.nextInt(gp.party.size()));

            while(!target.isAlive()) {
                target = gp.party.get(random.nextInt(gp.party.size()));
            }

            int enemyDamage = enemy.attackPower;

            target.receiveDamage(enemyDamage);

            gp.message = enemy.name +
                    " attacked " +
                    target.getName() +
                    " for " +
                    enemyDamage +
                    " damage.";
        }

        else if(enemyChoice == 1) {

            enemy.attackPower += 3;

            gp.message = enemy.name +
                    " used Buff. Attack increased.";
        }

        else {

            gp.message = enemy.name +
                    " used Taunt. Your heroes feel pressured.";
        }
    }

    public boolean allHeroesDead() {

        for(Character hero : gp.party) {

            if(hero.isAlive()) {
                return false;
            }
        }

        return true;
    }
}