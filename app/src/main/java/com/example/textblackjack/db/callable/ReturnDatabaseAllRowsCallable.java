package com.example.textblackjack.db.callable;

import androidx.annotation.NonNull;

import com.example.textblackjack.db.UserDao;
import com.example.textblackjack.db.UserHistoryAndBank;

import java.util.concurrent.Callable;

public class ReturnDatabaseAllRowsCallable implements Callable<UserHistoryAndBank[]> {
    /*Callable object to create instance.
     *Returns all rows of a specified gametype */
    private UserDao userdao;

    private int gametype;

    public ReturnDatabaseAllRowsCallable(@NonNull UserDao userdao, int gametype){
        this.userdao = userdao;
        this.gametype = gametype;
    }

    @NonNull
    public UserHistoryAndBank[] call(){
        return this.userdao.getUserMatchHistory(this.gametype);
    }
}
