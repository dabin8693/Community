package org.techtown.community;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

import com.google.android.material.textfield.TextInputLayout;

import java.util.concurrent.ExecutionException;

public class passwordfindActivity extends AppCompatActivity {

    EditText emailinput, passwordinput, passwordinputcheck, codeinput;
    Button emailsend, success;
    ImageView exit;
    String[] emaillist, passwordlist, inputemail;
    String exemail, sendcode;
    private BackgroundTask3 task;
    private int start;
    private LinearLayout container;
    private LayoutInflater inflater;
    private TextInputLayout layoutemail, layoutpassword, layoutpasswordinputcheck;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passwordfind);
        emaillist = new String[100];
        passwordlist = new String[100];
        inputemail = new String[1];

        final Intent intent = getIntent();
        if(intent.getExtras() != null) {
            Bundle bundle = intent.getExtras();
            emaillist = bundle.getStringArray("emaillist");
            passwordlist = bundle.getStringArray("passwordlist");
        }

        exit = findViewById(R.id.pwexit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        emailinput = findViewById(R.id.signemail2);

        emailsend = findViewById(R.id.emsend);
        emailsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int k = 0;
                exemail = emailinput.getText().toString();
                if(start == 0) {
                    container = findViewById(R.id.signcontainer2);
                    inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    inflater.inflate(R.layout.signcode, container, true);

                    codeinput = container.findViewById(R.id.codeinput);
                    start = 1;
                }
                Toast.makeText(passwordfindActivity.this, "인증번호 발송", Toast.LENGTH_SHORT).show();

                inputemail[0] = emailinput.getText().toString();


                task = new BackgroundTask3();

                task.execute(inputemail);
                /*
                for(int i = 0; i<100; i++){
                    if(exemail.equals(emaillist[i]) == true){
                        //이메일 보냄
                        Log.d("이메일1ㅇ "+i+":",emaillist[i]);
                        Toast.makeText(passwordfindActivity.this, "인증번호 발송", Toast.LENGTH_SHORT).show();
                        inputemail[0] = exemail;
                        task = new BackgroundTask2(passwordlist[i]);
                        task.execute(inputemail);
                        k = 1;
                    }

                }

                 */
                if(k != 1){
                    Toast.makeText(passwordfindActivity.this, "다시 입력하세요", Toast.LENGTH_SHORT).show();
                    k = 1;
                }
            }
        });

        passwordinput = findViewById(R.id.signpassword2);
        passwordinputcheck = findViewById(R.id.signpasswordcheck2);
        layoutemail = findViewById(R.id.layoutsignemail2);
        layoutpassword = findViewById(R.id.layoutsignpassword2);
        layoutpasswordinputcheck = findViewById(R.id.layoutsignpasswordcheck2);

        emailinput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(emailinput.getText().toString().length() == 0) {

                    layoutemail.setError("입력하세요");

                }else {
                    int check2 = 0;
                    if (emailinput.getText().toString().contains("@naver.com") == true) {
                        layoutemail.setError("이메일형식 O");
                        check2++;
                    }
                    if (emailinput.getText().toString().contains("@daum.net") == true) {
                        layoutemail.setError("이메일형식 O");
                        check2++;
                    }
                    if (emailinput.getText().toString().contains("@gmail.com") == true) {
                        layoutemail.setError("이메일형식 O");
                        check2++;
                    }
                    if (check2 == 0) {
                        layoutemail.setError("이메일형식이아닙니다.");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

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
        success = findViewById(R.id.succ);
        success.setOnClickListener(new View.OnClickListener() {
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

                if(passwordinput.getText().toString().length() > 0){
                    if(passwordinputcheck.getText().toString().length() > 0){
                        if(sendcode != null){
                            if(codeinput.getText() != null){
                                if(codeinput.getText().toString().equals(sendcode) == true){
                                    if(passwordinput.getText().toString().equals(passwordinputcheck.getText().toString()) == true){
                                        Intent intentsign = new Intent(passwordfindActivity.this, loginActivity.class);
                                        intentsign.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                                        for(int i = 0; i<100; i++){
                                            if(emaillist[i] == null){
                                                i = 100;
                                            }else {
                                                if (emaillist[i].equals(emailinput.getText().toString()) == true) {
                                                    passwordlist[i] = passwordinput.getText().toString();
                                                }
                                            }
                                        }

                                        intentsign.putExtra("passwordlist", passwordlist);

                                        intentsign.putExtra("pass", 1);//비번창에서부터왔다
                                        Toast.makeText(passwordfindActivity.this, "비밀번호변경완료", Toast.LENGTH_SHORT).show();
                                        startActivity(intentsign);
                                    }else{
                                        Toast.makeText(passwordfindActivity.this, "입력란을 확인하세요", Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    Toast.makeText(passwordfindActivity.this, "입력란을 확인하세요", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(passwordfindActivity.this, "입력란을 확인하세요", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(passwordfindActivity.this, "입력란을 확인하세요", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(passwordfindActivity.this, "입력란을 확인하세요", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(passwordfindActivity.this, "입력란을 확인하세요", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}

//메인쓰레드에서 보내는 인자 자료형
//갱신작업할때 메인쓰레드로 보내는 인자 자료형
//작업종료후 메인쓰레드로 보내는 인자 자료형
class BackgroundTask2 extends AsyncTask<String, Void, Void> {
    String password;

    public BackgroundTask2(String password) {
        this.password = password;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d("onpre : ","이상없음");
    }


    @Override
    protected Void doInBackground(String... inputemail) {

        SendMail mailServer = new SendMail();

        mailServer.sendPasswordCode(password,inputemail[0]);//오래걸림
        Log.d("인풋값?? : ",inputemail[0]);
        Log.d("비밀번호 : ",password);

        return null;
    }



    @Override
    protected void onCancelled() {
        super.onCancelled();
    }
}

class BackgroundTask3 extends AsyncTask<String, Void, String> {
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