package com.huangtao.user.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huangtao.user.R;
import com.huangtao.user.model.MeetingNote;
import com.huangtao.user.model.MeetingNoteWrapper;
import com.huangtao.user.model.User;
import com.huangtao.user.network.Network;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MeetingNoteAdapter extends RecyclerView.Adapter<MeetingNoteAdapter.MeetingNoteVH> {

    List<MeetingNoteWrapper> data;

    public MeetingNoteAdapter(List<MeetingNoteWrapper> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public MeetingNoteVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_meeting_note, viewGroup, false);
        return new MeetingNoteVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MeetingNoteVH viewHolder, final int i) {
        MeetingNoteWrapper meetingNoteWrapper = data.get(i);
        MeetingNote meetingNote = meetingNoteWrapper.getMeetingNote();

        switch (meetingNote.getMeetingNoteType()){
            case HTML:
                viewHolder.type.setImageResource(R.mipmap.ic_note_text);
                break;
            case VOICE:
                viewHolder.type.setImageResource(R.mipmap.ic_note_voice);
                break;
        }

        viewHolder.title.setText(TextUtils.isEmpty(meetingNote.getTitle()) ? "无标题" : meetingNote.getTitle());
        if(meetingNote.getCollectorIds() != null && meetingNote.getCollectorIds().size() > 0) {
            viewHolder.favorite.setText(meetingNote.getCollectorIds().size() + "人收藏");
        } else {
            viewHolder.favorite.setVisibility(View.GONE);
        }
        viewHolder.favoriteIcon.setImageResource(meetingNoteWrapper.getCollected() ? R.mipmap.ic_note_favorite_true : R.mipmap.ic_note_favorite_false);

        Network.getInstance().queryUserById(meetingNote.getOwnerId()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.body() != null) {
                    viewHolder.owner.setText(response.body().getName());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
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

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public static class MeetingNoteVH extends RecyclerView.ViewHolder{

        @BindView(R.id.type)
        ImageView type;

        @BindView(R.id.title)
        TextView title;

        @BindView(R.id.owner)
        TextView owner;

        @BindView(R.id.favorite)
        TextView favorite;

        @BindView(R.id.favorite_icon)
        ImageView favoriteIcon;

        public MeetingNoteVH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
