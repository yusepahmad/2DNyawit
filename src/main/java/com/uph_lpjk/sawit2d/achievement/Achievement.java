package com.uph_lpjk.sawit2d.achievement;

public class Achievement {
    public final String id;
    public final String name;
    public final String description;
    public final String clue;
    private boolean unlocked = false;

    public Achievement(String id, String name, String description, String clue) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.clue = clue;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public void unlock() {
        this.unlocked = true;
    }

    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }
}
