package com.uph_lpjk.sawit2d.object;

import com.uph_lpjk.sawit2d.controller.GamePanel;
import com.uph_lpjk.sawit2d.entity.Entity;

public class ObjBibitSawit extends Entity {
    public ObjBibitSawit(GamePanel gp) {
        super(gp);
        setType(Type.MATERIAL);
        setName("Bibit Sawit");
        down1 = setupImage("/sawit/sawit-fase-1", gp.getTileSize(), gp.getTileSize());
        setDescription("[" + getName() + "]\nBibit kelapa sawit yang siap ditanam.");
    }
}
