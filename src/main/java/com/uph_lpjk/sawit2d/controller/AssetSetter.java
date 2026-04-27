package com.uph_lpjk.sawit2d.controller;

import com.uph_lpjk.sawit2d.interactive.tile.DryTree;

public class AssetSetter {

    private final GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObject() {
        int i = 0;

        // HOME (Position: Col 41, Row 2)
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

        // GARAGE (Position: Col 43, Row 2)
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

        // MARKET (Position: Col 10, Row 3)
        this.gp.setObject(
                i,
                new com.uph_lpjk.sawit2d.object.ObjMarketTopLeft(this.gp),
                this.gp.getTileSize() * 10,
                this.gp.getTileSize() * 3);
        i++;
        this.gp.setObject(
                i,
                new com.uph_lpjk.sawit2d.object.ObjMarketTopRight(this.gp),
                this.gp.getTileSize() * 11,
                this.gp.getTileSize() * 3);
        i++;
        this.gp.setObject(
                i,
                new com.uph_lpjk.sawit2d.object.ObjMarketBottomLeft(this.gp),
                this.gp.getTileSize() * 10,
                this.gp.getTileSize() * 4);
        i++;
        this.gp.setObject(
                i,
                new com.uph_lpjk.sawit2d.object.ObjMarketBottomRight(this.gp),
                this.gp.getTileSize() * 11,
                this.gp.getTileSize() * 4);
        i++;
    }

    public void setInteractiveTile() {
        int i = 0;

        // Populate the entire Farm Grid area with DryTrees
        int farmAreaStart = 8;
        int farmAreaEnd = 41;

        for (int row = farmAreaStart; row <= farmAreaEnd; row++) {
            for (int col = farmAreaStart; col <= farmAreaEnd; col++) {

                // Skip lane tiles (the outer border of the 34x34 farm grid)
                if (col == farmAreaStart
                        || row == farmAreaStart
                        || col == farmAreaEnd
                        || row == farmAreaEnd) {
                    continue;
                }

                if (i < gp.getInteractiveTile().length) {
                    this.gp.setInteractiveTile(i, new DryTree(gp, col, row));
                    i++;
                }
            }
        }
    }
}
