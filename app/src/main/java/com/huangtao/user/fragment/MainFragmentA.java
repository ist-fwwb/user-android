package com.huangtao.user.fragment;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.hjq.bar.TitleBar;
import com.huangtao.user.R;
import com.huangtao.user.activity.MeetingActivity;
import com.huangtao.user.activity.MeetingroomListActivity;
import com.huangtao.user.common.Constants;
import com.huangtao.user.common.MyLazyFragment;
import com.huangtao.user.helper.CommonUtils;
import com.huangtao.user.model.Meeting;
import com.huangtao.user.network.Network;
import com.huangtao.user.widget.XCollapsingToolbarLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragmentA extends MyLazyFragment
        implements XCollapsingToolbarLayout.OnScrimsListener, View.OnClickListener {

    @BindView(R.id.abl_test_bar)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.ctl_test_bar)
    XCollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.t_test_title)
    Toolbar mToolbar;
    @BindView(R.id.tb_test_a_bar)
    TitleBar mTitleBar;

    @BindView(R.id.tv_test_search)
    TextView mSearchView;

    @BindView(R.id.appoint)
    LinearLayout appoint;
    @BindView(R.id.appoint_smart)
    LinearLayout appointSmart;
    @BindView(R.id.add_meeting)
    LinearLayout addMeeting;

    @BindView(R.id.no_meeting)
    TextView noMeeting;
    @BindView(R.id.meeting_container)
    LinearLayout meetingContainer;

    public static MainFragmentA newInstance() {
        return new MainFragmentA();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main_home;
    }

    @Override
    protected int getTitleBarId() {
        return R.id.tb_test_a_bar;
    }

    @Override
    protected void initView() {
        // 给这个ToolBar设置顶部内边距，才能和TitleBar进行对齐
        ImmersionBar.setTitleBar(getFragmentActivity(), mToolbar);

        //设置渐变监听
        mCollapsingToolbarLayout.setOnScrimsListener(this);

        mSearchView.setOnClickListener(this);
        appoint.setOnClickListener(this);
        appointSmart.setOnClickListener(this);
        addMeeting.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        if(!Constants.uid.isEmpty()){
            // 今日会议
            Date dt = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
            Network.getInstance().queryMeetingByUid(Constants.uid, sdf.format(dt)).enqueue(new Callback<List<Meeting>>() {
                @Override
                public void onResponse(Call<List<Meeting>> call, Response<List<Meeting>> response) {
                    List<Meeting> meetings = response.body();
                    if (meetings.size() > 0) {
                        noMeeting.setVisibility(View.GONE);

                        for (final Meeting meeting : meetings) {
                            View v = LayoutInflater.from(getFragmentActivity()).inflate(R.layout.layout_main_meeting, null);

                            TextView name = v.findViewById(R.id.name);
                            TextView location = v.findViewById(R.id.location);
                            TextView time = v.findViewById(R.id.time);
                            TextView status = v.findViewById(R.id.status);

                            name.setText(meeting.getHeading());
                            location.setText(meeting.getLocation());
                            time.setText(CommonUtils.getFormatTime(meeting.getStartTime(), meeting.getEndTime()));

                            switch (meeting.getStatus()){
                                case Pending:
                                    status.setText("未开始");
                                    status.setTextColor(getFragmentActivity().getColor(R.color.douban_green));
                                    break;
                                case Running:
                                    status.setText("进行中");
                                    break;
                                case Stopped:
                                    status.setText("已结束");
                                    status.setTextColor(getFragmentActivity().getColor(R.color.douban_gray));
                                    break;
                                case Cancelled:
                                    status.setText("已取消");
                                    status.setTextColor(getFragmentActivity().getColor(R.color.douban_gray));
                                    break;
                            }

                            v.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getFragmentActivity(), MeetingActivity.class);
                                    intent.putExtra("meeting", meeting);
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
        }
    }

    @Override
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
    }

    @Override
    public boolean statusBarDarkFont() {
        return mCollapsingToolbarLayout.isScrimsShown();
    }

    /**
     * {@link XCollapsingToolbarLayout.OnScrimsListener}
     */
    @Override
    public void onScrimsStateChange(boolean shown) {
        // CollapsingToolbarLayout 发生了渐变
        if (shown) {
            mSearchView.setBackgroundResource(R.drawable.bg_home_search_bar_gray);
            getStatusBarConfig().statusBarDarkFont(true).init();
        }else {
            mSearchView.setBackgroundResource(R.drawable.bg_home_search_bar_transparent);
            getStatusBarConfig().statusBarDarkFont(false).init();
        }
    }


    @Override
    public void onClick(View v) {
        if (v == mSearchView) {

        } else if (v == appoint) {
            startActivity(MeetingroomListActivity.class);
        } else if (v == appointSmart) {

        } else if (v == addMeeting) {

        }
    }
}