package com.uph_lpjk.sawit2d.object;

import java.awt.image.BufferedImage;

import com.uph_lpjk.sawit2d.controller.GamePanel;
import com.uph_lpjk.sawit2d.entity.Entity;

public class ObjGarageBottomLeft extends Entity {
    public ObjGarageBottomLeft(GamePanel gp) {
        super(gp);

        setType(Type.HOME);
        setName("Garage");
        setCollision(true);
        down1 = setup("/place/garage/garage-bottom-left-3", gp.getTileSize(), gp.getTileSize());
    }

    @Override
    public BufferedImage getDown1() {
        return down1;
    }
}
