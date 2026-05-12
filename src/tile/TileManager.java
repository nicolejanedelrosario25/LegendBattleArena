/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tile;

import game.GamePanel;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
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

    public TileManager(GamePanel gp) {
        this.gp = gp;
        tile = new Tile[3];
        mapTileNum = new int[12][16];

        getTileImage();
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
            e.printStackTrace();
        }
    }

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
    }
}