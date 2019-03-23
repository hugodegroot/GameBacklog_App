package com.example.gamebacklog;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.CalendarContract;

import java.util.Calendar;
import java.util.Date;

@Entity(tableName = "game_list")
@TypeConverters(GameStatusConverter.class)
public class Game implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "gametitle")
    private String gameTitle;

    @ColumnInfo(name = "gameplatform")
    private String gamePlatform;

    @ColumnInfo(name = "gamedate")
    private Long gameDate;

    @ColumnInfo(name = "gamestatus")
    private GameStatus gameStatus;

    public Game(String gameTitle, String gamePlatform, GameStatus gameStatus) {
        this.gameTitle = gameTitle;
        this.gamePlatform = gamePlatform;
        this.gameStatus = gameStatus;

        this.gameDate = Calendar.getInstance().getTimeInMillis();
    }

    protected Game(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.gameTitle = in.readString();
        this.gamePlatform = in.readString();
        this.gameDate = in.readLong();
        int tmpStatus = in.readInt();
        this.gameStatus = tmpStatus == -1 ? null : GameStatus.values()[tmpStatus];
    }

    public static final Parcelable.Creator<Game> CREATOR = new Parcelable.Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGameTitle() {
        return gameTitle;
    }

    public void setGameTitle(String gameTitle) {
        this.gameTitle = gameTitle;
    }

    public String getGamePlatform() {
        return gamePlatform;
    }

    public void setGamePlatform(String gamePlatform) {
        this.gamePlatform = gamePlatform;
    }

    public Long getGameDate() {
        return gameDate;
    }

    public void setGameDate(Long gameDate) {
        this.gameDate = gameDate;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeString(gameTitle);
        dest.writeString(gamePlatform);
        dest.writeLong(gameDate);
        dest.writeInt(this.gameStatus == null ? -1 : this.gameStatus.ordinal());
    }
}


