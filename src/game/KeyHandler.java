package game;

import entity.Enemy;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JOptionPane;

public class KeyHandler implements KeyListener {

    GamePanel gp;

    public boolean upPressed;
    public boolean downPressed;
    public boolean leftPressed;
    public boolean rightPressed;
    public boolean enterPressed;

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        // TITLE SCREEN
        if (gp.gameState == gp.titleState) {
            if (code == KeyEvent.VK_UP) {
                gp.ui.commandNum--;
                gp.playSE(0);
                if (gp.ui.commandNum < 0) {
                    gp.ui.commandNum = 1;
                }
            }
            if (code == KeyEvent.VK_DOWN) {
                gp.ui.commandNum++;
                gp.playSE(0);
                if (gp.ui.commandNum > 1) {
                    gp.ui.commandNum = 0;
                }
            }
            if (code == KeyEvent.VK_ENTER) {
                if (gp.ui.commandNum == 0) {
                    gp.stopMusic();
                    gp.playMusic(1);
                    gp.playSE(1);
                    gp.ui.commandNum = 0;
                    enterPressed = true;
                    gp.gameState = gp.characterState;
                } else if (gp.ui.commandNum == 1) {
                    System.exit(0);
                }
            }
        }

        // CHARACTER SELECTION SCREEN
        else if (gp.gameState == gp.characterState) {
            if (code == KeyEvent.VK_UP) {
                gp.ui.commandNum--;
                gp.playSE(0);
                if (gp.ui.commandNum < 0) {
                    gp.ui.commandNum = (gp.party.size() == 3) ? 5 : 4;   
                }
            }
            if (code == KeyEvent.VK_DOWN) {
                gp.ui.commandNum++;
                int maxCommand = (gp.party.size() == 3) ? 5 : 4;
                gp.playSE(0);
                if (gp.ui.commandNum > maxCommand) {
                    gp.ui.commandNum = 0;
                }
            }
            if (code == KeyEvent.VK_ENTER) {
                gp.playSE(2);
                if (gp.ui.commandNum == 5 && gp.party.size() > 0) {
                    if (gp.party.size() == 3) {
                        gp.player = gp.party.get(0);
                        gp.player.worldX = gp.tileSize * 23;
                        gp.player.worldY = gp.tileSize * 21;
                        gp.player.direction = "down";
                        gp.stopMusic();
                        gp.playMusic(2);
                        gp.setupItems();
                        gp.gameState = gp.playState;
                    }
                } else if (gp.ui.commandNum < 5) {
                    gp.chooseHeroes();
                }
            }
            if (code == KeyEvent.VK_BACK_SPACE) {
                if (gp.party.size() > 0) {
                    gp.party.remove(gp.party.size() - 1);
                    if (gp.ui.commandNum == 5) {
                        gp.ui.commandNum = 4;
                    }
                    System.out.println("Removed last hero. Party size: " + gp.party.size());
                }
            }
        }

        // PLAYING SCREEN
        else if (gp.gameState == gp.playState) {          
            
            if (code == KeyEvent.VK_ESCAPE) {
                // Reset inputs so your character doesn't keep moving while menu is open
                upPressed = false;
                downPressed = false;
                leftPressed = false;
                rightPressed = false;

                openGameMenu();
            }
            
            // MOVEMENT KEYS
            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP)   upPressed = true;
            if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) downPressed = true;
            if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) leftPressed = true;
            if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) rightPressed = true;
        }
        
        // BATTLE SCREEN
        else if (gp.gameState == gp.battleState) {
            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                gp.playSE(0);
                gp.ui.commandNum--;
                if (gp.ui.commandNum < 0) gp.ui.commandNum = 3;
            }
            if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                gp.playSE(0);
                gp.ui.commandNum++;
                if (gp.ui.commandNum > 3) gp.ui.commandNum = 0;
            }
            if (code == KeyEvent.VK_ENTER) {
                gp.playSE(2);
                gp.battleManager.handlePlayerChoice();
            }
            if (code == KeyEvent.VK_ESCAPE) {
                gp.gameState = gp.playState;
            }
        }
    }

    public void openGameMenu() {
        String[] options = { "Save Game", "Load Game", "Resume", "Exit Game" };

        int choice = JOptionPane.showOptionDialog(
                null, "Choose an option:", "Game Menu",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, options, options[0]
        );

        if (choice == 0) {
            gp.saveLoad.saveGame();
            gp.message = "Game saved.";
        } else if (choice == 1) {
            gp.saveLoad.loadGame();
            gp.message = "Game loaded.";
        } else if (choice == 2 || choice < 0) {
            gp.message = "Game resumed.";
        } else if (choice == 3) {
            int confirm = JOptionPane.showConfirmDialog(
                    null, "Are you sure you want to exit?", "Exit Game",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            } else {
                gp.message = "Game resumed.";
            }
        }
        
        // CRITICAL: Request focus window immediately back to Game Panel 
        // to prevent keys breaking after option selection closes
        gp.requestFocusInWindow();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP)   upPressed = false;
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) downPressed = false;
        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) leftPressed = false;
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) rightPressed = false;
        if (code == KeyEvent.VK_ENTER) enterPressed = false;
    }
}