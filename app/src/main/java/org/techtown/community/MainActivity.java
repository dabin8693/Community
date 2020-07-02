package org.techtown.community;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;//네비게이션바
    private View drawerView;//네비게이션바
    private RecyclerView recyclerView;//리사이클러뷰
    private LinearLayoutManager layoutManager;//리사이클러뷰
    private final listAdapter adapter = new listAdapter();//리사이클러뷰
    private final listAdapter2 adapter2 = new listAdapter2();//리사이클러뷰
    private LinearLayout container, container2;//네비게이션바 인플레이터
    private LayoutInflater inflater;//네비게이션바 인플레잉터
    private ImageView menu, exit, notice, write, inform, search;
    private TextView allpost, hotpost, noticepost, board, board0, board1, board2, board3, board4, mtboard;
    private String[] emaillist, passwordlist, nicknamelist;//로그인,회원가입시 필요
    private String[] itemhead, itembody, itemnickname, itememail, itemtime, itemuri, hothead, hotnickname, hottime, hotbody, hotemail;//글 정보
    private String nowemail, nownickname;//메인창에서 씀
    private String stringimage;
    private int login, fmnumber, bdnumber;// 1 = 로그인 0 = 로그아웃
    private int[] itemnumber, itemshowpeople, itemgoodnumber, hotshowpeople, hotnumber, itemboard, hotgoodnumber, itemcommentnumber, hotcommentnumber;
    private boolean[] itemimageYes, hotimageYes;
    private String[] bhead, bnickname, btime, bemail, bbody, nhead, nnickname, ntime, nemail, nbody;
    private int[] bshowpeople, bnumber, bgoodnumber, nshowpeople, ngoodnumber, nnumber, ncommentnumber, bcommentnumber, bboard, nboard, hotboard;
    private boolean[] bimageYes, nimageYes;
    private ProgressDialog progressDialog;
    private Handler han;
    private Thread th;
    private Button loading;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //Shared shared = new Shared(MainActivity.this);
        itemcommentnumber = new int[100];
        itemboard = new int[100];
        itemgoodnumber = new int[100];
        itemimageYes = new boolean[100];
        itemuri = new String[100];
        emaillist = new String[100];
        passwordlist = new String[100];
        nicknamelist = new String[100];
        itemnickname = new String[100];
        itememail = new String[100];
        itemhead = new String[100];
        itembody = new String[100];
        itemnumber = new int[100];
        itemtime = new String[100];
        itemshowpeople = new int[100];
        login = 0;

        bdnumber = 10;//초기값 전체글
        fmnumber = 1;//초기값 전체글



        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawerView = (View)(findViewById(R.id.drawer_view));
        //ctrl+5 사용가능 메소드
        //clearState();
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("잠시 기다려 주세요.");
        progressDialog.show();
        han = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                Log.d("핸들러시작","ㅇㅇ");
                int a = msg.arg1;
                Log.d("a는"+Integer.toString(a),"ㅇㅇ");
                if(a == 1){
                    recyclerView.setAdapter(adapter);
                    progressDialog.dismiss();
                    Log.d("프그 종료","ㅇㅇ");
                }
            }
        };

        th = new Thread(new Runnable() {
            @Override
            public void run() {
                boolean bool = true;
                while (bool) {
                    Message msg = han.obtainMessage();
                    restoreState();
                    int k = 0;
                    for(int i = 0; i<100; i++) {
                        if(itemhead[i] == null) {
                            k = i;
                            i = 100;
                        }
                    }
                    if(k >= 1) {
                        for (int t = k - 1; t >= 0; t--) {

                            adapter.addItem(new list(itemhead[t], itemnickname[t], itemtime[t], itemshowpeople[t], itemcommentnumber[t], itemimageYes[t], itemboard[t]));
                        }
                    }
                    Log.d("쓰레드 시작","ㅇㅇ");
                    msg.arg1 = 1;
                    han.sendMessage(msg);
                    bool = false;
                }
            }
        });
        th.start();


        //Log.d("create",Integer.toString(login));

        mtboard = findViewById(R.id.mtboard);
        board = findViewById(R.id.board);//전체글
        board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                container2.removeAllViewsInLayout();
                inflater.inflate(R.layout.noitem,container2,true);
                hotpost.setTextColor(Color.parseColor("#60000000"));
                allpost.setTextColor(Color.parseColor("#FF000000"));
                noticepost.setTextColor(Color.parseColor("#60000000"));
                noticepost.setText("공지사항");
                bdnumber = 10;
                fmnumber = 1;
                allpost.setText("전체글");
                mtboard.setText("전체글게시판");
                adapter.resetItem();
                int k = 0;
                for(int i = 0; i<100; i++) {
                    if(itemhead[i] == null) {
                        k = i;
                        i = 100;
                    }
                }
                if(k >= 1) {
                    for (int t = k - 1; t >= 0; t--) {

                        adapter.addItem(new list(itemhead[t], itemnickname[t], itemtime[t], itemshowpeople[t], itemcommentnumber[t], itemimageYes[t],itemboard[t]));
                    }
                }
                recyclerView.setAdapter(adapter);
                drawerLayout.closeDrawers();
            }

        });
        board0 = findViewById(R.id.board0);//자유
        board0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                container2.removeAllViewsInLayout();
                inflater.inflate(R.layout.noitem,container2,true);
                hotpost.setTextColor(Color.parseColor("#60000000"));
                allpost.setTextColor(Color.parseColor("#FF000000"));
                noticepost.setTextColor(Color.parseColor("#60000000"));
                noticepost.setText("공지사항");
                bdnumber = 0;
                fmnumber = 1;
                allpost.setText("자유글");
                mtboard.setText("자유게시판");
                bhead = new String[100];
                bimageYes = new boolean[100];
                bnickname = new String[100];
                bshowpeople = new int[100];
                btime = new String[100];
                bbody = new String[100];
                bemail = new String[100];
                bnumber = new int[100];
                bgoodnumber = new int[100];
                bcommentnumber = new int[100];
                bboard = new int[100];

                int s = 0;
                for(int i = 0; i<100; i++){
                    if(itemnumber[i] == 0){
                        i = 100;
                    }else{
                        if(itemboard[i] == 0){
                            bhead[s] = itemhead[i];
                            bimageYes[s] = itemimageYes[i];
                            bnickname[s] = itemnickname[i];
                            bshowpeople[s] = itemshowpeople[i];
                            btime[s] = itemtime[i];
                            bbody[s] = itembody[i];
                            bemail[s] = itememail[i];
                            bnumber[s] = itemnumber[i];
                            bgoodnumber[s] = itemgoodnumber[i];
                            bcommentnumber[s] = itemcommentnumber[i];
                            bboard[s] = itemboard[i];
                            s++;
                        }
                    }
                }
                adapter.resetItem();
                int k = 0;
                for(int i = 0; i<100; i++) {
                    if(bhead[i] == null) {
                        k = i;
                        i = 100;
                    }
                }
                if(k >= 1) {
                    for (int t = k - 1; t >= 0; t--) {

                        adapter.addItem(new list(bhead[t], bnickname[t], btime[t], bshowpeople[t], bcommentnumber[t], bimageYes[t], bboard[t]));
                    }
                }
                recyclerView.setAdapter(adapter);
                drawerLayout.closeDrawers();
            }
        });
        board1 = findViewById(R.id.board1);//공지
        board1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                container2.removeAllViewsInLayout();
                inflater.inflate(R.layout.noitem,container2,true);
                hotpost.setTextColor(Color.parseColor("#60000000"));
                allpost.setTextColor(Color.parseColor("#FF000000"));
                noticepost.setTextColor(Color.parseColor("#60000000"));
                noticepost.setText("공지사항");
                bdnumber = 1;
                fmnumber = 1;
                allpost.setText("공지글");
                mtboard.setText("공지글게시판");
                bhead = new String[100];
                bimageYes = new boolean[100];
                bnickname = new String[100];
                bshowpeople = new int[100];
                btime = new String[100];
                bbody = new String[100];
                bemail = new String[100];
                bnumber = new int[100];
                bgoodnumber = new int[100];
                bcommentnumber = new int[100];
                bboard = new int[100];

                int s = 0;
                for(int i = 0; i<100; i++){
                    if(itemnumber[i] == 0){
                        i = 100;
                    }else{
                        if(itemboard[i] == 1){
                            bhead[s] = itemhead[i];
                            bimageYes[s] = itemimageYes[i];
                            bnickname[s] = itemnickname[i];
                            bshowpeople[s] = itemshowpeople[i];
                            btime[s] = itemtime[i];
                            bbody[s] = itembody[i];
                            bemail[s] = itememail[i];
                            bnumber[s] = itemnumber[i];
                            bgoodnumber[s] = itemgoodnumber[i];
                            bcommentnumber[s] = itemcommentnumber[i];
                            bboard[s] = itemboard[i];
                            s++;
                        }
                    }
                }
                adapter.resetItem();
                int k = 0;
                for(int i = 0; i<100; i++) {
                    if(bhead[i] == null) {
                        k = i;
                        i = 100;
                    }
                }
                if(k >= 1) {
                    for (int t = k - 1; t >= 0; t--) {

                        adapter.addItem(new list(bhead[t], bnickname[t], btime[t], bshowpeople[t], bcommentnumber[t], bimageYes[t], bboard[t]));
                    }
                }
                recyclerView.setAdapter(adapter);
                drawerLayout.closeDrawers();
            }
        });
        board2 = findViewById(R.id.board2);//사건
        board2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                container2.removeAllViewsInLayout();
                inflater.inflate(R.layout.noitem,container2,true);
                hotpost.setTextColor(Color.parseColor("#60000000"));
                allpost.setTextColor(Color.parseColor("#FF000000"));
                noticepost.setTextColor(Color.parseColor("#60000000"));
                noticepost.setText("뉴스");
                bdnumber = 2;
                fmnumber = 1;
                allpost.setText("사건/사고");
                mtboard.setText("사건/사고게시판");
                bhead = new String[100];
                bimageYes = new boolean[100];
                bnickname = new String[100];
                bshowpeople = new int[100];
                btime = new String[100];
                bbody = new String[100];
                bemail = new String[100];
                bnumber = new int[100];
                bgoodnumber = new int[100];
                bcommentnumber = new int[100];
                bboard = new int[100];

                int s = 0;
                for(int i = 0; i<100; i++){
                    if(itemnumber[i] == 0){
                        i = 100;
                    }else{
                        if(itemboard[i] == 2){
                            bhead[s] = itemhead[i];
                            bimageYes[s] = itemimageYes[i];
                            bnickname[s] = itemnickname[i];
                            bshowpeople[s] = itemshowpeople[i];
                            btime[s] = itemtime[i];
                            bbody[s] = itembody[i];
                            bemail[s] = itememail[i];
                            bnumber[s] = itemnumber[i];
                            bgoodnumber[s] = itemgoodnumber[i];
                            bcommentnumber[s] = itemcommentnumber[i];
                            bboard[s] = itemboard[i];
                            s++;
                        }
                    }
                }
                adapter.resetItem();
                int k = 0;
                for(int i = 0; i<100; i++) {
                    if(bhead[i] == null) {
                        k = i;
                        i = 100;
                    }
                }
                if(k >= 1) {
                    for (int t = k - 1; t >= 0; t--) {

                        adapter.addItem(new list(bhead[t], bnickname[t], btime[t], bshowpeople[t], bcommentnumber[t], bimageYes[t], bboard[t]));
                    }
                }
                recyclerView.setAdapter(adapter);
                drawerLayout.closeDrawers();
            }
        });
        board3 = findViewById(R.id.board3);//이벤트
        board3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                container2.removeAllViewsInLayout();
                inflater.inflate(R.layout.noitem,container2,true);
                hotpost.setTextColor(Color.parseColor("#60000000"));
                allpost.setTextColor(Color.parseColor("#FF000000"));
                noticepost.setTextColor(Color.parseColor("#60000000"));
                noticepost.setText("공지사항");
                bdnumber = 3;
                fmnumber = 1;
                allpost.setText("이벤트");
                mtboard.setText("이벤트게시판");
                bhead = new String[100];
                bimageYes = new boolean[100];
                bnickname = new String[100];
                bshowpeople = new int[100];
                btime = new String[100];
                bbody = new String[100];
                bemail = new String[100];
                bnumber = new int[100];
                bgoodnumber = new int[100];
                bcommentnumber = new int[100];
                bboard = new int[100];

                int s = 0;
                for(int i = 0; i<100; i++){
                    if(itemnumber[i] == 0){
                        i = 100;
                    }else{
                        if(itemboard[i] == 3){
                            bhead[s] = itemhead[i];
                            bimageYes[s] = itemimageYes[i];
                            bnickname[s] = itemnickname[i];
                            bshowpeople[s] = itemshowpeople[i];
                            btime[s] = itemtime[i];
                            bbody[s] = itembody[i];
                            bemail[s] = itememail[i];
                            bnumber[s] = itemnumber[i];
                            bgoodnumber[s] = itemgoodnumber[i];
                            bcommentnumber[s] = itemcommentnumber[i];
                            bboard[s] = itemboard[i];
                            s++;
                        }
                    }
                }
                adapter.resetItem();
                int k = 0;
                for(int i = 0; i<100; i++) {
                    if(bhead[i] == null) {
                        k = i;
                        i = 100;
                    }
                }
                if(k >= 1) {
                    for (int t = k - 1; t >= 0; t--) {

                        adapter.addItem(new list(bhead[t], bnickname[t], btime[t], bshowpeople[t], bcommentnumber[t], bimageYes[t], bboard[t]));
                    }
                }
                recyclerView.setAdapter(adapter);
                drawerLayout.closeDrawers();
            }
        });
        board4 = findViewById(R.id.board4);//중고
        board4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                container2.removeAllViewsInLayout();
                inflater.inflate(R.layout.noitem,container2,true);
                hotpost.setTextColor(Color.parseColor("#60000000"));
                allpost.setTextColor(Color.parseColor("#FF000000"));
                noticepost.setTextColor(Color.parseColor("#60000000"));
                noticepost.setText("공지사항");
                bdnumber = 4;
                fmnumber = 1;
                allpost.setText("중고글");
                mtboard.setText("중고거래게시판");
                bhead = new String[100];
                bimageYes = new boolean[100];
                bnickname = new String[100];
                bshowpeople = new int[100];
                btime = new String[100];
                bbody = new String[100];
                bemail = new String[100];
                bnumber = new int[100];
                bgoodnumber = new int[100];
                bcommentnumber = new int[100];
                bboard = new int[100];

                int s = 0;
                for(int i = 0; i<100; i++){
                    if(itemnumber[i] == 0){
                        i = 100;
                    }else{
                        if(itemboard[i] == 4){
                            bhead[s] = itemhead[i];
                            bimageYes[s] = itemimageYes[i];
                            bnickname[s] = itemnickname[i];
                            bshowpeople[s] = itemshowpeople[i];
                            btime[s] = itemtime[i];
                            bbody[s] = itembody[i];
                            bemail[s] = itememail[i];
                            bnumber[s] = itemnumber[i];
                            bgoodnumber[s] = itemgoodnumber[i];
                            bcommentnumber[s] = itemcommentnumber[i];
                            bboard[s] = itemboard[i];
                            s++;
                        }
                    }
                }
                adapter.resetItem();
                int k = 0;
                for(int i = 0; i<100; i++) {
                    if(bhead[i] == null) {
                        k = i;
                        i = 100;
                    }
                }
                if(k >= 1) {
                    for (int t = k - 1; t >= 0; t--) {

                        adapter.addItem(new list(bhead[t], bnickname[t], btime[t], bshowpeople[t], bcommentnumber[t], bimageYes[t], bboard[t]));
                    }
                }
                recyclerView.setAdapter(adapter);
                drawerLayout.closeDrawers();
            }
        });
        allpost = findViewById(R.id.allpost);
        allpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                container2.removeAllViewsInLayout();
                inflater.inflate(R.layout.noitem,container2,true);
                allpost.setTextColor(Color.parseColor("#FF000000"));
                hotpost.setTextColor(Color.parseColor("#60000000"));
                noticepost.setTextColor(Color.parseColor("#60000000"));
                fmnumber = 1;
                adapter.resetItem();
                if (bdnumber == 10) {
                    int k = 0;
                    for (int i = 0; i < 100; i++) {
                        if (itemhead[i] == null) {
                            k = i;
                            i = 100;
                        }
                    }
                    if (k >= 1) {
                        for (int t = k - 1; t >= 0; t--) {

                            adapter.addItem(new list(itemhead[t], itemnickname[t], itemtime[t], itemshowpeople[t], itemcommentnumber[t], itemimageYes[t], itemboard[t]));
                        }
                    }
                    recyclerView.setAdapter(adapter);
                }else{
                    int k = 0;
                    for (int i = 0; i < 100; i++) {
                        if (bhead[i] == null) {
                            k = i;
                            i = 100;
                        }
                    }
                    if (k >= 1) {
                        for (int t = k - 1; t >= 0; t--) {

                            adapter.addItem(new list(bhead[t], bnickname[t], btime[t], bshowpeople[t], bcommentnumber[t], bimageYes[t], bboard[t]));
                        }
                    }
                    recyclerView.setAdapter(adapter);
                }
            }
        });
        hotpost = findViewById(R.id.hotpost);
        hotpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                container2.removeAllViewsInLayout();
                inflater.inflate(R.layout.noitem,container2,true);
                hotpost.setTextColor(Color.parseColor("#FF000000"));
                allpost.setTextColor(Color.parseColor("#60000000"));
                noticepost.setTextColor(Color.parseColor("#60000000"));
                fmnumber = 2;
                if (bdnumber == 10) {
                    int t = 0;
                    for (int i = 0; i < 100; i++) {
                        if (itemnumber[i] == 0) {
                            t = i;
                            i = 100;
                        }
                    }
                    if (t != 0) {
                        hothead = new String[t];
                        hotimageYes = new boolean[t];
                        hotnickname = new String[t];
                        hotshowpeople = new int[t];
                        hottime = new String[t];
                        hotbody = new String[t];
                        hotemail = new String[t];
                        hotnumber = new int[t];
                        hotgoodnumber = new int[t];
                        hotcommentnumber = new int[t];
                        hotboard = new int[t];
                        int[] hottest = new int[t];
                        for (int i = 0; i < hothead.length; i++) {
                            hothead[i] = itemhead[i];
                            hotimageYes[i] = itemimageYes[i];
                            hotnickname[i] = itemnickname[i];
                            hotshowpeople[i] = itemshowpeople[i];
                            hottime[i] = itemtime[i];
                            hotbody[i] = itembody[i];
                            hotemail[i] = itememail[i];
                            hotnumber[i] = itemnumber[i];
                            hotgoodnumber[i] = itemgoodnumber[i];
                            hotcommentnumber[i] = itemcommentnumber[i];
                            hotboard[i] = itemboard[i];
                            //Log.d("추천 수"+i+":",Integer.toString(itemgoodnumber[i]));
                            //Log.d("아이템 번호"+i+":",Integer.toString(itemnumber[i]));
                        }
/////////////////////////////////////////////////////////


                        for (int i = 0; i < hothead.length - 1; i++) {
                            for (int k = i; k < hothead.length - 1; k++) {
                                if (hotgoodnumber[i] > hotgoodnumber[k + 1]) {
                                    String temp = hothead[i];
                                    boolean temp1 = hotimageYes[i];
                                    String temp2 = hotnickname[i];
                                    int temp3 = hotshowpeople[i];
                                    String temp4 = hottime[i];
                                    String temp5 = hotbody[i];
                                    String temp6 = hotemail[i];
                                    int temp7 = hotnumber[i];
                                    int temp8 = hotgoodnumber[i];
                                    int temp9 = hotcommentnumber[i];
                                    int temp10 = hotboard[i];

                                    hothead[i] = hothead[k + 1];
                                    hotimageYes[i] = hotimageYes[k + 1];
                                    hotnickname[i] = hotnickname[k + 1];
                                    hotshowpeople[i] = hotshowpeople[k + 1];
                                    hottime[i] = hottime[k + 1];
                                    hotbody[i] = hotbody[k + 1];
                                    hotemail[i] = hotemail[k + 1];
                                    hotnumber[i] = hotnumber[k + 1];
                                    hotgoodnumber[i] = hotgoodnumber[k + 1];
                                    hotcommentnumber[i] = hotcommentnumber[k + 1];
                                    hotboard[i] = hotboard[k + 1];

                                    hothead[k + 1] = temp;
                                    hotimageYes[k + 1] = temp1;
                                    hotnickname[k + 1] = temp2;
                                    hotshowpeople[k + 1] = temp3;
                                    hottime[k + 1] = temp4;
                                    hotbody[k + 1] = temp5;
                                    hotemail[k + 1] = temp6;
                                    hotnumber[k + 1] = temp7;
                                    hotgoodnumber[k + 1] = temp8;
                                    hotcommentnumber[k + 1] = temp9;
                                    hotboard[k + 1] = temp10;
                                }
                            }
                        }



                        adapter.resetItem();
                        int k = 0;
                        k = hothead.length;
                        if (k >= 1) {
                            for (int a = k - 1; a >= 0; a--) {

                                adapter.addItem(new list(hothead[a], hotnickname[a], hottime[a], hotshowpeople[a], hotcommentnumber[a], hotimageYes[a], hotboard[a]));
                            }
                        }
                        recyclerView.setAdapter(adapter);
                    }
                }else{
                    int t = 0;
                    for (int i = 0; i < 100; i++) {
                        if (bnumber[i] == 0) {
                            t = i;
                            i = 100;
                        }
                    }
                    if (t != 0) {
                        hothead = new String[t];
                        hotimageYes = new boolean[t];
                        hotnickname = new String[t];
                        hotshowpeople = new int[t];
                        hottime = new String[t];
                        hotbody = new String[t];
                        hotemail = new String[t];
                        hotnumber = new int[t];
                        hotgoodnumber = new int[t];
                        hotcommentnumber = new int[t];
                        hotboard = new int[t];
                        for (int i = 0; i < hothead.length; i++) {
                            hothead[i] = bhead[i];
                            hotimageYes[i] = bimageYes[i];
                            hotnickname[i] = bnickname[i];
                            hotshowpeople[i] = bshowpeople[i];
                            hottime[i] = btime[i];
                            hotbody[i] = bbody[i];
                            hotemail[i] = bemail[i];
                            hotnumber[i] = bnumber[i];
                            hotgoodnumber[i] = bgoodnumber[i];
                            hotcommentnumber[i] = bcommentnumber[i];
                            hotboard[i] = bboard[i];
                        }

                        for (int i = 0; i < hothead.length - 1; i++) {
                            for (int k = i; k < hothead.length - 1; k++) {
                                if (hotgoodnumber[i] > hotgoodnumber[k + 1]) {
                                    String temp = hothead[i];
                                    boolean temp1 = hotimageYes[i];
                                    String temp2 = hotnickname[i];
                                    int temp3 = hotshowpeople[i];
                                    String temp4 = hottime[i];
                                    String temp5 = hotbody[i];
                                    String temp6 = hotemail[i];
                                    int temp7 = hotnumber[i];
                                    int temp8 = hotgoodnumber[i];
                                    int temp9 = hotcommentnumber[i];
                                    int temp10 = hotboard[i];

                                    hothead[i] = hothead[k + 1];
                                    hotimageYes[i] = hotimageYes[k + 1];
                                    hotnickname[i] = hotnickname[k + 1];
                                    hotshowpeople[i] = hotshowpeople[k + 1];
                                    hottime[i] = hottime[k + 1];
                                    hotbody[i] = hotbody[k + 1];
                                    hotemail[i] = hotemail[k + 1];
                                    hotnumber[i] = hotnumber[k + 1];
                                    hotgoodnumber[i] = hotgoodnumber[k + 1];
                                    hotcommentnumber[i] = hotcommentnumber[k + 1];
                                    hotboard[i] = hotboard[k + 1];

                                    hothead[k + 1] = temp;
                                    hotimageYes[k + 1] = temp1;
                                    hotnickname[k + 1] = temp2;
                                    hotshowpeople[k + 1] = temp3;
                                    hottime[k + 1] = temp4;
                                    hotbody[k + 1] = temp5;
                                    hotemail[k + 1] = temp6;
                                    hotnumber[k + 1] = temp7;
                                    hotgoodnumber[k + 1] = temp8;
                                    hotcommentnumber[k + 1] = temp9;
                                    hotboard[k + 1] = temp10;
                                }
                            }
                        }
                        adapter.resetItem();
                        int k = 0;
                        k = hothead.length;
                        if (k >= 1) {
                            for (int a = k - 1; a >= 0; a--) {

                                adapter.addItem(new list(hothead[a], hotnickname[a], hottime[a], hotshowpeople[a], hotcommentnumber[a], hotimageYes[a], hotboard[a]));
                            }
                        }
                        recyclerView.setAdapter(adapter);
                    }
                }
            }
        });
        noticepost = findViewById(R.id.noticepost);
        noticepost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bdnumber != 2) {
                    container2.removeAllViewsInLayout();
                    inflater.inflate(R.layout.noitem,container2,true);
                    hotpost.setTextColor(Color.parseColor("#60000000"));
                    allpost.setTextColor(Color.parseColor("#60000000"));
                    noticepost.setTextColor(Color.parseColor("#FF000000"));
                    //noticepost.setPaintFlags(noticepost.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG); //밑줄 추가
                    //noticepost.setPaintFlags(noticepost.getPaintFlags() & ~Paint.UNDERLINE_TEXT_FLAG); //밑줄 제거
                    //mtboard.setText("전체글게시판");
                    fmnumber = 3;

                    nhead = new String[100];
                    nimageYes = new boolean[100];
                    nnickname = new String[100];
                    nshowpeople = new int[100];
                    ntime = new String[100];
                    nbody = new String[100];
                    nemail = new String[100];
                    nnumber = new int[100];
                    ngoodnumber = new int[100];
                    ncommentnumber = new int[100];
                    nboard = new int[100];

                    int s = 0;
                    for (int i = 0; i < 100; i++) {
                        if (itemnumber[i] == 0) {
                            i = 100;
                        } else {
                            if (itemboard[i] == 1) {
                                nhead[s] = itemhead[i];
                                nimageYes[s] = itemimageYes[i];
                                nnickname[s] = itemnickname[i];
                                nshowpeople[s] = itemshowpeople[i];
                                ntime[s] = itemtime[i];
                                nbody[s] = itembody[i];
                                nemail[s] = itememail[i];
                                nnumber[s] = itemnumber[i];
                                ngoodnumber[s] = itemgoodnumber[i];
                                ncommentnumber[s] = itemcommentnumber[i];
                                nboard[s] = itemboard[i];
                                s++;
                            }
                        }
                    }
                    adapter.resetItem();
                    int k = 0;
                    for (int i = 0; i < 100; i++) {
                        if (nhead[i] == null) {
                            k = i;
                            i = 100;
                        }
                    }
                    if (k >= 1) {
                        for (int t = k - 1; t >= 0; t--) {

                            adapter.addItem(new list(nhead[t], nnickname[t], ntime[t], nshowpeople[t], ncommentnumber[t], nimageYes[t], nboard[t]));
                        }
                    }
                    recyclerView.setAdapter(adapter);
                }else{
                    hotpost.setTextColor(Color.parseColor("#60000000"));
                    allpost.setTextColor(Color.parseColor("#60000000"));
                    noticepost.setTextColor(Color.parseColor("#FF000000"));
                    fmnumber = 3;
                    adapter.resetItem();
                    recyclerView.setAdapter(adapter);

                    container2.removeAllViewsInLayout();

                    inflater.inflate(R.layout.search_item,container2,true);
                    SearchView search = container2.findViewById(R.id.newsearch);
                    search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            //Toast.makeText(MainActivity.this, "[검색버튼클릭] 검색어 = "+query, Toast.LENGTH_SHORT).show();
                            adapter2.resetItem();
                            String[] input = new String[1];
                            input[0] = query;
                            new org.techtown.community.MainActivity.Description().execute(input[0]);
                            return true;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            //Toast.makeText(MainActivity.this, "입력하고있는 단어 = "+newText, Toast.LENGTH_SHORT).show();
                            //adapter.resetItem();
                            return true;
                        }
                    });
                    //bdnumber가 2일때 = 사건사고게시판에서 공지사항 눌렀을때
                }
            }
        });
        container2 = findViewById(R.id.containerm);
        mtboard.setText("전체글게시판");
        allpost.setTextColor(Color.parseColor("#FF000000"));
        hotpost.setTextColor(Color.parseColor("#60000000"));
        noticepost.setTextColor(Color.parseColor("#60000000"));

