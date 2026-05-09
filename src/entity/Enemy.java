/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

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

    public Enemy(String name, int x, int y, int hp, int attackPower) {

        this.name = name;
        this.x = x;
        this.y = y;
        this.hp = hp;
        this.maxHp = hp;
        this.attackPower = attackPower;
        this.speed = 0;
        this.direction = "down";

        getEnemyImage();
    }

    public void getEnemyImage() {

        try {
            image = ImageIO.read(
                    getClass().getResourceAsStream("/resources/enemy/slime.png")
            );
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {

        if(image != null) {
            g2.drawImage(image, x, y, 48, 48, null);
        } else {
            g2.setColor(Color.red);
            g2.fillOval(x, y, 40, 40);
        }

        // ENEMY NAME
        g2.setColor(Color.white);
        g2.drawString(name, x - 5, y - 15);

        // HP BAR BACKGROUND
        g2.setColor(Color.black);
        g2.fillRect(x, y - 10, 48, 6);

        // HP BAR
        g2.setColor(Color.red);

        int hpBarWidth = hp * 48 / maxHp;

        if(hpBarWidth < 0) {
            hpBarWidth = 0;
        }

        g2.fillRect(x, y - 10, hpBarWidth, 6);
    }
}