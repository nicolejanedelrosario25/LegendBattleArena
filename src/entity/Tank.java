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
public class Tank extends Character {

    public Tank(GamePanel gp) {
        super(gp, "Tank", 160, 10, 15);
        
        getCharacterImage("/resources/player/sprites_display/tank_sc.png");
        
        getTankSprite();
    }
    
        public void getTankSprite(){
        try{
            up1 = ImageIO.read(getClass().getResourceAsStream("/resources/player/sprites_action/tank_up_1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/resources/player/sprites_action/tank_up_2.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/resources/player/sprites_action/tank_down_1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/resources/player/sprites_action/tank_down_2.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/resources/player/sprites_action/tank_left_1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/resources/player/sprites_action/tank_left_2.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/resources/player/sprites_action/tank_right_1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/resources/player/sprites_action/tank_right_2.png"));
            
            profileSprite = ImageIO.read(getClass().getResourceAsStream("/resources/player/sprites_display/tank_sc.png"));
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