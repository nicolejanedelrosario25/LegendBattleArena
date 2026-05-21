package ui;

import game.GamePanel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class UI {

    GamePanel gp;
    Graphics2D g2;

    public Image menuBackground;
    public BufferedImage battleBackground;

    public Rectangle playButton, exitButton, continueButton;

    public Font pixelFont;
    public Font fontTitle;
    public Font fontMenuOptions;
    public Font fontBattleUI;
    public Font fontStats;
    public Font fontHPBarText;
    public Font fontDetails;
    public Font fontBattleHeroArrow;
    public Font fontStartAdventure;

    public int playX, playY;
    public int exitX, exitY;
    public int commandNum = 0;

    private entity.Character warriorP, mageP, tankP, healerP, archerP;

    public UI(GamePanel gp) {
        this.gp = gp;

        getMenuImage();
        getBattleImage();

        warriorP = new entity.Warrior(gp);
        mageP = new entity.Mage(gp);
        tankP = new entity.Tank(gp);
        healerP = new entity.Healer(gp);
        archerP = new entity.Archer(gp);

        try {
            Font font = Font.createFont(
                    Font.TRUETYPE_FONT,
                    getClass().getResourceAsStream("/resources/uis/marumonica.ttf")
            );
            pixelFont = font.deriveFont(Font.PLAIN, 48F);
        } catch(FontFormatException | IOException e) {
            pixelFont = new Font("Arial", Font.BOLD, 48);
        }

        fontTitle = pixelFont.deriveFont(Font.BOLD, 52F);
        fontMenuOptions = pixelFont.deriveFont(32F);
        fontBattleUI = pixelFont.deriveFont(Font.BOLD, 22F);
        fontStats = pixelFont.deriveFont(Font.BOLD, 18F);
        fontHPBarText = pixelFont.deriveFont(14F);
        fontDetails = pixelFont.deriveFont(20F);
        fontBattleHeroArrow = pixelFont.deriveFont(Font.BOLD, 30F);
        fontStartAdventure = pixelFont.deriveFont(Font.BOLD, 40F);

        BufferedImage scratchImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D scratchG2 = scratchImage.createGraphics();
        scratchG2.setFont(pixelFont);
        FontMetrics metrics = scratchG2.getFontMetrics();

        String playText = "PLAY GAME";
        playX = gp.screenWidth / 2 - metrics.stringWidth(playText) / 2;
        playY = gp.screenHeight / 2;
        playButton = new Rectangle(playX, playY - 40, 300, 50);

        String exitText = "EXIT";
        exitX = gp.screenWidth / 2 - metrics.stringWidth(exitText) / 2;
        exitY = playY + (gp.tileSize * 2);
        exitButton = new Rectangle(exitX, exitY - 30, 100, 40);

        scratchG2.dispose();
    }

    public void getMenuImage() {
        URL url = getClass().getResource("/resources/uis/start_bg.gif");
        if(url != null) {
            menuBackground = new ImageIcon(url).getImage();
        }
    }

    public void getBattleImage() {
        try {
            battleBackground = ImageIO.read(getClass().getResourceAsStream("/resources/uis/battle.jpg"));
        } catch(Exception e) {
            System.out.println("Could not load battle background.");
        }
    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;

        if(gp.gameState == gp.titleState) {
            drawTitleScreen();
        } else if(gp.gameState == gp.characterState) {
            drawCharacterScreen();
        } else if(gp.gameState == gp.playState) {
            drawCharacter();
            drawMessages();
        } else if(gp.gameState == gp.battleState) {
            drawBattleScreen();      
        }
    }

    public void drawTitleScreen() {
        if(menuBackground != null) {
            g2.drawImage(menuBackground, 0, 0, gp.screenWidth, gp.screenHeight, gp);
        }

        g2.setFont(pixelFont);

        g2.setColor(commandNum == 0 ? Color.YELLOW : Color.WHITE);
        if(commandNum == 0) {
            g2.drawString(">", playX - 40, playY);
        }
        g2.drawString("PLAY GAME", playX, playY);

        g2.setColor(commandNum == 1 ? Color.YELLOW : Color.WHITE);
        if(commandNum == 1) {
            g2.drawString(">", exitX - 40, exitY);
        }
        g2.drawString("EXIT", exitX, exitY);
    }

    public void drawCharacterScreen() {
        if(menuBackground != null) {
            g2.drawImage(menuBackground, 0, 0, gp.screenWidth, gp.screenHeight, gp);
        }

        g2.setFont(fontTitle);

        String text = "SELECT CHARACTER";
        int x = getXforCenteredText(text);
        int y = gp.tileSize * 2;

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

        for(int i = 0; i < heroNames.length; i++) {
            int lineY = frameY + gp.tileSize + (i * gp.tileSize);

            g2.setFont(fontMenuOptions);
            g2.setColor(i == commandNum ? Color.YELLOW : Color.WHITE);

            if(i == commandNum) {
                g2.drawString(">", frameX + 15, lineY);
            }

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

        for(int i = 0; i < gp.party.size(); i++) {
            int partyLineY = rightY + (gp.tileSize * 2) + (i * gp.tileSize);

            if(gp.party.get(i).profileSprite != null) {
                g2.drawImage(gp.party.get(i).profileSprite, rightX + 30, partyLineY - 35, gp.tileSize, gp.tileSize, null);
            }

            g2.drawString((i + 1) + ". " + gp.party.get(i).getName(), rightX + 85, partyLineY);
        }

        entity.Character previewHero = null;

        if(commandNum == 0) previewHero = warriorP;
        else if(commandNum == 1) previewHero = mageP;
        else if(commandNum == 2) previewHero = tankP;
        else if(commandNum == 3) previewHero = healerP;
        else if(commandNum == 4) previewHero = archerP;

        if(previewHero != null && previewHero.profileSprite != null) {
            int previewSize = gp.tileSize * 3;
            int previewX = rightX + (rightWidth / 2) - (previewSize / 2);
            int previewY = rightY + (gp.tileSize * 4);

            g2.drawImage(previewHero.profileSprite, previewX, previewY, previewSize, previewSize, null);
        }

        if(gp.party.size() == 3) {
            text = "START ADVENTURE";
            x = gp.tileSize;
            y = gp.tileSize * 11;

            g2.setFont(fontStartAdventure);
            g2.setColor(commandNum == 5 ? Color.YELLOW : Color.WHITE);

            if(commandNum == 5) {
                g2.drawString(">", x - 30, y);
            }

            g2.drawString(text, x, y);
            continueButton = new Rectangle(x, y - 35, 400, 45);
        } else {
            continueButton = null;
        }

        g2.setFont(fontDetails);
        g2.setColor(Color.WHITE);
        g2.drawString("[BACKSPACE] to remove last hero", rightX + 30, rightY + rightHeight - 20);
    }

    public void drawSubWindow(int x, int y, int width, int height) {
        g2.setColor(new Color(0, 0, 0, 210));
        g2.fillRoundRect(x, y, width, height, 35, 35);

        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
    }

    public int getXforCenteredText(String text) {
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.screenWidth / 2 - length / 2;
    }

    public void drawCharacter() {
        int x = 20;
        int y = 20;
        int iconSize = gp.tileSize;

        for(int i = 0; i < gp.party.size(); i++) {
            entity.Character member = gp.party.get(i);

            if(member.profileSprite != null) {
                g2.drawImage(member.profileSprite, x, y, iconSize, iconSize, null);
            }

            g2.setColor(Color.BLACK);
            g2.fillRect(x + iconSize + 10, y + 10, 100, 12);

            double hpValue = (double)member.getHp() / member.getMaxHp();
            g2.setColor(Color.RED);
            g2.fillRect(x + iconSize + 10, y + 10, (int)(100 * hpValue), 12);

            g2.setColor(Color.BLACK);
            g2.fillRect(x + iconSize + 10, y + 26, 100, 8);

            double mpValue = (double)member.getMana() / member.getMaxMana();
            g2.setColor(Color.CYAN);
            g2.fillRect(x + iconSize + 10, y + 26, (int)(100 * mpValue), 8);

            g2.setFont(fontHPBarText);
            g2.setColor(Color.WHITE);
            g2.drawString(member.getName(), x + iconSize + 10, y + 45);

            y += 70;
        }
    }

    public void drawMessages() {
        if(gp.message != null && !gp.message.isEmpty()) {
            g2.setFont(fontDetails);

            g2.setColor(Color.BLACK);
            g2.drawString(gp.message, gp.tileSize * 5 + 2, gp.tileSize * 2 + 2);

            g2.setColor(Color.WHITE);
            g2.drawString(gp.message, gp.tileSize * 5, gp.tileSize * 2);
        }
    }

    public void drawBattleMenu() {
        String[] options = {"ATTACK", "SKILL", "USE ITEM", "FLEE"};

        int btnWidth = gp.tileSize * 4;
        int btnHeight = (int)(gp.tileSize * 0.8);

        int x = (gp.screenWidth / 2) - (btnWidth / 2);
        int startY = (gp.screenHeight / 2) - gp.tileSize;

        g2.setFont(fontBattleUI);

        for(int i = 0; i < options.length; i++) {
            int y = startY + (i * (btnHeight + 12));

            if(i == commandNum) {
                g2.setColor(Color.YELLOW);
                g2.drawString(">", x - 25, y + (btnHeight / 2) + 8);
                g2.setStroke(new BasicStroke(4));
            } else {
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2));
            }

            g2.drawRoundRect(x, y, btnWidth, btnHeight, 10, 10);

            g2.setColor(new Color(0, 0, 0, 150));
            g2.fillRoundRect(x + 2, y + 2, btnWidth - 4, btnHeight - 4, 8, 8);

            g2.setColor(i == commandNum ? Color.YELLOW : Color.WHITE);

            int stringWidth = g2.getFontMetrics().stringWidth(options[i]);
            int textX = x + (btnWidth / 2) - (stringWidth / 2);
            int textY = y + (btnHeight / 2) + 7;

            g2.drawString(options[i], textX, textY);
        }
    }

    public void drawBattleScreen() {
        if(battleBackground != null) {
            g2.drawImage(battleBackground, 0, 0, gp.screenWidth, gp.screenHeight, null);
        } else {
            g2.setColor(new Color(40, 40, 60));
            g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        }

        int heroW = gp.tileSize * 2;
        int heroH = gp.tileSize * 2;

        int baseX = gp.tileSize;
        int baseY = gp.screenHeight / 2 - heroH / 2;

        int[] heroX = {baseX + 60, baseX, baseX + 30};
        int[] heroY = {baseY + 30, baseY - 50, baseY - 110};

        if(gp.party != null && !gp.party.isEmpty()) {
            for(int i = gp.party.size() - 1; i >= 0; i--) {
                entity.Character hero = gp.party.get(i);

                if(hero == null) continue;

                int hx = (i < heroX.length) ? heroX[i] : heroX[0];
                int hy = (i < heroY.length) ? heroY[i] : heroY[0];
                
                hero.originalBattleX = hx;
                hero.originalBattleY = hy;

                if(!hero.attacking){
                    hero.battleX = hx;
                    hero.battleY = hy;
                }
                
                if(!hero.battlePositionInitialized) {

                    hero.battleX = hx;
                    hero.battleY = hy;

                    hero.originalBattleX = hx;
                    hero.originalBattleY = hy;

                    hero.battlePositionInitialized = true;
                }
        
                
                if(!hero.isAlive()) {
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.35f));
                }

                if(hero.right1 != null) {
                    g2.drawImage(hero.right1, hero.battleX, hero.battleY, gp.tileSize * 2, gp.tileSize * 2, null);
                } else {
                    g2.setColor(Color.BLUE);
                    g2.fillRect(hx, hy, heroW, heroH);
                }

                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

                if(i == gp.currentHeroIndex && hero.isAlive()) {
                    g2.setFont(fontBattleHeroArrow);
                    g2.setColor(Color.YELLOW);
                    g2.drawString(">", hx - 22, hy + heroH / 2);
                }
            }
        }

        if(gp.enemy != null && gp.enemy.hp > 0) {
            int enemyX = gp.screenWidth - (gp.tileSize * 5);
            int enemyY = (gp.screenHeight / 2) - (gp.tileSize * 2);
            
            gp.enemy.battleX = enemyX;
            gp.enemy.battleY = enemyY;

            int enemySize;

            if(gp.enemy.name.equals("Dragon")) {
                enemySize = 480;
                enemyX -= 210;
                enemyY -= 170;
            } else if(gp.enemy.name.equals("Goblin")) {
                enemySize = 320;
                enemyX -= 120;
                enemyY -= 100;
            } else if(gp.enemy.name.equals("Skeleton")) {
                enemySize = 300;
                enemyX -= 110;
                enemyY -= 90;
            } else if(gp.enemy.name.equals("Slime")) {
                enemySize = 220;
                enemyX -= 70;
                enemyY -= 50;
            } else {
                enemySize = 180;
            }

            if(gp.enemy.gifImage != null) {
                g2.drawImage(gp.enemy.gifImage, enemyX, enemyY, enemySize, enemySize, null);
            } else {
                BufferedImage activeEnemySprite =
                        (gp.enemy.spriteNum == 1) ? gp.enemy.image1 : gp.enemy.image2;

                if(activeEnemySprite != null) {
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

        if(gp.party != null && !gp.party.isEmpty()) {
            for(int i = 0; i < gp.party.size(); i++) {
                entity.Character hero = gp.party.get(i);

                if(hero == null) continue;

                int rowY = hudY + 30 + (i * 38);

                g2.setFont(fontStats);
                g2.setColor(hero.isAlive() ? Color.WHITE : Color.GRAY);
                g2.drawString(hero.getName(), leftBoxX + 20, rowY);

                g2.setFont(fontHPBarText);

                g2.setColor(Color.WHITE);
                g2.drawString("HP: " + hero.getHp() + "/" + hero.getMaxHp(), leftBoxX + 110, rowY);

                int hpBarX = leftBoxX + 220;
                int hpBarY = rowY - 10;
                int hpBarW = 90;
                int hpBarH = 7;

                g2.setColor(Color.DARK_GRAY);
                g2.fillRect(hpBarX, hpBarY, hpBarW, hpBarH);

                if(hero.isAlive()) {
                    double hpScale = (double)hero.getHp() / hero.getMaxHp();
                    g2.setColor(Color.GREEN);
                    g2.fillRect(hpBarX, hpBarY, (int)(hpBarW * hpScale), hpBarH);
                }

                g2.setColor(Color.CYAN);
                g2.drawString("MP: " + hero.getMana() + "/" + hero.getMaxMana(), leftBoxX + 110, rowY + 15);

                int mpBarX = leftBoxX + 220;
                int mpBarY = rowY + 5;
                int mpBarW = 90;
                int mpBarH = 6;

                g2.setColor(Color.DARK_GRAY);
                g2.fillRect(mpBarX, mpBarY, mpBarW, mpBarH);

                double mpScale = (double)hero.getMana() / hero.getMaxMana();

                g2.setColor(Color.CYAN);
                g2.fillRect(mpBarX, mpBarY, (int)(mpBarW * mpScale), mpBarH);
            }

            drawBattleMenu();
        }

        if(gp.enemy != null) {
            g2.setFont(fontDetails);
            g2.setColor(Color.WHITE);
            g2.drawString(gp.enemy.name.toUpperCase(), rightBoxX + 15, hudY + 30);

            g2.setFont(fontHPBarText);
            g2.drawString("HP: " + gp.enemy.hp + "/" + gp.enemy.maxHp, rightBoxX + 15, hudY + 52);

            int eBarX = rightBoxX + 15;
            int eBarY = hudY + 60;
            int eBarW = rightBoxWidth - 30;
            int eBarH = 10;

            g2.setColor(Color.DARK_GRAY);
            g2.fillRect(eBarX, eBarY, eBarW, eBarH);

            if(gp.enemy.maxHp > 0) {
                double ehpScale = (double)gp.enemy.hp / gp.enemy.maxHp;

                g2.setColor(new Color(220, 50, 50));
                g2.fillRect(eBarX, eBarY, (int)(eBarW * ehpScale), eBarH);
            }

            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(1));
            g2.drawRect(eBarX, eBarY, eBarW, eBarH);
        }

        if(gp.message != null){

            // TOP MESSAGE BOX
            int boxWidth = gp.screenWidth - 300;
            int boxHeight = 70;

            int boxX = (gp.screenWidth / 2) - (boxWidth / 2);
            int boxY = 40;

            // background
            g2.setColor(new Color(0, 0, 0, 210));
            g2.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 30, 30);

            // border
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(4));
            g2.drawRoundRect(boxX, boxY, boxWidth, boxHeight, 30, 30);

            // text
            g2.setFont(fontDetails.deriveFont(Font.BOLD, 28F));

            int textWidth = g2.getFontMetrics().stringWidth(gp.message);

            int textX = boxX + (boxWidth / 2) - (textWidth / 2);

            int textY = boxY + 45;

            // shadow
            g2.setColor(Color.BLACK);
            g2.drawString(gp.message, textX + 2, textY + 2);

            // main text
            g2.setColor(Color.YELLOW);
            g2.drawString(gp.message, textX, textY);
        }
    }
}