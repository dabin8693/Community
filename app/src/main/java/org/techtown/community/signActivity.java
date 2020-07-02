package org.techtown.community;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.concurrent.ExecutionException;

public class signActivity extends AppCompatActivity {

    private TextInputEditText emailinput, passwordinput, passwordinputcheck;
    private TextInputLayout layoutemail, layoutpassword, layoutpasswordinputcheck;
    private EditText codeinput;
    private LinearLayout container;
    private LayoutInflater inflater;
    private Button checkbutton, signbutton, sendemail;
    private ImageView exit;
    private String[] emaillist, passwordlist, nicknamelist;//인텐트 사용(메인, 로그인, 회원가입)액티비티
    private BackgroundTask task;
    private int value;
    //고유필드
    private String checkemail, sendcode, inputcode;//중복확인을 눌렀을때 입력한 이메일
    private int index, start;//db 비어있는 위치
    private String[] inputemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        inputemail = new String[1];

        emaillist = new String[100];
        passwordlist = new String[100];
        nicknamelist = new String[100];

        final Intent intent = getIntent();
        if (intent.getExtras() != null) {
            Bundle bundle = intent.getExtras();
            emaillist = bundle.getStringArray("emaillist");
            passwordlist = bundle.getStringArray("passwordlist");
            nicknamelist = bundle.getStringArray("nicknamelist");
        }

        emailinput = findViewById(R.id.signemail);
        passwordinput = findViewById(R.id.signpassword);
        passwordinputcheck = findViewById(R.id.signpasswordcheck);
        layoutemail = findViewById(R.id.layoutsignemail);
        layoutpassword = findViewById(R.id.layoutsignpassword);
        layoutpasswordinputcheck = findViewById(R.id.layoutsignpasswordcheck);

