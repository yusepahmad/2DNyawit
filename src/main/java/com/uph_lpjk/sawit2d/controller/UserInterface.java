package com.uph_lpjk.sawit2d.controller;

import com.uph_lpjk.sawit2d.achievement.Achievement;
import com.uph_lpjk.sawit2d.entity.Entity;
import com.uph_lpjk.sawit2d.farm.FarmGrid;
import com.uph_lpjk.sawit2d.farm.FarmTileType;
import com.uph_lpjk.sawit2d.object.ObjGold;
import com.uph_lpjk.sawit2d.utility.AssetLoader;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Consumer;

public class UserInterface {

    public enum BannerTone {
        INFO,
        SUCCESS,
        WARNING,
        DANGER,
        WEATHER,
        ECONOMY,
        GAME_OVER
    }

    private static class Banner {
        private final BannerTone tone;
        private final String title;
        private final String detail;
        private final BufferedImage icon;
        private int age;

        private Banner(BannerTone tone, String title, String detail, BufferedImage icon) {
            this.tone = tone;
            this.title = title;
            this.detail = detail;
            this.icon = icon;
            this.age = 0;
        }
    }

    private final GamePanel gp;
    private final AssetLoader assetLoader = new AssetLoader();

    protected BufferedImage gold;
    private final BufferedImage infoIcon;
    private final BufferedImage successIcon;
    private final BufferedImage warningIcon;
    private final BufferedImage dangerIcon;
    private final BufferedImage weatherIcon;
    private final BufferedImage economyIcon;
    private final BufferedImage gameOverIcon;
    private final BufferedImage firefighterIcon;

    private Graphics2D g2;
    private Font maruMonica, purisaBold;

    private Banner activeBanner;
    private final Queue<Banner> bannerQueue = new LinkedList<>();
    private final BufferedImage managerIcon;
    private final BufferedImage loadingRatRun;
    private final BufferedImage loadingRatIdle;
    private final BufferedImage loadingSawit;

    // EVENT SYSTEM
    private boolean eventActive = false;
    private String eventTitle = "";
    private String eventMessage = "";
    private String[] eventOptions = new String[0];
    private Consumer<Integer> eventCallback;
    private final List<Rectangle> eventButtonRects = new ArrayList<>();

    private int commandNum = 0;
    private int subState = 0;
    private int slotCol = 0;
    private int slotRow = 0;

    private int buyQty = 1;
    private int sellQty = 1;

    public int getBuyQty() {
        return buyQty;
    }

    public void setBuyQty(int buyQty) {
        this.buyQty = Math.max(1, buyQty);
    }

    public int getSellQty() {
        return sellQty;
    }

    public void setSellQty(int sellQty) {
        this.sellQty = Math.max(1, sellQty);
    }

    public int getSubState() {
        return subState;
    }

    public void setSubState(int subState) {
        this.subState = subState;
    }

    private int frameX = 9;
    private int frameY = 8;

