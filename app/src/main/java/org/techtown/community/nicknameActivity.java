package org.techtown.community;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class nicknameActivity extends AppCompatActivity {

    private String[] nicknamelist, itemnickname;
    private String nownickname, nicknameinputcheck;
    private EditText nicknameinput;
    private Button nicknamecheck, nicknamechange;
    private ImageView exit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nickname);

        nicknamelist = new String[100];
        itemnickname = new String[100];

        final Intent intent = getIntent();
        if(intent.getExtras() != null) {
            Bundle bundle = intent.getExtras();
            nownickname = bundle.getString("nownickname");
            nicknamelist = bundle.getStringArray("nicknamelist");
            itemnickname = bundle.getStringArray("itemnickname");

        }

        nicknameinput = findViewById(R.id.nicknameinput);//닉네임 입력창

        nicknamecheck = findViewById(R.id.nicknamecheck);
        nicknamecheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nicknameinput.getText().toString().length() > 0){
                    int k = 0;//중복 횟수
                    for(int i = 0; i<100; i++){
                        if(nicknamelist[i] == null){
                            i = 100;
                        }else {
                            if (nicknamelist[i].equals(nicknameinput.getText().toString()) == true) {
                                k++;
                            }
                        }
                    }
                    if(k == 0){//중복 없음
                        nicknameinputcheck = nicknameinput.getText().toString();
                        Toast.makeText(nicknameActivity.this, "사용가능", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(nicknameActivity.this, "중복", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(nicknameActivity.this, "닉네임을 입력하세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        nicknamechange = findViewById(R.id.nicknamechangesuccess);
        nicknamechange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nicknameinput.getText().toString().length() > 0){
                    if(nicknameinputcheck != null){
                        if(nicknameinputcheck.equals(nicknameinput.getText().toString()) == true){
                            for(int i = 0; i<100; i++){
                                if(nicknamelist[i].equals(nownickname) == true){//무조건 1개 있음// 현재 닉네임 db검색
                                    nicknamelist[i] = nicknameinput.getText().toString();//닉네임 리스트 최신화
                                    i = 100;
                                }
                            }
                            for(int i = 0; i<100; i++){
                                if(itemnickname[i] == null){
                                    i = 100;
                                }else {
                                    if (itemnickname[i].equals(nownickname) == true) {//1개이상(여러개) 있음// 현재 닉네임 db검색
                                        itemnickname[i] = nicknameinput.getText().toString();//아이템 닉네임 리스트 최신화
                                    }
                                }
                            }
                            nownickname = nicknameinput.getText().toString();
                            Toast.makeText(nicknameActivity.this, "변경완료", Toast.LENGTH_SHORT).show();
                            Intent intentchange = new Intent(nicknameActivity.this,informActivity.class);
                            intentchange.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            intentchange.putExtra("nicknamelist",nicknamelist);
                            intentchange.putExtra("itemnickname",itemnickname);
                            intentchange.putExtra("nownickname",nownickname);
                            intentchange.putExtra("nicknameActivity",1);
                            startActivity(intentchange);
                        }else{
                            Toast.makeText(nicknameActivity.this, "중복체크하세요", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(nicknameActivity.this, "중복체크하세요", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(nicknameActivity.this, "닉네임을입력하세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        exit = findViewById(R.id.nicknameexit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
