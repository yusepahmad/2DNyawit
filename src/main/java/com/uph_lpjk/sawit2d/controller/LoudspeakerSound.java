package com.uph_lpjk.sawit2d.controller;

import java.io.File;
import java.net.URL;

import javax.sound.sampled.*;

/**
 * Plays loudspeaker sounds from both classpath resources and absolute file paths. All errors are
 * swallowed so a missing file cannot crash the game.
 */
public class LoudspeakerSound {

    public static void playResource(String resourcePath) {
        try {
            URL url = LoudspeakerSound.class.getResource(resourcePath);
            if (url == null) return;
            AudioInputStream ais = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            clip.start();
        } catch (Exception e) {
            // Missing or incompatible sound file — silently ignore
        }
    }

    public static void playFile(String absolutePath) {
        try {
            File f = new File(absolutePath);
            if (!f.exists()) return;
            AudioInputStream ais = AudioSystem.getAudioInputStream(f);
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            clip.start();
        } catch (Exception e) {
            // Missing or incompatible sound file — silently ignore
        }
    }
}
