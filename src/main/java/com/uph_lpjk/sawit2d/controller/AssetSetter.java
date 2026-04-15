package com.uph_lpjk.sawit2d.controller;

import com.uph_lpjk.sawit2d.interactive.tile.DryTree;

import java.util.Random;

public class AssetSetter {

    private final GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObject() {
        int i = 0;

        // HOME (Position: Col 2, Row 2)
        this.gp.setObject(
                i,
                new com.uph_lpjk.sawit2d.object.ObjHomeTopLeft(this.gp),
                this.gp.getTileSize() * 41,
                this.gp.getTileSize() * 2);
        i++;
        this.gp.setObject(
                i,
                new com.uph_lpjk.sawit2d.object.ObjHomeTopRight(this.gp),
                this.gp.getTileSize() * 42,
                this.gp.getTileSize() * 2);
        i++;
        this.gp.setObject(
                i,
                new com.uph_lpjk.sawit2d.object.ObjHomeBottomLeft(this.gp),
                this.gp.getTileSize() * 41,
                this.gp.getTileSize() * 3);
        i++;
        this.gp.setObject(
                i,
                new com.uph_lpjk.sawit2d.object.ObjHomeBottomRight(this.gp),
                this.gp.getTileSize() * 42,
                this.gp.getTileSize() * 3);
        i++;

        // GARAGE (Position: Col 40, Row 2)
        this.gp.setObject(
                i,
                new com.uph_lpjk.sawit2d.object.ObjGarageTopLeft(this.gp),
                this.gp.getTileSize() * 43,
                this.gp.getTileSize() * 2);
        i++;
        this.gp.setObject(
                i,
                new com.uph_lpjk.sawit2d.object.ObjGarageTopRight(this.gp),
                this.gp.getTileSize() * 44,
                this.gp.getTileSize() * 2);
        i++;
        this.gp.setObject(
                i,
                new com.uph_lpjk.sawit2d.object.ObjGarageBottomLeft(this.gp),
                this.gp.getTileSize() * 43,
                this.gp.getTileSize() * 3);
        i++;
        this.gp.setObject(
                i,
                new com.uph_lpjk.sawit2d.object.ObjGarageBottomRight(this.gp),
                this.gp.getTileSize() * 44,
                this.gp.getTileSize() * 3);
        i++;
    }

    public void setInteractiveTile() {
        int i = 0;
        Random random = new java.util.Random();

        // Define Safe Zone (Building areas in Branch B)
        int houseX = 2, houseY = 2;
        int garageX = 45, garageY = 2;
        int farmAreaStart = 7, farmAreaEnd = 42;

        for (int row = 0; row < gp.getMaxWorldRow(); row++) {
            for (int col = 0; col < gp.getMaxWorldCol(); col++) {

                // 1. AVOID BUILDINGS & FARM GRID
                if (Math.abs(col - houseX) < 5 && Math.abs(row - houseY) < 5) continue;
                if (Math.abs(col - garageX) < 5 && Math.abs(row - garageY) < 5) continue;
                if (col >= farmAreaStart
                        && col <= farmAreaEnd
                        && row >= farmAreaStart
                        && row <= farmAreaEnd) continue;

                // 2. ONLY ON VALID TILES (Grass/Earth)
                int tileNum = gp.getMapTileNum(col, row);
                if (tileNum == 10 || tileNum == 11 || tileNum == 39) {

                    // 3. CLUSTER LOGIC (Sin/Cos Noise)
                    double clusterNoise = Math.sin(col * 0.4) * Math.cos(row * 0.4);

                    if (clusterNoise > -0.1) {
                        if (random.nextInt(100) < 85) {
                            if (i < 900) {
                                this.gp.setInteractiveTile(i, new DryTree(gp, col, row));
                                i++;
                            }
                        }
                    }
                }
            }
        }
    }
}