    public UserInterface(GamePanel gp) {
        this.gp = gp;

        try {
            InputStream inputStream = getClass().getResourceAsStream("/fonts/Purisa Bold.ttf");
            this.purisaBold = Font.createFont(Font.TRUETYPE_FONT, inputStream);
            inputStream = getClass().getResourceAsStream("/fonts/x12y16pxMaruMonica.ttf");
            this.maruMonica = Font.createFont(Font.TRUETYPE_FONT, inputStream);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // CREATE HUD OBJECT
        Entity objGold = new ObjGold(gp);
        this.gold = objGold.getImage();

        this.infoIcon = assetLoader.loadImage(28, 28, "/tile/weather-sunny");
        this.successIcon = assetLoader.loadImage(28, 28, "/tile/earth");
        this.warningIcon = assetLoader.loadImage(28, 28, "/tile/after-banjir");
        this.dangerIcon = assetLoader.loadImage(28, 28, "/tile/kebakaran");
        this.weatherIcon = assetLoader.loadImage(28, 28, "/tile/weather-cloudy");
        this.economyIcon = assetLoader.loadImage(28, 28, "/objects/gold/goldie");
        this.gameOverIcon = assetLoader.loadImage(28, 28, "/tile/after-kebakaran-2");
        this.firefighterIcon = assetLoader.loadImage(48, 48, "/player/elephant/elephant-front");
        this.managerIcon = assetLoader.loadImage(144, 144, "/player/attacks/rat-tracing.png");
        this.loadingRatRun = assetLoader.loadImage(48, 48, "/player/walking/rat-run-to-right");
        this.loadingRatIdle = assetLoader.loadImage(48, 48, "/player/walking/rat");
        this.loadingSawit = assetLoader.loadImage(48, 48, "/sawit/sawit-panen");
    }

    public void setupEvent(
            String title, String message, String[] options, Consumer<Integer> callback) {
        this.eventTitle = title;
        this.eventMessage = message;
        this.eventOptions = options;
        this.eventCallback = callback;
        this.eventActive = true;
    }

    public void handleEventInput(int x, int y) {
        if (!eventActive) return;
        for (int i = 0; i < eventButtonRects.size(); i++) {
            if (eventButtonRects.get(i).contains(x, y)) {
                eventActive = false;
                if (eventCallback != null) {
                    eventCallback.accept(i);
                }
                return;
            }
        }
    }

    public int getSlotRow() {
        return this.slotRow;
    }

    public void setSlotRow(int slotRow) {
        this.slotRow = slotRow;
    }

    public int getSlotCol() {
        return this.slotCol;
    }

    public void setSlotCol(int slotCol) {
        this.slotCol = slotCol;
    }

    public Entity getSelectedItem() {
        int itemIndex = getItemIndexOnSlot();
        if (itemIndex < this.gp.getPlayerInventorySize()) {
            return this.gp.getPlayerInventory(itemIndex);
        }
        return null;
    }

    public int getCommandNum() {
        return this.commandNum;
    }

    public void setCommandNum(int num) {
        this.commandNum = num;
    }

    public void addMessage(String text) {
        Banner banner = resolveBannerFromText(text);
        if (banner == null) {
            return;
        }
        pushBanner(banner.tone, banner.title, banner.detail);
    }

    public void pushBanner(BannerTone tone, String title, String detail) {
        this.bannerQueue.offer(createBanner(tone, title, detail));
        if (this.activeBanner == null) {
            this.activeBanner = this.bannerQueue.poll();
        }
    }

    public void resetNotifications() {
        this.activeBanner = null;
        this.bannerQueue.clear();
    }

    public void interactDialog() {
        if (this.activeBanner != null) {
            int displayedChars = this.activeBanner.age / 2;
            if (displayedChars < this.activeBanner.detail.length()) {
                this.activeBanner.age = this.activeBanner.detail.length() * 2;
            } else {
                this.activeBanner = this.bannerQueue.poll();
                if (this.activeBanner != null) {
                    this.activeBanner.age = 0;
                }
            }
        }
    }

    public boolean isDialogActive() {
        return this.activeBanner != null;
    }

    private Banner resolveBannerFromText(String text) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        String lower = text.toLowerCase();
        if (lower.contains("game over") || lower.contains("gold habis")) {
            return createBanner(BannerTone.GAME_OVER, "Game Over", text);
        }
        if (lower.contains("hujan")) {
            return createBanner(BannerTone.WEATHER, "Hujan Turun", text);
        }
        if (lower.contains("api") || lower.contains("kebakaran") || lower.contains("firebreak")) {
            return createBanner(BannerTone.DANGER, "Kebakaran", text);
        }
        if (lower.contains("jual") || lower.contains("stok")) {
            return createBanner(BannerTone.ECONOMY, "Transaksi", text);
        }
        if (lower.contains("panen") || lower.contains("tanam") || lower.contains("tumbuh")) {
            return createBanner(BannerTone.SUCCESS, "Aktivitas Kebun", text);
        }
        if (lower.contains("gold tidak cukup") || lower.contains("tidak bisa")) {
            return createBanner(BannerTone.WARNING, "Peringatan", text);
        }
        if (lower.contains("got a")) {
            return createBanner(BannerTone.INFO, "Item Baru", text);
        }
        return createBanner(BannerTone.INFO, "Info", text);
    }

    private Banner createBanner(BannerTone tone, String title, String detail) {
        return new Banner(tone, title, detail, resolveBannerIcon(tone));
    }

    private BufferedImage resolveBannerIcon(BannerTone tone) {
        switch (tone) {
            case WEATHER:
                return this.weatherIcon;
            case DANGER:
                return this.dangerIcon;
            case ECONOMY:
                return this.economyIcon;
            case SUCCESS:
                return this.successIcon;
            case WARNING:
                return this.warningIcon;
            case GAME_OVER:
                return this.gameOverIcon;
            default:
                return this.infoIcon;
        }
    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;

        // SET FONT
        this.g2.setFont(this.maruMonica);
        // this.g2.setFont(purisaBold);

        this.g2.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        this.g2.setColor(Color.white);

        switch (this.gp.getGameState()) {
            case LOADING:
                drawLoadingScreen();
                break;
            case TITLE:
                drawTitleScreen();
                break;
            case PLAY:
                drawPlayerGold();
                drawOverview();
                drawControlHint();
                break;
            case CHARACTER:
                drawInventory();
                drawFarmStatus();
                break;
            case PAUSE:
                drawPauseScreen();
                break;
            case GAME_OVER:
                drawGameOverScreen();
                break;
            case EVENT:
                drawEventScreen();
                break;
            case MARKET:
                drawMarketScreen();
                break;
        }

        // Banner notifikasi selalu tampil di semua state
        drawBanner();
    }

    private void drawMarketScreen() {
        if (subState == 0) {
            marketMain();
        } else if (subState == 1) {
            marketBuyQty();
        } else if (subState == 2) {
            marketSellQty();
        }
    }

