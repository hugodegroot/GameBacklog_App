package com.example.gamebacklog;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private GameRepository mRepository;
    private LiveData<List<Game>> mGames;

    public MainViewModel(@NonNull Application application) {
        super(application);
        mRepository = new GameRepository(application.getApplicationContext());
        mGames = mRepository.getAllGames();
    }

    public LiveData<List<Game>> getGames() {
        return mGames;
    }

    public void insert(Game game) {
        mRepository.insert(game);
    }

    public void insert(List<Game> games) {
        mRepository.insert(games);
    }

    public void update(Game game) {
        mRepository.update(game);
    }

    public void delete(List<Game> games) {
        mRepository.delete(games);
    }

    public void delete(Game game) {
        mRepository.delete(game);
    }
}