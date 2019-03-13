package com.huangtao.user.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huangtao.user.R;
import com.huangtao.user.common.Constants;
import com.huangtao.user.helper.CommonUtils;
import com.huangtao.user.model.Meeting;
import com.huangtao.user.model.MeetingRoom;
import com.huangtao.user.model.meta.MeetingRoomUtils;
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
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MeetingroomListAdapter extends RecyclerView.Adapter<MeetingroomListAdapter.MeetingroomVH> {

    Context mContext;
    List<MeetingRoom> data;

    public MeetingroomListAdapter(Context context, List<MeetingRoom> data) {
        mContext = context;
        this.data = data;
    }

    @NonNull
    @Override
    public MeetingroomVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_meetingroom, viewGroup, false);
        return new MeetingroomVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MeetingroomVH viewHolder, final int i) {
        MeetingRoom meetingRoom = data.get(i);

        viewHolder.location.setText(meetingRoom.getLocation());

        for(MeetingRoomUtils utils : meetingRoom.getUtils()){
            switch (utils){
                case AIRCONDITIONER:
                    viewHolder.airConditioner.setVisibility(View.VISIBLE);
                    break;
                case TABLE:
                    viewHolder.desk.setVisibility(View.VISIBLE);
                    break;
                case PROJECTOR:
                    viewHolder.projector.setVisibility(View.VISIBLE);
                    break;
                case BLACKBOARD:
                    viewHolder.blackboard.setVisibility(View.VISIBLE);
                    break;
                case POWERSUPPLY:
                    viewHolder.power.setVisibility(View.VISIBLE);
                    break;
            }
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(i);
            }
        });

        // 会议室状态
        Calendar calendar = Calendar.getInstance();
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Network.getInstance().queryMeeting(sdf.format(dt), meetingRoom.getId(), calendar.get
                (Calendar.HOUR) * 2 + calendar.get(Calendar.MINUTE) / 30).enqueue(new Callback<List<Meeting>>() {
            @Override
            public void onResponse(Call<List<Meeting>> call, Response<List<Meeting>> response) {
                if (response.body() == null || response.body().size() == 0 || response.body().get(0) == null) {
                    viewHolder.status.setText("今日暂无会议");
                } else {
                    Meeting meeting = response.body().get(0);
                    String first = meeting.getStartTime() / 2 + ":" + ((meeting.getStartTime() + 1) % 2 == 0 ? "30" : "00");
                    String last = meeting.getEndTime() / 2 + ":" + ((meeting.getEndTime() + 1) % 2 == 0 ? "30" : "00");
                    if (meeting.getStatus() == Status.Pending) {
                        viewHolder.status.setText("空闲中 " + first + "开始");
                    } else if (meeting.getStatus() == Status.Running) {
                        viewHolder.status.setText("会议中 " + last + "结束");
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Meeting>> call, Throwable t) {

            }
        });

        if(meetingRoom.getImages() != null && meetingRoom.getImages().size() > 0) {
            Observable.just(FileManagement.getFile(mContext, meetingRoom.getImages().get(0),
                    Constants.MEETING_ROOM_DIR))
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<String>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(String s) {
                            if(!s.isEmpty()){
                                viewHolder.pic.setImageBitmap(CommonUtils.getLoacalBitmap(s));
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

    public static class MeetingroomVH extends RecyclerView.ViewHolder{

        @BindView(R.id.pic)
        ImageView pic;

        @BindView(R.id.location)
        TextView location;

        @BindView(R.id.status)
        TextView status;

        @BindView(R.id.air_conditioner)
        ImageView airConditioner;

        @BindView(R.id.blackboard)
        ImageView blackboard;

        @BindView(R.id.desk)
        ImageView desk;

        @BindView(R.id.projector)
        ImageView projector;

        @BindView(R.id.power)
        ImageView power;

        public MeetingroomVH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
