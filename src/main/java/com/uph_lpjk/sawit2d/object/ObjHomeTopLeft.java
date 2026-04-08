package com.uph_lpjk.sawit2d.object;

import java.awt.image.BufferedImage;

import com.uph_lpjk.sawit2d.controller.GamePanel;
import com.uph_lpjk.sawit2d.entity.Entity;

public class ObjHomeTopLeft extends Entity {
    public ObjHomeTopLeft(GamePanel gp) {
        super(gp);

        setType(Type.HOME);
        setName("Home Top Left");
        setCollision(true);
        down1 = setup("/place/home/home-top-left-1", gp.getTileSize(), gp.getTileSize());
    }

    @Override
    public BufferedImage getDown1() {
        return down1;
    }
}
