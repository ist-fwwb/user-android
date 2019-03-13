package com.huangtao.user.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangtao.user.R;
import com.huangtao.user.common.Constants;
import com.huangtao.user.helper.CommonUtils;
import com.huangtao.user.model.Meeting;
import com.huangtao.user.model.MeetingNote;
import com.huangtao.user.model.MeetingRoom;
import com.huangtao.user.model.SearchResultItem;
import com.huangtao.user.model.User;
import com.huangtao.user.model.meta.MeetingRoomUtils;
import com.huangtao.user.model.meta.MeetingType;
import com.huangtao.user.model.meta.Status;
import com.huangtao.user.network.FileManagement;
import com.huangtao.user.network.Network;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.SearchResultVH> {

    private OnItemClickListener onItemClickListener;

    List<SearchResultItem> data;

    Context mContext;

    public SearchResultAdapter(Context context, List<SearchResultItem> data) {
        mContext = context;
        this.data = data;
    }

    @NonNull
    @Override
    public SearchResultVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_search_result, viewGroup, false);
        return new SearchResultVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final SearchResultVH viewHolder, final int i) {
        final SearchResultItem item = data.get(i);

        if(item.isFirst()) {
            viewHolder.header.setVisibility(View.VISIBLE);
        }

        switch (item.getType()) {
            case MEETING:
                viewHolder.header.setText("会议");
                initMeeting(item.getMeeting(), viewHolder);
                break;
            case NOTE:
                viewHolder.header.setText("会议笔记");
                initNote(item.getNote(), viewHolder);
                break;
            case USER:
                viewHolder.header.setText("人员");
                initUser(item.getUser(), viewHolder);
                break;
            case MEETINGROOM:
                viewHolder.header.setText("会议室");
                initMeetingRoom(item.getMeetingRoom(), viewHolder);
                break;
            default:
                break;
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null) {
                    onItemClickListener.onItemClick(item);
                }
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
        void onItemClick(SearchResultItem item);
    }

    private void initMeeting(final Meeting meeting, final SearchResultVH viewHolder) {
        View status = viewHolder.meeting.findViewById(R.id.meeting_status);
        TextView time = viewHolder.meeting.findViewById(R.id.meeting_time);
        TextView date = viewHolder.meeting.findViewById(R.id.meeting_date);
        TextView heading = viewHolder.meeting.findViewById(R.id.meeting_heading);
        TextView location = viewHolder.meeting.findViewById(R.id.meeting_location);
        ImageView urgency = viewHolder.meeting.findViewById(R.id.meeting_urgency);
        final CircleImageView head = viewHolder.meeting.findViewById(R.id.meeting_host_head);

        switch (meeting.getStatus()) {
            case Pending:
                status.setBackgroundColor(mContext.getColor(R.color.douban_green));
                break;
            case Running:
                status.setBackgroundColor(mContext.getColor(R.color.douban_red));
                break;
            case Stopped:
                status.setBackgroundColor(mContext.getColor(R.color.douban_blue));
                break;
            case Cancelled:
                status.setBackgroundColor(mContext.getColor(R.color.douban_gray_55_percent));
                break;
        }

        time.setText(meeting.getStartTime() / 2 + " : " + ((meeting.getStartTime() + 1) % 2 == 0 ? "30" : "00"));
        date.setText(meeting.getDate());
        heading.setText(TextUtils.isEmpty(meeting.getHeading()) ? "无主题" : meeting.getHeading());
        location.setText(meeting.getLocation());

        if(meeting.getType() == MeetingType.URGENCY) {
            urgency.setVisibility(View.VISIBLE);
        }

        if(meeting.getHost() != null) {
            Observable.just(FileManagement.getUserHead(mContext, meeting.getHost()))
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<String>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(String s) {
                            if (!s.isEmpty()) {
                                head.setImageBitmap(CommonUtils.getLoacalBitmap(s));
                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }

    private void initNote(final MeetingNote meetingNote, final SearchResultVH viewHolder) {
        ImageView type = viewHolder.note.findViewById(R.id.note_type);
        TextView title= viewHolder.note.findViewById(R.id.note_title);
        final TextView owner= viewHolder.note.findViewById(R.id.note_owner);
        TextView favorite= viewHolder.note.findViewById(R.id.note_favorite);
        ImageView favoriteIcon= viewHolder.note.findViewById(R.id.note_favorite_icon);

        switch (meetingNote.getMeetingNoteType()){
            case HTML:
                type.setImageResource(R.mipmap.ic_note_text);
                break;
            case VOICE:
                type.setImageResource(R.mipmap.ic_note_voice);
                break;
        }

        title.setText(TextUtils.isEmpty(meetingNote.getTitle()) ? "无标题" : meetingNote.getTitle());
        if(meetingNote.getCollectorIds() != null && meetingNote.getCollectorIds().size() > 0) {
            favorite.setText(meetingNote.getCollectorIds().size() + "人收藏");
        } else {
            favorite.setVisibility(View.GONE);
        }
        favoriteIcon.setImageResource(meetingNote.isFavorite(Constants.uid) ? R.mipmap.ic_note_favorite_true : R.mipmap.ic_note_favorite_false);

        Network.getInstance().queryUserById(meetingNote.getOwnerId()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.body() != null) {
                    owner.setText(response.body().getName());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    private void initUser(final User user, SearchResultVH viewHolder) {
        final ImageView head = viewHolder.user.findViewById(R.id.user_head);
        TextView name = viewHolder.user.findViewById(R.id.user_name);

        name.setText(user.getName());
        Observable.just(FileManagement.getUserHead(mContext, user))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        if(!s.isEmpty()){
                            head.setImageBitmap(CommonUtils.getLoacalBitmap(s));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void initMeetingRoom(final MeetingRoom meetingRoom, final SearchResultVH viewHolder) {
        ImageView pic = viewHolder.meetingroom.findViewById(R.id.meetingroom_pic);
        TextView location = viewHolder.meetingroom.findViewById(R.id.meetingroom_location);
        final TextView status = viewHolder.meetingroom.findViewById(R.id.meetingroom_status);
        ImageView airConditioner = viewHolder.meetingroom.findViewById(R.id.air_conditioner);
        ImageView blackboard = viewHolder.meetingroom.findViewById(R.id.blackboard);
        ImageView desk = viewHolder.meetingroom.findViewById(R.id.desk);
        ImageView projector = viewHolder.meetingroom.findViewById(R.id.projector);
        ImageView power = viewHolder.meetingroom.findViewById(R.id.power);

        location.setText(meetingRoom.getLocation());

        for(MeetingRoomUtils utils : meetingRoom.getUtils()){
            switch (utils){
                case AIRCONDITIONER:
                    airConditioner.setVisibility(View.VISIBLE);
                    break;
                case TABLE:
                    desk.setVisibility(View.VISIBLE);
                    break;
                case PROJECTOR:
                    projector.setVisibility(View.VISIBLE);
                    break;
                case BLACKBOARD:
                    blackboard.setVisibility(View.VISIBLE);
                    break;
                case POWERSUPPLY:
                    power.setVisibility(View.VISIBLE);
                    break;
            }
        }

        // 会议室状态
        Calendar calendar = Calendar.getInstance();
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Network.getInstance().queryMeeting(sdf.format(dt), meetingRoom.getId(), calendar.get
                (Calendar.HOUR) * 2 + calendar.get(Calendar.MINUTE) / 30).enqueue(new Callback<List<Meeting>>() {
            @Override
            public void onResponse(Call<List<Meeting>> call, Response<List<Meeting>> response) {
                if (response.body() == null || response.body().size() == 0 || response.body().get(0) == null) {
                    status.setText("今日暂无会议");
                } else {
                    Meeting meeting = response.body().get(0);
                    String first = meeting.getStartTime() / 2 + ":" + ((meeting.getStartTime() + 1) % 2 == 0 ? "30" : "00");
                    String last = meeting.getEndTime() / 2 + ":" + ((meeting.getEndTime() + 1) % 2 == 0 ? "30" : "00");
                    if (meeting.getStatus() == Status.Pending) {
                        status.setText("空闲中 " + first + "开始");
                    } else if (meeting.getStatus() == Status.Running) {
                        status.setText("会议中 " + last + "结束");
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Meeting>> call, Throwable t) {

            }
        });
    }

    public static class SearchResultVH extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_header_char)
        TextView header;

        @BindView(R.id.meeting)
        RelativeLayout meeting;

        @BindView(R.id.note)
        RelativeLayout note;

        @BindView(R.id.user)
        RelativeLayout user;

        @BindView(R.id.meetingroom)
        RelativeLayout meetingroom;

        public SearchResultVH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
