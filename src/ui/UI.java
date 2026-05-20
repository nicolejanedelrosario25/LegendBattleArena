/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import game.GamePanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Image;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author DELL
 */
public class UI {
    
    GamePanel gp;
    Graphics2D g2;
    public Image menuBackground;
    public BufferedImage battleBackground;
    
    public Rectangle playButton, exitButton, continueButton;
    public Font pixelFont;
    
    // --- CACHED FONT VARIATIONS ---
    public Font fontTitle;
    public Font fontMenuOptions;
    public Font fontBattleUI;
    public Font fontStats;
    public Font fontHPBarText;
    public Font fontDetails;
    
    public Font fontBattleHeroArrow;
    public Font fontStartAdventure;
    public Font fontEnemyHp;
    
    // --- PRE-CALCULATED TITLE SCREEN POSITIONS ---
    public int playX, playY;
    public int exitX, exitY;
    
    public int commandNum = 0;
    private entity.Character warriorP, mageP, tankP, healerP, archerP;
    
    public UI(GamePanel gp){
        this.gp = gp;
        getMenuImage();
        getBattleImage();
        warriorP = new entity.Warrior(gp);
        mageP = new entity.Mage(gp);
        tankP = new entity.Tank(gp);
        healerP = new entity.Healer(gp);
        archerP = new entity.Archer(gp);
        
        try{
            Font font = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/resources/uis/marumonica.ttf"));
            pixelFont = font.deriveFont(Font.PLAIN, 48F);           
        }catch (FontFormatException | IOException e){
            e.printStackTrace();
            pixelFont = new Font("Arial", Font.BOLD, 48);
        }
        
        // Allocate font instances ONCE at startup
        fontTitle = pixelFont.deriveFont(Font.BOLD, 52F);
        fontMenuOptions = pixelFont.deriveFont(32F);
        fontBattleUI = pixelFont.deriveFont(Font.BOLD, 22F);
        fontStats = pixelFont.deriveFont(Font.BOLD, 18F);
        fontHPBarText = pixelFont.deriveFont(14F);
        fontDetails = pixelFont.deriveFont(20F);
        fontBattleHeroArrow = pixelFont.deriveFont(24F);
        fontStartAdventure = pixelFont.deriveFont(Font.BOLD, 40F);
        fontEnemyHp = pixelFont.deriveFont(16F);
        
        // Pre-calculate text centering & button boundaries for Title Screen
        BufferedImage scratchImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D scratchG2 = scratchImage.createGraphics();
        scratchG2.setFont(pixelFont);
        FontMetrics metrics = scratchG2.getFontMetrics();
        
        // Calculate "PLAY GAME" layout variables
        String playText = "PLAY GAME";
        int playWidth = metrics.stringWidth(playText);
        this.playX = gp.screenWidth / 2 - playWidth / 2;
        this.playY = gp.screenHeight / 2;
        this.playButton = new Rectangle(playX, playY - 40, 300, 50);
        
        // Calculate "EXIT" layout variables
        String exitText = "EXIT";
        int exitWidth = metrics.stringWidth(exitText);
        this.exitX = gp.screenWidth / 2 - exitWidth / 2;
        this.exitY = playY + (gp.tileSize * 2);
        this.exitButton = new Rectangle(exitX, exitY - 30, 100, 40);
        
        scratchG2.dispose();
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
        else if(gp.gameState == gp.characterState){
            drawCharacterScreen();
        }
        else if(gp.gameState == gp.playState){
            drawCharacter();
            drawMessages();
        }
        else if(gp.gameState == gp.battleState){
            drawBattleScreen();
        }
    }
    
    public void drawTitleScreen(){
        if (menuBackground != null){
            g2.drawImage(menuBackground, 0, 0, gp.screenWidth, gp.screenHeight, gp);
        }
        
        g2.setFont(pixelFont);
        
        // "PLAY GAME" selection styling
        if(commandNum == 0){
            g2.setColor(Color.YELLOW);
            g2.drawString(">", playX - 40, playY);
        }else{
            g2.setColor(Color.WHITE);
        }
        g2.drawString("PLAY GAME", playX, playY);
        
        // "EXIT" selection styling
        if(commandNum == 1){
            g2.setColor(Color.YELLOW);
            g2.drawString(">", exitX - 40, exitY);
        }else{
            g2.setColor(Color.WHITE);
        }
        g2.drawString("EXIT", exitX, exitY);
    }
    
