package com.uph_lpjk.sawit2d.entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.uph_lpjk.sawit2d.controller.GamePanel;
import com.uph_lpjk.sawit2d.utility.UtilityTools;

public class Entity {
    
    protected int speed;
    protected int worldX, worldY;
    protected int solidAreaDefaultX, solidAreaDefaultY;
    protected Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    protected Rectangle attackArea = new Rectangle(0, 0, 0, 0);
    protected BufferedImage image, image2, image3;
    protected BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    protected BufferedImage attackUp1, attackUp2, attackDown1, attackDown2, attackLeft1, attackLeft2, attackRight1, attackRight2;
    
    final private GamePanel gp;
    
    protected int spriteCounter = 0;
    protected int spriteNum = 1;
    
    private String direction = "down";
    private boolean collisionOn = false;

    // STATE
    protected boolean collision;
    protected boolean attacking;

    // ATTRIBUTES OBJECT / CHARACTER
    private String name;
    private int life;
    private int gold;

    // ITEM ATTRIBUTES
    private String description;
    private int attackValue;
    private int value;

    // TYPE
    private int type; // 0 = player, 1 = npc
    final public int type_player = 0;
    final public int type_npc = 1;
    final public int type_sword = 2;
    final public int type_axe = 3;
    final public int type_consumable = 4;
    final public int type_pickupOnly = 5;
    
    public Entity(GamePanel gp) {
        this.gp = gp;
    }
    
    public void use(Entity entity) {}
    public BufferedImage getDown1() { return this.down1; }
    public BufferedImage getImage() { return this.image; }
    public BufferedImage getImage2() { return this.image2; }
    public BufferedImage getImage3() { return this.image3; }

    public int getTileSize() {
        return this.gp.getTileSize();
    }

    public int getScreenWidth() {
        return this.gp.getScreenWidth();
    }

    public int getScreenHeight() {
        return this.gp.getScreenHeight();
    }

    public int getWorldX() {
        return this.worldX;
    }
    
    public void setWorldX(int worldX) {
        this.worldX = worldX;
    }

    public int getWorldY() {
        return this.worldY;
    }

    public void setWorldY(int worldY) {
        this.worldY = worldY;
    }

    public Rectangle getSolidArea() {
        return this.solidArea;
    }

    public int getSolidAreaX() {
        return this.solidArea.x;
    }

    public void setSolidAreaX(int x) {
        this.solidArea.x = x;
    }

    public int getSolidAreaY() {
        return this.solidArea.y;
    }

    public void setSolidAreaY(int y) {
        this.solidArea.y = y;
    }

    public int getSolidAreaWidth() {
        return this.solidArea.width;
    }

    public int getSolidAreaHeight() {
        return this.solidArea.height;
    }

    public int getSolidAreaDefaultX() {
        return this.solidAreaDefaultX;
    }

    public int getSolidAreaDefaultY() {
        return this.solidAreaDefaultY;
    }

    public int getSpeed() {
        return this.speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public String getDirection() {
        return this.direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public boolean getCollisionOn() {
        return this.collisionOn;
    }

    public void setCollisionOn(boolean collision) {
        this.collisionOn = collision;
    }

    public boolean isCollision() {
        return this.collision;
    }

    public void setCollision(boolean collision) {
        this.collision = collision;
    }


    // OBJECT ATTRIBUTE
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public int getLife() {
        return this.life;
    }

    public void setGold(int gold) {
        this.gold += gold;
    }

    public int getGold() {
        return this.gold;
    }

    // TYPE
    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    // ITEM ATTRIBUTE
    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }
    
    public void setAttackValue(int attack) {
        this.attackValue = attack;
    }

    public int getAttackValue() {
        return this.attackValue;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public BufferedImage setup(String imagePath, int width, int height) {
        UtilityTools uTool = new UtilityTools();
        BufferedImage image = null;

        try {
            String fullPath = imagePath + ".png";
            InputStream inputStream = getClass().getResourceAsStream(fullPath);

            if(inputStream == null) {
                System.out.println("Resource not found: " + fullPath);
                return null;
            }

            image = ImageIO.read(inputStream);
            image = uTool.scaledImage(image, width, height);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        int screenX = worldX - this.gp.getCameraX();
        int screenY = worldY - this.gp.getCameraY();

        if(
            worldX + this.gp.getTileSize() > this.gp.getCameraX() &&
            worldX - this.gp.getTileSize() < this.gp.getCameraX() + this.gp.getScreenWidth() &&
            worldY + this.gp.getTileSize() > this.gp.getCameraY() &&
            worldY - this.gp.getTileSize() < this.gp.getCameraY() + this.gp.getScreenHeight() 
        ) {
            switch (getDirection()) {
                case "up":
                    if(this.spriteNum == 1) image = this.up1;
                    if (this.spriteNum == 2) image = this.up2;
                    break;
                case "down":
                    if(this.spriteNum == 1) image = this.down1;
                    if(this.spriteNum == 2) image = this.down2;
                    break;
                case "left":
                    if(this.spriteNum == 1) image = this.left1;
                    if(this.spriteNum == 2) image = this.left2;
                    break;
                case "right":
                    if(this.spriteNum == 1) image = this.right1;
                    if(this.spriteNum == 2) image = this.right2;
                    break;
            }

            if(image == null) {
                image = this.down1;
            }

            g2.drawImage(image, screenX, screenY, null);
        }
    }
}
