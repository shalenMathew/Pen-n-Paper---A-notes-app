package com.example.pennpaper.db;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.pennpaper.entity.DataDetails;

import java.util.List;

@Dao
public interface DataDAO {


    @Insert
public long addData(DataDetails dataDetails);

    @Update
public void updateData(DataDetails dataDetails);

    @Delete
    public void deleteData(DataDetails dataDetails);

@Query(" select * from data")
    public List<DataDetails> getAllData();

@Query(" select * from data where data_id ==:id ")
public DataDetails getData(long id);

}
