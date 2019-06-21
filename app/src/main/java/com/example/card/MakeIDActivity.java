package com.example.card;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class MakeIDActivity extends AppCompatActivity {
    ImageButton btn_First;
    private DB_Helper dbHelper;
    public String time;
    public int level;
    public int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_id);

        Intent intent = getIntent();
        time = intent.getExtras().getString("time");
        level = intent.getExtras().getInt("level");
        score = intent.getExtras().getInt("score");

        final EditText edtxt_name = findViewById(R.id.edtxt_name);

        btn_First = findViewById(R.id.btn_firstactivity);

        btn_First.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                if(edtxt_name.getText().toString().equals("")) ;
                else {
                    String name;
                    name = edtxt_name.getText().toString();
                    dbHelper = new DB_Helper(MakeIDActivity.this);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();

                    StringBuffer sb = new StringBuffer();
                    sb.append("insert into RANKING(NAME, LEVEL, TIME, SCORE) values(?, ?, ?, ?)");

                    db.execSQL(sb.toString(), new Object[]{name, Integer.toString(level), time, score});
                    Intent intent = new Intent(getApplicationContext(), RankActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
