package com.example.card;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.card.databinding.Item1Binding;

import java.util.Vector;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private Vector<Integer> _1to32 = new Vector<>();
    private Vector<Integer> visible = new Vector<>();
    Context context;

    int width = 0, height = 0;

    public ItemAdapter(Context context) {
        this.context = context;
        for (int i = 0; i < 16; i++) {
            visible.add(i, View.VISIBLE);
        }
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {



        Item1Binding binding = Item1Binding.inflate(LayoutInflater.from(context), viewGroup, false);
        return new ItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int position) {
        Item1Binding binding = itemViewHolder.binding;
        int number = _1to32.get(position);

        if (width != 0 && height != 0) {
            binding.numberBtn.getLayoutParams().width = width-5;
            binding.numberBtn.getLayoutParams().height = height-10;
        }
        binding.numberBtn.setBackgroundResource(R.drawable.cheesesmall);
        binding.numberBtn.setTextSize(20);

        binding.numberBtn.setText(String.valueOf(number));
        binding.numberBtn.setVisibility(visible.get(position));
        //난이도 설정.
        /*if(number >= 1 && number <=10) {
            binding.numberBtn.setBackgroundColor(Color.YELLOW);
            binding.numberBtn.setTextColor(Color.BLACK);
        } else if (number >= 11 && number <= 20) {
            binding.numberBtn.setBackgroundColor(Color.GREEN);
            binding.numberBtn.setTextColor(Color.BLACK);
        } else if (number >= 21 && number <= 30) {
            binding.numberBtn.setBackgroundColor(Color.BLUE);
            binding.numberBtn.setTextColor(Color.WHITE);
        } else if (number >= 31 && number <= 40) {
            binding.numberBtn.setBackgroundColor(Color.BLACK);
            binding.numberBtn.setTextColor(Color.WHITE);
        } else {
            binding.numberBtn.setBackgroundColor(Color.RED);
            binding.numberBtn.setTextColor(Color.WHITE);
        }*/
    }

    @Override
    public int getItemCount() {
        return _1to32.size();
    }

    void init1to16(int number) {
        _1to32.add(number);
    }

    void update17to32(int position, int number) {
        _1to32.remove(position);
        _1to32.add(position, number);
    }

    void setUpVisible(int position) {
        visible.remove(position);
        visible.add(position, View.INVISIBLE);
    }

    void setLength(int width, int height) {
        this.width = width;
        this.height = height;
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {
        Item1Binding binding;

        ItemViewHolder(Item1Binding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


}
