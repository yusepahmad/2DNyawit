package com.uph_lpjk.sawit2d.entity;

import java.awt.Rectangle;

import com.uph_lpjk.sawit2d.controller.GamePanel;

public class Home extends Entity {
    
    final private GamePanel gp;
    final private int tileSize;
    final private int screenWidth, screenHeight;

    final protected int screenX;
    final protected int screenY;

    public Home(GamePanel gp) {
        super(gp);
        this.gp = gp;
        this.tileSize = getTileSize();
        this.screenWidth = getScreenWidth();
        this.screenHeight = getScreenHeight();

        this.screenX = this.screenWidth/2 - (this.tileSize/2);
        this.screenY = this.screenHeight/2 - (this.tileSize/2);

        // SOLID AREA
        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 32;
    }

    private void setDefaultValues() {

    }

    private void getHomeImage() {
        
    }
}
