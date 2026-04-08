package com.uph_lpjk.sawit2d.object;

import java.awt.image.BufferedImage;

import com.uph_lpjk.sawit2d.controller.GamePanel;
import com.uph_lpjk.sawit2d.entity.Entity;

public class ObjHomeBottomRight extends Entity {
    public ObjHomeBottomRight(GamePanel gp) {
        super(gp);

        setType(Type.HOME);
        setName("Home Bottom Right");
        setCollision(true);
        down1 = setup("/place/home/home-bottom-right-4", gp.getTileSize(), gp.getTileSize());
    }

    @Override
    public BufferedImage getDown1() {
        return down1;
    }
}
