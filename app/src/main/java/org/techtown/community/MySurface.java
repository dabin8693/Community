package org.techtown.community;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class MySurface extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener, Runnable {
    private SurfaceHolder holder;
    private Thread thread;
    private boolean surfaceReady, drawing;
    private Canvas canvas;
    private Bitmap goat, beaver, bull, deer, elephant, tiger, whale;
    //private int[] matrix = new int[49];
    private int[][] matrix = new int[8][7];
    private int touch1, touch2, touch_x, touch_y, touch_x2, touch_y2, y_up, y_down, x_left, x_right, change_check, y_up2, y_down2, x_left2, x_right2, touch_x_all, touch_y_all;
    private int x_right_all, x_left_all, y_up_all, y_down_all;
    private long start_time, before_time;
    private Paint paint = new Paint();
    private Paint textpaint = new Paint();
    private int falling_count_check = 0;
    int score = 0;

    public MySurface(Context context, AttributeSet attrs){
        super(context, attrs);
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        setOnTouchListener(this);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        this.holder = holder;
        if (thread != null)
        {

            drawing = false;
            try
            {
                thread.join();
            } catch (InterruptedException e)
            { // do nothing
            }
        }
        for(int i = 0; i<8; i++){
            for(int k = 0; k<7; k++){
                matrix[i][k] = (int)((Math.random()*7)+1);
            }
        }
        touch_x = -1;
        touch_y = -1;
        touch_x2 = -1;
        touch_y2 = -1;
        goat = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.goat);
        goat = Bitmap.createScaledBitmap(goat,250,250,false);
        beaver = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.beaver);
        beaver = Bitmap.createScaledBitmap(beaver,250,250,false);
        bull = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.bull);
        bull = Bitmap.createScaledBitmap(bull,250,250,false);
        deer = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.deer);
        deer = Bitmap.createScaledBitmap(deer,250,250,false);
        elephant = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.elephant);
        elephant = Bitmap.createScaledBitmap(elephant,250,250,false);
        tiger = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.tiger);
        tiger = Bitmap.createScaledBitmap(tiger,250,250,false);
        whale = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.whale);
        whale = Bitmap.createScaledBitmap(whale,250,250,false);
        paint.setColor(Color.parseColor("#501DDB16"));
        textpaint.setColor(Color.parseColor("#bb000000"));
        textpaint.setTextSize(100.0f);
        textpaint.setAntiAlias(true);
        drawing = true;
        surfaceReady = true;
        thread = new Thread(this,"thread");
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        thread_stop();
        holder.getSurface().release();
        this.holder = null;
        surfaceReady = false;
    }

    public void thread_stop(){
        if(thread == null){
            return;
        }
        drawing = false;
        while (true){
            try {
                thread.join(5000);
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        thread = null;
    }

    @Override
    public void run() {
        int dup_check = 1;
        start_time = System.currentTimeMillis();
        while (drawing){
            if(holder == null){
                return;
            }

            canvas = holder.lockCanvas();
            try {
                synchronized (holder){
                    canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                    canvas.drawColor(Color.parseColor("#ffffffff"));

                    int while_count = 0;
                    while(dup_check == 1){//전체 검사
                        int[][] sub_matrix = matrix;
                        for(int i = 0; i<7; i++){
                            for(int k = 0; k<7; k++){
                                touch_x_all = k;
                                touch_y_all = i;
                                dupcheck_xright_all();
                                dupcheck_xleft_all();
                                if(x_left_all + x_right_all > 1){
                                    while (x_left_all != 0) {
                                        matrix[touch_y_all][touch_x_all - x_left_all] = 0;
                                        x_left_all -= 1;
                                    }
                                    while (x_right_all != 0) {
                                        matrix[touch_y_all][touch_x_all + x_right_all] = 0;
                                        x_right_all -= 1;
                                    }
                                    matrix[touch_y_all][touch_x_all] = 0;
                                    i += x_right_all + 1;
                                }
                            }
                        }

                        for(int i = 0; i<7; i++){
                            for(int k = 0; k<7; k++){
                                touch_x_all = i;
                                touch_y_all = k;
                                dupcheck_yup_all();
                                dupcheck_ydown_all();
                                if(y_down_all + y_up_all > 1){
                                    while (y_up_all != 0) {
                                        sub_matrix[touch_y_all + y_up_all][touch_x_all] = 0;
                                        y_up_all -= 1;
                                    }
                                    while (y_down_all != 0) {
                                        sub_matrix[touch_y_all - y_down_all][touch_x_all] = 0;
                                        y_down_all -= 1;
                                    }
                                    matrix[touch_y_all][touch_x_all] = 0;
                                    k += y_up_all + 1;
                                }
                            }
                        }

                        touch_x_all = -1;
                        touch_y_all = -1;
                        x_right_all = 0;
                        x_left_all = 0;
                        y_down_all = 0;
                        y_up_all = 0;

                        for(int i = 0; i<7; i++){
                            for(int k = 0; k<7; k++){
                                if(sub_matrix[i][k] == matrix[i][k]){

                                }else{
                                    if(sub_matrix[i][k] == 0){
                                        matrix[i][k] = 0;
                                    }
                                }
                            }
                        }

                        all_falling();

                        int last_line_count = 0;
                        for (int i = 0; i < 7; i++) {
                            if (matrix[6][i] == 0) {
                                last_line_count++;
                                matrix[6][i] = (int) ((Math.random() * 7) + 1);
                            }
                        }
                        if (last_line_count > 0) {
                            all_falling();
                            falling_count_check++;//다시중복검사
                        }



                        if(falling_count_check == 0) {
                            dup_check = 0;
                            while_count = 0;
                        }
                        if(while_count > 10){

                            falling_count_check = 0;
                            dup_check = 0;
                        }
                        while_count++;
                        falling_count_check = 0;
                    }

                    canvas.drawText(score+"점",0,100,textpaint);
                    draw();//배열값대로 화면 최신화작업
                    change();//자리변경
                    if(touch1>0){//첫터치 이펙트 적용
                        canvas.drawRect(20+touch_x*200,2050-touch_y*200,220+touch_x*200,2250-touch_y*200,paint);
                    }
                    //중복체크
                    if(change_check == 1) {
                        dup_check = 1;//전체중복체크 해야됨
                        int check_count = 0;
                        int check_count2 = 0;
                        Log.d("바꿨습니당ㅇㅇ","dsf");
                        dupcheck_yup();
                        dupcheck_ydown();
                        dupcheck_xleft();
                        dupcheck_xright();
                        Log.d("xleft:",Integer.toString(x_left));
                        Log.d("xright:",Integer.toString(x_right));
                        if (x_left + x_right > 1) {//제거 제거당하는위치 0으로
                            // 1보다 커야되는이유 오른쪽칸+왼쪽칸+터치한자기자신 = 3
                            Log.d("퍼스트x","dsf");
                            int temp_x_left = x_left;
                            while (temp_x_left != 0) {
                                matrix[touch_y][touch_x - temp_x_left] = 0;
                                temp_x_left -= 1;
                            }
                            int temp_x_right = x_right;
                            while (temp_x_right != 0) {
                                matrix[touch_y][touch_x + temp_x_right] = 0;
                                temp_x_right -= 1;
                            }

                            check_count++;
                        }
                        if (y_down + y_up > 1) {//제거 제거당하는위치 0으로
                            Log.d("퍼스트y","dsf");
                            int temp_y_up = y_up;
                            while (temp_y_up != 0) {
                                matrix[touch_y + temp_y_up][touch_x] = 0;
                                temp_y_up -= 1;
                            }
                            int temp_y_down = y_down;
                            while (temp_y_down != 0) {
                                matrix[touch_y - temp_y_down][touch_x] = 0;
                                temp_y_down -= 1;
                            }

                            check_count++;
                        }

////////////////////////////////////////////////////////////////

                        dupcheck_y2up();
                        dupcheck_y2down();
                        dupcheck_x2left();
                        dupcheck_x2right();

                        if (x_left2 + x_right2 > 1) {//제거 제거당하는위치 0으로
                            int temp_x_left2 = x_left2;
                            while (temp_x_left2 != 0) {
                                matrix[touch_y2][touch_x2 - temp_x_left2] = 0;
                                temp_x_left2 -= 1;
                            }
                            int temp_x_right2 = x_right2;
                            while (temp_x_right2 != 0) {
                                matrix[touch_y2][touch_x2 + temp_x_right2] = 0;
                                temp_x_right2 -= 1;
                            }

                            check_count2++;
                        }
                        if (y_down2 + y_up2 > 1) {//제거 제거당하는위치 0으로
                            int temp_y_up2 = y_up2;
                            while (temp_y_up2 != 0) {
                                matrix[touch_y2 + temp_y_up2][touch_x2] = 0;
                                temp_y_up2 -= 1;
                            }
                            int temp_y_down2 = y_down2;
                            while (temp_y_down2 != 0) {
                                matrix[touch_y2 - temp_y_down2][touch_x2] = 0;
                                temp_y_down2 -= 1;
                            }

                            check_count2++;
                        }

                        if(check_count > 0){
                            matrix[touch_y][touch_x] = 0;
                        }
                        if(check_count2 > 0){
                            matrix[touch_y2][touch_x2] = 0;
                        }

                        falling_leftright();
                        falling_updown();

                        y_up = 0;
                        y_down = 0;
                        x_right = 0;
                        x_left = 0;
                        y_up2 = 0;
                        y_down2 = 0;
                        x_right2 = 0;
                        x_left2 = 0;

                        touch_x = -1;//터치했던 좌표 초기화
                        touch_y = -1;
                        touch_x2 = -1;
                        touch_y2 = -1;

                        change_check = 0;// 중복검사 다시못하게 터치로 서로 자리를 바꾸면 그때 다시 중복검사 할 수 있음

                    }
                }
            }finally {
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }

    public void all_falling(){
        for(int i = 0; i<7; i++){
            for(int k = 0; k<7; k++){
                if(matrix[k][i] == 0){
                    for(int t = k; t<7; t++) {
                        if (t < 6) {
                            matrix[t][i] = matrix[t + 1][i];
                        }
                        if(k <6 ) {//k가 6이면 한칸씩 안내렸는데도 불구하고 새로운 값을 부여하기때문
                            if (t == 6) {
                                score += 100;
                                matrix[t][i] = (int) ((Math.random() * 7) + 1);
                            }
                        }
                    }
                    falling_count_check++;
                }
            }
        }
    }

    public void dupcheck_xright_all(){
        if(touch_x_all == 6){
            x_right_all = 0;
        }else if(touch_x == 5){
            if(matrix[touch_y_all][touch_x_all] == matrix[touch_y_all][touch_x_all + 1]){
                x_right_all = 1;
            }else{
                x_right_all = 0;
            }
        }else if(touch_x_all == 4) {
            if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all][touch_x_all + 1]) {
                if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all][touch_x_all + 2]) {
                    x_right_all = 2;
                } else {
                    x_right_all = 1;
                }
            } else {
                x_right_all = 0;
            }
        }else if(touch_x_all == 3){
            if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all][touch_x_all + 1]) {
                if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all][touch_x_all + 2]) {
                    if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all][touch_x_all + 3]){
                        x_right_all = 3;
                    }else{
                        x_right_all = 2;
                    }
                } else {
                    x_right_all = 1;
                }
            } else {
                x_right_all = 0;
            }
        }else if(touch_x_all == 2){
            if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all][touch_x_all + 1]) {
                if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all][touch_x_all + 2]) {
                    if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all][touch_x_all + 3]){
                        if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all][touch_x_all + 4]){
                            x_right_all = 4;
                        }else{
                            x_right_all = 3;
                        }
                    }else{
                        x_right_all = 2;
                    }
                } else {
                    x_right_all = 1;
                }
            } else {
                x_right_all = 0;
            }
        }else if(touch_x_all == 1){
            if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all][touch_x_all + 1]) {
                if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all][touch_x_all + 2]) {
                    if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all][touch_x_all + 3]){
                        if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all][touch_x_all + 4]){
                            if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all][touch_x_all + 5]){
                                x_right_all = 5;
                            }else{
                                x_right_all = 4;
                            }
                        }else{
                            x_right_all = 3;
                        }
                    }else{
                        x_right_all = 2;
                    }
                } else {
                    x_right_all = 1;
                }
            } else {
                x_right_all = 0;
            }
        }else if(touch_x_all == 0){
            if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all][touch_x_all + 1]) {
                if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all][touch_x_all + 2]) {
                    if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all][touch_x_all + 3]){
                        if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all][touch_x_all + 4]){
                            if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all][touch_x_all + 5]){
                                if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all][touch_x_all + 6]){
                                    x_right_all = 6;
                                }else{
                                    x_right_all = 5;
                                }
                            }else{
                                x_right_all = 4;
                            }
                        }else{
                            x_right_all = 3;
                        }
                    }else{
                        x_right_all = 2;
                    }
                } else {
                    x_right_all = 1;
                }
            } else {
                x_right_all = 0;
            }
        }
    }

    public void dupcheck_xleft_all(){
        if(touch_x_all == 0){
            x_left_all = 0;
        }else if(touch_x_all == 1){
            if(matrix[touch_y_all][touch_x_all] == matrix[touch_y_all][touch_x_all - 1]){
                x_left_all = 1;
            }else{
                x_left_all = 0;
            }
        }else if(touch_x_all == 2) {
            if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all][touch_x_all - 1]) {
                if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all][touch_x_all - 2]) {
                    x_left_all = 2;
                } else {
                    x_left_all = 1;
                }
            } else {
                x_left_all = 0;
            }
        }else if(touch_x_all == 3){
            if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all][touch_x_all - 1]) {
                if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all][touch_x_all - 2]) {
                    if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all][touch_x_all - 3]){
                        x_left_all = 3;
                    }else{
                        x_left_all = 2;
                    }
                } else {
                    x_left_all = 1;
                }
            } else {
                x_left_all = 0;
            }
        }else if(touch_x_all == 4){
            if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all][touch_x_all - 1]) {
                if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all][touch_x_all - 2]) {
                    if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all][touch_x_all - 3]){
                        if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all][touch_x_all - 4]){
                            x_left_all = 4;
                        }else{
                            x_left_all = 3;
                        }
                    }else{
                        x_left_all = 2;
                    }
                } else {
                    x_left_all = 1;
                }
            } else {
                x_left_all = 0;
            }
        }else if(touch_x_all == 5){
            if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all][touch_x_all - 1]) {
                if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all][touch_x_all - 2]) {
                    if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all][touch_x_all - 3]){
                        if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all][touch_x_all - 4]){
                            if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all][touch_x_all - 5]){
                                x_left_all = 5;
                            }else{
                                x_left_all = 4;
                            }
                        }else{
                            x_left_all = 3;
                        }
                    }else{
                        x_left_all = 2;
                    }
                } else {
                    x_left_all = 1;
                }
            } else {
                x_left_all = 0;
            }
        }else if(touch_x_all == 6){
            if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all][touch_x_all - 1]) {
                if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all][touch_x_all - 2]) {
                    if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all][touch_x_all - 3]){
                        if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all][touch_x_all - 4]){
                            if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all][touch_x_all - 5]){
                                if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all][touch_x_all - 6]){
                                    x_left_all = 6;
                                }else{
                                    x_left_all = 5;
                                }
                            }else{
                                x_left_all = 4;
                            }
                        }else{
                            x_left_all = 3;
                        }
                    }else{
                        x_left_all = 2;
                    }
                } else {
                    x_left_all = 1;
                }
            } else {
                x_left_all = 0;
            }
        }
    }

    public void dupcheck_yup_all(){
        if(touch_y_all == 6){
            y_up_all = 0;
        }else if(touch_y_all == 5){
            if(matrix[touch_y_all][touch_x_all] == matrix[touch_y_all + 1][touch_x_all]){
                y_up_all = 1;
            }else{
                y_up_all = 0;
            }
        }else if(touch_y_all == 4) {
            if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all + 1][touch_x_all]) {
                if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all + 2][touch_x_all]) {
                    y_up_all = 2;
                } else {
                    y_up_all = 1;
                }
            } else {
                y_up_all = 0;
            }
        }else if(touch_y_all == 3){
            if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all + 1][touch_x_all]) {
                if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all + 2][touch_x_all]) {
                    if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all + 3][touch_x_all]){
                        y_up_all = 3;
                    }else{
                        y_up_all = 2;
                    }
                } else {
                    y_up_all = 1;
                }
            } else {
                y_up_all = 0;
            }
        }else if(touch_y_all == 2){
            if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all + 1][touch_x_all]) {
                if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all + 2][touch_x_all]) {
                    if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all + 3][touch_x_all]){
                        if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all + 4][touch_x_all]){
                            y_up_all = 4;
                        }else{
                            y_up_all = 3;
                        }
                    }else{
                        y_up_all = 2;
                    }
                } else {
                    y_up_all = 1;
                }
            } else {
                y_up_all = 0;
            }
        }else if(touch_y_all == 1){
            if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all + 1][touch_x_all]) {
                if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all + 2][touch_x_all]) {
                    if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all + 3][touch_x_all]){
                        if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all + 4][touch_x_all]){
                            if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all + 5][touch_x_all]){
                                y_up_all = 5;
                            }else{
                                y_up_all = 4;
                            }
                        }else{
                            y_up_all = 3;
                        }
                    }else{
                        y_up_all = 2;
                    }
                } else {
                    y_up_all = 1;
                }
            } else {
                y_up_all = 0;
            }
        }else if(touch_y_all == 0){
            if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all + 1][touch_x_all]) {
                if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all + 2][touch_x_all]) {
                    if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all + 3][touch_x_all]){
                        if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all + 4][touch_x_all]){
                            if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all + 5][touch_x_all]){
                                if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all + 6][touch_x_all]){
                                    y_up_all = 6;
                                }else{
                                    y_up_all = 5;
                                }
                            }else{
                                y_up_all = 4;
                            }
                        }else{
                            y_up_all = 3;
                        }
                    }else{
                        y_up_all = 2;
                    }
                } else {
                    y_up_all = 1;
                }
            } else {
                y_up_all = 0;
            }
        }
    }

    public void dupcheck_ydown_all(){
        if(touch_y_all == 0){
            y_down_all = 0;
        }else if(touch_y_all == 1){
            if(matrix[touch_y_all][touch_x_all] == matrix[touch_y_all-1][touch_x_all]){
                y_down_all = 1;
            }else{
                y_down_all = 0;
            }
        }else if(touch_y_all == 2) {
            if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all - 1][touch_x_all]) {
                if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all - 2][touch_x_all]) {
                    y_down_all = 2;
                } else {
                    y_down_all = 1;
                }
            } else {
                y_down_all = 0;
            }
        }else if(touch_y_all == 3){
            if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all - 1][touch_x_all]) {
                if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all - 2][touch_x_all]) {
                    if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all - 3][touch_x_all]){
                        y_down_all = 3;
                    }else{
                        y_down_all = 2;
                    }
                } else {
                    y_down_all = 1;
                }
            } else {
                y_down_all = 0;
            }
        }else if(touch_y_all == 4){
            if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all - 1][touch_x_all]) {
                if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all - 2][touch_x_all]) {
                    if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all - 3][touch_x_all]){
                        if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all - 4][touch_x_all]){
                            y_down_all = 4;
                        }else{
                            y_down_all = 3;
                        }
                    }else{
                        y_down_all = 2;
                    }
                } else {
                    y_down_all = 1;
                }
            } else {
                y_down_all = 0;
            }
        }else if(touch_y_all == 5){
            if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all - 1][touch_x_all]) {
                if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all - 2][touch_x_all]) {
                    if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all - 3][touch_x_all]){
                        if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all - 4][touch_x_all]){
                            if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all - 5][touch_x_all]){
                                y_down_all = 5;
                            }else{
                                y_down_all = 4;
                            }
                        }else{
                            y_down_all = 3;
                        }
                    }else{
                        y_down_all = 2;
                    }
                } else {
                    y_down_all = 1;
                }
            } else {
                y_down_all = 0;
            }
        }else if(touch_y_all == 6){
            if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all - 1][touch_x_all]) {
                if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all - 2][touch_x_all]) {
                    if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all - 3][touch_x_all]){
                        if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all - 4][touch_x_all]){
                            if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all - 5][touch_x_all]){
                                if (matrix[touch_y_all][touch_x_all] == matrix[touch_y_all - 6][touch_x_all]){
                                    y_down_all = 6;
                                }else{
                                    y_down_all = 5;
                                }
                            }else{
                                y_down_all = 4;
                            }
                        }else{
                            y_down_all = 3;
                        }
                    }else{
                        y_down_all = 2;
                    }
                } else {
                    y_down_all = 1;
                }
            } else {
                y_down_all = 0;
            }
        }

    }

    public void falling_leftright(){

        if(touch_y > touch_y2) {//첫번째터치가 두번째터치보다 y가 클경우 첫번째터치후 두번째터치 작업
            if (x_left + x_right > 1) {//가로 중복제거된부분만 통과(첫번째터치)

                int b = 0;
                while (x_left - b != 0) {//가로 왼쪽부분
                    if (matrix[touch_y][touch_x - (x_left - b)] == 0) {//왼쪽 끝부터 시작
                        int a = 0;
                        if(touch_y == 6){//맨윗줄은 와일에 못들어가서 따로 해야됨
                            matrix[touch_y + a][touch_x - (x_left - b)] = (int) ((Math.random() * 7) + 1);// 맨위에는 비니깐 새로운 동물 배치
                            score += 100;
                        }
                        while (touch_y + a != 6) {//위에서 한칸씩 내려오는 와일문
                            matrix[touch_y + a][touch_x - (x_left - b)] = matrix[touch_y + a + 1][touch_x - (x_left - b)];
                            a++;
                            if (touch_y + a == 6) {
                                Log.d("새로운동물배치1",Integer.toString(matrix[touch_y + a][touch_x - (x_left - b)]));
                                matrix[touch_y + a][touch_x - (x_left - b)] = (int) ((Math.random() * 7) + 1);// 맨위에는 비니깐 새로운 동물 배치
                                score += 100;
                                Log.d("새로운동물배치2",Integer.toString(matrix[touch_y + a][touch_x - (x_left - b)]));
                            }
                        }
                    }
                    b++;
                }
            }

            if (x_left + x_right > 1) {//가로 중복제거된부분만 통과(첫번째터치)

                int b = 0;
                while (x_right - b != 0) {//가로 오른쪽부분
                    if (matrix[touch_y][touch_x + (x_right - b)] == 0) {//오른쪽 끝부터 시작
                        int a = 0;
                        if(touch_y == 6){//맨윗줄은 와일에 못들어가서 따로 해야됨
                            matrix[touch_y + a][touch_x + (x_right - b)] = (int) ((Math.random() * 7) + 1);// 맨위에는 비니깐 새로운 동물 배치
                            score += 100;
                        }
                        while (touch_y + a != 6) {//위에서 한칸씩 내려오는 와일문
                            matrix[touch_y + a][touch_x + (x_right - b)] = matrix[touch_y + a + 1][touch_x + (x_right - b)];
                            a++;
                            if (touch_y + a == 6) {
                                Log.d("새로운동물배치1",Integer.toString(matrix[touch_y + a][touch_x + (x_right - b)]));
                                matrix[touch_y + a][touch_x + (x_right - b)] = (int) ((Math.random() * 7) + 1);// 맨위에는 비니깐 새로운 동물 배치
                                score += 100;
                                Log.d("새로운동물배치2",Integer.toString(matrix[touch_y + a][touch_x + (x_right - b)]));
                            }
                        }
                    }
                    b++;
                }
            }

            if(x_left + x_right > 1){//중앙 제거(첫번째터치)
                int a = 0;
                if(touch_y == 6){
                    matrix[touch_y + a][touch_x] = (int) ((Math.random() * 7) + 1);// 맨위에는 비니깐 새로운 동물 배치
                    score += 100;
                }
                while(touch_y + a != 6){
                    matrix[touch_y + a][touch_x] = matrix[touch_y + a + 1][touch_x];
                    a++;
                    if(touch_y + a == 6){
                        matrix[touch_y + a][touch_x] = (int) ((Math.random() * 7) + 1);// 맨위에는 비니깐 새로운 동물 배치
                        score += 100;
                    }
                }
            }

            if (x_left2 + x_right2 > 1) {//가로 중복제거된부분만 통과(두번째터치)

                int b = 0;
                while (x_left2 - b != 0) {//가로 왼쪽부분
                    Log.d("x위치는while111ㅇㄻ:",Integer.toString(x_left2 - b));
                    Log.d("x위치는111ㅇㄻ:",Integer.toString(touch_x2 - x_left2 - b));
                    if (matrix[touch_y2][touch_x2 - (x_left2 - b)] == 0) {//왼쪽 끝부터 시작
                        int a = 0;
                        if (touch_y2 == 6) {
                            matrix[touch_y2 + a][touch_x2 - (x_left2 - b)] = (int) ((Math.random() * 7) + 1);// 맨위에는 비니깐 새로운 동물 배치
                            score += 100;
                        }
                        while (touch_y2 + a != 6) {//위에서 한칸씩 내려오는 와일문
                            matrix[touch_y2 + a][touch_x2 - (x_left2 - b)] = matrix[touch_y2 + a + 1][touch_x2 - (x_left2 - b)];
                            a++;
                            if (touch_y2 + a == 6) {
                                matrix[touch_y2 + a][touch_x2 - (x_left2 - b)] = (int) ((Math.random() * 7) + 1);// 맨위에는 비니깐 새로운 동물 배치
                                score += 100;
                            }
                        }
                    }
                    b++;
                }
            }

            if (x_left2 + x_right2 > 1) {//가로 중복제거된부분만 통과(두번째터치)

                int b = 0;
                while (x_right2 - b != 0) {//가로 오른쪽부분
                    if (matrix[touch_y2][touch_x2 + (x_right2 - b)] == 0) {//오른쪽 끝부터 시작
                        int a = 0;
                        if (touch_y2 == 6) {
                            matrix[touch_y2 + a][touch_x2 + (x_right2 - b)] = (int) ((Math.random() * 7) + 1);// 맨위에는 비니깐 새로운 동물 배치
                            score += 100;
                        }
                        while (touch_y2 + a != 6) {//위에서 한칸씩 내려오는 와일문
                            matrix[touch_y2 + a][touch_x2 + (x_right2 - b)] = matrix[touch_y2 + a + 1][touch_x2 + (x_right2 - b)];
                            a++;
                            if (touch_y2 + a == 6) {
                                matrix[touch_y2 + a][touch_x2 + (x_right2 - b)] = (int) ((Math.random() * 7) + 1);// 맨위에는 비니깐 새로운 동물 배치
                                score += 100;
                            }
                        }
                    }
                    b++;
                }
            }

            if(x_left2 + x_right2 > 1){//중앙 제거(두번째터치)
                int a = 0;
                if(touch_y2 == 6){
                    matrix[touch_y2 + a][touch_x2] = (int) ((Math.random() * 7) + 1);// 맨위에는 비니깐 새로운 동물 배치
                    score += 100;
                }
                while(touch_y2 + a != 6){
                    matrix[touch_y2 + a][touch_x2] = matrix[touch_y2 + a + 1][touch_x2];
                    a++;
                    if(touch_y2 + a == 6){
                        matrix[touch_y2 + a][touch_x2] = (int) ((Math.random() * 7) + 1);// 맨위에는 비니깐 새로운 동물 배치
                        score += 100;
                    }
                }
            }

        }else{//두번째터치작업후 첫번째 터치 작업
            if (x_left2 + x_right2 > 1) {//가로 중복제거된부분만 통과(두번째터치)

                int b = 0;
                while (x_left2 - b != 0) {//가로 왼쪽부분
                    if (matrix[touch_y2][touch_x2 - (x_left2 - b)] == 0) {//왼쪽 끝부터 시작
                        int a = 0;
                        if (touch_y2 == 6) {
                            matrix[touch_y2 + a][touch_x2 - (x_left2 - b)] = (int) ((Math.random() * 7) + 1);// 맨위에는 비니깐 새로운 동물 배치
                            score += 100;
                        }
                        while (touch_y2 + a != 6) {//위에서 한칸씩 내려오는 와일문
                            matrix[touch_y2 + a][touch_x2 - (x_left2 - b)] = matrix[touch_y2 + a + 1][touch_x2 - (x_left2 - b)];
                            a++;
                            if (touch_y2 + a == 6) {
                                matrix[touch_y2 + a][touch_x2 - (x_left2 - b)] = (int) ((Math.random() * 7) + 1);// 맨위에는 비니깐 새로운 동물 배치
                                score += 100;
                            }
                        }
                    }
                    b++;
                }
            }

            if (x_left2 + x_right2 > 1) {//가로 중복제거된부분만 통과(두번째터치)

                int b = 0;
                while (x_right2 - b != 0) {//가로 오른쪽부분
                    if (matrix[touch_y2][touch_x2 + (x_right2 - b)] == 0) {//오른쪽 끝부터 시작
                        int a = 0;
                        if (touch_y2 == 6) {
                            matrix[touch_y2 + a][touch_x2 + (x_right2 - b)] = (int) ((Math.random() * 7) + 1);// 맨위에는 비니깐 새로운 동물 배치
                            score += 100;
                        }
                        while (touch_y2 + a != 6) {//위에서 한칸씩 내려오는 와일문
                            matrix[touch_y2 + a][touch_x2 + (x_right2 - b)] = matrix[touch_y2 + a + 1][touch_x2 + (x_right2 - b)];
                            a++;
                            if (touch_y2 + a == 6) {
                                matrix[touch_y2 + a][touch_x2 + (x_right2 - b)] = (int) ((Math.random() * 7) + 1);// 맨위에는 비니깐 새로운 동물 배치
                                score += 100;
                            }
                        }
                    }
                    b++;
                }
            }

            if(x_left2 + x_right2 > 1){//중앙 제거(두번째터치)
                int a = 0;
                if(touch_y2 == 6){
                    matrix[touch_y2 + a][touch_x2] = (int) ((Math.random() * 7) + 1);// 맨위에는 비니깐 새로운 동물 배치
                    score += 100;
                }
                while(touch_y2 + a != 6){
                    matrix[touch_y2 + a][touch_x2] = matrix[touch_y2 + a + 1][touch_x2];
                    a++;
                    if(touch_y2 + a == 6){
                        matrix[touch_y2 + a][touch_x2] = (int) ((Math.random() * 7) + 1);// 맨위에는 비니깐 새로운 동물 배치
                        score += 100;
                    }
                }
            }

            if (x_left + x_right > 1) {//가로 중복제거된부분만 통과(첫번째터치)

                int b = 0;
                while (x_left - b != 0) {//가로 왼쪽부분
                    if (matrix[touch_y][touch_x - (x_left - b)] == 0) {//왼쪽 끝부터 시작
                        int a = 0;
                        if (touch_y == 6) {
                            matrix[touch_y + a][touch_x - (x_left - b)] = (int) ((Math.random() * 7) + 1);// 맨위에는 비니깐 새로운 동물 배치
                            score += 100;
                        }
                        while (touch_y + a != 6) {//위에서 한칸씩 내려오는 와일문
                            matrix[touch_y + a][touch_x - (x_left - b)] = matrix[touch_y + a + 1][touch_x - (x_left - b)];
                            a++;
                            if (touch_y + a == 6) {
                                matrix[touch_y + a][touch_x - (x_left - b)] = (int) ((Math.random() * 7) + 1);// 맨위에는 비니깐 새로운 동물 배치
                                score += 100;
                            }
                        }
                    }
                    b++;
                }
            }

            if (x_left + x_right > 1) {//가로 중복제거된부분만 통과(첫번째터치)

                int b = 0;
                while (x_right - b != 0) {//가로 오른쪽부분
                    Log.d("x위치는while222ㅇㄻ:",Integer.toString(x_right - b));
                    Log.d("x위치는222ㅇㄻ:",Integer.toString(touch_x - (x_right - b)));
                    if (matrix[touch_y][touch_x + (x_right - b)] == 0) {//오른쪽 끝부터 시작
                        int a = 0;
                        if (touch_y == 6) {
                            matrix[touch_y + a][touch_x + (x_right - b)] = (int) ((Math.random() * 7) + 1);// 맨위에는 비니깐 새로운 동물 배치
                            score += 100;
                        }
                        while (touch_y + a != 6) {//위에서 한칸씩 내려오는 와일문
                            matrix[touch_y + a][touch_x + (x_right - b)] = matrix[touch_y + a + 1][touch_x + (x_right - b)];
                            a++;
                            if (touch_y + a == 6) {
                                matrix[touch_y + a][touch_x + (x_right - b)] = (int) ((Math.random() * 7) + 1);// 맨위에는 비니깐 새로운 동물 배치
                                score += 100;
                            }
                        }
                    }
                    b++;
                }
            }

            if(x_left + x_right > 1){//중앙 제거(첫번째터치)
                int a = 0;
                if(touch_y == 6){
                    matrix[touch_y + a][touch_x] = (int) ((Math.random() * 7) + 1);// 맨위에는 비니깐 새로운 동물 배치
                    score += 100;
                }
                while(touch_y + a != 6){
                    matrix[touch_y + a][touch_x] = matrix[touch_y + a + 1][touch_x];
                    a++;
                    if(touch_y + a == 6){
                        matrix[touch_y + a][touch_x] = (int) ((Math.random() * 7) + 1);// 맨위에는 비니깐 새로운 동물 배치
                        score += 100;
                    }
                }
            }

        }

    }

    public void falling_updown(){
        //여기오기전에 터치한 위치가 지워져야됨
        Log.d("업다운 진입","ㅇㅇㅇ");
        if(touch_y2 > touch_y){
            Log.d("y2>y","ㅇㅇㅇ");
            if(y_down + y_up > 1){//y가 y2부터 작으니 y값부터 순차적으로 올라 검사해야됨
                Log.d("y_down + y_up > 1","ㅇㅇㅇ");
                int check_count = 1;
                while(check_count > 0) {
                    check_count = 0;
                    int a = 0;
                    while (touch_y - y_down + a != 7) {

                        if (matrix[touch_y - y_down + a][touch_x] == 0) {//0인구간 찾기
                            int b = a;
                            while (touch_y - y_down + b != 7) {//0인구간부터 위에서 한칸씩 내리기
                                if(matrix[touch_y - y_down + b + 1][touch_x] == 0){
                                    check_count++;
                                }
                                matrix[touch_y - y_down + b][touch_x] = matrix[touch_y - y_down + b + 1][touch_x];
                                b++;
                                if (touch_y - y_down + b == 7) {
                                    matrix[touch_y - y_down + b][touch_x] = (int) ((Math.random() * 7) + 1);
                                    score += 100;
                                }
                            }
                        }
                        a++;
                    }
                }
            }

            if(y_down2 + y_up2 > 1){
                Log.d("y_down2 + y_up2 > 1","ㅇㅇㅇ");
                int check_count = 1;
                while(check_count > 0) {
                    check_count = 0;
                    int a = 0;
                    while (touch_y2 - y_down2 + a != 7) {
                        //Log.d("첫번쨰와일임", "ㅇㅇㅇ");
                        //Log.d("touch_y2 - y_down2:", Integer.toString(touch_y2 - y_down2));
                        //Log.d("touch_y2 - y_down2 + a:", Integer.toString(touch_y2 - y_down2 + a));
                        //Log.d("touch_y2", Integer.toString(touch_y2));
                        if (matrix[touch_y2 - y_down2 + a][touch_x2] == 0) {//0인구간 찾기
                            int b = a;
                            while (touch_y2 - y_down2 + b != 7) {//0인구간부터 위에서 한칸씩 내리기
                                //Log.d("두번쨰와일임", "ㅇㅇㅇ");
                                //Log.d("matrix" + Integer.toString(touch_y2 - y_down2 + b) + ":", Integer.toString(matrix[touch_y - y_down + b][touch_x2]));
                                if(matrix[touch_y2 - y_down2 + b + 1][touch_x2] == 0){
                                    check_count++;
                                }
                                matrix[touch_y2 - y_down2 + b][touch_x2] = matrix[touch_y2 - y_down2 + b + 1][touch_x2];
                                //Log.d("matrix" + Integer.toString(touch_y2 - y_down2 + b) + ":", Integer.toString(matrix[touch_y2 - y_down2 + b][touch_x2]));
                                //matrix[touch_y - y_down + a + 1][touch_x] = 0;

                                b++;
                                if (touch_y2 - y_down2 + b == 7) {
                                    matrix[touch_y2 - y_down2 + b][touch_x2] = (int) ((Math.random() * 7) + 1);
                                    score += 100;
                                }
                            }
                        }
                        a++;
                    }
                }
            }

        }else{//위치가 같거나 다를경우, 위치가 같으면 y를 먼저 검사하나 y2를 먼저 검사하나 상관없음
            Log.d("y2<=y","ㅇㅇㅇ");
            if(y_down2 + y_up2 > 1){//y2가 y부터 작으니 y2값부터 순차적으로 올라 검사해야됨
                Log.d("y_down2 + y_up2 > 1","ㅇㅇㅇ");
                int check_count = 1;
                while(check_count > 0) {
                    check_count = 0;
                    int a = 0;
                    while (touch_y2 - y_down2 + a != 7) {
                        //Log.d("첫번쨰와일임", "ㅇㅇㅇ");
                        //Log.d("touch_y2 - y_down2:", Integer.toString(touch_y2 - y_down2));
                        //Log.d("touch_y2 - y_down2 + a:", Integer.toString(touch_y2 - y_down2 + a));
                        //Log.d("touch_y2", Integer.toString(touch_y2));
                        if (matrix[touch_y2 - y_down2 + a][touch_x2] == 0) {//0인구간 찾기
                            int b = a;
                            while (touch_y2 - y_down2 + b != 7) {//0인구간부터 위에서 한칸씩 내리기
                                //Log.d("두번쨰와일임", "ㅇㅇㅇ");
                                if(matrix[touch_y2 - y_down2 + b + 1][touch_x2] == 0){
                                    check_count++;
                                }
                                matrix[touch_y2 - y_down2 + b][touch_x2] = matrix[touch_y2 - y_down2 + b + 1][touch_x2];
                                //matrix[touch_y - y_down + a + 1][touch_x] = 0;

                                b++;
                                if (touch_y2 - y_down2 + b == 7) {
                                    matrix[touch_y2 - y_down2 + b][touch_x2] = (int) ((Math.random() * 7) + 1);
                                    score += 100;
                                }
                            }
                        }
                        a++;
                    }
                }
            }

            if(y_down + y_up > 1){
                Log.d("y_down + y_up > 1","ㅇㅇㅇ");
                int check_count = 1;
                while(check_count > 0) {
                    check_count = 0;
                    int a = 0;
                    while (touch_y - y_down + a != 7) {
                        //Log.d("첫번쨰와일임", "ㅇㅇㅇ");
                        //Log.d("touch_y - y_down:", Integer.toString(touch_y - y_down));
                        //Log.d("touch_y - y_down + a:", Integer.toString(touch_y - y_down + a));
                        //Log.d("touch_y", Integer.toString(touch_y));
                        if (matrix[touch_y - y_down + a][touch_x] == 0) {//0인구간 찾기
                            int b = a;
                            while (touch_y - y_down + b != 7) {//0인구간부터 위에서 한칸씩 내리기
                                //Log.d("두번쨰와일임", "ㅇㅇㅇ");
                                if(matrix[touch_y - y_down + b + 1][touch_x] == 0){
                                    check_count++;
                                }
                                matrix[touch_y - y_down + b][touch_x] = matrix[touch_y - y_down + b + 1][touch_x];
                                //matrix[touch_y - y_down + a + 1][touch_x] = 0;

                                b++;
                                if (touch_y - y_down + b == 7) {
                                    matrix[touch_y - y_down + b][touch_x] = (int) ((Math.random() * 7) + 1);
                                    score += 100;
                                }
                            }
                        }
                        a++;
                    }
                }
            }

        }
    }

    public void change(){
        if(touch1>0){
            if(touch2>0){//두번째터치
                if(((touch_y+1 == touch_y2) && (touch_x == touch_x2)) || ((touch_y-1 == touch_y2) && (touch_x == touch_x2)) || ((touch_y == touch_y2) && (touch_x+1 == touch_x2)) || ((touch_y == touch_y2) && (touch_x-1 == touch_x2))){
                    //두번째터치위치가 상하좌우인지 확인
                    int temp = matrix[touch_y][touch_x];
                    int temp2 = matrix[touch_y2][touch_x2];
                    matrix[touch_y2][touch_x2] = temp;
                    matrix[touch_y][touch_x] = temp2;//값 체인지
                    //touch_x = -1;//두번째터치로 인한 값 초기화
                    //touch_y = -1;
                    //touch_x2 = -1;
                    //touch_y2 = -1;
                    touch1 = 0;
                    touch2 = 0;
                    change_check = 1;// 중복검사할수있음
                    Log.d("상하좌우입니다.","ㅇㅁㄻㄷ");
                }else{//아니면 첫번째,두번째 터치를 초기화하고 터치를 처음부터 다시 받음
                    Log.d("상하좌우 ㅇ가아닙니다.","ㅇㅁㄻㄷ");
                    touch_x = -1;//0은 쓰는값이라서 -1로 초기화 하면 중복검사 안함
                    touch_y = -1;
                    touch_x2 = -1;
                    touch_y2 = -1;
                    touch1 = 0;
                    touch2 = 0;
                }

            }
        }
    }

    public void dupcheck_xleft(){
        if(touch_x == 0){
            x_left = 0;
        }else if(touch_x == 1){
            if(matrix[touch_y][touch_x] == matrix[touch_y][touch_x - 1]){
                x_left = 1;
            }else{
                x_left = 0;
            }
        }else if(touch_x == 2) {
            if (matrix[touch_y][touch_x] == matrix[touch_y][touch_x - 1]) {
                if (matrix[touch_y][touch_x] == matrix[touch_y][touch_x - 2]) {
                    x_left = 2;
                } else {
                    x_left = 1;
                }
            } else {
                x_left = 0;
            }
        }else if(touch_x == 3){
            if (matrix[touch_y][touch_x] == matrix[touch_y][touch_x - 1]) {
                if (matrix[touch_y][touch_x] == matrix[touch_y][touch_x - 2]) {
                    if (matrix[touch_y][touch_x] == matrix[touch_y][touch_x - 3]){
                        x_left = 3;
                    }else{
                        x_left = 2;
                    }
                } else {
                    x_left = 1;
                }
            } else {
                x_left = 0;
            }
        }else if(touch_x == 4){
            if (matrix[touch_y][touch_x] == matrix[touch_y][touch_x - 1]) {
                if (matrix[touch_y][touch_x] == matrix[touch_y][touch_x - 2]) {
                    if (matrix[touch_y][touch_x] == matrix[touch_y][touch_x - 3]){
                        if (matrix[touch_y][touch_x] == matrix[touch_y][touch_x - 4]){
                            x_left = 4;
                        }else{
                            x_left = 3;
                        }
                    }else{
                        x_left = 2;
                    }
                } else {
                    x_left = 1;
                }
            } else {
                x_left = 0;
            }
        }else if(touch_x == 5){
            if (matrix[touch_y][touch_x] == matrix[touch_y][touch_x - 1]) {
                if (matrix[touch_y][touch_x] == matrix[touch_y][touch_x - 2]) {
                    if (matrix[touch_y][touch_x] == matrix[touch_y][touch_x - 3]){
                        if (matrix[touch_y][touch_x] == matrix[touch_y][touch_x - 4]){
                            if (matrix[touch_y][touch_x] == matrix[touch_y][touch_x - 5]){
                                x_left = 5;
                            }else{
                                x_left = 4;
                            }
                        }else{
                            x_left = 3;
                        }
                    }else{
                        x_left = 2;
                    }
                } else {
                    x_left = 1;
                }
            } else {
                x_left = 0;
            }
        }else if(touch_x == 6){
            if (matrix[touch_y][touch_x] == matrix[touch_y][touch_x - 1]) {
                if (matrix[touch_y][touch_x] == matrix[touch_y][touch_x - 2]) {
                    if (matrix[touch_y][touch_x] == matrix[touch_y][touch_x - 3]){
                        if (matrix[touch_y][touch_x] == matrix[touch_y][touch_x - 4]){
                            if (matrix[touch_y][touch_x] == matrix[touch_y][touch_x - 5]){
                                if (matrix[touch_y][touch_x] == matrix[touch_y][touch_x - 6]){
                                    x_left = 6;
                                }else{
                                    x_left = 5;
                                }
                            }else{
                                x_left = 4;
                            }
                        }else{
                            x_left = 3;
                        }
                    }else{
                        x_left = 2;
                    }
                } else {
                    x_left = 1;
                }
            } else {
                x_left = 0;
            }
        }
    }

    public void dupcheck_xright(){
        if(touch_x == 6){
            x_right = 0;
        }else if(touch_x == 5){
            if(matrix[touch_y][touch_x] == matrix[touch_y][touch_x + 1]){
                x_right = 1;
            }else{
                x_right = 0;
            }
        }else if(touch_x == 4) {
            if (matrix[touch_y][touch_x] == matrix[touch_y][touch_x + 1]) {
                if (matrix[touch_y][touch_x] == matrix[touch_y][touch_x + 2]) {
                    x_right = 2;
                } else {
                    x_right = 1;
                }
            } else {
                x_right = 0;
            }
        }else if(touch_x == 3){
            if (matrix[touch_y][touch_x] == matrix[touch_y][touch_x + 1]) {
                if (matrix[touch_y][touch_x] == matrix[touch_y][touch_x + 2]) {
                    if (matrix[touch_y][touch_x] == matrix[touch_y][touch_x + 3]){
                        x_right = 3;
                    }else{
                        x_right = 2;
                    }
                } else {
                    x_right = 1;
                }
            } else {
                x_right = 0;
            }
        }else if(touch_x == 2){
            if (matrix[touch_y][touch_x] == matrix[touch_y][touch_x + 1]) {
                if (matrix[touch_y][touch_x] == matrix[touch_y][touch_x + 2]) {
                    if (matrix[touch_y][touch_x] == matrix[touch_y][touch_x + 3]){
                        if (matrix[touch_y][touch_x] == matrix[touch_y][touch_x + 4]){
                            x_right = 4;
                        }else{
                            x_right = 3;
                        }
                    }else{
                        x_right = 2;
                    }
                } else {
                    x_right = 1;
                }
            } else {
                x_right = 0;
            }
        }else if(touch_x == 1){
            if (matrix[touch_y][touch_x] == matrix[touch_y][touch_x + 1]) {
                if (matrix[touch_y][touch_x] == matrix[touch_y][touch_x + 2]) {
                    if (matrix[touch_y][touch_x] == matrix[touch_y][touch_x + 3]){
                        if (matrix[touch_y][touch_x] == matrix[touch_y][touch_x + 4]){
                            if (matrix[touch_y][touch_x] == matrix[touch_y][touch_x + 5]){
                                x_right = 5;
                            }else{
                                x_right = 4;
                            }
                        }else{
                            x_right = 3;
                        }
                    }else{
                        x_right = 2;
                    }
                } else {
                    x_right = 1;
                }
            } else {
                x_right = 0;
            }
        }else if(touch_x == 0){
            if (matrix[touch_y][touch_x] == matrix[touch_y][touch_x + 1]) {
                if (matrix[touch_y][touch_x] == matrix[touch_y][touch_x + 2]) {
                    if (matrix[touch_y][touch_x] == matrix[touch_y][touch_x + 3]){
                        if (matrix[touch_y][touch_x] == matrix[touch_y][touch_x + 4]){
                            if (matrix[touch_y][touch_x] == matrix[touch_y][touch_x + 5]){
                                if (matrix[touch_y][touch_x] == matrix[touch_y][touch_x + 6]){
                                    x_right = 6;
                                }else{
                                    x_right = 5;
                                }
                            }else{
                                x_right = 4;
                            }
                        }else{
                            x_right = 3;
                        }
                    }else{
                        x_right = 2;
                    }
                } else {
                    x_right = 1;
                }
            } else {
                x_right = 0;
            }
        }
    }

    public void dupcheck_yup(){
        if(touch_y == 6){
            y_up = 0;
        }else if(touch_y == 5){
            if(matrix[touch_y][touch_x] == matrix[touch_y+1][touch_x]){
                y_up = 1;
            }else{
                y_up = 0;
            }
        }else if(touch_y == 4) {
            if (matrix[touch_y][touch_x] == matrix[touch_y + 1][touch_x]) {
                if (matrix[touch_y][touch_x] == matrix[touch_y + 2][touch_x]) {
                    y_up = 2;
                } else {
                    y_up = 1;
                }
            } else {
                y_up = 0;
            }
        }else if(touch_y == 3){
            if (matrix[touch_y][touch_x] == matrix[touch_y + 1][touch_x]) {
                if (matrix[touch_y][touch_x] == matrix[touch_y + 2][touch_x]) {
                    if (matrix[touch_y][touch_x] == matrix[touch_y + 3][touch_x]){
                        y_up = 3;
                    }else{
                        y_up = 2;
                    }
                } else {
                    y_up = 1;
                }
            } else {
                y_up = 0;
            }
        }else if(touch_y == 2){
            if (matrix[touch_y][touch_x] == matrix[touch_y + 1][touch_x]) {
                if (matrix[touch_y][touch_x] == matrix[touch_y + 2][touch_x]) {
                    if (matrix[touch_y][touch_x] == matrix[touch_y + 3][touch_x]){
                        if (matrix[touch_y][touch_x] == matrix[touch_y + 4][touch_x]){
                            y_up = 4;
                        }else{
                            y_up = 3;
                        }
                    }else{
                        y_up = 2;
                    }
                } else {
                    y_up = 1;
                }
            } else {
                y_up = 0;
            }
        }else if(touch_y == 1){
            if (matrix[touch_y][touch_x] == matrix[touch_y + 1][touch_x]) {
                if (matrix[touch_y][touch_x] == matrix[touch_y + 2][touch_x]) {
                    if (matrix[touch_y][touch_x] == matrix[touch_y + 3][touch_x]){
                        if (matrix[touch_y][touch_x] == matrix[touch_y + 4][touch_x]){
                            if (matrix[touch_y][touch_x] == matrix[touch_y + 5][touch_x]){
                                y_up = 5;
                            }else{
                                y_up = 4;
                            }
                        }else{
                            y_up = 3;
                        }
                    }else{
                        y_up = 2;
                    }
                } else {
                    y_up = 1;
                }
            } else {
                y_up = 0;
            }
        }else if(touch_y == 0){
            if (matrix[touch_y][touch_x] == matrix[touch_y + 1][touch_x]) {
                if (matrix[touch_y][touch_x] == matrix[touch_y + 2][touch_x]) {
                    if (matrix[touch_y][touch_x] == matrix[touch_y + 3][touch_x]){
                        if (matrix[touch_y][touch_x] == matrix[touch_y + 4][touch_x]){
                            if (matrix[touch_y][touch_x] == matrix[touch_y + 5][touch_x]){
                                if (matrix[touch_y][touch_x] == matrix[touch_y + 6][touch_x]){
                                    y_up = 6;
                                }else{
                                    y_up = 5;
                                }
                            }else{
                                y_up = 4;
                            }
                        }else{
                            y_up = 3;
                        }
                    }else{
                        y_up = 2;
                    }
                } else {
                    y_up = 1;
                }
            } else {
                y_up = 0;
            }
        }
    }

    public void dupcheck_ydown(){
        if(touch_y == 0){
            y_down = 0;
        }else if(touch_y == 1){
            if(matrix[touch_y][touch_x] == matrix[touch_y-1][touch_x]){
                y_down = 1;
            }else{
                y_down = 0;
            }
        }else if(touch_y == 2) {
            if (matrix[touch_y][touch_x] == matrix[touch_y - 1][touch_x]) {
                if (matrix[touch_y][touch_x] == matrix[touch_y - 2][touch_x]) {
                    y_down = 2;
                } else {
                    y_down = 1;
                }
            } else {
                y_down = 0;
            }
        }else if(touch_y == 3){
            if (matrix[touch_y][touch_x] == matrix[touch_y - 1][touch_x]) {
                if (matrix[touch_y][touch_x] == matrix[touch_y - 2][touch_x]) {
                    if (matrix[touch_y][touch_x] == matrix[touch_y - 3][touch_x]){
                        y_down = 3;
                    }else{
                        y_down = 2;
                    }
                } else {
                    y_down = 1;
                }
            } else {
                y_down = 0;
            }
        }else if(touch_y == 4){
            if (matrix[touch_y][touch_x] == matrix[touch_y - 1][touch_x]) {
                if (matrix[touch_y][touch_x] == matrix[touch_y - 2][touch_x]) {
                    if (matrix[touch_y][touch_x] == matrix[touch_y - 3][touch_x]){
                        if (matrix[touch_y][touch_x] == matrix[touch_y - 4][touch_x]){
                            y_down = 4;
                        }else{
                            y_down = 3;
                        }
                    }else{
                        y_down = 2;
                    }
                } else {
                    y_down = 1;
                }
            } else {
                y_down = 0;
            }
        }else if(touch_y == 5){
            if (matrix[touch_y][touch_x] == matrix[touch_y - 1][touch_x]) {
                if (matrix[touch_y][touch_x] == matrix[touch_y - 2][touch_x]) {
                    if (matrix[touch_y][touch_x] == matrix[touch_y - 3][touch_x]){
                        if (matrix[touch_y][touch_x] == matrix[touch_y - 4][touch_x]){
                            if (matrix[touch_y][touch_x] == matrix[touch_y - 5][touch_x]){
                                y_down = 5;
                            }else{
                                y_down = 4;
                            }
                        }else{
                            y_down = 3;
                        }
                    }else{
                        y_down = 2;
                    }
                } else {
                    y_down = 1;
                }
            } else {
                y_down = 0;
            }
        }else if(touch_y == 6){
            if (matrix[touch_y][touch_x] == matrix[touch_y - 1][touch_x]) {
                if (matrix[touch_y][touch_x] == matrix[touch_y - 2][touch_x]) {
                    if (matrix[touch_y][touch_x] == matrix[touch_y - 3][touch_x]){
                        if (matrix[touch_y][touch_x] == matrix[touch_y - 4][touch_x]){
                            if (matrix[touch_y][touch_x] == matrix[touch_y - 5][touch_x]){
                                if (matrix[touch_y][touch_x] == matrix[touch_y - 6][touch_x]){
                                    y_down = 6;
                                }else{
                                    y_down = 5;
                                }
                            }else{
                                y_down = 4;
                            }
                        }else{
                            y_down = 3;
                        }
                    }else{
                        y_down = 2;
                    }
                } else {
                    y_down = 1;
                }
            } else {
                y_down = 0;
            }
        }

    }

    public void dupcheck_x2left(){
        if(touch_x2 == 0){
            x_left2 = 0;
        }else if(touch_x2 == 1){
            if(matrix[touch_y2][touch_x2] == matrix[touch_y2][touch_x2 - 1]){
                x_left2 = 1;
            }else{
                x_left2 = 0;
            }
        }else if(touch_x2 == 2) {
            if (matrix[touch_y2][touch_x2] == matrix[touch_y2][touch_x2 - 1]) {
                if (matrix[touch_y2][touch_x2] == matrix[touch_y2][touch_x2 - 2]) {
                    x_left2 = 2;
                } else {
                    x_left2 = 1;
                }
            } else {
                x_left2 = 0;
            }
        }else if(touch_x2 == 3){
            if (matrix[touch_y2][touch_x2] == matrix[touch_y2][touch_x2 - 1]) {
                if (matrix[touch_y2][touch_x2] == matrix[touch_y2][touch_x2 - 2]) {
                    if (matrix[touch_y2][touch_x2] == matrix[touch_y2][touch_x2 - 3]){
                        x_left2 = 3;
                    }else{
                        x_left2 = 2;
                    }
                } else {
                    x_left2 = 1;
                }
            } else {
                x_left2 = 0;
            }
        }else if(touch_x2 == 4){
            if (matrix[touch_y2][touch_x2] == matrix[touch_y2][touch_x2 - 1]) {
                if (matrix[touch_y2][touch_x2] == matrix[touch_y2][touch_x2 - 2]) {
                    if (matrix[touch_y2][touch_x2] == matrix[touch_y2][touch_x2 - 3]){
                        if (matrix[touch_y2][touch_x2] == matrix[touch_y2][touch_x2 - 4]){
                            x_left2 = 4;
                        }else{
                            x_left2 = 3;
                        }
                    }else{
                        x_left2 = 2;
                    }
                } else {
                    x_left2 = 1;
                }
            } else {
                x_left2 = 0;
            }
        }else if(touch_x2 == 5){
            if (matrix[touch_y2][touch_x2] == matrix[touch_y2][touch_x2 - 1]) {
                if (matrix[touch_y2][touch_x2] == matrix[touch_y2][touch_x2 - 2]) {
                    if (matrix[touch_y2][touch_x2] == matrix[touch_y2][touch_x2 - 3]){
                        if (matrix[touch_y2][touch_x2] == matrix[touch_y2][touch_x2 - 4]){
                            if (matrix[touch_y2][touch_x2] == matrix[touch_y2][touch_x2 - 5]){
                                x_left2 = 5;
                            }else{
                                x_left2 = 4;
                            }
                        }else{
                            x_left2 = 3;
                        }
                    }else{
                        x_left2 = 2;
                    }
                } else {
                    x_left2 = 1;
                }
            } else {
                x_left2 = 0;
            }
        }else if(touch_x2 == 6){
            if (matrix[touch_y2][touch_x2] == matrix[touch_y2][touch_x2 - 1]) {
                if (matrix[touch_y2][touch_x2] == matrix[touch_y2][touch_x2 - 2]) {
                    if (matrix[touch_y2][touch_x2] == matrix[touch_y2][touch_x2 - 3]){
                        if (matrix[touch_y2][touch_x2] == matrix[touch_y2][touch_x2 - 4]){
                            if (matrix[touch_y2][touch_x2] == matrix[touch_y2][touch_x2 - 5]){
                                if (matrix[touch_y2][touch_x2] == matrix[touch_y2][touch_x2 - 6]){
                                    x_left2 = 6;
                                }else{
                                    x_left2 = 5;
                                }
                            }else{
                                x_left2 = 4;
                            }
                        }else{
                            x_left2 = 3;
                        }
                    }else{
                        x_left2 = 2;
                    }
                } else {
                    x_left2 = 1;
                }
            } else {
                x_left2 = 0;
            }
        }
    }

    public void dupcheck_x2right(){
        if(touch_x2 == 6){
            x_right2 = 0;
        }else if(touch_x2 == 5){
            if(matrix[touch_y2][touch_x2] == matrix[touch_y2][touch_x2 + 1]){
                x_right2 = 1;
            }else{
                x_right2 = 0;
            }
        }else if(touch_x2 == 4) {
            if (matrix[touch_y2][touch_x2] == matrix[touch_y2][touch_x2 + 1]) {
                if (matrix[touch_y2][touch_x2] == matrix[touch_y2][touch_x2 + 2]) {
                    x_right2 = 2;
                } else {
                    x_right2 = 1;
                }
            } else {
                x_right2 = 0;
            }
        }else if(touch_x2 == 3){
            if (matrix[touch_y2][touch_x2] == matrix[touch_y2][touch_x2 + 1]) {
                if (matrix[touch_y2][touch_x2] == matrix[touch_y2][touch_x2 + 2]) {
                    if (matrix[touch_y2][touch_x2] == matrix[touch_y2][touch_x2 + 3]){
                        x_right2 = 3;
                    }else{
                        x_right2 = 2;
                    }
                } else {
                    x_right2 = 1;
                }
            } else {
                x_right2 = 0;
            }
        }else if(touch_x2 == 2){
            if (matrix[touch_y2][touch_x2] == matrix[touch_y2][touch_x2 + 1]) {
                if (matrix[touch_y2][touch_x2] == matrix[touch_y2][touch_x2 + 2]) {
                    if (matrix[touch_y2][touch_x2] == matrix[touch_y2][touch_x2 + 3]){
                        if (matrix[touch_y2][touch_x2] == matrix[touch_y2][touch_x2 + 4]){
                            x_right2 = 4;
                        }else{
                            x_right2 = 3;
                        }
                    }else{
                        x_right2 = 2;
                    }
                } else {
                    x_right2 = 1;
                }
            } else {
                x_right2 = 0;
            }
        }else if(touch_x2 == 1){
            if (matrix[touch_y2][touch_x2] == matrix[touch_y2][touch_x2 + 1]) {
                if (matrix[touch_y2][touch_x2] == matrix[touch_y2][touch_x2 + 2]) {
                    if (matrix[touch_y2][touch_x2] == matrix[touch_y2][touch_x2 + 3]){
                        if (matrix[touch_y2][touch_x2] == matrix[touch_y2][touch_x2 + 4]){
                            if (matrix[touch_y2][touch_x2] == matrix[touch_y2][touch_x2 + 5]){
                                x_right2 = 5;
                            }else{
                                x_right2 = 4;
                            }
                        }else{
                            x_right2 = 3;
                        }
                    }else{
                        x_right2 = 2;
                    }
                } else {
                    x_right2 = 1;
                }
            } else {
                x_right2 = 0;
            }
        }else if(touch_x2 == 0){
            if (matrix[touch_y2][touch_x2] == matrix[touch_y2][touch_x2 + 1]) {
                if (matrix[touch_y2][touch_x2] == matrix[touch_y2][touch_x2 + 2]) {
                    if (matrix[touch_y2][touch_x2] == matrix[touch_y2][touch_x2 + 3]){
                        if (matrix[touch_y2][touch_x2] == matrix[touch_y2][touch_x2 + 4]){
                            if (matrix[touch_y2][touch_x2] == matrix[touch_y2][touch_x2 + 5]){
                                if (matrix[touch_y2][touch_x2] == matrix[touch_y2][touch_x2 + 6]){
                                    x_right2 = 6;
                                }else{
                                    x_right2 = 5;
                                }
                            }else{
                                x_right2 = 4;
                            }
                        }else{
                            x_right2 = 3;
                        }
                    }else{
                        x_right2 = 2;
                    }
                } else {
                    x_right2 = 1;
                }
            } else {
                x_right2 = 0;
            }
        }
    }

    public void dupcheck_y2up(){
        if(touch_y2 == 6){
            y_up2 = 0;
        }else if(touch_y2 == 5){
            if(matrix[touch_y2][touch_x2] == matrix[touch_y2 + 1][touch_x2]){
                y_up2 = 1;
            }else{
                y_up2 = 0;
            }
        }else if(touch_y2 == 4) {
            if (matrix[touch_y2][touch_x2] == matrix[touch_y2 + 1][touch_x2]) {
                if (matrix[touch_y2][touch_x2] == matrix[touch_y2 + 2][touch_x2]) {
                    y_up2 = 2;
                } else {
                    y_up2 = 1;
                }
            } else {
                y_up2 = 0;
            }
        }else if(touch_y2 == 3){
            if (matrix[touch_y2][touch_x2] == matrix[touch_y2 + 1][touch_x2]) {
                if (matrix[touch_y2][touch_x2] == matrix[touch_y2 + 2][touch_x2]) {
                    if (matrix[touch_y2][touch_x2] == matrix[touch_y2 + 3][touch_x2]){
                        y_up2 = 3;
                    }else{
                        y_up2 = 2;
                    }
                } else {
                    y_up2 = 1;
                }
            } else {
                y_up2 = 0;
            }
        }else if(touch_y2 == 2){
            if (matrix[touch_y2][touch_x2] == matrix[touch_y2 + 1][touch_x2]) {
                if (matrix[touch_y2][touch_x2] == matrix[touch_y2 + 2][touch_x2]) {
                    if (matrix[touch_y2][touch_x2] == matrix[touch_y2 + 3][touch_x2]){
                        if (matrix[touch_y2][touch_x2] == matrix[touch_y2 + 4][touch_x2]){
                            y_up2 = 4;
                        }else{
                            y_up2 = 3;
                        }
                    }else{
                        y_up2 = 2;
                    }
                } else {
                    y_up2 = 1;
                }
            } else {
                y_up2 = 0;
            }
        }else if(touch_y2 == 1){
            if (matrix[touch_y2][touch_x2] == matrix[touch_y2 + 1][touch_x2]) {
                if (matrix[touch_y2][touch_x2] == matrix[touch_y2 + 2][touch_x2]) {
                    if (matrix[touch_y2][touch_x2] == matrix[touch_y2 + 3][touch_x2]){
                        if (matrix[touch_y2][touch_x2] == matrix[touch_y2 + 4][touch_x2]){
                            if (matrix[touch_y2][touch_x2] == matrix[touch_y2 + 5][touch_x2]){
                                y_up2 = 5;
                            }else{
                                y_up2 = 4;
                            }
                        }else{
                            y_up2 = 3;
                        }
                    }else{
                        y_up2 = 2;
                    }
                } else {
                    y_up2 = 1;
                }
            } else {
                y_up2 = 0;
            }
        }else if(touch_y2 == 0){
            if (matrix[touch_y2][touch_x2] == matrix[touch_y2 + 1][touch_x2]) {
                if (matrix[touch_y2][touch_x2] == matrix[touch_y2 + 2][touch_x2]) {
                    if (matrix[touch_y2][touch_x2] == matrix[touch_y2 + 3][touch_x2]){
                        if (matrix[touch_y2][touch_x2] == matrix[touch_y2 + 4][touch_x2]){
                            if (matrix[touch_y2][touch_x2] == matrix[touch_y2 + 5][touch_x2]){
                                if (matrix[touch_y2][touch_x2] == matrix[touch_y2 + 6][touch_x2]){
                                    y_up2 = 6;
                                }else{
                                    y_up2 = 5;
                                }
                            }else{
                                y_up2 = 4;
                            }
                        }else{
                            y_up2 = 3;
                        }
                    }else{
                        y_up2 = 2;
                    }
                } else {
                    y_up2 = 1;
                }
            } else {
                y_up2 = 0;
            }
        }
    }

    public void dupcheck_y2down(){
        if(touch_y2 == 0){
            y_down2 = 0;
        }else if(touch_y2 == 1){
            if(matrix[touch_y2][touch_x2] == matrix[touch_y2 - 1][touch_x2]){
                y_down2 = 1;
            }else{
                y_down2 = 0;
            }
        }else if(touch_y2 == 2) {
            if (matrix[touch_y2][touch_x2] == matrix[touch_y2 - 1][touch_x2]) {
                if (matrix[touch_y2][touch_x2] == matrix[touch_y2 - 2][touch_x2]) {
                    y_down2 = 2;
                } else {
                    y_down2 = 1;
                }
            } else {
                y_down2 = 0;
            }
        }else if(touch_y2 == 3){
            if (matrix[touch_y2][touch_x2] == matrix[touch_y2 - 1][touch_x2]) {
                if (matrix[touch_y2][touch_x2] == matrix[touch_y2 - 2][touch_x2]) {
                    if (matrix[touch_y2][touch_x2] == matrix[touch_y2 - 3][touch_x2]){
                        y_down2 = 3;
                    }else{
                        y_down2 = 2;
                    }
                } else {
                    y_down2 = 1;
                }
            } else {
                y_down2 = 0;
            }
        }else if(touch_y2 == 4){
            if (matrix[touch_y2][touch_x2] == matrix[touch_y2 - 1][touch_x2]) {
                if (matrix[touch_y2][touch_x2] == matrix[touch_y2 - 2][touch_x2]) {
                    if (matrix[touch_y2][touch_x2] == matrix[touch_y2 - 3][touch_x2]){
                        if (matrix[touch_y2][touch_x2] == matrix[touch_y2 - 4][touch_x2]){
                            y_down2 = 4;
                        }else{
                            y_down2 = 3;
                        }
                    }else{
                        y_down2 = 2;
                    }
                } else {
                    y_down2 = 1;
                }
            } else {
                y_down2 = 0;
            }
        }else if(touch_y2 == 5){
            if (matrix[touch_y2][touch_x2] == matrix[touch_y2 - 1][touch_x2]) {
                if (matrix[touch_y2][touch_x2] == matrix[touch_y2 - 2][touch_x2]) {
                    if (matrix[touch_y2][touch_x2] == matrix[touch_y2 - 3][touch_x2]){
                        if (matrix[touch_y2][touch_x2] == matrix[touch_y2 - 4][touch_x2]){
                            if (matrix[touch_y2][touch_x2] == matrix[touch_y2 - 5][touch_x2]){
                                y_down2 = 5;
                            }else{
                                y_down2 = 4;
                            }
                        }else{
                            y_down2 = 3;
                        }
                    }else{
                        y_down2 = 2;
                    }
                } else {
                    y_down2 = 1;
                }
            } else {
                y_down2 = 0;
            }
        }else if(touch_y2 == 6){
            if (matrix[touch_y2][touch_x2] == matrix[touch_y2 - 1][touch_x2]) {
                if (matrix[touch_y2][touch_x2] == matrix[touch_y2 - 2][touch_x2]) {
                    if (matrix[touch_y2][touch_x2] == matrix[touch_y2 - 3][touch_x2]){
                        if (matrix[touch_y2][touch_x2] == matrix[touch_y2 - 4][touch_x2]){
                            if (matrix[touch_y2][touch_x2] == matrix[touch_y2 - 5][touch_x2]){
                                if (matrix[touch_y2][touch_x2] == matrix[touch_y2 - 6][touch_x2]){
                                    y_down2 = 6;
                                }else{
                                    y_down2 = 5;
                                }
                            }else{
                                y_down2 = 4;
                            }
                        }else{
                            y_down2 = 3;
                        }
                    }else{
                        y_down2 = 2;
                    }
                } else {
                    y_down2 = 1;
                }
            } else {
                y_down2 = 0;
            }
        }

    }

    public void draw(){
        String name = "";
        int x, y;
        x = 0;
        y = 0;
        for(int i = 0; i<7; i++){
            if(i == 0){
                y = 0;
            }else if(i == 1){
                y = 200;
            }else if(i == 2){
                y = 400;
            }else if(i == 3){
                y = 600;
            }else if(i == 4){
                y = 800;
            }else if(i == 5){
                y = 1000;
            }else if(i == 6){
                y = 1200;
            }
            for(int k = 0; k<7; k++){
                if(k == 0){
                    x = 0;
                }else if(k == 1){
                    x = 200;
                }else if(k == 2){
                    x = 400;
                }else if(k == 3){
                    x = 600;
                }else if(k == 4){
                    x = 800;
                }else if(k == 5){
                    x = 1000;
                }else if(k == 6){
                    x = 1200;
                }
                if(matrix[i][k] == 1){
                    name = "goat";
                    canvas.drawBitmap(goat, 20+x,2000-y , null);
                }else if(matrix[i][k] == 2){
                    name = "beaver";
                    canvas.drawBitmap(beaver, 20+x,2000-y , null);
                }else if(matrix[i][k] == 3){
                    name = "bull";
                    canvas.drawBitmap(bull, 20+x,2000-y , null);
                }else if(matrix[i][k] == 4){
                    name = "deer";
                    canvas.drawBitmap(deer, 20+x,2000-y , null);
                }else if(matrix[i][k] == 5){
                    name = "elephant";
                    canvas.drawBitmap(elephant, 20+x,2000-y , null);
                }else if(matrix[i][k] == 6){
                    name = "tiger";
                    canvas.drawBitmap(tiger, 20+x,2000-y , null);
                }else if(matrix[i][k] == 7){
                    name = "whale";
                    canvas.drawBitmap(whale, 20+x,2000-y , null);
                }else{
                    //Log.d("0입니당ㅇㅇ","ㅇㄴㄹ");
                }

            }

        }
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //Log.d("터칭ㅁㄹ","ㅇㅇ");
        //Log.d("ㅁㄹㄷㅁ",Float.toString(event.getX()));
        long now_time = System.currentTimeMillis() - start_time;
        long time = (long) ((now_time - before_time) / 100d);
        Log.d("시간",Long.toString(time));
        if(time > 0) {
            //Log.d("통과ㅇㅇ",Long.toString(time));
            if (event.getX() < 220 && event.getY() < 2200 && event.getY() > 2000) {
                if (touch1 == 0) {
                    Log.d("0,0","ㅇㅇ1");
                    //canvas.drawRect(0,2000,220,2200,paint);
                    touch1 = matrix[0][0];//동물번호
                    touch_x = 0;//x위치
                    touch_y = 0;//y위치
                } else {
                    //canvas.drawRect(0,2000,220,2200,paint);
                    Log.d("0,0","ㅇㅇ2");
                    touch2 = matrix[0][0];
                    touch_x2 = 0;
                    touch_y2 = 0;
                }
            } else if (event.getX() < 420 && event.getX() >= 220 && event.getY() < 2200 && event.getY() > 2000) {
                if (touch1 == 0) {
                    Log.d("0,1","ㅇㅇ1");
                    touch1 = matrix[0][1];
                    touch_x = 1;
                    touch_y = 0;
                } else {
                    Log.d("0,1","ㅇㅇ2");
                    touch2 = matrix[0][1];
                    touch_x2 = 1;
                    touch_y2 = 0;
                }
            } else if (event.getX() < 620 && event.getX() >= 420 && event.getY() < 2200 && event.getY() > 2000) {
                if (touch1 == 0) {
                    Log.d("0,2","ㅇㅇ1");
                    touch1 = matrix[0][2];
                    touch_x = 2;
                    touch_y = 0;
                } else {
                    Log.d("0,2","ㅇㅇ2");
                    touch2 = matrix[0][2];
                    touch_x2 = 2;
                    touch_y2 = 0;
                }
            } else if (event.getX() < 820 && event.getX() >= 620 && event.getY() < 2200 && event.getY() > 2000) {
                if (touch1 == 0) {
                    Log.d("0,3","ㅇㅇ1");
                    touch1 = matrix[0][3];
                    touch_x = 3;
                    touch_y = 0;
                } else {
                    Log.d("0,3","ㅇㅇ2");
                    touch2 = matrix[0][3];
                    touch_x2 = 3;
                    touch_y2 = 0;
                }
            } else if (event.getX() < 1020 && event.getX() >= 820 && event.getY() < 2200 && event.getY() > 2000) {
                if (touch1 == 0) {
                    touch1 = matrix[0][4];
                    touch_x = 4;
                    touch_y = 0;
                } else {
                    touch2 = matrix[0][4];
                    touch_x2 = 4;
                    touch_y2 = 0;
                }
            } else if (event.getX() < 1220 && event.getX() >= 1020 && event.getY() < 2200 && event.getY() > 2000) {
                if (touch1 == 0) {
                    touch1 = matrix[0][5];
                    touch_x = 5;
                    touch_y = 0;
                } else {
                    touch2 = matrix[0][5];
                    touch_x2 = 5;
                    touch_y2 = 0;
                }
            } else if (event.getX() < 1420 && event.getX() >= 1220 && event.getY() < 2200 && event.getY() > 2000) {
                if (touch1 == 0) {
                    touch1 = matrix[0][6];
                    touch_x = 6;
                    touch_y = 0;
                } else {
                    touch2 = matrix[0][6];
                    touch_x2 = 6;
                    touch_y2 = 0;
                }
            } else if (event.getX() < 220 && event.getY() <= 2000 && event.getY() > 1800) {
                if (touch1 == 0) {
                    touch1 = matrix[1][0];
                    touch_x = 0;
                    touch_y = 1;
                } else {
                    touch2 = matrix[1][0];
                    touch_x2 = 0;
                    touch_y2 = 1;
                }
            } else if (event.getX() < 420 && event.getX() >= 220 && event.getY() <= 2000 && event.getY() > 1800) {
                if (touch1 == 0) {
                    touch1 = matrix[1][1];
                    touch_x = 1;
                    touch_y = 1;
                } else {
                    touch2 = matrix[1][1];
                    touch_x2 = 1;
                    touch_y2 = 1;
                }
            } else if (event.getX() < 620 && event.getX() >= 420 && event.getY() <= 2000 && event.getY() > 1800) {
                if (touch1 == 0) {
                    touch1 = matrix[1][2];
                    touch_x = 2;
                    touch_y = 1;
                } else {
                    touch2 = matrix[1][2];
                    touch_x2 = 2;
                    touch_y2 = 1;
                }
            } else if (event.getX() < 820 && event.getX() >= 620 && event.getY() <= 2000 && event.getY() > 1800) {
                if (touch1 == 0) {
                    touch1 = matrix[1][3];
                    touch_x = 3;
                    touch_y = 1;
                } else {
                    touch2 = matrix[1][3];
                    touch_x2 = 3;
                    touch_y2 = 1;
                }
            } else if (event.getX() < 1020 && event.getX() >= 820 && event.getY() <= 2000 && event.getY() > 1800) {
                if (touch1 == 0) {
                    touch1 = matrix[1][4];
                    touch_x = 4;
                    touch_y = 1;
                } else {
                    touch2 = matrix[1][4];
                    touch_x2 = 4;
                    touch_y2 = 1;
                }
            } else if (event.getX() < 1220 && event.getX() >= 1020 && event.getY() <= 2000 && event.getY() > 1800) {
                if (touch1 == 0) {
                    touch1 = matrix[1][5];
                    touch_x = 5;
                    touch_y = 1;
                } else {
                    touch2 = matrix[1][5];
                    touch_x2 = 5;
                    touch_y2 = 1;
                }
            } else if (event.getX() < 1420 && event.getX() >= 1220 && event.getY() <= 2000 && event.getY() > 1800) {
                if (touch1 == 0) {
                    touch1 = matrix[1][6];
                    touch_x = 6;
                    touch_y = 1;
                } else {
                    touch2 = matrix[1][6];
                    touch_x2 = 6;
                    touch_y2 = 1;
                }
            } else if (event.getX() < 220 && event.getY() <= 1800 && event.getY() > 1600) {
                if (touch1 == 0) {
                    touch1 = matrix[2][0];
                    touch_x = 0;
                    touch_y = 2;
                } else {
                    touch2 = matrix[2][0];
                    touch_x2 = 0;
                    touch_y2 = 2;
                }
            } else if (event.getX() < 420 && event.getX() >= 220 && event.getY() <= 1800 && event.getY() > 1600) {
                if (touch1 == 0) {
                    touch1 = matrix[2][1];
                    touch_x = 1;
                    touch_y = 2;
                } else {
                    touch2 = matrix[2][1];
                    touch_x2 = 1;
                    touch_y2 = 2;
                }
            } else if (event.getX() < 620 && event.getX() >= 420 && event.getY() <= 1800 && event.getY() > 1600) {
                if (touch1 == 0) {
                    touch1 = matrix[2][2];
                    touch_x = 2;
                    touch_y = 2;
                } else {
                    touch2 = matrix[2][2];
                    touch_x2 = 2;
                    touch_y2 = 2;
                }
            } else if (event.getX() < 820 && event.getX() >= 620 && event.getY() <= 1800 && event.getY() > 1600) {
                if (touch1 == 0) {
                    touch1 = matrix[2][3];
                    touch_x = 3;
                    touch_y = 2;
                } else {
                    touch2 = matrix[2][3];
                    touch_x2 = 3;
                    touch_y2 = 2;
                }
            } else if (event.getX() < 1020 && event.getX() >= 820 && event.getY() <= 1800 && event.getY() > 1600) {
                if (touch1 == 0) {
                    touch1 = matrix[2][4];
                    touch_x = 4;
                    touch_y = 2;
                } else {
                    touch2 = matrix[2][4];
                    touch_x2 = 4;
                    touch_y2 = 2;
                }
            } else if (event.getX() < 1220 && event.getX() >= 1020 && event.getY() <= 1800 && event.getY() > 1600) {
                if (touch1 == 0) {
                    touch1 = matrix[2][5];
                    touch_x = 5;
                    touch_y = 2;
                } else {
                    touch2 = matrix[2][5];
                    touch_x2 = 5;
                    touch_y2 = 2;
                }
            } else if (event.getX() < 1420 && event.getX() >= 1220 && event.getY() <= 1800 && event.getY() > 1600) {
                if (touch1 == 0) {
                    touch1 = matrix[2][6];
                    touch_x = 6;
                    touch_y = 2;
                } else {
                    touch2 = matrix[2][6];
                    touch_x2 = 6;
                    touch_y2 = 2;
                }
            } else if (event.getX() < 220 && event.getY() <= 1600 && event.getY() > 1400) {
                if (touch1 == 0) {
                    touch1 = matrix[3][0];
                    touch_x = 0;
                    touch_y = 3;
                } else {
                    touch2 = matrix[3][0];
                    touch_x2 = 0;
                    touch_y2 = 3;
                }
            } else if (event.getX() < 420 && event.getX() >= 220 && event.getY() <= 1600 && event.getY() > 1400) {
                if (touch1 == 0) {
                    touch1 = matrix[3][1];
                    touch_x = 1;
                    touch_y = 3;
                } else {
                    touch2 = matrix[3][1];
                    touch_x2 = 1;
                    touch_y2 = 3;
                }
            } else if (event.getX() < 620 && event.getX() >= 420 && event.getY() <= 1600 && event.getY() > 1400) {
                if (touch1 == 0) {
                    touch1 = matrix[3][2];
                    touch_x = 2;
                    touch_y = 3;
                } else {
                    touch2 = matrix[3][2];
                    touch_x2 = 2;
                    touch_y2 = 3;
                }
            } else if (event.getX() < 820 && event.getX() >= 620 && event.getY() <= 1600 && event.getY() > 1400) {
                if (touch1 == 0) {
                    touch1 = matrix[3][3];
                    touch_x = 3;
                    touch_y = 3;
                } else {
                    touch2 = matrix[3][3];
                    touch_x2 = 3;
                    touch_y2 = 3;
                }
            } else if (event.getX() < 1020 && event.getX() >= 820 && event.getY() <= 1600 && event.getY() > 1400) {
                if (touch1 == 0) {
                    touch1 = matrix[3][4];
                    touch_x = 4;
                    touch_y = 3;
                } else {
                    touch2 = matrix[3][4];
                    touch_x2 = 4;
                    touch_y2 = 3;
                }
            } else if (event.getX() < 1220 && event.getX() >= 1020 && event.getY() <= 1600 && event.getY() > 1400) {
                if (touch1 == 0) {
                    touch1 = matrix[3][5];
                    touch_x = 5;
                    touch_y = 3;
                } else {
                    touch2 = matrix[3][5];
                    touch_x2 = 5;
                    touch_y2 = 3;
                }
            } else if (event.getX() < 1420 && event.getX() >= 1220 && event.getY() <= 1600 && event.getY() > 1400) {
                if (touch1 == 0) {
                    touch1 = matrix[3][6];
                    touch_x = 6;
                    touch_y = 3;
                } else {
                    touch2 = matrix[3][6];
                    touch_x2 = 6;
                    touch_y2 = 3;
                }
            } else if (event.getX() < 220 && event.getY() <= 1400 && event.getY() > 1200) {
                if (touch1 == 0) {
                    touch1 = matrix[4][0];
                    touch_x = 0;
                    touch_y = 4;
                } else {
                    touch2 = matrix[4][0];
                    touch_x2 = 0;
                    touch_y2 = 4;
                }
            } else if (event.getX() < 420 && event.getX() >= 220 && event.getY() <= 1400 && event.getY() > 1200) {
                if (touch1 == 0) {
                    touch1 = matrix[4][1];
                    touch_x = 1;
                    touch_y = 4;
                } else {
                    touch2 = matrix[4][1];
                    touch_x2 = 1;
                    touch_y2 = 4;
                }
            } else if (event.getX() < 620 && event.getX() >= 420 && event.getY() <= 1400 && event.getY() > 1200) {
                if (touch1 == 0) {
                    touch1 = matrix[4][2];
                    touch_x = 2;
                    touch_y = 4;
                } else {
                    touch2 = matrix[4][2];
                    touch_x2 = 2;
                    touch_y2 = 4;
                }
            } else if (event.getX() < 820 && event.getX() >= 620 && event.getY() <= 1400 && event.getY() > 1200) {
                if (touch1 == 0) {
                    touch1 = matrix[4][3];
                    touch_x = 3;
                    touch_y = 4;
                } else {
                    touch2 = matrix[4][3];
                    touch_x2 = 3;
                    touch_y2 = 4;
                }
            } else if (event.getX() < 1020 && event.getX() >= 820 && event.getY() <= 1400 && event.getY() > 1200) {
                if (touch1 == 0) {
                    touch1 = matrix[4][4];
                    touch_x = 4;
                    touch_y = 4;
                } else {
                    touch2 = matrix[4][4];
                    touch_x2 = 4;
                    touch_y2 = 4;
                }
            } else if (event.getX() < 1220 && event.getX() >= 1020 && event.getY() <= 1400 && event.getY() > 1200) {
                if (touch1 == 0) {
                    touch1 = matrix[4][5];
                    touch_x = 5;
                    touch_y = 4;
                } else {
                    touch2 = matrix[4][5];
                    touch_x2 = 5;
                    touch_y2 = 4;
                }
            } else if (event.getX() < 1420 && event.getX() >= 1220 && event.getY() <= 1400 && event.getY() > 1200) {
                if (touch1 == 0) {
                    touch1 = matrix[4][6];
                    touch_x = 6;
                    touch_y = 4;
                } else {
                    touch2 = matrix[4][6];
                    touch_x2 = 6;
                    touch_y2 = 4;
                }
            } else if (event.getX() < 220 && event.getY() <= 1200 && event.getY() > 1000) {
                if (touch1 == 0) {
                    touch1 = matrix[5][0];
                    touch_x = 0;
                    touch_y = 5;
                } else {
                    touch2 = matrix[5][0];
                    touch_x2 = 0;
                    touch_y2 = 5;
                }
            } else if (event.getX() < 420 && event.getX() >= 220 && event.getY() <= 1200 && event.getY() > 1000) {
                if (touch1 == 0) {
                    touch1 = matrix[5][1];
                    touch_x = 1;
                    touch_y = 5;
                } else {
                    touch2 = matrix[5][1];
                    touch_x2 = 1;
                    touch_y2 = 5;
                }
            } else if (event.getX() < 620 && event.getX() >= 420 && event.getY() <= 1200 && event.getY() > 1000) {
                if (touch1 == 0) {
                    touch1 = matrix[5][2];
                    touch_x = 2;
                    touch_y = 5;
                } else {
                    touch2 = matrix[5][2];
                    touch_x2 = 2;
                    touch_y2 = 5;
                }
            } else if (event.getX() < 820 && event.getX() >= 620 && event.getY() <= 1200 && event.getY() > 1000) {
                if (touch1 == 0) {
                    touch1 = matrix[5][3];
                    touch_x = 3;
                    touch_y = 5;
                } else {
                    touch2 = matrix[5][3];
                    touch_x2 = 3;
                    touch_y2 = 5;
                }
            } else if (event.getX() < 1020 && event.getX() >= 820 && event.getY() <= 1200 && event.getY() > 1000) {
                if (touch1 == 0) {
                    touch1 = matrix[5][4];
                    touch_x = 4;
                    touch_y = 5;
                } else {
                    touch2 = matrix[5][4];
                    touch_x2 = 4;
                    touch_y2 = 5;
                }
            } else if (event.getX() < 1220 && event.getX() >= 1020 && event.getY() <= 1200 && event.getY() > 1000) {
                if (touch1 == 0) {
                    touch1 = matrix[5][5];
                    touch_x = 5;
                    touch_y = 5;
                } else {
                    touch2 = matrix[5][5];
                    touch_x2 = 5;
                    touch_y2 = 5;
                }
            } else if (event.getX() < 1420 && event.getX() >= 1220 && event.getY() <= 1200 && event.getY() > 1000) {
                if (touch1 == 0) {
                    touch1 = matrix[5][6];
                    touch_x = 6;
                    touch_y = 5;
                } else {
                    touch2 = matrix[5][6];
                    touch_x2 = 6;
                    touch_y2 = 5;
                }
            } else if (event.getX() < 220 && event.getY() <= 1000 && event.getY() > 800) {
                if (touch1 == 0) {
                    touch1 = matrix[6][0];
                    touch_x = 0;
                    touch_y = 6;
                } else {
                    touch2 = matrix[0][6];
                    touch_x2 = 0;
                    touch_y2 = 6;
                }
            } else if (event.getX() < 420 && event.getX() >= 220 && event.getY() <= 1000 && event.getY() > 800) {
                if (touch1 == 0) {
                    touch1 = matrix[6][1];
                    touch_x = 1;
                    touch_y = 6;
                } else {
                    touch2 = matrix[6][1];
                    touch_x2 = 1;
                    touch_y2 = 6;
                }
            } else if (event.getX() < 620 && event.getX() >= 420 && event.getY() <= 1000 && event.getY() > 800) {
                if (touch1 == 0) {
                    touch1 = matrix[6][2];
                    touch_x = 2;
                    touch_y = 6;
                } else {
                    touch2 = matrix[6][2];
                    touch_x2 = 2;
                    touch_y2 = 6;
                }
            } else if (event.getX() < 820 && event.getX() >= 620 && event.getY() <= 1800 && event.getY() > 800) {
                if (touch1 == 0) {
                    touch1 = matrix[6][3];
                    touch_x = 3;
                    touch_y = 6;
                } else {
                    touch2 = matrix[6][3];
                    touch_x2 = 3;
                    touch_y2 = 6;
                }
            } else if (event.getX() < 1020 && event.getX() >= 820 && event.getY() <= 1000 && event.getY() > 800) {
                if (touch1 == 0) {
                    touch1 = matrix[6][4];
                    touch_x = 4;
                    touch_y = 6;
                } else {
                    touch2 = matrix[6][4];
                    touch_x2 = 4;
                    touch_y2 = 6;
                }
            } else if (event.getX() < 1220 && event.getX() >= 1020 && event.getY() <= 1000 && event.getY() > 800) {
                if (touch1 == 0) {
                    touch1 = matrix[6][5];
                    touch_x = 5;
                    touch_y = 6;
                } else {
                    touch2 = matrix[6][5];
                    touch_x2 = 5;
                    touch_y2 = 6;
                }
            } else if (event.getX() < 1420 && event.getX() >= 1220 && event.getY() <= 1000 && event.getY() > 800) {
                if (touch1 == 0) {
                    touch1 = matrix[6][6];
                    touch_x = 6;
                    touch_y = 6;
                } else {
                    touch2 = matrix[6][6];
                    touch_x2 = 6;
                    touch_y2 = 6;
                }
            }


        }
        before_time = System.currentTimeMillis() - start_time;
        //Log.d("터치1",Integer.toString(touch1));
        //Log.d("터치2",Integer.toString(touch2));
        //Log.d("터치x",Integer.toString(touch_x));
        //Log.d("터치y",Integer.toString(touch_y));
        return true;
    }

}
