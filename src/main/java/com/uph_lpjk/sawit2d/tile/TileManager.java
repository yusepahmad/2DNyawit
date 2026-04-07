package com.uph_lpjk.sawit2d.tile;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.uph_lpjk.sawit2d.controller.GamePanel;
import com.uph_lpjk.sawit2d.utility.AssetLoader;

public class TileManager {
    
    final private GamePanel gp;
    final private int tileSize;
    final private int maxWorldCol;
    final private int maxWorldRow;

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
        setupCustom(45, false, "/assets/home-top-left-1.png", "assets/home-top-left-1.png", "../assets/home-top-left-1.png");
        setupCustom(46, false, "/assets/home-top-right-2.png", "assets/home-top-right-2.png", "../assets/home-top-right-2.png");
        setupCustom(47, false, "/assets/home-bottom-left-3.png", "assets/home-bottom-left-3.png", "../assets/home-bottom-left-3.png");
        setupCustom(48, false, "/assets/home-bottom-right-4.png", "assets/home-bottom-right-4.png", "../assets/home-bottom-right-4.png");
        setupCustom(49, false, "/assets/garage-top-left-1.png", "assets/garage-top-left-1.png", "../assets/garage-top-left-1.png");
        setupCustom(50, false, "/assets/garage-top-right-2.png", "assets/garage-top-right-2.png", "../assets/garage-top-right-2.png");
        setupCustom(51, false, "/assets/garage-bottom-left-3.png", "assets/garage-bottom-left-3.png", "../assets/garage-bottom-left-3.png");
        setupCustom(52, false, "/assets/garage-bottom-right-4.png", "assets/garage-bottom-right-4.png", "../assets/garage-bottom-right-4.png");
    }

    private void setup(int index, String imageName, boolean collision) {
        setup(index, "tile", imageName, collision);
    }

    private void setup(int index, String folder, String imageName, boolean collision) {
        setupCustom(index, collision, "/" + folder + "/" + imageName + ".png", folder + "/" + imageName + ".png", "../" + folder + "/" + imageName + ".png");
    }

    private void setupCustom(int index, boolean collision, String... candidates) {
        this.tile[index] = new Tile();
        this.tile[index].image = this.assetLoader.loadImage(this.tileSize, this.tileSize, candidates);
        this.tile[index].collision = collision;
    }

    public void loadMap(String mapName) {
        if ("farm_land".equals(mapName)) {
            createFarmMap();
            return;
        }

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

    private void createFarmMap() {
        java.util.Random random = new java.util.Random(42);

        for (int row = 0; row < this.maxWorldRow; row++) {
            for (int col = 0; col < this.maxWorldCol; col++) {
                this.mapTileNum[col][row] = 10;
                this.objectMapTileNum[col][row] = 0;
            }
        }

        // Outer tree belt and decoration zone.
        for (int row = 0; row < this.maxWorldRow; row++) {
            for (int col = 0; col < this.maxWorldCol; col++) {
                int edgeDistance = Math.min(Math.min(row, this.maxWorldRow - 1 - row), Math.min(col, this.maxWorldCol - 1 - col));
                boolean outerBand = edgeDistance < 7;
                double treeChance = edgeDistance < 3 ? 0.84 : edgeDistance < 5 ? 0.68 : 0.46;
                if (outerBand && random.nextDouble() < treeChance) {
                    this.objectMapTileNum[col][row] = 41;
                }
            }
        }

        // Natural water feature near the top edge to break the flatness.
        paintWaterStream(random);

        // Extra sparse trees around the upper-middle area.
        scatterTrees(random, 16, 0, 34, 8, 0.24);

        // Large farming plot in the middle of the world.
        for (int row = 8; row <= 41; row++) {
            for (int col = 8; col <= 41; col++) {
                this.mapTileNum[col][row] = 39;
            }
        }

        // Walkable dirt border around the farm.
        for (int row = 7; row <= 42; row++) {
            this.mapTileNum[7][row] = 26;
            this.mapTileNum[42][row] = 26;
        }
        for (int col = 7; col <= 42; col++) {
            this.mapTileNum[col][7] = 26;
            this.mapTileNum[col][42] = 26;
        }

        // House decoration in the upper-left clearing.
        this.objectMapTileNum[2][2] = 45;
        this.objectMapTileNum[3][2] = 46;
        this.objectMapTileNum[2][3] = 47;
        this.objectMapTileNum[3][3] = 48;

        // Garage decoration in the upper-right clearing.
        this.objectMapTileNum[45][2] = 49;
        this.objectMapTileNum[46][2] = 50;
        this.objectMapTileNum[45][3] = 51;
        this.objectMapTileNum[46][3] = 52;

        // Keep small clean paths around the buildings.
        for (int row = 1; row <= 5; row++) {
            for (int col = 1; col <= 6; col++) {
                if (col > 1 && col < 5 && row > 1 && row < 5) {
                    continue;
                }
                if (this.objectMapTileNum[col][row] == 0 && random.nextDouble() < 0.30) {
                    this.objectMapTileNum[col][row] = 41;
                }
            }
        }
        for (int row = 1; row <= 5; row++) {
            for (int col = 43; col <= 48; col++) {
                if (col > 44 && col < 47 && row > 1 && row < 5) {
                    continue;
                }
                if (this.objectMapTileNum[col][row] == 0 && random.nextDouble() < 0.28) {
                    this.objectMapTileNum[col][row] = 41;
                }
            }
        }
    }

    private void paintWaterStream(java.util.Random random) {
        int[] riverCols = {3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23, 25, 27, 29, 31, 33, 35, 37};
        int[] riverRows = {4, 4, 3, 3, 2, 2, 3, 4, 4, 3, 3, 2, 2, 3, 4, 4, 3, 3};

        for (int i = 0; i < riverCols.length; i++) {
            int col = riverCols[i];
            int row = riverRows[i];
            paintWaterBlob(random, col, row, 1, 1);
            if (i % 4 == 0) {
                paintWaterBlob(random, col + 1, row + 1, 1, 1);
            }
        }

        // Small inlet near the left top to make the stream feel natural.
        paintWaterBlob(random, 2, 5, 2, 1);
    }

    private void paintWaterBlob(java.util.Random random, int centerCol, int centerRow, int radiusCol, int radiusRow) {
        for (int row = centerRow - radiusRow - 1; row <= centerRow + radiusRow + 1; row++) {
            for (int col = centerCol - radiusCol - 1; col <= centerCol + radiusCol + 1; col++) {
                if (row < 0 || col < 0 || row >= this.maxWorldRow || col >= this.maxWorldCol) {
                    continue;
                }
                double dx = (col - centerCol) / (double) Math.max(1, radiusCol);
                double dy = (row - centerRow) / (double) Math.max(1, radiusRow);
                double distance = dx * dx + dy * dy;
                if (distance <= 1.0) {
                    this.mapTileNum[col][row] = 12 + random.nextInt(14);
                    this.objectMapTileNum[col][row] = 0;
                } else if (distance <= 1.35 && random.nextDouble() < 0.35) {
                    this.mapTileNum[col][row] = 12 + random.nextInt(14);
                    this.objectMapTileNum[col][row] = 41;
                }
            }
        }
    }

    private void scatterTrees(java.util.Random random, int startCol, int startRow, int endCol, int endRow, double chance) {
        for (int row = startRow; row <= endRow; row++) {
            for (int col = startCol; col <= endCol; col++) {
                if (row < 0 || col < 0 || row >= this.maxWorldRow || col >= this.maxWorldCol) {
                    continue;
                }
                if (this.mapTileNum[col][row] == 12 || this.mapTileNum[col][row] == 13 || this.mapTileNum[col][row] == 14
                        || this.mapTileNum[col][row] == 15 || this.mapTileNum[col][row] == 16 || this.mapTileNum[col][row] == 17
                        || this.mapTileNum[col][row] == 18 || this.mapTileNum[col][row] == 19 || this.mapTileNum[col][row] == 20
                        || this.mapTileNum[col][row] == 21 || this.mapTileNum[col][row] == 22 || this.mapTileNum[col][row] == 23
                        || this.mapTileNum[col][row] == 24 || this.mapTileNum[col][row] == 25) {
                    continue;
                }
                if (this.objectMapTileNum[col][row] == 0 && random.nextDouble() < chance) {
                    this.objectMapTileNum[col][row] = 41;
                }
            }
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
                if (
                    worldX + this.tileSize < cameraX ||
                    worldX - this.tileSize > cameraX + screenWidth ||
                    worldY + this.tileSize < cameraY ||
                    worldY - this.tileSize > cameraY + screenHeight
                ) {
                    continue;
                }

                int screenX = worldX - cameraX;
                int screenY = worldY - cameraY;
                g2.drawImage(this.tile[tileNum].image, screenX, screenY, null);
            }
        }
    }
}
