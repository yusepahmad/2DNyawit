package com.uph_lpjk.sawit2d.interactive.tile;

import com.uph_lpjk.sawit2d.controller.GamePanel;

public class Trunk extends InteractiveTile {

    GamePanel gp;

    public int daysOld = 0;

    public Trunk(GamePanel gp, int col, int row) {
        super(gp, col, row);
        this.gp = gp;
        setWorldX(this.gp.getTileSize() * col);
        setWorldY(this.gp.getTileSize() * row);

        down1 =
                setupImage(
                        "/tiles_interactive/trunk", this.gp.getTileSize(), this.gp.getTileSize());

        this.collision = false;

        // Define a smaller, more realistic solid area (centered bottom)
        this.solidArea.x = 0;
        this.solidArea.y = 0;
        this.solidArea.width = 0;
        this.solidArea.height = 0;
        this.solidAreaDefaultX = this.solidArea.x;
        this.solidAreaDefaultY = this.solidArea.y;
    }
}
