package com.uph_lpjk.sawit2d.farm;

public class RiskSystem {
    private int currentRisk = 0;

    public void evaluate(FarmGrid grid) {
        int planted = grid.countByType(FarmTileType.SAWIT);
        int firebreak = grid.countByType(FarmTileType.FIREBREAK);
        int largestCluster = grid.countConnectedPlantedArea(false);
        int overcrowdBonus = 0;
        if (largestCluster > 36 && !grid.hasFirebreak()) {
            overcrowdBonus = (largestCluster - 36) / 2;
        }
        currentRisk = Math.max(0, planted - (firebreak * 2) + overcrowdBonus);
    }

    public int getCurrentRisk() {
        return currentRisk;
    }
}
