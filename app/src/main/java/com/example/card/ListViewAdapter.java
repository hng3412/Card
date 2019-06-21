package com.example.card;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {

    private ArrayList<ListVO> listVO = new ArrayList<ListVO>() ;
    public ListViewAdapter() {

    }

    @Override
    public int getCount() {
        return listVO.size() ;
    }

    // ** 이 부분에서 리스트뷰에 데이터를 넣어줌 **
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //postion = ListView의 위치      /   첫번째면 position = 0
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_listview, parent, false);
        }

        TextView rank = (TextView) convertView.findViewById(R.id.txt_rank) ;
        TextView name = (TextView) convertView.findViewById(R.id.txt_name) ;
        TextView level = (TextView) convertView.findViewById(R.id.txt_level) ;
        TextView time = (TextView) convertView.findViewById(R.id.txt_time) ;
        TextView score = (TextView) convertView.findViewById(R.id.txt_score) ;


        ListVO listViewItem = listVO.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        rank.setText(listViewItem.getRank());
        name.setText(listViewItem.getName());
        level.setText(listViewItem.getLevel());
        time.setText(listViewItem.getTime());
        score.setText(listViewItem.getScore());



        //리스트뷰 클릭 이벤트
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });


        return convertView;
    }


    @Override
    public long getItemId(int position) {
        return position ;
    }


    @Override
    public Object getItem(int position) {
        return listVO.get(position) ;
    }

    // 데이터값 넣어줌
    public void addVO(String rank, String name,String level,String time,String score) {
        ListVO item = new ListVO();

        item.setRank(rank);
        item.setName(name);
        item.setLevel(level);
        item.setTime(time);
        item.setScore(score);

        listVO.add(item);
    }
}