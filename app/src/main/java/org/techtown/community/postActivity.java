package org.techtown.community;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class postActivity extends AppCompatActivity {

    private String[] itemhead, itembody, itemnickname, itememail, itemtime, itemgoodcheck;//글쓴이 정보창 글보기 용도
    private String head, body, nickname, email, nowemail, stringimage, nowstringimage, nowtime, nownickname;
    private ImageView back, postinform, home, good;
    private TextView Thead, Tbody, Tnickname, insert, delete, person, goodnumber, pboard, comment;
    private EditText inputcomment;
    private Button commentup;
    private LinearLayout container;
    private LayoutInflater inflater;
    private int nownumber, showpeople;
    private int[] itemnumber, itemshowpeople, itemgoodnumber, itemboard;
    private RecyclerView recyclerView, recyclerView2;//리사이클러뷰
    private LinearLayoutManager layoutManager, layoutManager2;//리사이클러뷰
    private final postlistAdapter adapter3 = new postlistAdapter();//리사이클러뷰
    private final commentAdapter adapterC = new commentAdapter();//리사이클러뷰
    private InputStream in;
    private String[] itemuri, spilturi, spiltgood, tonickname, toemail;
    private boolean[] itemimageYes;
    private boolean showYes;
    private ArrayList<commentlist> list;
    private String[] cimage, cnickname, cbody, ctime, cemail, spiltcemail, emaillist;
    private int[] ctype, itemcommentnumber, ccomment;
    private String commentemail;
    private int now, thstop;
    private Thread th;
    private ProgressDialog progressDialog;
    private Handler han;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        Log.d("온크리에이트","ㅇㅇ");
/*
        han = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                progressDialog = new ProgressDialog(postActivity.this);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setMessage("잠시 기다려 주세요.");
                progressDialog.show();
                Log.d("핸들러시작","ㅇㅇ");
                int a = msg.arg1;
                Log.d("a는"+Integer.toString(a),"ㅇㅇ");
                if(a == 1){
                    progressDialog.dismiss();
                    Log.d("프그 종료","ㅇㅇ");
                }
            }
        };

        th = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Message msg = han.obtainMessage();
                    Log.d("쓰레드 시작","ㅇㅇ");
                    msg.arg1 = thstop;
                    han.sendMessage(msg);
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(thstop == 1){
                        try {
                            Log.d("쓰레드 무한슬립","ㅇㅇ");
                            Thread.sleep(100000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        th.start();

 */




        emaillist = new String[100];
        itemcommentnumber = new int[100];
        ccomment = new int[100];
        toemail = new String[100];
        tonickname = new String[100];
        cimage = new String[100];
        cnickname = new String[100];
        cbody = new String[100];
        ctime = new String[100];
        cemail = new String[100];
        ctype = new int[100];
        ////
        itemboard = new int[100];
        itemgoodnumber = new int[100];
        itemgoodcheck = new String[100];
        itemimageYes = new boolean[100];
        itemuri = new String[100];//itemuri 임시값
        //spilturi = new String[100];

        itemhead = new String[100];
        itembody = new String[100];
        itemnickname = new String[100];
        itememail = new String[100];
        itemnumber = new int[100];
        itemtime = new String[100];
        itemshowpeople = new int[100];

        final Intent intent = getIntent();
        if(intent.getExtras() != null) {
            Bundle bundle = intent.getExtras();

                head = bundle.getString("head");
                body = bundle.getString("body");
                nickname = bundle.getString("nickname");
                nownumber = bundle.getInt("nownumber");
                showpeople = bundle.getInt("showpeople");
                //글쓴이 정보창 갈때 쓰임
                nowemail = bundle.getString("nowemail");
                nownickname = bundle.getString("nownickname");
                email = bundle.getString("email");
                itemhead = bundle.getStringArray("itemhead");
                itembody = bundle.getStringArray("itembody");
                itemnickname = bundle.getStringArray("itemnickname");
                itememail = bundle.getStringArray("itememail");
                itemnumber = bundle.getIntArray("itemnumber");
                itemtime = bundle.getStringArray("itemtime");
                itemshowpeople = bundle.getIntArray("itemshowpeople");
                itemimageYes = bundle.getBooleanArray("itemimageYes");

        }
        //Log.d("냐우이메일ㅁㄷㄹㄷㅁ:",nowemail);

        restoreprofile();
        restoreState2();
        ///////////////////////////////////////////////////////////////
        ///쓰레드 필요
        showrecycle();
        //////쓰레드 필요
        restoreComment();
////////////////////////////////////////////////////////////////
        recyclerView2 = findViewById(R.id.recyclerViewcomment);
        layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView2.setLayoutManager(layoutManager2);

        inputcomment = findViewById(R.id.inputcomment);
        commentup = findViewById(R.id.commentup);
        commentup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm");
                nowtime = sdf.format(date);


                adapterC.addItem(new commentlist(nowstringimage,nownickname,inputcomment.getText().toString(),nowtime,0,null));
                for(int i = 0; i<100; i++){
                    if(cnickname[i] == null){
                        cimage[i] = nowstringimage;
                        cnickname[i] = nownickname;
                        cbody[i] = inputcomment.getText().toString();
                        ctime[i] = nowtime;
                        cemail[i] = nowemail;
                        ctype[i] = 0;
                        //Log.d("냐우이메일은?ㅇㅇ???",nowemail);
                        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        for(int k = 0; k<100; k++){
                            if(nownumber == itemnumber[k]){
                                editor.putInt("itemcommentnumber"+k,i+1);
                                editor.putInt("itemcomment"+k+","+6,i+1);
                                itemcommentnumber[k] = i+1;
                                ccomment[k] = i+1;
                                for(int t = 0; t<100; t++){
                                    if(cnickname[t] == null){

                                    }else{
                                        editor.putString("itemcomment"+k+","+0+","+t,cimage[t]);
                                        editor.putString("itemcomment"+k+","+1+","+t,cnickname[t]);
                                        editor.putString("itemcomment"+k+","+2+","+t,cbody[t]);
                                        editor.putString("itemcomment"+k+","+3+","+t,ctime[t]);
                                        editor.putInt("itemcomment"+k+","+4+","+t,ctype[t]);
                                        editor.putString("itemcomment"+k+","+5+","+t,cemail[t]);
                                    }
                                }
                            }
                        }

                        editor.commit();
                        i = 100;
                    }
                }
                recyclerView2.setAdapter(adapterC);
                inputcomment.setText(null);
            }
        });
        for(int i = 0; i<100; i++) {
            if(cnickname[i] != null) {
                if(tonickname[i] == null) {
                    adapterC.addItem(new commentlist(cimage[i], cnickname[i], cbody[i], ctime[i], ctype[i],null));
                }else{
                    adapterC.addItem(new commentlist(cimage[i], cnickname[i], cbody[i], ctime[i], ctype[i],tonickname[i]));
                }
            }
        }
        recyclerView2.setAdapter(adapterC);

        adapterC.setOnItemClickListener(new OnlistItemClickListener3() {
            @Override
            public void onItemClick(commentAdapter.CenterViewHolder holder, View view, int position) {
                //Toast.makeText(getApplicationContext(),"아이템 1선택: "+position,Toast.LENGTH_LONG).show();
                Intent intent1 = new Intent(postActivity.this,recommentwriteActivity.class);
                SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                //Log.d("댓글의답글 포지션:",Integer.toString(position));
                editor.putString("tempimage",cimage[position]);
                editor.commit();
                intent1.putExtra("cnickname",cnickname[position]);
                intent1.putExtra("cbody",cbody[position]);
                intent1.putExtra("ctime",ctime[position]);
                intent1.putExtra("ctype",ctype[position]);
                intent1.putExtra("cemail",cemail[position]);
                intent1.putExtra("position",position);
                intent1.putExtra("nownumber",nownumber);
                intent1.putExtra("itememail",itememail);
                intent1.putExtra("itemnumber",itemnumber);
                intent1.putExtra("nowemail",nowemail);
                intent1.putExtra("nownickname",nownickname);
                intent1.putExtra("toemail", toemail);
                startActivity(intent1);

            }
        });
        adapterC.setOnItemClickListener1(new OnlistItemClickListener4() {
            @Override
            public void onItemClick(commentAdapter.LeftViewHolder holder, View view, int position) {
                //Toast.makeText(getApplicationContext(),"아이템 2선택: "+position,Toast.LENGTH_LONG).show();
                Intent intent1 = new Intent(postActivity.this,recommentwriteActivity.class);
                SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                //Log.d("답글의답글 포지션:",Integer.toString(position));
                editor.putString("tempimage",cimage[position]);
                editor.commit();
                intent1.putExtra("cnickname",cnickname[position]);
                intent1.putExtra("cbody",cbody[position]);
                intent1.putExtra("ctime",ctime[position]);
                intent1.putExtra("ctype",ctype[position]);
                intent1.putExtra("cemail",cemail[position]);
                intent1.putExtra("position",position);
                intent1.putExtra("nownumber",nownumber);
                intent1.putExtra("itememail",itememail);
                intent1.putExtra("itemnumber",itemnumber);
                intent1.putExtra("nowemail",nowemail);
                intent1.putExtra("nownickname",nownickname);
                intent1.putExtra("toemail", toemail);
                startActivity(intent1);
            }
        });
        adapterC.setOnItemClickListener0(new OnlistItemClickListener33() {//댓글 이미지
            @Override
            public void onItemClick(commentAdapter.CenterViewHolder holder, View view, int position) {
                //Toast.makeText(getApplicationContext(),"아이템 11선택: "+position,Toast.LENGTH_LONG).show();
                Intent intentinform = new Intent(postActivity.this,postinformActivity.class);
                intentinform.putExtra("head",head);
                intentinform.putExtra("body",body);
                intentinform.putExtra("email",cemail[position]);
                intentinform.putExtra("nickname",cnickname[position]);
                intentinform.putExtra("nownumber",nownumber);
                intentinform.putExtra("showpeople",showpeople);
                intentinform.putExtra("nowemail",nowemail);
                intentinform.putExtra("nownickname",nownickname);

                intentinform.putExtra("itemtime",itemtime);
                intentinform.putExtra("itemnumber",itemnumber);
                intentinform.putExtra("itemhead",itemhead);
                intentinform.putExtra("itembody",itembody);
                intentinform.putExtra("itemnickname",itemnickname);
                intentinform.putExtra("itememail",itememail);
                intentinform.putExtra("itemshowpeople",itemshowpeople);
                intentinform.putExtra("itemimageYes",itemimageYes);
                startActivity(intentinform);
            }
        });
        adapterC.setOnItemClickListener11(new OnlistItemClickListener44() {//답글 이미지
            @Override
            public void onItemClick(commentAdapter.LeftViewHolder holder, View view, int position) {
                //Toast.makeText(getApplicationContext(),"아이템 22선택: "+position,Toast.LENGTH_LONG).show();
                Intent intentinform = new Intent(postActivity.this,postinformActivity.class);
                intentinform.putExtra("head",head);
                intentinform.putExtra("body",body);
                intentinform.putExtra("email",cemail[position]);
                intentinform.putExtra("nickname",cnickname[position]);
                intentinform.putExtra("nownumber",nownumber);
                intentinform.putExtra("showpeople",showpeople);
                intentinform.putExtra("nowemail",nowemail);
                intentinform.putExtra("nownickname",nownickname);

                intentinform.putExtra("itemtime",itemtime);
                intentinform.putExtra("itemnumber",itemnumber);
                intentinform.putExtra("itemhead",itemhead);
                intentinform.putExtra("itembody",itembody);
                intentinform.putExtra("itemnickname",itemnickname);
                intentinform.putExtra("itememail",itememail);
                intentinform.putExtra("itemshowpeople",itemshowpeople);
                intentinform.putExtra("itemimageYes",itemimageYes);
                startActivity(intentinform);
            }
        });
        adapterC.setOnItemClickListener00(new OnlistItemClickListener333() {
            @Override
            public void onItemClick(commentAdapter.CenterViewHolder holder, View view, final int position) {
                if(nowemail.equals(cemail[position]) == true){
                    final String str[] = {"수정","삭제"};
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(postActivity.this);
                    builder.setTitle("선택")
                            .setNegativeButton("취소",null)
                            .setItems(str, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(which == 0){
                                        int k = 0;
                                        for(int i = 0; i<100; i++){
                                            if(nownumber == itemnumber[i]){
                                                k = i;
                                                i = 100;
                                            }
                                        }
                                        Intent intent1 = new Intent(postActivity.this,commentinsertActivity.class);
                                        intent1.putExtra("cbody",cbody[position]);
                                        intent1.putExtra("cbodylist",cbody);
                                        intent1.putExtra("position",position);
                                        intent1.putExtra("itemindex",k);
                                        startActivity(intent1);
                                        //수정액티비티
                                    }else if(which == 1){
                                        //삭제
                                        for(int i = 0; i<100; i++){
                                            if(nownumber == itemnumber[i] == true){
                                                itemcommentnumber[i] -= 1;
                                                ccomment[i] -= 1;
                                                i = 100;
                                            }
                                        }
                                        for(int i = position; i<98; i++){
                                            cimage[i] = cimage[i+1];
                                            cnickname[i] = cnickname[i+1];
                                            cemail[i] = cemail[i+1];
                                            cbody[i] = cbody[i+1];
                                            ctime[i] = ctime[i+1];
                                            ctype[i] = ctype[i+1];
                                            tonickname[i] = tonickname[i+1];
                                            toemail[i] = toemail[i+1];
                                        }
                                        //itemcommentnumber[i] = pref.getInt("itemcommentnumber"+i,0);
                                        //ccomment[i] = pref.getInt("itemcomment"+i+","+6,0);
                                        adapterC.resetItem();
                                        for(int i = 0; i<100; i++) {
                                            if(cnickname[i] != null) {
                                                if(tonickname[i] == null) {
                                                    adapterC.addItem(new commentlist(cimage[i], cnickname[i], cbody[i], ctime[i], ctype[i],null));
                                                }else{
                                                    adapterC.addItem(new commentlist(cimage[i], cnickname[i], cbody[i], ctime[i], ctype[i],tonickname[i]));
                                                }
                                            }
                                        }
                                        recyclerView2.setAdapter(adapterC);
                                        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = pref.edit();
                                        for(int k = 0; k<100; k++){
                                            if(nownumber == itemnumber[k]){
                                                editor.putInt("itemcommentnumber"+k,itemcommentnumber[k]);
                                                editor.putInt("itemcomment"+k+","+6,ccomment[k]);
                                                for(int t = 0; t<100; t++){
                                                    if(cnickname[t] == null){
                                                        editor.putString("itemcomment"+k+","+0+","+t,null);
                                                        editor.putString("itemcomment"+k+","+1+","+t,null);
                                                        editor.putString("itemcomment"+k+","+2+","+t,null);
                                                        editor.putString("itemcomment"+k+","+3+","+t,null);
                                                        editor.putInt("itemcomment"+k+","+4+","+t,0);
                                                        editor.putString("itemcomment"+k+","+5+","+t,null);
                                                        editor.putString("itemcomment"+k+","+7+","+t,null);
                                                    }else{
                                                        editor.putString("itemcomment"+k+","+0+","+t,cimage[t]);
                                                        editor.putString("itemcomment"+k+","+1+","+t,cnickname[t]);
                                                        editor.putString("itemcomment"+k+","+2+","+t,cbody[t]);
                                                        editor.putString("itemcomment"+k+","+3+","+t,ctime[t]);
                                                        editor.putInt("itemcomment"+k+","+4+","+t,ctype[t]);
                                                        editor.putString("itemcomment"+k+","+5+","+t,cemail[t]);
                                                        editor.putString("itemcomment"+k+","+7+","+t,toemail[t]);
                                                    }
                                                }
                                            }
                                        }
                                        editor.commit();
                                    }
                                    //Toast.makeText(getApplicationContext(),"선택 "+str[which]+"위치 "+Integer.toString(which),Toast.LENGTH_LONG).show();
                                }
                            });
                    //builder.show();
                    android.app.AlertDialog dialog = builder.create();
                    dialog.show();
                }else{
                    Toast.makeText(postActivity.this,"작성자가 아닙니다",Toast.LENGTH_SHORT).show();
                }
            }
        });
        adapterC.setOnItemClickListener111(new OnlistItemClickListener444() {
            @Override
            public void onItemClick(commentAdapter.LeftViewHolder holder, View view, final int position) {
                if(nowemail.equals(cemail[position]) == true){
                    final String str[] = {"수정","삭제"};
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(postActivity.this);
                    builder.setTitle("선택")
                            .setNegativeButton("취소",null)
                            .setItems(str, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(which == 0){
                                        int k = 0;
                                        for(int i = 0; i<100; i++){
                                            if(nownumber == itemnumber[i]){
                                                k = i;
                                                i = 100;
                                            }
                                        }
                                        Intent intent1 = new Intent(postActivity.this,commentinsertActivity.class);
                                        intent1.putExtra("cbody",cbody[position]);
                                        intent1.putExtra("cbodylist",cbody);
                                        intent1.putExtra("position",position);
                                        intent1.putExtra("itemindex",k);
                                        startActivity(intent1);
                                        //수정액티비티
                                    }else if(which == 1){
                                        //삭제
                                        for(int i = 0; i<100; i++){
                                            if(nownumber == itemnumber[i] == true){
                                                itemcommentnumber[i] -= 1;
                                                ccomment[i] -= 1;
                                                i = 100;
                                            }
                                        }
                                        for(int i = position; i<98; i++){
                                            cimage[i] = cimage[i+1];
                                            cemail[i] = cemail[i+1];
                                            cnickname[i] = cnickname[i+1];
                                            cbody[i] = cbody[i+1];
                                            ctime[i] = ctime[i+1];
                                            ctype[i] = ctype[i+1];
                                            tonickname[i] = tonickname[i+1];
                                            toemail[i] = toemail[i+1];
                                        }
                                        //itemcommentnumber[i] = pref.getInt("itemcommentnumber"+i,0);
                                        //ccomment[i] = pref.getInt("itemcomment"+i+","+6,0);
                                        adapterC.resetItem();
                                        for(int i = 0; i<100; i++) {
                                            if(cnickname[i] != null) {
                                                if(tonickname[i] == null) {
                                                    adapterC.addItem(new commentlist(cimage[i], cnickname[i], cbody[i], ctime[i], ctype[i],null));
                                                }else{
                                                    adapterC.addItem(new commentlist(cimage[i], cnickname[i], cbody[i], ctime[i], ctype[i],tonickname[i]));
                                                }
                                            }
                                        }
                                        recyclerView2.setAdapter(adapterC);
                                        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = pref.edit();
                                        for(int k = 0; k<100; k++){
                                            if(nownumber == itemnumber[k]){
                                                editor.putInt("itemcommentnumber"+k,itemcommentnumber[k]);
                                                editor.putInt("itemcomment"+k+","+6,ccomment[k]);
                                                for(int t = 0; t<100; t++){
                                                    if(cnickname[t] == null){
                                                        editor.putString("itemcomment"+k+","+0+","+t,null);
                                                        editor.putString("itemcomment"+k+","+1+","+t,null);
                                                        editor.putString("itemcomment"+k+","+2+","+t,null);
                                                        editor.putString("itemcomment"+k+","+3+","+t,null);
                                                        editor.putInt("itemcomment"+k+","+4+","+t,0);
                                                        editor.putString("itemcomment"+k+","+5+","+t,null);
                                                        editor.putString("itemcomment"+k+","+7+","+t,null);
                                                    }else{
                                                        editor.putString("itemcomment"+k+","+0+","+t,cimage[t]);
                                                        editor.putString("itemcomment"+k+","+1+","+t,cnickname[t]);
                                                        editor.putString("itemcomment"+k+","+2+","+t,cbody[t]);
                                                        editor.putString("itemcomment"+k+","+3+","+t,ctime[t]);
                                                        editor.putInt("itemcomment"+k+","+4+","+t,ctype[t]);
                                                        editor.putString("itemcomment"+k+","+5+","+t,cemail[t]);
                                                        editor.putString("itemcomment"+k+","+7+","+t,toemail[t]);
                                                    }
                                                }
                                            }
                                        }
                                        editor.commit();
                                    }
                                    //Toast.makeText(getApplicationContext(),"선택 "+str[which]+"위치 "+Integer.toString(which),Toast.LENGTH_LONG).show();
                                }
                            });
                    //builder.show();
                    android.app.AlertDialog dialog = builder.create();
                    dialog.show();
                }else{
                    Toast.makeText(postActivity.this,"작성자가 아닙니다",Toast.LENGTH_SHORT).show();
                }
            }
        });

        pboard = findViewById(R.id.pboard);
        //Log.d("head:",head);
        Thead = findViewById(R.id.head2);
        Thead.setText(head);
        Tbody = findViewById(R.id.body2);
        Tbody.setText(body);
        Tnickname = findViewById(R.id.postnickname);
        Tnickname.setText(nickname);
        person = findViewById(R.id.person);

        goodnumber = findViewById(R.id.goodnumber);

        showpeople = showpeople + 1;
        for(int i = 0; i<100; i++){
            if(itemnumber[i] == 0){
                i = 100;
            }else{
                if(itemnumber[i] == nownumber){
                    itemshowpeople[i] = showpeople;
                    goodnumber.setText(Integer.toString(itemgoodnumber[i]));
                    if(itemboard[i] == 10){
                        pboard.setText("전체글");
                    }else if(itemboard[i] == 0){
                        pboard.setText("자유글");
                    }else if(itemboard[i] == 1){
                        pboard.setText("공지글");

                    }else if(itemboard[i] == 2){
                        pboard.setText("사건/사고");
                    }else if(itemboard[i] == 3){
                        pboard.setText("이벤트");
                    }else if(itemboard[i] == 4){
                        pboard.setText("중고글");

                    }
                }
            }
        }


        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();//조회수 저장
        editor.putInt("showpeople",showpeople);
        for(int i = 0; i<100; i++){//i = 100 하면 안됨 리사이클러뷰 버그남
            editor.putInt("itemshowpeople"+i,itemshowpeople[i]);
        }
        editor.commit();
        person.setText(Integer.toString(showpeople));


        goodnumber = findViewById(R.id.goodnumber);


        good = findViewById(R.id.good);
        good.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nowemail != null) {
                    int c = 0;
                    int check = 0;
                    for (int i = 0; i < 100; i++) {
                        if (itemnumber[i] == nownumber) {
                            c = i;
                        }
                    }
                    spiltgood = null;
                    if (itemgoodcheck[c] != null) {
                        spiltgood = itemgoodcheck[c].split(",");
                        //Log.d("스플릿0:",spiltgood[0]);
                        //Log.d("스플릿1:",spiltgood[1]);
                    }
                    if (spiltgood != null) {
                        //Log.d("스플릿 굿 0 :",spiltgood[0]);
                        //Log.d("스플릿 크기 : ", Integer.toString(spiltgood.length));
                        for (int i = 0; i < spiltgood.length; i++) {
                            if (spiltgood[i].equals(nowemail) == true) {
                                check++;
                            }
                        }
                        if (check == 0) {
                            goodnumber.setText(Integer.toString(itemgoodnumber[c] + 1));
                            itemgoodnumber[c] += 1;
                            if (itemgoodcheck[c] == null) {
                                itemgoodcheck[c] = nowemail;
                            } else {
                                itemgoodcheck[c] += "," + nowemail;
                            }
                            SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();//조회수 저장
                            for (int k = 0; k < 100; k++) {
                                editor.putString("itemgoodcheck" + k, itemgoodcheck[k]);
                                editor.putInt("itemgoodnumber" + k, itemgoodnumber[k]);
                            }
                            Toast.makeText(postActivity.this, "추천했습니다.", Toast.LENGTH_SHORT).show();
                            editor.commit();
                        } else {
                            Toast.makeText(postActivity.this, "이미 추천한 글입니다.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        goodnumber.setText(Integer.toString(itemgoodnumber[c] + 1));
                        itemgoodnumber[c] += 1;
                        itemgoodcheck[c] = nowemail;
                        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();//조회수 저장
                        for (int k = 0; k < 100; k++) {
                            editor.putString("itemgoodcheck" + k, itemgoodcheck[k]);
                            editor.putInt("itemgoodnumber" + k, itemgoodnumber[k]);
                        }
                        Toast.makeText(postActivity.this, "추천했습니다.", Toast.LENGTH_SHORT).show();
                        editor.commit();
                    }
                }else{
                    Toast.makeText(postActivity.this, "로그인하세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if(email.equals(nowemail) == true) {
            container = findViewById(R.id.postcontainer);
            inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.delete_item, container, true);

            delete = container.findViewById(R.id.delete);
            insert = container.findViewById(R.id.insert);



        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                final AlertDialog.Builder builder = new AlertDialog.Builder(postActivity.this);
                builder.setTitle("안내");
                builder.setMessage("삭제하시겠습니까?");
                builder.setIcon(android.R.drawable.ic_dialog_alert);

                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
                        if(pref != null){
                            for(int i = 0; i<100; i++){
                                if(itemhead[i] == null){
                                    i = 100;
                                }else{
                                    itemuri[i] = pref.getString("itemuri"+i,null);
                                    //Log.d("소환ㅇㅇㅇ"+i+"::",itemimage[i]);
                                }
                            }
                        }//이미지가 없는 글에서는 호출을 안하기 때문에 이미지가 없는글에서 삭제할때 에러남 그걸 방지하기위한 호출
                        //////////////////////////////////////////////////

                        int c = 0;
                        for (int i = 0; i < 100; i++) {
                            if (itemnumber[i] == nownumber) {
                                now = i;
                                c = i;
                            }
                        }
                        for (int i = 0; i < 100; i++) {
                            if (itemnumber[c + i + 1] == 0) {
                                itemhead[c + i] = null;
                                itembody[c + i] = null;
                                itemnickname[c + i] = null;
                                itememail[c + i] = null;
                                itemtime[c + i] = null;
                                itemnumber[c + i] = 0;
                                itemshowpeople[c + i] = 0;
                                itemuri[c + i] = null;
                                itemimageYes[c + i] = false;
                                itemgoodcheck[c + i] = null;
                                itemgoodnumber[c + i] = 0;
                                itemboard[c + i] = 0;

                                //cimage[c + i] = null;
                                //cnickname[c + i] = null;
                                //cbody[c + i] = null;
                                //ctime[c + i] = null;
                                //ctype[c + i] = 0;
                                //cemail[c + i] = null;
                                ccomment[c + i] = 0;
                                itemcommentnumber[c + i] = 0;
                                //toemail[c + i] = null;

                                i = 100;
                            } else {
                                itemhead[c + i] = itemhead[c + i + 1];
                                itembody[c + i] = itembody[c + i + 1];
                                itemnickname[c + i] = itemnickname[c + i + 1];
                                itememail[c + i] = itememail[c + i + 1];
                                itemtime[c + i] = itemtime[c + i + 1];
                                itemnumber[c + i] = itemnumber[c + i + 1];
                                itemshowpeople[c + i] = itemshowpeople[c + i + 1];
                                itemuri[c + i] = itemuri[c + i + 1];
                                itemimageYes[c + i] = itemimageYes[c + i + 1];
                                itemgoodcheck[c + i] = itemgoodcheck[c + i + 1];
                                itemgoodnumber[c + i] = itemgoodnumber[c + i + 1];
                                itemboard[c + i] = itemboard[c + i + 1];

                                //cimage[c + i] = cimage[c + i +1];
                                //cnickname[c + i] = cnickname[c + i + 1];
                                //cbody[c + i] = cbody[c + i + 1];
                                //ctime[c + i] = ctime[c + i + 1];
                                //ctype[c + i] = ctype[c + i + 1];
                                //cemail[c + i] = cemail[c + i + 1];
                                ccomment[c + i] = ccomment[c + i + 1];
                                itemcommentnumber[c + i] = itemcommentnumber[c + i + 1];
                                //toemail[c + i] = toemail[c + i + 1];
                            }
                        }
                        //Log.d("ccc는ccc : ",Integer.toString(c));


                        deletesave();
                        finish();

                    }
                });
                builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setNeutralButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();


            }

        });
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentinsert = new Intent(postActivity.this,postinsertActivity.class);
                intentinsert.putExtra("itemhead",itemhead);
                intentinsert.putExtra("itembody",itembody);
                intentinsert.putExtra("head",head);
                intentinsert.putExtra("body",body);
                intentinsert.putExtra("nickname",nickname);
                intentinsert.putExtra("itemnumber",itemnumber);
                intentinsert.putExtra("nownumber",nownumber);
                intentinsert.putExtra("showYes",showYes);
                intentinsert.putExtra("itemimageYes",itemimageYes);
                intentinsert.putExtra("showpeople",showpeople);
                startActivity(intentinsert);
            }
        });
        }
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        home = findViewById(R.id.posthome);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intenthome = new Intent(postActivity.this, MainActivity.class);
                intenthome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intenthome.putExtra("itemhead", itemhead);
                intenthome.putExtra("itembody", itembody);
                intenthome.putExtra("itemshowpeople",itemshowpeople);
                intenthome.putExtra("postActivity",1);
                //인텐트 안씀// save 써야됨
                startActivity(intenthome);


            }

        });

        postinform = findViewById(R.id.postinform);
        postinform.setBackground(new ShapeDrawable(new OvalShape()));
        postinform.setClipToOutline(true);
        //restoreprofile();
        if(stringimage != null){
            byte[] decodedByteArray = Base64.decode(stringimage, Base64.NO_WRAP);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);

            postinform.setImageBitmap(decodedBitmap);
        }
        postinform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//메인에서 받은 인텐트값 다 옮겨야 됨//postinform -> post 갈꺼 대비

                Intent intentinform = new Intent(postActivity.this,postinformActivity.class);
                intentinform.putExtra("head",head);
                intentinform.putExtra("body",body);
                intentinform.putExtra("email",email);
                intentinform.putExtra("nickname",nickname);
                intentinform.putExtra("nownumber",nownumber);
                intentinform.putExtra("showpeople",showpeople);
                intentinform.putExtra("nowemail",nowemail);
                intentinform.putExtra("nownickname",nownickname);

                intentinform.putExtra("itemtime",itemtime);
                intentinform.putExtra("itemnumber",itemnumber);
                intentinform.putExtra("itemhead",itemhead);
                intentinform.putExtra("itembody",itembody);
                intentinform.putExtra("itemnickname",itemnickname);
                intentinform.putExtra("itememail",itememail);
                intentinform.putExtra("itemshowpeople",itemshowpeople);
                intentinform.putExtra("itemimageYes",itemimageYes);
                startActivity(intentinform);
                //finish();//엑티비티중복때문에 flag오류뜨는거같음
            }
        });


    }




    protected void deletesave(){
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        int max = 0;
        //now 현재위치 삭제버튼 리스너에 있음
        for(int i = 0; i<100; i++){
            if(itememail[i] == null){//삭제후itememail//기존보다한칸작음
                max = i;//삭제후 마지막칸 한칸뒤
                i = 100;
            }
        }
        for(int i = now+1; i<max+1; i++){
            for(int k = 0; k<100; k++) {
                String temp = pref.getString("itemcomment" + i + "," + 0 + "," + k, null);
                editor.putString("itemcomment"+(i-1)+","+0+","+k,temp);//이미지
                String temp1 = pref.getString("itemcomment" + i + "," + 1 + "," + k, null);
                editor.putString("itemcomment"+(i-1)+","+1+","+k,temp1);//닉네임
                String temp2 = pref.getString("itemcomment" + i + "," + 2 + "," + k, null);
                editor.putString("itemcomment"+(i-1)+","+2+","+k,temp2);//글내용
                String temp3 = pref.getString("itemcomment" + i + "," + 3 + "," + k, null);
                editor.putString("itemcomment"+(i-1)+","+3+","+k,temp3);//시간
                int temp4 = pref.getInt("itemcomment" + i + "," + 4 + "," + k, 0);
                editor.putInt("itemcomment"+(i-1)+","+4+","+k,temp4);//타입
                String temp5 = pref.getString("itemcomment" + i + "," + 5 + "," + k, null);
                editor.putString("itemcomment"+(i-1)+","+5+","+k,temp5);//이메일
                //int temp6 = pref.getInt("itemcomment" + i + "," + 6 + "," + k, 0);
                //editor.putInt("itemcomment"+(i-1)+","+6+","+k,temp6);//댓글개수
                String temp7 = pref.getString("itemcomment" + i + "," + 7 + "," + k, null);
                editor.putString("itemcomment"+(i-1)+","+7+","+k,temp7);//상대방이메일
                if(temp1 == null){
                    k = 100;
                }
            }
        }
        for(int i = 0; i<100; i++) {//마지막 줄 초기화
            String temp10 = pref.getString("itemcomment" + max + "," + 1 + "," + i, null);
            editor.putString("itemcomment" + max + "," + 0 + "," + i, null);//이미지
            editor.putString("itemcomment"+max+","+1+","+i,null);//닉네임
            editor.putString("itemcomment"+max+","+2+","+i,null);//글내용
            editor.putString("itemcomment"+max+","+3+","+i,null);//시간
            editor.putInt("itemcomment"+max+","+4+","+i,0);//타입
            editor.putString("itemcomment"+max+","+5+","+i,null);//이메일
            //editor.putInt("itemcomment"+max+","+6+","+i,0);//댓글개수
            editor.putString("itemcomment"+max+","+7+","+i,null);//상대방이메일

            if(temp10 == null){
                i = 100;
            }
        }
        for(int i = 0; i<100; i++) {
            if(itemnumber[i] == 0){
                editor.putInt("itemnumber"+i,0);// 뒤에 있는 키값 초기화
                editor.putString("itemnickname" + i, null);
                editor.putString("itememail"+i,null);
                editor.putString("itemhead"+i,null);
                editor.putString("itembody"+i,null);
                editor.putInt("itemnumber"+i,0);
                editor.putString("itemtime"+i,null);
                editor.putInt("itemshowpeople"+i,0);
                editor.putString("itemuri"+i,null);
                editor.putBoolean("itemimageYes"+i,false);
                editor.putString("itemuri"+i,null);
                editor.putString("itemgoodcheck"+i,null);
                editor.putInt("itemgoodnumber"+i,0);
                editor.putInt("itemboard"+i,0);

                editor.putInt("itemcommentnumber"+i,0);//메인용 댓글개수
                editor.putInt("itemcomment"+i+","+6,0);//댓글개수
                /*
                for(int t = 0; t<100; t++){
                    if(cnickname[t] == null){//뒤i 뒤t 삭제
                        editor.putString("itemcomment"+i+","+0+","+t,null);
                        editor.putString("itemcomment"+i+","+1+","+t,null);
                        editor.putString("itemcomment"+i+","+2+","+t,null);
                        editor.putString("itemcomment"+i+","+3+","+t,null);
                        editor.putInt("itemcomment"+i+","+4+","+t,0);
                        editor.putString("itemcomment"+i+","+5+","+t,null);
                        editor.putString("itemcomment"+i+","+7+","+t,null);
                        t = 100;
                    }else{//뒤i 전t 삭제
                        editor.putString("itemcomment"+i+","+0+","+t,null);
                        editor.putString("itemcomment"+i+","+1+","+t,null);
                        editor.putString("itemcomment"+i+","+2+","+t,null);
                        editor.putString("itemcomment"+i+","+3+","+t,null);
                        editor.putInt("itemcomment"+i+","+4+","+t,0);
                        editor.putString("itemcomment"+i+","+5+","+t,null);
                        editor.putString("itemcomment"+i+","+7+","+t,null);
                    }
                }

                 */
                i = 100;
            }else {
                editor.putString("itemnickname" + i, itemnickname[i]);
                editor.putString("itememail"+i,itememail[i]);
                editor.putString("itemhead"+i,itemhead[i]);
                editor.putString("itembody"+i,itembody[i]);
                editor.putInt("itemnumber"+i,itemnumber[i]);
                editor.putString("itemtime"+i,itemtime[i]);
                editor.putInt("itemshowpeople"+i, itemshowpeople[i]);
                editor.putString("itemuri"+i,itemuri[i]);
                editor.putBoolean("itemimageYes"+i,itemimageYes[i]);
                editor.putString("itemuri"+i,itemuri[i]);
                editor.putString("itemgoodcheck"+i,itemgoodcheck[i]);
                editor.putInt("itemgoodnumber"+i,itemgoodnumber[i]);
                editor.putInt("itemboard"+i,itemboard[i]);

                editor.putInt("itemcommentnumber"+i,itemcommentnumber[i]);
                editor.putInt("itemcomment"+i+","+6,ccomment[i]);

            }
        }

        editor.commit();
    }
    protected void restoreState(){
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        if(pref != null){
            //head = pref.getString("head",null);
            //body = pref.getString("body",null);
            //showpeople = pref.getInt("showpeople",0);
            for(int i = 0; i<100; i++){
                if(itemhead[i] == null){
                    i = 100;
                }else{
                    itemshowpeople[i] = pref.getInt("itemshowpeople"+i,0);
                    itemhead[i] = pref.getString("itemhead"+i,null);
                    itembody[i] = pref.getString("itembody"+i,null);
                }
            }

        }
    }

    protected void restoreState2(){
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        if(pref != null){
            //head = pref.getString("head",null);
            //body = pref.getString("body",null);
            //showpeople = pref.getInt("showpeople",0);
            for(int i = 0; i<100; i++){
                if(itemhead[i] == null){
                    i = 100;
                }else{
                    itemgoodcheck[i] = pref.getString("itemgoodcheck"+i,null);
                    itemgoodnumber[i] = pref.getInt("itemgoodnumber"+i,0);
                    itemboard[i] = pref.getInt("itemboard"+i,0);
                }
            }

        }
    }
    protected void restoreComment(){
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        if(pref != null){
            itemcommentnumber = new int[100];
            ccomment = new int[100];
            cbody = new String[100];
            ctime = new String[100];
            cemail = new String[100];
            ctype = new int[100];
            toemail = new String[100];
            cimage = new String[100];
            cnickname = new String[100];
            tonickname = new String[100];
            for(int a = 0; a<100; a++) {
                if(emaillist[a] != null) {
                    if(nowemail != null) {
                        if (nowemail.equals(emaillist[a]) == true) {//여기가 문제??
                            nowstringimage = pref.getString("profile" + a, null);
                            a = 100;
                        }
                    }
                }
            }
            for(int i = 0; i<100; i++){
                itemcommentnumber[i] = pref.getInt("itemcommentnumber"+i,0);
                ccomment[i] = pref.getInt("itemcomment"+i+","+6,0);
                if(nownumber == itemnumber[i]){
                    int k = pref.getInt("itemcomment"+i+","+6,0);

                    for(int t = 0; t<k; t++){
                        cbody[t] = pref.getString("itemcomment"+i+","+2+","+t,null);
                        ctime[t] = pref.getString("itemcomment"+i+","+3+","+t,null);
                        cemail[t] = pref.getString("itemcomment"+i+","+5+","+t,null);
                        ctype[t] = pref.getInt("itemcomment"+i+","+4+","+t,0);
                        toemail[t] = pref.getString("itemcomment"+i+","+7+","+t,null);

                        for(int a = 0; a<100; a++) {
                            if(emaillist[a] != null) {
                                if(cemail[t] != null) {
                                    if (cemail[t].equals(emaillist[a]) == true) {//여기가 문제??
                                        cimage[t] = pref.getString("profile" + a, null);
                                        cnickname[t] = pref.getString("nicknamelist" + a, null);

                                    }
                                }
                            }
                        }
                        for(int a = 0; a<100; a++) {
                            if(emaillist[a] != null) {
                                if(toemail[t] != null) {
                                    if (toemail[t].equals(emaillist[a]) == true) {//여기가 문제??
                                        tonickname[t] = pref.getString("nicknamelist" + a, null);

                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
    }
    protected void imagerestore(){
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        if(pref != null){
            for(int i = 0; i<100; i++){
                if(itemhead[i] == null){
                    i = 100;
                }else{
                    itemuri[i] = null;//사진 수정대비 기존값 초기화
                    itemuri[i] = pref.getString("itemuri"+i,null);
                    itemimageYes[i] = pref.getBoolean("itemimageYes"+i,false);//사진 수정떄문에
                }
            }
        }
    }

    protected  void saveState(){
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("head",head);
        editor.putString("body",body);
        for(int i = 0; i<100; i++) {
            if(itemhead[i] == null){
                i = 100;
            }else {
                editor.putString("itemhead"+i, itemhead[i]);
                editor.putString("itembody"+i, itembody[i]);
            }
        }
        editor.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("온스타트","ㅇㅇ");
        thstop = 1;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("온리줌","ㅇㅇ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        restoreState2();
        restoreComment();
        adapterC.resetItem();
        for(int i = 0; i<100; i++) {
            if(cnickname[i] != null) {
                if(tonickname[i] == null) {
                    adapterC.addItem(new commentlist(cimage[i], cnickname[i], cbody[i], ctime[i], ctype[i],null));
                }else{
                    adapterC.addItem(new commentlist(cimage[i], cnickname[i], cbody[i], ctime[i], ctype[i],tonickname[i]));
                }
            }
        }
        recyclerView2.setAdapter(adapterC);
        for (int i = 0; i < 100; i++) {
            if (itemnumber[i] == nownumber) {
                goodnumber.setText(Integer.toString(itemgoodnumber[i]));
                if(itemboard[i] == 10){
                    pboard.setText("전체글");
                }else if(itemboard[i] == 0){
                    pboard.setText("자유글");
                }else if(itemboard[i] == 1){
                    pboard.setText("공지글");

                }else if(itemboard[i] == 2){
                    pboard.setText("사건/사고");
                }else if(itemboard[i] == 3){
                    pboard.setText("이벤트");
                }else if(itemboard[i] == 4){
                    pboard.setText("중고글");

                }
            }
        }

        int ok = 0;
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        for(int i = 0; i<100; i++){
            itemnumber[i] = pref.getInt("itemnumber"+i,0);
            itemimageYes[i] = pref.getBoolean("itemimageYes"+i,false);
        }
        for (int i = 0; i < 100; i++) {
            if (itemnumber[i] == nownumber) {//아이템 고유번호 확인// 쉐어드 값이랑 다르면 다음 액티비티에서 글을 삭제한것임
                restoreState();
                showrecycle();

                person.setText(Integer.toString(itemshowpeople[i]));
                Thead.setText(itemhead[i]);
                Tbody.setText(itembody[i]);
                i = 100;
                ok = 1;
            }
        }
        if(ok != 1){
            finish();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(intent.getExtras() != null) {
            Bundle bundle = intent.getExtras();

            if(bundle.getInt("insertActivity") == 1){//수정//안씀
                head = bundle.getString("head");
                body = bundle.getString("body");
                itemhead = bundle.getStringArray("itemhead");
                itembody = bundle.getStringArray("itembody");
                Thead.setText(head);
                Tbody.setText(body);
            }
            /*
            if(bundle.getInt("postinformActivity") == 1){
                itemhead = bundle.getStringArray("itemhead");
                itembody = bundle.getStringArray("itembody");
            }

             */
            setIntent(intent);
        }
    }

    public void showrecycle(){


            //Log.d("0번임ㅁㅇㅇㅇㅇ","ㅇㄴ");
            recyclerView = findViewById(R.id.recyclerViewpost);
            layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
            adapter3.resetItem();//기존 리사이클러뷰 초기화
            int re = 0;
            //recyclerView.setNestedScrollingEnabled(false);
            imagerestore();//쉐어드에서 이미지 불러오기//여기서 기존 uri 초기화함
            spilturi = null;// 사진을 수정할경우 기존값 초기화
            //Log.d("1번임ㅁㅇㅇㅇㅇ","ㅇㄴ");
            for (int i = 0; i < 100; i++) {
                if (itemnumber[i] == nownumber) {
                    if (itemuri[i] != null) {

                        spilturi = itemuri[i].split(",");

                    }
                }
            }
            if (spilturi != null) {
                for (int i = 0; i < spilturi.length; i++) {
                    if (spilturi[i] == null) {

                    } else {

                        adapter3.addItem(new postlist(spilturi[i]));
                        re = 1;
                    }
                }
            }
            if(re != 1){
                adapter3.resetItem();
                //Log.d("리셋임ㅇㅇ","ㅇㅇㅇ");
            }
            recyclerView.setAdapter(adapter3);

    }
    protected void restoreprofile(){
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        if(pref != null){
            for(int i = 0; i<100; i++){
                emaillist[i] = pref.getString("emaillist"+i,null);
            }
            for(int i = 0; i<100; i++){
                if(emaillist[i] == null){
                    i = 100;
                }else{
                    if(email.equals(emaillist[i]) == true){
                        stringimage = pref.getString("profile"+i,null);
                    }
                }
            }

        }
    }
}

class Backrecycle extends AsyncTask<Object, Void, Void> {


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }


    @Override
    protected Void doInBackground(Object... objects) {

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }
}