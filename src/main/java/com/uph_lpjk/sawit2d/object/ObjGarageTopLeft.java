package com.uph_lpjk.sawit2d.object;

import java.awt.image.BufferedImage;

import com.uph_lpjk.sawit2d.controller.GamePanel;
import com.uph_lpjk.sawit2d.entity.Entity;

public class ObjGarageTopLeft extends Entity {
    public ObjGarageTopLeft(GamePanel gp) {
        super(gp);

        setType(Type.HOME);
        setName("Garage");
        setCollision(true);
        down1 = setup("/place/garage/garage-top-left-1", gp.getTileSize(), gp.getTileSize());
    }

    @Override
    public BufferedImage getDown1() {
        return down1;
    }
}
