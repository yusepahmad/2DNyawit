package com.uph_lpjk.sawit2d.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    private final GamePanel gp;
    private boolean upPressed,
            downPressed,
            leftPressed,
            rightPressed,
            enterPressed,
            actionKeyPressed;
    private boolean actionPressed, nextDayPressed, sellPressed, firebreakPressed;
    private boolean autoPlantPressed, autoSellPressed, autoHarvestPressed;
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

    public boolean getActionPressed() {
        return this.actionKeyPressed;
    }

    public void setActionPressed(boolean pressed) {
        this.actionKeyPressed = pressed;
    }

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

    public boolean getShowDebugText() {
        return this.showDebugText;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        switch (this.gp.getGameState()) {
            case TITLE:
                titleState(code);
                break;
            case PLAY:
                playState(code);
                break;
            case CHARACTER:
                characterState(code);
                break;
            case PAUSE:
                pauseState(code);
                break;
            case GAME_OVER:
                gameOverState(code);
                break;
            case MARKET:
                marketState(code);
                break;
        }
    }

    private void marketState(int code) {
        int subState = gp.getUserInterface().getSubState();
        int commandNum = gp.getUICommandNum();

        if (subState == 0) {
            // MAIN MARKET MENU
            if (code == KeyEvent.VK_W) {
                gp.setUICommandNum(commandNum - 1);
                if (gp.getUICommandNum() < 0) gp.setUICommandNum(2);
                gp.playSoundEffect(9);
            }
            if (code == KeyEvent.VK_S) {
                gp.setUICommandNum(commandNum + 1);
                if (gp.getUICommandNum() > 2) gp.setUICommandNum(0);
                gp.playSoundEffect(9);
            }
            if (code == KeyEvent.VK_ENTER) {
                switch (commandNum) {
                    case 0: // Buy Seeds
                        gp.getUserInterface().setSubState(1);
                        gp.getUserInterface().setBuyQty(1);
                        break;
                    case 1: // Sell Harvest
                        gp.getUserInterface().setSubState(2);
                        gp.getUserInterface().setSellQty(1);
                        break;
                    case 2: // Exit
                        gp.setGameState(GamePanel.State.PLAY);
                        gp.setUICommandNum(0);
                        break;
                }
                gp.playSoundEffect(9);
            }
            if (code == KeyEvent.VK_ESCAPE || code == KeyEvent.VK_M) {
                gp.setGameState(GamePanel.State.PLAY);
                gp.setUICommandNum(0);
                gp.playSoundEffect(9);
            }
        } else if (subState == 1) {
            // BUY QUANTITY SELECTION
            if (code == KeyEvent.VK_W || code == KeyEvent.VK_D) {
                gp.getUserInterface().setBuyQty(gp.getUserInterface().getBuyQty() + 1);
                gp.playSoundEffect(9);
            }
            if (code == KeyEvent.VK_S || code == KeyEvent.VK_A) {
                gp.getUserInterface().setBuyQty(gp.getUserInterface().getBuyQty() - 1);
                gp.playSoundEffect(9);
            }
            if (code == KeyEvent.VK_ENTER) {
                int qty = gp.getUserInterface().getBuyQty();
                int totalCost = qty * 30;
                if (gp.getPlayerGold() >= totalCost) {
                    gp.setPlayerGold(-totalCost);
                    com.uph_lpjk.sawit2d.object.ObjBibitSawit seeds =
                            new com.uph_lpjk.sawit2d.object.ObjBibitSawit(gp);
                    seeds.amount = qty;
                    gp.getPlayer().obtainItem(seeds);
                    gp.addUIMessage("Berhasil membeli " + qty + " bibit sawit.");
                    gp.getUserInterface().setSubState(0);
                } else {
                    gp.addUIMessage("Gold tidak cukup!");
                }
                gp.playSoundEffect(9);
            }
            if (code == KeyEvent.VK_ESCAPE) {
                gp.getUserInterface().setSubState(0);
                gp.playSoundEffect(9);
            }
        } else if (subState == 2) {
            // SELL QUANTITY SELECTION
            if (code == KeyEvent.VK_W || code == KeyEvent.VK_D) {
                gp.getUserInterface().setSellQty(gp.getUserInterface().getSellQty() + 1);
                gp.playSoundEffect(9);
            }
            if (code == KeyEvent.VK_S || code == KeyEvent.VK_A) {
                gp.getUserInterface().setSellQty(gp.getUserInterface().getSellQty() - 1);
                gp.playSoundEffect(9);
            }
            if (code == KeyEvent.VK_F) { // Sell All
                gp.getUserInterface().setSellQty(gp.getFarmState().getInventory());
                gp.playSoundEffect(9);
            }
            if (code == KeyEvent.VK_ENTER) {
                int qty = gp.getUserInterface().getSellQty();
                if (qty > 0 && qty <= gp.getFarmState().getInventory()) {
                    gp.getFarmSystem().sellInventory(qty);
                    gp.getUserInterface().setSubState(0);
                } else if (qty > gp.getFarmState().getInventory()) {
                    gp.addUIMessage("Stok tidak cukup!");
                }
                gp.playSoundEffect(9);
            }
            if (code == KeyEvent.VK_ESCAPE) {
                gp.getUserInterface().setSubState(0);
                gp.playSoundEffect(9);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W) this.upPressed = false;
        if (code == KeyEvent.VK_S) this.downPressed = false;
        if (code == KeyEvent.VK_A) this.leftPressed = false;
        if (code == KeyEvent.VK_D) this.rightPressed = false;
        if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_E) this.enterPressed = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    private void titleState(int code) {
        if (code == KeyEvent.VK_W) {
            this.gp.setUICommandNum(this.gp.getUICommandNum() - 1);
            if (this.gp.getUICommandNum() < 0) this.gp.setUICommandNum(2);
        }
        if (code == KeyEvent.VK_S) {
            this.gp.setUICommandNum(this.gp.getUICommandNum() + 1);
            if (this.gp.getUICommandNum() > 2) this.gp.setUICommandNum(0);
        }
        if (code == KeyEvent.VK_ENTER) {
            if (this.gp.getUICommandNum() == 0) {
                this.gp.setGameState(GamePanel.State.PLAY);
                gp.playMusic(0);
            }
            if (this.gp.getUICommandNum() == 2) System.exit(0);
        }
    }

    private void playState(int code) {
        if (code == KeyEvent.VK_W) this.upPressed = true;
        if (code == KeyEvent.VK_S) this.downPressed = true;
        if (code == KeyEvent.VK_A) this.leftPressed = true;
        if (code == KeyEvent.VK_D) this.rightPressed = true;

        // INTERACTION (E or ENTER)
        if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_E) {
            this.enterPressed = true;
            this.actionPressed = true;
        }

        // ATTACK (F)
        if (code == KeyEvent.VK_F) {
            this.actionKeyPressed = true;
        }

        if (code == KeyEvent.VK_N) this.nextDayPressed = true;
        if (code == KeyEvent.VK_Q) this.sellPressed = true;
        if (code == KeyEvent.VK_B) this.firebreakPressed = true;
        if (code == KeyEvent.VK_H) this.autoPlantPressed = true;
        if (code == KeyEvent.VK_J) this.autoSellPressed = true;
        if (code == KeyEvent.VK_K) this.autoHarvestPressed = true;
        if (code == KeyEvent.VK_R) this.gp.loadMap();
        if (code == KeyEvent.VK_ESCAPE) this.gp.setGameState(GamePanel.State.PAUSE);
        if (code == KeyEvent.VK_C) this.gp.setGameState(GamePanel.State.CHARACTER);
        if (code == KeyEvent.VK_M) {
            if (isNearMarket()) {
                this.gp.setGameState(GamePanel.State.MARKET);
                this.gp.setUICommandNum(0);
            } else {
                this.gp.addUIMessage("Anda harus berada di dekat pasar untuk bertransaksi!");
            }
        }
        if (code == KeyEvent.VK_T) this.showDebugText = !this.showDebugText;
    }

    private boolean isNearMarket() {
        int playerCol = gp.getPlayer().getWorldX() / gp.getTileSize();
        int playerRow = gp.getPlayer().getWorldY() / gp.getTileSize();

        // Market is at (10, 3). Check if player is within 4 tiles radius.
        int marketX = 10;
        int marketY = 3;

        return Math.abs(playerCol - marketX) <= 4 && Math.abs(playerRow - marketY) <= 4;
    }

    public void characterState(int code) {
        if (code == KeyEvent.VK_C) this.gp.setGameState(GamePanel.State.PLAY);
        if (code == KeyEvent.VK_W) {
            if (this.gp.getUISlotRow() != 0) {
                this.gp.setUISlotRow(this.gp.getUISlotRow() - 1);
                this.gp.playSoundEffect(9);
            }
        }
        if (code == KeyEvent.VK_A) {
            if (this.gp.getUISlotCol() != 0) {
                this.gp.setUISlotCol(this.gp.getUISlotCol() - 1);
                this.gp.playSoundEffect(9);
            }
        }
        if (code == KeyEvent.VK_S) {
            if (this.gp.getUISlotRow() != (this.gp.getUISlotRows() - 1)) {
                this.gp.setUISlotRow(this.gp.getUISlotRow() + 1);
                this.gp.playSoundEffect(9);
            }
        }
        if (code == KeyEvent.VK_D) {
            if (this.gp.getUISlotCol() != (this.gp.getUISlotCols() - 1)) {
                this.gp.setUISlotCol(this.gp.getUISlotCol() + 1);
                this.gp.playSoundEffect(9);
            }
        }

        if (code == KeyEvent.VK_ENTER) {
            this.gp.setPlayerSelectItem();
        }
    }

    private void pauseState(int code) {
        int subState = gp.getUserInterface().getSubState();
        int commandNum = gp.getUICommandNum();

        if (code == KeyEvent.VK_ESCAPE) {
            if (subState == 0) {
                gp.setGameState(GamePanel.State.PLAY);
            } else {
                gp.getUserInterface().setSubState(0);
                gp.setUICommandNum(0);
            }
            return;
        }

        if (subState == 0) {
            // MAIN PAUSE MENU
            if (code == KeyEvent.VK_W) {
                gp.setUICommandNum(commandNum - 1);
                if (gp.getUICommandNum() < 0) gp.setUICommandNum(4);
                gp.playSoundEffect(9);
            }
            if (code == KeyEvent.VK_S) {
                gp.setUICommandNum(commandNum + 1);
                if (gp.getUICommandNum() > 4) gp.setUICommandNum(0);
                gp.playSoundEffect(9);
            }
            if (code == KeyEvent.VK_ENTER) {
                switch (commandNum) {
                    case 0: // Resume
                        gp.setGameState(GamePanel.State.PLAY);
                        break;
                    case 1: // Settings
                        gp.getUserInterface().setSubState(1);
                        gp.setUICommandNum(0);
                        break;
                    case 2: // Reload Game
                        gp.getFarmSystem().resetSession();
                        gp.loadMap();
                        gp.getPlayer().resetToDefaultValues();
                        gp.setGameState(GamePanel.State.PLAY);
                        break;
                    case 3: // Game Menu
                        gp.getFarmSystem().resetSession();
                        gp.getUserInterface().resetNotifications();
                        gp.setUICommandNum(0);
                        gp.setGameState(GamePanel.State.TITLE);
                        gp.stopMusic();
                        break;
                    case 4: // Exit
                        System.exit(0);
                        break;
                }
                gp.playSoundEffect(9);
            }
        } else if (subState == 1) {
            // SETTINGS MENU
            if (code == KeyEvent.VK_W) {
                gp.setUICommandNum(commandNum - 1);
                if (gp.getUICommandNum() < 0) gp.setUICommandNum(2);
                gp.playSoundEffect(9);
            }
            if (code == KeyEvent.VK_S) {
                gp.setUICommandNum(commandNum + 1);
                if (gp.getUICommandNum() > 2) gp.setUICommandNum(0);
                gp.playSoundEffect(9);
            }
            if (code == KeyEvent.VK_A) {
                if (commandNum == 0 && gp.music.getVolumeScale() > 0) {
                    gp.music.setVolumeScale(gp.music.getVolumeScale() - 1);
                }
                if (commandNum == 1 && gp.se.getVolumeScale() > 0) {
                    gp.se.setVolumeScale(gp.se.getVolumeScale() - 1);
                }
                gp.playSoundEffect(9);
            }
            if (code == KeyEvent.VK_D) {
                if (commandNum == 0 && gp.music.getVolumeScale() < 5) {
                    gp.music.setVolumeScale(gp.music.getVolumeScale() + 1);
                }
                if (commandNum == 1 && gp.se.getVolumeScale() < 5) {
                    gp.se.setVolumeScale(gp.se.getVolumeScale() + 1);
                }
                gp.playSoundEffect(9);
            }
            if (code == KeyEvent.VK_ENTER && commandNum == 2) {
                gp.getUserInterface().setSubState(0);
                gp.setUICommandNum(1); // Return to 'Settings' option
                gp.playSoundEffect(9);
            }
        }
    }

    private void gameOverState(int code) {
        if (code == KeyEvent.VK_ENTER) this.gp.returnHomeFromGameOver();
    }
}
