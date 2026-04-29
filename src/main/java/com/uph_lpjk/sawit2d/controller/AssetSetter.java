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

        int farmAreaStart = 8;
        int farmAreaEnd = 41;

        // Pola biner 32-kolom untuk setiap baris dalam farm (kolom 9-40).
        // 1 = ada pohon, 0 = tanah terbuka. Membentuk pola hutan organik.
        // Baris indeks 0 = row 9, dst.
        // Each long encodes one row of the 32-col inner farm area (cols 9-40).
        // Bit 31 = leftmost col, bit 0 = rightmost col. 1=tree, 0=open ground.
        long[] forestPattern = {
            // row  9
            0b11101101111011101110011111101101L,
            // row 10
            0b11001111101100111100011101001111L,
            // row 11
            0b01111100111100001111111001100001L,
            // row 12
            0b11100011100111100011100001110000L,
            // row 13
            0b00111111101111011111110000011111L,
            // row 14
            0b11001100011111110110000111100011L,
            // row 15
            0b11111110000111111100011100000011L,
            // row 16
            0b00001111111100001111001111111100L,
            // row 17
            0b11100001110001111111011110000111L,
            // row 18
            0b01111111001111000001111111101110L,
            // row 19
            0b10100011101111111000001110000011L,
            // row 20
            0b00011100001111001111110000001110L,
            // row 21
            0b11110000000001111110000011110001L,
            // row 22
            0b00111111101100000111111101110111L,
            // row 23
            0b11100000001111111000000110000000L,
            // row 24
            0b00000001110000000001111000000011L,
            // row 25
            0b11111100000001111111001111111000L,
            // row 26
            0b00000001111111100000011110000000L,
            // row 27
            0b11111110000000001111100001111111L,
            // row 28
            0b00000011111111100000001110000000L,
            // row 29
            0b11111000001110011111100000111111L,
            // row 30
            0b00011111110000000000111111100000L,
            // row 31
            0b11100000111111100011100000000011L,
            // row 32
            0b00001111110000001110000011111100L,
        };

        for (int row = farmAreaStart + 1; row < farmAreaEnd; row++) {
            int patternRow = row - (farmAreaStart + 1);
            long pattern =
                    (patternRow < forestPattern.length) ? forestPattern[patternRow] : 0xFFFFFFFFL;

            for (int col = farmAreaStart + 1; col < farmAreaEnd; col++) {
                int colIndex = col - (farmAreaStart + 1); // 0-31
                // Bit check: bit 31 = col 0 (leftmost), bit 0 = col 31 (rightmost)
                boolean hasTree = ((pattern >> (31 - colIndex)) & 1L) == 1L;

                if (hasTree && i < gp.getInteractiveTile().length) {
                    this.gp.setInteractiveTile(i, new DryTree(gp, col, row));
                    i++;
                }
            }
        }
    }
}
