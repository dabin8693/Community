package org.techtown.community;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class recommentwriteActivity extends AppCompatActivity {

    private String timage, tnickname, tbody, ttime, temail, nowemail, nownickname, stringimage, nowtime;
    private String[] cimage, cnickname, cbody, ctime, cemail, emaillist, toemail;
    private int ttype, position, nownumber, commentnumber, itemcommentnumber;
    private int[] ctype;
    private int[] itemnumber;
    private ImageView commentimage;
    private TextView commentnickname, commentbody, commenttime;
    private EditText inputcomment;
    private Button commentup;
    private String[] itememail, itemnickname, itemimage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommentwrite);

        emaillist = new String[100];
        cimage = new String[100];
        cnickname = new String[100];
        cbody = new String[100];
        cemail = new String[100];
        ctime = new String[100];
        ctype = new int[100];
        toemail = new String[100];

        itemnumber = new int[100];
        itememail = new String[100];
        final Intent intent = getIntent();
        if(intent.getExtras() != null) {
            Bundle bundle = intent.getExtras();
            position = bundle.getInt("position");

            tnickname = bundle.getString("cnickname");
            tbody = bundle.getString("cbody");
            ttime = bundle.getString("ctime");
            temail = bundle.getString("cemail");
            ttype = bundle.getInt("ctype");
            nownumber = bundle.getInt("nownumber");
            itememail = bundle.getStringArray("itememail");
            itemnumber = bundle.getIntArray("itemnumber");
            nowemail = bundle.getString("nowemail");
            nownickname = bundle.getString("nownickname");
            toemail = bundle.getStringArray("toemail");
        }
        restoreComment();
        restoreprofile();
        commentimage = findViewById(R.id.commentimage3);
        if(timage != null) {
            byte[] decodedByteArray = Base64.decode(timage, Base64.NO_WRAP);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
            commentimage.setImageBitmap(decodedBitmap);
        }
        commentnickname = findViewById(R.id.commentnickname3);
        commentnickname.setText(tnickname);
        commentbody = findViewById(R.id.commentbody3);
        commentbody.setText(tbody);
        commenttime = findViewById(R.id.commenttime3);
        commenttime.setText(ttime);
        Log.d("도착후 포지션:",Integer.toString(position));
        inputcomment = findViewById(R.id.inputcomment2);
        commentup = findViewById(R.id.commentup2);
        commentup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int k = 0;
                for(int i = 0; i<100; i++){
                    if(cnickname[i] == null){
                        k = i;
                        i = 100;
                    }
                }
                for(int i = k; i>position + 1; i--){
                    cnickname[i] = cnickname[i-1];
                    cimage[i] = cimage[i-1];
                    cbody[i] = cbody[i-1];
                    ctime[i] = ctime[i-1];
                    cemail[i] = cemail[i-1];
                    ctype[i] = ctype[i-1];
                    toemail[i] = toemail[i-1];
                    Log.d("body"+i+":",cbody[i]);
                }
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm");
                nowtime = sdf.format(date);
                cnickname[position+1] = nownickname;
                cimage[position+1] = stringimage;
                //Log.d("스트링이미지저장하기:",stringimage);
                cbody[position+1] = inputcomment.getText().toString();
                ctime[position+1] = nowtime;
                cemail[position+1] = nowemail;
                ctype[position+1] = 1;
                toemail[position+1] = temail;
                Log.d("bodyㅇ"+(position+1)+":",cbody[position+1]);
                SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                for(int a = 0; a<100; a++){
                    if(nownumber == itemnumber[a]){
                        editor.putInt("itemcomment"+a+","+6,commentnumber+1);
                        editor.putInt("itemcommentnumber"+a,itemcommentnumber+1);
                        for(int t = 0; t<100; t++){
                            if(cnickname[t] == null){

                            }else{
                                int num = position + 1;
                                editor.putString("itemcomment"+a+","+0+","+t,cimage[t]);
                                editor.putString("itemcomment"+a+","+1+","+t,cnickname[t]);
                                editor.putString("itemcomment"+a+","+2+","+t,cbody[t]);
                                editor.putString("itemcomment"+a+","+3+","+t,ctime[t]);
                                editor.putInt("itemcomment"+a+","+4+","+t,ctype[t]);
                                editor.putString("itemcomment"+a+","+5+","+t,cemail[t]);
                                //editor.putString("itemcomment"+a+","+7+","+num,temail);
                                editor.putString("itemcomment"+a+","+7+","+t,toemail[t]);
                            }
                        }
                    }
                }
                editor.commit();
                finish();
            }
        });

    }

    protected void restoreComment(){
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        if(pref != null){
            for(int i = 0; i<100; i++){
                if(nownumber == itemnumber[i]){
                    int k = pref.getInt("itemcomment"+i+","+6,0);
                    itemcommentnumber = pref.getInt("itemcommentnumber"+i,0);
                    commentnumber = k;
                    for(int t = 0; t<k; t++){
                        cimage[t] = pref.getString("itemcomment"+i+","+0+","+t,null);
                        cnickname[t] = pref.getString("itemcomment"+i+","+1+","+t,null);
                        cbody[t] = pref.getString("itemcomment"+i+","+2+","+t,null);
                        ctime[t] = pref.getString("itemcomment"+i+","+3+","+t,null);
                        cemail[t] = pref.getString("itemcomment"+i+","+5+","+t,null);
                        ctype[t] = pref.getInt("itemcomment"+i+","+4+","+t,0);
                    }
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
                if(emaillist[i] != null) {
                    if (emaillist[i].equals(nowemail) == true) {
                        stringimage = pref.getString("profile" + i, null);
                        //Log.d("스트링이미지불러오기:",stringimage);
                    }
                }
            }
            timage = pref.getString("tempimage",null);
        }
    }
}
