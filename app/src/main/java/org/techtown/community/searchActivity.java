package org.techtown.community;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class searchActivity extends AppCompatActivity {
    private RecyclerView recyclerView;//리사이클러뷰
    private LinearLayoutManager layoutManager;//리사이클러뷰
    private final listAdapter adapter = new listAdapter();//리사이클러뷰
    String[] nicknamelist, emaillist, itemnickname, itemhead, itembody, itememail, itemtime;
    String nowemail;
    int[] itemnumber, itemshowpeople, itemcommentnumber, itemboard;
    boolean[] itemimageYes;
    EditText input;
    ImageView search, back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search2);
        nicknamelist = new String[100];
        emaillist = new String[100];
        itemnickname = new String[100];
        itemhead = new String[100];
        itembody = new String[100];
        itememail = new String[100];
        itemtime = new String[100];
        itemnumber = new int[100];
        itemshowpeople = new int[100];
        itemimageYes = new boolean[100];
        itemcommentnumber = new int[100];
        itemboard = new int[100];

        final Intent intent = getIntent();
        if(intent.getExtras() != null) {
            Bundle bundle = intent.getExtras();
            nowemail = bundle.getString("nowemail");

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
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        if(pref != null){
            for(int i = 0; i<100; i++){
                if(itemhead[i] == null){
                    i = 100;
                }else{
                    //Log.d("아이는 "+i+":",Integer.toString(i));

                    itemcommentnumber[i] = pref.getInt("itemcommentnumber"+i,0);
                    itemboard[i] = pref.getInt("itemboard"+i,0);
                    //Log.d("아이템닉네임 "+i+":",itemnickname[i]);
                }
            }
        }
        recyclerView = findViewById(R.id.recyclerViewsearch);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        input = findViewById(R.id.searchinput);
        input.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == event.KEYCODE_ENTER){
                    adapter.resetItem();
                    int[] index = new int[100];
                    int k = 0;
                    String tinput = input.getText().toString();
                    if(tinput.length()>0) {
                        for (int i = 0; i < 100; i++) {
                            if(itemhead[i] != null) {
                                if (itemhead[i].contains(tinput) == true) {
                                    adapter.addItem(new list(itemhead[i], itemnickname[i], itemtime[i], itemshowpeople[i], itemcommentnumber[i], itemimageYes[i],itemboard[i]));
                                    index[k] = i;
                                }
                                if (itembody[i].contains(tinput) == true) {
                                    if (index[k] != i) {
                                        adapter.addItem(new list(itemhead[i], itemnickname[i], itemtime[i], itemshowpeople[i], itemcommentnumber[i], itemimageYes[i],itemboard[i]));
                                    }
                                }
                                k++;
                            }else{
                                i = 100;
                            }
                        }
                    }
                    recyclerView.setAdapter(adapter);
                    return true;
                }
                return false;
            }
        });

        search = findViewById(R.id.seesearch);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.resetItem();
                int[] index = new int[100];
                int k = 0;
                String tinput = input.getText().toString();
                if(tinput.length()>0) {
                    for (int i = 0; i < 100; i++) {
                        if(itemhead[i] != null) {
                            if (itemhead[i].contains(tinput) == true) {
                                adapter.addItem(new list(itemhead[i], itemnickname[i], itemtime[i], itemshowpeople[i], itemcommentnumber[i], itemimageYes[i],itemboard[i]));
                                index[k] = i;
                            }
                            if (itembody[i].contains(tinput) == true) {
                                if (index[k] != i) {
                                    adapter.addItem(new list(itemhead[i], itemnickname[i], itemtime[i], itemshowpeople[i], itemcommentnumber[i], itemimageYes[i],itemboard[i]));
                                }
                            }
                            k++;
                        }else{
                            i = 100;
                        }
                    }
                }
                recyclerView.setAdapter(adapter);
            }
        });

        back = findViewById(R.id.seeback);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        adapter.setOnItemClickListener(new OnlistItemClickListener() {
            @Override
            public void onItemClick(listAdapter.ViewHolder holder, View view, int position) {
                list item = adapter.getItem(position);
                //Toast.makeText(getApplicationContext(),"아이템 선택: "+position,Toast.LENGTH_LONG).show();
                int s = 0;
                for(int i = 0; i<100; i++){
                    if(itemhead[i] == null){
                        s = i - 1;
                        i = 100;
                    }
                }
                //Log.d("postition::",Integer.toString(position));
                Intent intentitem = new Intent(searchActivity.this,postActivity.class);
                //Log.d("itemhead[]:",itemhead[s - position]);
                intentitem.putExtra("head",itemhead[s - position]);
                intentitem.putExtra("body",itembody[s - position]);
                intentitem.putExtra("nickname",itemnickname[s - position]);
                intentitem.putExtra("email",itememail[s - position]);
                intentitem.putExtra("nownumber",itemnumber[s - position]);//nownumber는 어뎁터에서만 intent / post,postinsert 사용
                intentitem.putExtra("showpeople",itemshowpeople[s - position]);
                intentitem.putExtra("nowemail",nowemail);

                intentitem.putExtra("itemtime",itemtime);
                intentitem.putExtra("itemnumber",itemnumber);//글위치 찾는용도
                intentitem.putExtra("itememail",itememail);//글삭제시 최신화하기 위해
                intentitem.putExtra("itemnickname",itemnickname);//글삭제시 최신화하기 위해
                intentitem.putExtra("itemhead",itemhead);//글쓴이 정보창 글보기 용도
                intentitem.putExtra("itembody",itembody);//글쓴이 정보창 글보기 용도
                intentitem.putExtra("itemshowpeople",itemshowpeople);
                intentitem.putExtra("itemimageYes",itemimageYes);
                startActivity(intentitem);
                finish();
            }
        });
        recyclerView.setAdapter(adapter);
    }
}
