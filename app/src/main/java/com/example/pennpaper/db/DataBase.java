package com.example.pennpaper.db;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.pennpaper.entity.DataDetails;

@Database( entities = DataDetails.class,version = 2)
public abstract class DataBase extends RoomDatabase {

    // linking DAO with database
public  abstract  DataDAO getDataDao();



}