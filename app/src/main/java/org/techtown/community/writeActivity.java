package org.techtown.community;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

public class writeActivity extends AppCompatActivity {

    private final int SELECT_IMAGE = 1;
    private final int SELECT_MOVIE = 2;
    private InputStream in;
    private String[] saveuri, itemuri, Stringimage;

    private String[] itememail, itemnickname, itemhead, itembody, itemtime, spinneritem;
    private String nowemail, nownickname, nowtime;
    private ImageView exit;
    private Button write, fileupload;
    private TextView spinnertext;
    private EditText headinput, bodyinput;
    private int[] itemnumber, itemboard;
    private int writeindex;
    private RecyclerView recyclerView;//리사이클러뷰
    private LinearLayoutManager layoutManager;//리사이클러뷰
    private final imagelistAdapter adapter2 = new imagelistAdapter();//리사이클러뷰
    private boolean[] itemimageYes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        itemboard = new int[100];
        itemimageYes = new boolean[100];

        Stringimage = new String[100];
        itemuri = new String[100];
        saveuri = new String[100];

        itemnickname = new String[100];
        itememail = new String[100];
        itemhead = new String[100];
        itembody = new String[100];
        itemnumber = new int[100];
        itemtime = new String[100];

        final Intent intent = getIntent();
        if(intent.getExtras() != null) {
            Bundle bundle = intent.getExtras();
            //itemuri = bundle.getStringArray("itemuri");
            itememail = bundle.getStringArray("itememail");
            itemnickname = bundle.getStringArray("itemnickname");
            nowemail = bundle.getString("nowemail");
            nownickname = bundle.getString("nownickname");

            itemhead = bundle.getStringArray("itemhead");
            itembody = bundle.getStringArray("itembody");
            itemnumber = bundle.getIntArray("itemnumber");//아이템 고유번호
            itemtime = bundle.getStringArray("itemtime");
            itemimageYes = bundle.getBooleanArray("itemimageYes");
        }
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        if(pref != null) {
            for(int i = 0; i<100; i++) {
                if(itemuri == null){
                    i = 100;
                }else {
                    itemuri[i] = pref.getString("itemuri" + i, null);
                }
            }
            for(int i = 0; i<100; i++) {
                itemboard[i] = pref.getInt("itemboard" + i, 0);
            }
        }
        headinput = findViewById(R.id.head);
        bodyinput = findViewById(R.id.body);

