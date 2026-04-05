package com.uph_lpjk.sawit2d.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.uph_lpjk.sawit2d.entity.Entity;
import com.uph_lpjk.sawit2d.object.ObjGold;


public class UserInterface {
    
    final private GamePanel gp;
    
    protected BufferedImage gold;
    
    private Graphics2D g2;
    private Font maruMonica, purisaBold;
    
    private boolean messageOn = false;
    private ArrayList<String> messages = new ArrayList<>();
    private ArrayList<Integer> messageCounter = new ArrayList<>();

    private boolean gameFinished = false;
    private String currentDialogue = "";
    private int commandNum = 0;
    private int titleScreenState = 0;
    private int slotCol = 0;
    private int slotRow = 0;
    private int subState = 0;
    
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
        this.messages.add(text);
        this.messageCounter.add(0);
    }

    public void drawMessage() {
        int messageX = this.gp.getTileSize();
        int messageY = this.gp.getTileSize() * 4;

        this.g2.setFont(this.g2.getFont().deriveFont(Font.BOLD, 32F));

        for(int i = 0; i < this.messages.size(); i++) {
            if(this.messages.get(i) != null) {
                this.g2.setColor(Color.black);
                this.g2.drawString(messages.get(i), messageX + 2, messageY + 2);

                this.g2.setColor(Color.white);
                this.g2.drawString(messages.get(i), messageX, messageY);

                int counter = this.messageCounter.get(i) + 1;
                this.messageCounter.set(i, counter);
                messageY += 50;

                if(this.messageCounter.get(i) > 180) {
                    this.messages.remove(i);
                    this.messageCounter.remove(i);
                    i--; // Perbaiki indeks agar tidak melompati elemen berikutnya
                }
            }
        }
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
                drawMessage();
                break;
            case PAUSE: drawPauseScreen(); break;
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
