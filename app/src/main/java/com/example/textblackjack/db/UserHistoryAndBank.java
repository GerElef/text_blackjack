package com.example.textblackjack.db;

import androidx.annotation.NonNull;
import androidx.room.Embedded;

//class created so I can return the inner join of these two tables/entities
public class UserHistoryAndBank {
    @Embedded
    UserHistory userhistory;

    @Embedded
    UserSessionBank userbank;

    private void setUserBank(@NonNull UserSessionBank userbank){
        this.userbank = userbank;
    }

    private void setUserHistory(@NonNull UserHistory userhistory){
        this.userhistory = userhistory;
    }

    public UserHistory getUserHistory(){
        return this.userhistory;
    }

    public UserSessionBank getUserBank(){
        return this.userbank;
    }
}
