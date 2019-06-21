package com.example.card;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DB_Helper extends SQLiteOpenHelper{

    private Context context;

    public DB_Helper(Context context){
        super(context, "Ranking.db", null, 1);
        this.context = context;
    }

    public void onCreate(SQLiteDatabase db){
        StringBuffer sb = new StringBuffer();

        sb.append("CREATE TABLE RANKING ( ");
        sb.append(" _ID INTEGER PRIMARY KEY AUTOINCREMENT, ");
        sb.append(" NAME TEXT, ");
        sb.append(" LEVEL TEXT, ");
        sb.append(" TIME TEXT, ");
        sb.append(" SCORE TEXT )");

        db.execSQL(sb.toString());

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void testDB() {
        SQLiteDatabase db = getReadableDatabase();
    }
}
