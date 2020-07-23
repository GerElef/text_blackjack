package com.example.textblackjack.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class UserSessionBank {
    @PrimaryKey @NonNull
    @ColumnInfo(name = "Bank_Session_Start")
    private String timestampStart;

    @ColumnInfo(name = "Bank_Game_Type")
    private int gameType;

    @ColumnInfo(name = "Session_Start_Balance")
    private int startingBalance;

    @ColumnInfo(name = "Session_End_Balance")
    private int endingBalance;

    public UserSessionBank(@NonNull String timestampStart, int gameType, int startingBalance, int endingBalance){
        setTimestampStart(timestampStart);
        setStartingBalance(startingBalance);
        setEndingBalance(endingBalance);
        setGameType(gameType);
    }

    private void setTimestampStart(@NonNull String s){
        this.timestampStart = s;
    }

    private void setGameType(int gameType){
        this.gameType = gameType;
    }

    private void setStartingBalance(int amount){
        this.startingBalance = amount;
    }

    private void setEndingBalance(int amount){
        this.endingBalance = amount;
    }

    @NonNull
    String getTimestampStart(){
        return this.timestampStart;
    }

    int getGameType() {
        return this.gameType;
    }

    public int getStartingBalance(){
        return this.startingBalance;
    }

    public int getEndingBalance(){
        return this.endingBalance;
    }
}