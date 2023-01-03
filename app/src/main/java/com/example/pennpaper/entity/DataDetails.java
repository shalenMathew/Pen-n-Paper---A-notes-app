package com.example.pennpaper.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "data")
public class DataDetails {

//    public static final String TABLE_NAME="data";
//    public static final String COLUMN_ID="data_id";
//    public static final String COLUMN_TITLE="data_title";
//    public static final String COLUMN_DESCRIPTION="data_description";
//    public static final String COLUMN_DATE="data_date";


    @ColumnInfo(name = "data_title")
    String title;

    @ColumnInfo(name = "data_description")
    String description;

    @ColumnInfo(name = "data_date")
   String date;

    @ColumnInfo(name = "data_id")
    @PrimaryKey(autoGenerate = true)
   int id;

    public DataDetails(String title, String description, String date, int id) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.id = id;
    }

    @Ignore
    public DataDetails() {

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

//    public static final String CREATE_TABLE =
//            " CREATE TABLE " + TABLE_NAME + "("
//                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
//                    + COLUMN_TITLE + " TEXT,"
//                    + COLUMN_DESCRIPTION + " TEXT,"
//                    + COLUMN_DATE + " TEXT"
//                    + ")";

}
