package com.example.lab4game.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity
public class ResultEntity {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "points")
    public int points;

    @ColumnInfo(name = "date")
    public long  date;

    @ColumnInfo(name = "game-time")
    public long game_time;
    public ResultEntity(int points, long date, long game_time){
        this.points = points;
        this.date = date;
        this.game_time = game_time;
    }
    public ResultEntity() {

    }
}

