package ru.nutscoon.mapsproject.Adapters;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.nutscoon.mapsproject.Models.Table;
import ru.nutscoon.mapsproject.R;

public class HourRecyclerViewAdapter extends RecyclerView.Adapter<HourRecyclerViewAdapter.ViewHolder>{
    private Table[] mData = new Table[0];
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public HourRecyclerViewAdapter(Context context, Table[] data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the cell layout from xml when needed

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.hoursitem, viewGroup, false);
        return new ViewHolder(view);
    }


//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = mInflater.inflate(R.layout.hoursitem, parent, false);
//        return new ViewHolder(view);
//    }


    @Override
    public void onBindViewHolder(@NonNull HourRecyclerViewAdapter.ViewHolder viewHolder, int i) {
        viewHolder.bind(mData[i]);
    }



    // binds the data to the textview in each cell
//    @Override
//    public void onBindViewHolder(ViewHolder holder, int position) {
//        Table table = mData[position];
//        holder.fromTextView.setText(table.getfromHours());
//        holder.toTextView.setText(table.gettoHours());
//        //holder.countFreeTV.setText(table.getCountFree());
//        //holder.countBusyTV.setText(table.getCountBusy());
//
//    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.length;
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//        TextView fromTextView;
//        TextView toTextView;
//        TextView countFreeTV;
//        TextView countBusyTV;


        ViewHolder(View itemView) {
            super(itemView);
//            toTextView = (TextView) itemView.findViewById(R.id.toTimeTextView);
//            fromTextView = (TextView) itemView.findViewById(R.id.fromTimeTextView);
//            countFreeTV= (TextView) itemView.findViewById(R.id.countFreeTV);
//            countBusyTV= (TextView) itemView.findViewById(R.id.countBusyTextView);

            itemView.setOnClickListener(this);
        }

        public void bind(Table data){
            TextView toTextView = (TextView) itemView.findViewById(R.id.toTimeTextView);
            TextView fromTextView = (TextView) itemView.findViewById(R.id.fromTimeTextView);
            TextView countFreeTV= (TextView) itemView.findViewById(R.id.countFreeTV);
            TextView countBusyTV= (TextView) itemView.findViewById(R.id.countBusyTextView);

            fromTextView.setText(data.getfromHours().substring(0, data.getfromHours().length() - 3));
            toTextView.setText(data.gettoHours().substring(0, data.gettoHours().length() - 3));
            countFreeTV.setText(data.getCountFree() + "");
            countBusyTV.setText(data.getCountBusy() + "");

        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
   public Table getItem(int id) {
        return mData[id];
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