    private void drawLoadingScreen() {
        // BACKGROUND
        this.g2.setColor(new Color(20, 20, 20));
        this.g2.fillRect(0, 0, this.gp.getScreenWidth(), this.gp.getScreenHeight());

        int barW = 500;
        int barH = 40;
        int barX = (this.gp.getScreenWidth() - barW) / 2;
        int barY = (this.gp.getScreenHeight() - barH) / 2 + 50;

        float progress = this.gp.getLoadingProgress();

        // BORDER
        this.g2.setColor(new Color(200, 200, 200));
        this.g2.setStroke(new BasicStroke(3));
        this.g2.drawRoundRect(barX, barY, barW, barH, 25, 25);

        // PROGRESS FILL (GREEN TRANSPARENT)
        this.g2.setColor(new Color(50, 200, 50, 100));
        int fillW = (int) (barW * progress);
        if (fillW > 0) {
            this.g2.fillRoundRect(barX, barY, fillW, barH, 25, 25);
        }

        // SAWIT PANEN AT THE END
        this.g2.drawImage(this.loadingSawit, barX + barW - 24, barY - 15, 48, 48, null);

        // RAT
        int ratX = barX + (int) (barW * progress) - 24;
        int ratY = barY - 15;
        BufferedImage ratImg = (progress < 1.0f) ? this.loadingRatRun : this.loadingRatIdle;
        this.g2.drawImage(ratImg, ratX, ratY, 48, 48, null);

        // LOADING TEXT
        this.g2.setFont(this.g2.getFont().deriveFont(Font.BOLD, 24F));
        this.g2.setColor(Color.WHITE);
        String text = "MEMUAT LAHAN...";
        int textX = getXforCenteredText(text);
        this.g2.drawString(text, textX, barY - 40);
    }

    private void marketMain() {
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, gp.getScreenWidth(), gp.getScreenHeight());

        int width = gp.getTileSize() * 10;
        int height = gp.getTileSize() * 9;
        int x = (gp.getScreenWidth() - width) / 2;
        int y = (gp.getScreenHeight() - height) / 2;

