package com.uph_lpjk.sawit2d.interactive.tile;

import java.awt.Graphics2D;

import com.uph_lpjk.sawit2d.controller.GamePanel;
import com.uph_lpjk.sawit2d.entity.Entity;

public class InteractiveTile extends Entity {
    
    final private GamePanel gp;
    protected boolean destructible = false;

    public InteractiveTile(GamePanel gp, int col, int row) {
        super(gp);
        this.gp = gp;
    }

    public void playSoundEffect() {}

    public boolean getDestructible() {
        return this.destructible;
    }

    public InteractiveTile getDestroyForm() {
        InteractiveTile tile = null;
        return tile;
    }

    public boolean isCorrectItem(Entity entity) {
        boolean isCorrectItem = false;
        return isCorrectItem;
    }

    public boolean isAttackable(Entity attacker) {
        return this.destructible == true && isCorrectItem(attacker) == true;
    }

    public void update() {
        if(getInvincible() == true) {
            invincibleCounter++;
            if(invincibleCounter > 20) {
                setInvincible(false);
                invincibleCounter = 0;
            }
        }
    }

    public void draw(Graphics2D g2) {
        int screenX = getWorldX() - this.gp.getCameraX();
        int screenY = getWorldY() - this.gp.getCameraY();

        if(
            getWorldX() + this.gp.getTileSize() > this.gp.getCameraX() &&
            getWorldX() - this.gp.getTileSize() < this.gp.getCameraX() + this.gp.getScreenWidth() &&
            getWorldY() + this.gp.getTileSize() > this.gp.getCameraY() &&
            getWorldY() - this.gp.getTileSize() < this.gp.getCameraY() + this.gp.getScreenHeight() 
        ) {

            g2.drawImage(getDown1(), screenX, screenY, null);

        }
    }
}
