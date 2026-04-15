package com.uph_lpjk.sawit2d.object;

import com.uph_lpjk.sawit2d.controller.GamePanel;
import com.uph_lpjk.sawit2d.entity.Entity;

public class ObjAxe extends Entity {

    GamePanel gp;

    public ObjAxe(GamePanel gp) {
        super(gp);
        this.gp = gp;

        setType(Type.EQUIPMENT);
        setName("Kapak Penebang");
        down1 = setupImage("/objects/weapons/axe", gp.getTileSize(), gp.getTileSize());
        setAttackValue(2);
        attackArea.width = 30;
        attackArea.height = 30;
        setDescription("[" + getName() + "]\nSedikit berkarat tetapi masih bisa menebang pohon.");
    }

    @Override
    public void use(Entity entity) {
        this.gp.playSoundEffect(1);
        this.gp.addUIMessage("Mendapatkan " + getName() + "!");
    }
}
