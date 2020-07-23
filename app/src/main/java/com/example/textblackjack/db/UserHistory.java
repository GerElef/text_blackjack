package com.example.textblackjack.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class UserHistory {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "Game_Session_Start")
    private String timestampStart;

    @NonNull
    @ColumnInfo(name = "Session_End")
    private String timestampEnd;

    @ColumnInfo(name = "Game_Type")
    private int gameType;

    public UserHistory(@NonNull String timestampStart, @NonNull String timestampEnd, int gameType){
        setTimestampStart(timestampStart);
        setTimestampEnd(timestampEnd);
        setGameType(gameType);
    }

    private void setGameType(int gameType){
        this.gameType = gameType;
    }

    private void setTimestampStart(@NonNull String s){
        this.timestampStart = s;
    }

    private void setTimestampEnd(@NonNull String s){
        this.timestampEnd = s;
    }

    int getGameType() {
        return this.gameType;
    }

    @NonNull
    public String getTimestampStart(){
        return this.timestampStart;
    }

    @NonNull
    public String getTimestampEnd(){
        return this.timestampEnd;
    }
}
