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
public class Healer extends Character {

    public Healer(GamePanel gp) {
        super(gp, "Healer", 100, 12, 4);

        this.maxMana = 50;
        this.mana = 50;
        
        getCharacterImage("/resources/player/sprites_display/healer_sc.png");
        getHealerSprite();
    }
    
     public void getHealerSprite(){
        try{
            up1 = ImageIO.read(getClass().getResourceAsStream("/resources/player/sprites_action/healer_up_1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/resources/player/sprites_action/healer_up_2.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/resources/player/sprites_action/healer_down_1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/resources/player/sprites_action/healer_down_2.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/resources/player/sprites_action/healer_left_1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/resources/player/sprites_action/healer_left_2.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/resources/player/sprites_action/healer_right_1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/resources/player/sprites_action/healer_right_2.png"));
            
            profileSprite = ImageIO.read(getClass().getResourceAsStream("/resources/player/sprites_display/healer_sc.png"));
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
