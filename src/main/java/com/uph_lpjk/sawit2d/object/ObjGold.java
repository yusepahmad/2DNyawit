package com.uph_lpjk.sawit2d.object;

import java.awt.image.BufferedImage;

import com.uph_lpjk.sawit2d.controller.GamePanel;
import com.uph_lpjk.sawit2d.entity.Entity;

public class ObjGold extends Entity {
    
    GamePanel gp;

    public ObjGold(GamePanel gp) {
        super(gp);
        this.gp = gp;

        setType(Type.PICKUP_ONLY);
        setName("Gold");
        setValue(1);
        down1 = setup("/objects/gold/goldie", this.gp.getTileSize(), this.gp.getTileSize());
        image = setup("/objects/gold/goldie", this.gp.getTileSize(), this.gp.getTileSize());
    }

    @Override
    public void use(Entity entity) {
        this.gp.playSoundEffect(1);
        this.gp.addUIMessage("Gold +" + getValue());
        this.gp.setPlayerGold(getValue());
    }

    @Override
    public BufferedImage getImage() {
        return this.image;
    }
}
