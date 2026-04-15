package com.uph_lpjk.sawit2d.entity;

import com.uph_lpjk.sawit2d.controller.GamePanel;

import java.awt.Color;
import java.awt.Graphics2D;

public class Particle extends Entity {

    GamePanel gp;
    Entity generator;
    Color color;
    int size;
    int xd;
    int yd;

    public Particle(
            GamePanel gp,
            Entity generator,
            Color color,
            int size,
            int speed,
            int maxLife,
            int xd,
            int yd) {
        super(gp);
        this.gp = gp;
        this.generator = generator;
        this.color = color;
        this.size = size;
        this.speed = speed;
        this.maxLife = maxLife;
        this.xd = xd;
        this.yd = yd;

        life = maxLife;
        int offset = (gp.getTileSize() / 2) - (size / 2);
        worldX = generator.getWorldX() + offset;
        worldY = generator.getWorldY() + offset;
    }

    @Override
    public void update() {
        life--;

        if (life < maxLife / 3) {
            yd++;
        }

        worldX += xd * speed;
        worldY += yd * speed;

        if (life == 0) {
            setAlive(false);
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        int screenX = worldX - this.gp.getCameraX();
        int screenY = worldY - this.gp.getCameraY();

        g2.setColor(color);
        g2.fillRect(screenX, screenY, size, size);
    }
}
