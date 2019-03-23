package com.example.gamebacklog;

import android.arch.persistence.room.TypeConverter;

public class GameStatusConverter {
    @TypeConverter
    public static GameStatus toGameStatus(int statusInt) {
        return GameStatus.values()[statusInt];
    }

    @TypeConverter
    public static int fromGameStatus(GameStatus status) {
        return status.getValue();
    }
}
