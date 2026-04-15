package com.uph_lpjk.sawit2d.entity;

import com.uph_lpjk.sawit2d.controller.GamePanel;
import com.uph_lpjk.sawit2d.controller.KeyHandler;
import com.uph_lpjk.sawit2d.object.ObjAxe;
import com.uph_lpjk.sawit2d.object.ObjBibitSawit;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Player extends Entity {

    private final GamePanel gp;
    private final KeyHandler keyH;
    private final ArrayList<Entity> inventory = new ArrayList<>();
    private final int MAX_INVENTORY_SIZE = 20;
    private final int tileSize;
    private final int screenWidth, screenHeight;

    protected final int screenX;
    protected final int screenY;

    private boolean attackCanceled = false;

    public Player(GamePanel gp, KeyHandler keyH) {
        super(gp);
        this.gp = gp;
        this.keyH = keyH;
        this.tileSize = getTileSize();
        this.screenWidth = getScreenWidth();
        this.screenHeight = getScreenHeight();

        this.screenX = this.screenWidth / 2 - (this.tileSize / 2);
        this.screenY = this.screenHeight / 2 - (this.tileSize / 2);

        // SOLID AREA
        this.solidArea = new Rectangle();
        this.solidArea.x = 8;
        this.solidArea.y = 16;
        this.solidAreaDefaultX = this.solidArea.x;
        this.solidAreaDefaultY = this.solidArea.y;
        this.solidArea.width = 32;
        this.solidArea.height = 32;

        setDefaultValues();
        getPlayerImage();
        getPlayerAttackImage();
        setItems();
    }

    private void setItems() {
        this.inventory.add(currentWeapon);
        this.inventory.add(new ObjAxe(this.gp));
        this.inventory.add(new ObjBibitSawit(this.gp));
    }

    @Override
    public Entity getCurrentWeapon() {
        return this.currentWeapon;
    }

    public String getInventoryDescription(int i) {
        return this.inventory.get(i).getDescription();
    }

    public Entity getInventory(int i) {
        return this.inventory.get(i);
    }

    public int getInventorySize() {
        return this.inventory.size();
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
        setWorldX(this.tileSize * 42);
        setWorldY(this.tileSize * 4);
        setSpeed(3);
        setDirection("down");

        // Player Status
        maxLife = 6;
        life = maxLife;
        gold = 1000;
        strength = 1;
        currentWeapon = new ObjAxe(gp);
        attack = getAttackCurrentWeapon();
    }

    public void resetToDefaultValues() {
        setDefaultValues();
    }

    private int getAttackCurrentWeapon() {
        attackArea = currentWeapon.attackArea;
        return attack = strength * currentWeapon.attackValue;
    }

    private void getPlayerImage() {
        mainCharacter = setupImage("/player/walking/rat", this.tileSize, this.tileSize);
        up1 = setupImage("/player/walking/rat-up", this.tileSize, this.tileSize);
        up2 = setupImage("/player/walking/rat-up", this.tileSize, this.tileSize);
        down1 = setupImage("/player/walking/rat-down", this.tileSize, this.tileSize);
        down2 = setupImage("/player/walking/rat-down", this.tileSize, this.tileSize);
        left1 = setupImage("/player/walking/rat-run-to-left", this.tileSize, this.tileSize);
        left2 = setupImage("/player/walking/rat-run-to-left", this.tileSize, this.tileSize);
        right1 = setupImage("/player/walking/rat-run-to-right", this.tileSize, this.tileSize);
        right2 = setupImage("/player/walking/rat-run-to-right", this.tileSize, this.tileSize);
    }

    private void getPlayerAttackImage() {
        if (currentWeapon.getType() == Type.AXE || currentWeapon.getType() == Type.EQUIPMENT) {
            this.attackDown1 =
                    setupImage(
                            "/player/attacks/boy_axe_down_1",
                            this.gp.getTileSize(),
                            this.gp.getTileSize() * 2);
            this.attackDown2 =
                    setupImage(
                            "/player/attacks/boy_axe_down_2",
                            this.gp.getTileSize(),
                            this.gp.getTileSize() * 2);
            this.attackUp1 =
                    setupImage(
                            "/player/attacks/boy_axe_up_1",
                            this.gp.getTileSize(),
                            this.gp.getTileSize() * 2);
            this.attackUp2 =
                    setupImage(
                            "/player/attacks/boy_axe_up_2",
                            this.gp.getTileSize(),
                            this.gp.getTileSize() * 2);
            this.attackRight1 =
                    setupImage(
                            "/player/attacks/boy_axe_right_1",
                            this.gp.getTileSize() * 2,
                            this.gp.getTileSize());
            this.attackRight2 =
                    setupImage(
                            "/player/attacks/boy_axe_right_2",
                            this.gp.getTileSize() * 2,
                            this.gp.getTileSize());
            this.attackLeft1 =
                    setupImage(
                            "/player/attacks/boy_axe_left_1",
                            this.gp.getTileSize() * 2,
                            this.gp.getTileSize());
            this.attackLeft2 =
                    setupImage(
                            "/player/attacks/boy_axe_left_2",
                            this.gp.getTileSize() * 2,
                            this.gp.getTileSize());
        }
    }

    @Override
    public void update() {
        if (this.attacking == true) {
            attacking();
        } else if (this.keyH.getUpPressed() == true
                || this.keyH.getDownPressed() == true
                || this.keyH.getLeftPressed() == true
                || this.keyH.getRightPressed() == true
                || this.keyH.getActionPressed() == true // Enter acts as Interact/Attack
        ) {
            if (this.keyH.getUpPressed() == true) {
                setDirection("up");
            } else if (this.keyH.getDownPressed() == true) {
                setDirection("down");
            } else if (this.keyH.getLeftPressed() == true) {
                setDirection("left");
            } else if (this.keyH.getRightPressed() == true) {
                setDirection("right");
            }

            // CHECK TILE COLLISION
            setCollisionOn(false);
            this.gp.checkTile(this);

            // CHECK OBJECT COLLISION
            int objectIndex = this.gp.getCheckObject(this, true);
            pickUpObject(objectIndex);

            // CHECK INTERACTIVE TILE COLLISION (Branch A)
            int iTileIndex = this.gp.getCheckInteractiveTile(this, this.gp.getInteractiveTile());

            // CHECK EVENT (Branch A)
            this.gp.getEventHandlerCheckEvent();

            // IF COLLISION IS FALSE, PLAYER CAN MOVE
            if (getCollisionOn() == false && this.keyH.getActionPressed() == false) {
                switch (getDirection()) {
                    case "up":
                        worldY -= speed;
                        break;
                    case "down":
                        worldY += speed;
                        break;
                    case "left":
                        worldX -= speed;
                        break;
                    case "right":
                        worldX += speed;
                        break;
                }
            }

            // Logic for Attacking (Branch A) vs Interacting (Branch B)
            // If enter is pressed and we are NOT interacting with a Farm Plot, trigger attack
            if (this.keyH.getActionPressed() == true && this.attackCanceled == false) {
                this.gp.playSoundEffect(7);
                this.attacking = true;
                this.spriteCounter = 0;
            }

            this.attackCanceled = false;
            this.gp.setKeyHActionPressed(false);

            this.spriteCounter++;
            if (this.spriteCounter > 10) {
                if (this.spriteNum == 1) {
                    this.spriteNum = 2;
                } else if (this.spriteNum == 2) {
                    this.spriteNum = 1;
                }
                this.spriteCounter = 0;
            }
        } else {
            this.spriteCounter++;
        }
    }

    public void attacking() {
        this.spriteCounter++;

        if (this.spriteCounter <= 5) {
            this.spriteNum = 1;
        }

        if (this.spriteCounter > 5 && this.spriteCounter <= 25) {
            this.spriteNum = 2;

            // Save the current worldX, worldY, solidArea
            int currentWorldX = this.worldX;
            int currentWorldY = this.worldY;
            int solidAreaWidth = solidArea.width;
            int solidAreaHeight = solidArea.height;

            // Adjust players worldX/Y for the attackArea
            switch (getDirection()) {
                case "up":
                    this.worldY -= this.attackArea.height;
                    break;
                case "down":
                    this.worldY += this.attackArea.height;
                    break;
                case "left":
                    this.worldX -= this.attackArea.width;
                    break;
                case "right":
                    this.worldX += this.attackArea.width;
                    break;
            }

            // attackArea become solidArea
            this.solidArea.width = this.attackArea.width;
            this.solidArea.height = this.attackArea.height;

            int iTileIndex = this.gp.getCheckInteractiveTile(this, this.gp.getInteractiveTile());
            damageInteractiveTile(iTileIndex);

            this.worldX = currentWorldX;
            this.worldY = currentWorldY;
            this.solidArea.width = solidAreaWidth;
            this.solidArea.height = solidAreaHeight;
        }

        if (this.spriteCounter > 25) {
            this.spriteNum = 1;
            this.spriteCounter = 0;
            this.attacking = false;
        }
    }

    public void damageInteractiveTile(int i) {
        if (i != 999
                && this.gp.getInteractiveTileIsAttackable(i, this)
                && this.gp.getInteractiveTileInvincible(i) == false) {
            this.gp.interactiveTilePlaySoundEffect(i);
            this.gp.setInteractiveTileLife(i, this.gp.getInteractiveTileLife(i) - 1);
            this.gp.setInteractiveTileInvincible(i, true);

            generateParticle(this.gp.getInteractiveTile(i), this.gp.getInteractiveTile(i));

            if (this.gp.getInteractiveTileLife(i) == 0) {
                this.gp.setInteractiveTile(i, this.gp.getInteractiveTileDestroyForm(i));
            }
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        int tempScreenX = worldX - gp.getCameraX();
        int tempScreenY = worldY - gp.getCameraY();

        switch (getDirection()) {
            case "up":
                if (this.attacking == false) {
                    if (this.spriteNum == 1) image = up1;
                    if (this.spriteNum == 2) image = up2;
                }
                if (this.attacking == true) {
                    tempScreenY = tempScreenY - this.tileSize;
                    if (this.spriteNum == 1) image = attackUp1;
                    if (this.spriteNum == 2) image = attackUp2;
                }
                break;
            case "down":
                if (this.attacking == false) {
                    if (this.spriteNum == 1) image = down1;
                    if (this.spriteNum == 2) image = down2;
                }
                if (this.attacking == true) {
                    if (this.spriteNum == 1) image = attackDown1;
                    if (this.spriteNum == 2) image = attackDown2;
                }
                break;
            case "left":
                if (this.attacking == false) {
                    if (this.spriteNum == 1) image = left1;
                    if (this.spriteNum == 2) image = left2;
                }
                if (this.attacking == true) {
                    tempScreenX = tempScreenX - this.tileSize;
                    if (this.spriteNum == 1) image = attackLeft1;
                    if (this.spriteNum == 2) image = attackLeft2;
                }
                break;
            case "right":
                if (this.attacking == false) {
                    if (this.spriteNum == 1) image = right1;
                    if (this.spriteNum == 2) image = right2;
                }
                if (this.attacking == true) {
                    if (this.spriteNum == 1) image = attackRight1;
                    if (this.spriteNum == 2) image = attackRight2;
                }
                break;
        }
        if (getInvincible() == true) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3F));
        }
        g2.drawImage(image, tempScreenX, tempScreenY, null);

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1F));
    }

    public void selectItem() {
        Entity selectedItem = this.gp.getUISelectedItem();
        if (selectedItem != null) {
            if (selectedItem.getType() == Entity.Type.EQUIPMENT
                    || selectedItem.getType() == Entity.Type.AXE) {
                this.currentWeapon = selectedItem;
                this.attack = getAttackCurrentWeapon();
                getPlayerAttackImage();
            }
            if (selectedItem.getType() == Entity.Type.CONSUMABLE) {
                selectedItem.use(this);
                this.inventory.remove(selectedItem);
            }
        }
    }

    public void pickUpObject(int i) {
        if (i != 999) {
            // PICKUP ONLY ITEMS (Unified type check)
            if (this.gp.getObject(i).getType() == Type.PICKUP_ONLY) {
                this.gp.setObjectUse(i, this);
                this.gp.setObject(i, null);
            }
        }
    }

    public void setAttackCanceled(boolean canceled) {
        this.attackCanceled = canceled;
    }
}
