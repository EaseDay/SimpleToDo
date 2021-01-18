package com.example.yizeprework;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

//Taking a data at a particular position and put it into a view holder.
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    public interface OnClickListener {
        void onItemClicked(int position);
    }

    public interface OnLongClickListener {
        void onItemLongClicked(int position);

    }

    OnLongClickListener longClickListener;
    OnClickListener clickListener;

    List<String> items;

    public ItemAdapter(List<String> items, OnLongClickListener longClickListener, OnClickListener clickListener) {
        this.items = items;
        this.longClickListener = longClickListener;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        //Use layout inflator to inflate a view.
        View todoView = LayoutInflater.from(viewGroup.getContext()).inflate(android.R.layout.simple_list_item_1,viewGroup,false);

        //Wrap it inside a view holder and return it.
        return new ViewHolder(todoView);
    }

    //Binding data to a particular view holder.
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        //Grab the item at the position.
        String item = items.get(i);

        //Bind the item into the specified view holder.
        viewHolder.bind(item);
    }

    //Tells the RV how many items are in the list.
    @Override
    public int getItemCount() {
        return items.size();
    }

    //Container to provide easy access to views that represent each row of the list.
    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(android.R.id.text1);
        }

        //Update the view inside of the view holder with this data.
        public void bind(String item) {
            tvItem.setText(item);

            //Quick click to edit.
            tvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Notify the listener which position was clicked.
                    clickListener.onItemClicked(getAdapterPosition());
                }
            });

            //Long press to delete.
            tvItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    //Notify the listener which position was long pressed.
                    longClickListener.onItemLongClicked(getAdapterPosition());
                    return true;
                }
            });
        }

    }
}
