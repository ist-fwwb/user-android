package com.huangtao.user.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huangtao.user.R;
import com.huangtao.user.model.Message;
import com.huangtao.user.model.meta.MessageStatus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageVH> {

    private OnItemClickListener onItemClickListener;

    List<Message> data;

    Context mContext;

    public MessageAdapter(Context context, List<Message> data) {
        mContext = context;
        this.data = data;
    }

    @NonNull
    @Override
    public MessageVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_message, viewGroup, false);
        return new MessageVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MessageVH viewHolder, final int i) {
        final Message message = data.get(i);

        viewHolder.title.setText(message.getTitle());
        viewHolder.content.setText(message.getContent());
        viewHolder.time.setText(message.getTime());

        if (message.getMessageStatus() == MessageStatus.NEW) {
            viewHolder.point.setVisibility(View.VISIBLE);
            viewHolder.title.setTextColor(ContextCompat.getColor(mContext, R.color.douban_gray));
        } else {
            viewHolder.point.setVisibility(View.INVISIBLE);
            viewHolder.title.setTextColor(ContextCompat.getColor(mContext, R.color.douban_gray_55_percent));
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (message.getMessageStatus() == MessageStatus.NEW) {
                    viewHolder.point.setVisibility(View.INVISIBLE);
                    viewHolder.title.setTextColor(ContextCompat.getColor(mContext, R.color.douban_gray_55_percent));
                }
                onItemClickListener.onItemClick(message);
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
        void onItemClick(Message msg);
    }

    public static class MessageVH extends RecyclerView.ViewHolder{

        @BindView(R.id.point)
        View point;

        @BindView(R.id.title)
        TextView title;

        @BindView(R.id.content)
        TextView content;

        @BindView(R.id.time)
        TextView time;

        public MessageVH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
