package com.example.lab4game.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;
@Database(entities = {ResultEntity.class}, version = 1)
abstract public class AppDatabase extends RoomDatabase {
    public abstract ResultDAO resultDAO();
}
