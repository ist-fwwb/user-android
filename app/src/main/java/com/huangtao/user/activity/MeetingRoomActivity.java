package com.huangtao.user.activity;

import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
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
import com.huangtao.user.model.MeetingRoom;
import com.huangtao.user.model.TimeSlice;
import com.huangtao.user.model.meta.MeetingRoomUtils;
import com.huangtao.user.network.Network;
import com.huangtao.user.widget.XCollapsingToolbarLayout;
import com.kelin.scrollablepanel.library.ScrollablePanel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    @BindView(R.id.order_layout)
    LinearLayout orderLayout;

    @BindView(R.id.table)
    ScrollablePanel scrollablePanel;

    private MeetingRoom meetingRoom;
    private boolean dateSelected = false;
    private Map<String, List<Boolean>> datas;
    MeetingRoomOrderAdapter orderAdapter;

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

        // 初始化预定表格
        List<String> dates = CommonUtils.getDateOfWeek(System.currentTimeMillis());
        for(final String date : dates){
            Network.getInstance().queryTimeSlice(date.replace(".", "-"), meetingRoom.getId()).enqueue(new Callback<TimeSlice>() {
                @Override
                public void onResponse(Call<TimeSlice> call, Response<TimeSlice> response) {
                    TimeSlice timeSlice = response.body();
                    List<Boolean> canOrder = new ArrayList<>();
                    for(String slice : timeSlice.getTimeSlice()){
                        if(slice == null){
                            canOrder.add(true);
                        } else {
                            canOrder.add(false);
                        }
                    }
                    datas.put(date, canOrder);

                    if(datas.size() >= 5){
                        orderAdapter = new MeetingRoomOrderAdapter(MeetingRoomActivity.this, datas, (int) ((getWindowManager().getDefaultDisplay().getWidth() * 0.9) / 5), btnTime);
                        scrollablePanel.setPanelAdapter(orderAdapter);
                    }
                }

                @Override
                public void onFailure(Call<TimeSlice> call, Throwable t) {
                    toast("网络请求失败");
                    t.printStackTrace();
                }
            });
        }


    }


    @Override
    public void onClick(View v) {
        if(v == order){
            if(dateSelected){

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
}
