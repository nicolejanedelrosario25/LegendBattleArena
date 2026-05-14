/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author nicol
 */
public class Warrior extends Character {

    public Warrior() {
        super("Warrior", 120, 18, 8);
        
        getCharacterImage("/resources/player/sprites_display/warrior_sc.png");
    }

    @Override
    public int attack() {
        return getAttackPower();
    }

    @Override
    public int useSkill() {
        // Power Slash
        return getAttackPower() + 15;
    }

    public String getPassiveTrait() {
        return "Passive: Lifesteal - recovers small HP after attacking.";
    }

    public String getSkillName() {
        return "Power Slash";
    }
}
