package org.techtown.community;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class commentinsertActivity extends AppCompatActivity {

    private EditText cinsert;
    private Button cinsertok;
    private String body;
    private String[] cbody;
    private int position, index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commentinsert);
        cbody = new String[100];

        final Intent intent = getIntent();
        if(intent.getExtras() != null) {
            Bundle bundle = intent.getExtras();
            cbody = bundle.getStringArray("cbodylist");
            body = bundle.getString("cbody");
            position = bundle.getInt("position");
            index = bundle.getInt("itemindex");
        }
        cinsert = findViewById(R.id.cinsert);
        cinsert.setText(body);
        cinsertok = findViewById(R.id.cinsertok);
        cinsertok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbody[position] = cinsert.getText().toString();
                SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                for(int i = 0; i<100; i++) {
                    editor.putString("itemcomment" + index + "," + 2 + "," + i, cbody[i]);
                }
                editor.commit();
                finish();
            }
        });
    }
}
