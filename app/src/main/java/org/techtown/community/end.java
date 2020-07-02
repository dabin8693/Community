package org.techtown.community;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class end extends AppCompatActivity {
    TextView scoret;
    Button restart_b;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.end);
        scoret = findViewById(R.id.score);
        restart_b = findViewById(R.id.restart);
        restart_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Score.score = 0;
                Score.mediaPlayer = null;
                Score.mediaPlayer_jump = null;
                Score.die = 0;
                Score.mRThread = null;
                Intent intent = new Intent(end.this,cowjumpgameActivity.class);
                startActivity(intent);
                //finish();
            }
        });
        double result_score = Score.score/1000d;
        scoret.setText(Double.toString(result_score)+"초");
        AlertDialog.Builder builder = new AlertDialog.Builder(end.this);
        builder.setTitle(result_score+"초")
                .setNegativeButton("취소",null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("스탑스탑ㅇㅁㄹ","ㅇㅁㄿㅁ");
        //System.exit(0);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //System.exit(0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //System.exit(0);
    }
}

