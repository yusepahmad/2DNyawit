package com.uph_lpjk.sawit2d.farm;

public class FarmTile {
    private static final int UNUSUED_LAND_LIMIT_DAYS = 14;
    private FarmTileType type;
    private int remainingGrowDays;
    private boolean readyToHarvest;
    private boolean burned;
    private FarmBurnHandledType handledType;
    private int unusedDays;

    public FarmTile() {
        this.type = FarmTileType.EMPTY;
        this.remainingGrowDays = 0;
        this.readyToHarvest = false;
        this.burned = false;
        this.handledType = FarmBurnHandledType.NONE;
        this.unusedDays = 0;
    }

    public FarmTileType getType() {
        return type;
    }

    public boolean isReadyToHarvest() {
        return readyToHarvest;
    }

    public int getGrowthStage() {
        if (type != FarmTileType.SAWIT) {
            return 0;
        }
        if (readyToHarvest) {
            return 3;
        }
        if (remainingGrowDays >= 2) {
            return 1;
        }
        return 2;
    }

    public void plant() {
        if (type == FarmTileType.EMPTY) {
            this.type = FarmTileType.SAWIT;
            this.remainingGrowDays = FarmTileType.SAWIT.getGrowTime();
            this.readyToHarvest = false;
            this.burned = false;
            this.handledType = FarmBurnHandledType.NONE;
            this.unusedDays = 0;
        }
    }

    public void advanceDay() {
        if (type == FarmTileType.SAWIT && !readyToHarvest) {
            remainingGrowDays = Math.max(0, remainingGrowDays - 1);
            if (remainingGrowDays == 0) {
                readyToHarvest = true;
            }
        }
    }

    public void clearBurnState() {
        burned = false;
        handledType = FarmBurnHandledType.NONE;
    }

    public void extinguish() {
        reset();
        burned = false;
        handledType = FarmBurnHandledType.NONE;
    }

    public boolean harvest() {
        if (readyToHarvest) {
            reset();
            return true;
        }
        return false;
    }

    public void destroy() {
        markBurned();
        reset();
    }

    public void markBurned() {
        burned = true;
        handledType = FarmBurnHandledType.NONE;
    }

    public boolean isBurned() {
        return burned;
    }

    public boolean isBurnedHandled() {
        return handledType != FarmBurnHandledType.NONE;
    }

    public void markBurnHandled(FarmBurnHandledType type) {
        if (burned && type != FarmBurnHandledType.NONE) {
            handledType = type;
        }
    }

    public void setType(FarmTileType type) {
        this.type = type;
        this.readyToHarvest = false;
        this.remainingGrowDays = type.getGrowTime();
        if (type != FarmTileType.EMPTY) {
            this.unusedDays = 0;
        }
    }

    public FarmBurnHandledType getBurnHandledType() {
        return handledType;
    }

    public void reset() {
        this.type = FarmTileType.EMPTY;
        this.remainingGrowDays = 0;
        this.readyToHarvest = false;
        this.burned = false;
        this.handledType = FarmBurnHandledType.NONE;
        this.unusedDays = 0;
    }

    public void resetBurnOnly() {
        this.burned = false;
        this.handledType = FarmBurnHandledType.NONE;
    }

    public void advanceUnusedDay() {
        if (this.type == FarmTileType.EMPTY && !this.burned) {
            this.unusedDays++;
        }
    }

    public boolean isLandExpired() {
        return this.type == FarmTileType.EMPTY && this.unusedDays >= UNUSUED_LAND_LIMIT_DAYS;
    }

    public int getUnusedDays() {
        return this.unusedDays;
    }

    public void resetUnusedDays() {
        this.unusedDays = 0;
    }
}
