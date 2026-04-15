package com.uph_lpjk.sawit2d.object;

import com.uph_lpjk.sawit2d.controller.GamePanel;
import com.uph_lpjk.sawit2d.entity.Entity;

import java.awt.image.BufferedImage;

public class ObjGarageTopRight extends Entity {
    public ObjGarageTopRight(GamePanel gp) {
        super(gp);

        setType(Type.HOME);
        setName("Garage");
        setCollision(true);
        down1 = setupImage("/place/garage/garage-top-right-2", gp.getTileSize(), gp.getTileSize());
    }

    @Override
    public BufferedImage getDown1() {
        return down1;
    }
}
