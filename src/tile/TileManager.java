/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tile;

import game.GamePanel;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
<<<<<<< HEAD
import java.io.IOException;
=======
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
>>>>>>> c458095 (Adding enemy sprites and map)
import javax.imageio.ImageIO;

/**
 *
 * @author nicol
 */
public class TileManager {

    GamePanel gp;
    public Tile[] tile;
    public int[][] mapTileNum;
    
    BufferedImage mapImage;

    BufferedImage mapImage;

    public TileManager(GamePanel gp) {
        this.gp = gp;
        tile = new Tile[6];
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];

        getTileImage();
<<<<<<< HEAD
        loadMap();
        loadMapImage();
    }

    public void getTileImage() {
        tile[0] = new Tile(java.awt.Color.darkGray, false);
        tile[1] = new Tile(java.awt.Color.gray, true);
        tile[2] = new Tile(java.awt.Color.green, false);
    }

    public void loadMapImage() {
        try {
            mapImage = ImageIO.read(
                    getClass().getResourceAsStream("/resources/map/battlemap.png"));
        } catch(IOException e) {
=======
        loadMap("/resources/maps/map.txt");
//        loadMapImage();
    }

    public void getTileImage() {
        try{
            tile[0] = new Tile();
            tile[0].image = ImageIO.read(getClass().getResourceAsStream("/resources/tiles/walls.png"));
            tile[0].collision = true;
            
            tile[1] = new Tile();
            tile[1].image = ImageIO.read(getClass().getResourceAsStream("/resources/tiles/bush.png"));
            
            tile[2] = new Tile();
            tile[2].image = ImageIO.read(getClass().getResourceAsStream("/resources/tiles/dirt.png"));
            
            tile[3] = new Tile();
            tile[3].image = ImageIO.read(getClass().getResourceAsStream("/resources/tiles/rocks_1.png"));
            tile[3].collision = true;
                
            tile[4] = new Tile();
            tile[4].image = ImageIO.read(getClass().getResourceAsStream("/resources/tiles/grass.png"));
            
            tile[5] = new Tile();
            tile[5].image = ImageIO.read(getClass().getResourceAsStream("/resources/tiles/water.png"));
        }catch(IOException e){
>>>>>>> c458095 (Adding enemy sprites and map)
            e.printStackTrace();
        }
    }

<<<<<<< HEAD
    public void loadMap() {
    int[][] map = {
        {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,2,1},
        {1,0,2,2,0,0,0,1,1,0,0,0,2,0,0,1},
        {1,0,0,0,0,2,0,0,0,0,2,0,0,0,0,1},
        {1,0,0,1,0,0,0,2,0,0,0,0,1,0,0,1},
        {1,0,0,1,0,0,0,0,0,2,0,0,1,0,0,1},
        {1,0,0,0,0,2,0,0,0,0,0,0,0,0,0,1},
        {1,0,2,0,0,0,0,1,1,0,2,0,0,2,0,1},
        {1,0,0,0,2,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,0,1,1,0,2,0,0,0,0,2,0,0,0,1},
        {1,0,0,0,0,0,0,0,2,0,0,0,0,0,0,1},
        {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
    };

    mapTileNum = map;
}

    public void draw(Graphics2D g2) {
        g2.drawImage(
                mapImage,
                0,
                0,
                gp.tileSize * gp.maxScreenCol,
                gp.tileSize * gp.maxScreenRow,
                null
        );
=======
//    public void loadMapImage() {
//        try {
//            mapImage = ImageIO.read(
//                    getClass().getResourceAsStream("/resources/map/battlemap.png"));
//        } catch(IOException e) {
//            e.printStackTrace();
//        }
//    }

    public void loadMap(String filePath) {
        try{
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            
            int col = 0;
            int row = 0;
            
            while(col < gp.maxWorldCol && row < gp.maxWorldRow){
                
                String line = br.readLine();
                
                while(col < gp.maxWorldCol){
                    String numbers[] = line.split(" ");
                    
                    int num = Integer.parseInt(numbers[col]);
                    
                    mapTileNum[col][row] = num;
                    col++;
                }
                if(col == gp.maxWorldCol){
                    col = 0;
                    row++;
                }
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {
        int worldCol = 0;
        int worldRow = 0;
        
        
        while(worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {
            int tileNum = mapTileNum[worldCol][worldRow];
            
            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;
            
            g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            worldCol++;

            
            if(worldCol == gp.maxWorldCol){
                worldCol = 0;
                worldRow++;

            }
        } 
>>>>>>> c458095 (Adding enemy sprites and map)
    }
}