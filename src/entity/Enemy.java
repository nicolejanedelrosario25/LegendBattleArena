/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import game.GamePanel;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

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

    int spriteCounter = 0;
    int spriteNum = 1;

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

            } else {

                image1 = ImageIO.read(getClass().getResourceAsStream("/resources/enemy/slime.png"));
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

            if(spriteNum == 1) {
                spriteNum = 2;
            } else {
                spriteNum = 1;
            }

            spriteCounter = 0;
        }
    }

    public void draw(Graphics2D g2) {

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        BufferedImage image;

        if(spriteNum == 1) {
            image = image1;
        } else {
            image = image2;
        }

        g2.drawImage(image, screenX, screenY, 96, 96, null);

        // ENEMY NAME
        g2.drawString(name, screenX + 15, screenY - 10);

        // HP BAR
        g2.fillRect(screenX, screenY - 20, hp * 96 / maxHp, 10);
    }
}