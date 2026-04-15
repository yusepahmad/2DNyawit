package com.uph_lpjk.sawit2d.farm;

import com.uph_lpjk.sawit2d.controller.GamePanel;

import java.util.function.Consumer;

public class FirefighterEventSystem {
    private static final int FIREFIGHTER_COST = 30;

    public void startFirefighterEvent(
            GamePanel gp, GameState state, Consumer<FirefighterResponse> callback) {
        String title = "Tim Gajah Pemadam Siaga";
        String message =
                "Asap masih naik dari petak kebun.\n"
                        + "Pilih apakah kamu mau memanggil tim gajah pemadam atau menangani api sendiri.";
        String[] options = {"Panggil tim gajah (" + FIREFIGHTER_COST + " gold)", "Tangani sendiri"};

        gp.setGameState(GamePanel.State.EVENT);
        gp.getUserInterface()
                .setupEvent(
                        title,
                        message,
                        options,
                        (Integer selection) -> {
                            FirefighterResponse response;
                            if (selection == 0) {
                                if (gp.getPlayerGold() < FIREFIGHTER_COST) {
                                    response =
                                            new FirefighterResponse(
                                                    "Tim gajah pemadam batal berangkat. Gold tidak cukup. Nyaawit bisa nangani bencana gabisa, dasar miskin",
                                                    FarmBurnHandledType.NONE);
                                } else {
                                    gp.setPlayerGold(-FIREFIGHTER_COST);
                                    state.modifyRisk(-3);
                                    state.addReputation(2);
                                    response =
                                            new FirefighterResponse(
                                                    "Tim gajah pemadam tiba, api lebih cepat terkendali. Gold -"
                                                            + FIREFIGHTER_COST
                                                            + ".",
                                                    FarmBurnHandledType.FIREFIGHTER);
                                }
                            } else {
                                state.modifyRisk(2);
                                state.addReputation(-1);
                                response =
                                        new FirefighterResponse(
                                                "Kamu memadamkan api sendiri. Risiko naik sedikit.",
                                                FarmBurnHandledType.NONE);
                            }

                            gp.setGameState(GamePanel.State.PLAY);
                            if (callback != null) {
                                callback.accept(response);
                            }
                        });
    }
}
