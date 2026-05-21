package game;

import data.EmptyInventoryException;
import entity.Character;
import entity.Enemy;
import object.Item;

import javax.swing.JOptionPane;
import java.lang.reflect.Field;
import java.util.Random;
import java.util.WeakHashMap;

/**
 * @author nicol
 */
public class BattleManager {

    GamePanel gp;
    Random random = new Random();

    public Enemy activeEnemy;

    private WeakHashMap<Character, Integer> manaStore = new WeakHashMap<>();
    private WeakHashMap<Character, Integer> maxManaStore = new WeakHashMap<>();

    public BattleManager(GamePanel gp) {
        this.gp = gp;
    }

    private void clearMovement() {
        gp.keyH.upPressed = false;
        gp.keyH.downPressed = false;
        gp.keyH.leftPressed = false;
        gp.keyH.rightPressed = false;
    }

    public void startBattle(Enemy enemy) {

        this.activeEnemy = enemy;
        gp.enemy = enemy;

        gp.currentHeroIndex = 0;
        gp.ui.commandNum = 0;
        gp.gameState = gp.battleState;

        gp.message = "Battle started against " + enemy.name + "!";

        gp.stopMusic();
        gp.playMusic(3);

        skipDeadHeroes();
    }

    public void handlePlayerChoice() {

        if(activeEnemy == null || activeEnemy.hp <= 0) {
            return;
        }

        if(gp.party.isEmpty()) {
            return;
        }

        if(gp.currentHeroIndex >= gp.party.size()) {
            gp.currentHeroIndex = 0;
        }

        Character currentHero = gp.party.get(gp.currentHeroIndex);

        if(!currentHero.isAlive()) {
            advanceTurn();
            return;
        }

        gp.turnsTaken++;

        switch(gp.ui.commandNum) {

            case 0: // ATTACK
                basicAttack(currentHero, activeEnemy);
                break;

            case 1: // SKILL
                if(getMana(currentHero) >= 20) {

                    setMana(currentHero, getMana(currentHero) - 20);

                    int damage = currentHero.useSkill();

                    activeEnemy.hp -= damage;

                    if(activeEnemy.hp < 0) {
                        activeEnemy.hp = 0;
                    }

                    gp.message = currentHero.getName() +
                            " used a skill and dealt " +
                            damage + " damage!";

                } else {
                    gp.message = currentHero.getName() + " does not have enough mana!";

                    JOptionPane.showMessageDialog(
                            null,
                            currentHero.getName() + " needs 20 mana to use a skill!"
                    );

                    return;
                }

                break;

            case 2: // ITEM
                try {
                    boolean used = useItem(currentHero);

                    if(!used) {
                        return;
                    }

                } catch(EmptyInventoryException e) {
                    gp.message = e.getMessage();
                    return;
                }

                break;

            case 3: // FLEE

            if(gp.currentWave == 6) {
                gp.message = "You cannot flee from the final boss!";
                JOptionPane.showMessageDialog(null, "You cannot flee from the Dragon Lord!");
                return;
            }

            gp.message = "You fled from battle.";

            gp.player.worldX = gp.tileSize * 17;
            gp.player.worldY = gp.tileSize * 24;

            clearMovement();

            gp.stopMusic();
            gp.playMusic(2);

            gp.gameState = gp.playState;

            gp.requestFocusInWindow();

            return;
                }

        advanceTurn();
    }

    private void advanceTurn() {

        if(activeEnemy == null) {
            return;
        }

        if(activeEnemy.hp <= 0) {
            activeEnemy.hp = 0;
            handleEnemyDefeated();
            return;
        }

        gp.currentHeroIndex++;

        skipDeadHeroes();

        if(gp.currentHeroIndex >= gp.party.size()) {

            enemyTurn(activeEnemy);

            if(activeEnemy.hp <= 0) {
                activeEnemy.hp = 0;
                handleEnemyDefeated();
                return;
            }

            gp.currentHeroIndex = 0;
            skipDeadHeroes();

            if(allHeroesDead()) {
                handlePartyDefeat();
            }
        }
    }

    private void skipDeadHeroes() {

        int attempts = 0;

        while(attempts < gp.party.size()) {

            if(gp.currentHeroIndex >= gp.party.size()) {
                gp.currentHeroIndex = 0;
            }

            if(gp.party.get(gp.currentHeroIndex).isAlive()) {
                break;
            }

            gp.currentHeroIndex++;
            attempts++;
        }
    }

