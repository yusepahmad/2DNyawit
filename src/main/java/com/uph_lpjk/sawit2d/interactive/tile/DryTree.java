package com.uph_lpjk.sawit2d.interactive.tile;

import com.uph_lpjk.sawit2d.controller.GamePanel;

public class DryTree extends InteractiveTile {
    
    GamePanel gp;

    public DryTree(GamePanel gp, int col, int row) {
        super(gp, col, row);
        this.gp = gp;
        setWorldX(this.gp.getTileSize() * col);
        setWorldY(this.gp.getTileSize() * row);

        down1 = setup("/tiles_interactive/drytree", this.gp.getTileSize(), this.gp.getTileSize());
        destructible = true;
        setLife(3);
    }


}
