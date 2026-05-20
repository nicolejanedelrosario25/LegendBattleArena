/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import game.GamePanel;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author nicol
 */
public class Warrior extends Character {

    public Warrior(GamePanel gp) {
        super(gp, "Warrior", 120, 18, 8);
        
        getCharacterImage("/resources/player/sprites_display/warrior_sc.png");
        
        getWarriorSprite();
    }

    public void getWarriorSprite(){
        try{
            up1 = ImageIO.read(getClass().getResourceAsStream("/resources/player/sprites_action/warrior_up_1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/resources/player/sprites_action/warrior_up_2.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/resources/player/sprites_action/warrior_down_1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/resources/player/sprites_action/warrior_down_2.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/resources/player/sprites_action/warrior_left_1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/resources/player/sprites_action/warrior_left_2.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/resources/player/sprites_action/warrior_right_1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/resources/player/sprites_action/warrior_right_2.png"));
            
            profileSprite = ImageIO.read(getClass().getResourceAsStream("/resources/player/sprites_display/warrior_sc.png"));
        }catch(IOException e){
            e.printStackTrace();
        }
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
