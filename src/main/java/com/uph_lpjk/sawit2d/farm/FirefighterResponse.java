package com.uph_lpjk.sawit2d.farm;

public class FirefighterResponse {
    private final String message;
    private final FarmBurnHandledType handledType;

    public FirefighterResponse(String message, FarmBurnHandledType handledType) {
        this.message = message;
        this.handledType = handledType;
    }

    public String getMessage() {
        return message;
    }

    public FarmBurnHandledType getHandledType() {
        return handledType;
    }
}
