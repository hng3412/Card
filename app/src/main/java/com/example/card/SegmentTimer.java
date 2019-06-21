package com.example.card;

import android.util.Log;
/*
public class SegmentTimer extends Thread {
    private static final String TAG = "ExampleThread";
    private boolean stop;
    CardGame view;

    int segSecond = 15;
    public SegmentTimer(CardGame view) {
        this.view = view;
        this.stop = false;
    }

    public void run() {
        while (!stop) {
            // Countdown
            view.SegmentIOControl(0);
            for (int i = 0; i < 70 ; i++) {
                view.SegmentControl(segSecond);
            }
            segSecond--;
            Log.d("left: ", Integer.toString(segSecond));
            if(segSecond == 0){
                view.timeover = view.TIMEOVER;
                view.postInvalidate();
                break;
            }
        }
        Log.d("경과된 시간 : ", "종료");
    }

    public void threadStop(boolean stop){
        this.stop = stop;
    }

}
*/