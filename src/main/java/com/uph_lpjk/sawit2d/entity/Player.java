package com.uph_lpjk.sawit2d.entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.uph_lpjk.sawit2d.controller.GamePanel;
import com.uph_lpjk.sawit2d.controller.KeyHandler;

public class Player extends Entity {
    
    final private GamePanel gp;
    final private KeyHandler keyH;
    final private int tileSize;
    final private int screenWidth, screenHeight;

    final protected int screenX;
    final protected int screenY;

    public Player(GamePanel gp, KeyHandler keyH) {
        super(gp);
        this.gp = gp;
        this.keyH = keyH;
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

        setDefaultValues();
        getPlayerImage();
    }

    public int getScreenX() {
        return this.screenX;
    }

    public int getScreenY() {
        return this.screenY;
    }

    @Override
    public int getSolidAreaX() {
        return this.solidArea.x;
    }

    @Override
    public int getSolidAreaY() {
        return this.solidArea.y;
    }

    private void setDefaultValues() {
        setWorldX(this.tileSize * 20);
        setWorldY(this.tileSize * 15);
        setSpeed(3);
    }

    public void resetToDefaultValues() {
        setDefaultValues();
    }

    private void getPlayerImage() {
        up1 = setup("/player/walking/boy_up_1", this.tileSize, this.tileSize);
        up2 = setup("/player/walking/boy_up_2", this.tileSize, this.tileSize);
        down1 = setup("/player/walking/boy_down_1", this.tileSize, this.tileSize);
        down2 = setup("/player/walking/boy_down_2", this.tileSize, this.tileSize);
        left1 = setup("/player/walking/boy_left_1", this.tileSize, this.tileSize);
        left2 = setup("/player/walking/boy_left_2", this.tileSize, this.tileSize);
        right1 = setup("/player/walking/boy_right_1", this.tileSize, this.tileSize);
        right2 = setup("/player/walking/boy_right_2", this.tileSize, this.tileSize);
    }

    public void update() {
        if(
            this.keyH.getUpPressed() == true ||
            this.keyH.getDownPressed() == true ||
            this.keyH.getLeftPressed() == true ||
            this.keyH.getRightPressed() == true ||
            this.keyH.getEnterPressed() == true
        ) {
            if(this.keyH.getUpPressed() == true) {
                setDirection("up");
            } else if(this.keyH.getDownPressed() == true) {
                setDirection("down");
            } else if(this.keyH.getLeftPressed() == true) {
                setDirection("left");
            } else if(this.keyH.getRightPressed() == true) {
                setDirection("right");
            }

            // CHECK TILE COLLISION
            setCollisionOn(false);
            this.gp.checkTile(this);

            // CHECK OBJECT COLLISION
            int objectIndex = this.gp.getCheckObject(this, true);
            pickUpObject(objectIndex);

            // IF COLLISION IS FALSE, PLAYER CAN MOVE
            if(getCollisionOn() == false && this.keyH.getEnterPressed() == false) {
                switch (getDirection()) {
                    case "up": worldY -= speed; break;
                    case "down": worldY += speed; break;
                    case "left": worldX -= speed; break;
                    case "right": worldX += speed; break;
                }
            }

            spriteCounter++;
            if(spriteCounter > 10) {
                if(spriteNum == 1) {
                    spriteNum = 2;
                } else if(spriteNum == 2) {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
        } else {
            spriteCounter++;
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        int tempScreenX = worldX - gp.getCameraX();
        int tempScreenY = worldY - gp.getCameraY();

        switch (getDirection()) {
            case "up":
                if(attacking==false) {
                    if(spriteNum==1) image = up1;
                    if(spriteNum==2) image = up2;
                }
                if(attacking == true) {
                    tempScreenY = tempScreenY - this.tileSize;
                    if(spriteNum==1) image = attackUp1;
                    if(spriteNum==2) image = attackUp2;
                }
                break;
            case "down":
                if(attacking==false) {
                    if(spriteNum==1) image = down1;
                    if(spriteNum==2) image = down2;
                }
                if(attacking == true) {
                    if(spriteNum==1) image = attackDown1;
                    if(spriteNum==2) image = attackDown2;
                }
                break;
            case "left":
                if(attacking==false) {
                    if(spriteNum == 1) image = left1;
                    if(spriteNum == 2) image = left2;
                }
                if(attacking == true) {
                    tempScreenX = tempScreenX - this.tileSize;
                    if(spriteNum==1) image = attackLeft1;
                    if(spriteNum==2) image = attackLeft2;
                }
                break;
            case "right":
                if(attacking==false) {
                    if(spriteNum == 1) image = right1;
                    if(spriteNum == 2) image = right2;
                }
                if(attacking == true) {
                    if(spriteNum==1) image = attackRight1;
                    if(spriteNum==2) image = attackRight2;
                }
                break;
        }
        g2.drawImage(image, tempScreenX, tempScreenY, null);
    }

    public void pickUpObject(int i) {
        if(i != 999) {
            // PICKUP ONLY TIMES
            if(this.gp.getObjectType(i) == type_pickupOnly) {
                this.gp.setObjectUse(i, this);
                this.gp.setObject(i, null);
            } else {
                // INVENTORY ITEMS
            }
        }
    }

}
