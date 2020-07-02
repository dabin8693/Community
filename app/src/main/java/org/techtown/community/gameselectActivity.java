package org.techtown.community;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class gameselectActivity extends AppCompatActivity {
    private Button run, jump, pang;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_select);
        run = findViewById(R.id.run);
        jump = findViewById(R.id.cowjump);
        pang = findViewById(R.id.pang);
        pang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(gameselectActivity.this,gameActivity.class);
                startActivity(intent);
            }
        });
        run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(gameselectActivity.this,rungameActivity.class);
                startActivity(intent);
            }
        });
        jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(gameselectActivity.this,cowjumpgameActivity.class);
                startActivity(intent);
            }
        });
    }
}
