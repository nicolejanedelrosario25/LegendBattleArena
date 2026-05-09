/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author nicol
 */
public class Mage extends Character {

    public Mage() {
        super("Mage", 80, 25, 3);
    }

    @Override
    public int attack() {
        return getAttackPower();
    }

    @Override
    public int useSkill() {
        // Fireball
        return getAttackPower() + 25;
    }

    public String getPassiveTrait() {
        return "Passive: Magic Boost - skills deal extra damage.";
    }

    public String getSkillName() {
        return "Fireball";
    }
}
