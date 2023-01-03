//package com.example.pennpaper.db;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//import android.provider.ContactsContract;
//
//import androidx.annotation.Nullable;
//
//import com.example.pennpaper.entity.DataDetails;
//
//import java.util.ArrayList;
//
//public class DataBaseHelper extends SQLiteOpenHelper {
//
//    public static final int DATABASE_VERSION =1;
//    public static final String DATABASE_NAME ="data_db";
//
//   public DataBaseHelper(Context context){
//       super(context,DATABASE_NAME,null,DATABASE_VERSION);
//   }
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//       db.execSQL(DataDetails.CREATE_TABLE);
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//       // updating table
//     db.execSQL(" DROP TABLE IF EXISTS " + DataDetails.TABLE_NAME);
//     onCreate(db);
//    }
//
//public  long insertData (String title,String description,String date){
//
//       // inserting data
//       SQLiteDatabase db = this.getWritableDatabase();
//    ContentValues values = new ContentValues();
//
////    ContentValues works like hash map storing value and its key
//
//    values.put(DataDetails.COLUMN_TITLE,title);
//    values.put(DataDetails.COLUMN_DESCRIPTION,description);
//    values.put(DataDetails.COLUMN_DATE,date);
//
//    long id = db.insert(DataDetails.TABLE_NAME,null,values);
//    db.close();
//
//    return  id;
//}
//
//
//public DataDetails getDataDetails(long id){
//// getting data according to id
//
//    SQLiteDatabase db = this.getReadableDatabase();
//    Cursor cursor = db.query(DataDetails.TABLE_NAME,
//            new String[]{
//                    DataDetails.COLUMN_ID,
//                    DataDetails.COLUMN_TITLE,
//                    DataDetails.COLUMN_DESCRIPTION,
//                    DataDetails.COLUMN_DATE},
//
//            DataDetails.COLUMN_ID + "=?",
//
//            new String[]{
//                    String.valueOf(id)
//            },
//            null,
//            null,
//            null,
//            null
//            );
//
//    if(cursor!=null){
//        cursor.moveToFirst();
//    }
//
//DataDetails dataDetails = new DataDetails(
//
//        cursor.getString(cursor.getColumnIndexOrThrow(DataDetails.COLUMN_TITLE)),
//        cursor.getString(cursor.getColumnIndexOrThrow(DataDetails.COLUMN_DESCRIPTION)),
//        cursor.getString(cursor.getColumnIndexOrThrow(DataDetails.COLUMN_DATE)),
//        cursor.getInt(cursor.getColumnIndexOrThrow(DataDetails.COLUMN_ID)));
//
//cursor.close();
//return  dataDetails;
//
//}
//
//
//public ArrayList<DataDetails> getAllDetails(){
//
//       ArrayList<DataDetails> dataDetailsList = new ArrayList<>();
//
//    String selectQuery = "SELECT * FROM " +DataDetails.TABLE_NAME + " ORDER BY "+
//         DataDetails.COLUMN_ID + " DESC";
//       SQLiteDatabase db = this.getWritableDatabase();
//
//       Cursor cursor = db.rawQuery(selectQuery,null);
//
//if(cursor.moveToFirst()){
//    do{
//
//        // getting data from db and setting it in object so that we can display in opn recycle view
//
//        DataDetails dataDetails = new DataDetails();
//
//        dataDetails.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DataDetails.COLUMN_ID)));
//        dataDetails.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(DataDetails.COLUMN_TITLE)));
//        dataDetails.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DataDetails.COLUMN_DESCRIPTION)));
//        dataDetails.setDate(cursor.getString(cursor.getColumnIndexOrThrow(DataDetails.COLUMN_DATE)));
//
//          dataDetailsList.add(dataDetails);
//
//    }while (cursor.moveToNext());
//}
//
//db.close();;
//
//return  dataDetailsList;
//}
//
//public int updateValue(DataDetails dataDetails){
//
//       SQLiteDatabase db = this.getWritableDatabase();
//       ContentValues values = new ContentValues();
//values.put(DataDetails.COLUMN_TITLE,dataDetails.getTitle());
//values.put(DataDetails.COLUMN_DESCRIPTION,dataDetails.getDescription());
//values.put(DataDetails.COLUMN_DATE,dataDetails.getDate());
//
//return db.update(DataDetails.TABLE_NAME,values,DataDetails.COLUMN_ID + "=?",new String[]{
//        String.valueOf(dataDetails.getId())});
//
//}
//
//
//
//public void deleteValue(DataDetails dataDetails){
//       SQLiteDatabase db = this.getWritableDatabase();
//
//       db.delete(DataDetails.TABLE_NAME,DataDetails.COLUMN_ID + "=?",new String[]{String.valueOf(dataDetails.getId())});
//      db.close();
//}
//
//
//
//}
