/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author nicol
 */
public class Healer extends Character {

    public Healer() {
        super("Healer", 100, 12, 5);
    }

    @Override
    public int attack() {
        return getAttackPower();
    }

    @Override
    public int useSkill() {
        // Holy Heal
        return 25;
    }

    public String getPassiveTrait() {
        return "Passive: Blessing - healing skills restore extra HP.";
    }

    public String getSkillName() {
        return "Holy Heal";
    }
}
