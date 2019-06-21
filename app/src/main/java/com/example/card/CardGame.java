package com.example.card;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.Random;

import java.util.Timer;
import java.util.TimerTask;

public class CardGame extends View {

    private TimerTask Buzzersecond;
    public TimerTask gamesecond;
    static int Gamecounter = 0;

    private final Handler handler = new Handler();
    public native int PiezoControl(int value);
    static int counter = 0;
    int PiezoData;
    public boolean StopSeg = false;

    public native String SegmentControl(int segcount);
    public native String SegmentClose(int segcount);
    public native String SegmentIOControl(int segvalue);
    public native int lifeLed(int left_num);
    static {System.loadLibrary("jniExample");}


    public static final int STATE_SHOW = 0;
    public static final int STATE_PLAY = 1;
    public static final int STATE_SEL1 = 2;
    public static final int STATE_SEL2 = 3;
    public static final int STATE_END = 4;
    public static final int STATE_NEW = 5;
    public static final int PLAYING = 0;
    public static final int TIMEOVER = 1;
    public static final int OPTION_LIFE = 0;
    public static final int OPTION_TIMER = 1;

    public boolean first_start = true;
    public int option = -1;
    public int btn_ranking = 0;

    public int period;
    public int background = 1;


    public int LEDcount;
    public int timeover;
    public int state;


    public int level;
    public int levelcount;//
    public int sleeping = 3;
    public int axis_x;
    public int axis_y;

    public int score = 0;

    Bitmap temp;
    Bitmap Front[];
    Bitmap New_Front[];
    Card MCard[][];
    CardThread thread;
    ExampleThread timer;
    SegmentTimer segTimer;

    public Card select1, select2;
    public int matchCount = 0;
    SoundPool SoundPool;

    int sound;
    Random rand;
    Random rand_card;
    MediaPlayer bgm;
    public int start, end;

