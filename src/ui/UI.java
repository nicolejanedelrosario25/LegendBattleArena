/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import game.GamePanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Image;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.IOException;
import java.net.URL;
import javax.swing.ImageIcon;

/**
 *
 * @author DELL
 */


public class UI {
    
    GamePanel gp;
    Graphics2D g2;
    public Image menuBackground;
    
    public Rectangle playButton, exitButton;
    public Font pixelFont;
    public int commandNum = 0;
    
    public UI(GamePanel gp){
        this.gp = gp;
        getMenuImage();
        
        try{
        Font font = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/resources/uis/marumonica.ttf"));
        pixelFont = font.deriveFont(Font.PLAIN, 48F);           
        }catch (FontFormatException | IOException e){
            e.printStackTrace();
            pixelFont = new Font("Arial", Font.BOLD, 48);
        }

    }
    
    public void getMenuImage(){
        
        URL url = getClass().getResource("/resources/uis/start_bg.gif");
        if(url != null){
            menuBackground = new ImageIcon(url).getImage();
        }
    }
    
    public void draw(Graphics2D g2){
        this.g2 = g2;
        g2.setFont(pixelFont);
        g2.setColor(Color.white);
        
        if(gp.gameState == gp.titleState){
            drawTitleScreen();
        }
        if(gp.gameState == gp.characterState){
            drawCharacterScreen();
        }
    }
    
    public void drawCharacterScreen(){
        g2.drawImage(menuBackground, 0, 0, gp.screenWidth, gp.screenHeight, gp);
        
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 52F));
        String text = "SELECT CHARACTER";
        int x = getXforCenteredText(text);
        int y = gp.tileSize * 2;
        g2.drawString(text, x, y);
        
        //title shadow
        g2.setColor(Color.BLACK);
        g2.drawString(text, x + 3, y + 3);
        g2.setColor(Color.WHITE);
        g2.drawString(text, x, y);
        
        int frameX = gp.tileSize;
        int frameY = gp.tileSize * 3;
        int frameWidth = gp.tileSize * 6;
        int frameHeight = gp.tileSize * 7;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);
        
        String[] heroNames = {"WARRIOR", "MAGE", "TANK", "HEALER", "ARCHER"};
        
        for(int i = 0; i < heroNames.length; i++){
            int lineY = frameY + gp.tileSize + (i * gp.tileSize);
            
            if(i == commandNum){
                g2.setColor(Color.YELLOW);
                g2.drawString(">", frameX + 15, lineY);
            }else{
                g2.setColor(Color.WHITE);
            }
            g2.setFont(pixelFont.deriveFont(32F));
            g2.drawString(heroNames[i], frameX + 60, lineY);
        }
        
        int rightX = gp.tileSize * 8;
        int rightY = gp.tileSize * 3;
        int rightWidth = gp.tileSize * 7;
        int rightHeight = gp.tileSize * 8;
        drawSubWindow(rightX, rightY, rightWidth, rightHeight);
        
        g2.setFont(pixelFont.deriveFont(32F));
        g2.setColor(Color.WHITE);
        g2.drawString("YOUR PARTY (" + gp.party.size() + "/3):", rightX + 30, rightY + gp.tileSize);
        
                //party list
        for(int i = 0; i < gp.party.size(); i++){
            //draw names of the hero selected
            int partyLineY = rightY + (gp.tileSize * 2) + (i * gp.tileSize);
            
            if(gp.party.get(i).profileSprite != null){
                g2.drawImage(gp.party.get(i).profileSprite, rightX + 30, partyLineY - 35, gp.tileSize, gp.tileSize, null);
            }
            g2.drawString((i + 1) + ". " + gp.party.get(i).getName(), rightX + 85, partyLineY);           
        }
        
        //preview image
        entity.Character previewHero = null;
        
        if(commandNum == 0){
            previewHero = new entity.Warrior();
        }else if(commandNum == 1){
            previewHero = new entity.Mage();
        }else if(commandNum == 2){
            previewHero = new entity.Tank();
        }else if(commandNum == 3){
            previewHero = new entity.Healer();
        }else if(commandNum == 4){
            previewHero = new entity.Archer();
        }
        
        if(previewHero != null && previewHero.profileSprite != null){
            int previewSize = gp.tileSize * 3;
            int previewX = rightX + (rightWidth/2) - (previewSize/2);
            int previewY = rightY + (gp.tileSize * 4);
            g2.drawImage(previewHero.profileSprite, previewX, previewY, previewSize, previewSize, null);
        }
        
        drawHeroDetails(rightX, rightY);  
        

        
    }
    
    public void drawHeroDetails(int x, int y){
       g2.setFont(pixelFont.deriveFont(24F));
       g2.setColor(Color.WHITE);
       
       int textX = x + 30;
       int textY = y + gp.tileSize;
       
       //changes text based on which hero is highlighted
//       switch(commandNum){
//           case 0 -> {
//               g2.drawString("Class: Warrior", textX, textY + 30);
//               g2.drawString("STR: 10 DEF: 8", textX, textY + 55);
//           }case 1 -> {
//               g2.drawString("Class: Mage", textX, textY + 30);
//               g2.drawString("INT: 15 MP: 20", textX, textY + 55);
//           }case 2 -> {
//               g2.drawString("Class: Tank", textX, textY + 30);
//               g2.drawString("HP: 150 DEF: 15", textX, textY + 55);
//           }case 3 -> {
//               g2.drawString("Class: Archer", textX, textY + 30);
//               g2.drawString("AGI: 12 RNG: 5", textX, textY + 55);
//           }case 4 -> {
//               g2.drawString("Class: Healer", textX, textY + 30);
//               g2.drawString("WIS: 12 HEAL: 10", textX, textY + 55);
//           }
//       }
    }
       
    public void drawSubWindow(int x, int y, int width, int height){
        g2.setColor(new Color(0, 0, 0, 210));
        g2.fillRoundRect(x, y, width, height, 35, 35);

        g2.setColor(Color.WHITE);
        g2.setStroke(new java.awt.BasicStroke(3));
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
    }    
    
    public void drawTitleScreen(){
        
        if (menuBackground != null){
            g2.drawImage(menuBackground, 0, 0, gp.screenWidth, gp.screenHeight, gp);
        }
        
        g2.setFont(pixelFont);
        
//        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 40F));
//        g2.setColor(Color.WHITE);
        
        String text ="PLAY GAME";
        int x = getXforCenteredText(text);
        int y = gp.screenHeight/2;
        playButton = new Rectangle(x, y - 40, 300, 50);
//        g2.drawString(text, x, y);
        
        if(commandNum == 0){
            g2.setColor(Color.YELLOW);
            g2.drawString(">", x - 40, y);
        }else{
            g2.setColor(Color.WHITE);
        }
        g2.drawString(text, x, y);
        
        text = "EXIT";
        x = getXforCenteredText(text);
        y+= gp.tileSize * 2;
        exitButton = new Rectangle(x, y - 30, 100, 40);
        
        if(commandNum == 1){
            g2.setColor(Color.YELLOW);
            g2.drawString(">", x- 40, y);
        }else{
            g2.setColor(Color.WHITE);
        }
        g2.drawString(text, x, y);
    }
    
    public int getXforCenteredText(String text){
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.screenWidth/2 - length/2;
    }
    
}
