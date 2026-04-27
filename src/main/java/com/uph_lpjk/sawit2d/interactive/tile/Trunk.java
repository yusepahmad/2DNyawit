package com.uph_lpjk.sawit2d.interactive.tile;

import com.uph_lpjk.sawit2d.controller.GamePanel;

public class Trunk extends InteractiveTile {

    GamePanel gp;

    public Trunk(GamePanel gp, int col, int row) {
        super(gp, col, row);
        this.gp = gp;
        setWorldX(this.gp.getTileSize() * col);
        setWorldY(this.gp.getTileSize() * row);

        down1 =
                setupImage(
                        "/tiles_interactive/trunk", this.gp.getTileSize(), this.gp.getTileSize());

        // Define a smaller, more realistic solid area (centered bottom)
        this.solidArea.x = 8;
        this.solidArea.y = 16;
        this.solidArea.width = 32;
        this.solidArea.height = 32;
        this.solidAreaDefaultX = this.solidArea.x;
        this.solidAreaDefaultY = this.solidArea.y;
    }
}
