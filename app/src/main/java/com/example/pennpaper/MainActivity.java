package com.example.pennpaper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    // hide the tool bar in theme

    SurfaceView surfaceView;
    MediaPlayer mediaPlayer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

   surfaceView=findViewById(R.id.surfaceView);
   mediaPlayer = MediaPlayer.create(this,R.raw.pennpaper);
   SurfaceHolder surfaceHolder = surfaceView.getHolder();

   surfaceHolder.addCallback(new SurfaceHolder.Callback() {
       @Override
       public void surfaceCreated(@NonNull SurfaceHolder holder) {
           mediaPlayer.setDisplay(surfaceHolder);
       }

       @Override
       public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

       }

       @Override
       public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

       }
   });

   mediaPlayer.start();




   new Handler().postDelayed(new Runnable() {
       @Override
       public void run() {

           Intent i = new Intent(MainActivity.this,HomeActivity.class);
           startActivity(i);
           finish();
           mediaPlayer.stop();


       }
   },3000);


    }
}