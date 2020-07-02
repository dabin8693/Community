package org.techtown.community;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class loginActivity extends AppCompatActivity {
//메인액티비티,회원가입액티비티,비밀번호찾기액티비티
    private TextInputEditText passwordinput;
    private Button loginbutton, signbutton, pwfind;
    private ImageView exit;
    private String[] emaillist, passwordlist, nicknamelist;//인텐트 사용(메인, 로그인, 회원가입)액티비티
    private int ok;
    private AutoCompleteTextView tcomplete, emailinput;
    private List<String> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emaillist = new String[100];
        passwordlist = new String[100];
        nicknamelist = new String[100];
        list = new ArrayList<String>();

        final Intent intent = getIntent();
        if(intent.getExtras() != null) {
            Bundle bundle = intent.getExtras();
            emaillist = bundle.getStringArray("emaillist");
            passwordlist = bundle.getStringArray("passwordlist");
            nicknamelist = bundle.getStringArray("nicknamelist");//회원가입 액티비티에서만 필요함
        }

        emailinput = findViewById(R.id.email);
        emailinput.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,  list ));
        for(int i = 0; i<100; i++){
            if(emaillist[i] == null){
                i = 100;
            }else{
                list.add(emaillist[i]);
            }
        }

        //emailinput = findViewById(R.id.email);
        passwordinput = findViewById(R.id.password);

        pwfind = findViewById(R.id.pwfind);
        pwfind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentpw = new Intent(loginActivity.this,passwordfindActivity.class);
                intentpw.putExtra("emaillist",emaillist);
                intentpw.putExtra("passwordlist",passwordlist);
                startActivity(intentpw);
            }
        });

        loginbutton = findViewById(R.id.login);
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ok = 0;
                if(emailinput.getText() != null){
                    if(passwordinput.getText() != null){
                        for(int i = 0; i<100; i++){
                            if(emaillist[i] == null){//회원정보 없음
                                i = 100;
                                //Toast.makeText(loginActivity.this, "6", Toast.LENGTH_SHORT).show();
                            }else{
                                if(emaillist[i].equals(emailinput.getText().toString()) == true){
                                    if(passwordlist[i].equals(passwordinput.getText().toString()) == true){
                                        ok = 1;
                                        Toast.makeText(loginActivity.this, "로그인성공", Toast.LENGTH_SHORT).show();

                                        Intent intentlogin = new Intent(loginActivity.this,MainActivity.class);
                                        intentlogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        intentlogin.putExtra("emaillist",emaillist);
                                        intentlogin.putExtra("passwordlist",passwordlist);
                                        intentlogin.putExtra("nicknamelist",nicknamelist);
                                        intentlogin.putExtra("nownickname",nicknamelist[i]);
                                        intentlogin.putExtra("nowemail",emaillist[i]);
                                        intentlogin.putExtra("loginActivity",1);//로그인액티비티에서왔다
                                        intentlogin.putExtra("login",1);//로그인상태


                                        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = pref.edit();
                                        editor.putInt("login",1);
                                        editor.putString("nowemail",emaillist[i]);
                                        editor.putString("nownickname",nicknamelist[i]);

                                        editor.commit();

                                        startActivity(intentlogin);
                                        //로그인성공
                                        i = 100;
                                    }
                                }
                            }
                        }
                    }
                }
                if(ok != 1) {
                    Toast.makeText(loginActivity.this, "로그인실패", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signbutton = findViewById(R.id.sign);
        signbutton.setOnClickListener(new View.OnClickListener() {//회원가입창 이동
            @Override
            public void onClick(View v) {
                Intent intentsign   = new Intent(loginActivity.this,signActivity.class);
                intentsign.putExtra("emaillist",emaillist);
                intentsign.putExtra("passwordlist",passwordlist);
                intentsign.putExtra("nicknamelist",nicknamelist);
                startActivity(intentsign);

            }
        });

        exit = findViewById(R.id.loginexit);
        exit.setOnClickListener(new View.OnClickListener() {//나갈때 플래그 사용//회원가입만 하고 로그인을 안했을때 예외처리
            @Override
            public void onClick(View v) {
                /*
                ok = 1;
                Intent intentexit   = new Intent(loginActivity.this,MainActivity.class);
                intentexit.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intentexit.putExtra("emaillist",emaillist);
                intentexit.putExtra("passwordlist",passwordlist);
                intentexit.putExtra("nicknamelist",nicknamelist);
                intentexit.putExtra("login",0);//로그아웃 상태
                intentexit.putExtra("loginActivity",1);//로그인액티비티에서왔다
                startActivity(intentexit);
                 */
                finish();
            }
        });
    }



    protected  void saveState(){
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        for(int i = 0; i<100; i++){
            if(emaillist[i] == null){
                i = 100;
            }else{
                editor.putString("emaillist"+i,emaillist[i]);
                editor.putString("passwordlist"+i,passwordlist[i]);
                editor.putString("nicknamelist"+i,nicknamelist[i]);
            }
        }

        editor.commit();
    }
    protected void clearState(){
        SharedPreferences pref = getSharedPreferences("pref",Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if(intent.getExtras() != null) {
            Bundle bundle = intent.getExtras();
            if(bundle.getInt("signActivity") == 1) {//회원가입창에서부터 왔다
                //회원가입창에서 데이터가 최신화된것을 받는다
                emaillist = bundle.getStringArray("emaillist");
                passwordlist = bundle.getStringArray("passwordlist");
                nicknamelist = bundle.getStringArray("nicknamelist");
                saveState();
            }else if(bundle.getInt("pass") == 1){
                passwordlist = bundle.getStringArray("passwordlist");
                saveState();
            }
            setIntent(intent);
        }
    }
}
