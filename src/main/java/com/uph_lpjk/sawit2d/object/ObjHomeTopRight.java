package com.uph_lpjk.sawit2d.object;

import java.awt.image.BufferedImage;

import com.uph_lpjk.sawit2d.controller.GamePanel;
import com.uph_lpjk.sawit2d.entity.Entity;

public class ObjHomeTopRight extends Entity {
    public ObjHomeTopRight(GamePanel gp) {
        super(gp);

        setType(Type.HOME);
        setName("Home");
        setCollision(true);
        down1 = setup("/place/home/home-top-right-2", gp.getTileSize(), gp.getTileSize());
    }

    @Override
    public BufferedImage getDown1() {
        return down1;
    }
}
