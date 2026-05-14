/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import game.GamePanel;

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

    BufferedImage image1, image2;
    Image gifImage;

    int spriteCounter = 0;
    int spriteNum = 1;
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
            if(name.equals("Slime")) {
                image1 = ImageIO.read(getClass().getResourceAsStream("/resources/enemy/slime_1.png"));
                image2 = ImageIO.read(getClass().getResourceAsStream("/resources/enemy/slime_2.png"));

            } else if(name.equals("Goblin")) {
                image1 = ImageIO.read(getClass().getResourceAsStream("/resources/enemy/gobline_idle_1.png"));
                image2 = ImageIO.read(getClass().getResourceAsStream("/resources/enemy/goblin_idle_2.png"));

            } else if(name.equals("Skeleton")) {
                image1 = ImageIO.read(getClass().getResourceAsStream("/resources/enemy/skeleton_idle_1.png"));
                image2 = ImageIO.read(getClass().getResourceAsStream("/resources/enemy/skeleton_idle_2.png"));

            } else if(name.equals("Dragon")) {
                ImageIcon icon = new ImageIcon(
                        getClass().getResource("/resources/enemy/dragon_idle.gif")
                );

                gifImage = icon.getImage();
                image1 = null;
                image2 = null;

            } else {
                image1 = ImageIO.read(getClass().getResourceAsStream("/resources/enemy/slime_1.png"));
                image2 = image1;
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

        if(actionLockCounter > 25) {

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

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        int size = 96;

        if(name.equals("Dragon")) {
            size = 180;
        }

        if(name.equals("Dragon") && gifImage != null) {
            g2.drawImage(gifImage, screenX, screenY, size, size, null);
        } else {
            BufferedImage image = (spriteNum == 1) ? image1 : image2;
            g2.drawImage(image, screenX, screenY, size, size, null);
        }

        g2.drawString(name, screenX + 15, screenY - 10);

        g2.fillRect(screenX, screenY - 20, hp * size / maxHp, 10);
    }
}