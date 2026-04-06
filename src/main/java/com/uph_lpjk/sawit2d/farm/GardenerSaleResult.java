package com.uph_lpjk.sawit2d.farm;

public class GardenerSaleResult {
    private final int sold;
    private final int income;
    private final double multiplier;
    private final String message;

    public GardenerSaleResult(int sold, int income, double multiplier, String message) {
        this.sold = sold;
        this.income = income;
        this.multiplier = multiplier;
        this.message = message;
    }

    public int getSold() {
        return sold;
    }

    public int getIncome() {
        return income;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public String getMessage() {
        return message;
    }
}