    private void handleEnemyDefeated() {

        gp.enemiesDefeated++;

        int reward = gp.currentWave * 25;
        gp.gold += reward;

        gp.message = activeEnemy.name +
                " defeated! You earned " +
                reward + " gold.";

        JOptionPane.showMessageDialog(
                null,
                activeEnemy.name + " defeated!\n\n+" + reward + " Gold earned!"
        );

        if(gp.currentWave == 6) {

            gp.enemy = null;
            activeEnemy = null;

            gp.message = "Victory! All waves cleared.";

            String[] endOptions = {
                "Start Over",
                "Exit Game"
            };

            int endChoice = JOptionPane.showOptionDialog(
                    null,
                    "CONGRATULATIONS!\n\n" +
                    "You defeated the Dragon Lord!\n\n" +
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

            if(endChoice == 0) {

                clearMovement();

                gp.currentWave = 1;
                gp.gold = 0;
                gp.enemiesDefeated = 0;
                gp.turnsTaken = 0;

                gp.party.clear();
                gp.inventory.clear();

                gp.player.worldX = gp.tileSize * 23;
                gp.player.worldY = gp.tileSize * 21;

                gp.ui.commandNum = 0;

                gp.stopMusic();
                gp.playMusic(1);

                gp.gameState = gp.characterState;

            } else {
                System.exit(0);
            }

            return;
        }

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

        gp.player.worldX = gp.tileSize * 17;
        gp.player.worldY = gp.tileSize * 24;

        clearMovement();

        gp.enemy = null;
        activeEnemy = null;

        gp.stopMusic();
        gp.playMusic(2);

        gp.gameState = gp.playState;

        gp.startNextWave();
    }

    private void handlePartyDefeat() {

        JOptionPane.showMessageDialog(
                null,
                "DEFEAT!\n\n" +
                "Enemies Defeated: " + gp.enemiesDefeated +
                "\nTurns Taken: " + gp.turnsTaken +
                "\nGold Remaining: " + gp.gold
        );

        gp.enemy = null;
        activeEnemy = null;

        gp.message = "Defeat. All heroes were defeated.";

        gp.player.worldX = gp.tileSize * 17;
        gp.player.worldY = gp.tileSize * 24;

        clearMovement();

        gp.stopMusic();
        gp.playMusic(2);

        gp.gameState = gp.playState;
    }

    public void basicAttack(Character hero, Enemy enemy) {

        int chance = random.nextInt(100) + 1;

        if(chance <= 15) {
            gp.message = hero.getName() + " attacked but missed!";
        } else {

            int damage = hero.attack();

            if(chance >= 85) {
                damage *= 2;

                gp.message = "CRITICAL HIT! " +
                        hero.getName() +
                        " dealt " +
                        damage + " damage!";
            } else {
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

    public boolean useItem(Character hero) throws EmptyInventoryException {

        if(gp.inventory.isEmpty()) {
            throw new EmptyInventoryException("Inventory is empty.");
        }

        int potionCount = 0;
        int manaCount = 0;
        int reviveCount = 0;

        for(Item item : gp.inventory) {

            if(item.getName().equals("Health Potion")) {
                potionCount++;
            } else if(item.getName().equals("Mana Elixir")) {
                manaCount++;
            } else if(item.getName().equals("Revive Scroll")) {
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

        if(choicesList.isEmpty()) {
            throw new EmptyInventoryException("No usable items.");
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
            return false;
        }

        String selected = choices[choice];

        if(selected.contains("Health Potion")) {

            hero.setHp(hero.getHp() + 30);

            gp.message = hero.getName() + " restored 30 HP!";

            removeInventoryItem("Health Potion");
        }

        else if(selected.contains("Mana Elixir")) {

            setMana(hero, getMana(hero) + 40);

            if(getMana(hero) > getMaxMana(hero)) {
                setMana(hero, getMaxMana(hero));
            }

            gp.message = hero.getName() + " restored 40 Mana!";

            removeInventoryItem("Mana Elixir");
        }

        else if(selected.contains("Revive Scroll")) {

            hero.setHp(40);

            gp.message = hero.getName() + " revived with 40 HP!";

            removeInventoryItem("Revive Scroll");
        }

        return true;
    }

    private void removeInventoryItem(String itemName) {

        for(int i = 0; i < gp.inventory.size(); i++) {

            if(gp.inventory.get(i).getName().equals(itemName)) {
                gp.inventory.remove(i);
                break;
            }
        }
    }

    public void enemyTurn(Enemy enemy) {

        int enemyChoice = random.nextInt(3);

        if(enemyChoice == 0) {

            java.util.List<Character> alive = new java.util.ArrayList<>();

            for(Character c : gp.party) {
                if(c.isAlive()) {
                    alive.add(c);
                }
            }

            if(alive.isEmpty()) {
                return;
            }

            Character target = alive.get(random.nextInt(alive.size()));

            target.receiveDamage(enemy.attackPower);

            gp.message = enemy.name +
                    " attacked " +
                    target.getName() +
                    " for " +
                    enemy.attackPower +
                    " damage!";
        }

        else if(enemyChoice == 1) {
            enemy.attackPower += 3;
            gp.message = enemy.name + " powered up! ATK +3";
        }

        else {
            gp.message = enemy.name + " used Taunt!";
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

    private int getMana(Character hero) {

        try {
            Field field = hero.getClass().getSuperclass().getDeclaredField("mana");
            field.setAccessible(true);
            return field.getInt(hero);
        } catch(Exception e) {
            if(!manaStore.containsKey(hero)) {
                manaStore.put(hero, 100);
            }

            return manaStore.get(hero);
        }
    }

    private void setMana(Character hero, int value) {

        try {
            Field field = hero.getClass().getSuperclass().getDeclaredField("mana");
            field.setAccessible(true);
            field.setInt(hero, value);
        } catch(Exception e) {
            manaStore.put(hero, value);
        }
    }

    private int getMaxMana(Character hero) {

        try {
            Field field = hero.getClass().getSuperclass().getDeclaredField("maxMana");
            field.setAccessible(true);
            return field.getInt(hero);
        } catch(Exception e) {
            if(!maxManaStore.containsKey(hero)) {
                maxManaStore.put(hero, 100);
            }

            return maxManaStore.get(hero);
        }
    }
}