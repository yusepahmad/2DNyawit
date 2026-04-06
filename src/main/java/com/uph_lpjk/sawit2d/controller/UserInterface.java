package com.uph_lpjk.sawit2d.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import com.uph_lpjk.sawit2d.entity.Entity;
import com.uph_lpjk.sawit2d.object.ObjGold;


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
        private int age;
        private final int duration;

        private Banner(BannerTone tone, String title, String detail, int duration) {
            this.tone = tone;
            this.title = title;
            this.detail = detail;
            this.duration = duration;
            this.age = 0;
        }
    }

    final private GamePanel gp;
    
    protected BufferedImage gold;
    
    private Graphics2D g2;
    private Font maruMonica, purisaBold;
    
    private Banner activeBanner;

    private int commandNum = 0;
    private int slotCol = 0;
    private int slotRow = 0;
    
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
        this.activeBanner = new Banner(tone, title, detail, 240);
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
            return new Banner(BannerTone.GAME_OVER, "Game Over", text, 240);
        }
        if (lower.contains("hujan")) {
            return new Banner(BannerTone.WEATHER, "Hujan Turun", text, 240);
        }
        if (lower.contains("api") || lower.contains("kebakaran") || lower.contains("firebreak")) {
            return new Banner(BannerTone.DANGER, "Kebakaran", text, 240);
        }
        if (lower.contains("jual") || lower.contains("stok")) {
            return new Banner(BannerTone.ECONOMY, "Transaksi", text, 240);
        }
        if (lower.contains("panen") || lower.contains("tanam") || lower.contains("tumbuh")) {
            return new Banner(BannerTone.SUCCESS, "Aktivitas Kebun", text, 240);
        }
        if (lower.contains("gold tidak cukup") || lower.contains("tidak bisa")) {
            return new Banner(BannerTone.WARNING, "Peringatan", text, 240);
        }
        return new Banner(BannerTone.INFO, "Info", text, 240);
    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;

        // SET FONT
        this.g2.setFont(this.maruMonica);
        // this.g2.setFont(purisaBold);

        this.g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        this.g2.setColor(Color.white);

        switch (this.gp.getGameState()) {
            case TITLE: drawTitleScreen(); break;
            case PLAY: 
                drawPlayerGold();
                drawFarmStatus();
                drawBanner();
                drawControlHint();
                break;
            case PAUSE: drawPauseScreen(); break;
            case GAME_OVER: drawGameOverScreen(); break;
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
        y += this.gp.getTileSize()*2;
        this.g2.drawImage(this.gp.getPlayerDown1(), x, y, this.gp.getTileSize() * 2, this.gp.getTileSize() * 2, null);

        // MENU
        this.g2.setFont(this.g2.getFont().deriveFont(Font.BOLD, 48f));

        text = "NEW GAME";
        x = getXforCenteredText(text);
        y += this.gp.getTileSize() * 3.5;
        this.g2.drawString(text, x, y);
        if(this.commandNum == 0) {
            this.g2.drawString(">", x - this.gp.getTileSize(), y);
        }

        text = "LOAD GAME";
        x = getXforCenteredText(text);
        y += this.gp.getTileSize();
        this.g2.drawString(text, x, y);
        if(this.commandNum == 1) {
            this.g2.drawString(">", x - this.gp.getTileSize(), y);
        }

        text = "QUIT";
        x = getXforCenteredText(text);
        y += this.gp.getTileSize();
        this.g2.drawString(text, x, y);
        if(this.commandNum == 2) {
            this.g2.drawString(">", x - this.gp.getTileSize(), y);
        }
    }

    private void drawPlayerGold() {
        String text = "$" + this.gp.getPlayerGold();
        this.g2.setFont(this.g2.getFont().deriveFont(Font.BOLD, 40F));
        int textWidth = (int)this.g2.getFontMetrics().getStringBounds(text, g2).getWidth();

        int imageWidth = this.gp.getTileSize();
        int imageHeight = this.gp.getTileSize();

        // Hitung posisi agar berada di pojok kanan atas
        int imageX = this.gp.getScreenWidth() - imageWidth - textWidth - (this.gp.getTileSize() / 2);
        int imageY = (this.gp.getTileSize() / 2);

        this.g2.drawImage(gold, imageX, imageY, imageWidth, imageHeight, null);
        
        int textX = imageX + imageWidth + 2;
        int textY = imageY + (imageHeight / 2) + 15;

        // Draw Shadow
        this.g2.setColor(Color.black);
        this.g2.drawString(text, textX+2, textY+2);
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

        this.g2.setFont(this.g2.getFont().deriveFont(Font.PLAIN, 24F));
        String text = "Tekan Enter untuk kembali ke home.";
        int textX = getXforCenteredText(text);
        this.g2.setColor(Color.white);
        this.g2.drawString(text, textX, y + 40);
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
        int bannerH = this.gp.getTileSize() + 12;
        int bannerX = (this.gp.getScreenWidth() - bannerW) / 2;
        int bannerY = this.gp.getScreenHeight() - bannerH - (this.gp.getTileSize() / 2);

        float alpha = 1.0f;
        if (this.activeBanner.age < 24) {
            alpha = this.activeBanner.age / 24.0f;
        } else if (this.activeBanner.duration - this.activeBanner.age < 24) {
            alpha = Math.max(0.0f, (this.activeBanner.duration - this.activeBanner.age) / 24.0f);
        }

        Color bg;
        Color accent;
        switch (this.activeBanner.tone) {
            case WEATHER:
                bg = new Color(28, 66, 110);
                accent = new Color(96, 185, 255);
                break;
            case DANGER:
                bg = new Color(95, 28, 28);
                accent = new Color(255, 90, 90);
                break;
            case ECONOMY:
                bg = new Color(42, 68, 34);
                accent = new Color(138, 214, 91);
                break;
            case SUCCESS:
                bg = new Color(32, 78, 52);
                accent = new Color(88, 230, 144);
                break;
            case WARNING:
                bg = new Color(92, 64, 22);
                accent = new Color(255, 193, 71);
                break;
            case GAME_OVER:
                bg = new Color(80, 18, 18);
                accent = new Color(255, 115, 115);
                break;
            default:
                bg = new Color(35, 40, 55);
                accent = new Color(180, 180, 180);
                break;
        }

        java.awt.Composite oldComposite = this.g2.getComposite();
        this.g2.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, alpha));
        this.g2.setColor(new Color(bg.getRed(), bg.getGreen(), bg.getBlue(), 215));
        this.g2.fillRoundRect(bannerX, bannerY, bannerW, bannerH, 22, 22);

        this.g2.setColor(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 220));
        this.g2.fillRoundRect(bannerX, bannerY, 12, bannerH, 22, 22);

        this.g2.setColor(new Color(255, 255, 255, 75));
        this.g2.drawRoundRect(bannerX, bannerY, bannerW, bannerH, 22, 22);

        this.g2.setFont(this.g2.getFont().deriveFont(Font.BOLD, 22F));
        this.g2.setColor(Color.white);
        this.g2.drawString(this.activeBanner.title, bannerX + 22, bannerY + 26);

        this.g2.setFont(this.g2.getFont().deriveFont(Font.PLAIN, 16F));
        this.g2.setColor(new Color(245, 245, 245));
        this.g2.drawString(this.activeBanner.detail, bannerX + 22, bannerY + 47);

        int barW = (int) ((bannerW - 24) * (1.0 - (double) this.activeBanner.age / this.activeBanner.duration));
        this.g2.setColor(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 220));
        this.g2.fillRect(bannerX + 12, bannerY + bannerH - 8, Math.max(0, barW), 4);

        this.g2.setComposite(oldComposite);
    }

    private void drawFarmStatus() {
        int boxX = this.gp.getTileSize() / 2;
        int boxY = this.gp.getTileSize() / 2;
        int boxW = this.gp.getTileSize() * 6;
        int boxH = this.gp.getTileSize() * 6;

        this.g2.setColor(new Color(0, 0, 0, 140));
        this.g2.fillRoundRect(boxX, boxY, boxW, boxH, 16, 16);
        this.g2.setColor(new Color(255, 255, 255, 180));
        this.g2.drawRoundRect(boxX, boxY, boxW, boxH, 16, 16);

        this.g2.setFont(this.g2.getFont().deriveFont(Font.BOLD, 17F));
        this.g2.setColor(Color.white);

        int textX = boxX + 14;
        int textY = boxY + 24;
        int lineHeight = 17;
        this.g2.drawString("Hari: " + this.gp.getFarmState().getDay(), textX, textY);
        this.g2.drawString("Jam: " + String.format("%02d:00", this.gp.getFarmState().getHour()), textX, textY + lineHeight);
        this.g2.drawString("Inventory: " + this.gp.getFarmState().getInventory(), textX, textY + lineHeight * 2);
        this.g2.drawString("Risk: " + this.gp.getFarmState().getRiskScore(), textX, textY + lineHeight * 3);
        this.g2.drawString("Rep: " + this.gp.getFarmState().getReputation(), textX, textY + lineHeight * 4);
        this.g2.drawString("Auto Tanam: " + (this.gp.getFarmSystem().isAutoPlantEnabled() ? "ON" : "OFF"), textX, textY + lineHeight * 5);
        this.g2.drawString("Auto Panen: " + (this.gp.getFarmSystem().isAutoHarvestEnabled() ? "ON" : "OFF"), textX, textY + lineHeight * 6);
    }

    private void drawControlHint() {
        this.g2.setFont(this.g2.getFont().deriveFont(Font.PLAIN, 14F));
        String text = "Klik kiri/E: aksi kebun | Klik kanan/F: firebreak (-gold) | H: auto tanam | K: auto panen | J: auto jual | N: next day | Q: jual stok | P: pause";
        int x = 16;
        int y = this.gp.getScreenHeight() - 18;

        this.g2.setColor(new Color(0, 0, 0, 140));
        this.g2.drawString(text, x + 1, y + 1);
        this.g2.setColor(Color.white);
        this.g2.drawString(text, x, y);
    }

    public int getItemIndexOnSlot() {
        int itemIndex = this.slotCol + (this.slotRow * 5);
        return itemIndex;
    }

    public int getXforCenteredText(String text) {
        int length = (int)this.g2.getFontMetrics().getStringBounds(text, this.g2).getWidth();
        int x = this.gp.getScreenWidth() / 2 - length / 2;
        return x;
    }

    public int getXForAlignToRightText(String text, int tailX) {
        int length = (int)this.g2.getFontMetrics().getStringBounds(text, this.g2).getWidth();
        int x = tailX - length;
        return x;
    }
}
