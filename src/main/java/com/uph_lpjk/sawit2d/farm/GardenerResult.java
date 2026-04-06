package com.uph_lpjk.sawit2d.farm;

public class GardenerResult {
    private final int harvested;
    private final int cost;
    private final String message;

    public GardenerResult(int harvested, int cost, String message) {
        this.harvested = harvested;
        this.cost = cost;
        this.message = message;
    }

    public int getHarvested() {
        return harvested;
    }

    public int getCost() {
        return cost;
    }

    public String getMessage() {
        return message;
    }
}
