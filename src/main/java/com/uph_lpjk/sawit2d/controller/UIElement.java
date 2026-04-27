package com.uph_lpjk.sawit2d.controller;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;

public class UIElement {
    private final Graphics2D g2;
    private final GamePanel gp;

    // Theme Colors
    public static final Color BG_COLOR = new Color(42, 32, 24, 215);
    public static final Color BORDER_COLOR = new Color(214, 166, 82, 180);
    public static final Color ACTIVE_COLOR = new Color(214, 166, 82, 220);
    public static final Color TEXT_COLOR = new Color(250, 230, 190);
    public static final Color GLOW_COLOR = new Color(255, 190, 0);

    public static final Color INACTIVE_COLOR = new Color(30, 22, 16, 230);

    public UIElement(Graphics2D g2, GamePanel gp) {
        this.g2 = g2;
        this.gp = gp;
    }

    public void beginWindow(int x, int y, int width, int height) {
        g2.setColor(BG_COLOR);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        g2.setColor(BORDER_COLOR);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
    }

    public void inventorySlot(
            int x, int y, int size, Image icon, boolean equipped, boolean selected) {
        // Slot background
        g2.setColor(new Color(60, 45, 35, 150));
        g2.fillRoundRect(x, y, size, size, 10, 10);

        if (equipped) {
            g2.setColor(new Color(255, 213, 0, 150));
            g2.fillRoundRect(x, y, size, size, 10, 10);
        }

        if (icon != null) {
            g2.drawImage(icon, x, y, size, size, null);
        }

        if (selected) {
            g2.setColor(GLOW_COLOR);
            g2.setStroke(new BasicStroke(3));
            g2.drawRoundRect(x, y, size, size, 10, 10);
        } else {
            g2.setColor(BORDER_COLOR);
            g2.setStroke(new BasicStroke(1));
            g2.drawRoundRect(x, y, size, size, 10, 10);
        }
    }

    public void text(String text, int x, int y, float size, Color color) {
        g2.setFont(g2.getFont().deriveFont(size));
        g2.setColor(color != null ? color : TEXT_COLOR);
        g2.drawString(text, x, y);
    }

    public void multilineText(String text, int x, int y, int lineHeight, float size) {
        g2.setFont(g2.getFont().deriveFont(size));
        g2.setColor(TEXT_COLOR);
        for (String line : text.split("\n")) {
            g2.drawString(line, x, y);
            y += lineHeight;
        }
    }
}
