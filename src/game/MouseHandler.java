package game;

import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseHandler extends MouseAdapter {
    
    GamePanel gp;
    public int hoveredHeroIndex = -1;
    private int lastHoveredCommand = -1;
    
    public MouseHandler(GamePanel gp) {
        this.gp = gp;
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        long currentTime = System.currentTimeMillis();
        
        // TITLE STATE
        if (gp.gameState == gp.titleState) {
            if (gp.ui.playButton != null && gp.ui.playButton.contains(x, y)) {
                gp.stopMusic();
                gp.playMusic(1);
                gp.gameState = gp.characterState;
                gp.lastStateChangeTime = System.currentTimeMillis();
                gp.ui.commandNum = 0;
                lastHoveredCommand = -1;
            }
            if (gp.ui.exitButton != null && gp.ui.exitButton.contains(x, y)) {
                System.exit(0);
            }
        } 
        // CHARACTER STATE
        else if (gp.gameState == gp.characterState) {
            if (currentTime - gp.lastStateChangeTime < 250) {
                return;
            }
            
            if (gp.ui.continueButton != null && gp.ui.continueButton.contains(x, y)) {
                if (gp.party.size() == 3) {
                    gp.player = gp.party.get(0);
                    gp.player.worldX = gp.tileSize * 23;
                    gp.player.worldY = gp.tileSize * 21;
                    gp.player.direction = "down";
                    gp.stopMusic();
                    gp.playMusic(2);
                    
                    gp.setupItems();
                    gp.gameState = gp.playState;
                    gp.lastStateChangeTime = currentTime;
                    lastHoveredCommand = -1;
                }
                return;
            }
            
            int rightX = gp.tileSize * 8;
            int rightY = gp.tileSize * 3;
            int rightWidth = gp.tileSize * 7;
            
            if (x >= rightX && x <= rightX + rightWidth) {
                int partyListTopOffset = rightY + (gp.tileSize * 2) - 30;
                if (y >= partyListTopOffset) {
                    int clickedRowIndex = (y - partyListTopOffset) / gp.tileSize;
                    if (clickedRowIndex >= 0 && clickedRowIndex < gp.party.size()) {
                        gp.party.remove(clickedRowIndex);
                        gp.lastStateChangeTime = currentTime;
                        return;
                    }
                }
                return;
            }
            
            int leftFrameX = gp.tileSize;
            int leftFrameWidth = gp.tileSize * 6;
            int frameY = gp.tileSize * 3;
            if (x >= leftFrameX && x <= leftFrameX + leftFrameWidth) {
                int startY = frameY + gp.tileSize;
                for(int i = 0; i < 5; i++){
                    int lineY = startY + (i * gp.tileSize);
                    if(y >= lineY - 30 && y <= lineY + 10){
                        gp.ui.commandNum = i;
                        gp.chooseHeroes();
                        break;
                    }
                }
            }
        } 
        // PLAY STATE
        else if (gp.gameState == gp.playState) {
            if (x >= 10 && x <= 60) {
                if (y >= 10 && y <= 60) {
                    changeHero(0);
                } else if (y >= 110 && y <= 160) {
                    changeHero(1);
                } else if (y >= 170 && y <= 220) {
                    changeHero(2);
                }
            }
        } 
        // BATTLE STATE
        else if (gp.gameState == gp.battleState) {
            int battleSWidth = gp.tileSize * 4;
            int battleSHeight = (int)(gp.tileSize * 0.8);
            int buttonGap = 12;
            int battleX = (gp.screenWidth / 2) - (battleSWidth / 2);
            int startY = (gp.screenHeight / 2) - (gp.tileSize * 1);
            
            for (int i = 0; i < 4; i++) {
                int battleY = startY + (i * (battleSHeight + buttonGap));
                if (x >= battleX && x <= battleX + battleSWidth && y >= battleY && y <= battleY + battleSHeight) {
                    gp.ui.commandNum = i;
                    gp.battleManager.handlePlayerChoice();
                    gp.playSE(3);
                    break;
                }
                  
            }
            
        }
    }
    
    public void changeHero(int index) {
        if (gp.party.size() > index) {
            int currentX = gp.player.worldX;
            int currentY = gp.player.worldY;
            
            gp.player = gp.party.get(index);
            gp.player.worldX = currentX;
            gp.player.worldY = currentY;
            gp.message = "Switching to " + gp.player.getName() + "!";
        }
    }
    
    @Override
    public void mouseMoved(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        boolean changingCursor = false;
        int newCommand = gp.ui.commandNum;
        
        // TITLE STATE
        if (gp.gameState == gp.titleState) {
            int previousCommand = gp.ui.commandNum;
            if (gp.ui.playButton != null && gp.ui.playButton.contains(x, y)) {
                newCommand = 0;
                changingCursor = true;
            } else if (gp.ui.exitButton != null && gp.ui.exitButton.contains(x, y)) {
                newCommand = 1;
                changingCursor = true;
            }
            if (newCommand != previousCommand) {
                gp.ui.commandNum = newCommand;
                lastHoveredCommand = newCommand;
                gp.playSE(0);
            }
        } 
        // CHARACTER STATE
        else if (gp.gameState == gp.characterState) {
            int previousCommand = gp.ui.commandNum;
            int frameX = gp.tileSize;
            int frameY = gp.tileSize * 3;
            int frameWidth = gp.tileSize * 6;
            
            if (x >= frameX && x <= frameX + frameWidth) {
                int startY = frameY + gp.tileSize;
                for (int i = 0; i < 5; i++) {
                    int lineY = startY + (i * gp.tileSize);
                    if (y >= lineY - 30 && y <= lineY + 10) {
                        newCommand = i;
                    }
                }
            }
            if (gp.ui.continueButton != null && gp.ui.continueButton.contains(x, y)) {
                newCommand = 5;
                changingCursor = true;
            }
            if (newCommand != previousCommand) {
                gp.ui.commandNum = newCommand;
                lastHoveredCommand = newCommand;
                gp.playSE(0);
            }
        } 
        // BATTLE STATE
        else if (gp.gameState == gp.battleState) {
            int battleSWidth = gp.tileSize * 4;
            int battleSHeight = (int)(gp.tileSize * 0.8);
            int buttonGap = 12;
            int battleX = (gp.screenWidth / 2) - (battleSWidth / 2);
            int startY = (gp.screenHeight / 2) - (gp.tileSize * 1);
            
            boolean hoverButton = false;
            for (int i = 0; i < 4; i++) {
                int buttonY = startY + (i * (battleSHeight + buttonGap));
                if (x >= battleX && x <= battleX + battleSWidth && y >= buttonY && y <= buttonY + battleSHeight) {
                    newCommand = i;
                    hoverButton = true;
                    changingCursor = true;
                    break;
                }
            }
            if (hoverButton && newCommand != lastHoveredCommand) {
                gp.ui.commandNum = newCommand;
                lastHoveredCommand = newCommand;
                gp.playSE(2);
            }  
        }
        
        // PLAY STATE HERO HOVER
        if (gp.gameState == gp.playState) {
            if (x >= 10 && x <= 60) {
                if (y >= 10 && y <= 60){
                    hoveredHeroIndex = 0;
                }
                else if (y >= 110 && y <= 160) {
                    hoveredHeroIndex = 1;
                }
                else if (y >= 170 && y <= 220){
                    hoveredHeroIndex = 2;
                }   
            }
            
        }
        
        gp.setCursor(new Cursor(changingCursor ? Cursor.HAND_CURSOR : Cursor.DEFAULT_CURSOR));              
    }
}