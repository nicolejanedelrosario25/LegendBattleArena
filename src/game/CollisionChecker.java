/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game;

import entity.Player;

/**
 *
 * @author nicol
 */
public class CollisionChecker {

    GamePanel gp;

    public CollisionChecker(GamePanel gp) {
    this.gp = gp;
}

    public boolean checkTile(Player player, int nextX, int nextY) {

        int leftCol = nextX / gp.tileSize;
        int rightCol = (nextX + 40) / gp.tileSize;

        int topRow = nextY / gp.tileSize;
        int bottomRow = (nextY + 40) / gp.tileSize;

        // SCREEN LIMITS
        if(leftCol < 0 || rightCol >= gp.maxScreenCol ||
           topRow < 0 || bottomRow >= gp.maxScreenRow) {

            return true;
        }

        int tile1 = gp.tileM.mapTileNum[topRow][leftCol];
        int tile2 = gp.tileM.mapTileNum[topRow][rightCol];

        int tile3 = gp.tileM.mapTileNum[bottomRow][leftCol];
        int tile4 = gp.tileM.mapTileNum[bottomRow][rightCol];

        return gp.tileM.tile[tile1].collision ||
               gp.tileM.tile[tile2].collision ||
               gp.tileM.tile[tile3].collision ||
               gp.tileM.tile[tile4].collision;
    }
}