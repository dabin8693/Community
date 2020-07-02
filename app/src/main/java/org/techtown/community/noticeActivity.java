package org.techtown.community;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

public class noticeActivity extends AppCompatActivity {

    private Switch s1;
    private LinearLayout l1;
    private ImageView i1;
    private String str;
    private boolean bool;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        s1 = findViewById(R.id.switch1);
        final SharedPreferences sharedPreferences = getSharedPreferences(str,0);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        s1.setChecked(sharedPreferences.getBoolean("bool",false));
        s1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e("boolout", String.valueOf(isChecked));
                bool = isChecked;
                if (isChecked == true){
                    editor.putBoolean("bool",true);

                    Toast.makeText(noticeActivity.this, "Switch-ON", Toast.LENGTH_SHORT).show();
                    //스위치 on시 action
                } else {
                    editor.putBoolean("bool",false);
                    editor.commit();
                    Toast.makeText(noticeActivity.this, "Switch-OFF", Toast.LENGTH_SHORT).show();
                    //스위치 off시 action
                }

                editor.commit();


            }
        });

        l1 = findViewById(R.id.exit2);
        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentexit2 = new Intent(noticeActivity.this,MainActivity.class);
                intentexit2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intentexit2);
            }
        });
        i1 = findViewById(R.id.exit3);
        i1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentexit3 = new Intent(noticeActivity.this,MainActivity.class);
                intentexit3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intentexit3);
            }
        });
    }

    @Override
    protected void onPause() {


        super.onPause();
    }

    protected void onStop() {

        super.onStop();
    }

    @Override
    protected void onDestroy() {


        super.onDestroy();
    }



}
