package com.example.pennpaper;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pennpaper.adapter.RecycleAdapter;
import com.example.pennpaper.db.DataBase;

import com.example.pennpaper.entity.DataDetails;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeActivity extends AppCompatActivity {

private RecyclerView recyclerView;
private RecycleAdapter recycleAdapter;
private ArrayList<DataDetails> dataDetailsArrayList = new ArrayList<>();
private DataBase dataBase;




    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // recycle view
        recyclerView = findViewById(R.id.recycler_view_contacts);

        // database
      dataBase = Room.databaseBuilder(getApplicationContext(),DataBase.class,"dataDB").allowMainThreadQueries().build();

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

    private void DisplayAllDataInBackground() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

     dataDetailsArrayList.addAll(dataBase.getDataDao().getAllData());

     // now notifying new data which was added
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

if(!isUpdated){
heading.setText("Adding new notes");
}else{
    heading.setText("Editing current notes");
}

if(isUpdated && dataDetails != null){
    tagTitle.setText(dataDetails.getTitle());
    descriptionTitle.setText(dataDetails.getDescription());
    dateTitle.setText(dataDetails.getDate());
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
               UpdateData(tagTitle.getText().toString(),descriptionTitle.getText().toString(),dateTitle.getText().toString(),position);
               // sending data gotten from user
           }else{
               // if save button is clicked
             CreateData(tagTitle.getText().toString(),descriptionTitle.getText().toString(),dateTitle.getText().toString());
           }
       }
   });

    }

    private void CreateData(String tagTitle, String descriptionTitle,String dateTitle) {

//        long id = db.insertData(tagTitle, descriptionTitle,dateTitle);
//        DataDetails dataDetails = db.getDataDetails(id);
//
//if(dataDetails != null){
//    dataDetailsArrayList.add(0,dataDetails);
//    recycleAdapter.notifyDataSetChanged();
//}

        long id = dataBase.getDataDao().addData(new DataDetails(tagTitle,descriptionTitle,dateTitle,0));
        DataDetails dataDetails = dataBase.getDataDao().getData(id);

        if(dataDetails!=null){
            dataDetailsArrayList.add(0,dataDetails);
            recycleAdapter.notifyDataSetChanged();
        }



    }


    private void UpdateData(String tagTitle, String descriptionTitle,String dateTitle,int position) {

DataDetails dataDetails = dataDetailsArrayList.get(position);  // --> getting the position where data should be updated from the adapter
        dataDetails.setTitle(tagTitle);
        dataDetails.setDescription(descriptionTitle);
        dataDetails.setDate(dateTitle);

        dataBase.getDataDao().updateData(dataDetails);

        dataDetailsArrayList.set(position,dataDetails);
        recycleAdapter.notifyDataSetChanged();


    }


    private void DeleteData(DataDetails dataDetails, int position) {

           dataDetailsArrayList.remove(position);
           dataBase.getDataDao().deleteData(dataDetails);
           recycleAdapter.notifyDataSetChanged();

    }
}