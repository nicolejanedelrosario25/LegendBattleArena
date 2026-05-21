package game;

import data.EmptyInventoryException;
import entity.Character;
import entity.Enemy;
import object.Item;

import javax.swing.JOptionPane;
import java.util.Random;

public class BattleManager {

    GamePanel gp;
    Random random = new Random();

    public Enemy activeEnemy;

    private boolean waitingForEnemyTurn = false;
    private long enemyTurnStartTime = 0;
    private final int ENEMY_DELAY = 1200;

    private boolean playerTaunted = false;
    private int tauntTurns = 0;

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

        waitingForEnemyTurn = false;

        gp.message = "Battle started against " + enemy.name + "!";

        gp.stopMusic();
        gp.playMusic(3);

        skipDeadHeroes();
    }

   public void update() {

    // Enemy delayed turn
    if(waitingForEnemyTurn) {

        long currentTime = System.currentTimeMillis();

        if(currentTime - enemyTurnStartTime >= ENEMY_DELAY) {

            enemyTurn(activeEnemy);

            waitingForEnemyTurn = false;
        }

        return;
        }

        // Safety check
        if(gp.party == null || gp.party.isEmpty()) {
            return;
        }

        if(gp.currentHeroIndex >= gp.party.size()) {
            return;
        }

        Character hero = gp.party.get(gp.currentHeroIndex);

        if(hero != null) {

            // Move forward while attacking
            if(hero.attacking) {

                hero.battleX += 12;

                if(hero.battleX >= gp.enemy.battleX - 120) {

                    hero.attacking = false;
                    hero.returning = true;

                    basicAttack(hero, gp.enemy);

                    gp.message = hero.getName() + " attacked!";
                }
            }

            // Move back
            if(hero.returning) {

                hero.battleX -= 12;

                if(hero.battleX <= hero.originalBattleX) {

                    hero.battleX = hero.originalBattleX;
                    hero.returning = false;

                    advanceTurn();
                }
            }
        }
    }

    public void handlePlayerChoice() {

        if(waitingForEnemyTurn) {
            return;
        }

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
            gp.currentHeroIndex++;
            skipDeadHeroes();
            return;
        }

        gp.turnsTaken++;

        if(playerTaunted) {
            gp.ui.commandNum = 0;
            gp.message = "Taunted! You are forced to attack!";
        }

        switch(gp.ui.commandNum) {

            case 0:
                currentHero.attacking = true;
                gp.message = currentHero.getName() + " attacks!";

                return;

            case 1: // SKILL

                if(currentHero.getMana() < 20) {
                    gp.message = currentHero.getName() + " does not have enough mana!";

                    JOptionPane.showMessageDialog(
                            null,
                            currentHero.getName() + " needs 20 mana to use a skill!"
                    );

                    return;
                }

                currentHero.setMana(currentHero.getMana() - 20);

                if(currentHero.getName().equals("Healer")) {

                    for(Character hero : gp.party) {
                        if(hero.isAlive()) {
                            hero.setHp(hero.getHp() + 30);
                        }
                    }

                    gp.message = "Healer restored 30 HP to all allies!";
                }

                else if(currentHero.getName().equals("Tank")) {

                    gp.message = "Tank used Shield! Enemy attack weakened.";

                    activeEnemy.attackPower -= 5;

                    if(activeEnemy.attackPower < 1) {
                        activeEnemy.attackPower = 1;
                    }
                }

                else {

                    int damage = currentHero.useSkill();

                    activeEnemy.hp -= damage;

                    if(activeEnemy.hp < 0) {
                        activeEnemy.hp = 0;
                    }

                    gp.message = currentHero.getName() +
                            " used a skill and dealt " +
                            damage + " damage!";
                }

                break;

            case 2://item
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

                // Cannot flee from final boss
                if(gp.currentWave == 6) {

                    gp.message = "You cannot flee from the final boss!";

                    JOptionPane.showMessageDialog(
                            null,
                            "You cannot flee from the Dragon Lord!"
                    );

                    return;
                }

                // 60% success chance
                int fleeChance = random.nextInt(100) + 1;

                if(fleeChance <= 60) {

                    // SUCCESS
                    gp.message = "You successfully fled from battle!";

                    gp.player.worldX = gp.tileSize * 17;
                    gp.player.worldY = gp.tileSize * 24;

                    clearMovement();

                    gp.stopMusic();
                    gp.playMusic(2);

                    gp.gameState = gp.playState;
                    gp.requestFocusInWindow();

                    return;
                }

                else {

                    // FAILED ESCAPE
                    int damage = activeEnemy.attackPower  + 10;

                    gp.message = "Failed to flee! All heroes took " +
                            damage + " damage!";

                    for(Character hero : gp.party) {

                        if(hero.isAlive()) {

                            hero.receiveDamage(damage);
                        }
                    }

                    JOptionPane.showMessageDialog(
                            null,
                            "Escape failed!\nAll party members took " +
                            damage + " damage!"
                    );

                    // Check if everyone died
                    if(allHeroesDead()) {

                        handlePartyDefeat();
                        return;
                    }

                    // Enemy gets turn after failed flee
                    waitingForEnemyTurn = true;
                    enemyTurnStartTime = System.currentTimeMillis();
                }
                break;
                
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

            waitingForEnemyTurn = true;
            enemyTurnStartTime = System.currentTimeMillis();

            gp.message = "Enemy is preparing to move...";
        }
    }

    private void skipDeadHeroes() {

        int attempts = 0;

        while(attempts < gp.party.size()) {

            if(gp.currentHeroIndex >= gp.party.size()) {
                break;
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

        if(playerTaunted) {

            if(chance <= 70) {
                gp.message = hero.getName() + " was taunted and missed the attack!";
                playerTaunted = false;
                tauntTurns = 0;
                return;
            }
        }

        if(chance <= 15) {
            gp.message = hero.getName() + " attacked but missed!";
        } else {
            animateAttack(hero, enemy);
             
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

        if(playerTaunted) {
            tauntTurns--;

            if(tauntTurns <= 0) {
                playerTaunted = false;
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

            hero.setMana(hero.getMana() + 40);

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

            int beforeHp = target.getHp();

            target.receiveDamage(enemy.attackPower);

            int actualDamage = beforeHp - target.getHp();

            gp.message = enemy.name +
                    " attacked " +
                    target.getName() +
                    " for " +
                    actualDamage +
                    " damage!";

            if(!target.isAlive()) {
                gp.message += " " + target.getName() + " was defeated!";
            }
        }

        else if(enemyChoice == 1) {
            enemy.attackPower += 3;
            gp.message = enemy.name + " powered up! ATK +3";
        }

        else {
            playerTaunted = true;
            tauntTurns = 1;
            gp.message = enemy.name + " used Taunt!";
        }
  
        gp.currentHeroIndex = 0;
        skipDeadHeroes();

        if(allHeroesDead()) {
            handlePartyDefeat();
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
    public void animateAttack(Character hero, Enemy enemy){

        hero.attacking = true;

        new Thread(() -> {

            // TARGET POSITION
            int targetX = enemy.battleX - 120;

            // MOVE FORWARD
            while(hero.battleX < targetX){

                hero.battleX += 12;

                gp.repaint();
                
                try{
                    Thread.sleep(16);
                }catch(Exception e){

                }
            }

            // IMPACT SHAKE
            gp.screenShakeTimer = 12;

            try{
                Thread.sleep(120);
            }catch(Exception e){

            }

            // RETURN
            while(hero.battleX > hero.originalBattleX){

                hero.battleX -= 12;
                
                gp.repaint();

                try{
                    Thread.sleep(16);
                }catch(Exception e){

                }
            }

            hero.battleX = hero.originalBattleX;
            hero.attacking = false;

        }).start();
    }
}
