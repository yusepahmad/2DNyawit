package com.uph_lpjk.sawit2d.controller;

import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Sound {
    private Clip clip;
    private URL soundUrl[] = new URL[30];
    private Clip clips[] = new Clip[30];
    private FloatControl fcs[] = new FloatControl[30];
    private FloatControl fc;
    private int volumeScale;
    private final int MAX_VOLUME_SCALE = 4;

    public Sound() {
        volumeScale = 3;
        soundUrl[0] = getClass().getResource("/sounds/spring.wav");
        soundUrl[1] = getClass().getResource("/sounds/coin.wav");
        soundUrl[2] = getClass().getResource("/sounds/powerup.wav");
        soundUrl[3] = getClass().getResource("/sounds/unlock.wav");
        soundUrl[4] = getClass().getResource("/sounds/fanfare.wav");
        soundUrl[5] = getClass().getResource("/sounds/hitmonster.wav");
        soundUrl[6] = getClass().getResource("/sounds/receivedamage.wav");
        soundUrl[7] = getClass().getResource("/sounds/swingwhooshweapon1.wav");
        soundUrl[8] = getClass().getResource("/sounds/levelup.wav");
        soundUrl[9] = getClass().getResource("/sounds/cursor.wav");
        soundUrl[10] = getClass().getResource("/sounds/burning.wav");
        soundUrl[11] = getClass().getResource("/sounds/cuttree.wav");
    }

    public void setFile(int i) {
        try {
            if (clips[i] == null) {
                AudioInputStream ais = AudioSystem.getAudioInputStream(soundUrl[i]);
                clips[i] = AudioSystem.getClip();
                clips[i].open(ais);
                fcs[i] = (FloatControl) clips[i].getControl(FloatControl.Type.MASTER_GAIN);
            }

            clip = clips[i];
            fc = fcs[i];
            clip.setFramePosition(0);
            checkVolume();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play() {
        if (clip != null) {
            clip.start();
        }
    }

    public void loop() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stop() {
        if (clip != null) {
            clip.stop();
        }
    }

    public void checkVolume() {
        if (fc != null) {

            int effectiveScale = Math.min(volumeScale, MAX_VOLUME_SCALE);

            switch (effectiveScale) {
                case 0:
                    fc.setValue(-80f);
                    break;
                case 1:
                    fc.setValue(-20f);
                    break;
                case 2:
                    fc.setValue(-12f);
                    break;
                case 3:
                    fc.setValue(-5f);
                    break;
                case 4:
                    fc.setValue(1f);
                    break;
                case 5:
                    fc.setValue(6f);
                    break;
            }
        }
    }

    public int getVolumeScale() {
        return volumeScale;
    }

    public void setVolumeScale(int scale) {

        if (scale >= 0 && scale <= 5) {
            this.volumeScale = scale;
            checkVolume();
        }
    }
}
