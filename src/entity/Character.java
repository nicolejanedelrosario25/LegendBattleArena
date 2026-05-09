/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

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
