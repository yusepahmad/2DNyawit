package com.uph_lpjk.sawit2d.controller;

import com.uph_lpjk.sawit2d.entity.Entity;
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
import java.util.List;
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
        private final int duration;

        private Banner(
                BannerTone tone, String title, String detail, int duration, BufferedImage icon) {
            this.tone = tone;
            this.title = title;
            this.detail = detail;
            this.icon = icon;
            this.duration = duration;
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

    // EVENT SYSTEM
    private boolean eventActive = false;
    private String eventTitle = "";
    private String eventMessage = "";
    private String[] eventOptions = new String[0];
    private Consumer<Integer> eventCallback;
    private final List<Rectangle> eventButtonRects = new ArrayList<>();

    private int commandNum = 0;
    private int slotCol = 0;
    private int slotRow = 0;
    private int inventoryTab = 0; // 0: Equipment, 1: Materials

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

        this.infoIcon =
                assetLoader.loadImage(28, 28, "/tile/weather-sunny.png", "/tile/grass00.png");
        this.successIcon =
                assetLoader.loadImage(
                        28, 28, "/tile/earth.png", "/tile/earth.png", "/tile/grass01.png");
        this.warningIcon =
                assetLoader.loadImage(
                        28, 28, "/tile/banjir.png", "/tile/banjir.png", "/tile/water00.png");
        this.dangerIcon = assetLoader.loadImage(28, 28, "/tile/kebakaran.png", "/tile/wall.png");
        this.weatherIcon =
                assetLoader.loadImage(28, 28, "/tile/weather-cloudy.png", "/tile/water01.png");
        this.economyIcon = assetLoader.loadImage(28, 28, "/objects/gold/goldie.png");
        this.gameOverIcon =
                assetLoader.loadImage(
                        28, 28, "/tile/after-kebakaran-2.png", "/tile/after-kebakaran.png");
        this.firefighterIcon =
                assetLoader.loadImage(
                        48,
                        48,
                        "/player/elephant/elephant-front.png",
                        "/player/elephant/elephant-front.png");
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

    public int getInventoryTab() {
        return inventoryTab;
    }

    public void setInventoryTab(int inventoryTab) {
        this.inventoryTab = inventoryTab;
        // Reset cursor when switching tabs
        this.slotCol = 0;
        this.slotRow = 0;
    }

    public Entity getSelectedItem() {
        ArrayList<Entity> filteredItems = new ArrayList<>();
        for (int i = 0; i < this.gp.getPlayerInventorySize(); i++) {
            Entity item = this.gp.getPlayerInventory(i);
            if (inventoryTab == 0
                    && (item.getType() == Entity.Type.EQUIPMENT
                            || item.getType() == Entity.Type.AXE
                            || item.getType() == Entity.Type.SWORD)) {
                filteredItems.add(item);
            } else if (inventoryTab == 1
                    && (item.getType() == Entity.Type.MATERIAL
                            || item.getType() == Entity.Type.CONSUMABLE)) {
                filteredItems.add(item);
            }
        }

        int itemIndex = getItemIndexOnSlot();
        if (itemIndex < filteredItems.size()) {
            return filteredItems.get(itemIndex);
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
        this.activeBanner = createBanner(tone, title, detail);
    }

    public void resetNotifications() {
        this.activeBanner = null;
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
        return new Banner(tone, title, detail, 240, resolveBannerIcon(tone));
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
            case TITLE:
                drawTitleScreen();
                break;
            case PLAY:
                drawPlayerGold();
                drawFarmStatus();
                drawBanner();
                drawControlHint();
                break;
            case CHARACTER:
                drawInventory();
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
        }
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

        // TABS - 50% Width Logic
        int tabWidth = frameWidth / 2;
        int tabHeight = 35;
        int tab1X = windowX;
        int tab2X = windowX + tabWidth;
        int tabY = windowY - tabHeight;

        ui.tabItem("Perlengkapan", tab1X, tabY, tabWidth, tabHeight, inventoryTab == 0);
        ui.tabItem("Bahan Baku", tab2X, tabY, tabWidth, tabHeight, inventoryTab == 1);

        ui.beginWindow(windowX, windowY, frameWidth, frameHeight);

        // SLOT SETUP
        int slotCols = getSlotCols();
        int slotRows = getSlotRows();
        int slotSize = this.gp.getTileSize() + 3;

        int totalSlotsWidth = (slotCols * slotSize) - 3;
        int totalSlotsHeight = (slotRows * slotSize) - 3;

        final int slotXstart = windowX + (frameWidth - totalSlotsWidth) / 2;
        final int slotYstart = windowY + (frameHeight - totalSlotsHeight) / 2;

        // FILTER ITEMS BY TAB
        ArrayList<Entity> filteredItems = new ArrayList<>();
        for (int i = 0; i < this.gp.getPlayerInventorySize(); i++) {
            Entity item = this.gp.getPlayerInventory(i);
            if (inventoryTab == 0
                    && (item.getType() == Entity.Type.EQUIPMENT
                            || item.getType() == Entity.Type.AXE
                            || item.getType() == Entity.Type.SWORD)) {
                filteredItems.add(item);
            } else if (inventoryTab == 1
                    && (item.getType() == Entity.Type.MATERIAL
                            || item.getType() == Entity.Type.CONSUMABLE)) {
                filteredItems.add(item);
            }
        }

        // DRAW SLOTSW
        int totalSlots = slotCols * slotRows;
        for (int i = 0; i < totalSlots; i++) {
            int col = i % slotCols;
            int row = i / slotCols;
            int x = slotXstart + (col * slotSize);
            int y = slotYstart + (row * slotSize);

            Entity item = i < filteredItems.size() ? filteredItems.get(i) : null;
            boolean equipped = item != null && item == this.gp.getPlayerCurrentWeapon();
            boolean selected = (col == slotCol && row == slotRow);

            ui.inventorySlot(
                    x,
                    y,
                    this.gp.getTileSize(),
                    item != null ? item.getDown1() : null,
                    equipped,
                    selected);
        }

        // DESCRIPTION FRAME
        int dFrameX = windowX;
        int dFrameY = windowY + frameHeight + 10;
        int dFrameWidth = frameWidth;
        int dFrameHeight = this.gp.getTileSize() * 2;

        int itemIndex = getItemIndexOnSlot();
        if (itemIndex < filteredItems.size()) {
            ui.beginWindow(dFrameX, dFrameY, dFrameWidth, dFrameHeight);
            Entity selectedItem = filteredItems.get(itemIndex);
            ui.multilineText(selectedItem.getDescription(), dFrameX + 20, dFrameY + 35, 32, 20f);
        }

        // HELP TEXT
        ui.text(
                "Q/E: Ganti Tab | Enter: Pilih",
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
        this.g2.setFont(this.g2.getFont().deriveFont(Font.PLAIN, 80F));
        String text = "PAUSED";
        int x = getXforCenteredText(text);
        int y = this.gp.getScreenHeight() / 2;

        g2.drawString(text, x, y);
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
        String subtitle = "Tanah Anda Disita Negara";
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

        this.activeBanner.age++;
        if (this.activeBanner.age > this.activeBanner.duration) {
            this.activeBanner = null;
            return;
        }

        int bannerW = this.gp.getTileSize() * 10;
        int detailMaxWidth = bannerW - 92;
        List<String> detailLines = wrapText(this.activeBanner.detail, detailMaxWidth);
        int bannerH = Math.max(this.gp.getTileSize() + 16, 62 + (detailLines.size() * 18));
        int bannerX = (this.gp.getScreenWidth() - bannerW) / 2;
        int bannerY = this.gp.getScreenHeight() - bannerH - (this.gp.getTileSize() / 2);

        float alpha = 1.0f;
        if (this.activeBanner.age < 24) {
            alpha = this.activeBanner.age / 24.0f;
        } else if (this.activeBanner.duration - this.activeBanner.age < 24) {
            alpha = Math.max(0.0f, (this.activeBanner.duration - this.activeBanner.age) / 24.0f);
        }

        Color bg = new Color(44, 34, 26);
        Color accent = new Color(214, 166, 82);

        java.awt.Composite oldComposite = this.g2.getComposite();
        this.g2.setComposite(
                java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, alpha));
        this.g2.setColor(new Color(bg.getRed(), bg.getGreen(), bg.getBlue(), 215));
        this.g2.fillRoundRect(bannerX, bannerY, bannerW, bannerH, 22, 22);

        this.g2.setColor(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 220));
        this.g2.fillRoundRect(bannerX, bannerY, 12, bannerH, 22, 22);

        this.g2.setColor(new Color(252, 220, 160, 120));
        this.g2.drawRoundRect(bannerX, bannerY, bannerW, bannerH, 18, 18);

        int iconX = bannerX + 24;
        int iconY = bannerY + 14;

        this.g2.setColor(new Color(255, 230, 170, 30));
        this.g2.fillRoundRect(iconX - 6, iconY - 4, 36, 36, 10, 10);
        if (this.activeBanner.icon != null) {
            this.g2.drawImage(this.activeBanner.icon, iconX, iconY, 24, 24, null);
        }

        this.g2.setFont(this.g2.getFont().deriveFont(Font.BOLD, 21F));
        this.g2.setColor(new Color(255, 240, 200));
        this.g2.drawString(this.activeBanner.title, bannerX + 64, bannerY + 28);

        this.g2.setFont(this.g2.getFont().deriveFont(Font.PLAIN, 15F));
        this.g2.setColor(new Color(245, 220, 180));
        int detailY = bannerY + 49;
        for (String line : detailLines) {
            this.g2.drawString(line, bannerX + 64, detailY);
            detailY += 18;
        }

        int barW =
                (int)
                        ((bannerW - 24)
                                * (1.0
                                        - (double) this.activeBanner.age
                                                / this.activeBanner.duration));
        this.g2.setColor(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 220));
        this.g2.fillRect(bannerX + 12, bannerY + bannerH - 8, Math.max(0, barW), 4);

        this.g2.setComposite(oldComposite);
    }

    private void drawFarmStatus() {
        int boxX = this.gp.getTileSize() / 2;
        int boxY = this.gp.getTileSize() / 2;
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
                "Inventory: " + this.gp.getFarmState().getInventory(),
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
