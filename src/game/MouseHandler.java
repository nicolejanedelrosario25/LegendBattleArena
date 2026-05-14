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
                gp.chooseHeroes();
                gp.setupItems();
                gp.gameState = gp.characterState;
            }
            //check if exit button is clicked
            if(gp.ui.exitButton != null && gp.ui.exitButton.contains(x, y)){
                System.exit(0);
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
        
        if(gp.ui.playButton.contains(x, y) || gp.ui.exitButton.contains(x, y)){
            gp.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        } else{
            gp.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        }
    }
    
}
