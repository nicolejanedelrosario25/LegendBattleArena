package tile;

import game.GamePanel;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.imageio.ImageIO;

public class TileManager {

    GamePanel gp;
    public Tile[] tile;
    public int[][] mapTileNum;

    public TileManager(GamePanel gp) {
        this.gp = gp;
        tile = new Tile[6];
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];
        getTileImage();
        loadMap("/resources/maps/map.txt");
    }

    public void getTileImage() {
        try {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadMap(String filePath) {
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;

            while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
                String line = br.readLine();
                if (line == null) break;

                String[] numbers = line.trim().split("\\s+");
                while (col < gp.maxWorldCol && col < numbers.length) {
                    mapTileNum[col][row] = Integer.parseInt(numbers[col]);
                    col++;
                }
                if (col == gp.maxWorldCol) {
                    col = 0;
                    row++;
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {
        if (gp.player == null) return;

        int worldCol = 0;
        int worldRow = 0;

        // FIXED: Pre-calculate screen bounds once per frame instead of per-tile
        int playerWorldX   = gp.player.worldX;
        int playerWorldY  = gp.player.worldY;
        int screenX    = gp.player.screenX;
        int screenY = gp.player.screenY;

        //world pixel coords of screen edges
        int leftEdge = playerWorldX - screenX;
        int topEdge = playerWorldY - screenY;
        int rightEdge = leftEdge + gp.screenWidth;
        int bottomEdge = topEdge + gp.screenHeight;
        
         // Convert to tile indices, clamped to map bounds
        int startCol = Math.max(0, leftEdge   / gp.tileSize);
        int startRow = Math.max(0, topEdge    / gp.tileSize);
        int endCol   = Math.min(gp.maxWorldCol - 1, rightEdge  / gp.tileSize);
        int endRow   = Math.min(gp.maxWorldRow - 1, bottomEdge / gp.tileSize);
        
        for(int row = startRow; row <= endRow; row++) {
            for(int col = startCol; col <= endCol; col++){
                int tileNum = mapTileNum[col][row];
                int worldTileX = col * gp.tileSize;
                int worldTileY = row * gp.tileSize;
                int drawX = worldTileX - playerWorldX + screenX;
                int drawY = worldTileY - playerWorldY + screenY;
                
                g2.drawImage(tile[tileNum].image, drawX, drawY, gp.tileSize, gp.tileSize, null);

            }             
        }
    }
}