package com.uph_lpjk.sawit2d.entity;

import com.uph_lpjk.sawit2d.controller.GamePanel;
import com.uph_lpjk.sawit2d.controller.KeyHandler;
import com.uph_lpjk.sawit2d.object.ObjAxe;

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
        this.inventory.add(new ObjAxe(this.gp));
    }

    public void obtainItem(Entity item) {
        if (item.stackable) {
            int index = searchItemInInventory(item.getName());
            if (index != 999) {
                this.inventory.get(index).amount += item.amount;
                return;
            }
        }

        if (this.inventory.size() < MAX_INVENTORY_SIZE) {
            this.inventory.add(item);
        }
    }

    public int searchItemInInventory(String itemName) {
        for (int i = 0; i < this.inventory.size(); i++) {
            if (this.inventory.get(i).getName().equals(itemName)) {
                return i;
            }
        }
        return 999;
    }

    public boolean consumeItem(String itemName, int amount) {
        int index = searchItemInInventory(itemName);
        if (index != 999) {
            Entity item = this.inventory.get(index);
            if (item.amount >= amount) {
                item.amount -= amount;
                if (item.amount <= 0) {
                    this.inventory.remove(index);
                }
                return true;
            }
        }
        return false;
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
        currentWeapon = null;
        attack = 0;
    }

    public void resetToDefaultValues() {
        setDefaultValues();
    }

    private int getAttackCurrentWeapon() {
        if (currentWeapon == null) return attack = 0;
        attackArea = currentWeapon.attackArea;
        return attack = strength * currentWeapon.attackValue;
    }

    private void getPlayerImage() {
        mainCharacter = setupImage("/player/walking/rat", this.tileSize, this.tileSize);
        // Use rat-back for up (walking away) and rat-up for second frame animation
        up1 = setupImage("/player/walking/rat-back", this.tileSize, this.tileSize);
        up2 = setupImage("/player/walking/rat-up", this.tileSize, this.tileSize);
        // Use rat (front-facing) and rat-down for alternating down frames
        down1 = setupImage("/player/walking/rat", this.tileSize, this.tileSize);
        down2 = setupImage("/player/walking/rat-down", this.tileSize, this.tileSize);
        // Use proper left/right directional sprites for side movement
        left1 = setupImage("/player/walking/rat-left", this.tileSize, this.tileSize);
        left2 = setupImage("/player/walking/rat-run-to-left", this.tileSize, this.tileSize);
        right1 = setupImage("/player/walking/rat-right", this.tileSize, this.tileSize);
        right2 = setupImage("/player/walking/rat-run-to-right", this.tileSize, this.tileSize);
    }

    private void getPlayerAttackImage() {
        if (currentWeapon == null) return;
        if (currentWeapon.getType() == Type.AXE || currentWeapon.getType() == Type.EQUIPMENT) {
            int ts = this.gp.getTileSize();

            // Down attack: mouse_kapak_bawah.png
            this.attackDown1 = setupImage("/player/attacks/mouse_kapak_bawah", ts, ts);
            this.attackDown2 = setupImage("/player/attacks/mouse_kapak_bawah", ts, ts);

            // Up attack: rat_axe_up.png
            this.attackUp1 = setupImage("/player/attacks/rat_axe_up", ts, ts);
            this.attackUp2 = setupImage("/player/attacks/rat_axe_up", ts, ts);

            // Left attack: 2-frame animation
            this.attackLeft1 = setupImage("/player/attacks/rat-axe-left-1", ts, ts);
            this.attackLeft2 = setupImage("/player/attacks/rat-axe-left-2", ts, ts);

            // Right attack: 2-frame animation
            this.attackRight1 = setupImage("/player/attacks/rat-axe-right-1", ts, ts);
            this.attackRight2 = setupImage("/player/attacks/rat-axe-right-2", ts, ts);
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
            if (this.keyH.getActionPressed() == true
                    && this.attackCanceled == false
                    && this.currentWeapon != null) {
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

        if (this.spriteCounter > 5 && this.spriteCounter <= 15) {
            this.spriteNum = 2;

            // Define attack box in world coordinates
            int worldX = getWorldX();
            int worldY = getWorldY();
            java.awt.Rectangle attackBox = new java.awt.Rectangle();
            attackBox.width = this.attackArea.width;
            attackBox.height = this.attackArea.height;

            switch (getDirection()) {
                case "up":
                    attackBox.x = worldX + (getTileSize() - attackBox.width) / 2;
                    attackBox.y = worldY - attackBox.height;
                    break;
                case "down":
                    attackBox.x = worldX + (getTileSize() - attackBox.width) / 2;
                    attackBox.y = worldY + getTileSize();
                    break;
                case "left":
                    attackBox.x = worldX - attackBox.width;
                    attackBox.y = worldY + (getTileSize() - attackBox.height) / 2;
                    break;
                case "right":
                    attackBox.x = worldX + getTileSize();
                    attackBox.y = worldY + (getTileSize() - attackBox.height) / 2;
                    break;
            }

            int iTileIndex = this.gp.getCheckAttack(this, this.gp.getInteractiveTile(), attackBox);
            damageInteractiveTile(iTileIndex);
        }

        if (this.spriteCounter > 15 && this.spriteCounter <= 20) {
            this.spriteNum = 1;
        }

        if (this.spriteCounter > 20) {
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

    private boolean isNearMarket() {
        int playerCol = (worldX + this.tileSize / 2) / this.tileSize;
        int playerRow = (worldY + this.tileSize / 2) / this.tileSize;
        return Math.abs(playerCol - 10) <= 6 && Math.abs(playerRow - 3) <= 6;
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        int tempScreenX = worldX - gp.getCameraX();
        int tempScreenY = worldY - gp.getCameraY();

        if (!this.attacking && isNearMarket()) {
            g2.drawImage(mainCharacter, tempScreenX, tempScreenY, null);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1F));
            return;
        }

        switch (getDirection()) {
            case "up":
                if (this.attacking == false) {
                    if (this.spriteNum == 1) image = up1;
                    if (this.spriteNum == 2) image = up2;
                }
                if (this.attacking == true) {
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

    private void equipAxeSprites() {
        int ts = this.tileSize;
        BufferedImage kapak = setupImage("/player/walking/mouse-kapak", ts, ts);
        mainCharacter = kapak;
        // down: dua frame yang sama karena tidak ada sprite jalan bawah+kapak lain
        down1 = kapak;
        down2 = kapak;
        // up: walking atas biasa (sudah smooth)
        up1 = setupImage("/player/walking/rat-back", ts, ts);
        up2 = setupImage("/player/walking/rat-up", ts, ts);
        // kiri: frame1=bawa kapak sambil jalan, frame2=langkah lari biasa → smooth walk
        left1 = setupImage("/player/attacks/rat-axe-left-2", ts, ts);
        left2 = setupImage("/player/attacks/rat-axe-left-2", ts, ts);
        // kanan: frame1=bawa kapak sambil jalan, frame2=langkah lari biasa → smooth walk
        right1 = setupImage("/player/attacks/rat-axe-right-2", ts, ts);
        right2 = setupImage("/player/attacks/rat-axe-right-2", ts, ts);
    }

    public void selectItem() {
        Entity selectedItem = this.gp.getUISelectedItem();
        if (selectedItem == null) return;

        if (selectedItem.getType() == Entity.Type.EQUIPMENT
                || selectedItem.getType() == Entity.Type.AXE) {

            // Toggle unequip: klik item yang sudah equipped → lepas
            if (this.currentWeapon == selectedItem) {
                this.currentWeapon = null;
                this.attack = 0;
                this.gp.setLoudspeakerEquipped(false);
                getPlayerImage();
                return;
            }

            this.currentWeapon = selectedItem;
            this.attack = getAttackCurrentWeapon();
            getPlayerAttackImage();

            boolean isLoudspeaker = selectedItem.getName().equals("Loudspeaker");
            this.gp.setLoudspeakerEquipped(isLoudspeaker);

            if (isLoudspeaker) {
                BufferedImage jokowi =
                        setupImage(
                                "/player/attacks/mouse-hidup-jokowi", this.tileSize, this.tileSize);
                mainCharacter = jokowi;
                down1 = jokowi;
                down2 = jokowi;
                up1 = jokowi;
                up2 = jokowi;
                left1 = jokowi;
                left2 = jokowi;
                right1 = jokowi;
                right2 = jokowi;
            } else if (selectedItem.getType() == Entity.Type.AXE) {
                equipAxeSprites();
            } else {
                getPlayerImage();
            }
        }

        if (selectedItem.getType() == Entity.Type.CONSUMABLE) {
            selectedItem.use(this);
            this.inventory.remove(selectedItem);
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
