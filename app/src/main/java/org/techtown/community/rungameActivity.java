package org.techtown.community;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class rungameActivity extends AppCompatActivity {

    private BaseSurface surface;
    private LinearLayout layout;
    public static Handler han;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.rungame);

        surface = (BaseSurface)findViewById(R.id.basesurface);


        han = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                Log.d("핸들러시작","ㅇㅇ");
                int a = msg.arg1;
                Log.d("a는"+Integer.toString(a),"ㅇㅇ");
                if(a == 1){
                    AlertDialog.Builder builder = new AlertDialog.Builder(rungameActivity.this);
                    builder.setTitle("끝")
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                            .setPositiveButton("다시시작", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    surface = null;
                                    surface = (BaseSurface)findViewById(R.id.basesurface);
                                    surface.startDrawThread();
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    Log.d("프그 종료","ㅇㅇ");
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        //surface.startDrawThread();
    }

    @Override
    protected void onPause() {
        surface.stopDrawThread();
        super.onPause();
    }
}

