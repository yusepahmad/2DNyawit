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

        // Space menutup notifikasi di state mana pun tanpa mengganggu tombol lain
        if (code == KeyEvent.VK_SPACE && this.gp.getUserInterface().isDialogActive()) {
            this.gp.getUserInterface().interactDialog();
            this.gp.playSoundEffect(9);
            return;
        }

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
            // MAIN MARKET MENU — 4 options: 0=Seeds, 1=Loudspeaker, 2=Sell, 3=Exit
            if (code == KeyEvent.VK_W) {
                gp.setUICommandNum(commandNum - 1);
                if (gp.getUICommandNum() < 0) gp.setUICommandNum(3);
                gp.playSoundEffect(9);
            }
            if (code == KeyEvent.VK_S) {
                gp.setUICommandNum(commandNum + 1);
                if (gp.getUICommandNum() > 3) gp.setUICommandNum(0);
                gp.playSoundEffect(9);
            }
            if (code == KeyEvent.VK_ENTER) {
                switch (commandNum) {
                    case 0: // Buy Seeds
                        gp.getUserInterface().setSubState(1);
                        gp.getUserInterface().setBuyQty(1);
                        break;
                    case 1: // Buy Loudspeaker
                        buyLoudspeaker();
                        break;
                    case 2: // Sell Harvest
                        gp.getUserInterface().setSubState(2);
                        gp.getUserInterface().setSellQty(1);
                        break;
                    case 3: // Exit
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
            // BUY QUANTITY SELECTION (seeds)
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
                    gp.getAchievements().onItemBought();
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
            int maxSell = gp.getFarmState().getInventory();
            if (code == KeyEvent.VK_W || code == KeyEvent.VK_D) {
                int next = gp.getUserInterface().getSellQty() + 1;
                gp.getUserInterface().setSellQty(Math.min(next, maxSell));
                gp.playSoundEffect(9);
            }
            if (code == KeyEvent.VK_S || code == KeyEvent.VK_A) {
                gp.getUserInterface().setSellQty(gp.getUserInterface().getSellQty() - 1);
                gp.playSoundEffect(9);
            }
            if (code == KeyEvent.VK_F) { // Sell All
                gp.getUserInterface().setSellQty(maxSell);
                gp.playSoundEffect(9);
            }
            if (code == KeyEvent.VK_ENTER) {
                int qty = gp.getUserInterface().getSellQty();
                if (qty > 0 && qty <= maxSell) {
                    gp.getFarmSystem().sellInventory(qty);
                    gp.getUserInterface().setSubState(0);
                } else if (maxSell == 0) {
                    gp.addUIMessage("Tidak ada TBS untuk dijual!");
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
                this.gp.getAchievements().resetAll();
                this.gp.setGameState(GamePanel.State.PLAY);
                gp.playMusic(0);
                this.gp.addUIMessage(
                        "Halo bos! Kenalin, saya manajer lahan baru di sini. Jas udah rapi, sepatu udah kinclong, tinggal nunggu instruksi bos buat nanem aja nih!");
            }
            if (this.gp.getUICommandNum() == 2) System.exit(0);
        }
    }

    private void playState(int code) {
        if (this.gp.getUserInterface().isDialogActive()) {
            if (code == KeyEvent.VK_E) {
                this.gp.getUserInterface().interactDialog();
                this.gp.playSoundEffect(9);
                this.enterPressed = false;
                this.actionPressed = false;
                this.actionKeyPressed = false;
            }
            return;
        }

        if (code == KeyEvent.VK_W) this.upPressed = true;
        if (code == KeyEvent.VK_S) this.downPressed = true;
        if (code == KeyEvent.VK_A) this.leftPressed = true;
        if (code == KeyEvent.VK_D) this.rightPressed = true;

        // INTERACTION (E or ENTER)
        if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_E) {
            this.enterPressed = true;
            this.actionPressed = true;
        }

        // ATTACK (F) — or loudspeaker sound 1 if equipped
        if (code == KeyEvent.VK_F) {
            if (this.gp.isLoudspeakerEquipped()) {
                LoudspeakerSound.playResource("/sounds/hey-antek-antek-asing-prabowo.wav");
                this.gp.getAchievements().onLoudspeakerUsed();
                this.gp.addUIMessage("Heii... Antek-antek asing");
            } else {
                this.actionKeyPressed = true;
            }
        }

        // LOUDSPEAKER sound 2 (G)
        if (code == KeyEvent.VK_G && this.gp.isLoudspeakerEquipped()) {
            LoudspeakerSound.playFile("/home/yusep/Downloads/hidup-jokowi.wav");
            this.gp.getAchievements().onLoudspeakerUsed();
            this.gp.addUIMessage("Hidup Jokowi!");
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
                this.gp.getAchievements().onMarketVisit();
            } else {
                this.gp.addUIMessage("Anda harus berada di dekat pasar untuk bertransaksi!");
            }
        }
        if (code == KeyEvent.VK_T) this.showDebugText = !this.showDebugText;
    }

    private boolean isNearMarket() {
        int ts = gp.getTileSize();
        // Use player center for more intuitive proximity detection
        int playerCol = (gp.getPlayer().getWorldX() + ts / 2) / ts;
        int playerRow = (gp.getPlayer().getWorldY() + ts / 2) / ts;

        // Market occupies cols 10-11, rows 3-4. Check proximity to market center (10, 3).
        int marketCol = 10;
        int marketRow = 3;

        return Math.abs(playerCol - marketCol) <= 6 && Math.abs(playerRow - marketRow) <= 6;
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
            // MAIN PAUSE MENU — 6 options: Resume, Settings, Achievements, Reload, Game Menu, Exit
            if (code == KeyEvent.VK_W) {
                gp.setUICommandNum(commandNum - 1);
                if (gp.getUICommandNum() < 0) gp.setUICommandNum(5);
                gp.playSoundEffect(9);
            }
            if (code == KeyEvent.VK_S) {
                gp.setUICommandNum(commandNum + 1);
                if (gp.getUICommandNum() > 5) gp.setUICommandNum(0);
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
                    case 2: // Achievements
                        gp.getUserInterface().setSubState(2);
                        break;
                    case 3: // Reload Game
                        gp.getFarmSystem().resetSession();
                        gp.getAchievements().resetAll();
                        gp.loadMap();
                        gp.resetInteractiveTiles();
                        gp.getPlayer().resetToDefaultValues();
                        gp.setGameState(GamePanel.State.PLAY);
                        break;
                    case 4: // Game Menu
                        gp.getFarmSystem().resetSession();
                        gp.getUserInterface().resetNotifications();
                        gp.setUICommandNum(0);
                        gp.setGameState(GamePanel.State.TITLE);
                        gp.stopMusic();
                        break;
                    case 5: // Exit
                        System.exit(0);
                        break;
                }
                gp.playSoundEffect(9);
            }
        } else if (subState == 2) {
            // ACHIEVEMENTS screen — ESC already handled above, nothing else needed
        } else if (subState == 1) {
            // SETTINGS MENU
            if (code == KeyEvent.VK_W) {
                gp.setUICommandNum(commandNum - 1);
                if (gp.getUICommandNum() < 0) gp.setUICommandNum(3);
                gp.playSoundEffect(9);
            }
            if (code == KeyEvent.VK_S) {
                gp.setUICommandNum(commandNum + 1);
                if (gp.getUICommandNum() > 3) gp.setUICommandNum(0);
                gp.playSoundEffect(9);
            }
            if (code == KeyEvent.VK_A) {
                if (commandNum == 0 && gp.music.getVolumeScale() > 0) {
                    gp.music.setVolumeScale(gp.music.getVolumeScale() - 1);
                }
                if (commandNum == 1 && gp.se.getVolumeScale() > 0) {
                    gp.se.setVolumeScale(gp.se.getVolumeScale() - 1);
                }
                if (commandNum == 2) {
                    gp.applyWindowScale(-1);
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
                if (commandNum == 2) {
                    gp.applyWindowScale(1);
                }
                gp.playSoundEffect(9);
            }
            if (code == KeyEvent.VK_ENTER && commandNum == 3) {
                gp.getUserInterface().setSubState(0);
                gp.setUICommandNum(1); // Return to 'Settings' option
                gp.playSoundEffect(9);
            }
        }
    }

    private void buyLoudspeaker() {
        final int LOUDSPEAKER_COST = 1000000;
        if (this.gp.getPlayerGold() >= LOUDSPEAKER_COST) {
            // Check if already owned
            if (this.gp.getPlayer().searchItemInInventory("Loudspeaker") != 999) {
                this.gp.addUIMessage("Kamu sudah punya Loudspeaker!");
                return;
            }
            this.gp.setPlayerGold(-LOUDSPEAKER_COST);
            this.gp.getPlayer().obtainItem(new com.uph_lpjk.sawit2d.object.ObjLoudspeaker(this.gp));
            this.gp.addUIMessage("Loudspeaker dibeli! Equip dari inventory, lalu F/G untuk pakai.");
            this.gp.getAchievements().onLoudspeakerBought();
        } else {
            this.gp.addUIMessage("Gold tidak cukup! Loudspeaker harganya $" + LOUDSPEAKER_COST);
        }
    }

    private void gameOverState(int code) {
        if (code == KeyEvent.VK_ENTER) this.gp.returnHomeFromGameOver();
    }
}
