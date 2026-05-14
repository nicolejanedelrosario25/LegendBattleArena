/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author nicol
 */
public class Archer extends Character {

    public Archer() {
        super("Archer", 90, 20, 4);
        
        getCharacterImage("/resources/player/sprites_display/archer_sc.png");
    }

    @Override
    public int attack() {
        return getAttackPower();
    }

    @Override
    public int useSkill() {
        // Rapid Shot
        return getAttackPower() + 18;
    }

    public String getPassiveTrait() {
        return "Passive: Eagle Eye - higher critical hit chance.";
    }

    public String getSkillName() {
        return "Rapid Shot";
    }
}
