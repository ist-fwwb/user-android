package com.huangtao.user.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangtao.user.R;
import com.kelin.scrollablepanel.library.PanelAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MeetingRoomOrderAdapter extends PanelAdapter {

    List<List<Boolean>> canOrder;

    int cellWidth;

    public MeetingRoomOrderAdapter(List<List<Boolean>> canOrder, int cellWidth) {
        this.canOrder = canOrder;
        this.cellWidth = cellWidth;
    }

    @Override
    public int getRowCount() {
        return 49;
    }

    @Override
    public int getColumnCount() {
        return 6;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int row, int column) {
        if(column == 0){
            holder.itemView.setVisibility(View.GONE);
        }

        OrderViewHolder viewHolder = (OrderViewHolder) holder;

        if(cellWidth > 0) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewHolder.time.getLayoutParams();
            layoutParams.width = cellWidth;
            layoutParams.height = (int) (cellWidth * 0.7);
            viewHolder.time.setLayoutParams(layoutParams);
        }

        if()
        viewHolder.time.setText(row + "行" + column + "列");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OrderViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_meetingroom_order, parent, false));
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.time)
        TextView time;

        public OrderViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
