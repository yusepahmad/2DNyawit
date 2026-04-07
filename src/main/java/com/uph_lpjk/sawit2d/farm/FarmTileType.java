package com.uph_lpjk.sawit2d.farm;

public enum FarmTileType {
    EMPTY(0),
    SAWIT(3),
    FIREBREAK(0),
    VEGETATION(0);

    private final int growTime;

    FarmTileType(int growTime) {
        this.growTime = growTime;
    }

    public int getGrowTime() {
        return growTime;
    }
}
