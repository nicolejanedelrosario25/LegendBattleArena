/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import game.GamePanel;
import game.KeyHandler;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;


/**
 *
 * @author nicol
 */
public class Player extends Entity {

    GamePanel gp;
    KeyHandler keyH;

    BufferedImage up1, up2;
    BufferedImage down1, down2;
    BufferedImage left1, left2;
    BufferedImage right1, right2;

    int spriteCounter = 0;
    int spriteNum = 1;

    public final int screenX;
    public final int screenY;

    public Player(GamePanel gp, KeyHandler keyH) {

        this.gp = gp;
        this.keyH = keyH;

        solidArea = new Rectangle(8, 16, 32, 32);

        screenX = gp.screenWidth / 2 - gp.tileSize / 2;
        screenY = gp.screenHeight / 2 - gp.tileSize / 2;

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {

    worldX = gp.tileSize * 24;
    worldY = gp.tileSize * 12;

    speed = 4;
    direction = "down";
}

    public void getPlayerImage() {

        try {
            up1 = ImageIO.read(getClass().getResourceAsStream("/resources/player/sprites_action/warrior_up_1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/resources/player/sprites_action/warrior_up_2.png"));

            down1 = ImageIO.read(getClass().getResourceAsStream("/resources/player/sprites_action/warrior_down_1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/resources/player/sprites_action/warrior_down_2.png"));

            left1 = ImageIO.read(getClass().getResourceAsStream("/resources/player/sprites_action/warrior_left_1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/resources/player/sprites_action/warrior_left_2.png"));

            right1 = ImageIO.read(getClass().getResourceAsStream("/resources/player/sprites_action/warrior_right_1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/resources/player/sprites_action/warrior_right_2.png"));

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void update() {

        int nextX = worldX;
        int nextY = worldY;

        boolean moving = false;

        if(keyH.upPressed) {
            nextY -= speed;
            direction = "up";
            moving = true;
        }

        if(keyH.downPressed) {
            nextY += speed;
            direction = "down";
            moving = true;
        }

        if(keyH.leftPressed) {
            nextX -= speed;
            direction = "left";
            moving = true;
        }

        if(keyH.rightPressed) {
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

        int playerLeft = worldX;
        int playerRight = worldX + 40;
        int playerTop = worldY;
        int playerBottom = worldY + 40;

        int enemyLeft = gp.enemy.worldX;
        int enemyRight = gp.enemy.worldX + 40;
        int enemyTop = gp.enemy.worldY;
        int enemyBottom = gp.enemy.worldY + 40;

        boolean touchingEnemy =
                playerRight > enemyLeft &&
                playerLeft < enemyRight &&
                playerBottom > enemyTop &&
                playerTop < enemyBottom;

        if(touchingEnemy) {
            gp.battleManager.startBattle(gp.enemy);

           worldX = gp.tileSize * 24;
            worldY = gp.tileSize * 12;
        }
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

        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
    }
}