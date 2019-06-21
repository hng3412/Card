package com.example.card;


import android.graphics.Rect;

public class Card {
    public static final int CARD_SHOW = 0;
    public static final int CARD_CLOSE = 1;
    public static final int CARD_OPEN = 2;
    public static final int CARD_MATCHED = 3;
    public static final int IMAGE_RED = 0;
    public static final int IMAGE_GREEN = 1;
    public static final int IMAGE_BLUE = 2;

    public int state;
    public int color;
    public Rect rect;

    public Card(int color, Rect rect){
        state = CARD_CLOSE;
        this.color = color;
        this.rect = rect;
    }

}
