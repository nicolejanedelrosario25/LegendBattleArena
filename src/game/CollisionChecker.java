/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game;

import entity.Entity;
//import entity.Player;

/**
 *
 * @author nicol
 */
public class CollisionChecker {

    GamePanel gp;

    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    public boolean checkTile(Entity entity, int nextX, int nextY) {

        int entityLeftWorldX = nextX + entity.solidArea.x;
        int entityRightWorldX = nextX + entity.solidArea.x + entity.solidArea.width - 1;
        int entityTopWorldY = nextY + entity.solidArea.y;
        int entityBottomWorldY = nextY + entity.solidArea.y + entity.solidArea.height - 1;

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