package com.uph_lpjk.sawit2d.object;

import com.uph_lpjk.sawit2d.controller.GamePanel;
import com.uph_lpjk.sawit2d.entity.Entity;

public class ObjLoudspeaker extends Entity {

    private final GamePanel gp;

    public ObjLoudspeaker(GamePanel gp) {
        super(gp);
        this.gp = gp;

        setType(Type.EQUIPMENT);
        setName("Loudspeaker");
        down1 = setupImage("/objects/loudspeaker/loudspeaker", gp.getTileSize(), gp.getTileSize());
        setAttackValue(0);
        stackable = false;
        setDescription("[" + getName() + "]\nToa ajaib. F: Antek Asing! G: Hidup Jokowi!");
    }

    @Override
    public void use(Entity entity) {
        // Equipping the loudspeaker — no consume action needed
    }
}
