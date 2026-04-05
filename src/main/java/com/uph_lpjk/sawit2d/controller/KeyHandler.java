package com.uph_lpjk.sawit2d.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    final private GamePanel gp;
    private boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed;
    private boolean showDebugText = false;

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    public boolean getUpPressed() {
        return this.upPressed;
    }

    public boolean getDownPressed() {
        return this.downPressed;
    }

    public boolean getLeftPressed() {
        return this.leftPressed;
    }

    public boolean getRightPressed() {
        return this.rightPressed;
    }

    public boolean getEnterPressed() {
        return this.enterPressed;
    }

    public boolean getShowDebugText() {
        return this.showDebugText;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        switch (this.gp.getGameState()) {
            case TITLE: titleState(code); break;
            case PLAY: playState(code); break;
            case PAUSE: pauseState(code); break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        
        if(code == KeyEvent.VK_W) {
            this.upPressed = false;
        }
        if(code == KeyEvent.VK_S) {
            this.downPressed = false;
        }
        if(code == KeyEvent.VK_A) {
            this.leftPressed = false;
        }
        if(code == KeyEvent.VK_D) {
            this.rightPressed = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    private void titleState(int code) {
        if(code == KeyEvent.VK_W) {
            this.gp.setUICommandNum(this.gp.getUICommandNum() - 1);
            if(this.gp.getUICommandNum() < 0) {
                this.gp.setUICommandNum(2);
            }
        }
        if(code == KeyEvent.VK_S) {
            this.gp.setUICommandNum(this.gp.getUICommandNum() + 1);
            if(this.gp.getUICommandNum() > 2) {
                this.gp.setUICommandNum(0);
            }
        }
        if(code == KeyEvent.VK_ENTER) {
            if(this.gp.getUICommandNum() == 0) {
                this.gp.setGameState(GamePanel.State.PLAY);
                gp.playMusic(0);
            }
            if(this.gp.getUICommandNum() == 1) {
                // NOTHING
            }
            if(this.gp.getUICommandNum() == 2) {
                System.exit(0);
            }
        }
    }

    private void playState(int code) {
        if(code == KeyEvent.VK_W) {
            this.upPressed = true;
        }
        if(code == KeyEvent.VK_S) {
            this.downPressed = true;
        }
        if(code == KeyEvent.VK_A) {
            this.leftPressed = true;
        }
        if(code == KeyEvent.VK_D) {
            this.rightPressed = true;
        }
        if(code == KeyEvent.VK_R) {
            this.gp.loadMap();
        }
        if(code == KeyEvent.VK_P) {
            this.gp.setGameState(GamePanel.State.PAUSE);
        }
        if(code == KeyEvent.VK_T) {
            if(this.showDebugText == false) {
                this.showDebugText = true;
            } else if(this.showDebugText == true) {
                this.showDebugText = false;
            }
        }
    }

    private void pauseState(int code) {
        if(code == KeyEvent.VK_P) {
            this.gp.setGameState(GamePanel.State.PLAY);
        }
    }

    private void dialogueState(int code) {

    }

    private void characterState(int code) {

    }

    private void optionState(int code) {

    }
    
}
