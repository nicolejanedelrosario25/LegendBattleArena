/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
/**
 *
 * @author DELL
 */
public class MouseHandler extends MouseAdapter{
    
    GamePanel gp;
    
    public MouseHandler(GamePanel gp){
        this.gp = gp;
    }
    
    @Override
    public void mousePressed(MouseEvent e){
        //we get the x and y of where the user clicked
        int x = e.getX();
        int y = e.getY();
        
        if(gp.gameState == gp.titleState){
            //check if play button is clicked
            if(gp.ui.playButton != null &&  gp.ui.playButton.contains(x, y)){
                gp.gameState = gp.characterState;
                gp.lastStateChangeTime = System.currentTimeMillis();
                
                gp.ui.commandNum = 0;
            }
            //check if exit button is clicked
            if(gp.ui.exitButton != null && gp.ui.exitButton.contains(x, y)){
                System.exit(0);
            }
            
        }else if (gp.gameState == gp.characterState){

               if(gp.ui.continueButton != null && gp.ui.continueButton.contains(x, y)){
                   if(gp.party.size() == 3){
                       gp.setupItems();
                       gp.gameState = gp.playState;
                   }
               }else{
                   gp.chooseHeroes();
               }
        } 
    }
    
    @Override
    public void mouseMoved(java.awt.event.MouseEvent e){
        int x = e.getX();
        int y = e.getY();
        
        if(gp.gameState == gp.titleState){
            //if mouse is over Play
            if(gp.ui.playButton != null && gp.ui.playButton.contains(x, y)){
                gp.ui.commandNum = 0;
            }
            //if mouse is over Exit
            else if(gp.ui.exitButton != null && gp.ui.exitButton.contains(x, y)){
                gp.ui.commandNum = 1;
            }
        }       
        //character selection
        else if(gp.gameState == gp.characterState){
            // We define the area where the names are drawn
            int frameX = gp.tileSize;
            int frameY  = gp.tileSize * 3;
            int frameWidth = gp.tileSize * 6;
            // If mouse is within the horizontal width of the selection box
            if(x >= frameX && x <= frameX + frameWidth){
                int startY = frameY + gp.tileSize;
                for(int i = 0; i < 5; i++){
                    int lineY = startY + (i * gp.tileSize);
                    // If mouse Y is near a specific hero's line
                    if(y >= lineY - 30 && y <= lineY + 10){
                        gp.ui.commandNum = i;
                    }
                }
            }

        }
        
        if(gp.ui.playButton.contains(x, y) || gp.ui.exitButton.contains(x, y)){
            gp.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        } else{
            gp.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        }
        
        if(gp.ui.continueButton != null && gp.ui.continueButton.contains(x, y)){
            gp.ui.commandNum = 5;
        }
    }
    
}
