package com.uph_lpjk.sawit2d.utility;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

public class AssetLoader {

    private final UtilityTools utilityTools = new UtilityTools();

    public BufferedImage loadImage(int width, int height, String... candidates) {
        for (String candidate : candidates) {
            BufferedImage image = loadRawImage(candidate);
            if (image != null) {
                return utilityTools.scaledImage(image, width, height);
            }
        }
        return createPlaceholder(width, height);
    }

    private BufferedImage loadRawImage(String candidate) {
        if (candidate == null || candidate.isBlank()) {
            return null;
        }

        String resourcePath = candidate.startsWith("/") ? candidate : "/" + candidate;
        try (InputStream inputStream = getClass().getResourceAsStream(resourcePath)) {
            if (inputStream != null) {
                return ImageIO.read(inputStream);
            }
        } catch (IOException ignored) {
        }

        Path filePath = Paths.get(candidate);
        if (!Files.exists(filePath) && candidate.startsWith("/")) {
            filePath = Paths.get(candidate.substring(1));
        }
        if (!Files.exists(filePath)) {
            filePath = Paths.get("..", candidate);
        }
        if (!Files.exists(filePath) && candidate.startsWith("/")) {
            filePath = Paths.get("..", candidate.substring(1));
        }

        if (Files.exists(filePath)) {
            try {
                return ImageIO.read(filePath.toFile());
            } catch (IOException ignored) {
            }
        }

        return null;
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
