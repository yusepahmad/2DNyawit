package com.uph_lpjk.sawit2d.farm;

public class GameState {
    private int day = 1;
    private int hour = 6;
    private int inventory = 0;
    private int reputation = 50;
    private int riskScore = 0;
    private String lastNotification = "Selamat datang di Sawit2D";
    private boolean raining = false;
    private int totalHarvested = 0;
    private int totalSold = 0;

    public int getDay() {
        return day;
    }

    public void incrementDay() {
        day++;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = Math.max(0, Math.min(23, hour));
    }

    public boolean advanceHour() {
        hour++;
        if (hour >= 24) {
            hour = 0;
            return true;
        }
        return false;
    }

    public void resetMorningHour() {
        hour = 6;
    }

    public int getInventory() {
        return inventory;
    }

    public void addInventory(int amount) {
        inventory = Math.max(0, inventory + amount);
    }

    public int takeInventory(int amount) {
        int actual = Math.min(amount, inventory);
        inventory -= actual;
        return actual;
    }

    public int getReputation() {
        return reputation;
    }

    public void addReputation(int amount) {
        reputation += amount;
    }

    public int getRiskScore() {
        return riskScore;
    }

    public void setRiskScore(int riskScore) {
        this.riskScore = Math.max(0, riskScore);
    }

    public void modifyRisk(int delta) {
        setRiskScore(this.riskScore + delta);
    }

    public String getLastNotification() {
        return lastNotification;
    }

    public void setLastNotification(String lastNotification) {
        this.lastNotification = lastNotification;
    }

    public boolean isRaining() {
        return raining;
    }

    public void setRaining(boolean raining) {
        this.raining = raining;
    }

    public int getTotalHarvested() {
        return totalHarvested;
    }

    public void addTotalHarvested(int amount) {
        totalHarvested += amount;
    }

    public int getTotalSold() {
        return totalSold;
    }

    public void addTotalSold(int amount) {
        totalSold += amount;
    }

    public void reset() {
        this.day = 1;
        this.hour = 6;
        this.inventory = 0;
        this.reputation = 50;
        this.riskScore = 0;
        this.lastNotification = "Selamat datang di Sawit2D";
        this.raining = false;
        this.totalHarvested = 0;
        this.totalSold = 0;
    }
}
