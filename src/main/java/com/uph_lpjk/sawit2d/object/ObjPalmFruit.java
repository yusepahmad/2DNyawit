package com.uph_lpjk.sawit2d.object;

import com.uph_lpjk.sawit2d.controller.GamePanel;
import com.uph_lpjk.sawit2d.entity.Entity;

public class ObjPalmFruit extends Entity {
    public ObjPalmFruit(GamePanel gp) {
        super(gp);
        setType(Type.MATERIAL);
        setName("Tandan Buah Segar");
        stackable = true;
        down1 = setupImage("/sawit/palm-fruit", gp.getTileSize(), gp.getTileSize());
        setDescription(
                "["
                        + getName()
                        + "]\nHasil panen kelapa sawit siap jual. Unit: TBS (Tandan Buah Segar)");
    }
}
