package com.example.textblackjack.db.callable;

import androidx.annotation.NonNull;

import com.example.textblackjack.db.UserDao;

import java.util.concurrent.Callable;

public class ReturnDatabaseLatestBankCallable implements Callable<Integer> {
    /*Callable object to create instance.
     *Returns latest bank of a specified gametype.*/
    private UserDao userdao;

    private int gametype;

    public ReturnDatabaseLatestBankCallable(@NonNull UserDao userdao, int gametype){
        this.userdao = userdao;
        this.gametype = gametype;
    }

    @NonNull
    public Integer call(){
        return this.userdao.getLatestBankRecord(this.gametype);
    }
}
