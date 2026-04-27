package com.uph_lpjk.sawit2d.tile;

import com.uph_lpjk.sawit2d.controller.GamePanel;
import com.uph_lpjk.sawit2d.utility.AssetLoader;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileManager {

    private final GamePanel gp;
    private final int tileSize;
    private final int maxWorldCol;
    private final int maxWorldRow;

    protected Tile[] tile;
    protected int mapTileNum[][];
    protected int objectMapTileNum[][];
    private final AssetLoader assetLoader = new AssetLoader();

    public TileManager(GamePanel gp) {
        this.gp = gp;
        this.tileSize = this.gp.getTileSize();
        this.maxWorldCol = this.gp.getMaxWorldCol();
        this.maxWorldRow = this.gp.getMaxWorldRow();

        this.tile = new Tile[60];
        this.mapTileNum = new int[this.maxWorldCol][this.maxWorldRow];
        this.objectMapTileNum = new int[this.maxWorldCol][this.maxWorldRow];

        getTileImage();
        loadMap("farm_land");
    }

    public int getMapTileNum(int col, int row) {
        return this.mapTileNum[col][row];
    }

    public BufferedImage getTileImage(int index) {
        if (index < 0 || index >= this.tile.length || this.tile[index] == null) {
            return null;
        }
        return this.tile[index].image;
    }

    public boolean getTileCollision(int i) {
        return this.tile[i].collision;
    }

    private void getTileImage() {
        setup(0, "tile", "grass00", false);
        setup(1, "tile", "grass00", false);
        setup(2, "tile", "grass00", false);
        setup(3, "tile", "grass00", false);
        setup(4, "tile", "grass00", false);
        setup(5, "tile", "grass00", false);
        setup(6, "tile", "grass00", false);
        setup(7, "tile", "grass00", false);
        setup(8, "tile", "grass00", false);
        setup(9, "tile", "grass00", false);
        setup(10, "tile", "grass00", false);
        setup(11, "tile", "grass01", false);
        setup(12, "tile", "water00", true);
        setup(13, "tile", "water01", true);
        setup(14, "tile", "water02", true);
        setup(15, "tile", "water03", true);
        setup(16, "tile", "water04", true);
        setup(17, "tile", "water05", true);
        setup(18, "tile", "water06", true);
        setup(19, "tile", "water07", true);
        setup(20, "tile", "water08", true);
        setup(21, "tile", "water09", true);
        setup(22, "tile", "water10", true);
        setup(23, "tile", "water11", true);
        setup(24, "tile", "water12", true);
        setup(25, "tile", "water13", true);
        setup(26, "tile", "road00", false);
        setup(27, "tile", "road01", false);
        setup(28, "tile", "road02", false);
        setup(29, "tile", "road03", false);
        setup(30, "tile", "road04", false);
        setup(31, "tile", "road05", false);
        setup(32, "tile", "road06", false);
        setup(33, "tile", "road07", false);
        setup(34, "tile", "road08", false);
        setup(35, "tile", "road09", false);
        setup(36, "tile", "road10", false);
        setup(37, "tile", "road11", false);
        setup(38, "tile", "road12", false);
        setup(39, "tile", "earth", false);
        setup(40, "tile", "wall", true);
        setup(41, "tile", "tree", true);
        setup(42, "tile", "hut", false);
        setup(43, "tile", "floor01", false);
        setup(44, "tile", "table01", true);

        // FARM SPECIFIC TILES
        setup(45, "/tile/earth", false);
        setup(46, "/sawit/sawit-fase-1", false);
        setup(47, "/sawit/sawit-fase-2", false);
        setup(48, "/sawit/sawit-panen", false);
        setup(49, "/tile/kebakaran", false);
        setup(50, "/fire/api-1", false);
        setup(51, "/fire/api-3", false);
        setup(52, "/fire/api-5", false);
        setup(53, "/tile/after-banjir", false);
        setup(54, "/tile/kebakaran-2", false);
    }

    private void setup(int index, String folder, String imageName, boolean collision) {
        setup(index, "/" + folder + "/" + imageName, collision);
    }

    private void setup(int index, String imagePath, boolean collision) {
        this.tile[index] = new Tile();
        this.tile[index].image =
                this.assetLoader.loadImage(this.tileSize, this.tileSize, imagePath);
        this.tile[index].collision = collision;
    }

    public void loadMap(String mapName) {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/maps/" + mapName + ".txt");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            int col = 0;
            int row = 0;

            while (row < this.maxWorldRow) {
                String line = bufferedReader.readLine();
                if (line == null) break;

                String numbers[] = line.split(" ");
                col = 0;
                while (col < this.maxWorldCol && col < numbers.length) {
                    int num = Integer.parseInt(numbers[col]);
                    this.mapTileNum[col][row] = num;
                    col++;
                }
                row++;
            }
            bufferedReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {
        int worldCol = 0;
        int worldRow = 0;

        int cameraX = gp.getCameraX();
        int cameraY = gp.getCameraY();
        int screenWidth = gp.getScreenWidth();
        int screenHeight = gp.getScreenHeight();

        while (worldCol < this.maxWorldCol && worldRow < this.maxWorldRow) {
            int tileNum = this.mapTileNum[worldCol][worldRow];

            int worldX = worldCol * this.tileSize;
            int worldY = worldRow * this.tileSize;

            int screenX = worldX - cameraX;
            int screenY = worldY - cameraY;

            if (worldX + this.tileSize > cameraX
                    && worldX - this.tileSize < cameraX + screenWidth
                    && worldY + this.tileSize > cameraY
                    && worldY - this.tileSize < cameraY + screenHeight) {
                g2.drawImage(this.tile[tileNum].image, screenX, screenY, null);
            }
            worldCol++;

            if (worldCol == this.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }
    }

    public void drawObjectLayer(Graphics2D g2) {
        int cameraX = gp.getCameraX();
        int cameraY = gp.getCameraY();
        int screenWidth = gp.getScreenWidth();
        int screenHeight = gp.getScreenHeight();

        for (int row = 0; row < this.maxWorldRow; row++) {
            for (int col = 0; col < this.maxWorldCol; col++) {
                int tileNum = this.objectMapTileNum[col][row];
                if (tileNum == 0) {
                    continue;
                }

                int worldX = col * this.tileSize;
                int worldY = row * this.tileSize;
                if (worldX + this.tileSize < cameraX
                        || worldX - this.tileSize > cameraX + screenWidth
                        || worldY + this.tileSize < cameraY
                        || worldY - this.tileSize > cameraY + screenHeight) {
                    continue;
                }

                int screenX = worldX - cameraX;
                int screenY = worldY - cameraY;
                g2.drawImage(this.tile[tileNum].image, screenX, screenY, null);
            }
        }
    }
}