    public CardGame(Context context){
        super(context);
        testStart();
        level = 0;
        levelcount = 1;

        PiezoData = 0;
        PiezoControl(PiezoData);

        axis_x = 4;
        axis_y = 1;

//        Background = BitmapFactory.decodeResource(getResources(), R.drawable.backspring);

//        Black = BitmapFactory.decodeResource(getResources(), R.drawable.card);

        Front = new Bitmap[16];
        New_Front = new Bitmap[16];

        Front[0] = BitmapFactory.decodeResource(getResources(), R.drawable.blackyellowsquare);
        Front[1] = BitmapFactory.decodeResource(getResources(), R.drawable.whitepinktri);
        Front[2] = BitmapFactory.decodeResource(getResources(), R.drawable.graybluecircle);
        Front[3] = BitmapFactory.decodeResource(getResources(), R.drawable.blackbluetri);
        Front[4] = BitmapFactory.decodeResource(getResources(), R.drawable.whiteyellowsquare);
        Front[5] = BitmapFactory.decodeResource(getResources(), R.drawable.graybluetri);
        Front[6] = BitmapFactory.decodeResource(getResources(), R.drawable.blackyellowcircle);
        Front[7] = BitmapFactory.decodeResource(getResources(), R.drawable.whiteyellowcircle);
        Front[8] = BitmapFactory.decodeResource(getResources(), R.drawable.graypinksquare);
        Front[9] = BitmapFactory.decodeResource(getResources(), R.drawable.whitepinksquare);
        Front[10] = BitmapFactory.decodeResource(getResources(), R.drawable.graybluesquare);
        Front[11] = BitmapFactory.decodeResource(getResources(), R.drawable.blackpinkcircle);
        Front[12] = BitmapFactory.decodeResource(getResources(), R.drawable.graypinkcircle);
        Front[13] = BitmapFactory.decodeResource(getResources(), R.drawable.blackbluesquare);
        Front[14] = BitmapFactory.decodeResource(getResources(), R.drawable.blackyellowtri);
        Front[15] = BitmapFactory.decodeResource(getResources(), R.drawable.grayyellowtri);

        for(int i = 0 ; i< 16 ; i++){
            New_Front[i] = Bitmap.createScaledBitmap(Front[i], 120, 120, true);
        }

        MCard = new Card[4][4];

        rand_card = new Random();


        for(int i = 0; i < 20; i++){
            int rd = rand_card.nextInt(15) ;
            temp = New_Front[0];
            New_Front[0] = New_Front[rd];
            New_Front[rd] = temp;
        }


        for(int y = 0 ; y < 4 ; y++){
            for(int x = 0; x < 4 ; x++){
                Point p = new Point(  x * 116 ,10 + y * 116);
                Rect rect = new Rect(p.x, p.y, p.x+New_Front[0].getWidth(), p.y+New_Front[0].getHeight());

                MCard[y][x] = new Card(Card.IMAGE_RED, rect);
            }
        }

        rand = new Random();

        state = STATE_SHOW;
        timeover = PLAYING;

        thread = new CardThread(this);
        thread.start();
    }
    public CardGame(Context context, AttributeSet attrs) {
        super(context, attrs);
        testStart();
        level = 0;
        levelcount = 1;
        PiezoData = 0;
        PiezoControl(PiezoData);

        axis_x = 4;
        axis_y = 1;

//        Background = BitmapFactory.decodeResource(getResources(), R.drawable.backspring);

//        Black = BitmapFactory.decodeResource(getResources(), R.drawable.card);

        Front = new Bitmap[16];
        New_Front = new Bitmap[16];

        Front[0] = BitmapFactory.decodeResource(getResources(), R.drawable.blackyellowsquare);
        Front[1] = BitmapFactory.decodeResource(getResources(), R.drawable.whitepinktri);
        Front[2] = BitmapFactory.decodeResource(getResources(), R.drawable.graybluecircle);
        Front[3] = BitmapFactory.decodeResource(getResources(), R.drawable.blackbluetri);
        Front[4] = BitmapFactory.decodeResource(getResources(), R.drawable.whiteyellowsquare);
        Front[5] = BitmapFactory.decodeResource(getResources(), R.drawable.graybluetri);
        Front[6] = BitmapFactory.decodeResource(getResources(), R.drawable.blackyellowcircle);
        Front[7] = BitmapFactory.decodeResource(getResources(), R.drawable.whiteyellowcircle);
        Front[8] = BitmapFactory.decodeResource(getResources(), R.drawable.graypinksquare);
        Front[9] = BitmapFactory.decodeResource(getResources(), R.drawable.whitepinksquare);
        Front[10] = BitmapFactory.decodeResource(getResources(), R.drawable.graybluesquare);
        Front[11] = BitmapFactory.decodeResource(getResources(), R.drawable.blackpinkcircle);
        Front[12] = BitmapFactory.decodeResource(getResources(), R.drawable.graypinkcircle);
        Front[13] = BitmapFactory.decodeResource(getResources(), R.drawable.blackbluesquare);
        Front[14] = BitmapFactory.decodeResource(getResources(), R.drawable.blackyellowtri);
        Front[15] = BitmapFactory.decodeResource(getResources(), R.drawable.grayyellowtri);

        for(int i = 0 ; i< 16 ; i++){
            New_Front[i] = Bitmap.createScaledBitmap(Front[i], 120, 120, true);
        }

        MCard = new Card[4][4];

        rand_card = new Random();


        for(int i = 0; i < 20; i++){
            int rd = rand_card.nextInt(15) ;
            temp = New_Front[0];
            New_Front[0] = New_Front[rd];
            New_Front[rd] = temp;
        }

        for(int y = 0 ; y < 4 ; y++){
            for(int x = 0; x < 4 ; x++){
                Point p = new Point(  x * 116 ,10 + y * 116);
                Rect rect = new Rect(p.x, p.y, p.x+New_Front[0].getWidth(), p.y+New_Front[0].getHeight());

                MCard[y][x] = new Card(Card.IMAGE_RED, rect);
            }
        }

        rand = new Random();

        state = STATE_SHOW;
        timeover = PLAYING;

        thread = new CardThread(this);
        thread.start();
    }

    public void testStart() {
        int scount = 0;

        Buzzersecond = new TimerTask() {

            @Override
            public void run() {
                counter++;
                Update();
            }
        };

        gamesecond = new TimerTask() {

            @Override
            public void run() {
                Gamecounter++;
                Update();
            }
        };

        Timer timerB = new Timer();
        Timer timerGame = new Timer();
        timerGame.schedule(gamesecond, 0, 100);
        timerB.schedule(Buzzersecond, 0, 300);
    }



