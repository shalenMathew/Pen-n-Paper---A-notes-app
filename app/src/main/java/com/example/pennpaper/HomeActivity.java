package com.example.pennpaper;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pennpaper.Alarm.BroadcastReceiver;
import com.example.pennpaper.Alarm.TimePicker;
import com.example.pennpaper.adapter.RecycleAdapter;
import com.example.pennpaper.db.DataBase;

import com.example.pennpaper.entity.DataDetails;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

private RecyclerView recyclerView;
private RecycleAdapter recycleAdapter;
private ArrayList<DataDetails> dataDetailsArrayList = new ArrayList<>();
private DataBase dataBase;

private  static final int REQUEST_CODE = 1;


    TextView timeTitle ;





    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // recycle view
        recyclerView = findViewById(R.id.recycler_view_contacts);


//        cancelATime =  findViewById(R.id.cancelTime);


//        pickATime.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DialogFragment timePicker = new TimePicker();
//                timePicker.show(getSupportFragmentManager(),"time picker");
//            }
//        });


//        cancelATime.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                cancelTime();
//            }
//        });

        // database
      dataBase = Room.databaseBuilder(getApplicationContext(),DataBase.class,"dataDB")
              .allowMainThreadQueries()
              .fallbackToDestructiveMigration()  // we have to increase the version and add this code whenever we make changes in database
              .build();

        // showing all data
     DisplayAllDataInBackground();

       // setting adapter
        recycleAdapter = new RecycleAdapter(this,dataDetailsArrayList,HomeActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(recycleAdapter);




        FloatingActionButton fab;
        fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAndEditData(false,null,-1);
            }
        });
    }

//public void btn1Listner(View view){
//
//
//
//if(view.getId() == view.getId(R.id.pickTime)) {
//    DialogFragment timePicker = new TimePicker();
//    timePicker.show(getSupportFragmentManager(), "time picker");
//}else  if (view.getId() == R.id.cancelTime){
//    cancelTime();
//}
//
//}

    private void DisplayAllDataInBackground() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

     dataDetailsArrayList.addAll(dataBase.getDataDao().getAllData());

//      now notifying new data which was added
        handler.post(new Runnable() {
            @Override
            public void run() {

                recycleAdapter.notifyDataSetChanged();

            }
        });


    }


    public void addAndEditData(boolean isUpdated,final DataDetails dataDetails,final int position){

        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        View view = layoutInflater.inflate(R.layout.adding_notes_card,null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);
        alertDialogBuilder.setView(view);

        TextView heading = view.findViewById(R.id.heading);
        EditText tagTitle = view.findViewById(R.id.addTag);
        EditText dateTitle = view.findViewById(R.id.addDate);
        EditText descriptionTitle = view.findViewById(R.id.addDescription);
        timeTitle = view.findViewById(R.id.addTime);
        Button pickATime = view.findViewById(R.id.pickTime);
        Button cancelTime = view.findViewById(R.id.cancelTime);

        pickATime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           DialogFragment timePicker = new TimePicker();
           timePicker.show(getSupportFragmentManager(),"time picker");

            }
        });



cancelTime.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
     cancelTime();
    }
});


if(!isUpdated){
heading.setText("Adding new notes");
}else{
    heading.setText("Editing current notes");
}

if(isUpdated && dataDetails != null){
    tagTitle.setText(dataDetails.getTitle());
    descriptionTitle.setText(dataDetails.getDescription());
    dateTitle.setText(dataDetails.getDate());
    timeTitle.setText(dataDetails.getTime());
}

// adding dailog buttons
alertDialogBuilder.setCancelable(true).setPositiveButton(isUpdated ? "UPDATE" : "SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

            }
        }).setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialog, int i) {

        if(isUpdated){
            DeleteData(dataDetails,position);
        }else{
            dialog.cancel();
        }
    }
});


   final  AlertDialog alertDialog = alertDialogBuilder.create();
   alertDialog.show();


   //set toast if tag is empty
   alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           if(TextUtils.isEmpty(tagTitle.getText().toString())){
               Toast.makeText(HomeActivity.this, "please enter tag", Toast.LENGTH_SHORT).show();
               return;
           }else{
               alertDialog.dismiss();
           }

           // function when update is clicked
           if(isUpdated && dataDetails != null){
               UpdateData(tagTitle.getText().toString(),descriptionTitle.getText().toString(),dateTitle.getText().toString(),position,timeTitle.getText().toString());
               // sending data gotten from user
           }else{
               // if save button is clicked
             CreateData(tagTitle.getText().toString(),descriptionTitle.getText().toString(),dateTitle.getText().toString(),timeTitle.getText().toString());
           }
       }
   });

    }

    private void CreateData(String tagTitle, String descriptionTitle,String dateTitle,String timeTitle) {

//        long id = db.insertData(tagTitle, descriptionTitle,dateTitle);
//        DataDetails dataDetails = db.getDataDetails(id);
//
//if(dataDetails != null){
//    dataDetailsArrayList.add(0,dataDetails);
//    recycleAdapter.notifyDataSetChanged();
//}

        long id = dataBase.getDataDao().addData(new DataDetails(tagTitle,descriptionTitle,dateTitle,0,timeTitle));
        DataDetails dataDetails = dataBase.getDataDao().getData(id);

        if(dataDetails!=null){

            dataDetailsArrayList.add(0,dataDetails);
            recycleAdapter.notifyDataSetChanged();

        }
    }


    private void UpdateData(String tagTitle, String descriptionTitle,String dateTitle,int position,String timeTitle) {

DataDetails dataDetails = dataDetailsArrayList.get(position);  // --> getting the position where data should be updated from the adapter
        dataDetails.setTitle(tagTitle);
        dataDetails.setDescription(descriptionTitle);
        dataDetails.setDate(dateTitle);
        dataDetails.setTime(timeTitle);

        dataBase.getDataDao().updateData(dataDetails);

        dataDetailsArrayList.set(position,dataDetails);
        recycleAdapter.notifyDataSetChanged();
    }


    private void DeleteData(DataDetails dataDetails, int position) {

           dataDetailsArrayList.remove(position);
           dataBase.getDataDao().deleteData(dataDetails);
           recycleAdapter.notifyDataSetChanged();

    }

    @Override
    public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {

// setting time

        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,hourOfDay);
        c.set(Calendar.MINUTE,minute);
        c.set(Calendar.SECOND,0);

        updateText(c);
       startAlarm(c);
    }

    private void startAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(HomeActivity.this, BroadcastReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(),REQUEST_CODE,intent,PendingIntent.FLAG_UPDATE_CURRENT);

   if (c.before(Calendar.getInstance())){
            c.add(Calendar.DATE,1);
        }

alarmManager.setExact(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),pi);


    }

    private void updateText(Calendar c) {
String timeText = " Alarm set for : " ;
       timeText += DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
       timeTitle.setText(timeText);
    }

    private void cancelTime() {

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(HomeActivity.this, BroadcastReceiver.class);
        PendingIntent pi =PendingIntent.getBroadcast(this,REQUEST_CODE,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pi);
        Toast.makeText(this, "Time has been cancelled", Toast.LENGTH_SHORT).show();
        timeTitle.setText("Pick a time to get notified");


    }
}