/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tile;

import game.GamePanel;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author nicol
 */
public class TileManager {

    GamePanel gp;
    public Tile[] tile;
    public int[][] mapTileNum;

    public TileManager(GamePanel gp) {
        this.gp = gp;
        tile = new Tile[3];
        mapTileNum = new int[12][16];

        getTileImage();
        loadMap();
    }

    public void getTileImage() {
        tile[0] = new Tile(Color.darkGray, false); // floor
        tile[1] = new Tile(Color.gray, true);      // wall
        tile[2] = new Tile(Color.green, false);    // grass
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
        for(int row = 0; row < 12; row++) {
            for(int col = 0; col < 16; col++) {
                int tileNum = mapTileNum[row][col];

                g2.setColor(tile[tileNum].color);
                g2.fillRect(col * 48, row * 48, 48, 48);
            }
        }
    }
}
