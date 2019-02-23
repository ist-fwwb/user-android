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
import com.huangtao.user.helper.CommonUtils;
import com.huangtao.user.model.Meeting;
import com.huangtao.user.model.meta.MeetingType;
import com.huangtao.user.network.FileManagement;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainMeetingAdapter extends RecyclerView.Adapter<MainMeetingAdapter.MeetingVH> {

    private OnItemClickListener onItemClickListener;

    List<Meeting> data;

    Context mContext;

    public MainMeetingAdapter(Context context, List<Meeting> data) {
        mContext = context;
        this.data = data;
    }

    @NonNull
    @Override
    public MeetingVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_main_meeting, viewGroup, false);
        return new MeetingVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MeetingVH viewHolder, final int i) {
        final Meeting meeting = data.get(i);

        switch (meeting.getStatus()) {
            case Pending:
                viewHolder.status.setBackgroundColor(mContext.getColor(R.color.douban_green));
                break;
            case Running:
                viewHolder.status.setBackgroundColor(mContext.getColor(R.color.douban_red));
                break;
            case Stopped:
                viewHolder.status.setBackgroundColor(mContext.getColor(R.color.douban_blue));
                break;
            case Cancelled:
                viewHolder.status.setBackgroundColor(mContext.getColor(R.color.douban_gray_55_percent));
                break;
        }

        viewHolder.time.setText(meeting.getStartTime() / 2 + " : " + ((meeting.getStartTime() + 1) % 2 == 0 ? "30" : "00"));
        viewHolder.duration.setText(CommonUtils.getDurationString(meeting.getStartTime(), meeting.getEndTime()));
        viewHolder.heading.setText(TextUtils.isEmpty(meeting.getHeading()) ? "无主题" : meeting.getHeading());
        viewHolder.location.setText(meeting.getLocation());
        viewHolder.wrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(meeting.getId());
            }
        });
        if(meeting.getType() == MeetingType.URGENCY) {
            viewHolder.urgency.setVisibility(View.VISIBLE);
        }

        Observable.just(FileManagement.getUserHead(mContext, meeting.getHost()))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        if(!s.isEmpty()){
                            viewHolder.head.setImageBitmap(CommonUtils.getLoacalBitmap(s));
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

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(String meetingId);
    }

    public static class MeetingVH extends RecyclerView.ViewHolder{

        @BindView(R.id.wrapper)
        RelativeLayout wrapper;

        @BindView(R.id.status)
        View status;

        @BindView(R.id.time)
        TextView time;

        @BindView(R.id.duration)
        TextView duration;

        @BindView(R.id.heading)
        TextView heading;

        @BindView(R.id.location)
        TextView location;

        @BindView(R.id.host_head)
        CircleImageView head;

        @BindView(R.id.urgency)
        ImageView urgency;

        public MeetingVH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
