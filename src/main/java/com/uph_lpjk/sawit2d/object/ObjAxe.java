package com.uph_lpjk.sawit2d.object;

import com.uph_lpjk.sawit2d.controller.GamePanel;
import com.uph_lpjk.sawit2d.entity.Entity;

public class ObjAxe extends Entity {
    
    GamePanel gp;
    
    public ObjAxe(GamePanel gp) {
        super(gp);
        this.gp = gp;

        setType(Type.AXE);
        setName("Woodcutter's Axe");
        down1 = setup("/objects/weapons/axe", gp.getTileSize(), gp.getTileSize());
        setAttackValue(2);
        attackArea.width = 30;
        attackArea.height = 30;
        setDescription("[" + getName() + "]\nA bit rusty but still\ncan cut some trees.");
    }

    @Override
    public void use(Entity entity) {
        this.gp.playSoundEffect(1);
        this.gp.addUIMessage("Got a " + getName() + "!");
    }
}