        spinneritem = new String[]{"자유게시판", "공지사항", "사건/사고", "이벤트", "중고거래"};
        //spinnertext = findViewById(R.id.spinnertext);
        final Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,spinneritem);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //spinnertext.setText(spinneritem[position]);
                writeindex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //spinnertext.setText("게시판 선택");
            }
        });



        recyclerView = findViewById(R.id.recyclerViewwrite);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        //recyclerView.setNestedScrollingEnabled(false);
        //adapter2.addItem(new imagelist("image"));
        //recyclerView.setAdapter(adapter2);

        fileupload = findViewById(R.id.fileupload);
        fileupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSelectImage();
            }
        });

        write = findViewById(R.id.up1);
        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(headinput.getText().toString().length()>0){//입력값 null체크
                    if(bodyinput.getText().toString().length()>0){//입력값 null체크
                        for(int i = 0; i<100; i++){
                            if(itemhead[i] == null){//빈자리 찾음
                                itemhead[i] = headinput.getText().toString();
                                itembody[i] = bodyinput.getText().toString();
                                itememail[i] = nowemail;
                                itemnickname[i] = nownickname;
                                itemboard[i] = writeindex;

                                long now = System.currentTimeMillis();
                                Date date = new Date(now);
                                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
                                nowtime = sdf.format(date);
                                Log.d("nowtime",nowtime);
                                itemtime[i] = nowtime;
                                if(i == 0) {
                                    itemnumber[i] = i + 1;
                                }else{
                                    itemnumber[i] = itemnumber[i - 1] + 1;
                                }

                                for(int k = 0; k<100; k++){
                                    if(Stringimage[k] == null){
                                        k = 100;
                                    }else{
                                        itemimageYes[i] = true;

                                        if(itemuri[i] == null){
                                            itemuri[i] = Stringimage[k];//첫항 널방지
                                            //Log.d("Stringimage"+i+"::",Stringimage[i]);
                                        }else {
                                            itemuri[i] +=","+Stringimage[k];
                                            //itemuri 인덱스 = 메인item포지션
                                            //Stringimage 인덱스 = 글쓰기item 포지션
                                            //Log.d("Stringimage"+i+"::",Stringimage[i]);
                                        }
                                    }
                                }
                                if(itemimageYes[i] != true){
                                    itemimageYes[i] = false;
                                }
                                i = 100;//탈출

                            }
                        }

                        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        for(int i = 0; i<100; i++) {
                            if(itemhead[i] == null){
                                i = 100;
                            }else {
                                editor.putString("itemnickname" + i, itemnickname[i]);
                                editor.putString("itememail"+i,itememail[i]);
                                editor.putString("itemhead"+i,itemhead[i]);
                                editor.putString("itembody"+i,itembody[i]);
                                editor.putInt("itemnumber"+i,itemnumber[i]);
                                editor.putString("itemtime"+i,itemtime[i]);
                                editor.putString("itemuri"+i,itemuri[i]);
                                editor.putBoolean("itemimageYes"+i,itemimageYes[i]);
                                editor.putInt("itemboard"+i,itemboard[i]);
                            }
                        }
                        editor.commit();

                        finish();
                    }else {
                        Toast.makeText(writeActivity.this, "내용을 입력하세요", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(writeActivity.this, "제목을 입력하세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        exit = findViewById(R.id.exit1);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        adapter2.setOnItemClickListener(new OnlistItemClickListener2() {
            @Override
            public void onItemClick(imagelistAdapter.ViewHolder holder, View view, int position) {
                adapter2.resetItem();
                for(int i = 0; i<100; i++){
                    if(saveuri[position + i + 1] == null){
                        saveuri[position + i] = null;
                        Stringimage[position + i] = null;
                        Log.d("포지션 : ",Integer.toString(position));
                        i = 100;
                    }else {
                        saveuri[position + i] = saveuri[position + i + 1];
                        Stringimage[position + i] = Stringimage[position + i + 1];
                    }
                }
                for(int i = 0; i<100; i++){
                    if(saveuri[i] == null){
                        i = 100;
                    }else{
                        try {
                            in = getContentResolver().openInputStream(Uri.parse(saveuri[i]));//uri -> inputstream
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        adapter2.addItem(new imagelist(in));
                    }
                }
                recyclerView.setAdapter(adapter2);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter2.resetItem();

        for(int i = 0; i<100; i++){
            if(saveuri[i] == null){
                i = 100;
            }else{
                try {
                    in = getContentResolver().openInputStream(Uri.parse(saveuri[i]));//uri -> inputstream
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                adapter2.addItem(new imagelist(in));
            }
        }

        recyclerView.setAdapter(adapter2);
        Log.d("onresume","ㅇddㅇㅇ");

    }

    ////////////////////////////////////////////////////////////////////////////////////

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

    // 동영상선택
    private void doSelectMovie()
    {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("video/*");
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        try
        {
            startActivityForResult(i, SELECT_MOVIE);
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
                String path = getPath(uri);
                String name = getName(uri);
                String uriId = getUriId(uri);

                for(int i = 0; i<100; i++){
                    if(saveuri[i] == null){//사진 절대 주소를 저장
                        saveuri[i] = uri.toString();//uri 저장 리사이클러뷰에 쓰기위한 배열

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
                        Stringimage[i] = Base64.encodeToString(imageBytes, Base64.NO_WRAP);

                        i = 100;
                    }
                }


                Log.e("###", "실제경로 : " + path + "\n파일명 : " + name + "\nuri : " + uri.toString() + "\nuri id : " + uriId);
            }
            else if (requestCode == SELECT_MOVIE)
            {
                Uri uri = intent.getData();
                String path = getPath(uri);
                String name = getName(uri);//name,uri는 있지만 실제경로,아이디가 null임
                String uriId = getUriId(uri);
                Log.e("###", "실제경로 : " + path + "\n파일명 : " + name + "\nuri : " + uri.toString() + "\nuri id : " + uriId);
            }
        }
    }

    // 실제 경로 찾기
    private String getPath(Uri uri)
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    // 파일명 찾기
    private String getName(Uri uri)
    {
        String[] projection = { MediaStore.Images.ImageColumns.DISPLAY_NAME };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DISPLAY_NAME);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    // uri 아이디 찾기
    private String getUriId(Uri uri)
    {
        String[] projection = { MediaStore.Images.ImageColumns._ID };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}
