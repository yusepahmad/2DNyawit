package com.uph_lpjk.sawit2d.object;

import com.uph_lpjk.sawit2d.controller.GamePanel;
import com.uph_lpjk.sawit2d.entity.Entity;

public class ObjMarket extends Entity {
    public ObjMarket(GamePanel gp) {
        super(gp);

        setName("Pasar");
        setCollision(true);
        // Using 2x2 tile size for the market building
        down1 = setupImage("/place/other/market-fixed", gp.getTileSize() * 2, gp.getTileSize() * 2);
        setDescription("[" + getName() + "]\nTempat jual beli hasil bumi.");
    }
}
