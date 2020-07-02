package org.techtown.community;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;


public class BaseSurface extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener, Runnable
{


    private SurfaceHolder holder;
    public static Thread drawThread;
    private boolean surfaceReady = false;
    private boolean drawingActive = false;
    private Paint samplePaint = new Paint();
    private static final int MAX_FRAME_TIME = (int) (1000.0 / 60.0);

    private static final String LOGTAG = "surface";
    private Context context;
    private Bitmap rectangle, triangle, c_rectangle, c_triangle, player, rectangle2, rotate_player;
    private float top, player_h, dy, jump, hurdle_h, die_h;
    private int hurdle_x;
    private int touch, rotate;
    private Paint paint = new Paint();
    private Canvas canvas;
    private ArrayList<Integer> arrayList = new ArrayList<>();
    private int[] hurdle_arr = new int[12];
    private int count, hurdle_kind;

    public BaseSurface(Context context, AttributeSet attrs)
    {//생성자
        super(context, attrs);
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        setOnTouchListener(this);


    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        this.holder = holder;

        if (drawThread != null)
        {
            Log.d(LOGTAG, "draw thread still active..");
            drawingActive = false;
            try
            {
                drawThread.join();
            } catch (InterruptedException e)
            { // do nothing
            }
        }

        rectangle = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.rectangle);
        rectangle = Bitmap.createScaledBitmap(rectangle, 200, 200, false);
        rectangle2 = Bitmap.createScaledBitmap(rectangle, 200, 400, false);
        player = Bitmap.createScaledBitmap(rectangle, 70, 70, false);
        triangle = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.triangle3);
        triangle = Bitmap.createScaledBitmap(triangle, 200, 200, false);
        dy = 0;
        jump = 250;
        player_h = 0;
        for(int i = 0; i<20; i++) {
            addarray();
        }
        for(int i = 0; i<12; i++){
            hurdle_arr[i] = arrayList.get(i);
        }

        surfaceReady = true;
        startDrawThread();
        Log.d(LOGTAG, "Created");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        // Surface is not used anymore - stop the drawing thread
        stopDrawThread();
        // and release the surface
        holder.getSurface().release();

        this.holder = null;
        surfaceReady = false;


        Log.d(LOGTAG, "Destroyed");
    }



    /**
     * Stops the drawing thread
     */
    public void stopDrawThread()
    {
        if (drawThread == null)
        {
            Log.d(LOGTAG, "DrawThread is null");
            return;
        }
        drawingActive = false;
        while (true)
        {
            try
            {
                Log.d(LOGTAG, "Request last frame");
                drawThread.join(5000);
                break;
            } catch (Exception e)
            {
                Log.e(LOGTAG, "Could not join with draw thread");
            }
        }
        drawThread = null;
    }

    /**
     * Creates a new draw thread and starts it.
     */
    public void startDrawThread()
    {
        if (surfaceReady && drawThread == null)
        {
            drawThread = new Thread(this, "Draw thread");
            drawingActive = true;
            drawThread.start();
        }
    }

    @Override
    public void run()
    {
        Log.d(LOGTAG, "Draw thread started");
        long frameStartTime;
        long frameTime;

        while (drawingActive)
        {
            if (holder == null)
            {
                return;
            }

            canvas = holder.lockCanvas();
            try {
                synchronized (holder){
                    canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                    canvas.drawColor(Color.parseColor("#ffffffff"));
                    //canvas.drawBitmap(rectangle2, 1800, 680, null);
                    //canvas.drawBitmap(triangle, 900, 640, null);
                    hurdle();
                    jump();
                }
            } finally {

                holder.unlockCanvasAndPost(canvas);
            }

        }
        Message msg = rungameActivity.han.obtainMessage();
        msg.arg1 = 1;
        rungameActivity.han.sendMessage(msg);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        // Handle touch events
        //Log.d("터지는:",Integer.toString(touch));
        //Log.d("높이는ㅇ0:",Float.toString(player_h));
        float x = event.getX();
        float y = event.getY();

        Log.d("x1:",Float.toString(x));

        Log.d("y1:",Float.toString(y));


        if(touch == 0){
            if(hurdle_h == player_h) {
                dy = 33;
                touch = 1;
            }
        }
        return true;
    }
    public void hurdle(){
        hurdle_x += 10;
        if(hurdle_x >190){
            count++;
            for(int i = 11; i>0; i--){
                hurdle_arr[i] = hurdle_arr[i-1];
            }
            hurdle_arr[0] = arrayList.get(12+count);
            //Log.d("새로운 배열값은:",Integer.toString(hurdle_arr[0]));
            hurdle_x = 0;
        }
        if(hurdle_arr[0] == 0){
            //Log.d("영이다","ㅇㅁㅁㅎ");
        }else if(hurdle_arr[0] == 1){
            canvas.drawBitmap(triangle, 2200 - hurdle_x, 880, null);
        }else if(hurdle_arr[0] == 2){
            canvas.drawBitmap(rectangle, 2200 - hurdle_x, 880, null);
        }else if(hurdle_arr[0] == 3){
            canvas.drawBitmap(rectangle, 2200 - hurdle_x, 880, null);
            canvas.drawBitmap(triangle, 2200 - hurdle_x, 680, null);
        }else{
            canvas.drawBitmap(rectangle, 2200 - hurdle_x, 880, null);
            canvas.drawBitmap(rectangle, 2200 - hurdle_x, 680, null);
        }
        if(hurdle_arr[1] == 0){

        }else if(hurdle_arr[1] == 1){
            canvas.drawBitmap(triangle, 2000 - hurdle_x, 880, null);
        }else if(hurdle_arr[1] == 2){
            canvas.drawBitmap(rectangle, 2000 - hurdle_x, 880, null);
        }else if(hurdle_arr[1] == 3){
            canvas.drawBitmap(rectangle, 2000 - hurdle_x, 880, null);
            canvas.drawBitmap(triangle, 2000 - hurdle_x, 680, null);
        }else{
            canvas.drawBitmap(rectangle, 2000 - hurdle_x, 880, null);
            canvas.drawBitmap(rectangle, 2000 - hurdle_x, 680, null);
        }
        if(hurdle_arr[2] == 0){

        }else if(hurdle_arr[2] == 1){
            canvas.drawBitmap(triangle, 1800 - hurdle_x, 880, null);
        }else if(hurdle_arr[2] == 2){
            canvas.drawBitmap(rectangle, 1800 - hurdle_x, 880, null);
        }else if(hurdle_arr[2] == 3){
            canvas.drawBitmap(rectangle, 1800 - hurdle_x, 880, null);
            canvas.drawBitmap(triangle, 1800 - hurdle_x, 680, null);
        }else{
            canvas.drawBitmap(rectangle, 1800 - hurdle_x, 880, null);
            canvas.drawBitmap(rectangle, 1800 - hurdle_x, 680, null);
        }
        if(hurdle_arr[3] == 0){

        }else if(hurdle_arr[3] == 1){
            canvas.drawBitmap(triangle, 1600 - hurdle_x, 880, null);
        }else if(hurdle_arr[3] == 2){
            canvas.drawBitmap(rectangle, 1600 - hurdle_x, 880, null);
        }else if(hurdle_arr[3] == 3){
            canvas.drawBitmap(rectangle, 1600 - hurdle_x, 880, null);
            canvas.drawBitmap(triangle, 1600 - hurdle_x, 680, null);
        }else{
            canvas.drawBitmap(rectangle, 1600 - hurdle_x, 880, null);
            canvas.drawBitmap(rectangle, 1600 - hurdle_x, 680, null);
        }
        if(hurdle_arr[4] == 0){

        }else if(hurdle_arr[4] == 1){
            canvas.drawBitmap(triangle, 1400 - hurdle_x, 880, null);
        }else if(hurdle_arr[4] == 2){
            canvas.drawBitmap(rectangle, 1400 - hurdle_x, 880, null);
        }else if(hurdle_arr[4] == 3){
            canvas.drawBitmap(rectangle, 1400 - hurdle_x, 880, null);
            canvas.drawBitmap(triangle, 1400 - hurdle_x, 680, null);
        }else{
            canvas.drawBitmap(rectangle, 1400 - hurdle_x, 880, null);
            canvas.drawBitmap(rectangle, 1400 - hurdle_x, 680, null);
        }
        if(hurdle_arr[5] == 0){

        }else if(hurdle_arr[5] == 1){
            canvas.drawBitmap(triangle, 1200 - hurdle_x, 880, null);
        }else if(hurdle_arr[5] == 2){
            canvas.drawBitmap(rectangle, 1200 - hurdle_x, 880, null);
        }else if(hurdle_arr[5] == 3){
            canvas.drawBitmap(rectangle, 1200 - hurdle_x, 880, null);
            canvas.drawBitmap(triangle, 1200 - hurdle_x, 680, null);
        }else{
            canvas.drawBitmap(rectangle, 1200 - hurdle_x, 880, null);
            canvas.drawBitmap(rectangle, 1200 - hurdle_x, 680, null);
        }
        if(hurdle_arr[6] == 0){
            if(hurdle_x>150){
                hurdle_kind = 0;
            }
        }else if(hurdle_arr[6] == 1){
            canvas.drawBitmap(triangle, 1000 - hurdle_x, 880, null);
            if(hurdle_x>150){
                hurdle_kind = 1;
            }
        }else if(hurdle_arr[6] == 2){
            canvas.drawBitmap(rectangle, 1000 - hurdle_x, 880, null);
            if(hurdle_x>150){
                hurdle_kind = 2;
            }
        }else if(hurdle_arr[6] == 3){
            canvas.drawBitmap(rectangle, 1000 - hurdle_x, 880, null);
            canvas.drawBitmap(triangle, 1000 - hurdle_x, 680, null);
            if(hurdle_x>150){
                hurdle_kind = 3;
            }
        }else{
            canvas.drawBitmap(rectangle, 1000 - hurdle_x, 880, null);
            canvas.drawBitmap(rectangle, 1000 - hurdle_x, 680, null);
            if(hurdle_x>150){
                hurdle_kind = 4;
            }
        }
        if(hurdle_arr[7] == 0){
            if(hurdle_x<=150) {
                hurdle_kind = 0;
            }
        }else if(hurdle_arr[7] == 1){
            canvas.drawBitmap(triangle, 800 - hurdle_x, 880, null);
            if(hurdle_x<=150) {
                hurdle_kind = 1;
            }
        }else if(hurdle_arr[7] == 2){
            canvas.drawBitmap(rectangle, 800 - hurdle_x, 880, null);
            if(hurdle_x<=150){
                hurdle_kind = 2;
            }
        }else if(hurdle_arr[7] == 3){
            canvas.drawBitmap(rectangle, 800 - hurdle_x, 880, null);
            canvas.drawBitmap(triangle, 800 - hurdle_x, 680, null);
            if(hurdle_x<=150){
                hurdle_kind = 3;
            }
        }else{
            canvas.drawBitmap(rectangle, 800 - hurdle_x, 880, null);
            canvas.drawBitmap(rectangle, 800 - hurdle_x, 680, null);
            if(hurdle_x<=150){
                hurdle_kind = 4;
            }
        }
        if(hurdle_arr[8] == 0){

        }else if(hurdle_arr[8] == 1){
            canvas.drawBitmap(triangle, 600 - hurdle_x, 880, null);
        }else if(hurdle_arr[8] == 2){
            canvas.drawBitmap(rectangle, 600 - hurdle_x, 880, null);
        }else if(hurdle_arr[8] == 3){
            canvas.drawBitmap(rectangle, 600 - hurdle_x, 880, null);
            canvas.drawBitmap(triangle, 600 - hurdle_x, 680, null);
        }else{
            canvas.drawBitmap(rectangle, 600 - hurdle_x, 880, null);
            canvas.drawBitmap(rectangle, 600 - hurdle_x, 680, null);
        }
        if(hurdle_arr[9] == 0){

        }else if(hurdle_arr[9] == 1){
            canvas.drawBitmap(triangle, 400 - hurdle_x, 880, null);
        }else if(hurdle_arr[9] == 2){
            canvas.drawBitmap(rectangle, 400 - hurdle_x, 880, null);
        }else if(hurdle_arr[9] == 3){
            canvas.drawBitmap(rectangle, 400 - hurdle_x, 880, null);
            canvas.drawBitmap(triangle, 400 - hurdle_x, 680, null);
        }else{
            canvas.drawBitmap(rectangle, 400 - hurdle_x, 880, null);
            canvas.drawBitmap(rectangle, 400 - hurdle_x, 680, null);
        }
        if(hurdle_arr[10] == 0){

        }else if(hurdle_arr[10] == 1){
            canvas.drawBitmap(triangle, 200 - hurdle_x, 880, null);
        }else if(hurdle_arr[10] == 2){
            canvas.drawBitmap(rectangle, 200 - hurdle_x, 880, null);
        }else if(hurdle_arr[10] == 3){
            canvas.drawBitmap(rectangle, 200 - hurdle_x, 880, null);
            canvas.drawBitmap(triangle, 200 - hurdle_x, 680, null);
        }else{
            canvas.drawBitmap(rectangle, 200 - hurdle_x, 880, null);
            canvas.drawBitmap(rectangle, 200 - hurdle_x, 680, null);
        }
        if(hurdle_arr[11] == 0){

        }else if(hurdle_arr[11] == 1){
            canvas.drawBitmap(triangle, 0 - hurdle_x, 880, null);
        }else if(hurdle_arr[11] == 2){
            canvas.drawBitmap(rectangle, 0 - hurdle_x, 880, null);
        }else if(hurdle_arr[11] == 3){
            canvas.drawBitmap(rectangle, 0 - hurdle_x, 880, null);
            canvas.drawBitmap(triangle, 0 - hurdle_x, 680, null);
        }else{
            canvas.drawBitmap(rectangle, 0 - hurdle_x, 880, null);
            canvas.drawBitmap(rectangle, 0 - hurdle_x, 680, null);
        }

        if(hurdle_kind == 0){
            hurdle_h = 0;
            die_h = 0;
        }else if(hurdle_kind == 1){
            hurdle_h = 0;
            die_h = 10;
        }else if(hurdle_kind == 2){
            hurdle_h = 200;
            die_h = 200;
        }else if(hurdle_kind == 3){
            hurdle_h = 200;
            die_h = 210;
        }else{
            hurdle_h = 400;
            die_h = 400;
        }
    }
    public void jump(){
        if(player_h < die_h){
            drawingActive = false;
        }
        //Log.d("게임종료",Float.toString(player_h));
        //Log.d("게임종료허들",Float.toString(hurdle_h));
        if(touch == 1){
            //Log.d("점프ㅁㄹ:",Float.toString(player_h));
            if(dy <= 33){
                dy -= 1;
            }
            if(dy == 10){
                dy = -dy;
            }
            //Log.d("점프:",Float.toString(player_h));
            player_h += dy;
            if(player_h < 0.1 + hurdle_h && player_h> hurdle_h - 25){//플레이어 위치와 장애물 위치가 같을때 점프할수있음
                dy = 0;
                player_h = hurdle_h;
                touch = 0;
                //Log.d("점프:",Float.toString(player_h));
            }else if(player_h < 0.1 + hurdle_h){
                player_h = 0;
                dy = 0;
                touch = 0;
                //Log.d("점프22:",Float.toString(player_h));
            }
            //player_h 플레이어 현재 위치 //hurdle_h 현재 위치 장애물 높이

        }
        if(touch == 0) {// 노 터치
            if (player_h > hurdle_h) {
                if (player_h > 0) {
                    player_h -= 15;
                    //player_h = 0;
                }else{
                    player_h = 0;
                }
                //player_h = 0;
            }
        }
        if(player_h<0){
            player_h = 0;
        }
        rotate++;
        if(touch == 0){
            rotate = 0;
        }
        Matrix rotateMatrix = new Matrix();
        rotateMatrix.postRotate(rotate*2);
        rotate_player = Bitmap.createBitmap(player, 0, 0, player.getWidth(), player.getHeight(),rotateMatrix, false);
        canvas.drawBitmap(rotate_player, 800, 1010-player_h, null);
        //1080
    }

    public void addarray(){
        for(int i = 0; i<15; i++){
            arrayList.add(0);
        }
        arrayList.add(1);
        arrayList.add(0);
        arrayList.add(1);
        arrayList.add(0);
        arrayList.add(1);
        for(int i = 0; i<5; i++){
            arrayList.add(0);
        }
        arrayList.add(2);
        arrayList.add(1);
        arrayList.add(1);
        arrayList.add(2);
        for(int i = 0; i<3; i++){
            arrayList.add(0);
        }
        arrayList.add(2);
        arrayList.add(4);
        arrayList.add(4);
        arrayList.add(1);
        arrayList.add(4);
        arrayList.add(1);
        arrayList.add(4);
        arrayList.add(3);
        arrayList.add(2);

    }
}

