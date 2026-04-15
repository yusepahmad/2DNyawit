package com.uph_lpjk.sawit2d.entity;

import com.uph_lpjk.sawit2d.controller.GamePanel;

import java.awt.Rectangle;

public class Home extends Entity {

    private final GamePanel gp;
    private final int tileSize;
    private final int screenWidth, screenHeight;

    protected final int screenX;
    protected final int screenY;

    public Home(GamePanel gp) {
        super(gp);
        this.gp = gp;
        this.tileSize = getTileSize();
        this.screenWidth = getScreenWidth();
        this.screenHeight = getScreenHeight();

        this.screenX = this.screenWidth / 2 - (this.tileSize / 2);
        this.screenY = this.screenHeight / 2 - (this.tileSize / 2);

        // SOLID AREA
        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 32;
    }

    private void setDefaultValues() {}

    private void getHomeImage() {}
}
