package com.uph_lpjk.sawit2d.farm;

import com.uph_lpjk.sawit2d.controller.GamePanel;

import java.util.Random;

public class DisasterSystem {
    private final Random random = new Random();
    private static final int RISK_THRESHOLD = 12;

    public boolean attemptDisaster(
            FarmGrid grid, GameState state, GamePanel gp, EconomySystem economy) {
        int risk = state.getRiskScore();
        if (risk < RISK_THRESHOLD) {
            return false;
        }
        int chance = Math.min(60, risk * 3);
        if (random.nextInt(100) >= chance) {
            return false;
        }
        int ignited = grid.igniteRandomPlanted(Math.max(1, risk / 5 + 1));
        if (ignited > 0) {
            economy.applyDisasterCost(gp);
            gp.playSoundEffect(10);
            state.addReputation(-2);
            state.modifyRisk(Math.max(1, ignited / 2));
            state.setLastNotification("Disaster: kebakaran mulai di " + ignited + " sawit.");
            return true;
        }
        return false;
    }
}
