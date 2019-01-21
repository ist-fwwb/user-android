package com.huangtao.user.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.hjq.bar.TitleBar;
import com.huangtao.user.R;
import com.huangtao.user.adapter.MeetingRoomOrderAdapter;
import com.huangtao.user.common.MyActivity;
import com.huangtao.user.helper.CommonUtils;
import com.huangtao.user.model.Meeting;
import com.huangtao.user.model.MeetingRoom;
import com.huangtao.user.model.TimeSlice;
import com.huangtao.user.model.User;
import com.huangtao.user.model.meta.MeetingRoomUtils;
import com.huangtao.user.network.Network;
import com.huangtao.user.widget.XCollapsingToolbarLayout;
import com.kelin.scrollablepanel.library.ScrollablePanel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MeetingRoomActivity extends MyActivity implements View.OnClickListener, XCollapsingToolbarLayout.OnScrimsListener {
    @BindView(R.id.appbar)
    AppBarLayout appBarLayout;

    @BindView(R.id.ctl_bar)
    XCollapsingToolbarLayout mCollapsingToolbarLayout;

    @BindView(R.id.tool_bar)
    Toolbar toolbar;

    @BindView(R.id.title_bar)
    TitleBar titleBar;

    @BindView(R.id.scrollView)
    NestedScrollView scrollView;

    @BindView(R.id.order)
    RelativeLayout order;
    @BindView(R.id.btn_time)
    TextView btnTime;

    @BindView(R.id.name)
    TextView name;

    @BindView(R.id.air_conditioner)
    LinearLayout airConditioner;
    @BindView(R.id.blackboard)
    LinearLayout blackboard;
    @BindView(R.id.desk)
    LinearLayout desk;
    @BindView(R.id.projector)
    LinearLayout projector;
    @BindView(R.id.power)
    LinearLayout power;
    @BindView(R.id.wifi)
    LinearLayout wifi;
    @BindView(R.id.network)
    LinearLayout network;
    @BindView(R.id.tv)
    LinearLayout tv;

    @BindView(R.id.meeting_container)
    LinearLayout meetingContainer;
    @BindView(R.id.no_meeting)
    TextView noMeeting;

    @BindView(R.id.order_layout)
    LinearLayout orderLayout;

    @BindView(R.id.table)
    ScrollablePanel scrollablePanel;

    private MeetingRoom meetingRoom;
    private Map<String, List<Boolean>> datas;
    MeetingRoomOrderAdapter orderAdapter;

    BroadcastReceiver receiver;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_meeting_room;
    }

    @Override
    protected int getTitleBarId() {
        return R.id.title_bar;
    }

    @Override
    protected void initView() {
        ImmersionBar.setTitleBar(getActivity(), toolbar);

        order.setOnClickListener(this);

        mCollapsingToolbarLayout.setOnScrimsListener(this);
        getStatusBarConfig().statusBarDarkFont(false).init();

        datas = new HashMap<>();
    }

    @Override
    protected void initData() {
        meetingRoom = (MeetingRoom) getIntent().getSerializableExtra("meetingroom");
        if(meetingRoom == null){
            finish();
        }

        name.setText(meetingRoom.getLocation());

        for(MeetingRoomUtils utils : meetingRoom.getUtils()){
            switch (utils){
                case AIRCONDITIONER:
                    airConditioner.setAlpha(1);
                    break;
                case TABLE:
                    desk.setAlpha(1);
                    break;
                case PROJECTOR:
                    projector.setAlpha(1);
                    break;
                case BLACKBOARD:
                    blackboard.setAlpha(1);
                    break;
                case POWERSUPPLY:
                    power.setAlpha(1);
                    break;
            }
        }

        // 今日会议
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Network.getInstance().queryMeeting(sdf.format(dt), meetingRoom.getId()).enqueue(new Callback<List<Meeting>>() {
            @Override
            public void onResponse(Call<List<Meeting>> call, Response<List<Meeting>> response) {
                List<Meeting> meetings = response.body();
                if (meetings.size() > 0) {
                    noMeeting.setVisibility(View.GONE);

                    for (final Meeting meeting : meetings) {
                        View v = LayoutInflater.from(MeetingRoomActivity.this).inflate(R.layout.layout_meetingroom_meeting, null);

                        TextView name = v.findViewById(R.id.name);
                        final TextView host = v.findViewById(R.id.host);
                        TextView time = v.findViewById(R.id.time);
                        TextView status = v.findViewById(R.id.status);

                        name.setText(!meeting.getHeading().isEmpty() ? meeting.getHeading() : "无主题");
                        time.setText(CommonUtils.getFormatTime(meeting.getStartTime(), meeting.getEndTime()));

                        switch (meeting.getStatus()){
                            case Pending:
                                status.setText("未开始");
                                status.setTextColor(MeetingRoomActivity.this.getColor(R.color.douban_green));
                                break;
                            case Running:
                                status.setText("进行中");
                                break;
                            case Stopped:
                                status.setText("已结束");
                                status.setTextColor(MeetingRoomActivity.this.getColor(R.color.douban_gray));
                                break;
                            case Cancelled:
                                status.setText("已取消");
                                status.setTextColor(MeetingRoomActivity.this.getColor(R.color.douban_gray));
                                break;
                        }

                        Network.getInstance().queryUserById(meeting.getHostId()).enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                if(response.body() != null){
                                    host.setText(response.body().getName());
                                }
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {

                            }
                        });

                        v.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(MeetingRoomActivity.this, MeetingActivity.class);
                                intent.putExtra("id", meeting.getId());
                                startActivity(intent);
                            }
                        });

                        meetingContainer.addView(v);
                    }

                }
            }

            @Override
            public void onFailure(Call<List<Meeting>> call, Throwable t) {

            }
        });


        // 初始化预定表格
        final List<String> dates = CommonUtils.getDateOfWeek(System.currentTimeMillis());
        Network.getInstance().queryTimeSlice(null, meetingRoom.getId()).enqueue(new Callback<List<TimeSlice>>() {
            @Override
            public void onResponse(Call<List<TimeSlice>> call, Response<List<TimeSlice>> response) {
                if (response.body() != null) {
                    List<TimeSlice> slices = response.body();

                    Calendar calendar = Calendar.getInstance();
                    int now = calendar.get(Calendar.HOUR_OF_DAY) * 2 + calendar.get(Calendar.MINUTE) / 30;

                    for (TimeSlice slice : slices) {
                        String month = slice.getDate().split("-")[1];
                        String day = slice.getDate().split("-")[2];
                        if (!dates.contains(month + "." + day)) {
                            continue;
                        }
                        List<Boolean> canOrder = new ArrayList<>();
                        for (int i = 0; i < slice.getTimeSlice().size(); i++) {
                            // 当天已经过了时间
                            if (Integer.parseInt(day) == calendar.get(Calendar.DAY_OF_MONTH) && i <= now) {
                                canOrder.add(false);
                                continue;
                            }

                            if (slice.getTimeSlice().get(i) == null) {
                                canOrder.add(true);
                            } else {
                                canOrder.add(false);
                            }
                        }
                        datas.put(month + "." + day, canOrder);
                    }
                    orderAdapter = new MeetingRoomOrderAdapter(MeetingRoomActivity.this, datas, (int) ((getWindowManager().getDefaultDisplay().getWidth() * 0.9) / 5), btnTime);
                    scrollablePanel.setPanelAdapter(orderAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<TimeSlice>> call, Throwable t) {
                toast("网络请求失败");
                t.printStackTrace();
            }
        });

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("appointSuccess");
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                finish();
            }
        };
        registerReceiver(receiver, intentFilter);
    }


    @Override
    public void onClick(View v) {
        if(v == order){
            if(orderAdapter.selectedColumn >= 0){
                Intent intent = new Intent(this, AppointActivity.class);
                intent.putExtra("meetingroom", meetingRoom);
                intent.putExtra("date", CommonUtils.getDateOfWeek(System.currentTimeMillis()).get(orderAdapter.selectedColumn));
                intent.putExtra("week", "周" + CommonUtils.getDayOfWeek(System.currentTimeMillis()).get(orderAdapter.selectedColumn));
                intent.putExtra("start", orderAdapter.selectedRowFirst);
                // 左闭右开
                intent.putExtra("end", orderAdapter.selectedRowLast + 1);
                startActivity(intent);
            } else {
                appBarLayout.setExpanded(false);
                int y = order.getTop();
                scrollView.smoothScrollTo(0, y);
            }
        }
    }



    @Override
    public void onScrimsStateChange(boolean shown) {
        if (shown) {
            titleBar.setTitle(meetingRoom.getLocation());
            titleBar.setLeftIcon(getDrawable(R.mipmap.bar_icon_back_black));
            getStatusBarConfig().statusBarDarkFont(true).init();
        }else {
            titleBar.setTitle("");
            titleBar.setLeftIcon(null);
            getStatusBarConfig().statusBarDarkFont(false).init();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
