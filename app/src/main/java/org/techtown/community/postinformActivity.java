package org.techtown.community;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class postinformActivity extends AppCompatActivity {
    private ImageView exit, userimage;
    private TextView Temail, Tnickname, postwrite, postcomment;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private final listAdapter adapter = new listAdapter();
    private String email, nickname, head, body, nowemail, stringimage;
    private String[] emaillist;
    private String[] itemnickname, itemhead, itembody, itememail, itemtime, informitemhead, informitemnickname, informitembody, informitememail, informitemtime;
    private int position, ok, nownumber, showpeople;
    private int[] informitemnumber, itemnumber, itemshowpeople, informitemshowpeople,itemcommentnumber, informitemcommentnumber, informitemboard;
    private boolean[] itemimageYes, informitemimageYes;
    private String[] cemail, chead, cnickname, cbody, ctime;
    private int[] cnumber, cshowpeople, ccommentnumber, itemboard, cboard;
    private boolean[] cimageYes;
    private int category;
    private String nownickname;
    private ProgressDialog progressDialog;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postinform);

        informitemcommentnumber = new int[100];
        itemcommentnumber = new int[100];
        cemail = new String[100];
        chead = new String[100];
        cnickname = new String[100];
        cbody = new String[100];
        ctime = new String[100];
        cnumber = new int[100];
        cshowpeople = new int[100];
        ccommentnumber = new int[100];
        cimageYes = new boolean[100];
        cboard = new int[100];

        emaillist = new String[100];
        itemimageYes = new boolean[100];
        informitemimageYes = new boolean[100];
        informitemhead = new String[100];
        informitemnickname = new String[100];
        informitembody = new String[100];
        informitememail = new String[100];
        informitemnumber = new int[100];
        informitemtime = new String[100];
        itemnumber = new int[100];
        itemshowpeople = new int[100];
        informitemshowpeople = new int[100];
        informitemboard = new int[100];

        itemnickname = new String[100];
        itemhead = new String[100];
        itembody = new String[100];
        itememail = new String[100];
        itemtime = new String[100];
        itemboard = new int[100];

        final Intent intent = getIntent();
        if(intent.getExtras() != null){
            Bundle bundle = intent.getExtras();
            email = bundle.getString("email");
            nickname = bundle.getString("nickname");
            head = bundle.getString("head");
            body = bundle.getString("body");
            nownumber = bundle.getInt("nownumber"); // back할때 넘겨줘야됨
            showpeople = bundle.getInt("showpeople");
            nowemail = bundle.getString("nowemail");
            nownickname = bundle.getString("nownickname");

            itememail = bundle.getStringArray("itememail");
            itemnickname = bundle.getStringArray("itemnickname");
            itemhead = bundle.getStringArray("itemhead");
            itembody = bundle.getStringArray("itembody");
            itemnumber = bundle.getIntArray("itemnumber");
            itemtime = bundle.getStringArray("itemtime");
            itemshowpeople = bundle.getIntArray("itemshowpeople");
            itemimageYes = bundle.getBooleanArray("itemimageYes");
        }
        restoreState();

        userimage = findViewById(R.id.userimage2);
        userimage.setBackground(new ShapeDrawable(new OvalShape()));
        userimage.setClipToOutline(true);
        restoreprofile();
        if(stringimage != null){
            byte[] decodedByteArray = Base64.decode(stringimage, Base64.NO_WRAP);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);

            userimage.setImageBitmap(decodedBitmap);
        }

        Temail = findViewById(R.id.informemail2);
        Tnickname = findViewById(R.id.informnickname2);

        Temail.setText(email);
        Tnickname.setText(nickname);


        postwrite = findViewById(R.id.postwrite);
        postwrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = 0;
                postwrite.setTextColor(Color.parseColor("#FF000000"));
                postcomment.setTextColor(Color.parseColor("#60000000"));
                adapter.resetItem();//아이템 리셋후 다시 생성
                int k = 0;
                int t = 0;
                for(int i = 0; i<100; i++){
                    if(itemnickname[i] == null){
                        i = 100;
                    }else{
                        if(nickname.equals(itemnickname[i]) == true){
                            Log.d("헤드"+i+":",itemhead[i]);
                            informitemhead[k] = itemhead[i];
                            informitemnickname[k] = itemnickname[i];
                            informitembody[k] = itembody[i];
                            informitememail[k] = itememail[i];
                            informitemnumber[k] = itemnumber[i];
                            informitemtime[k] = itemtime[i];
                            informitemshowpeople[k] = itemshowpeople[i];
                            informitemimageYes[k] = itemimageYes[i];
                            informitemcommentnumber[k] = itemcommentnumber[i];
                            informitemboard[k] = itemboard[i];
                            //adapter.addItem(new list(informitemhead[k], informitemnickname[k],informitemtime[k],0,0));
                            k++;
                            t = k;
                        }
                    }
                }
                if(t >= 1) {
                    for (int z = t - 1; z >= 0; z--) {
                        Log.d("어뎁터:","ㅁㅎㅁㅎㅇ");
                        adapter.addItem(new list(informitemhead[z], informitemnickname[z], informitemtime[z], informitemshowpeople[z], informitemcommentnumber[z],informitemimageYes[z],informitemboard[z]));
                    }
                }
                recyclerView.setAdapter(adapter);

            }
        });

        postcomment = findViewById(R.id.postcomment);
        postcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = 1;
                postwrite.setTextColor(Color.parseColor("#60000000"));
                postcomment.setTextColor(Color.parseColor("#FF000000"));
                restoreComment();
                adapter.resetItem();//아이템 리셋후 다시 생성
                int t = 0;
                for(int i = 0; i<100; i++){
                    if(chead[i] == null){
                        t = i;
                        i = 100;
                    }
                }
                for (int z = t - 1; z >= 0; z--) {
                    Log.d("어뎁터:","ㅁㅎㅁㅎㅇ");
                    adapter.addItem(new list(chead[z], cnickname[z], ctime[z], cshowpeople[z], ccommentnumber[z],cimageYes[z], cboard[z]));
                }

                recyclerView.setAdapter(adapter);

            }
        });
        postwrite.setTextColor(Color.parseColor("#FF000000"));
        postcomment.setTextColor(Color.parseColor("#60000000"));

        exit = findViewById(R.id.informexit2);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        recyclerView = findViewById(R.id.recyclerView3);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        int k = 0;
        int t = 0;
        for(int i = 0; i<100; i++){
            if(itemnickname[i] == null){
                i = 100;
            }else{
                if(nickname.equals(itemnickname[i]) == true){
                    informitemhead[k] = itemhead[i];
                    informitemnickname[k] = itemnickname[i];
                    informitembody[k] = itembody[i];
                    informitememail[k] = itememail[i];
                    informitemnumber[k] = itemnumber[i];
                    informitemtime[k] = itemtime[i];
                    informitemshowpeople[k] = itemshowpeople[i];
                    informitemimageYes[k] = itemimageYes[i];
                    informitemcommentnumber[k] = itemcommentnumber[i];
                    informitemboard[k] = itemboard[i];
                    //adapter.addItem(new list(informitemhead[k], informitemnickname[k],informitemtime[k],0,0));
                    k++;
                    t = k;
                }
            }
        }
        if(t >= 1) {
            for (int z = t - 1; z >= 0; z--) {
                adapter.addItem(new list(informitemhead[z], informitemnickname[z], informitemtime[z], informitemshowpeople[z], informitemcommentnumber[z],informitemimageYes[z],informitemboard[z]));
            }
        }

        adapter.setOnItemClickListener(new OnlistItemClickListener() {
            @Override
            public void onItemClick(listAdapter.ViewHolder holder, View view, int position) {
                if(category == 0) {
                    int s = 0;
                    for (int i = 0; i < 100; i++) {
                        if (informitemhead[i] == null) {
                            s = i - 1;
                            i = 100;
                        }
                    }
                    //Toast.makeText(getApplicationContext(),"아이템 선택: "+item.getTitle(),Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(), "아이템 선택: " + position, Toast.LENGTH_LONG).show();
                    Intent intentitem = new Intent(postinformActivity.this, postActivity.class);
                    intentitem.putExtra("head", informitemhead[s - position]);
                    intentitem.putExtra("body", informitembody[s - position]);
                    intentitem.putExtra("nickname", informitemnickname[s - position]);
                    intentitem.putExtra("email", informitememail[s - position]);
                    intentitem.putExtra("nownumber", informitemnumber[s - position]);//nownumber는 어뎁터에서만 intent / post,postinsert 사용
                    intentitem.putExtra("showpeople", itemshowpeople[s - position]);
                    intentitem.putExtra("nowemail", nowemail);
                    intentitem.putExtra("nownickname",nownickname);

                    intentitem.putExtra("itemtime", itemtime);
                    intentitem.putExtra("itemnumber", itemnumber);
                    intentitem.putExtra("itememail", itememail);//글삭제시 최신화하기 위해
                    intentitem.putExtra("itemnickname", itemnickname);//글삭제시 최신화하기 위해
                    intentitem.putExtra("itemhead", itemhead);//글쓴이 정보창 글보기 용도
                    intentitem.putExtra("itembody", itembody);//글쓴이 정보창 글보기 용도
                    intentitem.putExtra("itemshowpeople", itemshowpeople);
                    intentitem.putExtra("itemimageYes", itemimageYes);
                    //intentitem.putExtra("where",1);
                    //intentitem.putExtra("postinformActivity",1);
                    startActivity(intentitem);
                    progressDialog = new ProgressDialog(postinformActivity.this);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setMessage("잠시 기다려 주세요.");
                    progressDialog.show();
                    //finish();
                }else if(category == 1){
                    int s = 0;
                    for (int i = 0; i < 100; i++) {
                        if (chead[i] == null) {
                            s = i - 1;
                            i = 100;
                        }
                    }
                    //Toast.makeText(getApplicationContext(),"아이템 선택: "+item.getTitle(),Toast.LENGTH_LONG).show();
                    ok = 1;
                    Toast.makeText(getApplicationContext(), "아이템 선택: " + position, Toast.LENGTH_LONG).show();
                    Intent intentitem = new Intent(postinformActivity.this, postActivity.class);
                    intentitem.putExtra("head", chead[s - position]);
                    intentitem.putExtra("body", cbody[s - position]);
                    intentitem.putExtra("nickname", cnickname[s - position]);
                    intentitem.putExtra("email", cemail[s - position]);
                    intentitem.putExtra("nownumber",cnumber[s - position]);//nownumber는 어뎁터에서만 intent / post,postinsert 사용
                    intentitem.putExtra("showpeople", cshowpeople[s - position]);
                    intentitem.putExtra("nowemail", nowemail);
                    intentitem.putExtra("nownickname",nownickname);

                    intentitem.putExtra("itemtime", itemtime);
                    intentitem.putExtra("itemnumber", itemnumber);
                    intentitem.putExtra("itememail", itememail);//글삭제시 최신화하기 위해
                    intentitem.putExtra("itemnickname", itemnickname);//글삭제시 최신화하기 위해
                    intentitem.putExtra("itemhead", itemhead);//글쓴이 정보창 글보기 용도
                    intentitem.putExtra("itembody", itembody);//글쓴이 정보창 글보기 용도
                    intentitem.putExtra("itemshowpeople", itemshowpeople);
                    intentitem.putExtra("itemimageYes", itemimageYes);
                    startActivity(intentitem);
                    progressDialog = new ProgressDialog(postinformActivity.this);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setMessage("잠시 기다려 주세요.");
                    progressDialog.show();
                }
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            progressDialog.dismiss();
        }catch (Exception e){

        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        restoreState();
        adapter.resetItem();//아이템 리셋후 다시 생성
        if(category == 0) {
            int k = 0;
            int t = 0;
            for (int i = 0; i < 100; i++) {
                if (itemnickname[i] == null) {
                    i = 100;
                } else {
                    if (nickname.equals(itemnickname[i]) == true) {
                        informitemhead[k] = itemhead[i];
                        informitemnickname[k] = itemnickname[i];
                        informitembody[k] = itembody[i];
                        informitememail[k] = itememail[i];
                        informitemnumber[k] = itemnumber[i];
                        informitemtime[k] = itemtime[i];
                        informitemshowpeople[k] = itemshowpeople[i];
                        informitemimageYes[k] = itemimageYes[i];
                        informitemcommentnumber[k] = itemcommentnumber[i];
                        informitemboard[k] = itemboard[i];
                        //adapter.addItem(new list(informitemhead[k], informitemnickname[k],informitemtime[k],0,0));
                        k++;
                        t = k;
                    }
                }
            }
            if (t >= 1) {
                for (int z = t - 1; z >= 0; z--) {
                    adapter.addItem(new list(informitemhead[z], informitemnickname[z], informitemtime[z], itemshowpeople[z], informitemcommentnumber[z], informitemimageYes[z], informitemboard[z]));
                }
            }
        }else if(category == 1){
            restoreComment();
            adapter.resetItem();//아이템 리셋후 다시 생성
            int t = 0;
            for(int i = 0; i<100; i++){
                if(chead[i] == null){
                    t = i;
                    i = 100;
                }
            }
            for (int z = t - 1; z >= 0; z--) {
                Log.d("어뎁터:","ㅁㅎㅁㅎㅇ");
                adapter.addItem(new list(chead[z], cnickname[z], ctime[z], cshowpeople[z], ccommentnumber[z],cimageYes[z],cboard[z]));
            }

            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        recyclerView.setAdapter(adapter);
    }


    protected void restoreState(){
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        if(pref != null){
            for(int i = 0; i<100; i++){
                if(itemhead[i] == null){
                    i = 100;
                }else{
                    itemshowpeople[i] = pref.getInt("itemshowpeople"+i,0);
                    itemhead[i] = pref.getString("itemhead"+i,null);
                    itembody[i] = pref.getString("itembody"+i,null);
                    itemimageYes[i] = pref.getBoolean("itemimageYes"+i,false);
                    itemnickname[i] = pref.getString("itemnickname"+i,null);
                    itememail[i] = pref.getString("itememail"+i, null);
                    itemnumber[i] = pref.getInt("itemnumber"+i,0);
                    itemtime[i] = pref.getString("itemtime"+i,null);
                    itemimageYes[i] = pref.getBoolean("itemimageYes"+i,false);
                    itemcommentnumber[i] = pref.getInt("itemcommentnumber"+i,0);
                    itemboard[i] = pref.getInt("itemboard"+i,0);
                    //itemcommentnumber[i]
                }
            }

        }
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

    protected void restoreComment(){
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        if(pref != null){
            cemail = new String[100];
            chead = new String[100];
            cnickname = new String[100];
            cbody = new String[100];
            cnumber = new int[100];
            ctime = new String[100];
            cshowpeople = new int[100];
            cimageYes = new boolean[100];
            ccommentnumber = new int[100];
            cboard = new int[100];
            int j = 0;
            for(int i = 0; i<100; i++){
                if(itemhead[i] != null){
                    int k = pref.getInt("itemcomment"+i+","+6,0);
                    int dupcheck = 0;

                    for(int t = 0; t<k; t++){
                        String temp = null;
                        temp = pref.getString("itemcomment"+i+","+5+","+t,null);
                        if(dupcheck == 0) {
                            if (temp != null) {
                                if (temp.equals(email) == true) {
                                    cemail[j] = itememail[i];
                                    chead[j] = itemhead[i];
                                    cnickname[j] = itemnickname[i];
                                    cbody[j] = itembody[i];
                                    cnumber[j] = itemnumber[i];
                                    ctime[j] = itemtime[i];
                                    cshowpeople[j] = itemshowpeople[i];
                                    cimageYes[j] = itemimageYes[i];
                                    ccommentnumber[j] = itemcommentnumber[i];
                                    cboard[j] = itemboard[i];
                                    Log.d("c이메일:", cemail[j]);
                                    j++;
                                    dupcheck++;
                                }
                            }
                        }
                    }

                }
            }

        }
    }

}
