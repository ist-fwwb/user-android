package com.huangtao.user.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.huangtao.user.R;
import com.huangtao.user.activity.MeetingActivity;
import com.huangtao.user.adapter.MainMeetingAdapter;
import com.huangtao.user.common.Constants;
import com.huangtao.user.common.MyLazyFragment;
import com.huangtao.user.model.Meeting;
import com.huangtao.user.network.Network;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragmentB extends MyLazyFragment
        implements View.OnClickListener,
        CalendarView.OnCalendarSelectListener,
        CalendarView.OnYearChangeListener {

    TextView mTextMonthDay;

    TextView mTextYear;

    TextView mTextLunar;

    TextView mTextCurrentDay;

    CalendarView mCalendarView;

    RelativeLayout mRelativeTool;
    private int mYear;
    CalendarLayout mCalendarLayout;

    RecyclerView mRecyclerView;

    MainMeetingAdapter mainMeetingAdapter;

    List<Meeting> datas;

    List<Meeting> meetings;

    public static MainFragmentB newInstance() {
        return new MainFragmentB();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main_meeting;
    }

    @Override
    protected int getTitleBarId() {
        return R.id.title;
    }

    @Override
    protected void initView() {
        mTextMonthDay = findViewById(R.id.tv_month_day);
        mTextYear = findViewById(R.id.tv_year);
        mTextLunar = findViewById(R.id.tv_lunar);
        mRelativeTool = findViewById(R.id.rl_tool);
        mCalendarView = findViewById(R.id.calendarView);
        mTextCurrentDay = findViewById(R.id.tv_current_day);
        mTextMonthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mCalendarLayout.isExpand()) {
                    mCalendarLayout.expand();
                    return;
                }
                mCalendarView.showYearSelectLayout(mYear);
                mTextLunar.setVisibility(View.GONE);
                mTextYear.setVisibility(View.GONE);
                mTextMonthDay.setText(String.valueOf(mYear));
            }
        });
        findViewById(R.id.fl_current).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarView.scrollToCurrent();
            }
        });
        mCalendarLayout = findViewById(R.id.calendarLayout);
        mCalendarView.setOnCalendarSelectListener(this);
        mCalendarView.setOnYearChangeListener(this);
        mTextYear.setText(String.valueOf(mCalendarView.getCurYear()));
        mYear = mCalendarView.getCurYear();
        mTextMonthDay.setText(mCalendarView.getCurMonth() + "月" + mCalendarView.getCurDay() + "日");
        mTextLunar.setText("今日");
        mTextCurrentDay.setText(String.valueOf(mCalendarView.getCurDay()));


        datas = new ArrayList<>();
        mainMeetingAdapter = new MainMeetingAdapter(getFragmentActivity(), datas);
        mainMeetingAdapter.setListener(new MainMeetingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String meetingId) {
                Intent intent = new Intent(getFragmentActivity(), MeetingActivity.class);
                intent.putExtra("id", meetingId);
                startActivity(intent);
            }
        });

        mRecyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getFragmentActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        mRecyclerView.setAdapter(mainMeetingAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected void initData() {
        Network.getInstance().queryMeetingByUid(Constants.uid, null, null).enqueue(new Callback<List<Meeting>>() {
            @Override
            public void onResponse(Call<List<Meeting>> call, Response<List<Meeting>> response) {
                if(response.body() != null) {
                    meetings = response.body();
                    Map<String, Calendar> map = new HashMap<>();
                    for(Meeting meeting : meetings){
                        if(!TextUtils.isEmpty(meeting.getDate())) {
                            Calendar calendar = getSchemeCalendar(meeting.getDate());
                            map.put(calendar.toString(), calendar);
                        }
                    }
                    mCalendarView.setSchemeDate(map);

                    Calendar calendar = mCalendarView.getSelectedCalendar();
                    filterMeetings(calendar.getYear(), calendar.getMonth(), calendar.getDay());
                }
            }

            @Override
            public void onFailure(Call<List<Meeting>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme(text);
        calendar.addScheme(new Calendar.Scheme());
        calendar.addScheme(0xFF008800, "假");
        calendar.addScheme(0xFF008800, "节");
        return calendar;
    }

    private Calendar getSchemeCalendar(String date) {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(TimeUtils.string2Date(date, new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())));

        java.util.Calendar curCal = java.util.Calendar.getInstance();
        curCal.setTime(new Date());

        Calendar calendar = new Calendar();
        calendar.setYear(cal.get(java.util.Calendar.YEAR));
        calendar.setMonth(cal.get(java.util.Calendar.MONTH) + 1);
        calendar.setDay(cal.get(java.util.Calendar.DAY_OF_MONTH));
        if(cal.getTimeInMillis() > curCal.getTimeInMillis() - 24 * 60 * 60 * 1000) {
            calendar.setSchemeColor(0xFFdf1356);
        } else {
            calendar.setSchemeColor(0xFFDCDCDC);
        }
        calendar.setScheme("议");
        return calendar;
    }


    @Override
    public void onCalendarOutOfRange(Calendar calendar) {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        mTextLunar.setVisibility(View.VISIBLE);
        mTextYear.setVisibility(View.VISIBLE);
        mTextMonthDay.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
        mTextYear.setText(String.valueOf(calendar.getYear()));
        mTextLunar.setText(calendar.getLunar());
        mYear = calendar.getYear();
        filterMeetings(calendar.getYear(), calendar.getMonth(), calendar.getDay());
    }

    @Override
    public void onYearChange(int year) {
        mTextMonthDay.setText(String.valueOf(year));
    }

    private void filterMeetings(int year, int month, int day) {
        Log.i("filter", year + " " + month + " " + day);
        if(meetings == null) {
            return;
        }

        datas.clear();
        for(Meeting meeting : meetings){
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(TimeUtils.string2Date(meeting.getDate(), new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())));
            Log.i("filter", cal.get(java.util.Calendar.YEAR) + " " + cal.get(java.util.Calendar.MONTH) + " " + cal.get(java.util.Calendar.DAY_OF_MONTH));
            if(cal.get(java.util.Calendar.YEAR) == year && cal.get(java.util.Calendar.MONTH) + 1 == month && cal.get(java.util.Calendar.DAY_OF_MONTH) == day) {
                datas.add(meeting);
            }
        }
        mainMeetingAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
    }
}