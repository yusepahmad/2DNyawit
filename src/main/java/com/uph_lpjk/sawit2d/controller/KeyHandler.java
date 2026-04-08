package com.uph_lpjk.sawit2d.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    final private GamePanel gp;
    private boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed, actionKeyPressed;
    private boolean actionPressed, nextDayPressed, sellPressed, firebreakPressed;
    private boolean autoPlantPressed, autoSellPressed, autoHarvestPressed;
    private boolean showDebugText = false;

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    public boolean getUpPressed() { return this.upPressed; }
    public boolean getDownPressed() { return this.downPressed; }
    public boolean getLeftPressed() { return this.leftPressed; }
    public boolean getRightPressed() { return this.rightPressed; }
    public boolean getEnterPressed() { return this.enterPressed; }
    public boolean getActionPressed() { return this.actionKeyPressed; }

    public void setActionPressed(boolean pressed) { this.actionKeyPressed = pressed;}

    public boolean consumeActionPressed() {
        boolean pressed = this.actionPressed;
        this.actionPressed = false;
        return pressed;
    }

    public boolean consumeNextDayPressed() {
        boolean pressed = this.nextDayPressed;
        this.nextDayPressed = false;
        return pressed;
    }

    public boolean consumeSellPressed() {
        boolean pressed = this.sellPressed;
        this.sellPressed = false;
        return pressed;
    }

    public boolean consumeFirebreakPressed() {
        boolean pressed = this.firebreakPressed;
        this.firebreakPressed = false;
        return pressed;
    }

    public boolean consumeAutoPlantPressed() {
        boolean pressed = this.autoPlantPressed;
        this.autoPlantPressed = false;
        return pressed;
    }

    public boolean consumeAutoSellPressed() {
        boolean pressed = this.autoSellPressed;
        this.autoSellPressed = false;
        return pressed;
    }

    public boolean consumeAutoHarvestPressed() {
        boolean pressed = this.autoHarvestPressed;
        this.autoHarvestPressed = false;
        return pressed;
    }

    public boolean getShowDebugText() { return this.showDebugText; }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        switch (this.gp.getGameState()) {
            case TITLE: titleState(code); break;
            case PLAY: playState(code); break;
            case PAUSE: pauseState(code); break;
            case GAME_OVER: gameOverState(code); break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if(code == KeyEvent.VK_W) this.upPressed = false;
        if(code == KeyEvent.VK_S) this.downPressed = false;
        if(code == KeyEvent.VK_A) this.leftPressed = false;
        if(code == KeyEvent.VK_D) this.rightPressed = false;
        if(code == KeyEvent.VK_ENTER || code == KeyEvent.VK_E) this.enterPressed = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    private void titleState(int code) {
        if(code == KeyEvent.VK_W) {
            this.gp.setUICommandNum(this.gp.getUICommandNum() - 1);
            if(this.gp.getUICommandNum() < 0) this.gp.setUICommandNum(2);
        }
        if(code == KeyEvent.VK_S) {
            this.gp.setUICommandNum(this.gp.getUICommandNum() + 1);
            if(this.gp.getUICommandNum() > 2) this.gp.setUICommandNum(0);
        }
        if(code == KeyEvent.VK_ENTER) {
            if(this.gp.getUICommandNum() == 0) {
                this.gp.setGameState(GamePanel.State.PLAY);
                // gp.playMusic(0);
            }
            if(this.gp.getUICommandNum() == 2) System.exit(0);
        }
    }

    private void playState(int code) {
        if(code == KeyEvent.VK_W) this.upPressed = true;
        if(code == KeyEvent.VK_S) this.downPressed = true;
        if(code == KeyEvent.VK_A) this.leftPressed = true;
        if(code == KeyEvent.VK_D) this.rightPressed = true;

        // INTERACTION (E or ENTER)
        if(code == KeyEvent.VK_ENTER || code == KeyEvent.VK_E) {
            this.enterPressed = true;
            this.actionPressed = true;
        }

        // ATTACK (F)
        if(code == KeyEvent.VK_F) {
            this.actionKeyPressed = true;
        }

        if(code == KeyEvent.VK_N) this.nextDayPressed = true;
        if(code == KeyEvent.VK_Q) this.sellPressed = true;
        if(code == KeyEvent.VK_B) this.firebreakPressed = true;
        if(code == KeyEvent.VK_H) this.autoPlantPressed = true;
        if(code == KeyEvent.VK_J) this.autoSellPressed = true;
        if(code == KeyEvent.VK_K) this.autoHarvestPressed = true;
        if(code == KeyEvent.VK_R) this.gp.loadMap();
        if(code == KeyEvent.VK_P) this.gp.setGameState(GamePanel.State.PAUSE);
        if(code == KeyEvent.VK_T) this.showDebugText = !this.showDebugText;
    }


    private void pauseState(int code) {
        if(code == KeyEvent.VK_P) this.gp.setGameState(GamePanel.State.PLAY);
    }

    private void gameOverState(int code) {
        if (code == KeyEvent.VK_ENTER) this.gp.returnHomeFromGameOver();
    }
}
