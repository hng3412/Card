package com.example.card;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.example.card.databinding.ActivityDigitBinding;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.view.KeyEvent.KEYCODE_1;

public class DigitActivity extends AppCompatActivity {
    BackThreadSeg segThread = new BackThreadSeg();
    int Segcount = 0;
    public native int digitLed(int left_num);
    public native int digitBuzzer(int value);
    public native String digitSegment(int segcount);
    public native String digitIOSegment(int segcount);
    public native String digitSegmentClose(int segcount);
    public native int digitSwOpen();
    public native int digitSwClose();
    public native int digitSwValue();
    public native String digitDotMatrix(String input);
    boolean segStop = false;

    DotBackThread Dotthread = new DotBackThread();
    String Dotvalue;
    boolean Dotstart = false;
    boolean Dotrestart = false;
    boolean Dotalive = true;
    String Dotresult = new String();

    static {
        System.loadLibrary("jniExample");
    }
    int Life = 5;

    Vector<Integer> _1to16, _17to32;
    ActivityDigitBinding binding;
    ItemAdapter adapter;
    Observable<Long> duration;
    Disposable disposable;
    int now;
    Handler handler;

    int click_x = 0;
    int click_y = 0;


    public int Switch_option;

    /*
     * Switch_option = 0 -> life
     * Switch_option = 1 -> timer
     * */

    //buzzer
    int buzzerData;
    static int buzzerCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_digit);
        handler = new Handler();
        init();

        Dotthread.setDaemon(true);
        Dotthread.start();
        Dotvalue = " ";

        Intent intent = getIntent();
        Switch_option = intent.getExtras().getInt("switch");

        if (Switch_option == 0) {
            //life
            digitLed(Life);
            Segcount=0;
            Dotstart=true;
            Dotalive = true;
            segStop = true;
        } else if (Switch_option == 1) {
            digitLed(0);
            Segcount=0;

            segThread.setDaemon(true);
            segThread.start();

            Dotstart=true;
            Dotalive = true;
            segStop = false;
            //timer
        } else {
            //nothing
            digitLed(0);
            Segcount=0;
            Dotstart=false;
            Dotalive = false;
            segStop = true;
        }

        binding.retryBtn.setOnClickListener(view -> {
            digitLed(0);
            Dotvalue = " ";
            Dotstart=false;
            segStop = true;
            Segcount=0;
            segStop = true;
            stop();
            digitSegmentClose(0);
            Intent intentMain = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intentMain);
            finish();
        });
        //buzzer


        Timer buzzerTimer = new Timer();
        buzzerTimer.schedule(tt, 0, 300);

        buzzerData = 0;
        digitBuzzer(buzzerData);

    }

    class DotBackThread extends Thread {
        public void run() {
            while (Dotalive) {
                if (!Dotstart) {
                    // do nothing
                }
                else {
                    if (Dotvalue.length() > 50) {
                    } else {
//						restart = false;
                        int i, j, ch;
                        char buf[] = new char[100];

                        buf = Dotvalue.toCharArray();

                        Dotresult = "00000000000000000000";

                        for (i = 0; i < Dotvalue.length(); i++) {
                            ch = Integer.valueOf(buf[i]);
                            ch -= 0x20;

                            // copy
                            for (j = 0; j < 5; j++) {
                                String str = new String();
                                str = Integer.toHexString((font[ch][j]));
                                if (str.length() < 2)
                                    Dotresult += "0";
                                Dotresult += str;
                            }
                            Dotresult += "00";
                        }
                        Dotresult += "00000000000000000000";
                        digitDotMatrix(Dotresult.substring(2 * 11, 2 * 11 + 20));

                    }
                    digitDotMatrix("00000000000000000000");
                }
            }
        }
    }

    private void timer() {
        duration = Observable.interval(10, TimeUnit.MILLISECONDS)
                .map(milli -> milli += 1L);
        disposable = duration.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    long sec = s / 100;
                    long milli = s % 100;
                    //runOnUiThread(() -> binding.timeTxtView.setText(Segcount));
                });
    }

    private void stop() {
        CompositeDisposable disposable = new CompositeDisposable();
        disposable.add(this.disposable);
        disposable.dispose();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stop();
    }

    private void init() {
        timer();
        binding.gridView.removeOnItemTouchListener(select);
        now = 1;
        _1to16 = new Vector<>();
        _17to32 = new Vector<>();
        binding.gridView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int length = binding.gridView.getWidth() / 4 - 8;
                adapter.setLength(length, length);
                adapter.notifyDataSetChanged();

                binding.gridView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        for (int i = 1; i <= 16; i++) {
            _1to16.add(i);
            _17to32.add(i + 16);
        }
        binding.gridView.setLayoutManager(new GridLayoutManager(this, 4));
        adapter = new ItemAdapter(this);
        binding.gridView.setAdapter(adapter);
        for (int i = 1; i <= 16; i++) {
            int rand = (int) (Math.random() * _1to16.size());
            adapter.init1to16(_1to16.get(rand));
            _1to16.remove(rand);
            adapter.notifyDataSetChanged();
        }
        binding.gridView.addOnItemTouchListener(select);
    }


    class BackThreadSeg extends Thread {
        public void run() {
            digitIOSegment(0);
            while (!segStop){
                    // Countdown
                    for (int i = 0; i < 2 ; i++) {
                        digitSegment(Segcount);
                        digitSegmentClose(0);
                    }
                    Segcount++;
                    // flag = 0;
            }
        }
    }

    private RecyclerView.OnItemTouchListener select = new RecyclerView.OnItemTouchListener() {
        @Override
        public boolean onInterceptTouchEvent(@NonNull RecyclerView parent, @NonNull MotionEvent evt) {
            try {
                switch (evt.getAction()) {
                    case MotionEvent.ACTION_UP:
                        //button click
                        Button child = (Button) parent.findChildViewUnder(evt.getX(), evt.getY());
                        if (child != null) {
                            // 버튼 번호 가져오기
                            int selected = Integer.parseInt(child.getText().toString());
                            if (selected == now) {
                                String temp0 = " ";
                                if(selected<10){
                                    Dotvalue = temp0.concat(Integer.toString(selected));
                                }else {
                                    Dotvalue = Integer.toString(selected);
                                }
                                //맞았을 때!!!
                                int position = parent.getChildAdapterPosition(child);
                                if (selected >= 17 && selected == now)
                                    adapter.setUpVisible(position);
                                now++;
                                if (_17to32 != null) {
                                    int rand = (int) (Math.random() * _17to32.size());
                                    adapter.update17to32(position, _17to32.get(rand));
                                    _17to32.remove(rand);
                                    if (_17to32.size() == 0) _17to32 = null;
                                }
                                adapter.notifyItemChanged(position);
                            } else {
                                buzzerData = 7;
                                digitBuzzer(buzzerData);
                                buzzerCount = 0;
                                while (buzzerCount < 1) {

                                }
                                buzzerData = 0;
                                digitBuzzer(buzzerData);
                                if (Switch_option == 0) {
                                    Life--;
                                    digitLed(Life);

                                    if (Life == 0) {
                                        //게임 오버
                                        segStop = true;
                                        stop();
                                    }
                                }
                                //틀렸을 때
                                Toast.makeText(DigitActivity.this, "다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                            }
                            if (now == 33) {
                                segStop = true;
                                stop();
                            }
                        }
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean b) {

        }
    };

    TimerTask tt = new TimerTask(){
        public void run(){
            buzzerCount++;
        }

    };




































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
