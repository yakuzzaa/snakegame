package com.example.lab4game.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
@Dao
public interface ResultDAO {
    @Query("SELECT * FROM ResultEntity order by points desc")
    List<ResultEntity> getAll();

    @Insert
    void insert(ResultEntity result);

}
