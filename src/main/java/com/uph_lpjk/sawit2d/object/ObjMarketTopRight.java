package com.uph_lpjk.sawit2d.object;

import com.uph_lpjk.sawit2d.controller.GamePanel;
import com.uph_lpjk.sawit2d.entity.Entity;

public class ObjMarketTopRight extends Entity {
    public ObjMarketTopRight(GamePanel gp) {
        super(gp);

        setType(Type.MARKET);
        setName("Pasar");
        setCollision(true);
        down1 = setupImage("/place/other/market_top_right", gp.getTileSize(), gp.getTileSize());
        setDescription("[" + getName() + "]\nTempat jual beli hasil bumi.");
    }
}
