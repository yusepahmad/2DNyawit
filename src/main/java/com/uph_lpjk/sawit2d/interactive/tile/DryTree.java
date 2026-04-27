package com.uph_lpjk.sawit2d.interactive.tile;

import com.uph_lpjk.sawit2d.controller.GamePanel;
import com.uph_lpjk.sawit2d.entity.Entity;

public class DryTree extends InteractiveTile {

    GamePanel gp;

    public DryTree(GamePanel gp, int col, int row) {
        super(gp, col, row);
        this.gp = gp;
        setWorldX(this.gp.getTileSize() * col);
        setWorldY(this.gp.getTileSize() * row);

        down1 =
                setupImage(
                        "/tiles_interactive/drytree", this.gp.getTileSize(), this.gp.getTileSize());
        destructible = true;
        setLife(3);

        // Define a smaller, more realistic solid area (centered bottom)
        this.solidArea.x = 8;
        this.solidArea.y = 16;
        this.solidArea.width = 32;
        this.solidArea.height = 32;
        this.solidAreaDefaultX = this.solidArea.x;
        this.solidAreaDefaultY = this.solidArea.y;
    }

    @Override
    public boolean isCorrectItem(Entity entity) {
        boolean isCorrectItem = false;
        if (entity.getCurrentWeapon() != null && entity.getCurrentWeapon().getType() == Type.AXE) {
            isCorrectItem = true;
        }
        return isCorrectItem;
    }

    @Override
    public InteractiveTile getDestroyForm() {
        InteractiveTile tile =
                new Trunk(gp, getWorldX() / gp.getTileSize(), getWorldY() / gp.getTileSize());
        return tile;
    }

    @Override
    public void playSoundEffect() {
        this.gp.playSoundEffect(11);
    }

    @Override
    public java.awt.Color getParticleColor() {
        return new java.awt.Color(65, 50, 30);
    }

    @Override
    public int getParticleSize() {
        return 6;
    }

    @Override
    public int getParticleSpeed() {
        return 1;
    }

    @Override
    public int getParticleMaxLife() {
        return 20;
    }
}
