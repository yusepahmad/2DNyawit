package com.uph_lpjk.sawit2d.tile;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import com.uph_lpjk.sawit2d.controller.GamePanel;
import com.uph_lpjk.sawit2d.utility.UtilityTools;

public class TileManager {
    
    final private GamePanel gp;
    final private int tileSize;
    final private int maxWorldCol;
    final private int maxWorldRow;

    protected Tile[] tile;
    protected int mapTileNum[][];

    public TileManager(GamePanel gp) {
        this.gp = gp;
        this.tileSize = this.gp.getTileSize();
        this.maxWorldCol = this.gp.getMaxWorldCol();
        this.maxWorldRow = this.gp.getMaxWorldRow();

        this.tile = new Tile[50];
        this.mapTileNum = new int[this.maxWorldCol][this.maxWorldRow];

        getTileImage();
        loadMap("sawit_land");
    }

    public int getMapTileNum(int col, int row) {
        return this.mapTileNum[col][row];
    }

    public boolean getTileCollision(int i) {
        return this.tile[i].collision;
    }
    
    private void getTileImage() {
        setup(0, "grass00", false);
        setup(1, "grass00", false);
        setup(2, "grass00", false);
        setup(3, "grass00", false);
        setup(4, "grass00", false);
        setup(5, "grass00", false);
        setup(6, "grass00", false);
        setup(7, "grass00", false);
        setup(8, "grass00", false);
        setup(9, "grass00", false);
        setup(10, "grass00", false);
        setup(11, "grass01", false);
        setup(12, "water00", true);
        setup(13, "water01", true);
        setup(14, "water02", true);
        setup(15, "water03", true);
        setup(16, "water04", true);
        setup(17, "water05", true);
        setup(18, "water06", true);
        setup(19, "water07", true);
        setup(20, "water08", true);
        setup(21, "water09", true);
        setup(22, "water10", true);
        setup(23, "water11", true);
        setup(24, "water12", true);
        setup(25, "water13", true);
        setup(26, "road00", false);
        setup(27, "road01", false);
        setup(28, "road02", false);
        setup(29, "road03", false);
        setup(30, "road04", false);
        setup(31, "road05", false);
        setup(32, "road06", false);
        setup(33, "road07", false);
        setup(34, "road08", false);
        setup(35, "road09", false);
        setup(36, "road10", false);
        setup(37, "road11", false);
        setup(38, "road12", false);
        setup(39, "earth", false);
        setup(40, "wall", true);
        setup(41, "tree", true);
        setup(42, "hut", false);
        setup(43, "floor01", false);
        setup(44, "table01", true);
    }

    private void setup(int index, String imageName, boolean collision) {
        setup(index, "tile", imageName, collision);
    }

    private void setup(int index, String folder, String imageName, boolean collision) {
        UtilityTools uTools = new UtilityTools();

        try {
            this.tile[index] = new Tile();
            this.tile[index].image = ImageIO.read(getClass().getResourceAsStream("/" + folder + "/" + imageName + ".png"));
            this.tile[index].image = uTools.scaledImage(tile[index].image, this.tileSize, this.tileSize);
            this.tile[index].collision = collision;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadMap(String mapName) {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/maps/" + mapName + ".txt");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            int col = 0;
            int row = 0;

            while(row < this.maxWorldRow) {
                String line = bufferedReader.readLine();
                if (line == null) break;

                String numbers[] = line.split(" ");
                col = 0;
                while(col < this.maxWorldCol && col < numbers.length) {
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

        while(worldCol < this.maxWorldCol && worldRow < this.maxWorldRow) {
            int tileNum = this.mapTileNum[worldCol][worldRow];

            int worldX = worldCol * this.tileSize;
            int worldY = worldRow * this.tileSize;

            int screenX = worldX - cameraX;
            int screenY = worldY - cameraY;

            if(
                worldX + this.tileSize > cameraX &&
                worldX - this.tileSize < cameraX + screenWidth &&
                worldY + this.tileSize > cameraY &&
                worldY - this.tileSize < cameraY + screenHeight
            ) {
                g2.drawImage(this.tile[tileNum].image, screenX, screenY, null);
            }
            worldCol++;

            if(worldCol == this.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }
    }
}
