package com.uph_lpjk.sawit2d.entity;

import java.awt.Color;
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
    protected BufferedImage mainCharacter;
    protected BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    protected BufferedImage attackUp1, attackUp2, attackDown1, attackDown2, attackLeft1, attackLeft2, attackRight1, attackRight2;
    
    final private GamePanel gp;
    
    protected int spriteNum = 1;
    
    // COUNTER
    protected int invincibleCounter = 0;
    protected int spriteCounter = 0;
    
    // STATE
    protected boolean collision;
    protected boolean attacking;

    private String direction = "down";
    private boolean collisionOn = false;
    private boolean invincible = false;
    private boolean alive = true;

    // ATTRIBUTES OBJECT / CHARACTER
    protected String name;
    protected int maxLife;
    protected int life;
    protected int gold;
    protected int strength;
    protected int attack;
    protected Entity currentWeapon;

    // ITEM ATTRIBUTES
    protected String description;
    protected int attackValue;
    protected int value;

    // TYPE
    public enum Type { PLAYER, NPC, SWORD, AXE, CONSUMABLE, PICKUP_ONLY, HOME }
    private Type type;
    
    public Entity(GamePanel gp) {
        this.gp = gp;
    }
    
    public void use(Entity entity) {}
    public void setAction() {}
    public BufferedImage getDown1() { return this.down1; }
    public BufferedImage getImage() { return this.image; }
    public BufferedImage getImage2() { return this.image2; }
    public BufferedImage getImage3() { return this.image3; }
    public BufferedImage getImageMainCharacter() { return this.mainCharacter; }

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

    public boolean getInvincible() {
        return this.invincible;
    }

    public void setInvincible(boolean invincible) {
        this.invincible = invincible;
    }

    public boolean getAlive() {
        return this.alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
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

    public Entity getCurrentWeapon() {
        return this.currentWeapon;
    }

    // RPG ATTRIBUTES (Restored from Branch A)
    public int getStrength() { return strength; }
    public void setStrength(int strength) { this.strength = strength; }
    public int getAttack() { return attack; }
    public void setAttack(int attack) { this.attack = attack; }
    public int getMaxLife() { return maxLife; }
    public void setMaxLife(int maxLife) { this.maxLife = maxLife; }

    // TYPE
    public Type getType() {
        return this.type;
    }

    public void setType(Type type) {
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

    public boolean isPickupOnly() {
        return this.type == Type.PICKUP_ONLY;
    }

    public Color getParticleColor() {
        Color color = null;
        return color;
    }

    public int getParticleSize() {
        int size = 0;
        return size;
    }

    public int getParticleSpeed () {
        int speed = 0;
        return speed;
    }

    public int getParticleMaxLife() {
        int maxLife = 0;
        return maxLife;
    }

    public void generateParticle(Entity generator, Entity target) {
        Color color = generator.getParticleColor();
        int size = generator.getParticleSize();
        int speed = generator.getParticleSpeed();
        int maxLife = generator.getParticleMaxLife();

        Particle p1 = new Particle(gp, target, color, size, speed, maxLife, -2, -1);
        Particle p2 = new Particle(gp, target, color, size, speed, maxLife, 2, -1);
        Particle p3 = new Particle(gp, target, color, size, speed, maxLife, -2, 1);
        Particle p4 = new Particle(gp, target, color, size, speed, maxLife, 2, 1);

        this.gp.addParticleList(p1);
        this.gp.addParticleList(p2);
        this.gp.addParticleList(p3);
        this.gp.addParticleList(p4);
    }

    public BufferedImage setup(String imagePath, int width, int height) {
        UtilityTools uTool = new UtilityTools();
        BufferedImage image = null;

        try {
            String fullPath = imagePath.endsWith(".png") ? imagePath : imagePath + ".png";
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

            // if (type == Type.HOME) {
            //     g2.drawImage(getTopLeftImage(), screenX, screenY, null);
            //     g2.drawImage(getTopRightImage(), screenX + this.gp.getTileSize(), screenY, null);
            //     g2.drawImage(getBottomLeftImage(), screenX, screenY + this.gp.getTileSize(), null);
            //     g2.drawImage(getBottomRightImage(), screenX + this.gp.getTileSize(), screenY + this.gp.getTileSize(), null);
            // } else {
            g2.drawImage(image, screenX, screenY, null);
            // }
        }
    }

    public void update() {
        setAction();

        this.collisionOn = false;
        this.gp.checkTile(this);
        this.gp.getCheckObject(this, false);
        this.gp.getCheckInteractiveTile(this, this.gp.getInteractiveTile());

        if(collisionOn == false) {
            switch (direction) {
                case "up": worldY -= speed; break;
                case "down": worldY += speed; break;
                case "left": worldX -= speed; break;
                case "right": worldX += speed; break;
            }
        }

        spriteCounter++;
        if(spriteCounter > 12) {
            if(spriteNum == 1) {
                spriteNum = 2;
            } else if(spriteNum == 2) {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
    }
}
