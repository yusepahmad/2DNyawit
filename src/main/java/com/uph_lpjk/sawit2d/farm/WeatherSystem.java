package com.uph_lpjk.sawit2d.farm;

import java.util.Random;

public class WeatherSystem {
    private static final double RAIN_CHANCE = 0.3;
    private final Random random = new Random();

    public boolean checkRain() {
        return random.nextDouble() < RAIN_CHANCE;
    }
}
