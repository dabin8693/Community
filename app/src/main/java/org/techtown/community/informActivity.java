package org.techtown.community;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class informActivity extends AppCompatActivity {

    private final int SELECT_IMAGE = 1;
    private final int SELECT_CARMER = 2;
    private File outputFile;
    private InputStream in;
    private String[] emaillist, nicknamelist, itemnickname, itemhead, itembody, itememail, itemtime;
    private String nowemail, nownickname, afternickname;
    private ImageView exit, userimage;
    private Button nicknamechange, logout;
    private TextView email, nickname, mywrite, mycomment;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private final listAdapter adapter = new listAdapter();
    private int ok;
    private String[] informitemhead, informitemnickname, informitembody, informitememail, informitemtime;
    private String[] chead, cnickname, cemail, cbody, ctime;
    private int[] informitemnumber, itemnumber, itemshowpeople, informitemshowpeople,itemcommentnumber,informitemcommentnumber, ccommentnumber, informitemboard, itemboard;
    private int[] cnumber, cshowpeople, cboard;
    private boolean[] itemimageYes, informitemimageYes;
    private boolean[] cimageYes;
    private int category;
    private File file;
    private String imageFilePath, profile, stringimage;
    private Uri photoUri;
    private ProgressDialog progressDialog;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inform);

        itemcommentnumber = new int[100];
        informitemcommentnumber = new int[100];
        ccommentnumber = new int[100];
        cemail = new String[100];
        chead = new String[100];
        cnickname = new String[100];
        cbody = new String[100];
        ctime = new String[100];
        cnumber = new int[100];
        cshowpeople = new int[100];
        cimageYes = new boolean[100];
        cboard = new int[100];

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

        emaillist = new String[100];
        nicknamelist = new String[100];
        itemnickname = new String[100];
        itemhead = new String[100];
        itembody = new String[100];
        itememail = new String[100];
        itemtime = new String[100];
        itemboard = new int[100];

        final Intent intent = getIntent();
        if(intent.getExtras() != null) {
            Bundle bundle = intent.getExtras();
            nowemail = bundle.getString("nowemail");
            nownickname = bundle.getString("nownickname");
            nicknamelist = bundle.getStringArray("nicknamelist");
            emaillist = bundle.getStringArray("emaillist");
            itemnickname = bundle.getStringArray("itemnickname");
            itemhead = bundle.getStringArray("itemhead");
            itembody = bundle.getStringArray("itembody");
            itememail = bundle.getStringArray("itememail");
            itemnumber = bundle.getIntArray("itemnumber");
            itemtime = bundle.getStringArray("itemtime");
            itemshowpeople = bundle.getIntArray("itemshowpeople");
            itemimageYes = bundle.getBooleanArray("itemimageYes");
        }



        restoreState();
        userimage = findViewById(R.id.userimage);
        mywrite = findViewById(R.id.mywrite);
        mycomment = findViewById(R.id.mycomment);
        mywrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = 0;
                mywrite.setTextColor(Color.parseColor("#FF000000"));
                mycomment.setTextColor(Color.parseColor("#60000000"));
                adapter.resetItem();//아이템 리셋후 다시 생성
                int k = 0;
                int t = 0;
                for(int i = 0; i<100; i++){
                    if(itemnickname[i] == null){
                        i = 100;
                    }else{
                        if(nownickname.equals(itemnickname[i]) == true){
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
        mycomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = 1;
                mywrite.setTextColor(Color.parseColor("#60000000"));
                mycomment.setTextColor(Color.parseColor("#FF000000"));
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
        });
        mywrite.setTextColor(Color.parseColor("#FF000000"));
        mycomment.setTextColor(Color.parseColor("#60000000"));
        restoreprofile();
        if(stringimage != null){
            byte[] decodedByteArray = Base64.decode(stringimage, Base64.NO_WRAP);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);

            userimage.setImageBitmap(decodedBitmap);
        }
        userimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String str[] = {"카메라","앨범"};
                AlertDialog.Builder builder = new AlertDialog.Builder(informActivity.this);
                builder.setTitle("선택")
                        .setNegativeButton("취소",null)
                        .setItems(str, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which == 0){
                                    doSelectCarmer();
                                }else if(which == 1){
                                    doSelectImage();
                                }
                                Toast.makeText(getApplicationContext(),"선택 "+str[which]+"위치 "+Integer.toString(which),Toast.LENGTH_LONG).show();
                            }
                        });
                //builder.show();
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        userimage.setBackground(new ShapeDrawable(new OvalShape()));
        userimage.setClipToOutline(true);

        email = findViewById(R.id.informemail);
        email.setText(nowemail);
        nickname = findViewById(R.id.informnickname);
        nickname.setText(nownickname);//닉네임 변경시 다시 설정

        exit = findViewById(R.id.informexit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("nownickname",null);
                editor.putString("nowemail",null);
                editor.putInt("login",0);
                editor.commit();
                finish();
            }
        });

        nicknamechange = findViewById(R.id.nicknamechange);
        nicknamechange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentnickname = new Intent(informActivity.this, nicknameActivity.class);
                intentnickname.putExtra("itemnickname",itemnickname);
                intentnickname.putExtra("nicknamelist",nicknamelist);
                intentnickname.putExtra("nownickname",nownickname);
                startActivity(intentnickname);
            }
        });

        recyclerView = findViewById(R.id.recyclerView2);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        int k = 0;
        int t = 0;
        for(int i = 0; i<100; i++){
            if(itemnickname[i] == null){
                i = 100;
            }else{
                if(nownickname.equals(itemnickname[i]) == true){
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

        adapter.setOnItemClickListener(new OnlistItemClickListener() {//post로 이동한후 뒤로가면 메인으로 가게 설정함//안그러면복잡함
            @Override
            public void onItemClick(listAdapter.ViewHolder holder, View view, int position) {
                if (category == 0) {
                    int s = 0;
                    for (int i = 0; i < 100; i++) {
                        if (informitemhead[i] == null) {
                            s = i - 1;
                            i = 100;
                        }
                    }
                    //Toast.makeText(getApplicationContext(),"아이템 선택: "+item.getTitle(),Toast.LENGTH_LONG).show();
                    ok = 1;
                    Toast.makeText(getApplicationContext(), "아이템 선택: " + position, Toast.LENGTH_LONG).show();
                    Intent intentitem = new Intent(informActivity.this, postActivity.class);
                    intentitem.putExtra("head", informitemhead[s - position]);
                    intentitem.putExtra("body", informitembody[s - position]);
                    intentitem.putExtra("nickname", informitemnickname[s - position]);
                    intentitem.putExtra("email", informitememail[s - position]);
                    intentitem.putExtra("nownumber", informitemnumber[s - position]);//nownumber는 어뎁터에서만 intent / post,postinsert 사용
                    intentitem.putExtra("showpeople", informitemnumber[s - position]);
                    intentitem.putExtra("nowemail", nowemail);
                    intentitem.putExtra("nownickname", nownickname);

                    intentitem.putExtra("itemtime", itemtime);
                    intentitem.putExtra("itemnumber", itemnumber);
                    intentitem.putExtra("itememail", itememail);//글삭제시 최신화하기 위해
                    intentitem.putExtra("itemnickname", itemnickname);//글삭제시 최신화하기 위해
                    intentitem.putExtra("itemhead", itemhead);//글쓴이 정보창 글보기 용도
                    intentitem.putExtra("itembody", itembody);//글쓴이 정보창 글보기 용도
                    intentitem.putExtra("itemshowpeople", itemshowpeople);
                    intentitem.putExtra("itemimageYes", itemimageYes);
                    startActivity(intentitem);
                    progressDialog = new ProgressDialog(informActivity.this);
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
                    Intent intentitem = new Intent(informActivity.this, postActivity.class);
                    intentitem.putExtra("head", chead[s - position]);
                    intentitem.putExtra("body", cbody[s - position]);
                    intentitem.putExtra("nickname", cnickname[s - position]);
                    intentitem.putExtra("email", cemail[s - position]);
                    intentitem.putExtra("nownumber",cnumber[s - position]);//nownumber는 어뎁터에서만 intent / post,postinsert 사용
                    intentitem.putExtra("showpeople", cshowpeople[s - position]);
                    intentitem.putExtra("nowemail", nowemail);
                    intentitem.putExtra("nownickname", nownickname);

                    intentitem.putExtra("itemtime", itemtime);
                    intentitem.putExtra("itemnumber", itemnumber);
                    intentitem.putExtra("itememail", itememail);//글삭제시 최신화하기 위해
                    intentitem.putExtra("itemnickname", itemnickname);//글삭제시 최신화하기 위해
                    intentitem.putExtra("itemhead", itemhead);//글쓴이 정보창 글보기 용도
                    intentitem.putExtra("itembody", itembody);//글쓴이 정보창 글보기 용도
                    intentitem.putExtra("itemshowpeople", itemshowpeople);
                    intentitem.putExtra("itemimageYes", itemimageYes);
                    startActivity(intentitem);
                    progressDialog = new ProgressDialog(informActivity.this);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setMessage("잠시 기다려 주세요.");
                    progressDialog.show();
                }
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        Log.d("onresult","ㅇㅇㅇ");
        super.onActivityResult(requestCode, resultCode, intent);
        Log.d("ㄴㅇㄹㄴㄹㅇ ", "ㅇㅇ");

        if (resultCode == RESULT_OK)
        {
            if (requestCode == SELECT_IMAGE)//앨범에서 사진 선택후
            {
                for(int i = 0; i<100; i++) {
                    Log.d("카메라ㄴㄴ ", "ㅇㅇ");
                }
                Uri uri = intent.getData();
                try {
                    in = getContentResolver().openInputStream(uri);
                    Bitmap img2 = BitmapFactory.decodeStream(in);
                    userimage.setImageBitmap(img2);

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                    img2.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

                    byte[] imageBytes = byteArrayOutputStream.toByteArray();
                    //비트맵형식을 string으로 바꿔 저장
                    profile = Base64.encodeToString(imageBytes, Base64.NO_WRAP);
                    Log.d("프로파일 : ",profile);
                    imageSave();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
            else if (requestCode == SELECT_CARMER)
            {
                Log.d("이미지파일:",imageFilePath);
                Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath);
                ExifInterface exif = null;
                try{
                    exif = new ExifInterface(imageFilePath);
                } catch (IOException e){
                    e.printStackTrace();
                }

                int exifOrientation;
                int exifDegree;

                if(exif != null){
                    exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                    exifDegree = exifOrientationToDegress(exifOrientation);
                }else{
                    exifDegree = 0;
                }
                userimage.setImageBitmap(rotate(bitmap,exifDegree));

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                rotate(bitmap,exifDegree).compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

                byte[] imageBytes = byteArrayOutputStream.toByteArray();
                //비트맵형식을 string으로 바꿔 저장
                profile = Base64.encodeToString(imageBytes, Base64.NO_WRAP);
                imageSave();
                Log.d("프로파일 : ",profile);

            }
        }
    }

    private int exifOrientationToDegress(int exifOrientation){
        if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_90){
            return 90;
        }else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_180){
            return 180;
        }else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_270){
            return 270;
        }
        return 0;
    }

    private Bitmap rotate(Bitmap bitmap, float degree){
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),matrix,true);
    }
    @Override
    protected void onResume() {
        super.onResume();
        recyclerView.setAdapter(adapter);
        Log.d("리줌:","ㅁㅎㅁㅎㅇ");
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
        Log.d("리스타트:","ㅁㅎㅁㅎㅇ");

        restoreState();
        if(category == 0) {
            adapter.resetItem();//아이템 리셋후 다시 생성
            int k = 0;
            int t = 0;
            for (int i = 0; i < 100; i++) {
                if (itemnickname[i] == null) {
                    i = 100;
                } else {
                    if (nownickname.equals(itemnickname[i]) == true) {
                        Log.d("헤드" + i + ":", itemhead[i]);
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
                    Log.d("어뎁터:", "ㅁㅎㅁㅎㅇ");
                    adapter.addItem(new list(informitemhead[z], informitemnickname[z], informitemtime[z], informitemshowpeople[z], informitemcommentnumber[k], informitemimageYes[z],informitemboard[z]));
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
    private void doSelectCarmer()
    {
        TedPermission.with(getApplicationContext())
                .setPermissionListener(permissionListener)
                .setRationaleMessage("카메라 권한이 필요합니다.")
                .setDeniedMessage("거부하셨습니다.")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA)
                .check();

        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if(i.resolveActivity(getPackageManager()) != null){
            File photoFile = null;
            try{
                photoFile = createImageFile();
            }catch (IOException e){

            }
            if(photoFile != null){
                photoUri = FileProvider.getUriForFile(getApplicationContext(),getPackageName(),photoFile);
                i.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
                startActivityForResult(i, SELECT_CARMER);
            }
        }
        //Uri probiderURI = FileProvider.getUriForFile(informActivity.this, getPackageName(),photoFile);
        //i.putExtra(MediaStore.EXTRA_OUTPUT,probiderURI);
        //i.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(file));
        //Uri uri = FileProvider.getUriForFile(getBaseContext(), "com.onedelay.chap7.fileprovider", file);
        //i.putExtra(MediaStore.EXTRA_OUTPUT, uri);

    }
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "TEST_" + timeStamp + "_";
        File storgeDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,".jpg",storgeDir);
        imageFilePath = image.getAbsolutePath();
        return image;
    }

    PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Toast.makeText(getApplicationContext(),"권한이 허용됨",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(getApplicationContext(),"권한이 거부됨",Toast.LENGTH_SHORT).show();

        }
    };
    protected void restoreState(){
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        if(pref != null){
            for(int i = 0; i<100; i++){
                if(itemhead[i] == null){
                    i = 100;
                }else{
                    //Log.d("아이는 "+i+":",Integer.toString(i));
                    itemshowpeople[i] = pref.getInt("itemshowpeople"+i,0);
                    itemhead[i] = pref.getString("itemhead"+i,null);
                    itembody[i] = pref.getString("itembody"+i,null);
                    itemimageYes[i] = pref.getBoolean("itemimageYes"+i,false);
                    itemnickname[i] = pref.getString("itemnickname"+i,null);
                    itememail[i] = pref.getString("itememail"+i, null);
                    itemnumber[i] = pref.getInt("itemnumber"+i,0);
                    itemtime[i] = pref.getString("itemtime"+i,null);
                    itemcommentnumber[i] = pref.getInt("itemcommentnumber"+i,0);
                    itemboard[i] = pref.getInt("itemboard"+i,0);
                    //Log.d("아이템닉네임 "+i+":",itemnickname[i]);
                }
            }

        }
    }
    protected void restoreprofile(){
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        if(pref != null){
            for(int i = 0; i<100; i++){
                if(emaillist[i] == null){
                    i = 100;
                }else{
                    if(nowemail.equals(emaillist[i]) == true){
                        stringimage = pref.getString("profile"+i,null);
                    }
                }
            }

        }
    }
    protected void imageSave(){
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        for(int i = 0; i<100; i++){
            if(emaillist[i] == null){

            }else {
                if (nowemail.equals(emaillist[i]) == true) {
                    editor.putString("profile" + i, profile);
                }
            }
        }
        editor.commit();
    }
    protected  void saveState(){
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("nownickname",nownickname);
        for(int i = 0; i<100; i++) {
         if(itemnickname[i] == null){
             i = 100;
         }else {
             editor.putString("itemnickname"+i, itemnickname[i]);
         }
        }
        for(int i = 0; i<100; i++) {
            if(nicknamelist[i] == null){
                i = 100;
            }else {
                editor.putString("nicknamelist"+i, nicknamelist[i]);
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
                if(bundle.getInt("nicknameActivity") == 1){
                    nownickname = bundle.getString("nownickname");
                    nicknamelist = bundle.getStringArray("nicknamelist");
                    itemnickname = bundle.getStringArray("itemnickname");
                    saveState();
                    nickname.setText(nownickname);
                    //adapter = new listAdapter();
                    adapter.resetItem();//아이템 리셋후 다시 생성
                    int k = 0;
                    int t = 0;
                    for(int i = 0; i<100; i++){
                        if(itemnickname[i] == null){
                            i = 100;
                        }else{
                            if(nownickname.equals(itemnickname[i]) == true){
                                informitemhead[k] = itemhead[i];
                                informitemnickname[k] = itemnickname[i];
                                informitembody[k] = itembody[i];
                                informitememail[k] = itememail[i];
                                informitemnumber[k] = itemnumber[i];
                                informitemtime[k] = itemtime[i];
                                //adapter.addItem(new list(informitemhead[k], informitemnickname[k],informitemtime[k],0,0));
                                k++;
                                t = k;
                            }
                        }
                    }
                    if(t >= 1) {
                        for (int z = t - 1; z >= 0; z--) {
                            adapter.addItem(new list(informitemhead[z], informitemnickname[z], informitemtime[z], 0, 0,false,0));
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
                                if (temp.equals(nowemail) == true) {
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
