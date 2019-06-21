package com.example.card;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    public native int swOpen();
    public native int swClose();
    public native int swGetvalue();

    KBEventHandler 			kb_eventHandler;
    KBThread	kb_thread;
    boolean		kb_Start;

    static {System.loadLibrary("jniExample");}


    EventHandler m_eventHandler;
    MyThread	m_thread;
    boolean	m_Start;
    int  NewValue;

    int mode = 0;
    public int selected =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        kb_Start = true;
        kb_eventHandler = new KBEventHandler();
        kb_thread = new KBThread();
        kb_thread.start();

        m_Start = false;

        swOpen();

        m_eventHandler = new EventHandler();
        m_thread = new MyThread();
        m_thread.start();

        UpdateValue();

        ImageButton btn = (ImageButton) findViewById(R.id.btn_card);
        ImageButton btn_dig = (ImageButton) findViewById(R.id.btn_digit);
        RadioGroup rg = (RadioGroup)findViewById(R.id.radiogroup);

        btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                intent.putExtra("switch", selected);
                startActivity(intent);
                finish();
            }
        });

        btn_dig.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), DigitActivity.class);
                intent.putExtra("switch", selected);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        m_Start = false;
        swClose();
    }

    @Override
    public void onResume() {
        m_Start = true;
        super.onResume();
    }

    @Override
    public void onPause() {
        m_Start = false;
        super.onPause();
    }

    public void UpdateValue() {
        NewValue = swGetvalue();
        {
            if ((NewValue & 0x10) == 0x10) {
                mode = 0;
                selected = 0;
            } else {
            }
            if ((NewValue & 0x20) == 0x20) {
                mode = 1;
                selected = 1;
            } else {
            }
        }
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
                    UpdateValue();
                }
            }
            catch(Exception e) {}

            return;
        }
    }
    class MyThread extends Thread
    {
        public void run()
        {
            try
            {
                while(true)
                {
                    if(m_Start){
                        Message msg1 = m_eventHandler.obtainMessage();
                        msg1.what= 1;
                        m_eventHandler.sendMessage(msg1);
                        Thread.sleep(100);
                    } else Thread.sleep(1000);
                }
            }
            catch (Exception e)
            {
//            	System.out.println(e);
            }
        }// end of run
    } // end of myThread

    /////////////////////////////////////////////////key board////////////////////////////////////////////

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);

        Log.d("KB","event");
        if(keyCode == KeyEvent.KEYCODE_1){
            //dipsw11.setChecked(true);
            Log.d("KB","1");
            return super.onKeyDown(keyCode, event);
        }

        if(keyCode == KeyEvent.KEYCODE_2){
            //dipsw12.setChecked(true);
            Log.d("KB","2");
            return super.onKeyDown(keyCode, event);
        }

        if(keyCode == event.KEYCODE_3){
            //dipsw13.setChecked(true);
            Log.d("KB","3");
            return super.onKeyDown(keyCode, event);
        }

        if(keyCode == KeyEvent.KEYCODE_4){
            //dipsw14.setChecked(true);
            Log.d("KB","4");
            return super.onKeyDown(keyCode, event);
        }
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            Log.d("KB","back");
            return super.onKeyDown(keyCode, event);

        }
        else{
            Log.d("KB","false");
            return super.onKeyDown(keyCode, event);
        }


        //return true;
        // �씠�윴�떇�쑝濡� 泥섎━瑜� �븯寃� �릺硫� �떆�뒪�뀥 諛� 踰꾪듉�쓣 �늻瑜닿쾶 �릺�뼱�룄 諛뷀깢�솕硫댁쑝濡� �굹媛�吏�吏��븡怨�
        // �떎瑜� 湲곕뒫�쓣 �떎�뻾�떆�궗 �닔 �엳�룄濡� �떆�뒪�뀥 踰꾪듉�쓣 �젣�뼱 �븷�닔 �엳�떎.
    }

    public class KBEventHandler extends Handler
    {
        KBEventHandler() {}
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
                while(true)
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


    //////////////////////////////////////////////////////////////////////////////////////////////////////
}
