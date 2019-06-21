package com.example.card;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

public class RankActivity extends AppCompatActivity {
    private ListView listview ;
    private ListViewAdapter adapter;
    private DB_Helper dbHelper;

    public int index = 0;
    public int minimum = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);

        ImageButton btn_rank_restart = findViewById(R.id.btn_rank_restart);

        btn_rank_restart.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();

            }
        });

        adapter = new ListViewAdapter();
        listview = (ListView) findViewById(R.id.List_view);

        listview.setAdapter(adapter);

        dbHelper = new DB_Helper(RankActivity.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String sql = "select * from RANKING ORDER BY LEVEL DESC, SCORE DESC";

        Cursor cursor = db.rawQuery(sql, null);

        while(cursor.moveToNext()) {
            index++;
            minimum--;

            int NAME_pos = cursor.getColumnIndex("NAME");
            int LEVEL_pos = cursor.getColumnIndex("LEVEL");
            int TIME_pos = cursor.getColumnIndex("TIME");
            int SCORE_pos = cursor.getColumnIndex("SCORE");


            String name = cursor.getString(NAME_pos);
            String level = cursor.getString(LEVEL_pos);
            String time = cursor.getString(TIME_pos);
            String score = cursor.getString(SCORE_pos);

            adapter.addVO( Integer.toString(index),name,level,time,score);
        }

        for( ; minimum>0 ; minimum--){
            adapter.addVO(null,null,null,null,null);
        }
        db.close();

    }
}