    public void onDraw(Canvas canvas){
        lifeLed(LEDcount);
        switch(level % 4 + 1){
            case 1:
                background = 1;
//                Background = BitmapFactory.decodeResource(getResources(), R.drawable.backspring);
                break;
            case 2:
                background = 2;
//                Background = BitmapFactory.decodeResource(getResources(), R.drawable.backsummer);
                break;
            case 3:
                background = 3;
//                Background = BitmapFactory.decodeResource(getResources(), R.drawable.backfall);
                break;
            case 4:
                background = 4;
//                Background = BitmapFactory.decodeResource(getResources(), R.drawable.backwinter);
                break;
        }

        //canvas.drawBitmap(Background, 0 , 0, null);

        if(timeover == TIMEOVER){
            Paint paint = new Paint();
            paint.setTextSize(30);
            paint.setColor(Color.BLACK);
            canvas.drawText("GAMEOVER", 50, 200,paint);

            try{
                Thread.sleep(1000);
            }

            catch(Exception e){

            }

            return;
        }

        if(state == STATE_NEW){

            state = STATE_SHOW;
            timeover = PLAYING;

            thread = new CardThread(this);
            thread.start();

            return;
        }


        for(int y = 0 ; y < axis_y ; y++){
            for(int x = 0 ; x < axis_x ; x++){
                switch(MCard[y][x].state){
                    case Card.CARD_SHOW:
                    case Card.CARD_OPEN:
                    case Card.CARD_MATCHED:
                        canvas.drawBitmap(New_Front[MCard[y][x].color], MCard[y][x].rect.left, MCard[y][x].rect.top, null);
                        break;
                }
            }
        }


        if(state == STATE_END){
            int period = end - start;
            int min = (int)(period/60.0);
            int sec = (int)(period%60);
            Paint paint = new Paint();
            paint.setTextSize(150);
            paint.setColor(Color.BLACK);
//            canvas.drawText("경과시간 : "+min+"분 "+sec+"초", 10, 200,paint);

            if(option == OPTION_TIMER) {
                stopThread();
            }

            thread = new CardThread(this);
            thread.start();
        }

    }
    public void SetOption(int Option){
        option = Option;
    }
    public void Shuffle(){

        if(option == OPTION_TIMER) {

//            timer = new ExampleThread(this);
//            timer.start();

            segTimer = new SegmentTimer(this);
            segTimer.setDaemon(true);
            //StopSeg = false;
            segTimer.start();

        }

        for(int y = 0; y <axis_y; y++){
            for(int x = 0 ; x < axis_x ; x++){
                MCard[y][x].color = (x*axis_y + y) % (axis_x*axis_y)/2;
            }
        }

        for(int r = 0 ; r < 10 ; r++){
            int x= rand.nextInt(axis_x), y = rand.nextInt(axis_y);
            int temp = MCard[0][0].color;
            MCard[0][0].color = MCard[y][x].color;
            MCard[y][x].color = temp;
        }


    }

    public boolean SimilarToOnTouchEvent(int pointx, int pointy){
        if (state == STATE_SHOW || state == STATE_SEL2) {
            return true;
        }

        if (state == STATE_END || state == STATE_NEW) {
            return true;
        }

        int px = pointx;
        int py = pointy;

        for (int y = 0; y < axis_y; y++) {
            for (int x = 0; x < axis_x; x++) {

                if (MCard[y][x].rect.contains(px, py)) {

                    if (MCard[y][x].state != Card.CARD_CLOSE){
                        return true;
                    }

                    switch (state) {
                        case STATE_PLAY:
                            select1 = MCard[y][x];
                            select1.state = Card.CARD_OPEN;
                            state = STATE_SEL1;
                            break;

                        case STATE_SEL1:
                            select2 = MCard[y][x];
                            select2.state = Card.CARD_OPEN;
                            state = STATE_SEL2;
                            break;

                        default:
                            return true;


                    }

                }
            }
        }

        invalidate();
        thread = new CardThread(this);
        thread.start();

        return true;
    }

    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        if (event.getAction() != MotionEvent.ACTION_DOWN) {
            return true;
        }

        if (state == STATE_SHOW || state == STATE_SEL2) {
            return true;
        }

        if (state == STATE_END || state == STATE_NEW) {
            return true;
        }

        int px = (int) event.getX();
        int py = (int) event.getY();

        for (int y = 0; y < axis_y; y++) {
            for (int x = 0; x < axis_x; x++) {

                if (MCard[y][x].rect.contains(px, py)) {

                    if (MCard[y][x].state != Card.CARD_CLOSE){
                        return true;
                    }

                    switch (state) {
                        case STATE_PLAY:
                            select1 = MCard[y][x];
                            select1.state = Card.CARD_OPEN;
                            state = STATE_SEL1;
                            break;

                        case STATE_SEL1:
                            select2 = MCard[y][x];
                            select2.state = Card.CARD_OPEN;
                            state = STATE_SEL2;
                            break;

                        default:
                            return true;


                    }

                }
            }
        }

        invalidate();
        thread = new CardThread(this);
        thread.start();
        return true;
    }

    protected void Update() {
        Runnable updater = new Runnable() {
            public void run() {
            }
        };
        handler.post(updater);
    }
    public class SegmentTimer extends Thread {
        private boolean stop;
        CardGame view;

        int segSecond = 15;
        public SegmentTimer(CardGame view) {
            this.view = view;
            this.stop = false;
        }

        public void run() {
            while (!stop) {
                try {
                        SegmentControl(segSecond);
                        Thread.sleep(100);
                        SegmentClose(0);
                }catch (InterruptedException e) {

                }
                segSecond--;
                if(segSecond == 0){
                    btn_ranking = 1;
                    view.timeover = view.TIMEOVER;
                    view.postInvalidate();
                    SegmentClose(0);
                    break;
                }
            }
        }

        public void threadStop(boolean stop){
            this.stop = stop;
        }

    }
    public void stopThread(){
        segTimer.threadStop(true);
        SegmentClose(0);
    }
}
