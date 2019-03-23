package com.example.gamebacklog;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface GameDao {
    @Query("SELECT * FROM game_list")
    LiveData<List<Game>> getAllGames();

    @Insert
    void insertGame(Game game);

    @Insert
    void insertGames(List<Game> games);

    @Delete
    void deleteGame(Game game);

    @Delete
    void deleteGames(List<Game> games);

    @Update
    void updateGame(Game game);
}
