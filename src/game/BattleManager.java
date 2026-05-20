package game;

import data.EmptyInventoryException;
import entity.Character;
import entity.Enemy;
import object.Item;

import javax.swing.JOptionPane;
import java.util.Random;

/**
 * @author nicol
 */
public class BattleManager {

    GamePanel gp;
    Random random = new Random();
    
    public Enemy activeEnemy;
    
    public BattleManager(GamePanel gp) {
        this.gp = gp;
    }
    
    private void clearMovement() {
        gp.keyH.upPressed = false;
        gp.keyH.downPressed = false;
        gp.keyH.rightPressed = false;
        gp.keyH.leftPressed = false;
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
        if (activeEnemy == null || activeEnemy.hp <= 0) {
            return;
        }
        if(gp.party.isEmpty()){
            return;
        }
        if(gp.currentHeroIndex >= gp.party.size()){
            gp.currentHeroIndex = 0;
        }
        
        // Grab the current active hero from GamePanel's variable
        Character currentHero = gp.party.get(gp.currentHeroIndex);
        
        //skip if this hero is dead
        if(!currentHero.isAlive()){
            advanceTurn();
            return;
        }
        gp.turnsTaken++;

        // Process choices using KeyHandler state selections (commandNum)
        switch (gp.ui.commandNum) {
            case 0: // ATTACK
                basicAttack(currentHero, activeEnemy);
                break;
            case 1: // SKILL
                int skillDmg = currentHero.useSkill();
                activeEnemy.hp = Math.max(0, activeEnemy.hp - skillDmg);
                gp.message = currentHero.getName() + " used a skill for " + skillDmg + " damage!";
                break;
            case 2: // ITEM
                try {
                    boolean used = useItem(currentHero);
                    if(!used){
                        return;
                    }
                } catch (EmptyInventoryException e) {
                    gp.message = e.getMessage();
                    return;
                }
                break;
            case 3: // FLEE
                if (gp.currentWave == 6) {
                    gp.message = "You cannot flee from the final boss!";
                    JOptionPane.showMessageDialog(null, "You cannot flee from the Dragon Lord!");
                    return;
                } else {
                    gp.message = "You fled from battle.";
                    gp.player.worldX = gp.tileSize * 12;
                    gp.player.worldY = gp.tileSize * 10;
                    clearMovement();
                    activeEnemy = null;
                    
                    gp.stopMusic();
                    gp.playMusic(2);
                    gp.gameState = gp.playState;
                    return;
                }
        }
        advanceTurn();
    }

    private void advanceTurn() {
        if(activeEnemy == null){
            return;
        }
        // 1. Check if Enemy has been defeated first
        if (activeEnemy.hp <= 0) {
            activeEnemy.hp = 0;
            handleEnemyDefeated();
            return;
        }

        // 2. If Enemy is alive, increment index to next party member
        gp.currentHeroIndex++;
        
        // 3. Skip anyone who is knocked out
        skipDeadHeroes();
        
        // 4. If index went past the last party member, it's the enemy's turn
        if (gp.currentHeroIndex >= gp.party.size()) {
                enemyTurn(activeEnemy);

                if(activeEnemy.hp <= 0){
                    handleEnemyDefeated();
                    return;
                }
                
                gp.currentHeroIndex = 0;
                skipDeadHeroes();

                if(allHeroesDead()){
                    handlePartyDefeat();
                }
        }
    }

