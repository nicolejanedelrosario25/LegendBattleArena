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
public class Enemy extends Entity {
    
    public String name;
    public int hp;
    public int maxHp;
    public int attackPower;

    BufferedImage image;
    GamePanel gp;
    
    public Enemy(GamePanel gp, String name, int x, int y, int hp, int attackPower) {
        this.gp = gp;
        this.name = name;
        this.worldX = x;
        this.worldY = y;
        this.hp = hp;
        this.maxHp = hp;
        this.attackPower = attackPower;
        this.speed = 0;
        this.direction = "down";

        getEnemyImage();
    }

    public void getEnemyImage() {

    String imagePath = "/resources/enemy/slime.png";

    if(name.equals("Slime")) {
        imagePath = "/resources/enemy/slime_1.png";

    } else if(name.equals("Goblin")) {
        imagePath = "/resources/enemy/gobline_idle_1.png";

    } else if(name.equals("Skeleton")) {
        imagePath = "/resources/enemy/skeleton_idle_1.png";
    }

    try {

        image = ImageIO.read(
                getClass().getResourceAsStream(imagePath));

    } catch(IOException e) {
        e.printStackTrace();
    }
}

    public void draw(Graphics2D g2) {
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;
        
        if(image != null) {
            g2.drawImage(image, screenX, screenY, 48, 48, null);
        } else {
            g2.setColor(Color.red);
            g2.fillOval(screenX, screenY, 40, 40);
        }

        g2.setColor(Color.white);
        g2.drawString(name, screenX - 5, screenY - 15);

        g2.setColor(Color.black);
        g2.fillRect(screenX, screenY - 10, 48, 6);

        g2.setColor(Color.red);

        int hpBarWidth = hp * 48 / maxHp;

        if(hpBarWidth < 0) {
            hpBarWidth = 0;
        }

        g2.fillRect(screenX, screenY - 10, hpBarWidth, 6);
    }
}