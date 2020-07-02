package org.techtown.community;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

public class cowjumpgameActivity extends AppCompatActivity {
    MySurfaceView mySurfaceView;
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //setContentView(R.layout.activity_main);

        //mySurfaceView = findViewById(R.id.surface);
        //mySurfaceView = new MySurfaceView(this);
        new Thread(){
            @Override
            public void run() {
                super.run();
                boolean bool = true;
                while (bool){
                    if(Score.die == 10){
                        bool = false;
                        Intent intent = new Intent(cowjumpgameActivity.this,end.class);
                        startActivity(intent);
                    }
                }
            }
        }.start();
        setContentView(new MySurfaceView(this));
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("온스탑ㅇㅇ","ㅁㅎㅈㄷ");
        if(Score.die != 10) {
            Score.mediaPlayer_jump.stop();
            Score.mediaPlayer_jump.release();
            Score.mediaPlayer.stop();
            Score.mediaPlayer.release();
        }
        try {
            //mRThread.join();
            Score.mRThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Score.mRThread.stop();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("온디스트로이ㅇㅇ","ㅁㅎㅈㄷ");
    }
}

