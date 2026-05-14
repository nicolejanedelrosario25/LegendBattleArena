/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author nicol
 */
public abstract class Character {
    private String name;
    private int hp;
    private int maxHp;
    private int attackPower;
    private int defensePower;
    public BufferedImage profileSprite;

    public Character(String name, int maxHp, int attackPower, int defensePower) {
        this.name = name;
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.attackPower = attackPower;
        this.defensePower = defensePower;
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
        int finalDamage = damage - defensePower;

        if(finalDamage < 1) {
            finalDamage = 1;
        }

        setHp(hp - finalDamage);
    }

    public abstract int attack();

    public abstract int useSkill();
}
