package com.example.gamebacklog;

public enum GameStatus {
    WANT_TO_PLAY(0),
    PLAYING(1),
    STALLED(2),
    DROPPED(3);

    public final int value;
    GameStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
