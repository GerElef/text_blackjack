package com.example.textblackjack.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {UserHistory.class, UserSessionBank.class}, version = 1, exportSchema = false)
public abstract class MatchHistoryDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}
