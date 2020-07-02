package org.techtown.community;


import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatCallback;

import java.io.IOException;



public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    Context mContext;
    SurfaceHolder mHolder;
    RenderingThread mRThread;
    float caw_x;
    int[] flat_x,flat_y,flat_y2, plat_change;
    float caw_y, be_caw_y, caw_dy;
    int start_x, start_y, finish_h, after_h, h;
    private SensorManager mSensorManager;
    private Sensor mAccelometerSensor;
    private RenderingThread renderingThread;
    private SensorEventListener mAccLis;
    int check, check2, number, check3;
    int fall, be_position;
    long start, end;
    Canvas canvas;
    Drawable cow;
    Paint textp, bgp, cloudp;
    boolean bool;
    int t, up, upcheck;
    int fall_dy, h_dy;
    int[] cloude_position_x, cloude_position_y;
    int cowrot, plat_fall_count;
    MediaPlayer mediaPlayer, mediaPlayer_jump;
    String bg_uri, jump_uri;



    public MySurfaceView(Context context) {
        super(context);
        mContext = context;
        Score.mRThread = mRThread;
        Score.mediaPlayer = mediaPlayer;
        Score.mediaPlayer_jump = mediaPlayer_jump;
        mHolder = getHolder();
        mHolder.addCallback(this);
        check = 0;
        check2 = 0;
        number = 0;
        be_caw_y = -1;
        start_x = 630;
        start_y = 2250;
        up = 1;
        upcheck = 1;
        textp = new Paint();
        textp.setAntiAlias(true);
        textp.setTextSize(100.0f);
        textp.setColor(Color.parseColor("#50000000"));
        bgp = new Paint();
        bgp.setColor(Color.parseColor("#aa47C83E"));
        cloudp = new Paint();
        cloudp.setColor(Color.parseColor("#99F6F6F6"));
        //textp.setARGB(255,0,0,0);
        cloude_position_x = new int[]{100, 800};
        cloude_position_y = new int[]{100, 200};
        flat_x = new int[6];
        flat_y = new int[6];
        flat_y2 = new int[]{1, 1, 1, 1, 1, 1};
        plat_change = new int[]{1, 1, 1, 1, 1, 1};
        //mRThread = new MySurfaceView.RenderingThread();
        Score.mRThread = new MySurfaceView.RenderingThread();
        bg_uri ="C:/Users/Lee/AndroidStudioProjects/surfaceex/app/src/main/res/raw/abd_mus2.ogg";
        jump_uri ="C:/Users/Lee/AndroidStudioProjects/surfaceex/app/src/main/res/raw/hijump.ogg";
        //bg_uri ="C:/Users/Lee/eclipse-workspace/Java5-2/src/music/Glorious-Morning-2-.mp3";
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        // Surface가 만들어질 때 호출됨
        //"org.techtown.surfaceex.R.raw.abd_mus2"
        //mRThread.start();
        Score.mRThread.start();
        //Using the Accelometer
        mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        mAccelometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mAccLis = new RenderingThread();
        mSensorManager.registerListener(mAccLis,mAccelometerSensor,SensorManager.SENSOR_DELAY_UI);

        //mediaPlayer = MediaPlayer.create(mContext, abd_mus2);
        //mediaPlayer_jump = MediaPlayer.create(mContext, hijump);
        Score.mediaPlayer = MediaPlayer.create(mContext, R.raw.abd_mus2);
        Score.mediaPlayer_jump = MediaPlayer.create(mContext, R.raw.hijump);
        /*
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(mContext, Uri.parse(bg_uri));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

         */
        //mediaPlayer.start();
        Score.mediaPlayer.start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        // Surface가 변경될 때 호출됨
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        // Surface가 종료될 때 호출됨
        try {
            //mRThread.join();
            Score.mRThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //다이얼로그
        /*
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(end/1000d+"초")
                .setNegativeButton("취소",null);
        AlertDialog dialog = builder.create();
        dialog.show();

         */
    }


    class RenderingThread extends Thread implements SensorEventListener {
        Bitmap img_cow, img_plat, img_cloud, img_bg, img_left_cow, img_right_cow;
        Bitmap img_ice_plat, img_ice_bg, img_desert_plat, img_desert_bg;

        public RenderingThread() {
            //Log.d("RenderingThread", "RenderingThread()");
            img_desert_plat = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.platform_aussie);
            img_desert_bg = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.uluru);
            img_ice_bg = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.mountains);
            img_ice_plat = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ice_platformd1);
            img_cow = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.cowrs);
            img_plat = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.platformd1);
            img_bg = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.intro_h);
            img_cloud = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.cloud);
            //img_left_cow = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.cowrs);
            //img_right_cow = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.cowrs);
            Matrix sideInversion = new Matrix();
            sideInversion.postRotate(30);
            //sideInversion.setScale(-1,1);
            img_right_cow = Bitmap.createBitmap(img_cow, 0, 0, img_cow.getWidth(), img_cow.getHeight(),sideInversion, false);
            Matrix sideInversion2 = new Matrix();
            sideInversion2.setScale(-1,1);
            img_right_cow = Bitmap.createBitmap(img_right_cow, 0, 0, img_right_cow.getWidth(), img_right_cow.getHeight(),sideInversion2, false);
            Matrix rotateMatrix = new Matrix();
            rotateMatrix.postRotate(30);
            img_left_cow = Bitmap.createBitmap(img_cow, 0, 0, img_cow.getWidth(), img_cow.getHeight(),rotateMatrix, false);
            Log.d("dafa","ㅁㅈㅎㅁ");
        }

        public void run() {
            //Log.d("RenderingThread", "run()");
            makeflat();
            bool = true;
            while (bool) {
                //Canvas canvas = null;
                canvas = mHolder.lockCanvas();
                try {
                    synchronized (mHolder) {
                        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                        canvas.drawColor(Color.parseColor("#ffffffff"));
                        //Log.d("x는",Integer.toString((int)caw_x));
                        falling();
                        for (int i = 0; i < 6; i++) {
                            if(plat_change[i] == 1) {
                                Log.d("1번임ㅇㅇㅇ","ㅁㅎㄱ");
                                canvas.drawBitmap(img_plat, flat_x[i], flat_y[i], null);
                            }else if(plat_change[i] == 2){
                                Log.d("2번임ㅇㅇㅇ","ㅁㅎㄱ");
                                canvas.drawBitmap(img_ice_plat, flat_x[i], flat_y[i], null);
                            }else if(plat_change[i] == 3){
                                Log.d("3번임ㅇㅇㅇ","ㅁㅎㄱ");
                                canvas.drawBitmap(img_desert_plat, flat_x[i], flat_y[i], null);
                            }
                        }

                        ////insert_y();
                        makeclude();
                        if(plat_change[5] == 1) {
                            canvas.drawBitmap(img_bg, 100, 1800, bgp);
                        }else if(plat_change[5] == 2){
                            canvas.drawBitmap(img_ice_bg, 100, 1800, bgp);
                        }else if(plat_change[5] == 3){
                            canvas.drawBitmap(img_desert_bg, 350, 2200, bgp);
                        }
                        if(cowrot == 0) {
                            canvas.drawBitmap(img_cow, start_x - caw_x, start_y - caw_y, null);
                        }else if(cowrot == 1){
                            canvas.drawBitmap(img_right_cow, start_x - caw_x, start_y - caw_y, null);
                        }else if(cowrot == 2){
                            canvas.drawBitmap(img_left_cow, start_x - caw_x, start_y - caw_y, null);
                        }
                        //canvas.rotate(30);
                        //cow = mContext.getResources().getDrawable(R.drawable.cowrs);
                        //cow.setBounds(630-30*(int)caw_x,1250+(int)caw_y,580-30*(int)caw_x,1300+(int)caw_y);
                        //cow.setAlpha(255);
                        //cow.draw(canvas);
                    }
                    dojump();
                    if(check2 == 1) {
                        end = System.currentTimeMillis() - start;
                        //System.out.println(end/1000d+"초");
                        canvas.drawText(end/1000d+"초",0,100,textp);
                    }

                    if(bool == false){
                        canvas.drawText(end/1000d+"초",0,100,textp);
                        Score.score = end;
                    }


                    /*
                    try {
                        sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                     */
                } finally {
                    if (canvas == null) return;
                    mHolder.unlockCanvasAndPost(canvas);
                }
            }
        }

        public void makeclude(){
            int check4 = 0;
            if(end/1000d > 5*up && end/1000d < 10*up) {
                //Log.e("t는",Integer.toString(t));
                if(t<99) {
                    t++;
                    check4 = upcheck;
                }
                if(check3 == 0) {
                    if (t == 99) {
                        up += 2;
                        check3 = 1;
                        upcheck += 1;
                    }
                }
                if(t > 9) {
                    cloudp.setColor(Color.parseColor("#" + t + "F6F6F6"));
                }else{
                    cloudp.setColor(Color.parseColor("#0" + t + "F6F6F6"));
                }

                if (check4 % 2 == 1) {
                    canvas.drawBitmap(img_cloud, cloude_position_x[0], cloude_position_y[0], cloudp);
                } else {
                    canvas.drawBitmap(img_cloud, cloude_position_x[1], cloude_position_y[1], cloudp);
                }

            }else{
                if(t > 0){
                    if(t>0){
                        t--;
                        check3 = 0;
                        check4 = upcheck;
                    }
                    if(t < 10){
                        cloudp.setColor(Color.parseColor("#0" + t + "F6F6F6"));
                    }else{
                        cloudp.setColor(Color.parseColor("#" + t + "F6F6F6"));
                    }
                    if(check4%2 == 0) {
                        canvas.drawBitmap(img_cloud, cloude_position_x[0], cloude_position_y[0], cloudp);
                    }else{
                        canvas.drawBitmap(img_cloud, cloude_position_x[1], cloude_position_y[1], cloudp);
                    }
                }
            }
        }

        public void insert_y(){

            if(caw_y<50 && caw_y>0) {
                be_caw_y = caw_y;
                caw_y = 50;
            }
            if(caw_y<100 && caw_y>50){
                be_caw_y = caw_y;
                caw_y = 100;
            }
            if(caw_y<150 && caw_y>100){
                be_caw_y = caw_y;
                caw_y = 150;
            }
            if(caw_y<200 && caw_y>150){
                be_caw_y = caw_y;
                caw_y = 200;
            }
            if(caw_y<250 && caw_y>200){
                be_caw_y = caw_y;
                caw_y = 250;
            }
            if(caw_y<300 && caw_y>250){
                be_caw_y = caw_y;
                caw_y = 300;
            }
            if(caw_y<350 && caw_y>300){
                be_caw_y = caw_y;
                caw_y = 350;
            }
            if(caw_y<400 && caw_y>350){
                be_caw_y = caw_y;
                caw_y = 400;
            }
            if(caw_y<450 && caw_y>400){
                be_caw_y = caw_y;
                caw_y = 450;
            }
            if(caw_y<500 && caw_y>450){
                be_caw_y = caw_y;
                caw_y = 500;
            }
            if(caw_y<550 && caw_y>500){
                be_caw_y = caw_y;
                caw_y = 550;
            }
            if(caw_y<600 && caw_y>550){
                be_caw_y = caw_y;
                caw_y = 600;
            }
            if(caw_y<650 && caw_y>600){
                be_caw_y = caw_y;
                caw_y = 650;
            }
            if(caw_y<700 && caw_y>650){
                be_caw_y = caw_y;
                caw_y = 700;
            }
            if(caw_y<750 && caw_y>700){
                be_caw_y = caw_y;
                caw_y = 750;
            }
            if(caw_y<800 && caw_y>750){
                be_caw_y = caw_y;
                caw_y = 800;
            }
            if(caw_y<850 && caw_y>800){
                be_caw_y = caw_y;
                caw_y = 850;
            }
            if(caw_y<900 && caw_y>850){
                be_caw_y = caw_y;
                caw_y = 900;
            }
            if(caw_y<950 && caw_y>900){
                be_caw_y = caw_y;
                caw_y = 950;
            }
            if(caw_y<1000 && caw_y>950){
                be_caw_y = caw_y;
                caw_y = 1000;
            }
            if(caw_y<1050 && caw_y>1000){
                be_caw_y = caw_y;
                caw_y = 1050;
            }
            if(caw_y<1100 && caw_y>1050){
                be_caw_y = caw_y;
                caw_y = 1100;
            }
            if(caw_y<1150 && caw_y>1100){
                be_caw_y = caw_y;
                caw_y = 1150;
            }
            if(caw_y<1200 && caw_y>1150){
                be_caw_y = caw_y;
                caw_y = 1200;
            }
            if(caw_y<1250 && caw_y>1200){
                be_caw_y = caw_y;
                caw_y = 1250;
            }
            if(caw_y<1300 && caw_y>1250){
                be_caw_y = caw_y;
                caw_y = 1300;
            }
            if(caw_y<1350 && caw_y>1300){
                be_caw_y = caw_y;
                caw_y = 1350;
            }
            if(caw_y<1400 && caw_y>1350){
                be_caw_y = caw_y;
                caw_y = 1400;
            }
            if(caw_y<1450 && caw_y>1400){
                be_caw_y = caw_y;
                caw_y = 1450;
            }
            if(caw_y<1500 && caw_y>1450){
                be_caw_y = caw_y;
                caw_y = 1500;
            }
            if(caw_y<1550 && caw_y>1500){
                be_caw_y = caw_y;
                caw_y = 1550;
            }
            if(caw_y<1600 && caw_y>1550){
                be_caw_y = caw_y;
                caw_y = 1600;
            }
            if(caw_y<1650 && caw_y>1600){
                be_caw_y = caw_y;
                caw_y = 1650;
            }
            if(caw_y<1700 && caw_y>1650){
                be_caw_y = caw_y;
                caw_y = 1700;
            }
            if(caw_y<1750 && caw_y>1700){
                be_caw_y = caw_y;
                caw_y = 1750;
            }
            if(caw_y<1800 && caw_y>1750){
                be_caw_y = caw_y;
                caw_y = 1800;
            }
            if(caw_y<1850 && caw_y>1800){
                be_caw_y = caw_y;
                caw_y = 1850;
            }
            if(caw_y<1900 && caw_y>1850){
                be_caw_y = caw_y;
                caw_y = 1900;
            }
            if(caw_y<1950 && caw_y>1900){
                be_caw_y = caw_y;
                caw_y = 1950;
            }
            if(caw_y<2000 && caw_y>1950){
                be_caw_y = caw_y;
                caw_y = 2000;
            }
            if(caw_y<2050 && caw_y>2000){
                be_caw_y = caw_y;
                caw_y = 2050;
            }
            if(caw_y<2100 && caw_y>2050){
                be_caw_y = caw_y;
                caw_y = 2100;
            }
            if(caw_y<2150 && caw_y>2100){
                be_caw_y = caw_y;
                caw_y = 2150;
            }
            if(caw_y<2200 && caw_y>2150){
                be_caw_y = caw_y;
                caw_y = 2200;
            }
            if(caw_y<2250 && caw_y>2200){
                be_caw_y = caw_y;
                caw_y = 2250;
            }
            if(caw_y<2300 && caw_y>2250){
                be_caw_y = caw_y;
                caw_y = 2200;
            }
            if(caw_y<2350 && caw_y>2300){
                be_caw_y = caw_y;
                caw_y = 2350;
            }
            if(caw_y<2400 && caw_y>2350){
                be_caw_y = caw_y;
                caw_y = 2400;
            }
            if(caw_y<2450 && caw_y>2400){
                be_caw_y = caw_y;
                caw_y = 2450;
            }
            if(caw_y<2500 && caw_y>2450){
                be_caw_y = caw_y;
                caw_y = 2500;
            }
            if(caw_y<2550 && caw_y>2500){
                be_caw_y = caw_y;
                caw_y = 2550;
            }
            if(caw_y<2600 && caw_y>2550){
                be_caw_y = caw_y;
                caw_y = 2600;
            }
            if(caw_y<2650 && caw_y>2600){
                be_caw_y = caw_y;
                caw_y = 2650;
            }
            if(caw_y<2700 && caw_y>2650){
                be_caw_y = caw_y;
                caw_y = 2700;
            }
            if(caw_y<2750 && caw_y>2700){
                be_caw_y = caw_y;
                caw_y = 2750;
            }
            if(caw_y<2800 && caw_y>2750){
                be_caw_y = caw_y;
                caw_y = 2800;
            }
            if(caw_y<2850 && caw_y>2800){
                be_caw_y = caw_y;
                caw_y = 2850;
            }
            if(caw_y<2900 && caw_y>2850){
                be_caw_y = caw_y;
                caw_y = 2900;
            }
            if(caw_y<2950 && caw_y>2900){
                be_caw_y = caw_y;
                caw_y = 2950;
            }
            if(caw_y<3000 && caw_y>2950){
                be_caw_y = caw_y;
                caw_y = 3000;
            }
            if(caw_y<3050 && caw_y>3000){
                be_caw_y = caw_y;
                caw_y = 3050;
            }

        }
        public void makeflat(){
            //int[] rand;
            //int[] arrtemp = new int[5];
            //int count = 0;
            //boolean bool = true;
            //rand = new int[]{0, 400, 800, 1200, 1600, 2000};
            for(int i = 0; i<6; i++) {
                flat_x[i] = ((int) (100 * Math.random() + 1) * 12);
            }
            flat_y = new int[]{0, 400, 800, 1200, 1600, 2000};//2500 부터 안보임 바닥
            /*
            while (bool) {
                int temp = rand[(int) (5 * Math.random())];
                for (int k = 0; k < 5; k++) {
                    if (arrtemp[k] == temp) {
                        count = 1;
                    }
                }
                if (count == 0) {
                    for (int k = 0; k < 5; k++) {
                        if (arrtemp[k] < 100) {
                            arrtemp[k] = temp;
                            k = 10;
                        }
                    }
                }
                count = 0;
                if(arrtemp[4] >100){
                    bool = false;
                }
                //flat_y[i] = 1400;
                //flat_x[i] = 900;
            }
            for(int i = 0; i<5; i++) {
                flat_y[i] = arrtemp[i];
            }

             */
        }

        public void falling(){
            if(fall > 0) {
                for (int i = 0; i < 6; i++) {
                    flat_y[i] += 40;
                    fall_dy += 1;
                }
                //check += 1;
                if (flat_y[5] == 2400) {
                    plat_fall_count += 1;
                    if(plat_fall_count>50){
                        for(int v = 0; v<6; v++){
                            if(plat_change[v] == 2){

                            }else{
                                plat_change[v] = 2;
                                v = 10;
                            }
                        }
                    }else if(plat_fall_count>20){
                        for(int v = 0; v<6; v++){
                            if(plat_change[v] == 3){

                            }else{
                                plat_change[v] = 3;
                                v = 10;
                            }
                        }
                    }else{

                    }
                    number -= 1;
                    fall_dy = 0;
                    if(number == 0) {
                        fall = 0;
                    }
                    //////////////////////////////////
                    for (int i = 5; i > 0; i--) {
                        flat_y2[i] = flat_y2[i - 1];
                    }
                    if(flat_y2[1] == 1){
                        int rand_y = ((int)(Math.random()*2));
                        if(rand_y == 0){
                            flat_y2[0] = 0;
                        }else{
                            flat_y2[0] = 1;
                        }
                    }else{
                        flat_y2[0] = 1;
                    }
                    flat_y = new int[]{0, 400, 800, 1200, 1600, 2000};
                    for(int i = 0; i<6; i++){
                        if(flat_y2[i] == 1){
                            if(i == 0){
                                flat_y[i] = 0;
                            }else {
                                flat_y[i] = i * 400;
                            }
                        }else{
                            flat_y[i] = 4000;
                        }
                    }
                    //check = 0;
                    for (int i = 5; i > 0; i--) {
                        flat_x[i] = flat_x[i - 1];
                    }
                    flat_x[0] = ((int) (100 * Math.random() + 1) * 12);
                }
                ///////////////////////
                if (flat_y[5] == 4400) {
                    plat_fall_count += 1;
                    if(plat_fall_count>50){
                        for(int v = 0; v<6; v++){
                            if(plat_change[v] == 2){

                            }else{
                                plat_change[v] = 2;
                                v = 10;
                            }
                        }
                    }else if(plat_fall_count>20){
                        for(int v = 0; v<6; v++){
                            if(plat_change[v] == 3){

                            }else{
                                plat_change[v] = 3;
                                v = 10;
                            }
                        }
                    }else{

                    }
                    number -= 1;
                    fall_dy = 0;
                    if(number == 0) {
                        fall = 0;
                    }
                    //////////////////////////////////
                    for (int i = 5; i > 0; i--) {
                        flat_y2[i] = flat_y2[i - 1];
                    }
                    if(flat_y2[1] == 1){
                        int rand_y = ((int)(Math.random()*2));
                        if(rand_y == 0){
                            flat_y2[0] = 0;
                        }else{
                            flat_y2[0] = 1;
                        }
                    }else{
                        flat_y2[0] = 1;
                    }
                    flat_y = new int[]{0, 400, 800, 1200, 1600, 2000};
                    for(int i = 0; i<6; i++){
                        if(flat_y2[i] == 1){
                            if(i == 0){
                                flat_y[i] = 0;
                            }else {
                                flat_y[i] = i * 400;
                            }
                        }else{
                            flat_y[i] = 4000;
                        }
                    }
                    //check = 0;
                    for (int i = 5; i > 0; i--) {
                        flat_x[i] = flat_x[i - 1];
                    }
                    flat_x[0] = ((int) (100 * Math.random() + 1) * 12);
                }
            }
        }
        public void dojump(){//2250
            //Log.e("카우y는",Integer.toString((int)caw_y));
            for (int i=0;i<6;i++) {
                if ((caw_x + 200 > (630 - flat_x[i])) && (caw_x - 100 < (630 - flat_x[i]))
                        && (caw_y + 26 > (2350 - flat_y[i])) && ((2350 - flat_y[i]) >= caw_y - 26) && (caw_dy < 0)) {
                    ////caw_dy = 100;
                    //mediaPlayer_jump.start();
                    Score.mediaPlayer_jump.start();
                    if(i == 0) {
                        caw_dy = 3;
                    }else if(i == 1){
                        caw_dy = 4;
                    }else if(i == 2){
                        caw_dy = 5;
                    }else if(i == 3){
                        caw_dy = 30;
                    }else if(i == 4){
                        caw_dy = 40;
                    }else{
                        caw_dy = 52;
                    }
                    caw_y = 2350 - flat_y[i];//보정
                    if(cowrot == 0) {
                        canvas.drawBitmap(img_cow, start_x - caw_x, start_y - caw_y, null);
                    }else if(cowrot == 1){
                        canvas.drawBitmap(img_right_cow, start_x - caw_x, start_y - caw_y, null);
                    }else if(cowrot == 2){
                        canvas.drawBitmap(img_left_cow, start_x - caw_x, start_y - caw_y, null);
                    }
                    //h = flat_y[i]+100;
                    //h = 2250 - flat_y[i];
                    //h = 2400 - flat_y[i];// - (5-i)*150;
                    h = 400;
                    //h_dy = 5-i;
                    //h = 2400 - flat_y[i];
                    if(check2 == 0){
                        //타이머 시작
                        start = System.currentTimeMillis();
                        check2 = 1;
                    }
                    if(i != 5) {
                        //if (be_position != i) {
                        fall = 1;
                        number = 5 - i;
                        //}
                    }
                    be_position = i;
                    //Log.e("도착ㅇㅇㅇㅇㅇㅇㅇㅇㅇ",Integer.toString((int)caw_y));
                }
            }
            ///////////////////////////////
            //종료//
            if(caw_y <= 0){
                if(check2 == 1){
                    check2 = 0;
                    bool = false;
                    //mediaPlayer.stop();
                    //mediaPlayer.release();
                    //mediaPlayer_jump.stop();
                    //mediaPlayer_jump.release();
                    Score.mediaPlayer.stop();
                    Score.mediaPlayer.release();
                    Score.mediaPlayer_jump.stop();
                    Score.mediaPlayer_jump.release();
                    Score.die = 10;
                    Score.score = (end/1000d);
                    //게임종료
                }
            }

            //Log.e("카우x는",Integer.toString((int)caw_x));
            /*
            if(caw_y > 1400){
                for(int i = 0; i<6; i++){
                    flat_y[i] -= 100;
                }
            }
             */
            if(caw_dy > -48) {
                //caw_dy -= 1.8;
                caw_dy -= 1.4;
            /*
                if(h_dy == 0) {
                    caw_dy -= 1.25;
                }else if(h_dy == 1){
                    caw_dy -= 1.7;
                }else if(h_dy == 2){
                    caw_dy -= 2.85;
                }else if(h_dy == 3){
                    caw_dy -= 4;
                }else if(h_dy == 4){
                    caw_dy -= 5;
                }else {
                    caw_dy -= 6;
                }

             */

            }
            caw_y += caw_dy;
            if (caw_y >= 1100 + h)  caw_dy = -caw_dy;//caw_dy =+ 0;
            //if (caw_y >= 900 + h - 40*fall_dy)  caw_dy = -caw_dy;//caw_dy =+ 0;
            if(caw_y <= 0){
                ////caw_dy = 100;
                caw_dy = 52;
                caw_y = 0;
                h = 0;
            }


            //Log.e("카우y는",Integer.toString((int)caw_y));
            /*
            if(start_x - 30*caw_x > 530){
                if(start_x - 30*caw_x < 720){
                    if(start_y-caw_y == 1850) {

                            check = -20;
                            finish_h = 400;
                            after_h = 20;

                    }else{
                        if(start_y-caw_y > 1850) {
                            if (check == 0) {
                                caw_y = 0;
                            }
                        }
                    }
                }
            }
            if(check < 60){
                caw_y += 20;
                check += 1;
            }
            if(check >= 60){
                caw_y -= 20;
                check += 1;
            }
            if(check == 120 + after_h){
                Log.d("카우y는",Integer.toString((int)caw_y));
                check = 0;
                after_h = 0;

            }

             */
            /*
            Log.d("와이는:",Integer.toString((int)caw_y));
            if(check == 0) {
                if (caw_y > -1290) {
                    caw_y -= 30;
                    //canvas.drawBitmap(img_cow, 630-30*caw_x, 2250+caw_y, null);
                } else {
                    check = 1;
                }
            }
            if(check == 1){
                caw_y += 30;
                //canvas.drawBitmap(img_cow, 630-30*caw_x, 2250+caw_y, null);
            }
            if(caw_y > - 10) {
                if (caw_y < 10) {
                    check = 0;
                }
            }

             */

        }


        @Override
        public void onSensorChanged(SensorEvent event) {
            //Log.d("센서x는",Integer.toString((int)caw_x));
            float a = event.values[0];
            if(a > 0.5){
                if(caw_x < 800) {
                    //caw_x += a * 20;//20~160
                    if(a>4.5) {
                        caw_x += a * a * a;//1~500
                    }else if(a>2){
                        caw_x += a * 20;
                    }else{
                        caw_x += a * 10;
                    }
                }else{
                    //caw_x = 630;
                    caw_x -= 1600;
                }
                cowrot = 1;
            }else if(a < -0.5){
                if(caw_x > -800) {
                    //caw_x -= -a * 20;
                    if(a<-4.5) {
                        caw_x -= -(a * a * a);
                    }else if(a<-2){
                        caw_x -= -a * 20;
                    }else{
                        caw_x -= -a * 10;
                    }
                }else{
                    //caw_x = -630;
                    caw_x += 1600;
                }
                cowrot = 2;
            }else{
                cowrot = 0;
            }
            //Log.e("x:",Integer.toString((int)caw_x));
            double accX = event.values[0];
            double accY = event.values[1];
            double accZ = event.values[2];

            double angleXZ = Math.atan2(accX,  accZ) * 180/Math.PI;
            double angleYZ = Math.atan2(accY,  accZ) * 180/Math.PI;
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    } // RenderingThread
}
