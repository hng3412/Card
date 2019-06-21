package com.example.card;


import android.graphics.Bitmap;
import android.util.Log;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import java.util.Random;
import java.util.Timer;

import java.util.Timer;
import java.util.TimerTask;

public class CardThread extends Thread{
    CardGame view;

    public CardThread(CardGame view){
        this.view = view;
    }

    public void run(){
        switch (view.state){
            case CardGame.STATE_SHOW:
                run_show();
                break;

            case CardGame.STATE_SEL2:
                run_sel2();
                break;

            case CardGame.STATE_END:
                run_new();
                break;
        }

    }

    public void run_show(){
        while(view.option == -1) { }

        if(view.first_start == true){

            if(view.option == view.OPTION_LIFE)
                view.LEDcount = 5;

            view.first_start = false;
        }
        view.state = CardGame.STATE_PLAY;
        view.matchCount = 0;

        for(int y = 0; y < view.axis_y ; y++){
            for(int x = 0; x< view.axis_x ; x++){
                view.MCard[y][x].state = Card.CARD_SHOW;
            }
        }

        view.Shuffle();

        view.postInvalidate();

        int sleeping = view.sleeping;

        //여기서부터 진행이 안됨

        try{
            Thread.sleep(sleeping*1000);
            //카드 나오고 기다리는 시간
        }
        catch (InterruptedException e){
        }

        for(int y = 0 ; y < view.axis_y ; y++){
            for(int x = 0; x < view.axis_x ; x++){
                view.MCard[y][x].state = Card.CARD_CLOSE;
            }
        }


        view.start = (int)(System.currentTimeMillis()/1000);
        view.postInvalidate();
    }

    public void run_sel2(){


        if(view.select1.color == view.select2.color){
            //score update!!!!
            view.select1.state = Card.CARD_MATCHED;
            view.select2.state = Card.CARD_MATCHED;
            view.matchCount = view.matchCount+2;

            view.score = view.score + view.level + 1;
        }

        else{

            try{
                Thread.sleep(500);
            }

            catch(Exception e){

            }

            //틀린거---buzzer
            view.PiezoData = 1;
            view.PiezoControl(5);
            view.counter=0;
            while(view.counter<1){
            }
            view.PiezoControl(0);
            try{
                Thread.sleep(500);
            }
            catch(Exception e){

            }
            //틀린거---buzzer

            if(view.option == view.OPTION_LIFE)
                view.LEDcount--;

            view.select1.state = Card.CARD_CLOSE;
            view.select2.state = Card.CARD_CLOSE;
        }
        if(view.option == view.OPTION_LIFE) {
            if (view.LEDcount == 0) {
                view.timeover = view.TIMEOVER;
                view.btn_ranking =1;
            }
        }

        view.state = (view.matchCount < view.axis_x*view.axis_y)?(CardGame.STATE_PLAY):
                (CardGame.STATE_END);

        view.end = (int)(System.currentTimeMillis()/1000);
        view.postInvalidate();
    }

    public void run_new(){

        if(view.levelcount == 0) {

            if(view.level <= 4) {
                view.level++;
                view.levelcount = 1;
                view.axis_y++;
            }

            else {
                view.level++;
                view.levelcount = 1;
                view.sleeping--;
            }
        }

        else{
            view.levelcount--;
        }

        switch(view.level%4 + 1){
            case 1:
                view.background = 1;
                break;
            case 2:
                view.background = 2;
                break;
            case 3:
                view.background = 3;
                break;
            case 4:
                view.background = 4;
                break;
        }

        view.state = CardGame.STATE_NEW;
        view.postInvalidate();

        view.rand_card = new Random();
        Bitmap temp;

        for(int i = 0; i < 20; i++){
            int rd = view.rand_card.nextInt(15) ;
            temp = view.New_Front[0];
            view.New_Front[0] = view.New_Front[rd];
            view.New_Front[rd] = temp;
        }
    }
}
