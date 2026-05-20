/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import game.GamePanel;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author nicol
 */
public abstract class Character extends Entity {
    public GamePanel gp;
    
    private String name;
    private int hp, maxHp, attackPower, defensePower;
    
    public BufferedImage up1, up2;
    public BufferedImage down1, down2;
    public BufferedImage left1, left2;
    public BufferedImage right1, right2;

    int spriteCounter = 0;
    int spriteNum = 1;
    
    public int screenX, screenY;
    
    public BufferedImage profileSprite;
    public abstract int attack();
    public abstract int useSkill();

    public Character(GamePanel gp, String name, int maxHp, int attackPower, int defensePower) {
        this.gp = gp;
        this.name = name;
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.attackPower = attackPower;
        this.defensePower = defensePower;
        
        this.screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        this.screenY = gp.screenHeight / 2 - (gp.tileSize / 2);
    }
    
     public void update() {

        int nextX = worldX;
        int nextY = worldY;

        boolean moving = false;

        if(gp.keyH.upPressed) {
            nextY -= speed;
            direction = "up";
            moving = true;
        }

        if(gp.keyH.downPressed) {
            nextY += speed;
            direction = "down";
            moving = true;
        }

        if(gp.keyH.leftPressed) {
            nextX -= speed;
            direction = "left";
            moving = true;
        }

        if(gp.keyH.rightPressed) {
            nextX += speed;
            direction = "right";
            moving = true;
        }

        boolean collision = gp.cChecker.checkTile(this, nextX, nextY);

        if(!collision) {
            worldX = nextX;
            worldY = nextY;
        }

        if(moving) {
            spriteCounter++;

            if(spriteCounter > 10) {
                if(spriteNum == 1) {
                    spriteNum = 2;
                } else {
                    spriteNum = 1;
                }

                spriteCounter = 0;
            }
        }

        checkEnemyCollision();
    }
     
     public void checkEnemyCollision() {

        if(gp.enemy == null) return;
//
//        int playerLeft = worldX;
//        int playerRight = worldX + 40;
//        int playerTop = worldY;
//        int playerBottom = worldY + 40;
//
//        int enemyLeft = gp.enemy.worldX;
//        int enemyRight = gp.enemy.worldX + 40;
//        int enemyTop = gp.enemy.worldY;
//        int enemyBottom = gp.enemy.worldY + 40;
//
////        boolean touchingEnemy =
////                playerRight > enemyLeft &&
////                playerLeft < enemyRight &&
////                playerBottom > enemyTop &&
////                playerTop < enemyBottom;
////
//        if(touchingEnemy) {
          if (this.worldX + solidArea.x < gp.enemy.worldX + gp.tileSize &&
            this.worldX + solidArea.x + solidArea.width > gp.enemy.worldX &&
            this.worldY + solidArea.y < gp.enemy.worldY + gp.tileSize &&
            this.worldY + solidArea.y + solidArea.height > gp.enemy.worldY) {
            
            gp.battleManager.startBattle(gp.enemy);
        }
//            gp.battleManager.startBattle(gp.enemy);
////
//           worldX = gp.tileSize * 24;
//            worldY = gp.tileSize * 12;
//        }
    }
     
      public void draw(Graphics2D g2) {

        BufferedImage image = down1;

        if(direction.equals("up")) {
            image = (spriteNum == 1) ? up1 : up2;
        } else if(direction.equals("down")) {
            image = (spriteNum == 1) ? down1 : down2;
        } else if(direction.equals("left")) {
            image = (spriteNum == 1) ? left1 : left2;
        } else if(direction.equals("right")) {
            image = (spriteNum == 1) ? right1 : right2;
        }
        if(image != null){
            g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
        }
        
        if(gp.gameState == gp.playState){
            int hoverIndex = gp.mouseH.hoveredHeroIndex;
            
            if(hoverIndex != -1 && hoverIndex < gp.party.size()){
                entity.Character hoveredHero = gp.party.get(hoverIndex);
                
                g2.setColor(new Color(255, 255, 255, 100));
                int rectY = 50 + (hoverIndex * 60);
                g2.drawRect(8, rectY - 2, 55, 55);
                
                g2.setFont(g2.getFont().deriveFont(14F));
                g2.setColor(Color.WHITE);
                String text = "Select " +hoveredHero.getName();
                g2.drawString(text, 70, rectY + 25);
            }
        }
    }

    public String getName() {
        return name;
    }
    
    public void getCharacterImage(String imagePath){
        try{
            profileSprite = ImageIO.read(getClass().getResourceAsStream(imagePath));
        }catch(IOException | IllegalArgumentException e){
            System.out.println("Error loading image: " + imagePath);
            e.printStackTrace();
        }
    }
    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public int getDefensePower() {
        return defensePower;
    }

    public void setHp(int hp) {
        if(hp < 0) {
            this.hp = 0;
        } else if(hp > maxHp) {
            this.hp = maxHp;
        } else {
            this.hp = hp;
        }
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public void receiveDamage(int damage) {
        int reduction = (int)(damage * (defensePower / 100.0) * 0.4);
        int finalDamage = damage - defensePower;
        setHp(hp - finalDamage);      
    }
}
 

  