/*
        for(int i = 0; i<100; i++) {
            if(itemhead[i] == null) {
                i = 100;
            }else{
                adapter.addItem(new list(itemhead[i], itemnickname[i], itemtime[i], 0, 0));
            }
        }

 */



        //리사이클러뷰,네비게이션바 find
        /////////////////////////////////////////////////////
        if(login == 0) {
            container = findViewById(R.id.container);
            inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.sidelogout_item, container, true);
            Button loginbutton = container.findViewById(R.id.sidelogin);
            loginbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {//로그인창 이동
                    Intent intentlogin = new Intent(org.techtown.community.MainActivity.this, loginActivity.class);
                    intentlogin.putExtra("emaillist", emaillist);
                    intentlogin.putExtra("passwordlist", passwordlist);
                    intentlogin.putExtra("nicknamelist", nicknamelist);
                    startActivity(intentlogin);

                }
            });
        }else if(login == 1){

            container = findViewById(R.id.container);
            inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.sidelogin_item,container,true);

            TextView sideemail = container.findViewById(R.id.sideemail);
            sideemail.setText(nowemail);
            TextView sidenickname = container.findViewById(R.id.sidenickname);
            sidenickname.setText(nownickname);

            ImageView sideinform = container.findViewById(R.id.sideinform);
            sideinform.setBackground(new ShapeDrawable(new OvalShape()));
            sideinform.setClipToOutline(true);
            restoreprofile();
            if(stringimage != null){
                byte[] decodedByteArray = Base64.decode(stringimage, Base64.NO_WRAP);
                Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);

                sideinform.setImageBitmap(decodedBitmap);
            }
            sideinform.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentinform = new Intent(org.techtown.community.MainActivity.this,informActivity.class);
                    intentinform.putExtra("emaillist",emaillist);
                    intentinform.putExtra("nicknamelist",nicknamelist);
                    intentinform.putExtra("nowemail",nowemail);
                    intentinform.putExtra("nownickname",nownickname);
                    intentinform.putExtra("itemnickname",itemnickname);//닉네임 변경시 최신화하기 위해
                    intentinform.putExtra("itemhead",itemhead);//내가쓴글 리사이클러뷰 아이템
                    intentinform.putExtra("itembody",itembody);
                    intentinform.putExtra("itememail",itememail);
                    intentinform.putExtra("itemnumber",itemnumber);
                    intentinform.putExtra("itemtime",itemtime);
                    intentinform.putExtra("itemshowpeople",itemshowpeople);
                    intentinform.putExtra("itemimageYes",itemimageYes);
                    startActivity(intentinform);
                }
            });
            Button sidelogout = container.findViewById(R.id.sidelogout);
            sidelogout.setOnClickListener(new View.OnClickListener() {//로그아웃버튼 클릭시
                @Override
                public void onClick(View v) {
                    login = 0;
                    nowemail = null;
                    nownickname = null;

                    container.removeAllViewsInLayout();
                    container = findViewById(R.id.container);
                    inflater.inflate(R.layout.sidelogout_item,container,true);
                    Button loginbutton = container.findViewById(R.id.sidelogin);
                    loginbutton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intentlogin = new Intent(org.techtown.community.MainActivity.this, loginActivity.class);
                            intentlogin.putExtra("emaillist",emaillist);
                            intentlogin.putExtra("passwordlist",passwordlist);
                            intentlogin.putExtra("nicknamelist",nicknamelist);
                            startActivity(intentlogin);
                        }
                    });
                }
            });
        }

        //네비게이션바 inflater 초기값 : 로그아웃상태 레이아웃
        /////////////////////////////////////////////////////
        menu = (ImageView)findViewById(R.id.memu);//
        menu.setOnClickListener(new View.OnClickListener() {//메뉴바 클릭시 로그아웃 상태이면 inflater 로그아웃 레이아웃 나타내기
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(drawerView);//네비게이션바 열기
                if(login == 0){//로그아웃 상태일때
                    container.removeAllViewsInLayout();
                    container = findViewById(R.id.container);
                    inflater.inflate(R.layout.sidelogout_item,container,true);
                    Button loginbutton = container.findViewById(R.id.sidelogin);
                    loginbutton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {//로그인창 이동
                            Intent intentlogin = new Intent(org.techtown.community.MainActivity.this, loginActivity.class);
                            intentlogin.putExtra("emaillist", emaillist);
                            intentlogin.putExtra("passwordlist", passwordlist);
                            intentlogin.putExtra("nicknamelist",nicknamelist);
                            startActivity(intentlogin);

                        }
                    });
                }else if(login == 1){
                    container.removeAllViewsInLayout();
                    container = findViewById(R.id.container);
                    inflater.inflate(R.layout.sidelogin_item,container,true);

                    TextView sideemail = container.findViewById(R.id.sideemail);
                    sideemail.setText(nowemail);
                    TextView sidenickname = container.findViewById(R.id.sidenickname);
                    sidenickname.setText(nownickname);

                    ImageView sideinform = container.findViewById(R.id.sideinform);
                    sideinform.setBackground(new ShapeDrawable(new OvalShape()));
                    sideinform.setClipToOutline(true);
                    restoreprofile();
                    if(stringimage != null){
                        byte[] decodedByteArray = Base64.decode(stringimage, Base64.NO_WRAP);
                        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);

                        sideinform.setImageBitmap(decodedBitmap);
                    }
                    sideinform.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intentinform = new Intent(org.techtown.community.MainActivity.this,informActivity.class);
                            intentinform.putExtra("emaillist",emaillist);
                            intentinform.putExtra("nicknamelist",nicknamelist);
                            intentinform.putExtra("nowemail",nowemail);
                            intentinform.putExtra("nownickname",nownickname);
                            intentinform.putExtra("itemnickname",itemnickname);//닉네임 변경시 최신화하기 위해
                            intentinform.putExtra("itemhead",itemhead);//내가쓴글 리사이클러뷰 아이템
                            intentinform.putExtra("itembody",itembody);
                            intentinform.putExtra("itememail",itememail);
                            intentinform.putExtra("itemnumber",itemnumber);
                            intentinform.putExtra("itemtime",itemtime);
                            intentinform.putExtra("itemshowpeople",itemshowpeople);
                            intentinform.putExtra("itemimageYes",itemimageYes);
                            startActivity(intentinform);;
                        }
                    });
                    Button sidelogout = container.findViewById(R.id.sidelogout);
                    sidelogout.setOnClickListener(new View.OnClickListener() {//로그아웃버튼 클릭시
                        @Override
                        public void onClick(View v) {
                            login = 0;
                            nowemail = null;
                            nownickname = null;

                            container.removeAllViewsInLayout();
                            container = findViewById(R.id.container);
                            inflater.inflate(R.layout.sidelogout_item,container,true);
                            Button loginbutton = container.findViewById(R.id.sidelogin);
                            loginbutton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intentlogin = new Intent(org.techtown.community.MainActivity.this, loginActivity.class);
                                    intentlogin.putExtra("emaillist",emaillist);
                                    intentlogin.putExtra("passwordlist",passwordlist);
                                    intentlogin.putExtra("nicknamelist",nicknamelist);
                                    startActivity(intentlogin);
                                }
                            });
                        }
                    });
                }
            }
        });
        //메뉴바 클릭시 1.네비게이션바 열기 2.(조건부 발동)로그아웃 상태이면 로그아웃 레이아웃 나타내기
        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        //////////////////////////////////////////////////////
        exit = (ImageView)findViewById(R.id.sideexit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
            }
        });
        //네비게이션바 나가기
        //////////////////////////////////////////////////////
        notice = findViewById(R.id.notice);//게임
        notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intentnotice = new Intent(org.techtown.community.MainActivity.this, noticeActivity.class);
                //startActivity(intentnotice);
                Intent intent = new Intent(MainActivity.this,gameselectActivity.class);
                startActivity(intent);
            }
        });

        write = findViewById(R.id.write);
        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(login == 1) {//글쓸때 null이 나오면 안됨
                    Intent intentwrite = new Intent(org.techtown.community.MainActivity.this, writeActivity.class);
                    intentwrite.putExtra("itememail",itememail);
                    intentwrite.putExtra("itemnickname",itemnickname);
                    intentwrite.putExtra("nowemail",nowemail);
                    intentwrite.putExtra("nownickname",nownickname);
                    intentwrite.putExtra("itemhead",itemhead);
                    intentwrite.putExtra("itembody",itembody);
                    intentwrite.putExtra("itemtime",itemtime);
                    intentwrite.putExtra("itemnumber",itemnumber);
                    intentwrite.putExtra("itemtime",itemtime);
                    intentwrite.putExtra("itemimageYes",itemimageYes);
                    //intentwrite.putExtra("itemuri",itemuri);
                    //Log.d("아이템 유알아이 : ",itemuri[0]);
                    startActivity(intentwrite);
                }else{
                    Toast.makeText(org.techtown.community.MainActivity.this, "로그인하세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        inform = findViewById(R.id.informcheck);
        inform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(login == 1){//내정보창 이동
                    Intent intentinform = new Intent(org.techtown.community.MainActivity.this,informActivity.class);
                    intentinform.putExtra("emaillist",emaillist);
                    intentinform.putExtra("nicknamelist",nicknamelist);
                    intentinform.putExtra("nowemail",nowemail);
                    intentinform.putExtra("nownickname",nownickname);
                    intentinform.putExtra("itemnickname",itemnickname);//닉네임 변경시 최신화하기 위해
                    intentinform.putExtra("itemhead",itemhead);//내가쓴글 리사이클러뷰 아이템
                    intentinform.putExtra("itembody",itembody);
                    intentinform.putExtra("itememail",itememail);
                    intentinform.putExtra("itemnumber",itemnumber);
                    intentinform.putExtra("itemtime",itemtime);
                    intentinform.putExtra("itemshowpeople",itemshowpeople);
                    intentinform.putExtra("itemimageYes",itemimageYes);
                    startActivity(intentinform);
                }else{//로그인창 이동
                    Intent intentlogin = new Intent(org.techtown.community.MainActivity.this,loginActivity.class);
                    intentlogin.putExtra("emaillist",emaillist);
                    intentlogin.putExtra("passwordlist",passwordlist);
                    intentlogin.putExtra("nicknamelist",nicknamelist);
                    startActivity(intentlogin);
                }
            }
        });

        search = findViewById(R.id.mainsearch);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//inform이랑 같은 인텐트 now넘버뺴고
                Intent intentsearch = new Intent(org.techtown.community.MainActivity.this,searchActivity.class);
                intentsearch.putExtra("nowemail",nowemail);
                intentsearch.putExtra("emaillist",emaillist);
                intentsearch.putExtra("nicknamelist",nicknamelist);
                intentsearch.putExtra("itemnickname",itemnickname);//닉네임 변경시 최신화하기 위해
                intentsearch.putExtra("itemhead",itemhead);//내가쓴글 리사이클러뷰 아이템
                intentsearch.putExtra("itembody",itembody);
                intentsearch.putExtra("itememail",itememail);
                intentsearch.putExtra("itemnumber",itemnumber);
                intentsearch.putExtra("itemtime",itemtime);
                intentsearch.putExtra("itemshowpeople",itemshowpeople);
                intentsearch.putExtra("itemimageYes",itemimageYes);
                startActivity(intentsearch);
            }
        });
        /////////////////////////////////////////////////////////////
        adapter2.setOnItemClickListener(new OnlistItemClickListener5() {
            @Override
            public void onItemClick(listAdapter2.ViewHolder holder, View view, int position) {
                if(bdnumber == 2) {
                    String link = adapter2.getItem(position).getResult_link_list();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                    startActivity(intent);
                }
            }
        });
        /////////////////////////////////////////////////////////////
        adapter.setOnItemClickListener(new OnlistItemClickListener() {
            @Override
            public void onItemClick(listAdapter.ViewHolder holder, View view, int position) {
                if(bdnumber == 10) {
                    if (fmnumber == 1) {
                        list item = adapter.getItem(position);
                        Toast.makeText(getApplicationContext(),"아이템 선택: "+position,Toast.LENGTH_LONG).show();
                        int s = 0;
                        for (int i = 0; i < 100; i++) {
                            if (itemhead[i] == null) {
                                s = i - 1;
                                i = 100;
                            }
                        }
                        //Log.d("postition::",Integer.toString(position));
                        Intent intentitem = new Intent(org.techtown.community.MainActivity.this, postActivity.class);
                        //Log.d("itemhead[]:",itemhead[s - position]);
                        intentitem.putExtra("head", itemhead[s - position]);
                        intentitem.putExtra("body", itembody[s - position]);
                        intentitem.putExtra("nickname", itemnickname[s - position]);
                        intentitem.putExtra("email", itememail[s - position]);
                        intentitem.putExtra("nownumber", itemnumber[s - position]);//nownumber는 어뎁터에서만 intent / post,postinsert 사용
                        intentitem.putExtra("showpeople", itemshowpeople[s - position]);
                        intentitem.putExtra("nowemail", nowemail);
                        intentitem.putExtra("nownickname",nownickname);

                        intentitem.putExtra("itemtime", itemtime);
                        intentitem.putExtra("itemnumber", itemnumber);//글위치 찾는용도
                        intentitem.putExtra("itememail", itememail);//글삭제시 최신화하기 위해
                        intentitem.putExtra("itemnickname", itemnickname);//글삭제시 최신화하기 위해
                        intentitem.putExtra("itemhead", itemhead);//글쓴이 정보창 글보기 용도
                        intentitem.putExtra("itembody", itembody);//글쓴이 정보창 글보기 용도
                        intentitem.putExtra("itemshowpeople", itemshowpeople);
                        intentitem.putExtra("itemimageYes", itemimageYes);
                        Log.d("인텐트 시작전임","ㅈㅈ");
                        startActivity(intentitem);
                        Log.d("인텐트 시작","ㅇㅇ");
                        progressDialog = new ProgressDialog(org.techtown.community.MainActivity.this);
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.setMessage("잠시 기다려 주세요.");
                        progressDialog.show();
                    } else if (fmnumber == 2) {
                        int s = 0;
                        s = hothead.length - 1;
                        //Toast.makeText(getApplicationContext(),"아이템 선택: "+item.getTitle(),Toast.LENGTH_LONG).show();
                        //Toast.makeText(getApplicationContext(), "아이템 선택: " + position, Toast.LENGTH_LONG).show();
                        Intent intentitem = new Intent(org.techtown.community.MainActivity.this, postActivity.class);
                        intentitem.putExtra("head", hothead[s - position]);
                        intentitem.putExtra("body", hotbody[s - position]);
                        intentitem.putExtra("nickname", hotnickname[s - position]);
                        intentitem.putExtra("email", hotemail[s - position]);
                        intentitem.putExtra("nownumber", hotnumber[s - position]);//nownumber는 어뎁터에서만 intent / post,postinsert 사용
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
                        progressDialog = new ProgressDialog(org.techtown.community.MainActivity.this);
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.setMessage("잠시 기다려 주세요.");
                        progressDialog.show();
                    } else if (fmnumber == 3) {
                        list item = adapter.getItem(position);
                        //Toast.makeText(getApplicationContext(),"아이템 선택: "+position,Toast.LENGTH_LONG).show();
                        int s = 0;
                        for (int i = 0; i < 100; i++) {
                            if (nhead[i] == null) {
                                s = i - 1;
                                i = 100;
                            }
                        }
                        //Log.d("postition::",Integer.toString(position));
                        Intent intentitem = new Intent(org.techtown.community.MainActivity.this, postActivity.class);
                        //Log.d("itemhead[]:",itemhead[s - position]);
                        intentitem.putExtra("head", nhead[s - position]);
                        intentitem.putExtra("body", nbody[s - position]);
                        intentitem.putExtra("nickname", nnickname[s - position]);
                        intentitem.putExtra("email", nemail[s - position]);
                        intentitem.putExtra("nownumber", nnumber[s - position]);//nownumber는 어뎁터에서만 intent / post,postinsert 사용
                        intentitem.putExtra("showpeople", nshowpeople[s - position]);
                        intentitem.putExtra("nowemail", nowemail);
                        intentitem.putExtra("nownickname", nownickname);

                        intentitem.putExtra("itemtime", itemtime);
                        intentitem.putExtra("itemnumber", itemnumber);//글위치 찾는용도
                        intentitem.putExtra("itememail", itememail);//글삭제시 최신화하기 위해
                        intentitem.putExtra("itemnickname", itemnickname);//글삭제시 최신화하기 위해
                        intentitem.putExtra("itemhead", itemhead);//글쓴이 정보창 글보기 용도
                        intentitem.putExtra("itembody", itembody);//글쓴이 정보창 글보기 용도
                        intentitem.putExtra("itemshowpeople", itemshowpeople);
                        intentitem.putExtra("itemimageYes", itemimageYes);
                        startActivity(intentitem);
                        progressDialog = new ProgressDialog(org.techtown.community.MainActivity.this);
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.setMessage("잠시 기다려 주세요.");
                        progressDialog.show();
                    }

                }else if(bdnumber != 10){
                    if (fmnumber == 1) {
                        list item = adapter.getItem(position);
                        //Toast.makeText(getApplicationContext(),"아이템 선택: "+position,Toast.LENGTH_LONG).show();
                        int s = 0;
                        for (int i = 0; i < 100; i++) {
                            if (bhead[i] == null) {
                                s = i - 1;
                                i = 100;
                            }
                        }
                        //Log.d("postition::",Integer.toString(position));
                        Intent intentitem = new Intent(org.techtown.community.MainActivity.this, postActivity.class);
                        //Log.d("itemhead[]:",itemhead[s - position]);
                        intentitem.putExtra("head", bhead[s - position]);
                        intentitem.putExtra("body", bbody[s - position]);
                        intentitem.putExtra("nickname", bnickname[s - position]);
                        intentitem.putExtra("email", bemail[s - position]);
                        intentitem.putExtra("nownumber", bnumber[s - position]);//nownumber는 어뎁터에서만 intent / post,postinsert 사용
                        intentitem.putExtra("showpeople", bshowpeople[s - position]);
                        intentitem.putExtra("nowemail", nowemail);
                        intentitem.putExtra("nownickname",nownickname);

                        intentitem.putExtra("itemtime", itemtime);
                        intentitem.putExtra("itemnumber", itemnumber);//글위치 찾는용도
                        intentitem.putExtra("itememail", itememail);//글삭제시 최신화하기 위해
                        intentitem.putExtra("itemnickname", itemnickname);//글삭제시 최신화하기 위해
                        intentitem.putExtra("itemhead", itemhead);//글쓴이 정보창 글보기 용도
                        intentitem.putExtra("itembody", itembody);//글쓴이 정보창 글보기 용도
                        intentitem.putExtra("itemshowpeople", itemshowpeople);
                        intentitem.putExtra("itemimageYes", itemimageYes);
                        startActivity(intentitem);
                        progressDialog = new ProgressDialog(org.techtown.community.MainActivity.this);
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.setMessage("잠시 기다려 주세요.");
                        progressDialog.show();
                    } else if (fmnumber == 2) {
                        int s = 0;
                        s = hothead.length - 1;
                        //Toast.makeText(getApplicationContext(),"아이템 선택: "+item.getTitle(),Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(), "아이템 선택: " + position, Toast.LENGTH_LONG).show();
                        Intent intentitem = new Intent(org.techtown.community.MainActivity.this, postActivity.class);
                        intentitem.putExtra("head", hothead[s - position]);
                        intentitem.putExtra("body", hotbody[s - position]);
                        intentitem.putExtra("nickname", hotnickname[s - position]);
                        intentitem.putExtra("email", hotemail[s - position]);
                        intentitem.putExtra("nownumber", hotnumber[s - position]);//nownumber는 어뎁터에서만 intent / post,postinsert 사용
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
                        progressDialog = new ProgressDialog(org.techtown.community.MainActivity.this);
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.setMessage("잠시 기다려 주세요.");
                        progressDialog.show();
                    } else if (fmnumber == 3) {
                        if (bdnumber != 2) {
                            list item = adapter.getItem(position);
                            //Toast.makeText(getApplicationContext(),"아이템 선택: "+position,Toast.LENGTH_LONG).show();
                            int s = 0;
                            for (int i = 0; i < 100; i++) {
                                if (nhead[i] == null) {
                                    s = i - 1;
                                    i = 100;
                                }
                            }
                            //Log.d("postition::",Integer.toString(position));
                            Intent intentitem = new Intent(org.techtown.community.MainActivity.this, postActivity.class);
                            //Log.d("itemhead[]:",itemhead[s - position]);
                            intentitem.putExtra("head", nhead[s - position]);
                            intentitem.putExtra("body", nbody[s - position]);
                            intentitem.putExtra("nickname", nnickname[s - position]);
                            intentitem.putExtra("email", nemail[s - position]);
                            intentitem.putExtra("nownumber", nnumber[s - position]);//nownumber는 어뎁터에서만 intent / post,postinsert 사용
                            intentitem.putExtra("showpeople", nshowpeople[s - position]);
                            intentitem.putExtra("nowemail", nowemail);
                            intentitem.putExtra("nownickname", nownickname);

                            intentitem.putExtra("itemtime", itemtime);
                            intentitem.putExtra("itemnumber", itemnumber);//글위치 찾는용도
                            intentitem.putExtra("itememail", itememail);//글삭제시 최신화하기 위해
                            intentitem.putExtra("itemnickname", itemnickname);//글삭제시 최신화하기 위해
                            intentitem.putExtra("itemhead", itemhead);//글쓴이 정보창 글보기 용도
                            intentitem.putExtra("itembody", itembody);//글쓴이 정보창 글보기 용도
                            intentitem.putExtra("itemshowpeople", itemshowpeople);
                            intentitem.putExtra("itemimageYes", itemimageYes);
                            startActivity(intentitem);
                            progressDialog = new ProgressDialog(org.techtown.community.MainActivity.this);
                            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            progressDialog.setMessage("잠시 기다려 주세요.");
                            progressDialog.show();
                        }else{

                            //bdnumber가 2일때 사건사고
                        }
                    }
                }
            }
        });

}


    @Override
    protected void onResume() {
        super.onResume();
        //Log.d("resume로그인",Integer.toString(login));

        if(bdnumber != 2) {
            recyclerView.setAdapter(adapter);
        }else{
            recyclerView.setAdapter(adapter2);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("온 스탑","ㅇㅇ");
        try {
            progressDialog.dismiss();
        }catch (Exception e){

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onRestart() {
        super.onRestart();
        //Log.d("restart로그인",Integer.toString(login));
        restoreState();
        //Log.d("메인에서 냐우 닉네임은re??",nownickname);
        if(login == 0){
            //Log.d("로그아웃됨ㅇㅇ","ㄴㅇㄹ");
            container.removeAllViewsInLayout();
            container = findViewById(R.id.container);
            inflater.inflate(R.layout.sidelogout_item,container,true);
            Button loginbutton = container.findViewById(R.id.sidelogin);
            loginbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {//로그인창 이동
                    Intent intentlogin = new Intent(MainActivity.this, loginActivity.class);
                    intentlogin.putExtra("emaillist", emaillist);
                    intentlogin.putExtra("passwordlist", passwordlist);
                    intentlogin.putExtra("nicknamelist",nicknamelist);
                    startActivity(intentlogin);

                }
            });
        }else if(login == 1){

            container = findViewById(R.id.container);
            inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.sidelogin_item,container,true);

            TextView sideemail = container.findViewById(R.id.sideemail);
            sideemail.setText(nowemail);
            TextView sidenickname = container.findViewById(R.id.sidenickname);
            sidenickname.setText(nownickname);

            ImageView sideinform = container.findViewById(R.id.sideinform);
            sideinform.setBackground(new ShapeDrawable(new OvalShape()));
            sideinform.setClipToOutline(true);
            restoreprofile();
            if(stringimage != null){
                byte[] decodedByteArray = Base64.decode(stringimage, Base64.NO_WRAP);
                Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);

                sideinform.setImageBitmap(decodedBitmap);
            }
            sideinform.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentinform = new Intent(MainActivity.this,informActivity.class);
                    intentinform.putExtra("emaillist",emaillist);
                    intentinform.putExtra("nicknamelist",nicknamelist);
                    intentinform.putExtra("nowemail",nowemail);
                    intentinform.putExtra("nownickname",nownickname);
                    intentinform.putExtra("itemnickname",itemnickname);//닉네임 변경시 최신화하기 위해
                    intentinform.putExtra("itemhead",itemhead);//내가쓴글 리사이클러뷰 아이템
                    intentinform.putExtra("itembody",itembody);
                    intentinform.putExtra("itememail",itememail);
                    intentinform.putExtra("itemnumber",itemnumber);
                    intentinform.putExtra("itemtime",itemtime);
                    intentinform.putExtra("itemshowpeople",itemshowpeople);
                    intentinform.putExtra("itemimageYes",itemimageYes);
                    startActivity(intentinform);
                }
            });
            Button sidelogout = container.findViewById(R.id.sidelogout);
            sidelogout.setOnClickListener(new View.OnClickListener() {//로그아웃버튼 클릭시
                @Override
                public void onClick(View v) {
                    login = 0;
                    nowemail = null;
                    nownickname = null;

                    container.removeAllViewsInLayout();
                    container = findViewById(R.id.container);
                    inflater.inflate(R.layout.sidelogout_item,container,true);
                    Button loginbutton = container.findViewById(R.id.sidelogin);
                    loginbutton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intentlogin = new Intent(MainActivity.this, loginActivity.class);
                            intentlogin.putExtra("emaillist",emaillist);
                            intentlogin.putExtra("passwordlist",passwordlist);
                            intentlogin.putExtra("nicknamelist",nicknamelist);
                            startActivity(intentlogin);
                        }
                    });
                }
            });
        }

        adapter.resetItem();//아이템 리셋후 다시 생성
        if(bdnumber == 10) {

            int k = 0;
            if (fmnumber == 1) {
                //int k = 0;
                hotpost.setTextColor(Color.parseColor("#60000000"));
                allpost.setTextColor(Color.parseColor("#FF000000"));
                noticepost.setTextColor(Color.parseColor("#60000000"));
                for (int i = 0; i < 100; i++) {
                    if (itemnumber[i] == 0) {//글쓸때 무조건 null 없게 해야됨
                        k = i;
                        i = 100;
                    }
                }
                if (k >= 1) {
                    for (int t = k - 1; t >= 0; t--) {
                        adapter.addItem(new list(itemhead[t], itemnickname[t], itemtime[t], itemshowpeople[t], itemcommentnumber[t], itemimageYes[t], itemboard[t]));
                    }
                }
                recyclerView.setAdapter(adapter);
            } else if (fmnumber == 2) {
                //int k = 0;
                hotpost.setTextColor(Color.parseColor("#FF000000"));
                allpost.setTextColor(Color.parseColor("#60000000"));
                noticepost.setTextColor(Color.parseColor("#60000000"));
                fitem();
                k = hothead.length;
                if (k >= 1) {
                    for (int a = k - 1; a >= 0; a--) {

                        adapter.addItem(new list(hothead[a], hotnickname[a], hottime[a], hotshowpeople[a], hotcommentnumber[a], hotimageYes[a], hotboard[a]));
                    }
                }
                recyclerView.setAdapter(adapter);
            } else if(fmnumber == 3){
                hotpost.setTextColor(Color.parseColor("#60000000"));
                allpost.setTextColor(Color.parseColor("#60000000"));
                noticepost.setTextColor(Color.parseColor("#FF000000"));
                nitem();
                for(int i = 0; i<100; i++) {
                    if(nhead[i] == null) {
                        k = i;
                        i = 100;
                    }
                }
                if(k >= 1) {
                    for (int t = k - 1; t >= 0; t--) {

                        adapter.addItem(new list(nhead[t], nnickname[t], ntime[t], nshowpeople[t], ncommentnumber[t], nimageYes[t], nboard[t]));
                    }
                }
                recyclerView.setAdapter(adapter);
            }
        }else if(bdnumber != 10){
            int k = 0;
            if (fmnumber == 1) {
                hotpost.setTextColor(Color.parseColor("#60000000"));
                allpost.setTextColor(Color.parseColor("#FF000000"));
                noticepost.setTextColor(Color.parseColor("#60000000"));
                bitem();
                //int k = 0;
                for (int i = 0; i < 100; i++) {
                    if (bnumber[i] == 0) {//글쓸때 무조건 null 없게 해야됨
                        k = i;
                        i = 100;
                    }
                }
                if (k >= 1) {
                    for (int t = k - 1; t >= 0; t--) {
                        adapter.addItem(new list(bhead[t], bnickname[t], btime[t], bshowpeople[t], bcommentnumber[t], bimageYes[t], bboard[t]));
                    }
                }
                recyclerView.setAdapter(adapter);
            } else if (fmnumber == 2) {
                hotpost.setTextColor(Color.parseColor("#FF000000"));
                allpost.setTextColor(Color.parseColor("#60000000"));
                noticepost.setTextColor(Color.parseColor("#60000000"));
                //int k = 0;
                bitem();
                fitem();
                k = hothead.length;
                if (k >= 1) {
                    for (int a = k - 1; a >= 0; a--) {

                        adapter.addItem(new list(hothead[a], hotnickname[a], hottime[a], hotshowpeople[a], hotcommentnumber[a], hotimageYes[a], hotboard[a]));
                    }
                }
                recyclerView.setAdapter(adapter);
            } else if(fmnumber == 3) {
                if (bdnumber != 2) {
                    hotpost.setTextColor(Color.parseColor("#60000000"));
                    allpost.setTextColor(Color.parseColor("#60000000"));
                    noticepost.setTextColor(Color.parseColor("#FF000000"));
                    bitem();
                    nitem();
                    for (int i = 0; i < 100; i++) {
                        if (nhead[i] == null) {
                            k = i;
                            i = 100;
                        }
                    }
                    if (k >= 1) {
                        for (int t = k - 1; t >= 0; t--) {

                            adapter.addItem(new list(nhead[t], nnickname[t], ntime[t], nshowpeople[t], ncommentnumber[t], nimageYes[t], nboard[t]));
                        }
                    }
                    recyclerView.setAdapter(adapter);
                }
            }else{
                hotpost.setTextColor(Color.parseColor("#60000000"));
                allpost.setTextColor(Color.parseColor("#60000000"));
                noticepost.setTextColor(Color.parseColor("#FF000000"));
                adapter.resetItem();
                recyclerView.setAdapter(adapter);
               //bdnumber가 2일때 사건사고
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Log.d("pause로그인",Integer.toString(login));
        //clearState();
        saveSate();
    }


    protected void nitem(){
        nhead = new String[100];
        nimageYes = new boolean[100];
        nnickname = new String[100];
        nshowpeople = new int[100];
        ntime = new String[100];
        nbody = new String[100];
        nemail = new String[100];
        nnumber = new int[100];
        ngoodnumber = new int[100];
        ncommentnumber = new int[100];
        nboard = new int[100];

        int s = 0;
        for(int i = 0; i<100; i++){
            if(itemnumber[i] == 0){
                i = 100;
            }else{
                if(itemboard[i] == 1){
                    nhead[s] = itemhead[i];
                    nimageYes[s] = itemimageYes[i];
                    nnickname[s] = itemnickname[i];
                    nshowpeople[s] = itemshowpeople[i];
                    ntime[s] = itemtime[i];
                    nbody[s] = itembody[i];
                    nemail[s] = itememail[i];
                    nnumber[s] = itemnumber[i];
                    ngoodnumber[s] = itemgoodnumber[i];
                    ncommentnumber[s] = itemcommentnumber[i];
                    nboard[s] = itemboard[i];
                    s++;
                }
            }
        }
    }
    protected void bitem(){
        bhead = new String[100];
        bimageYes = new boolean[100];
        bnickname = new String[100];
        bshowpeople = new int[100];
        btime = new String[100];
        bbody = new String[100];
        bemail = new String[100];
        bnumber = new int[100];
        bgoodnumber = new int[100];
        bcommentnumber = new int[100];
        bboard = new int[100];

        int s = 0;
        if(bdnumber == 0) {
            for (int i = 0; i < 100; i++) {
                if (itemnumber[i] == 0) {
                    i = 100;
                } else {
                    if (itemboard[i] == 0) {
                        bhead[s] = itemhead[i];
                        bimageYes[s] = itemimageYes[i];
                        bnickname[s] = itemnickname[i];
                        bshowpeople[s] = itemshowpeople[i];
                        btime[s] = itemtime[i];
                        bbody[s] = itembody[i];
                        bemail[s] = itememail[i];
                        bnumber[s] = itemnumber[i];
                        bgoodnumber[s] = itemgoodnumber[i];
                        bcommentnumber[s] = itemcommentnumber[i];
                        bboard[s] = itemboard[i];
                        s++;
                    }
                }
            }
        }else if(bdnumber == 1){
            for (int i = 0; i < 100; i++) {
                if (itemnumber[i] == 0) {
                    i = 100;
                } else {
                    if (itemboard[i] == 1) {
                        bhead[s] = itemhead[i];
                        bimageYes[s] = itemimageYes[i];
                        bnickname[s] = itemnickname[i];
                        bshowpeople[s] = itemshowpeople[i];
                        btime[s] = itemtime[i];
                        bbody[s] = itembody[i];
                        bemail[s] = itememail[i];
                        bnumber[s] = itemnumber[i];
                        bgoodnumber[s] = itemgoodnumber[i];
                        bcommentnumber[s] = itemcommentnumber[i];
                        bboard[s] = itemboard[i];
                        s++;
                    }
                }
            }
        }else if(bdnumber == 2){
            for (int i = 0; i < 100; i++) {
                if (itemnumber[i] == 0) {
                    i = 100;
                } else {
                    if (itemboard[i] == 2) {
                        bhead[s] = itemhead[i];
                        bimageYes[s] = itemimageYes[i];
                        bnickname[s] = itemnickname[i];
                        bshowpeople[s] = itemshowpeople[i];
                        btime[s] = itemtime[i];
                        bbody[s] = itembody[i];
                        bemail[s] = itememail[i];
                        bnumber[s] = itemnumber[i];
                        bgoodnumber[s] = itemgoodnumber[i];
                        bcommentnumber[s] = itemcommentnumber[i];
                        bboard[s] = itemboard[i];
                        s++;
                    }
                }
            }
        }else if (bdnumber == 3){
            for (int i = 0; i < 100; i++) {
                if (itemnumber[i] == 0) {
                    i = 100;
                } else {
                    if (itemboard[i] == 3) {
                        bhead[s] = itemhead[i];
                        bimageYes[s] = itemimageYes[i];
                        bnickname[s] = itemnickname[i];
                        bshowpeople[s] = itemshowpeople[i];
                        btime[s] = itemtime[i];
                        bbody[s] = itembody[i];
                        bemail[s] = itememail[i];
                        bnumber[s] = itemnumber[i];
                        bgoodnumber[s] = itemgoodnumber[i];
                        bcommentnumber[s] = itemcommentnumber[i];
                        bboard[s] = itemboard[i];
                        s++;
                    }
                }
            }
        }else if(bdnumber == 4){
            for (int i = 0; i < 100; i++) {
                if (itemnumber[i] == 0) {
                    i = 100;
                } else {
                    if (itemboard[i] == 4) {
                        bhead[s] = itemhead[i];
                        bimageYes[s] = itemimageYes[i];
                        bnickname[s] = itemnickname[i];
                        bshowpeople[s] = itemshowpeople[i];
                        btime[s] = itemtime[i];
                        bbody[s] = itembody[i];
                        bemail[s] = itememail[i];
                        bnumber[s] = itemnumber[i];
                        bgoodnumber[s] = itemgoodnumber[i];
                        bcommentnumber[s] = itemcommentnumber[i];
                        bboard[s] = itemboard[i];
                        s++;
                    }
                }
            }
        }
    }
    protected void fitem(){
        if (bdnumber == 10) {
            int t = 0;
            for (int i = 0; i < 100; i++) {
                if (itemnumber[i] == 0) {
                    t = i;
                    i = 100;
                }
            }
            if (t != 0) {
                hothead = new String[t];
                hotimageYes = new boolean[t];
                hotnickname = new String[t];
                hotshowpeople = new int[t];
                hottime = new String[t];
                hotbody = new String[t];
                hotemail = new String[t];
                hotnumber = new int[t];
                hotgoodnumber = new int[t];
                hotcommentnumber = new int[t];
                hotboard = new int[t];

                for (int i = 0; i < hothead.length; i++) {
                    hothead[i] = itemhead[i];
                    hotimageYes[i] = itemimageYes[i];
                    hotnickname[i] = itemnickname[i];
                    hotshowpeople[i] = itemshowpeople[i];
                    hottime[i] = itemtime[i];
                    hotbody[i] = itembody[i];
                    hotemail[i] = itememail[i];
                    hotnumber[i] = itemnumber[i];
                    hotgoodnumber[i] = itemgoodnumber[i];
                    hotcommentnumber[i] = itemcommentnumber[i];
                    hotboard[i] = itemboard[i];
                }

                for (int i = 0; i < hothead.length - 1; i++) {
                    for (int k = i; k < hothead.length - 1; k++) {
                        if (hotgoodnumber[i] > hotgoodnumber[k + 1]) {
                            String temp = hothead[i];
                            boolean temp1 = hotimageYes[i];
                            String temp2 = hotnickname[i];
                            int temp3 = hotshowpeople[i];
                            String temp4 = hottime[i];
                            String temp5 = hotbody[i];
                            String temp6 = hotemail[i];
                            int temp7 = hotnumber[i];
                            int temp8 = hotgoodnumber[i];
                            int temp9 = hotcommentnumber[i];
                            int temp10 = hotboard[i];

                            hothead[i] = hothead[k + 1];
                            hotimageYes[i] = hotimageYes[k + 1];
                            hotnickname[i] = hotnickname[k + 1];
                            hotshowpeople[i] = hotshowpeople[k + 1];
                            hottime[i] = hottime[k + 1];
                            hotbody[i] = hotbody[k + 1];
                            hotemail[i] = hotemail[k + 1];
                            hotnumber[i] = hotnumber[k + 1];
                            hotgoodnumber[i] = hotgoodnumber[k + 1];
                            hotcommentnumber[i] = hotcommentnumber[k + 1];
                            hotboard[i] = hotboard[k + 1];

                            hothead[k + 1] = temp;
                            hotimageYes[k + 1] = temp1;
                            hotnickname[k + 1] = temp2;
                            hotshowpeople[k + 1] = temp3;
                            hottime[k + 1] = temp4;
                            hotbody[k + 1] = temp5;
                            hotemail[k + 1] = temp6;
                            hotnumber[k + 1] = temp7;
                            hotgoodnumber[k + 1] = temp8;
                            hotcommentnumber[k + 1] = temp9;
                            hotboard[k + 1] = temp10;
                        }
                    }
                }

            }
        }else{
            int t = 0;
            for (int i = 0; i < 100; i++) {
                if (bnumber[i] == 0) {
                    t = i;
                    i = 100;
                }
            }
            if (t != 0) {
                hothead = new String[t];
                hotimageYes = new boolean[t];
                hotnickname = new String[t];
                hotshowpeople = new int[t];
                hottime = new String[t];
                hotbody = new String[t];
                hotemail = new String[t];
                hotnumber = new int[t];
                hotgoodnumber = new int[t];
                hotcommentnumber = new int[t];
                hotboard = new int[t];

                for (int i = 0; i < hothead.length; i++) {
                    hothead[i] = bhead[i];
                    hotimageYes[i] = bimageYes[i];
                    hotnickname[i] = bnickname[i];
                    hotshowpeople[i] = bshowpeople[i];
                    hottime[i] = btime[i];
                    hotbody[i] = bbody[i];
                    hotemail[i] = bemail[i];
                    hotnumber[i] = bnumber[i];
                    hotgoodnumber[i] = bgoodnumber[i];
                    hotcommentnumber[i] = bcommentnumber[i];
                    hotboard[i] = bboard[i];
                }

                for (int i = 0; i < hothead.length - 1; i++) {
                    for (int k = i; k < hothead.length - 1; k++) {
                        if (hotgoodnumber[i] > hotgoodnumber[k + 1]) {
                            String temp = hothead[i];
                            boolean temp1 = hotimageYes[i];
                            String temp2 = hotnickname[i];
                            int temp3 = hotshowpeople[i];
                            String temp4 = hottime[i];
                            String temp5 = hotbody[i];
                            String temp6 = hotemail[i];
                            int temp7 = hotnumber[i];
                            int temp8 = hotgoodnumber[i];
                            int temp9 = hotcommentnumber[i];
                            int temp10 = hotboard[i];

                            hothead[i] = hothead[k + 1];
                            hotimageYes[i] = hotimageYes[k + 1];
                            hotnickname[i] = hotnickname[k + 1];
                            hotshowpeople[i] = hotshowpeople[k + 1];
                            hottime[i] = hottime[k + 1];
                            hotbody[i] = hotbody[k + 1];
                            hotemail[i] = hotemail[k + 1];
                            hotnumber[i] = hotnumber[k + 1];
                            hotgoodnumber[i] = hotgoodnumber[k + 1];
                            hotcommentnumber[i] = hotcommentnumber[k + 1];
                            hotboard[i] = hotboard[k + 1];

                            hothead[k + 1] = temp;
                            hotimageYes[k + 1] = temp1;
                            hotnickname[k + 1] = temp2;
                            hotshowpeople[k + 1] = temp3;
                            hottime[k + 1] = temp4;
                            hotbody[k + 1] = temp5;
                            hotemail[k + 1] = temp6;
                            hotnumber[k + 1] = temp7;
                            hotgoodnumber[k + 1] = temp8;
                            hotcommentnumber[k + 1] = temp9;
                            hotboard[k + 1] = temp10;
                        }
                    }
                }

            }
        }
    }
    protected void saveSate(){
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        for(int i = 0; i<100; i++) {
            if(emaillist[i] == null){
                i = 100;
            }else {
                editor.putString("emaillist" + i, emaillist[i]);
                editor.putString("nicknamelist"+i,nicknamelist[i]);
                editor.putString("passwordlist"+i,passwordlist[i]);
            }
        }
        for(int i = 0; i<100; i++) {
            if(itemnumber[i] == 0){
                i = 100;
            }else {
                editor.putString("itemnickname" + i, itemnickname[i]);
                editor.putString("itememail"+i,itememail[i]);
                editor.putString("itemhead"+i,itemhead[i]);
                editor.putString("itembody"+i,itembody[i]);
                editor.putInt("itemnumber"+i,itemnumber[i]);
                editor.putString("itemtime"+i,itemtime[i]);
                editor.putInt("itemshowpeople"+i, itemshowpeople[i]);
                editor.putBoolean("itemimageYes"+i, itemimageYes[i]);
                editor.putInt("itemboard"+i,itemboard[i]);
                //editor.putString("itemuri"+i,itemuri[i]);
            }
        }
        editor.putInt("login",login);
        editor.putString("nowemail",nowemail);
        editor.putString("nownickname",nownickname);
        editor.commit();
    }

    protected void restoreState(){
        SharedPreferences pref = getSharedPreferences("pref",Activity.MODE_PRIVATE);
        if(pref != null) {
            for (int i = 0; i < 100; i++) {
                if (pref.getString("emaillist" + i, null) == null) {
                    i = 100;
                } else {
                    emaillist[i] = pref.getString("emaillist" + i, null);
                    nicknamelist[i] = pref.getString("nicknamelist" + i, null);
                    passwordlist[i] = pref.getString("passwordlist" + i, null);
                }
            }
            for (int i = 0; i < 100; i++) {
                if (pref.getInt("itemnumber" + i, 0) == 0) {//삭제한 글로인한 맨끝값 초기화
                    itemnumber[i] = 0;
                    itememail[i] = null;
                    itemhead[i] = null;
                    itembody[i] = null;
                    itemnickname[i] = null;
                    itemtime[i] = null;
                    itemshowpeople[i] = 0;
                    itemimageYes[i] = false;
                    itemgoodnumber[i] = 0;
                    itemboard[i] = 0;
                    itemcommentnumber[i] = 0;
                    i = 100;
                } else {
                    itemnickname[i] = pref.getString("itemnickname" + i, null);
                    itememail[i] = pref.getString("itememail" + i, null);
                    itemhead[i] = pref.getString("itemhead" + i, null);
                    itembody[i] = pref.getString("itembody" + i, null);
                    itemnumber[i] = pref.getInt("itemnumber" + i, 0);
                    itemtime[i] = pref.getString("itemtime"+i,null);
                    itemshowpeople[i] = pref.getInt("itemshowpeople"+i,0);
                    itemimageYes[i] = pref.getBoolean("itemimageYes"+i,false);
                    itemgoodnumber[i] = pref.getInt("itemgoodnumber"+i,0);
                    itemboard[i] = pref.getInt("itemboard"+i,0);
                    itemcommentnumber[i] = pref.getInt("itemcommentnumber"+i,0);
                    //itemuri[i] = pref.getString("itemuri"+i,null);
                }
            }
            login = pref.getInt("login", 0);
            nowemail = pref.getString("nowemail",null);
            nownickname = pref.getString("nownickname",null);

        }
        //Log.d("itemnumber[0]re:",Integer.toString(itemnumber[0]));
        //Log.d("itemnumber[1]re:",Integer.toString(itemnumber[1]));
        //Log.d("itemnumber[2]re:",Integer.toString(itemnumber[2]));
        //Log.d("itemnumber[3]re:",Integer.toString(itemnumber[3]));
    }

    protected void clearState(){
        SharedPreferences pref = getSharedPreferences("pref",Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
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



    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(intent.getExtras() != null) {
            //Log.d("newintent : ", "1");
            Bundle bundle = intent.getExtras();

            /////////////////////////////////////////////////////////////
            if(bundle.getInt("loginActivity") == 1){//로그인 액티비티에서 옴
                login = bundle.getInt("login");// 1 = 로그인 0 = 로그아웃
                emaillist = bundle.getStringArray("emaillist");
                passwordlist = bundle.getStringArray("passwordlist");
                nicknamelist = bundle.getStringArray("nicknamelist");
                nowemail = bundle.getString("nowemail");//로그인이 아니면 null
                nownickname = bundle.getString("nownickname");//로그인이 아니면 null
                //Log.d("메인에서 냐우 닉네임은??",nownickname);
                //Log.d("메인에서 냐우 닉네임은??",nownickname);
                //Log.d("메인에서 냐우 닉네임은??",nownickname);
                //******************************************************************
                if(bundle.getInt("login") == 1){//로그인 했을시
                    container.removeAllViewsInLayout();
                    container = findViewById(R.id.container);
                    inflater.inflate(R.layout.sidelogin_item,container,true);

                    TextView sideemail = container.findViewById(R.id.sideemail);
                    sideemail.setText(nowemail);
                    TextView sidenickname = container.findViewById(R.id.sidenickname);
                    sidenickname.setText(nownickname);

                    ImageView sideinform = container.findViewById(R.id.sideinform);
                    sideinform.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intentinform = new Intent(MainActivity.this,informActivity.class);
                            intentinform.putExtra("emaillist",emaillist);
                            intentinform.putExtra("nicknamelist",nicknamelist);
                            intentinform.putExtra("nowemail",nowemail);
                            intentinform.putExtra("nownickname",nownickname);
                            intentinform.putExtra("itemnickname",itemnickname);//닉네임 변경시 최신화하기 위해
                            intentinform.putExtra("itemhead",itemhead);//내가쓴글 리사이클러뷰 아이템
                            intentinform.putExtra("itembody",itembody);
                            intentinform.putExtra("itememail",itememail);
                            intentinform.putExtra("itemnumber",itemnumber);
                            intentinform.putExtra("itemtime",itemtime);
                            intentinform.putExtra("itemshowpeople",itemshowpeople);
                            intentinform.putExtra("itemimageYes",itemimageYes);
                            startActivity(intentinform);
                        }
                    });
                    Button sidelogout = container.findViewById(R.id.sidelogout);
                    sidelogout.setOnClickListener(new View.OnClickListener() {//로그아웃버튼 클릭시
                        @Override
                        public void onClick(View v) {
                            login = 0;
                            nowemail = null;
                            nownickname = null;

                            container.removeAllViewsInLayout();
                            container = findViewById(R.id.container);
                            inflater.inflate(R.layout.sidelogout_item,container,true);
                            Button loginbutton = container.findViewById(R.id.sidelogin);
                            loginbutton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intentlogin = new Intent(MainActivity.this, loginActivity.class);
                                    intentlogin.putExtra("emaillist",emaillist);
                                    intentlogin.putExtra("passwordlist",passwordlist);
                                    intentlogin.putExtra("nicknamelist",nicknamelist);
                                    startActivity(intentlogin);
                                }
                            });
                        }
                    });
                }
                //******************************************************************
            }
            /////////////////////////////////////////////////////////////

            setIntent(intent);
        }
    }

    private class Description extends AsyncTask<String, Void, Void> {
        final String clientId = "WiTxqesSF47AfO2D_h2b";//애플리케이션 클라이언트 아이디값";
        final String clientSecret = "A4VWWzgJ7S";//애플리케이션 클라이언트 시크릿값";
        final int display = 5; // 보여지는 검색결과의 수
        //진행바표시
        private ProgressDialog progressDialog;



        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            //recyclerView.setLayoutManager(layoutManager);
            //진행다일로그 시작
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("잠시 기다려 주세요.");
            progressDialog.show();

        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                String text = URLEncoder.encode(strings[0], "UTF-8"); //검색어";
                String apiURL = "https://openapi.naver.com/v1/search/news.xml?query="+ text + "&display=25&start=1&sort=date"; // 뉴스의 json 결과
                //String apiURL = "https://openapi.naver.com/v1/search/blog.xml?query="+ text; // 블로그의 xml 결과
                URL url = new URL(apiURL);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("X-Naver-Client-Id", clientId);
                con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
                ////////////////////////
                InputStream is =con.getInputStream();
                // DOM  파서 생성
                DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
                DocumentBuilder builder=factory.newDocumentBuilder();
                Document document=builder.parse(is);

                // 최상위 루트태그를 가져온다.
                Element root=document.getDocumentElement();
                // item 태그 객체들을 가져온다.
                NodeList item_list=root.getElementsByTagName("item");
                // 태그 개수만큼 반복한다.
                for(int i=0 ; i<item_list.getLength(); i++){
                    // i 번째 태그 객체를 가져온다.
                    Element item_tag=(Element)item_list.item(i);
                    // item 태그 내의 title 과 link 를 가져온다.
                    NodeList title_list=item_tag.getElementsByTagName("title");
                    NodeList link_list=item_tag.getElementsByTagName("link");
                    NodeList description_list=item_tag.getElementsByTagName("description");
                    Element description_tag = (Element)description_list.item(0);
                    Element title_tag = (Element)title_list.item(0);
                    Element link_tag=(Element)link_list.item(0);

                    String description= Html.fromHtml(description_tag.getTextContent()).toString();
                    String title=Html.fromHtml(title_tag.getTextContent()).toString();
                    String link=Html.fromHtml(link_tag.getTextContent()).toString();
                    adapter2.addItem(new array(title,link,description));
                    //array.result_title_list.add(title);
                    //array.result_link_list.add(link);
                    //array.resutl_description_list.add(description);
                    Log.d("아이템 타이틀 :",title);
                    Log.d("아이템 링크 :",link);
                    Log.d("아이템 디스크립 :",description);

                }
                //recyclerView.setAdapter(adapter);
                /////////////////////////
                    /*
                    int responseCode = con.getResponseCode();
                    BufferedReader br;
                    if(responseCode==200) { // 정상 호출
                        br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    } else {  // 에러 발생
                        br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                    }
                    String inputLine;
                    StringBuffer response = new StringBuffer();
                    while ((inputLine = br.readLine()) != null) {
                        response.append(inputLine);
                    }
                    br.close();
                    //System.out.println(response.toString());
                    Log.d("결ㅐㅐ과",response.toString());

                     */
            } catch (Exception e) {
                Log.d("ㅇㅐㅐㅇ","ㅇㅇ");
                System.out.println(e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //ArraList를 인자로 해서 어답터와 연결한다.

            recyclerView.setAdapter(adapter2);

            progressDialog.dismiss();
        }
    }
}

