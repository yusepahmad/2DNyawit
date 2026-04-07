package com.uph_lpjk.sawit2d.farm;

import com.uph_lpjk.sawit2d.controller.GamePanel;

public class EconomySystem {
    private static final int BASE_PRICE = 120;
    private static final int DISASTER_COST = 80;
    private static final int FIRE_SPREAD_COST = 40;

    public int collectIncome(int harvested) {
        return harvested * BASE_PRICE;
    }

    public int collectIncome(int harvested, double multiplier) {
        return (int) Math.round(harvested * BASE_PRICE * multiplier);
    }

    public void addGold(GamePanel gp, int amount) {
        gp.setPlayerGold(amount);
    }

    public void applyDisasterCost(GamePanel gp) {
        gp.setPlayerGold(-DISASTER_COST);
    }

    public void applyFireSpreadCost(GamePanel gp, int spreadCount) {
        if (spreadCount <= 0) {
            return;
        }
        gp.setPlayerGold(-(FIRE_SPREAD_COST * spreadCount));
    }
}
