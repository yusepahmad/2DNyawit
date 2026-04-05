package com.uph_lpjk.sawit2d.controller;

import com.uph_lpjk.sawit2d.interactive.tile.DryTree;
import com.uph_lpjk.sawit2d.object.ObjAxe;
import com.uph_lpjk.sawit2d.object.ObjGold;

public class AssetSetter {
    
    final private GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObject() {
        int i = 0;
        this.gp.setObject(i, 
            new ObjAxe(this.gp), 
            this.gp.getTileSize()*41, 
            this.gp.getTileSize()*21);
        i++;
        this.gp.setObject(i, 
            new ObjGold(this.gp), 
            this.gp.getTileSize()*41, 
            this.gp.getTileSize()*15);
        i++;
    }

    public void setInteractiveTile() {
        int i = 0;
        java.util.Random random = new java.util.Random();
        
        int playerStartX = 46;
        int playerStartY = 18;
        int clearRadius = 7;

        for(int row = 0; row < gp.getMaxWorldRow(); row++) {
            for(int col = 0; col < gp.getMaxWorldCol(); col++) {
                
                // 1. ZONA AMAN (RUMAH)
                double distToPlayer = Math.sqrt(Math.pow(col - playerStartX, 2) + Math.pow(row - playerStartY, 2));
                if(distToPlayer < clearRadius) continue;

                // 2. CEK TILE VALID (Hanya di Rumput/Tanah/Tile 45)
                int tileNum = gp.getMapTileNum(col, row);
                if(tileNum == 10 || tileNum == 11 || tileNum == 39 || tileNum == 45) {
                    
                    // 3. LOGIKA KLASTER (Noise sederhana menggunakan Sin/Cos)
                    // Mengatur angka di dalam sin/cos mengubah "ukuran" kelompok pohon
                    double clusterNoise = Math.sin(col * 0.4) * Math.cos(row * 0.4);
                    
                    // clusterNoise > -0.2 artinya pohon akan tumbuh di sekitar 60% daratan
                    // Ini menciptakan pulau-pulau hutan dan area rumput terbuka yang luas
                    if(clusterNoise > -0.2) {
                        
                        // 4. KERAPATAN BERHIMPITAN (Probabilitas di dalam klaster)
                        // 90% pohon akan tumbuh di dalam area klaster agar terlihat berhimpitan
                        if(random.nextInt(100) < 90) {
                            if(i < 900) { // Batas array iTile baru
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
