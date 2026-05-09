/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import game.GamePanel;
import game.KeyHandler;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;


/**
 *
 * @author nicol
 */
public class Player extends Entity {

    GamePanel gp;
    KeyHandler keyH;

    BufferedImage up, down, left, right;

    public Player(GamePanel gp, KeyHandler keyH) {

        this.gp = gp;
        this.keyH = keyH;

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {

        x = 100;
        y = 100;
        speed = 4;
        direction = "down";
    }

    public void getPlayerImage() {

        try {
            up = ImageIO.read(getClass().getResourceAsStream("/resources/player/up.png"));
            down = ImageIO.read(getClass().getResourceAsStream("/resources/player/down.png"));
            left = ImageIO.read(getClass().getResourceAsStream("/resources/player/left.png"));
            right = ImageIO.read(getClass().getResourceAsStream("/resources/player/right.png"));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {

        int nextX = x;
        int nextY = y;

        if(keyH.upPressed) {
            nextY -= speed;
            direction = "up";
        }

        if(keyH.downPressed) {
            nextY += speed;
            direction = "down";
        }

        if(keyH.leftPressed) {
            nextX -= speed;
            direction = "left";
        }

        if(keyH.rightPressed) {
            nextX += speed;
            direction = "right";
        }

        boolean collision = gp.cChecker.checkTile(this, nextX, nextY);

        if(!collision) {
            x = nextX;
            y = nextY;
        }

        checkEnemyCollision();
    }

    public void checkEnemyCollision() {

        if(gp.enemy == null) return;

        int playerLeft = x;
        int playerRight = x + 40;
        int playerTop = y;
        int playerBottom = y + 40;

        int enemyLeft = gp.enemy.x;
        int enemyRight = gp.enemy.x + 40;
        int enemyTop = gp.enemy.y;
        int enemyBottom = gp.enemy.y + 40;

        boolean touchingEnemy =
                playerRight > enemyLeft &&
                playerLeft < enemyRight &&
                playerBottom > enemyTop &&
                playerTop < enemyBottom;

        if(touchingEnemy) {
            gp.battleManager.startBattle(gp.enemy);

            x = 100;
            y = 100;
        }
    }

    public void draw(Graphics2D g2) {

        BufferedImage image = down;

        if(direction.equals("up")) {
            image = up;
        } else if(direction.equals("down")) {
            image = down;
        } else if(direction.equals("left")) {
            image = left;
        } else if(direction.equals("right")) {
            image = right;
        }

        g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
    }
}