package com.example.card;

import android.util.Log;

public class ExampleThread extends Thread {
    private static final String TAG = "ExampleThread";
    private boolean stop;
    CardGame view;

    public ExampleThread(CardGame view) {
        this.view = view;
        this.stop = false;
    }

    public void run() {
        int second = 15;
        while (!stop) {
            second--;
            try {
                Thread.sleep(1000);
            }catch (InterruptedException e) {

            }

            if(second == 0){
                view.timeover = view.TIMEOVER;
                view.postInvalidate();
                break;
            }
        }
    }

    public void threadStop(boolean stop){
        this.stop = stop;
    }


}
