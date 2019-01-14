package com.huangtao.user.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huangtao.user.R;

public class MeetingroomListAdapter extends RecyclerView.Adapter<MeetingroomListAdapter.MeetingroomVH> {

//    List<BluetoothDevice> data;

//    public BluetoothListAdapter(List<BluetoothDevice> data) {
//        this.data = data;
//    }

    @NonNull
    @Override
    public MeetingroomVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_meetingroom, viewGroup, false);
        return new MeetingroomVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MeetingroomVH viewHolder, final int i) {

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public static class MeetingroomVH extends RecyclerView.ViewHolder{
        public MeetingroomVH(@NonNull View itemView) {
            super(itemView);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

}
