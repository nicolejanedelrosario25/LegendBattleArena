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
public class Mage extends Character {

    public Mage(GamePanel gp) {
        super(gp, "Mage", 80, 25, 3);
        
        getCharacterImage("/resources/player/sprites_display/mage_sc.png");
        
        getMageSprite();
    }

     public void getMageSprite(){
        try{
            up1 = ImageIO.read(getClass().getResourceAsStream("/resources/player/sprites_action/mage_up_1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/resources/player/sprites_action/mage_up_2.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/resources/player/sprites_action/mage_down_1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/resources/player/sprites_action/mage_down_2.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/resources/player/sprites_action/mage_left_1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/resources/player/sprites_action/mage_left_2.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/resources/player/sprites_action/mage_right_1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/resources/player/sprites_action/mage_right_2.png"));
            
            profileSprite = ImageIO.read(getClass().getResourceAsStream("/resources/player/sprites_display/mage_sc.png"));
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
