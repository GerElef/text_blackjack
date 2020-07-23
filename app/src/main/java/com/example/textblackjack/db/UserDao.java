package com.example.textblackjack.db;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface UserDao {
    //Query that returns the entire matchhistory of the specified gametype
    @Query("SELECT * FROM UserHistory INNER JOIN UserSessionBank ON UserHistory.Game_Session_Start = UserSessionBank.Bank_Session_Start WHERE UserHistory.Game_Type LIKE :gametype ORDER BY UserHistory.Game_Session_Start ASC")
    UserHistoryAndBank[] getUserMatchHistory(int gametype);

    //Query that returns the latest bank record of the specified gametpye
    @Query("SELECT Session_End_Balance FROM UserSessionBank WHERE UserSessionBank.Bank_Game_Type LIKE :gametype ORDER BY Bank_Session_Start DESC LIMIT 1")
    int getLatestBankRecord(int gametype);

    //Query that inserts timerow and bankrow specified
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTimeAndBank(@NonNull UserHistory userhistoryrow, @NonNull UserSessionBank userbankrow);
}
