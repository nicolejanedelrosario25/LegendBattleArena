/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author nicol
 */
public class KeyHandler implements KeyListener {

    GamePanel gp;

    public boolean upPressed, downPressed, leftPressed, rightPressed;

    public KeyHandler(GamePanel gp) {

        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        int code = e.getKeyCode();

        //TITLE STATE LOGIC
        if(gp.gameState == gp.titleState){
            if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP){
                gp.ui.commandNum--;
                if(gp.ui.commandNum < 0){
                    gp.ui.commandNum = 1;
                }
            }
            if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN){
              gp.ui.commandNum++;
              if(gp.ui.commandNum > 1){
                  gp.ui.commandNum = 0;
              }
            }
            if(code == KeyEvent.VK_ENTER){
                if(gp.ui.commandNum == 0){

                    gp.gameState = gp.characterState;
                    gp.ui.commandNum = 0;
                    gp.requestFocusInWindow();
                }
                if(gp.ui.commandNum == 1){
                    System.exit(0);
                }
            }
            
        }
        
        //character selection logic
        else if(gp.gameState == gp.characterState) {
            if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP){
                gp.ui.commandNum--;
                if(gp.ui.commandNum < 0){
                    gp.ui.commandNum = 4;
                }
            }
            else if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN){
                gp.ui.commandNum++;
                if(gp.ui.commandNum > 4){
                    gp.ui.commandNum = 0;
                }
            }
            if(code == KeyEvent.VK_ENTER){
                
                gp.chooseHeroes();
                
                if(gp.party.size() == 3){
                    gp.setupItems();              
                    gp.gameState = gp.playState; 
                    gp.requestFocusInWindow();
                }
            }
        }
        
        //play state logic
        else if(gp.gameState == gp.playState){
            
        // SAVE GAME
            if(code == KeyEvent.VK_P) {
                gp.saveLoad.saveGame();
            }

            // LOAD GAME
            if(code == KeyEvent.VK_L) {
                gp.saveLoad.loadGame();
            }

            // MOVEMENT KEYS
            if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                upPressed = true;
            }

            if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                downPressed = true;
            }

            if(code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
                leftPressed = true;
            }

            if(code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
                rightPressed = true;
            }
        }
        
    }

    @Override
    public void keyReleased(KeyEvent e) {

        int code = e.getKeyCode();

        if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {

            upPressed = false;
        }

        if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {

            downPressed = false;
        }

        if(code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {

            
            leftPressed = false;
        }

        if(code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {

            rightPressed = false;
        }
    }
}