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
public class Archer extends Character {

    public Archer(GamePanel gp) {
          super(gp, "Archer", 90, 22, 4);

            this.maxMana = 40;
            this.mana = 40;
        
        getCharacterImage("/resources/player/sprites_display/archer_sc.png");
        getArcherSprite();
    }

    public void getArcherSprite(){
        try{
            up1 = ImageIO.read(getClass().getResourceAsStream("/resources/player/sprites_action/archer_up_1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/resources/player/sprites_action/archer_up_2.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/resources/player/sprites_action/archer_down_1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/resources/player/sprites_action/archer_down_2.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/resources/player/sprites_action/archer_left_1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/resources/player/sprites_action/archer_left_2.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/resources/player/sprites_action/archer_right_1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/resources/player/sprites_action/archer_right_2.png"));
            
            profileSprite = ImageIO.read(getClass().getResourceAsStream("/resources/player/sprites_display/archer_sc.png"));
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
