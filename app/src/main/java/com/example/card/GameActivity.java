package com.example.card;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class GameActivity extends AppCompatActivity {
    ImageButton btn_newstart;
    ImageButton btn_ranking;

    EventHandler kb_eventHandler;
    KBThread kb_thread;
    boolean		kb_Start;

    CardGame CG;
    public int Switch_option;
    LinearLayout layout;
    Context cont;

    int previousback = 0;
    int newstart = 0;

    String timeover;
    public int min;
    public int sec;
    public int score;

    public int ranking = 0;
    public boolean KBstop = false;

    //Dot
    public native String CardDotMatrix(String input);
    static {System.loadLibrary("jniExample");}


    CardDotBackThread CardDotthread = new CardDotBackThread();
    String CardDotvalue;
    boolean CardDotstart = false;
    boolean CardDotalive = true;
    String CardDotresult = new String();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //KB//

        kb_Start = true;
        kb_eventHandler = new EventHandler();
        kb_thread = new KBThread();
        kb_thread.start();
/*
        CardDotvalue = " ";
        CardDotstart=true;
        CardDotalive = true;
        CardDotthread.setDaemon(true);
        CardDotthread.start();
*/


        cont = this;

        Intent intent = getIntent();
        Switch_option = intent.getExtras().getInt("switch");

        CG = findViewById(R.id.gameView);
        CG.SetOption(Switch_option);

        layout = findViewById(R.id.Layout_game);

        btn_newstart = findViewById(R.id.btn_new);
        btn_ranking = findViewById(R.id.btn_rank);

        btn_newstart.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                newstart = 1;
                //KBstop = true;
                if(Switch_option == 1)
                    CG.stopThread();

                for(int i=0;i<16;i++) {
                    CG.Front[i].recycle();
                }

                for(int i=0;i<16;i++) {
                    CG.New_Front[i].recycle();
                }
                CG.temp.recycle();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        BackThread thread = new BackThread();
        thread.setDaemon(true);
        thread.start();

        BackgroundThread backthread = new BackgroundThread();
        backthread.setDaemon(true);
        backthread.start();



        btn_ranking.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                ranking=1;
                //KBstop = true;
                Intent intent = new Intent(getApplicationContext(), MakeIDActivity.class);
                intent.putExtra("level", CG.level + 1);
                min = (int) (CG.period / 60.0);
                sec = (int) (CG.period % 60);

                timeover = Integer.toString(min) + "분 " + Integer.toString(sec) + "초";
                score = CG.score;
                intent.putExtra("time", timeover);
                intent.putExtra("score", score);

                startActivity(intent);
                finish();
            }
        });
    }

    class BackgroundThread extends Thread{
        public void run() {
            while(true){
                if(CG.background == previousback)
                    continue;

                else {
                    previousback = CG.background;
                    switch (CG.background) {
                        case 1:
                            handler.sendEmptyMessage(0);
                            break;
                        case 2:
                            handler.sendEmptyMessage(1);
                            break;
                        case 3:
                            handler.sendEmptyMessage(2);
                            break;
                        case 4:
                            handler.sendEmptyMessage(3);
                            break;
                    }
                }

                if(newstart==1){
                    if(Switch_option == 1)
                        CG.stopThread();
                    break;
                }

                if(ranking ==1)
                    break;

            } // end while
        } // end run()
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 0:
                    layout.setBackground(ContextCompat.getDrawable(cont,R.drawable.m1));
                    break;
                case 1:
                    layout.setBackground(ContextCompat.getDrawable(cont,R.drawable.m2));
                    break;
                case 2:
                    layout.setBackground(ContextCompat.getDrawable(cont,R.drawable.m3));
                    break;
                case 3:
                    layout.setBackground(ContextCompat.getDrawable(cont,R.drawable.m4));
                    break;
            }
        }
    };

    class BackThread extends Thread{
        public void run() {
            while(true){
                if(CG.btn_ranking == 1) {
                    back_handler.sendEmptyMessage(0);

                    if(Switch_option == 1) {
                        CG.stopThread();
                    }
                }
                if(newstart==1){
                    break;
                }

                if(ranking ==1)
                    break;

            } // end while
        } // end run()
    }


    Handler back_handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 0){   // Message id 가 0 이면
                btn_ranking.setVisibility(View.VISIBLE);
            }
        }
    };



    //Dot


    class CardDotBackThread extends Thread {
        public void run() {
            while (CardDotalive) {
                if (!CardDotstart) {
                    // do nothing
                }
                else {
                    if (CardDotvalue.length() > 50) {
                    } else {
//						restart = false;
                        int i, j, ch;
                        char buf[] = new char[100];

                        buf = CardDotvalue.toCharArray();

                        CardDotresult = "00000000000000000000";

                        for (i = 0; i < CardDotvalue.length(); i++) {
                            ch = Integer.valueOf(buf[i]);
                            ch -= 0x20;

                            // copy
                            for (j = 0; j < 5; j++) {
                                String str = new String();
                                str = Integer.toHexString((font[ch][j]));
                                if (str.length() < 2)
                                    CardDotresult += "0";
                                CardDotresult += str;
                            }
                            CardDotresult += "00";
                        }
                        CardDotresult += "00000000000000000000";
                        CardDotMatrix(CardDotresult.substring(2 * 11, 2 * 11 + 20));

                    }
                    CardDotMatrix("00000000000000000000");
                }
            }
        }
    }


















    ///////key pad///////
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == event.KEYCODE_1){
            Log.d("KB","1");
            CG.SimilarToOnTouchEvent(50,80);
            return true;
        }

        if(keyCode == event.KEYCODE_2){
            Log.d("KB","2");
            CG.SimilarToOnTouchEvent(160,80);
            return true;
        }

        if(keyCode == event.KEYCODE_3){
            Log.d("KB","3");
            CG.SimilarToOnTouchEvent(270,80);
            return true;
        }

        if(keyCode == event.KEYCODE_4){
            Log.d("KB","4");
            CG.SimilarToOnTouchEvent(380,80);
            return true;
        }

        if(keyCode == event.KEYCODE_5){
            Log.d("KB","5");
            CG.SimilarToOnTouchEvent(50,190);
            return true;
        }

        if(keyCode == event.KEYCODE_6){
            Log.d("KB","6");
            CG.SimilarToOnTouchEvent(160,190);
            return true;
        }

        if(keyCode == event.KEYCODE_7){
            Log.d("KB","7");
            CG.SimilarToOnTouchEvent(270,190);
            return true;
        }

        if(keyCode == event.KEYCODE_8){
            Log.d("KB","8");
            CG.SimilarToOnTouchEvent(380,190);
            return true;
        }

        if(keyCode == event.KEYCODE_9){
            Log.d("KB","9");
            CG.SimilarToOnTouchEvent(50,300);
            return true;
        }

        if(keyCode == event.KEYCODE_P){
            Log.d("KB","P");
            CG.SimilarToOnTouchEvent(160,300);
            return true;
        }

        if(keyCode == event.KEYCODE_O){
            Log.d("KB","P");
            CG.SimilarToOnTouchEvent(270,300);
            return true;
        }

        if(keyCode == event.KEYCODE_Q){
            Log.d("KB","P");
            CG.SimilarToOnTouchEvent(380,300);
            return true;
        }

        if(keyCode == event.KEYCODE_R){
            Log.d("KB","P");
            CG.SimilarToOnTouchEvent(50,410);
            return true;
        }

        if(keyCode == event.KEYCODE_T){
            Log.d("KB","P");
            CG.SimilarToOnTouchEvent(160,410);
            return true;
        }

        if(keyCode == event.KEYCODE_Y){
            Log.d("KB","P");
            CG.SimilarToOnTouchEvent(270,410);
            return true;
        }

        if(keyCode == event.KEYCODE_W){
            Log.d("KB","P");
            CG.SimilarToOnTouchEvent(380,410);
            return true;
        }
        if(keyCode == event.KEYCODE_BACK) {
            Log.d("KB","BACK");
            return true;
        }

        return false;
        // 이런식으로 처리를 하게 되면 시스템 백 버튼을 누르게 되어도 바탕화면으로 나가지지않고
        // 다른 기능을 실행시킬 수 있도록 시스템 버튼을 제어 할수 있다.
    }

    public class EventHandler extends Handler
    {
        EventHandler() {}
        public void handleMessage(Message msg)
        {
            try
            {
                if(msg.what==1)
                {
                }
            }
            catch(Exception e) {}

            return;
        }
    }
    class KBThread extends Thread
    {
        public void run()
        {
            try
            {
                while(!KBstop)
                {
                    if(kb_Start){
                        Thread.sleep(100);
                    } else Thread.sleep(1000);
                }
            }
            catch (Exception e)
            {
            }
        }// end of run
    } // end of myThread


    //font//

    public int font[][] = { /* 5x7 ASCII character font */
            { 0x00, 0x00, 0x00, 0x00, 0x00 }, /* 0x20 space */
            { 0x00, 0x00, 0x4f, 0x00, 0x00 }, /* 0x21 ! */
            { 0x00, 0x07, 0x00, 0x07, 0x00 }, /* 0x22 " */
            { 0x14, 0x7f, 0x14, 0x7f, 0x14 }, /* 0x23 # */
            { 0x24, 0x2a, 0x7f, 0x2a, 0x12 }, /* 0x24 $ */
            { 0x23, 0x13, 0x08, 0x64, 0x62 }, /* 0x25 % */
            { 0x36, 0x49, 0x55, 0x22, 0x50 }, /* 0x26 & */
            { 0x00, 0x05, 0x03, 0x00, 0x00 }, /* 0x27 ' */
            { 0x00, 0x1c, 0x22, 0x41, 0x00 }, /* 0x28 ( */
            { 0x00, 0x41, 0x22, 0x1c, 0x00 }, /* 0x29 ) */
            { 0x14, 0x08, 0x3e, 0x08, 0x14 }, /* 0x2a * */
            { 0x08, 0x08, 0x3e, 0x08, 0x08 }, /* 0x2b + */
            { 0x00, 0x50, 0x30, 0x00, 0x00 }, /* 0x2c , */
            { 0x08, 0x08, 0x08, 0x08, 0x08 }, /* 0x2d - */
            { 0x00, 0x60, 0x60, 0x00, 0x00 }, /* 0x2e . */
            { 0x20, 0x10, 0x08, 0x04, 0x02 }, /* 0x2f / */
            { 0x3e, 0x51, 0x49, 0x45, 0x3e }, /* 0x30 0 */
            { 0x00, 0x42, 0x7f, 0x40, 0x00 }, /* 0x31 1 */
            { 0x42, 0x61, 0x51, 0x49, 0x46 }, /* 0x32 2 */
            { 0x21, 0x41, 0x45, 0x4b, 0x31 }, /* 0x33 3 */
            { 0x18, 0x14, 0x12, 0x7f, 0x10 }, /* 0x34 4 */
            { 0x27, 0x45, 0x45, 0x45, 0x39 }, /* 0x35 5 */
            { 0x3c, 0x4a, 0x49, 0x49, 0x30 }, /* 0x36 6 */
            { 0x01, 0x71, 0x09, 0x05, 0x03 }, /* 0x37 7 */
            { 0x36, 0x49, 0x49, 0x49, 0x36 }, /* 0x38 8 */
            { 0x06, 0x49, 0x49, 0x29, 0x1e }, /* 0x39 9 */
            { 0x00, 0x36, 0x36, 0x00, 0x00 }, /* 0x3a : */
            { 0x00, 0x56, 0x36, 0x00, 0x00 }, /* 0x3b ; */
            { 0x08, 0x14, 0x22, 0x41, 0x00 }, /* 0x3c < */
            { 0x14, 0x14, 0x14, 0x14, 0x14 }, /* 0x3d = */
            { 0x00, 0x41, 0x22, 0x14, 0x08 }, /* 0x3e > */
            { 0x02, 0x01, 0x51, 0x09, 0x06 }, /* 0x3f ? */
            { 0x32, 0x49, 0x79, 0x41, 0x3e }, /* 0x40 @ */
            { 0x7e, 0x11, 0x11, 0x11, 0x7e }, /* 0x41 A */
            { 0x7f, 0x49, 0x49, 0x49, 0x36 }, /* 0x42 B */
            { 0x3e, 0x41, 0x41, 0x41, 0x22 }, /* 0x43 C */
            { 0x7f, 0x41, 0x41, 0x22, 0x1c }, /* 0x44 D */
            { 0x7f, 0x49, 0x49, 0x49, 0x41 }, /* 0x45 E */
            { 0x7f, 0x09, 0x09, 0x09, 0x01 }, /* 0x46 F */
            { 0x3e, 0x41, 0x49, 0x49, 0x7a }, /* 0x47 G */
            { 0x7f, 0x08, 0x08, 0x08, 0x7f }, /* 0x48 H */
            { 0x00, 0x41, 0x7f, 0x41, 0x00 }, /* 0x49 I */
            { 0x20, 0x40, 0x41, 0x3f, 0x01 }, /* 0x4a J */
            { 0x7f, 0x08, 0x14, 0x22, 0x41 }, /* 0x4b K */
            { 0x7f, 0x40, 0x40, 0x40, 0x40 }, /* 0x4c L */
            { 0x7f, 0x02, 0x0c, 0x02, 0x7f }, /* 0x4d M */
            { 0x7f, 0x04, 0x08, 0x10, 0x7f }, /* 0x4e N */
            { 0x3e, 0x41, 0x41, 0x41, 0x3e }, /* 0x4f O */
            { 0x7f, 0x09, 0x09, 0x09, 0x06 }, /* 0x50 P */
            { 0x3e, 0x41, 0x51, 0x21, 0x5e }, /* 0x51 Q */
            { 0x7f, 0x09, 0x19, 0x29, 0x46 }, /* 0x52 R */
            { 0x26, 0x49, 0x49, 0x49, 0x32 }, /* 0x53 S */
            { 0x01, 0x01, 0x7f, 0x01, 0x01 }, /* 0x54 T */
            { 0x3f, 0x40, 0x40, 0x40, 0x3f }, /* 0x55 U */
            { 0x1f, 0x20, 0x40, 0x20, 0x1f }, /* 0x56 V */
            { 0x3f, 0x40, 0x38, 0x40, 0x3f }, /* 0x57 W */
            { 0x63, 0x14, 0x08, 0x14, 0x63 }, /* 0x58 X */
            { 0x07, 0x08, 0x70, 0x08, 0x07 }, /* 0x59 Y */
            { 0x61, 0x51, 0x49, 0x45, 0x43 }, /* 0x5a Z */
            { 0x00, 0x7f, 0x41, 0x41, 0x00 }, /* 0x5b [ */
            { 0x02, 0x04, 0x08, 0x10, 0x20 }, /* 0x5c \ */
            { 0x00, 0x41, 0x41, 0x7f, 0x00 }, /* 0x5d ] */
            { 0x04, 0x02, 0x01, 0x02, 0x04 }, /* 0x5e ^ */
            { 0x40, 0x40, 0x40, 0x40, 0x40 }, /* 0x5f _ */
            { 0x00, 0x01, 0x02, 0x04, 0x00 }, /* 0x60 ` */
            { 0x20, 0x54, 0x54, 0x54, 0x78 }, /* 0x61 a */
            { 0x7f, 0x48, 0x44, 0x44, 0x38 }, /* 0x62 b */
            { 0x38, 0x44, 0x44, 0x44, 0x20 }, /* 0x63 c */
            { 0x38, 0x44, 0x44, 0x48, 0x7f }, /* 0x64 d */
            { 0x38, 0x54, 0x54, 0x54, 0x18 }, /* 0x65 e */
            { 0x08, 0x7e, 0x09, 0x01, 0x02 }, /* 0x66 f */
            { 0x0c, 0x52, 0x52, 0x52, 0x3e }, /* 0x67 g */
            { 0x7f, 0x08, 0x04, 0x04, 0x78 }, /* 0x68 h */
            { 0x00, 0x04, 0x7d, 0x00, 0x00 }, /* 0x69 i */
            { 0x20, 0x40, 0x44, 0x3d, 0x00 }, /* 0x6a j */
            { 0x7f, 0x10, 0x28, 0x44, 0x00 }, /* 0x6b k */
            { 0x00, 0x41, 0x7f, 0x40, 0x00 }, /* 0x6c l */
            { 0x7c, 0x04, 0x18, 0x04, 0x7c }, /* 0x6d m */
            { 0x7c, 0x08, 0x04, 0x04, 0x78 }, /* 0x6e n */
            { 0x38, 0x44, 0x44, 0x44, 0x38 }, /* 0x6f o */
            { 0x7c, 0x14, 0x14, 0x14, 0x08 }, /* 0x70 p */
            { 0x08, 0x14, 0x14, 0x18, 0x7c }, /* 0x71 q */
            { 0x7c, 0x08, 0x04, 0x04, 0x08 }, /* 0x72 r */
            { 0x48, 0x54, 0x54, 0x54, 0x20 }, /* 0x73 s */
            { 0x04, 0x3f, 0x44, 0x40, 0x20 }, /* 0x74 t */
            { 0x3c, 0x40, 0x40, 0x20, 0x7c }, /* 0x75 u */
            { 0x1c, 0x20, 0x40, 0x20, 0x1c }, /* 0x76 v */
            { 0x3c, 0x40, 0x30, 0x40, 0x3c }, /* 0x77 w */
            { 0x44, 0x28, 0x10, 0x28, 0x44 }, /* 0x78 x */
            { 0x0c, 0x50, 0x50, 0x50, 0x3c }, /* 0x79 y */
            { 0x44, 0x64, 0x54, 0x4c, 0x44 }, /* 0x7a z */
            { 0x00, 0x08, 0x36, 0x41, 0x00 }, /* 0x7b { */
            { 0x00, 0x00, 0x77, 0x00, 0x00 }, /* 0x7c | */
            { 0x00, 0x41, 0x36, 0x08, 0x00 }, /* 0x7d } */
            { 0x08, 0x04, 0x08, 0x10, 0x08 } }; /* 0x7e ~ */
}