/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author nicol
 */
public class Tank extends Character {

    public Tank() {
        super("Tank", 160, 10, 15);
        
        getCharacterImage("/resources/player/sprites_display/tank_sc.png");
    }

    @Override
    public int attack() {
        return getAttackPower();
    }

    @Override
    public int useSkill() {
        // Shield Bash
        return getAttackPower() + 10;
    }

    public String getPassiveTrait() {
        return "Passive: Iron Body - receives reduced damage.";
    }

    public String getSkillName() {
        return "Shield Bash";
    }
}