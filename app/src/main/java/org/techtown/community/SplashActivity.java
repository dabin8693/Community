package org.techtown.community;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        Log.d("스플레쉬 시작","ㅇㅇ");
        Intent intent = new Intent(SplashActivity.this,MainActivity.class);
        startActivity(intent);
        Log.d("인텐트 시작","ㅇㅇ");
        progressDialog = new ProgressDialog(SplashActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("잠시 기다려 주세요.");
        progressDialog.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("스플레쉬 종 료","ㅇㅇ");
        progressDialog.dismiss();
        finish();
    }
}