        emailinput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {//입력전

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {//입력변화
                if(emailinput.getText().toString().length() == 0){
                    layoutemail.setError("입력하세요");
                }else{
                    int check = 0;
                    if (emailinput.getText().toString().length() > 0) {
                        for (int i = 0; i < 100; i++) {
                            if (emaillist[i] != null) {
                                if (emailinput.getText().toString().equals(emaillist[i]) == true) {
                                    //중복
                                    layoutemail.setError("중복");
                                    check++;
                                }

                            }
                            if (emaillist[i] == null) {
                                if (check == 0) {//이메일db의 모든 인덱스 검사를 마친후 중복이 없으면
                                    index = i;//db 비어있는 위치, 새로운 가입자 넣는 위치
                                    checkemail = emailinput.getText().toString();

                                    int check2 = 0;
                                    if (emailinput.getText().toString().contains("@naver.com") == true) {
                                        //emailinput.setHintTextColor(Color.parseColor("#4CAF50"));


                                        //#4CAF50
                                        layoutemail.setError("사용가능");
                                        check2++;
                                    }
                                    if (emailinput.getText().toString().contains("@daum.net") == true) {
                                        //emailinput.setHintTextColor(Color.parseColor("#4CAF50"));
                                        layoutemail.setError("사용가능");
                                        check2++;
                                    }
                                    if (emailinput.getText().toString().contains("@gmail.com") == true) {
                                        //emailinput.setHintTextColor(Color.parseColor("#4CAF50"));
                                        layoutemail.setError("사용가능");
                                        check2++;
                                    }
                                    if(check2 == 0){
                                        layoutemail.setError("이메일형식이아닙니다.");
                                    }
                                    i = 100;//탈출

                                } else {
                                    //layoutemail.setError("중복");
                                    i = 100;//탈출
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {//입력끝

            }
        });
        passwordinput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(passwordinput.getText().toString().length() == 0){
                    layoutpassword.setError("입력하세요");
                }else {
                    layoutpassword.setError("사용가능");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        passwordinputcheck.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(passwordinputcheck.getText().toString().length() == 0){
                    layoutpasswordinputcheck.setError("입력하세요");
                }else {
                    if (passwordinput.getText().toString().equals(passwordinputcheck.getText().toString()) == true) {
                        layoutpasswordinputcheck.setError("사용가능");
                    } else {
                        layoutpasswordinputcheck.setError("비밀번호를 다시 입력하세요");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

       StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitDiskReads().permitDiskWrites().permitNetwork().build());



        sendemail = findViewById(R.id.sendemail);
        sendemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(signActivity.this, "인증번호 발송", Toast.LENGTH_SHORT).show();

                inputemail[0] = emailinput.getText().toString();


                task = new BackgroundTask();

                task.execute(inputemail);



                if(start == 0) {
                    container = findViewById(R.id.signcontainer);
                    inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    inflater.inflate(R.layout.signcode, container, true);

                    codeinput = container.findViewById(R.id.codeinput);
                    start = 1;
                }

            }
        });


        exit = findViewById(R.id.signexit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        signbutton = findViewById(R.id.signcheck);
        signbutton.setOnClickListener(new View.OnClickListener() {//회원가입 플래그 사용
            @Override
            public void onClick(View v) {

                    if(start == 1) {
                        try {
                            sendcode = task.get();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }else{
                        sendcode = null;
                    }

                if (emailinput.getText().toString().length() > 0) {
                    if (passwordinput.getText().toString().length() > 0) {
                        if (passwordinputcheck.getText().toString().length() > 0) {
                            if (checkemail != null) {//중복검사끝난 입력한email
                                if(codeinput.getText().toString() != null) {//인증번호입력
                                    if (sendcode != null) {
                                        if(codeinput.getText().toString().equals(sendcode) == true) {
                                            if (emailinput.getText().toString().equals(checkemail) == true) {//중복검사가 끝났을때저장한값이랑  같은값인지
                                                if (passwordinput.getText().toString().equals(passwordinputcheck.getText().toString()) == true) {//비번이랑 비번확인값이 같은지
                                                    Intent intentsign = new Intent(signActivity.this, loginActivity.class);
                                                    intentsign.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                    String[] cutnickname = emailinput.getText().toString().split("@");

                                                    emaillist[index] = emailinput.getText().toString();
                                                    passwordlist[index] = passwordinput.getText().toString();
                                                    nicknamelist[index] = cutnickname[0];//닉네임 초기값으로 이메일 넣기
                                                    intentsign.putExtra("emaillist", emaillist);
                                                    intentsign.putExtra("passwordlist", passwordlist);
                                                    intentsign.putExtra("nicknamelist", nicknamelist);

                                                    intentsign.putExtra("signActivity", 1);//회원가입창에서부터왔다
                                                    Toast.makeText(signActivity.this, "회원가입성공", Toast.LENGTH_SHORT).show();
                                                    startActivity(intentsign);

                                                }
                                            }else{
                                                Toast.makeText(signActivity.this, "이메일 중복", Toast.LENGTH_SHORT).show();
                                            }
                                        }else{
                                            Toast.makeText(signActivity.this, "인증번호가 잘못됬습니다", Toast.LENGTH_SHORT).show();
                                        }
                                    }else{
                                        Toast.makeText(signActivity.this, "인증번호를 발송하세요", Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    Toast.makeText(signActivity.this, "인증번호를 입력하세요", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(signActivity.this, "이메일 중복", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(signActivity.this, "비밀번호확인란을 입력하세요", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(signActivity.this, "비밀번호란을 입력하세요", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(signActivity.this, "이메일을 입력하세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
//메인쓰레드에서 보내는 인자 자료형
//갱신작업할때 메인쓰레드로 보내는 인자 자료형
//작업종료후 메인쓰레드로 보내는 인자 자료형
class BackgroundTask extends AsyncTask<String, Void, String> {
    String sendcode;


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d("onpre : ","이상없음");
    }


    @Override
    protected String doInBackground(String... inputemail) {
        
        SendMail mailServer = new SendMail();

        mailServer.sendSecurityCode(inputemail[0]);//오래걸림
        sendcode = mailServer.code;
        Log.d("인증번호 : ",sendcode);
        return sendcode;
    }


    @Override
    protected void onPostExecute(String sendcode) {
        super.onPostExecute(sendcode);

    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }
}