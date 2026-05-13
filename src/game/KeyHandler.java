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

        // ENTER KEY
        if(code == KeyEvent.VK_ENTER) {

            if(gp.gameState == gp.titleState) {
                
                
                gp.chooseHeroes();
                gp.setupItems();
                
                gp.gameState = gp.playState;
                
                
                gp.requestFocusInWindow();
            }
        }

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