    private void skipDeadHeroes() {
        int attempts = 0;
        // Cycle the index forward if the current hero index sits on a dead hero
        while (attempts < gp.party.size()) {
            if(gp.currentHeroIndex >= gp.party.size()){
                gp.currentHeroIndex = 0;
            }
   
            if (gp.party.get(gp.currentHeroIndex).isAlive()) {
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

        gp.message = activeEnemy.name + " defeated! You earned " + reward + " gold.";
        JOptionPane.showMessageDialog(null, activeEnemy.name + " defeated!\n\n+" + reward + " Gold earned!");

        // FINAL WAVE SCENARIO
        if (gp.currentWave == 6) {
            gp.enemy = null;
            activeEnemy = null;
            gp.message = "Victory! All waves cleared.";

            String[] endOptions = {"Start Over", "Exit Game"};
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

            if (endChoice == 0) {
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

        // PROGRESS TO NEXT WAVE
        gp.currentWave++;
        int shopChoice = JOptionPane.showConfirmDialog(
                null,
                "Wave cleared!\nGold: " + gp.gold + "\nDo you want to open the shop?",
                "Shop",
                JOptionPane.YES_NO_OPTION
        );

        if (shopChoice == JOptionPane.YES_OPTION) {
            gp.openShop();
        }

        gp.player.worldX = gp.tileSize * 17;
        gp.player.worldY = gp.tileSize * 24;
        clearMovement();
        
        gp.enemy = null;
        activeEnemy = null;
        
        //re-link the current player state reference securely
        if(!gp.party.isEmpty()){
            gp.player = gp.party.get(0);
        }
        
        gp.stopMusic();
        gp.playMusic(2);
        gp.gameState = gp.playState; // Revert game state loop back to exploratory/play mode
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
        gp.player.worldX = gp.tileSize * 45;
        gp.player.worldY = gp.tileSize * 26;

        clearMovement();
        
        gp.stopMusic();
        gp.playMusic(2);
        gp.gameState = gp.playState; // Break back out to game panel rendering loop
    }

    public void basicAttack(Character hero, Enemy enemy) {
        int chance = random.nextInt(100) + 1;

        if (chance <= 15) {
            gp.message = hero.getName() + " attacked but missed!";
        } else {
            int damage = hero.attack();

            if (chance >= 85) {
                damage *= 2;
                gp.message = "CRITICAL HIT! " + hero.getName() + " dealt " + damage + " damage!";
            } else {
                gp.message = hero.getName() + " attacked " + enemy.name + " for " + damage + " damage.";
            }

            enemy.hp = Math.max(0, enemy.hp - damage);
        }
    }

    public boolean useItem(Character hero) throws EmptyInventoryException {
        if (gp.inventory.isEmpty()) {
            throw new EmptyInventoryException("Inventory is empty.");
        }

        int potionCount = 0;
        int manaCount = 0;
        int reviveCount = 0;

        for (Item item : gp.inventory) {
            if (item.getName().equals("Health Potion")) {
                potionCount++;
            } else if (item.getName().equals("Mana Elixir")) {
                manaCount++;
            } else if (item.getName().equals("Revive Scroll")) {
                reviveCount++;
            }
        }

        java.util.ArrayList<String> choicesList = new java.util.ArrayList<>();
        if (potionCount > 0) choicesList.add("Health Potion x" + potionCount);
        if (manaCount > 0) choicesList.add("Mana Elixir x" + manaCount);
        if (reviveCount > 0) choicesList.add("Revive Scroll x" + reviveCount);

        if(choicesList.isEmpty()){
            throw new EmptyInventoryException("No usable items.");
        }
        
        String[] choices = choicesList.toArray(new String[0]);
        int choice = JOptionPane.showOptionDialog(null, "Choose Item", "Inventory", JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE, null, choices, choices[0]);
        
        if(choice < 0){
            gp.message = "Item cancelled";
            return false;
        }
        String selected = choices[choice];
        if(selected.contains("Health Potion")){
            hero.setHp(hero.getHp() + 30);
            gp.message = hero.getName() + " restored 30 HP!";
            removeInventoryItem("Health Potion!");
        }else if(selected.contains("Mana Elixir")){
            gp.message = hero.getName() + " used Mana Elixir!";
            removeInventoryItem("Mana Elixir");
        }else if(selected.contains("Revive scroll")){
            hero.setHp(40);
            gp.message = hero.getName() + " revived with 40 HP!";
            removeInventoryItem("Revive Scroll");
        }
        return true;     
    }

    private void removeInventoryItem(String itemName) {
        for (int i = 0; i < gp.inventory.size(); i++) {
            if (gp.inventory.get(i).getName().equals(itemName)) {
                gp.inventory.remove(i);
                break;
            }
        }
    }

    public void enemyTurn(Enemy enemy) {
        int enemyChoice = random.nextInt(3);

        if (enemyChoice == 0) {
            java.util.List<Character> alive = new java.util.ArrayList<>();
            for(Character c: gp.party){
                if(c.isAlive()){
                    alive.add(c);
                }
            }
            if(alive.isEmpty()){
                return;
            }
            
            Character target = alive.get(random.nextInt(alive.size()));
            target.receiveDamage(enemy.attackPower);
            gp.message = enemy.name + " attacked" + target.getName() + " for " + enemy.attackPower + " damage!";
        }else if(enemyChoice == 1){
            enemy.attackPower += 3;
            gp.message = enemy.name + " powered up! ATK +" + 3;
        }else{
            gp.message = enemy.name + " used Taunt!";
        }
    }

    public boolean allHeroesDead() {
        for (Character hero : gp.party) {
            if (hero.isAlive()) {
                return false;
            }
        }
        return true;
    }
    
//    public void update() {
//        // Leave empty for now, or add real-time turn timers here if needed later
//    }
//
//    // Keep GamePanel's paintComponent hook happy
//    public void draw(java.awt.Graphics2D g2) {
//        // Leave empty because gp.ui.draw() is rendering the screens
//    }
}