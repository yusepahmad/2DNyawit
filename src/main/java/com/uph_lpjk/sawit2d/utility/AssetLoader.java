package com.uph_lpjk.sawit2d.utility;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class AssetLoader {

    private final UtilityTools utilityTools = new UtilityTools();

    public BufferedImage loadImage(int width, int height, String imagePath) {
        if (imagePath == null || imagePath.isBlank()) {
            return createPlaceholder(width, height);
        }

        // Ensure the path ends with .png
        String fullPath = imagePath.endsWith(".png") ? imagePath : imagePath + ".png";

        // Ensure the path starts with /
        String resourcePath = fullPath.startsWith("/") ? fullPath : "/" + fullPath;

        try (InputStream inputStream = getClass().getResourceAsStream(resourcePath)) {
            if (inputStream != null) {
                BufferedImage image = ImageIO.read(inputStream);
                return utilityTools.scaledImage(image, width, height);
            }
        } catch (IOException ignored) {
        }

        return createPlaceholder(width, height);
    }

    private BufferedImage createPlaceholder(int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setColor(new Color(255, 0, 255, 120));
        g2.fillRect(0, 0, width, height);
        g2.dispose();
        return image;
    }
}
