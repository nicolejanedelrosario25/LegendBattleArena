/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import game.GamePanel;
import java.awt.Color;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author nicol
 */
public class Enemy extends Entity {

    GamePanel gp;

    public String name;
    public int hp;
    public int maxHp;
    public int attackPower;
    
    public int battleX;
    public int battleY;

    public BufferedImage image1, image2;
    public Image gifImage;

    int spriteCounter = 0;
    public int spriteNum = 1;
    int actionLockCounter = 0;

    public Enemy(GamePanel gp, String name, int worldX, int worldY, int hp, int attackPower) {

        this.gp = gp;
        this.name = name;
        this.worldX = worldX;
        this.worldY = worldY;
        this.hp = hp;
        this.maxHp = hp;
        this.attackPower = attackPower;

        getEnemyImage();
    }

    public void getEnemyImage() {

        try {
            
            switch(name){

                case "Slime":
                image1 = ImageIO.read(getClass().getResourceAsStream("/resources/enemy/slime_1.png"));
                image2 = ImageIO.read(getClass().getResourceAsStream("/resources/enemy/slime_2.png"));
                break;

                case "Goblin":
                    image1 = ImageIO.read(getClass().getResourceAsStream("/resources/enemy/goblin_idle_1.png"));
                    image2 = ImageIO.read(getClass().getResourceAsStream("/resources/enemy/goblin_idle_2.png"));
                    break;

                case "Skeleton":
                    image1 = ImageIO.read(getClass().getResourceAsStream("/resources/enemy/skeleton_idle_1.png"));
                    image2 = ImageIO.read(getClass().getResourceAsStream("/resources/enemy/skeleton_idle_2.png"));
                    break;

                case "Orc":
                    gifImage = new ImageIcon(
                        getClass().getResource("/resources/enemy/orc_idle.gif")
                    ).getImage();
                    break;

                case "Necromancer":
                    gifImage = new ImageIcon(
                        getClass().getResource("/resources/enemy/necromancer_idle.gif")
                    ).getImage();
                    break;

                case "Dragon":
                    gifImage = new ImageIcon(
                        getClass().getResource("/resources/enemy/dragon_idle.gif")
                    ).getImage();
                    break;

                default:
                    image1 = ImageIO.read(getClass().getResourceAsStream("/resources/enemy/slime_1.png"));
                    image2 = image1;
                    break;
            }

        } catch(Exception e) {
            System.out.println("Enemy image not found for: " + name);
            e.printStackTrace();
        }
    }

    public void update() {

        spriteCounter++;

        if(spriteCounter > 20) {
            spriteNum = (spriteNum == 1) ? 2 : 1;
            spriteCounter = 0;
        }

        actionLockCounter++;

        if(actionLockCounter > 90) {

            int random = (int)(Math.random() * 4);

            int nextX = worldX;
            int nextY = worldY;

            if(random == 0) {
                nextY -= 8;
            } else if(random == 1) {
                nextY += 8;
            } else if(random == 2) {
                nextX -= 8;
            } else if(random == 3) {
                nextX += 8;
            }

            if(nextX > 48 &&
               nextY > 48 &&
               nextX < gp.worldWidth - 96 &&
               nextY < gp.worldHeight - 96) {

                worldX = nextX;
                worldY = nextY;
            }

            actionLockCounter = 0;
        }
    }

    public void draw(Graphics2D g2) {

        if (gp.player == null){
            return;
        }
        
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        int size = 96;

        if(name.equals("Dragon")) {
            size = 260;
        }
//
//        if(name.equals("Dragon") && gifImage != null) {
//            g2.drawImage(gifImage, screenX, screenY, size, size, null);
//        } else {
//            BufferedImage image = (spriteNum == 1) ? image1 : image2;
//            if(image != null){
//                g2.drawImage(image, screenX, screenY, size, size, null);
//            }
//        }
        if(gifImage != null){
            g2.drawImage(gifImage, screenX, screenY, size, size, null);
        }else{
            BufferedImage image = (spriteNum == 1) ? image1 : image2;
            
            if(image != null){
                g2.drawImage(image, screenX, screenY, size, size, null);
            }
        }
        
        //setup color properties for tag string and overlay UI bars
        g2.setColor(Color.WHITE);
        g2.drawString(name, screenX + 15, screenY - 10);
        
        //draw health bar backing box
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(screenX, screenY - 22, size, 6);
        
        int barWidth = 0;
        if(maxHp > 0 && hp > 0){
            barWidth = Math.max(0, (hp*size)/maxHp);
        }

        
        g2.setColor(Color.RED);
        g2.fillRect(screenX, screenY - 22, barWidth, 6);
    }
}