    public void drawCharacterScreen(){
        g2.drawImage(menuBackground, 0, 0, gp.screenWidth, gp.screenHeight, gp);
        
        g2.setFont(fontTitle);
        String text = "SELECT CHARACTER";
        int x = getXforCenteredText(text);
        int y = gp.tileSize * 2;
        
        // Title shadow
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
            g2.setFont(fontMenuOptions);
            g2.drawString(heroNames[i], frameX + 60, lineY);
        }
        
        int rightX = gp.tileSize * 8;
        int rightY = gp.tileSize * 3;
        int rightWidth = gp.tileSize * 7;
        int rightHeight = gp.tileSize * 8;
        drawSubWindow(rightX, rightY, rightWidth, rightHeight);
        
        g2.setFont(fontMenuOptions);
        g2.setColor(Color.WHITE);
        g2.drawString("YOUR PARTY (" + gp.party.size() + "/3):", rightX + 30, rightY + gp.tileSize);
        
        // Party list render loop
        for(int i = 0; i < gp.party.size(); i++){
            int partyLineY = rightY + (gp.tileSize * 2) + (i * gp.tileSize);
            
            if(gp.party.get(i).profileSprite != null){
                g2.drawImage(gp.party.get(i).profileSprite, rightX + 30, partyLineY - 35, gp.tileSize, gp.tileSize, null);
            }
            g2.drawString((i + 1) + ". " + gp.party.get(i).getName(), rightX + 85, partyLineY);           
        }
        
        // Preview image matching
        entity.Character previewHero = null;
        if(commandNum == 0){
            previewHero = warriorP;
        }else if(commandNum == 1){
            previewHero = mageP;
        }else if(commandNum == 2){
            previewHero = tankP;
        }else if(commandNum == 3){
            previewHero = healerP;
        }else if(commandNum == 4){
            previewHero = archerP;
        }
        
        if(previewHero != null && previewHero.profileSprite != null){
            int previewSize = gp.tileSize * 3;
            int previewX = rightX + (rightWidth/2) - (previewSize/2);
            int previewY = rightY + (gp.tileSize * 4);
            g2.drawImage(previewHero.profileSprite, previewX, previewY, previewSize, previewSize, null);
        }
        
        drawHeroDetails(rightX, rightY);  
        
        // Dynamic "Start Adventure" choice box tracking
        if(gp.party.size() == 3){
            text = "START ADVENTURE";
            x = gp.tileSize * 1;
            y = gp.tileSize * 11;
            
            g2.setFont(fontStartAdventure);
            g2.setColor(Color.WHITE);
            
            if(commandNum == 5){
                g2.setColor(Color.YELLOW);
                g2.drawString(">", x - 30, y);
            }
            
            g2.drawString(text, x, y);
            continueButton = new Rectangle(x, y - 35, 400, 45);
        }else{
            continueButton = null;
        }
        
        for(int i = 0; i < gp.party.size(); i++){
            int partyLineY = rightY + (gp.tileSize * 2) + (i * gp.tileSize);
            g2.setColor(Color.WHITE);
            if(gp.party.get(i).profileSprite != null){
                g2.drawImage(gp.party.get(i).profileSprite, rightX + 30, partyLineY - 35, gp.tileSize, gp.tileSize, null);
            }
        }
        
