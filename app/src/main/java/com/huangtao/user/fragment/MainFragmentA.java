package com.huangtao.user.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.hjq.bar.TitleBar;
import com.huangtao.user.R;
import com.huangtao.user.activity.MeetingActivity;
import com.huangtao.user.activity.MeetingRoomActivity;
import com.huangtao.user.activity.MeetingroomListActivity;
import com.huangtao.user.activity.SmartAppointActivity;
import com.huangtao.user.common.Constants;
import com.huangtao.user.common.MyLazyFragment;
import com.huangtao.user.helper.CommonUtils;
import com.huangtao.user.model.Meeting;
import com.huangtao.user.model.MeetingRoom;
import com.huangtao.user.model.meta.MeetingType;
import com.huangtao.user.model.meta.Status;
import com.huangtao.user.network.Network;
import com.huangtao.user.widget.XCollapsingToolbarLayout;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

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
    @BindView(R.id.scan)
    ImageView scan;

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
    @BindView(R.id.meeting_refresh)
    ImageView meetingRefresh;

    ProgressDialog dialog;

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
        dialog = new ProgressDialog(getFragmentActivity());
        dialog.setMessage("正在加载中…");

        // 给这个ToolBar设置顶部内边距，才能和TitleBar进行对齐
        ImmersionBar.setTitleBar(getFragmentActivity(), mToolbar);

        //设置渐变监听
        mCollapsingToolbarLayout.setOnScrimsListener(this);

        mSearchView.setOnClickListener(this);
        appoint.setOnClickListener(this);
        appointSmart.setOnClickListener(this);
        addMeeting.setOnClickListener(this);
        meetingRefresh.setOnClickListener(this);
        scan.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        if(!Constants.uid.isEmpty()){
            initMeetingList(false);
        }
    }

    private void initMeetingList(final boolean isRefresh) {
        // 今日会议
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Network.getInstance().queryMeetingByUid(Constants.uid, sdf.format(dt), null).enqueue(new Callback<List<Meeting>>() {
            @Override
            public void onResponse(Call<List<Meeting>> call, Response<List<Meeting>> response) {
                List<Meeting> meetings = response.body();
                meetingContainer.removeAllViews();
                if (meetings != null && meetings.size() > 0) {

                    for (final Meeting meeting : meetings) {
                        View v = LayoutInflater.from(getFragmentActivity()).inflate(R.layout.layout_main_meeting, null);

                        TextView name = v.findViewById(R.id.name);
                        TextView location = v.findViewById(R.id.location);
                        TextView time = v.findViewById(R.id.time);
                        TextView status = v.findViewById(R.id.status);
                        ImageView urgency = v.findViewById(R.id.urgency);

                        name.setText(!meeting.getHeading().isEmpty() ? meeting.getHeading() : "无主题");
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
                                status.setTextColor(getFragmentActivity().getColor(R.color.douban_gray_55_percent));
                                break;
                            case Cancelled:
                                status.setText("已取消");
                                status.setTextColor(getFragmentActivity().getColor(R.color.douban_gray_55_percent));
                                break;
                        }

                        if (meeting.getType() == MeetingType.URGENCY) {
                            urgency.setVisibility(View.VISIBLE);
                        }

                        v.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getFragmentActivity(), MeetingActivity.class);
                                intent.putExtra("id", meeting.getId());
                                startActivity(intent);
                            }
                        });

                        if(meeting.getStatus() != Status.Cancelled)
                            meetingContainer.addView(v);
                    }

                    if(meetingContainer.getChildCount() > 0) {
                        noMeeting.setVisibility(View.GONE);
                    } else {
                        noMeeting.setVisibility(View.VISIBLE);
                    }

                    if(isRefresh){
                        toast("刷新成功");
                    }
                } else {
                    noMeeting.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<Meeting>> call, Throwable t) {

            }
        });
    }

    private void joinMeeting(String attendantNum) {
        Network.getInstance().joinMeeting(attendantNum, Constants.uid).enqueue(new Callback<Meeting>() {
            @Override
            public void onResponse(Call<Meeting> call, final Response<Meeting> response) {
                dialog.dismiss();
                if (response != null && response.body() != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getFragmentActivity());
                    builder.setMessage("加入成功！");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getFragmentActivity(), MeetingActivity.class);
                            intent.putExtra("id", response.body().getId());
                            startActivity(intent);
                        }
                    });
                    builder.show();
                } else {
                    toast("加入失败");
                }
            }

            @Override
            public void onFailure(Call<Meeting> call, Throwable t) {
                toast("加入失败");
                t.printStackTrace();
                dialog.dismiss();
            }
        });
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
            scan.setImageResource(R.mipmap.ic_scan_black);
        }else {
            mSearchView.setBackgroundResource(R.drawable.bg_home_search_bar_transparent);
            getStatusBarConfig().statusBarDarkFont(false).init();
            scan.setImageResource(R.mipmap.ic_scan_white);
        }
    }


    @Override
    public void onClick(View v) {
        if (v == mSearchView) {

        } else if (v == appoint) {
            startActivity(MeetingroomListActivity.class);
        } else if (v == appointSmart) {
            startActivity(SmartAppointActivity.class);
        } else if (v == addMeeting) {
            final String[] items = { "扫一扫","输入会议码"};
            AlertDialog.Builder listDialog = new AlertDialog.Builder(getFragmentActivity());
            listDialog.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            Intent intent = new Intent(getFragmentActivity(), CaptureActivity.class);
                            startActivityForResult(intent, 0);
                            break;
                        case 1:
                            final EditText editText = new EditText(getFragmentActivity());
                            AlertDialog.Builder inputDialog = new AlertDialog.Builder(getFragmentActivity());
                            inputDialog.setTitle("请输入会议码").setView(editText);
                            inputDialog.setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            joinMeeting(editText.getText().toString());
                                        }
                                    }).show();
                            break;
                    }
                }
            });
            listDialog.show();
        } else if (v == meetingRefresh) {
            initMeetingList(true);
        } else if (v == scan) {
            Intent intent = new Intent(getFragmentActivity(), CaptureActivity.class);
            startActivityForResult(intent, 1);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        /**
         * 处理二维码扫描结果
         */
        if(data != null) {
            Bundle bundle = data.getExtras();
            dialog.show();
            switch (requestCode){
                case 0:
                    // 加入会议
                    if (bundle != null && bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                        joinMeeting(bundle.getString(CodeUtils.RESULT_STRING));
                    }
                    break;
                case 1:
                    // 扫一扫
                    if (bundle != null && bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                        processResult(bundle.getString(CodeUtils.RESULT_STRING));
                    }
                    break;
            }
        }
    }

    private void processResult(String result) {
        if (isFourDigit(result)) joinMeeting(result);
        //扫会议室端的码跳转到对应会议室
        else {
            Network.getInstance().queryMeetingroomById(result).enqueue(new Callback<MeetingRoom>() {
                @Override
                public void onResponse(Call<MeetingRoom> call, Response<MeetingRoom> response) {
                    Intent intent = new Intent(getContext(), MeetingRoomActivity.class);
                    intent.putExtra("meetingroom", response.body());
                    startActivity(intent);
                    dialog.dismiss();
                }

                @Override
                public void onFailure(Call<MeetingRoom> call, Throwable t) {
                    t.printStackTrace();
                    toast("跳转失败");
                    dialog.dismiss();
                }
            });
        }
    }

    private boolean isFourDigit(String s){
        return s.length() == 4;
    }
}