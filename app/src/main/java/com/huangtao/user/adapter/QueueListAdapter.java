package com.huangtao.user.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.huangtao.user.R;
import com.huangtao.user.helper.CommonUtils;
import com.huangtao.user.model.QueueNode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QueueListAdapter extends RecyclerView.Adapter<QueueListAdapter.QueueVH> {

    private OnItemClickListener onItemClickListener;

    List<QueueNode> data;

    public QueueListAdapter(List<QueueNode> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public QueueVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_queue, viewGroup, false);
        return new QueueVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final QueueVH viewHolder, final int i) {
        QueueNode queueNode = data.get(i);

        viewHolder.time.setText(CommonUtils.getFormatTimeForQueueList(queueNode.getDate(), queueNode.getTimeRange().getStart(), queueNode.getTimeRange().getEnd()));
        viewHolder.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public static class QueueVH extends RecyclerView.ViewHolder{

        @BindView(R.id.time)
        TextView time;

        @BindView(R.id.meetingroom)
        TextView meetingroom;

        @BindView(R.id.check)
        Button check;

        public QueueVH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
