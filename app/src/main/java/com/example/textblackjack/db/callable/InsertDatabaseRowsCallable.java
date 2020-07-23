package com.example.textblackjack.db.callable;

import androidx.annotation.NonNull;

import com.example.textblackjack.db.UserDao;
import com.example.textblackjack.db.UserHistory;
import com.example.textblackjack.db.UserSessionBank;

import java.util.concurrent.Callable;

public class InsertDatabaseRowsCallable implements Callable<Boolean> {
    /*Callable object to create instance.
     *Inserts all input in the database and serves as a thread.*/
    private UserDao userdao;

    private UserHistory timerow;

    private UserSessionBank bankrow;

    public InsertDatabaseRowsCallable(@NonNull UserDao userdao, @NonNull UserHistory timerow, @NonNull UserSessionBank bankrow){
        this.userdao = userdao;
        this.timerow = timerow;
        this.bankrow = bankrow;
    }

    @NonNull
    public Boolean call(){
        this.userdao.insertTimeAndBank(this.timerow, this.bankrow);
        return Boolean.TRUE;
    }

}
