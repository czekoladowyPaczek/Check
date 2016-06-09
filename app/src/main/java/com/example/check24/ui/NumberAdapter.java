package com.example.check24.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.check24.R;

import java.util.List;

/**
 * Created by marcingawel on 09.06.2016.
 */

public class NumberAdapter extends RecyclerView.Adapter<NumberAdapter.ViewHolder> {

    public interface OnItemClicked {
        void onItemRemove(int position);
        void onItemChange(int position);
    }

    private LayoutInflater inflater;
    private List<Double> numbers;
    private OnItemClicked listener;

    public NumberAdapter(List<Double> numbers, Context context, OnItemClicked listener) {
        inflater = LayoutInflater.from(context);

        this.numbers = numbers;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.view_list_item, parent, false), listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(numbers.get(position));
    }

    @Override
    public int getItemCount() {
        return numbers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView numberView;
        private Button removeButton;

        public ViewHolder(View itemView, final OnItemClicked listener) {
            super(itemView);

            numberView = (TextView) itemView.findViewById(R.id.number);
            removeButton = (Button) itemView.findViewById(R.id.remove);

            removeButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemRemove(getAdapterPosition());
                }
            });
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemChange(getAdapterPosition());
                }
            });
        }

        public void bind(Double number) {
            numberView.setText(String.valueOf(number.doubleValue()));
        }
    }
}