        g2.setFont(fontDetails);
        g2.drawString("[BACKSPACE] to remove last hero", rightX + 30, rightY + rightHeight - 20);
    }
    
    public void drawHeroDetails(int x, int y){
       g2.setFont(pixelFont.deriveFont(24F));
       g2.setColor(Color.WHITE);
       
       int textX = x + 30;
       int textY = y + (gp.tileSize * 7);
    }
       
    public void drawSubWindow(int x, int y, int width, int height){
        g2.setColor(new Color(0, 0, 0, 210));
        g2.fillRoundRect(x, y, width, height, 35, 35);

        g2.setColor(Color.WHITE);
        g2.setStroke(new java.awt.BasicStroke(3));
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
    }    
    
    public int getXforCenteredText(String text){
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.screenWidth/2 - length/2;
    }
    
    public void drawCharacter(){
        int x = 20;
        int y = 20;
        int iconSize = gp.tileSize; 

        for (int i = 0; i < gp.party.size(); i++) {
            entity.Character member = gp.party.get(i);

            if (member.profileSprite != null) {
                g2.drawImage(member.profileSprite, x, y, iconSize, iconSize, null);
            }

            g2.setColor(Color.BLACK);
            g2.fillRect(x + iconSize + 10, y + 10, 100, 12); 

            double hpBarValue = (double)member.getHp() / member.getMaxHp();
            g2.setColor(new Color(255, 0, 0)); 
            g2.fillRect(x + iconSize + 10, y + 10, (int)(100 * hpBarValue), 12);

            g2.setColor(Color.BLACK);
            g2.fillRect(x + iconSize + 10, y + 26, 100, 8);
            g2.setColor(new Color(0, 100, 255)); 
            g2.fillRect(x + iconSize + 10, y + 26, 80, 8); 

            g2.setFont(fontHPBarText);
            g2.setColor(Color.WHITE);
            g2.drawString(member.getName(), x + iconSize + 10, y + 40);

            y += 70; 
        }
    }
    
    public void drawMessages() {
        if (gp.message != null && !gp.message.isEmpty()) {
            g2.setFont(fontDetails);
            g2.setColor(Color.BLACK);
            g2.drawString(gp.message, gp.tileSize * 5 + 2, gp.tileSize *2 + 2);

            g2.setColor(Color.WHITE);
            g2.drawString(gp.message, gp.tileSize * 5, gp.tileSize * 2);
        }
    }
    
    public void getBattleImage(){
        try{
            battleBackground = ImageIO.read(getClass().getResourceAsStream("/resources/uis/battle.jpg"));
        }catch(Exception e){
            System.out.println("Error: Could not load battle background image!");
            e.printStackTrace();
        }
    }
    
    public void drawBattleMenu() {
        String[] options = {"ATTACK", "SKILL", "USE ITEM", "FLEE"};

        int btnWidth = gp.tileSize * 4; 
        int btnHeight = (int)(gp.tileSize * 0.8); 

        int x = (gp.screenWidth / 2) - (btnWidth / 2); 
        int startY = (gp.screenHeight / 2) - (gp.tileSize * 1); 

        g2.setFont(fontBattleUI);

        for(int i = 0; i < options.length; i++) {
            int y = startY + (i * (btnHeight + 12)); 

            if(i == commandNum) {
                g2.setColor(Color.YELLOW);
                g2.drawString(">", x - 25, y + (btnHeight / 2) + 8);

                g2.setStroke(new BasicStroke(4));
                g2.drawRoundRect(x, y, btnWidth, btnHeight, 10, 10);
            } else {
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(x, y, btnWidth, btnHeight, 10, 10);
            }

            g2.setColor(new Color(0, 0, 0, 150));
            g2.fillRoundRect(x + 2, y + 2, btnWidth - 4, btnHeight - 4, 8, 8);

            if(i == commandNum) {
                g2.setColor(Color.YELLOW);
            } else {
                g2.setColor(Color.WHITE);
            }

            int stringWidth = g2.getFontMetrics().stringWidth(options[i]);
            int textX = x + (btnWidth / 2) - (stringWidth / 2);
            int textY = y + (btnHeight / 2) + 7; 

            g2.drawString(options[i], textX, textY);
        }
    }
    
    public void drawBattleScreen(){
        if(battleBackground != null){
            g2.drawImage(battleBackground, 0, 0, gp.screenWidth, gp.screenHeight, null);
        }else{
            g2.setColor(new Color(40, 40, 60));
            g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        }
        
        // ── Hero positions: staggered diagonal like RPG battle (front/mid/back) ──
        // FIXED: positions now form a diagonal instead of a straight vertical line
        // Hero 0 = front  (lower-left)
        // Hero 1 = middle (center-left, slightly up)
        // Hero 2 = back   (upper-left, further back)
        int[] heroX = {
            gp.tileSize * 2,                    // front hero X
            gp.tileSize * 2 + 30,              // mid hero X (slightly right = further back)
            gp.tileSize * 2 + 60               // back hero X
        };
        int[] heroY = {
            gp.screenHeight / 2 - gp.tileSize, // front hero Y (lower)
            gp.screenHeight / 2 - (int)(gp.tileSize * 2.2), // mid hero Y
            gp.screenHeight / 2 - (int)(gp.tileSize * 3.5)  // back hero Y (highest)
        };
        
        if(gp.party != null && !gp.party.isEmpty()){
            for(int i = gp.party.size() - 1; i >= 0; i--){
                entity.Character hero = gp.party.get(i);
                if(hero == null){
                    continue;
                }
                int hx = (i < heroX.length) ? heroX[i] : heroX[heroX.length - 1];
                int hy = (i < heroY.length) ? heroY[i] : heroY[heroY.length - 1];
                if(!hero.isAlive()){
                    g2.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, 0.35f));
                }
                //this
                if(hero.right1 != null){
                    g2.drawImage(hero.right1, hx, hy, gp.tileSize * 2, gp.tileSize * 2, null);
                }else{
                    g2.setColor(Color.BLUE);
                    g2.fillRect(hx, hy, gp.tileSize * 2, gp.tileSize * 2);
                }//until this
                
//                g2.drawImage(hero.right1, hx, hy, gp.tileSize * 2, gp.tileSize * 2, null);
                
                //reset alpha
                g2.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, 1.0f));
                
                //arrow on hero
                if(i == gp.currentHeroIndex && hero.isAlive()){
                    g2.setFont(fontBattleHeroArrow);
                    g2.setColor(Color.YELLOW);
                    g2.drawString(">", hx- 22, hy + gp.tileSize);
                }                 
            }
        }
        
        if(gp.enemy != null && gp.enemy.hp > 0){
            int enemyX = gp.screenWidth - (gp.tileSize * 5);
            int enemyY = (gp.screenHeight / 2) - (gp.tileSize * 2);
            
            int enemySize = gp.enemy.name.equals("Dragon") ? 180: 96;
            if(gp.enemy.name.equals("Dragon")){
                enemyX -= 40;
            }
            
            if(gp.enemy.name.equals("Dragon") && gp.enemy.gifImage != null){
                g2.drawImage(gp.enemy.gifImage, enemyX, enemyY, enemySize, enemySize, null);
            }else{
                java.awt.image.BufferedImage activeEnemySprite = (gp.enemy.spriteNum == 1) ? gp.enemy.image1 : gp.enemy.image2;
                if(activeEnemySprite != null){
                    g2.drawImage(activeEnemySprite, enemyX, enemyY, enemySize, enemySize, null);
                }
            }
        }
        
        int hudY = gp.screenHeight - (int)(gp.tileSize * 3.2);
        int hudHeight = (int)(gp.tileSize * 2.8);
        int leftBoxX = 20;
        int leftBoxWidth = gp.tileSize * 7;
        drawSubWindow(leftBoxX, hudY, leftBoxWidth, hudHeight);
        
        int rightBoxWidth = gp.tileSize * 4;
        int rightBoxX = gp.screenWidth - rightBoxWidth - 20;
        drawSubWindow(rightBoxX, hudY, rightBoxWidth, hudHeight);
        
        if(gp.party != null && !gp.party.isEmpty()){
            for(int i = 0; i < gp.party.size(); i++){
                entity.Character hero = gp.party.get(i);
                if(hero == null){
                    continue;
                }
                int rowY = hudY + 35 + (i * 32);
                
                g2.setFont(fontStats);
                g2.setColor(hero.isAlive() ? Color.WHITE : Color.GRAY);
                g2.drawString(hero.getName(), leftBoxX + 20, rowY);
                
                g2.setFont(fontHPBarText);
                g2.drawString("HP: " + hero.getHp() + "/" + hero.getMaxHp(), leftBoxX + 105, rowY);
                
                int barX = leftBoxX + 190;
                int barY = rowY - 10;
                int barW = 95;
                int barH = 8;
                g2.setColor(Color.DARK_GRAY);
                g2.fillRect(barX, barY, barW, barH);
                if(hero.isAlive()){
                    double hpScale = (double)hero.getHp() / hero.getMaxHp();
                    Color barColor = hpScale > 0.5 ? new Color(0, (int)(255 * hpScale), 0) : new Color(255, (int)(255 * hpScale * 2), 0);
                    g2.setColor(barColor);
                    g2.fillRect(barX, barY, (int)(barW * hpScale), barH); 
                }

            }
            drawBattleMenu();
        }
        
        if(gp.enemy != null){
            g2.setFont(fontDetails);
            g2.setColor(Color.WHITE);
            g2.drawString(gp.enemy.name.toUpperCase(), rightBoxX + 15, hudY + 30);
            
            g2.setFont(fontHPBarText); // Safe fast resize
            g2.drawString("HP: " + gp.enemy.hp + "/" + gp.enemy.maxHp, rightBoxX + 15, hudY + 52);  
        }
        
        //enemy hp bar
        int eBarX = rightBoxX + 15;
        int eBarY = hudY + 60;
        int eBarW = rightBoxWidth - 30;
        int eBarH = 10;
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(eBarX, eBarY, eBarW, eBarH);
        
        if(gp.enemy.maxHp > 0){
            double ehpScale = (double) gp.enemy.hp / gp.enemy.maxHp;
            g2.setColor(new Color(220, 50, 50));
            g2.fillRect(eBarX, eBarY, (int)(eBarW * ehpScale), eBarH);
        }
        
        //hp bar border
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(1));
        g2.drawRect(eBarX, eBarY, eBarW, eBarH);
        
        if(gp.message != null){
            g2.setFont(fontDetails);
            g2.setColor(Color.YELLOW);
            g2.drawString(gp.message, gp.tileSize, gp.screenHeight - 40);
        }
    }
}