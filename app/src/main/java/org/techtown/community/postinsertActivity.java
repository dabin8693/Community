package org.techtown.community;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class postinsertActivity extends AppCompatActivity {

    private InputStream in;
    private final int SELECT_IMAGE = 1;
    private String[] itemhead, itembody, itemuri, spilturi,str_image;
    private String head, body, nickname;
    private boolean showYes;
    private boolean[] itemimageYes;
    private Button insert, upimage;
    private int nownumber, showpeople;
    private int[] itemnumber, itemboard;
    private ImageView back;
    private TextView tnickname, tperson, ptboard;
    private EditText ehead, ebody;
    private RecyclerView recyclerView;//리사이클러뷰
    private LinearLayoutManager layoutManager;//리사이클러뷰
    private final imagelistAdapter adapter3 = new imagelistAdapter();//리사이클러뷰
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postinsert);

        itemboard = new int[100];
        itemhead = new String[100];
        itembody = new String[100];
        itemnumber = new int[100];
        itemuri = new String[100];
        str_image = new String[100];
        itemimageYes = new boolean[100];

        Intent intent = getIntent();
        if(intent.getExtras() != null){
            Bundle bundle = intent.getExtras();
            itemhead = bundle.getStringArray("itemhead");
            itembody = bundle.getStringArray("itembody");
            head = bundle.getString("head");
            body = bundle.getString("body");
            nickname = bundle.getString("nickname");
            nownumber = bundle.getInt("nownumber");
            itemnumber = bundle.getIntArray("itemnumber");
            showYes = bundle.getBoolean("showYes");
            itemimageYes = bundle.getBooleanArray("itemimageYes");
            showpeople = bundle.getInt("showpeople");
        }
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        for(int i = 0; i<100; i++){
            if(itemhead[i] == null){
                i = 100;
            }else{
                itemboard[i] = pref.getInt("itemboard"+i,0);
            }
        }
        ptboard = findViewById(R.id.ptboard);

        for(int i = 0; i<100; i++){
            if(itemnumber[i] == nownumber){
                if(itemboard[i] == 10){
                    ptboard.setText("전체글");
                }else if(itemboard[i] == 0){
                    ptboard.setText("자유글");
                }else if(itemboard[i] == 1){
                    ptboard.setText("공지글");

                }else if(itemboard[i] == 2){
                    ptboard.setText("사건/사고");
                }else if(itemboard[i] == 3){
                    ptboard.setText("이벤트");
                }else if(itemboard[i] == 4){
                    ptboard.setText("중고글");

                }
            }
        }
        tperson = findViewById(R.id.person2);
        tperson.setText(Integer.toString(showpeople));

        tnickname = findViewById(R.id.insertnickname);
        tnickname.setText(nickname);

        ehead = findViewById(R.id.inserthead);
        ehead.setText(head);

        ebody = findViewById(R.id.insertbody);
        ebody.setText(body);

        back = findViewById(R.id.insertback);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        showrecycle();


        adapter3.setOnItemClickListener(new OnlistItemClickListener2() {
            @Override
            public void onItemClick(imagelistAdapter.ViewHolder holder, View view, int position) {
                adapter3.resetItem();
                for(int i = 0; i<100; i++){//아이템 삭제
                    if(str_image[position + i + 1] == null){
                        str_image[position + i] = null;
                        Log.d("포지션 : ",Integer.toString(position));
                        i = 100;
                    }else {
                        str_image[position + i] = str_image[position + i + 1];
                    }
                }
                if (str_image != null) {//리사이클러뷰에 아이템 넣기
                    for (int i = 0; i < 100; i++) {
                        if (str_image[i] == null) {
                            i = 100;
                        } else {
                            //Log.d("2번임ㅁㅇㅇㅇㅇ","ㅇㄴ");
                            adapter3.addItem(new imagelist(str_image[i]));
                        }
                    }
                }
                recyclerView.setAdapter(adapter3);
            }
        });

        upimage = findViewById(R.id.upimage);
        upimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSelectImage();
            }
        });

        insert = findViewById(R.id.insertok);
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ehead.getText().toString().length()>0){
                    if(ebody.getText().toString().length()>0){
                        int k = 0;
                        for(int i = 0; i<100; i++){
                            if(itemnumber[i] == nownumber){
                                k = i;
                            }
                        }
                        head = ehead.getText().toString();
                        Log.d("ehead:",ehead.getText().toString());
                        body = ebody.getText().toString();
                        Log.d("ebody:",ebody.getText().toString());
                        itemhead[k] = ehead.getText().toString();
                        itembody[k] = ebody.getText().toString();

                        itemuri[k] = null;//초기화하고 다시 담기
                        itemimageYes[k] = false;//초기화하고 다시 담기
                            for (int i = 0; i < 100; i++) {// k = 현재 인덱스
                                if (str_image[i] == null) {
                                    //i = 100;
                                } else {
                                    itemimageYes[k] = true;

                                    if (itemuri[k] == null) {
                                        itemuri[k] = str_image[i];//첫항 널방지
                                        //Log.d("Stringimage"+i+"::",Stringimage[i]);
                                    } else {
                                        itemuri[k] += "," + str_image[i];
                                        //itemuri 인덱스 = 메인item포지션
                                        //Stringimage 인덱스 = 글쓰기item 포지션
                                        //Log.d("Stringimage"+i+"::",Stringimage[i]);
                                    }
                                }
                            }
                            if (itemimageYes[k] != true) {
                                itemimageYes[k] = false;
                            }


                        saveState();
                        finish();
                    }else{
                        Toast.makeText(postinsertActivity.this, "내용을 입력하세요", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(postinsertActivity.this, "제목을 입력하세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    protected  void saveState(){
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("head",head);
        editor.putString("body",body);
        for(int i = 0; i<100; i++) {
            if(itemnumber[i] == nownumber){
                editor.putString("itemhead"+i, itemhead[i]);
                editor.putString("itembody"+i, itembody[i]);
                editor.putBoolean("itemimageYes"+i,itemimageYes[i]);
                editor.putString("itemuri"+i,itemuri[i]);

            }
        }
        editor.commit();
    }

    public void showrecycle(){

            Log.d("0번임ㅁㅇㅇㅇㅇ","ㅇㄴ");
            recyclerView = findViewById(R.id.recyclerViewwrite2);
            layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
            //recyclerView.setNestedScrollingEnabled(false);
            imagerestore();//쉐어드에서 이미지 불러오기
            Log.d("1번임ㅁㅇㅇㅇㅇ","ㅇㄴ");
            for (int i = 0; i < 100; i++) {
                if (itemnumber[i] == nownumber) {
                    if (itemuri[i] != null) {
                        //Log.d("아이템 이미지 : ",itemimage[i]);
                        spilturi = itemuri[i].split(",");

                    }
                }
            }
            if(spilturi != null) {
                for (int i = 0; i < spilturi.length; i++) {
                    str_image[i] = spilturi[i];
                }
                if (spilturi != null) {
                    for (int i = 0; i < spilturi.length; i++) {
                        if (spilturi[i] == null) {

                        } else {
                            Log.d("2번임ㅁㅇㅇㅇㅇ", "ㅇㄴ");
                            adapter3.addItem(new imagelist(spilturi[i]));
                        }
                    }
                }
            }
            recyclerView.setAdapter(adapter3);

    }

    protected void imagerestore(){
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
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        recyclerView = findViewById(R.id.recyclerViewwrite2);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter3.resetItem();
/*
        if (spilturi != null) {//이거 제거
            for (int i = 0; i < spilturi.length; i++) {
                if (spilturi[i] == null) {

                } else {
                    Log.d("몇개","??");
                    adapter3.addItem(new imagelist(spilturi[i]));
                }
            }
        }

 */


        for(int i = 0; i<100; i++){
            if(str_image[i] == null){

            }else{
                Log.d("몇개2","??");
                adapter3.addItem(new imagelist(str_image[i]));
            }
        }

        recyclerView.setAdapter(adapter3);
        Log.d("onresume","ㅇddㅇㅇ");

    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    // 이미지 선택
    private void doSelectImage()
    {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        try
        {
            startActivityForResult(i, SELECT_IMAGE);
        } catch (android.content.ActivityNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        Log.d("onresult","ㅇㅇㅇ");
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == RESULT_OK)
        {
            if (requestCode == SELECT_IMAGE)//앨범에서 사진 선택후
            {
                Uri uri = intent.getData();
                //String path = getPath(uri);
                //String name = getName(uri);
                //String uriId = getUriId(uri);

                for(int i = 0; i<100; i++){
                    if(str_image[i] == null){//사진 절대 주소를 저장
                        //itemuri[i] = uri.toString();//uri 저장 리사이클러뷰에 쓰기위한 배열

                        //////////////////////////여기부턴 uri -> stream -> bitmap -> string 변환// 저장,인텐트를 위한 배열
                        try {
                            in = getContentResolver().openInputStream(uri);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }


                        Bitmap img2 = BitmapFactory.decodeStream(in);
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                        img2.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

                        byte[] imageBytes = byteArrayOutputStream.toByteArray();
                        //비트맵형식을 string으로 바꿔 저장
                        str_image[i] = Base64.encodeToString(imageBytes, Base64.NO_WRAP);

                        i = 100;
                    }
                }


                //Log.e("###", "실제경로 : " + path + "\n파일명 : " + name + "\nuri : " + uri.toString() + "\nuri id : " + uriId);
            }

        }
    }
}
