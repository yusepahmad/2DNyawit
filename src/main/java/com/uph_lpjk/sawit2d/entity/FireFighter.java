package com.uph_lpjk.sawit2d.entity;

import com.uph_lpjk.sawit2d.controller.GamePanel;
import com.uph_lpjk.sawit2d.farm.FarmTile;

import java.awt.Color;

public class FireFighter extends Entity {

    private final GamePanel gp;
    private final int targetX;
    private final int targetY;
    private final FarmTile targetTile;
    private boolean extinguishing = false;
    private int extinguishCounter = 0;

    public FireFighter(GamePanel gp, int targetX, int targetY, FarmTile targetTile) {
        super(gp);
        this.gp = gp;
        this.targetX = targetX;
        this.targetY = targetY;
        this.targetTile = targetTile;

        // Start at player's position
        setWorldX(gp.getPlayerWorldX());
        setWorldY(gp.getPlayerWorldY());
        setSpeed(4);
        setDirection("down");
        setAlive(true);

        loadFireFighterImages();
    }

    public void loadFireFighterImages() {
        up1 = setupImage("/player/elephant/elephant-back", gp.getTileSize(), gp.getTileSize());
        up2 = setupImage("/player/elephant/elephant-back", gp.getTileSize(), gp.getTileSize());
        down1 = setupImage("/player/elephant/elephant-front", gp.getTileSize(), gp.getTileSize());
        down2 = setupImage("/player/elephant/elephant-front", gp.getTileSize(), gp.getTileSize());
        left1 = setupImage("/player/elephant/elephant-left", gp.getTileSize(), gp.getTileSize());
        left2 = setupImage("/player/elephant/elephant-left", gp.getTileSize(), gp.getTileSize());
        right1 = setupImage("/player/elephant/elephant-right", gp.getTileSize(), gp.getTileSize());
        right2 = setupImage("/player/elephant/elephant-right", gp.getTileSize(), gp.getTileSize());
    }

    @Override
    public Color getParticleColor() {
        return new Color(0, 150, 255); // Water blue
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

    @Override
    public void update() {
        if (extinguishing) {
            extinguishCounter++;

            // Wiggle animation
            if (extinguishCounter % 5 == 0) {
                if (spriteNum == 1) {
                    spriteNum = 2;
                } else {
                    spriteNum = 1;
                }
            }

            // Water particles and splash sound
            if (extinguishCounter % 15 == 0) {
                generateParticle(this, this);
            }

            if (extinguishCounter > 60) {
                targetTile.extinguish();
                gp.playSoundEffect(3); // Extinguish sound
                setAlive(false);
            }
            return;
        }

        // Move towards target
        int diffX = targetX - getWorldX();
        int diffY = targetY - getWorldY();

        if (Math.abs(diffX) > getSpeed()) {
            if (diffX > 0) {
                setWorldX(getWorldX() + getSpeed());
                setDirection("right");
            } else {
                setWorldX(getWorldX() - getSpeed());
                setDirection("left");
            }
        } else if (Math.abs(diffY) > getSpeed()) {
            if (diffY > 0) {
                setWorldY(getWorldY() + getSpeed());
                setDirection("down");
            } else {
                setWorldY(getWorldY() - getSpeed());
                setDirection("up");
            }
        } else {
            // Reached target
            extinguishing = true;
        }

        spriteCounter++;
        if (spriteCounter > 12) {
            if (spriteNum == 1) {
                spriteNum = 2;
            } else if (spriteNum == 2) {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
    }
}