        drawSubWindow(x, y, width, height);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48F));
        String text = "PASAR SAWIT";
        int tx = getXforCenteredText(text);
        int ty = y + gp.getTileSize() * 2;
        g2.setColor(new Color(255, 240, 200));
        g2.drawString(text, tx, ty);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 24F));
        g2.setColor(Color.white);
        text = "Gold Anda: $" + gp.getPlayerGold();
        tx = getXforCenteredText(text);
        ty += gp.getTileSize();
        g2.drawString(text, tx, ty);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 28F));
        String[] options = {
            "Beli Bibit Sawit ($30)",
            "Beli Loudspeaker ($1,000,000)",
            "Jual Hasil Panen ($120)",
            "Keluar"
        };
        ty += gp.getTileSize();
        for (int i = 0; i < options.length; i++) {
            text = options[i];
            tx = getXforCenteredText(text);
            ty += (int) (gp.getTileSize() * 1.1);
            g2.drawString(text, tx, ty);
            if (commandNum == i) {
                g2.drawString(">", tx - 30, ty);
            }
        }
    }

    private void marketBuyQty() {
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, gp.getScreenWidth(), gp.getScreenHeight());

        int width = gp.getTileSize() * 8;
        int height = gp.getTileSize() * 7;
        int x = (gp.getScreenWidth() - width) / 2;
        int y = (gp.getScreenHeight() - height) / 2;

        drawSubWindow(x, y, width, height);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 36F));
        String text = "Beli Bibit";
        int tx = getXforCenteredText(text);
        int ty = y + (int) (gp.getTileSize() * 1.5);
        g2.setColor(new Color(255, 240, 200));
        g2.drawString(text, tx, ty);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 24F));
        g2.setColor(new Color(200, 200, 200));
        text = "Gold kamu: $" + gp.getPlayerGold();
        tx = getXforCenteredText(text);
        ty += gp.getTileSize();
        g2.drawString(text, tx, ty);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 28F));
        g2.setColor(Color.white);
        text = "Jumlah: < " + buyQty + " >";
        tx = getXforCenteredText(text);
        ty += (int) (gp.getTileSize() * 1.2);
        g2.drawString(text, tx, ty);

        int total = buyQty * 30;
        boolean canAfford = gp.getPlayerGold() >= total;
        g2.setColor(canAfford ? Color.white : new Color(255, 80, 80));
        text = "Total: $" + total;
        tx = getXforCenteredText(text);
        ty += gp.getTileSize();
        g2.drawString(text, tx, ty);

        if (!canAfford) {
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20F));
            g2.setColor(new Color(255, 80, 80));
            text = "⚠ Gold tidak cukup!";
            tx = getXforCenteredText(text);
            ty += (int) (gp.getTileSize() * 0.8);
            g2.drawString(text, tx, ty);
        }

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 18F));
        g2.setColor(Color.white);
        text = "[ENTER] Konfirmasi  [ESC] Batal";
        tx = getXforCenteredText(text);
        ty += (int) (gp.getTileSize() * 1.0);
        g2.drawString(text, tx, ty);
    }

    private void marketSellQty() {
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, gp.getScreenWidth(), gp.getScreenHeight());

        int width = gp.getTileSize() * 8;
        int height = gp.getTileSize() * 6;
        int x = (gp.getScreenWidth() - width) / 2;
        int y = (gp.getScreenHeight() - height) / 2;

        drawSubWindow(x, y, width, height);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 36F));
        String text = "Jual Hasil Panen";
        int tx = getXforCenteredText(text);
        int ty = y + (int) (gp.getTileSize() * 1.5);
        g2.setColor(new Color(255, 240, 200));
        g2.drawString(text, tx, ty);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 28F));
        g2.setColor(Color.white);

        int maxSell = gp.getFarmState().getInventory();
        text = "Tersedia: " + maxSell + " TBS";
        tx = getXforCenteredText(text);
        ty += gp.getTileSize();
        g2.drawString(text, tx, ty);

        text = "Jumlah: < " + sellQty + " >";
        tx = getXforCenteredText(text);
        ty += (int) (gp.getTileSize() * 1.5);
        g2.drawString(text, tx, ty);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 18F));
        text = "[ENTER] Konfirmasi  [F] Jual Semua  [ESC] Batal";
        tx = getXforCenteredText(text);
        ty += (int) (gp.getTileSize() * 1.2);
        g2.drawString(text, tx, ty);
    }

    private void drawEventScreen() {
        if (!eventActive) return;

        int width = 620;
        int height = 300;
        int x = (gp.getScreenWidth() - width) / 2;
        int y = (gp.getScreenHeight() - height) / 2;

        drawSubWindow(x, y, width, height);

        // ICON
        g2.drawImage(firefighterIcon, x + 30, y + 30, 60, 60, null);

        // TITLE
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 28F));
        g2.setColor(new Color(255, 240, 200));
        g2.drawString(eventTitle, x + 110, y + 60);

        // BODY
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 20F));
        g2.setColor(new Color(245, 220, 180));
        drawWrappedText(eventMessage, x + 110, y + 100, width - 140, 25, false);

        // BUTTONS
        eventButtonRects.clear();
        int btnW = 220;
        int btnH = 50;
        int btnX = x + width - btnW - 30;
        int btnY = y + height - btnH - 30;

        for (int i = eventOptions.length - 1; i >= 0; i--) {
            Rectangle rect = new Rectangle(btnX, btnY, btnW, btnH);
            eventButtonRects.add(0, rect); // Keep order for index matching

            // Draw Button Background
            g2.setColor(new Color(92, 64, 22));
            g2.fillRoundRect(rect.x, rect.y, rect.width, rect.height, 10, 10);
            g2.setColor(new Color(214, 166, 82));
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(rect.x, rect.y, rect.width, rect.height, 10, 10);

            // Draw Text
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 18F));
            g2.setColor(Color.WHITE);
            int textX =
                    rect.x + (rect.width - g2.getFontMetrics().stringWidth(eventOptions[i])) / 2;
            int textY = rect.y + (rect.height / 2) + 7;
            g2.drawString(eventOptions[i], textX, textY);

            btnX -= btnW + 20;
        }
    }

    private void drawTitleScreen() {
        this.g2.setColor(new Color(0, 0, 0));
        this.g2.fillRect(0, 0, this.gp.getScreenWidth(), this.gp.getScreenHeight());

        // TITLE NAME
        this.g2.setFont(this.g2.getFont().deriveFont(Font.BOLD, 96F));
        String text = "Sawit sawitan";
        int x = getXforCenteredText(text);
        int y = this.gp.getTileSize() * 3;

        // SHODOW
        this.g2.setColor(Color.gray);
        this.g2.drawString(text, x + 5, y + 5);
        // MAIN COLOR
        this.g2.setColor(Color.white);
        this.g2.drawString(text, x, y);

        // BLUE BOY IMAGE
        x = this.gp.getScreenWidth() / 2 - (this.gp.getTileSize() * 2) / 2;
        y += this.gp.getTileSize() * 2;
        this.g2.drawImage(
                this.gp.getPlayerMainCharacter(),
                x,
                y,
                this.gp.getTileSize() * 2,
                this.gp.getTileSize() * 2,
                null);

        // MENU
        this.g2.setFont(this.g2.getFont().deriveFont(Font.BOLD, 48f));

        text = "NEW GAME";
        x = getXforCenteredText(text);
        y += this.gp.getTileSize() * 3.5;
        this.g2.drawString(text, x, y);
        if (this.commandNum == 0) {
            this.g2.drawString(">", x - this.gp.getTileSize(), y);
        }

        text = "LOAD GAME";
        x = getXforCenteredText(text);
        y += this.gp.getTileSize();
        this.g2.drawString(text, x, y);
        if (this.commandNum == 1) {
            this.g2.drawString(">", x - this.gp.getTileSize(), y);
        }

        text = "QUIT";
        x = getXforCenteredText(text);
        y += this.gp.getTileSize();
        this.g2.drawString(text, x, y);
        if (this.commandNum == 2) {
            this.g2.drawString(">", x - this.gp.getTileSize(), y);
        }
    }

    public int getFrameX() {
        return frameX;
    }

    public int getFrameY() {
        return frameY;
    }

    public int getSlotCols() {
        return frameX - 3;
    }

    public int getSlotRows() {
        return frameY - 2;
    }

    private void drawInventory() {
        UIElement ui = new UIElement(g2, gp);

        // MAIN FRAME
        int windowX = this.gp.getTileSize() * 10;
        int windowY = this.gp.getTileSize();
        int frameWidth = (this.gp.getTileSize() * this.frameX) + 1;
        int frameHeight = (this.gp.getTileSize() * this.frameY) + 1;

        ui.beginWindow(windowX, windowY, frameWidth, frameHeight);

        // SLOT SETUP
        int slotCols = getSlotCols();
        int slotRows = getSlotRows();
        int slotSize = this.gp.getTileSize() + 3;

        int totalSlotsWidth = (slotCols * slotSize) - 3;
        int totalSlotsHeight = (slotRows * slotSize) - 3;

        final int slotXstart = windowX + (frameWidth - totalSlotsWidth) / 2;
        final int slotYstart = windowY + (frameHeight - totalSlotsHeight) / 2;

        // DRAW SLOTS
        int totalSlots = slotCols * slotRows;
        for (int i = 0; i < totalSlots; i++) {
            int col = i % slotCols;
            int row = i / slotCols;
            int x = slotXstart + (col * slotSize);
            int y = slotYstart + (row * slotSize);

            Entity item =
                    i < this.gp.getPlayerInventorySize() ? this.gp.getPlayerInventory(i) : null;
            boolean equipped = item != null && item == this.gp.getPlayerCurrentWeapon();
            boolean selected = (col == slotCol && row == slotRow);

            ui.inventorySlot(
                    x,
                    y,
                    this.gp.getTileSize(),
                    item != null ? item.getDown1() : null,
                    equipped,
                    selected);

            // DRAW AMOUNT
            if (item != null && item.stackable) {
                g2.setFont(g2.getFont().deriveFont(Font.BOLD, 14F));
                g2.setColor(Color.white);
                int amountX = x + gp.getTileSize() - 15;
                int amountY = y + gp.getTileSize() - 5;
                g2.drawString(String.valueOf(item.amount), amountX, amountY);
            }
        }

        // DESCRIPTION FRAME
        int dFrameX = windowX;
        int dFrameY = windowY + frameHeight + 10;
        int dFrameWidth = frameWidth;
        int dFrameHeight = this.gp.getTileSize() * 2;

        int itemIndex = getItemIndexOnSlot();
        if (itemIndex < this.gp.getPlayerInventorySize()) {
            ui.beginWindow(dFrameX, dFrameY, dFrameWidth, dFrameHeight);
            Entity selectedItem = this.gp.getPlayerInventory(itemIndex);
            ui.multilineText(selectedItem.getDescription(), dFrameX + 20, dFrameY + 35, 32, 20f);
        }

        // HELP TEXT
        ui.text(
                "Enter: Pilih",
                windowX + 20,
                windowY + frameHeight - 15,
                14f,
                UIElement.BORDER_COLOR);
    }

    private void drawPlayerGold() {
        String text = "$" + this.gp.getPlayerGold();
        this.g2.setFont(this.g2.getFont().deriveFont(Font.BOLD, 40F));
        int textWidth = (int) this.g2.getFontMetrics().getStringBounds(text, g2).getWidth();

        int imageWidth = this.gp.getTileSize();
        int imageHeight = this.gp.getTileSize();

        int imageX =
                this.gp.getScreenWidth() - imageWidth - textWidth - (this.gp.getTileSize() / 2);
        int imageY = (this.gp.getTileSize() / 2);

        this.g2.drawImage(gold, imageX, imageY, imageWidth, imageHeight, null);

        int textX = imageX + imageWidth + 2;
        int textY = imageY + (imageHeight / 2) + 15;

        // Draw Shadow
        this.g2.setColor(Color.black);
        this.g2.drawString(text, textX + 2, textY + 2);
        // Draw Text
        this.g2.setColor(Color.white);
        this.g2.drawString(text, textX, textY);
    }

    private void drawPauseScreen() {
        if (subState == 0) {
            pauseMain();
        } else if (subState == 1) {
            pauseSettings();
        } else if (subState == 2) {
            drawAchievementScreen();
        }
    }

    private void pauseMain() {
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, gp.getScreenWidth(), gp.getScreenHeight());

        int width = gp.getTileSize() * 8;
        int height = gp.getTileSize() * 10;
        int x = (gp.getScreenWidth() - width) / 2;
        int y = (gp.getScreenHeight() - height) / 2;

        drawSubWindow(x, y, width, height);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48F));
        String text = "PAUSED";
        int tx = getXforCenteredText(text);
        int ty = y + gp.getTileSize() * 2;
        g2.setColor(new Color(255, 240, 200));
        g2.drawString(text, tx, ty);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 28F));
        g2.setColor(Color.white);

        String[] options = {
            "Resume", "Settings", "Achievements", "Reload Game", "Game Menu", "Exit Game"
        };
        for (int i = 0; i < options.length; i++) {
            text = options[i];
            tx = getXforCenteredText(text);
            ty += gp.getTileSize() * 1.1;
            g2.drawString(text, tx, ty);
            if (commandNum == i) {
                g2.drawString(">", tx - 30, ty);
            }
        }
    }

    private void pauseSettings() {
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, gp.getScreenWidth(), gp.getScreenHeight());

        int width = gp.getTileSize() * 10;
        int height = gp.getTileSize() * 9;
        int x = (gp.getScreenWidth() - width) / 2;
        int y = (gp.getScreenHeight() - height) / 2;

        drawSubWindow(x, y, width, height);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 42F));
        String text = "SETTINGS";
        int tx = getXforCenteredText(text);
        int ty = y + (int) (gp.getTileSize() * 1.8);
        g2.setColor(new Color(255, 240, 200));
        g2.drawString(text, tx, ty);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 24F));
        g2.setColor(Color.white);

        // Music Volume (commandNum 0)
        text = "Music Volume";
        tx = x + gp.getTileSize();
        ty += (int) (gp.getTileSize() * 1.5);
        g2.drawString(text, tx, ty);
        drawVolumeBar(x + width - gp.getTileSize() * 4, ty - 20, gp.music.getVolumeScale());
        if (commandNum == 0) g2.drawString(">", tx - 25, ty);

        // SE Volume (commandNum 1)
        text = "SE Volume";
        ty += gp.getTileSize();
        g2.drawString(text, tx, ty);
        drawVolumeBar(x + width - gp.getTileSize() * 4, ty - 20, gp.se.getVolumeScale());
        if (commandNum == 1) g2.drawString(">", tx - 25, ty);

        // Window Size (commandNum 2)
        text = "Window Size";
        ty += gp.getTileSize();
        g2.drawString(text, tx, ty);
        String scaleLabel = "< " + GamePanel.WINDOW_SCALE_LABELS[gp.getWindowScaleIndex()] + " >";
        int labelX = x + width - gp.getTileSize() * 4;
        g2.drawString(scaleLabel, labelX, ty);
        if (commandNum == 2) g2.drawString(">", tx - 25, ty);

        // Back (commandNum 3)
        text = "Back";
        tx = getXforCenteredText(text);
        ty += (int) (gp.getTileSize() * 1.5);
        g2.drawString(text, tx, ty);
        if (commandNum == 3) g2.drawString(">", tx - 25, ty);
    }

    private void drawVolumeBar(int x, int y, int scale) {
        int width = 120;
        int height = 24;

        g2.setColor(new Color(70, 50, 30));
        g2.fillRect(x, y, width, height);
        g2.setColor(new Color(214, 166, 82));
        g2.drawRect(x, y, width, height);

        int volumeWidth = (width / 5) * scale;
        g2.fillRect(x, y, volumeWidth, height);
    }

    private void drawAchievementScreen() {
        int ts = gp.getTileSize();
        java.util.List<Achievement> all = new java.util.ArrayList<>(gp.getAchievements().getAll());
        int total = all.size();
        int unlocked = (int) all.stream().filter(Achievement::isUnlocked).count();

        // Panel sizing: tall enough for all entries
        int lineH = 36;
        int headerH = ts + 20;
        int footerH = ts;
        int width = ts * 13;
        int height = headerH + total * lineH + footerH + 24;
        int x = (gp.getScreenWidth() - width) / 2;
        int y = (gp.getScreenHeight() - height) / 2;

        // Dim background
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, gp.getScreenWidth(), gp.getScreenHeight());
        drawSubWindow(x, y, width, height);

        // Title + progress fraction
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20F));
        g2.setColor(new Color(255, 240, 200));
        String title = "ACHIEVEMENTS  " + unlocked + "/" + total;
        g2.drawString(title, getXforCenteredText(title), y + 28);

        // Divider
        g2.setColor(new Color(180, 150, 80, 160));
        g2.drawLine(x + 16, y + 36, x + width - 16, y + 36);

        // Progress bar
        int barW = width - 32;
        int barH = 6;
        int barX = x + 16;
        int barY = y + 40;
        g2.setColor(new Color(50, 50, 50));
        g2.fillRoundRect(barX, barY, barW, barH, 4, 4);
        int filled = total > 0 ? (barW * unlocked / total) : 0;
        g2.setColor(new Color(100, 210, 100));
        if (filled > 0) g2.fillRoundRect(barX, barY, filled, barH, 4, 4);

        // Achievement list
        int textX = x + 16;
        int textY = y + headerH + 20;
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 14F));

        for (Achievement a : all) {
            if (a.isUnlocked()) {
                // Badge terbuka
                g2.setColor(new Color(80, 180, 80));
                g2.fillRoundRect(textX, textY - 13, 18, 16, 4, 4);
                g2.setColor(new Color(220, 255, 220));
                g2.drawString("V", textX + 4, textY - 1);

                // Nama achievement (bold)
                g2.setFont(g2.getFont().deriveFont(Font.BOLD, 14F));
                g2.setColor(new Color(130, 230, 130));
                g2.drawString(a.name, textX + 24, textY - 1);

                // Deskripsi (italic kecil)
                g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 12F));
                g2.setColor(new Color(180, 210, 180));
                g2.drawString(a.description, textX + 24, textY + 14);
            } else {
                // Badge terkunci
                g2.setColor(new Color(80, 80, 80));
                g2.fillRoundRect(textX, textY - 13, 18, 16, 4, 4);
                g2.setColor(new Color(160, 160, 160));
                g2.drawString("?", textX + 5, textY - 1);

                // Placeholder nama
                g2.setFont(g2.getFont().deriveFont(Font.BOLD, 14F));
                g2.setColor(new Color(130, 130, 130));
                g2.drawString("??? (Terkunci)", textX + 24, textY - 1);

                // Clue
                g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 12F));
                g2.setColor(new Color(180, 165, 100));
                g2.drawString("Petunjuk: " + a.clue, textX + 24, textY + 14);
            }
            textY += lineH;
        }

        // Footer hint
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 14F));
        g2.setColor(new Color(160, 160, 160));
        String hint = "[ESC] Kembali";
        g2.drawString(hint, getXforCenteredText(hint), y + height - 10);
    }

    private void drawGameOverScreen() {
        this.g2.setColor(new Color(0, 0, 0, 220));
        this.g2.fillRect(0, 0, this.gp.getScreenWidth(), this.gp.getScreenHeight());

        this.g2.setFont(this.g2.getFont().deriveFont(Font.BOLD, 72F));
        String title = "GAME OVER";
        int x = getXforCenteredText(title);
        int y = this.gp.getScreenHeight() / 2 - 20;
        this.g2.setColor(Color.red);
        this.g2.drawString(title, x, y);

        this.g2.setFont(this.g2.getFont().deriveFont(Font.BOLD, 26F));
        String reason = this.gp.getFarmState().getLastNotification();
        String subtitle = "Yah Kamu Cupu sih, coba lagi deh!";
        if (reason != null && reason.toLowerCase().contains("gold habis")) {
            subtitle = "Gold Anda Habis Dasar Miskin Gausah so soan nyawit deh lu";
        }
        int subtitleX = getXforCenteredText(subtitle);
        this.g2.setColor(new Color(255, 210, 120));
        this.g2.drawString(subtitle, subtitleX, y + 42);

        this.g2.setFont(this.g2.getFont().deriveFont(Font.PLAIN, 22F));
        this.g2.setColor(Color.white);
        drawWrappedText(
                reason,
                this.gp.getScreenWidth() / 2,
                y + 86,
                this.gp.getScreenWidth() - 220,
                28,
                true);

        this.g2.setFont(this.g2.getFont().deriveFont(Font.PLAIN, 24F));
        String text = "Tekan Enter untuk kembali ke home.";
        int textX = getXforCenteredText(text);
        this.g2.drawString(text, textX, this.gp.getScreenHeight() - 80);
    }

    private void drawBanner() {
        if (this.activeBanner == null) {
            return;
        }

        this.g2.setColor(new Color(0, 0, 0, 100));
        this.g2.fillRect(0, 0, this.gp.getScreenWidth(), this.gp.getScreenHeight());

        this.activeBanner.age++;

        int boxW = this.gp.getScreenWidth() - 100;
        int boxH = this.gp.getTileSize() * 3 + 20;
        int boxX = 50;
        int boxY = this.gp.getScreenHeight() - boxH - 20;

        int portraitSize = 220;
        int portraitX = boxX - 40;
        int portraitY = boxY + boxH - portraitSize + 20;

        int bubbleX = boxX + 140;
        int bubbleY = boxY;
        int bubbleW = boxW - 140;
        int bubbleH = boxH;

        this.g2.setColor(new Color(245, 235, 210, 245));
        this.g2.fillRoundRect(bubbleX, bubbleY, bubbleW, bubbleH, 20, 20);

        this.g2.setColor(new Color(200, 190, 170, 255));
        this.g2.setStroke(new BasicStroke(4));
        this.g2.drawRoundRect(bubbleX, bubbleY, bubbleW, bubbleH, 20, 20);

        if (this.managerIcon != null) {
            this.g2.drawImage(
                    this.managerIcon, portraitX, portraitY, portraitSize, portraitSize, null);
        }

        int textX = bubbleX + 32;
        int textY = bubbleY + 32;
        this.g2.setColor(Color.BLACK);
        this.g2.setFont(this.g2.getFont().deriveFont(Font.PLAIN, 22F));
        int displayedChars = this.activeBanner.age / 2;
        if (displayedChars > this.activeBanner.detail.length()) {
            displayedChars = this.activeBanner.detail.length();
        }

        String displayedText = this.activeBanner.detail.substring(0, displayedChars);
        List<String> detailLines = wrapText(displayedText, bubbleW - 48);

        int detailY = bubbleY + 48;
        for (String line : detailLines) {
            this.g2.drawString(line, textX, detailY);
            detailY += 24;
        }

        if (displayedChars == this.activeBanner.detail.length()) {
            if (this.activeBanner.age % 60 < 30) {
                this.g2.setFont(this.g2.getFont().deriveFont(Font.BOLD, 14F));
                this.g2.drawString(
                        "[ENTER] Lanjut", bubbleX + bubbleW - 110, bubbleY + bubbleH - 15);
            }
        }
    }

    private void drawOverview() {
        FarmGrid grid = this.gp.getFarmSystem().getFarmGrid();

        int boxX = this.gp.getTileSize() / 2;
        int boxY = this.gp.getTileSize() / 2;
        int boxW = this.gp.getTileSize() * 5;
        int boxH = this.gp.getTileSize() * 4;

        this.g2.setColor(new Color(42, 32, 24, 200));
        this.g2.fillRoundRect(boxX, boxY, boxW, boxH, 12, 12);
        this.g2.setColor(new Color(238, 204, 126, 220));
        this.g2.drawRoundRect(boxX, boxY, boxW, boxH, 12, 12);

        this.g2.setFont(this.g2.getFont().deriveFont(Font.BOLD, 15F));
        this.g2.setColor(new Color(250, 230, 190));

        int textX = boxX + 12;
        int textY = boxY + 22;
        int lineH = 18;

        this.g2.drawString("=== Overview ===", textX, textY);
        this.g2.drawString(
                "Total Panen : " + this.gp.getFarmState().getTotalHarvested() + " TBS",
                textX,
                textY + lineH);
        this.g2.drawString(
                "Total Jual  : " + this.gp.getFarmState().getTotalSold() + " TBS",
                textX,
                textY + lineH * 2);
        this.g2.drawString(
                "Lahan Aktif : " + grid.countByType(FarmTileType.SAWIT), textX, textY + lineH * 3);
        this.g2.drawString("Siap Panen  : " + grid.countReadyTiles(), textX, textY + lineH * 4);
    }

    private void drawFarmStatus() {
        int boxX = this.gp.getTileSize() / 2;
        int boxY = this.gp.getTileSize();
        int boxW = this.gp.getTileSize() * 6;
        int boxH = this.gp.getTileSize() * 7;

        this.g2.setColor(new Color(42, 32, 24, 200));
        this.g2.fillRoundRect(boxX, boxY, boxW, boxH, 12, 12);
        this.g2.setColor(new Color(238, 204, 126, 220));
        this.g2.drawRoundRect(boxX, boxY, boxW, boxH, 12, 12);

        this.g2.setFont(this.g2.getFont().deriveFont(Font.BOLD, 17F));
        this.g2.setColor(new Color(250, 230, 190));

        int textX = boxX + 14;
        int textY = boxY + 24;
        int lineHeight = 17;
        this.g2.drawString("Hari: " + this.gp.getFarmState().getDay(), textX, textY);
        this.g2.drawString(
                "Jam: " + String.format("%02d:00", this.gp.getFarmState().getHour()),
                textX,
                textY + lineHeight);
        this.g2.drawString(
                "Inventory: " + this.gp.getFarmState().getInventory() + " TBS",
                textX,
                textY + lineHeight * 2);
        this.g2.drawString(
                "Risk: " + this.gp.getFarmState().getRiskScore(), textX, textY + lineHeight * 3);
        this.g2.drawString(
                "Rep: " + this.gp.getFarmState().getReputation(), textX, textY + lineHeight * 4);
        this.g2.drawString(
                "Tanpa tanam: " + this.gp.getFarmSystem().getDaysWithoutPlanting() + " hari",
                textX,
                textY + lineHeight * 5);
        this.g2.drawString(
                "Auto Tanam: " + (this.gp.getFarmSystem().isAutoPlantEnabled() ? "ON" : "OFF"),
                textX,
                textY + lineHeight * 6);
        this.g2.drawString(
                "Auto Panen: " + (this.gp.getFarmSystem().isAutoHarvestEnabled() ? "ON" : "OFF"),
                textX,
                textY + lineHeight * 7);
    }

    private void drawControlHint() {
        this.g2.setFont(this.g2.getFont().deriveFont(Font.PLAIN, 14F));
        String text =
                "E/Enter: aksi kebun | F: serang | B: firebreak (-gold) | H: auto tanam | K: auto panen | J: auto jual | N: next day | Q: jual stok | P: pause";
        int x = 16;
        int y = this.gp.getScreenHeight() - 18;

        this.g2.setColor(new Color(0, 0, 0, 140));
        this.g2.drawString(text, x + 1, y + 1);
        this.g2.setColor(Color.white);
        this.g2.drawString(text, x, y);
    }

    public int getItemIndexOnSlot() {
        int itemIndex = this.slotCol + (this.slotRow * getSlotCols());
        return itemIndex;
    }

    public int getXforCenteredText(String text) {
        int length = (int) this.g2.getFontMetrics().getStringBounds(text, this.g2).getWidth();
        int x = this.gp.getScreenWidth() / 2 - length / 2;
        return x;
    }

    public int getXForAlignToRightText(String text, int tailX) {
        int length = (int) this.g2.getFontMetrics().getStringBounds(text, this.g2).getWidth();
        int x = tailX - length;
        return x;
    }

    private List<String> wrapText(String text, int maxWidth) {
        ArrayList<String> lines = new ArrayList<>();
        if (text == null || text.isBlank()) {
            lines.add("");
            return lines;
        }

        String[] paragraphs = text.split("\\n");
        for (String paragraph : paragraphs) {
            String normalized = paragraph.trim();
            if (normalized.isEmpty()) {
                lines.add("");
                continue;
            }

            String[] words = normalized.split("\\s+");
            StringBuilder line = new StringBuilder();
            for (String word : words) {
                String testLine = line.length() == 0 ? word : line + " " + word;
                int width = this.g2.getFontMetrics().stringWidth(testLine);
                if (width > maxWidth && line.length() > 0) {
                    lines.add(line.toString());
                    line = new StringBuilder(word);
                } else {
                    if (line.length() > 0) {
                        line.append(" ");
                    }
                    line.append(word);
                }
            }
            if (line.length() > 0) {
                lines.add(line.toString());
            }
        }

        if (lines.isEmpty()) {
            lines.add("");
        }
        return lines;
    }

    private void drawWrappedText(
            String text, int centerX, int startY, int maxWidth, int lineHeight, boolean centered) {
        List<String> lines = wrapText(text, maxWidth);
        int y = startY;
        for (String line : lines) {
            int drawX =
                    centered
                            ? centerX
                                    - (int)
                                                    this.g2
                                                            .getFontMetrics()
                                                            .getStringBounds(line, this.g2)
                                                            .getWidth()
                                            / 2
                            : centerX;
            this.g2.drawString(line, drawX, y);
            y += lineHeight;
        }
    }

    private void drawSubWindow(int x, int y, int width, int height) {
        Color c = new Color(42, 32, 24, 215);
        this.g2.setColor(c);
        this.g2.fillRoundRect(x, y, width, height, 35, 35);

        c = new Color(214, 166, 82, 220);
        this.g2.setColor(c);
        this.g2.setStroke(new BasicStroke(5));
        this.g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
    }
}
