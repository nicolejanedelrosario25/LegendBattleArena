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

        int entityLeftWorldX = nextX + player.solidArea.x;
        int entityRightWorldX = nextX + player.solidArea.x + player.solidArea.width - 1;
        int entityTopWorldY = nextY + player.solidArea.y;
        int entityBottomWorldY = nextY + player.solidArea.y + player.solidArea.height - 1;

        int leftCol = entityLeftWorldX / gp.tileSize;
        int rightCol = entityRightWorldX / gp.tileSize;
        int topRow = entityTopWorldY / gp.tileSize;
        int bottomRow = entityBottomWorldY / gp.tileSize;

        if(leftCol < 0 || rightCol >= gp.maxWorldCol ||
           topRow < 0 || bottomRow >= gp.maxWorldRow) {
            return true;
        }

        int tile1 = gp.tileM.mapTileNum[leftCol][topRow];
        int tile2 = gp.tileM.mapTileNum[rightCol][topRow];
        int tile3 = gp.tileM.mapTileNum[leftCol][bottomRow];
        int tile4 = gp.tileM.mapTileNum[rightCol][bottomRow];

        return gp.tileM.tile[tile1].collision ||
               gp.tileM.tile[tile2].collision ||
               gp.tileM.tile[tile3].collision ||
               gp.tileM.tile[tile4].collision;
    }
}