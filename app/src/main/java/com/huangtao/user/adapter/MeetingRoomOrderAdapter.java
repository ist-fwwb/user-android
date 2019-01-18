package com.huangtao.user.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huangtao.user.R;
import com.huangtao.user.helper.CommonUtils;
import com.kelin.scrollablepanel.library.PanelAdapter;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MeetingRoomOrderAdapter extends PanelAdapter {

    Context context;

    Map<String, List<Boolean>> canOrder;

    int cellWidth;

    TextView btnTime;

    List<String> weeks;
    List<String> dates;

    public int selectedColumn = -1, selectedRowFirst, selectedRowLast;

    OrderViewHolder[][] cells = new OrderViewHolder[48][5];

    public MeetingRoomOrderAdapter(Context context, Map<String, List<Boolean>> canOrder, int cellWidth, TextView btnTime) {
        this.context = context;
        this.canOrder = canOrder;
        this.cellWidth = cellWidth;
        this.btnTime = btnTime;

        weeks = CommonUtils.getDayOfWeek(System.currentTimeMillis());
        dates = CommonUtils.getDateOfWeek(System.currentTimeMillis());
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int row, final int column) {
        if(column == 0){
            holder.itemView.setVisibility(View.GONE);
            return;
        }

        final OrderViewHolder viewHolder = (OrderViewHolder) holder;

        if (row == 0) {

            viewHolder.timeContainer.setVisibility(View.GONE);
            viewHolder.dayContainer.setVisibility(View.VISIBLE);

            if(cellWidth > 0) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewHolder.dayContainer.getLayoutParams();
                layoutParams.width = cellWidth;
                layoutParams.height = (int) (cellWidth * 0.7);
                viewHolder.dayContainer.setLayoutParams(layoutParams);
            }

            viewHolder.day.setText(weeks.get(column - 1));
            viewHolder.date.setText(dates.get(column - 1));

        } else {
            cells[row - 1][column - 1] = viewHolder;

            if(cellWidth > 0) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewHolder.timeContainer.getLayoutParams();
                layoutParams.width = cellWidth;
                layoutParams.height = (int) (cellWidth * 0.7);
                viewHolder.timeContainer.setLayoutParams(layoutParams);
            }

            String timeStr = (row - 1) / 2 + ":" + (row % 2 == 0 ? "30" : "00");

            viewHolder.time.setText(timeStr);

            String date = dates.get(column - 1);
            if(canOrder.containsKey(date)){
                if(canOrder.get(date).get(row - 1)){
                    // 该cell可以预定
                    viewHolder.time.setTextColor(context.getColor(R.color.douban_gray));
                    viewHolder.time.setBackgroundColor(context.getColor(getCellBackgroundColor(column - 1, row - 1)));

                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(selectedColumn < 0){
                                selectedColumn = column - 1;
                                selectedRowFirst = row - 1;
                                selectedRowLast = row - 1;
                            } else {
                                if(selectedColumn != column - 1){
                                    Toast.makeText(context, "只能选择同一天", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                selectedRowLast = row - 1;
                            }
                            refreshSelectedCell();
                        }
                    });



                } else {
                    viewHolder.time.setTextColor(context.getColor(R.color.douban_gray_28_percent));
                    viewHolder.time.setBackground(context.getDrawable(R.mipmap.ic_ordered));
                    cells[row - 1][column - 1] = null;
                }
            }


            // 只显示8点之后和十点之前的
            if(row <= 16 || row >= 45){
                viewHolder.timeContainer.setVisibility(View.GONE);
            } else {
                viewHolder.timeContainer.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OrderViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_meetingroom_order, parent, false));
    }

    private int getCellBackgroundColor(int column, int row) {
        if(column == selectedColumn && row >= selectedRowFirst && row <= selectedRowLast){
            return R.color.douban_yellow_50_percent;
        }
        return R.color.white;
    }

    private void refreshSelectedCell() {
        btnTime.setVisibility(View.VISIBLE);
        String first = selectedRowFirst / 2 + ":" + ((selectedRowFirst + 1) % 2 == 0 ? "30" : "00");
        String last = (selectedRowLast + 1) / 2 + ":" + ((selectedRowLast+2) % 2 == 0 ? "30" : "00");
        btnTime.setText("周" + weeks.get(selectedColumn) + " " + first + "-" + last);

        if(selectedColumn >= 0) {
            for (int i = selectedRowFirst; i <= selectedRowLast; i++) {
                cells[i][selectedColumn].time.setBackgroundColor(context.getColor(R.color.douban_yellow_50_percent));
            }
        }
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.time_container)
        RelativeLayout timeContainer;
        @BindView(R.id.time)
        TextView time;

        @BindView(R.id.day_container)
        RelativeLayout dayContainer;
        @BindView(R.id.day)
        TextView day;
        @BindView(R.id.date)
        TextView date;

        public OrderViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            setIsRecyclable(false);
        }
    }
}
