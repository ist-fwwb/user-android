package com.huangtao.user.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.huangtao.user.R;
import com.huangtao.user.common.MyActivity;
import com.huangtao.user.helper.CommonUtils;
import com.huangtao.widget.ClearEditText;

import java.util.Calendar;

import butterknife.BindView;

public class SmartAppointActivity extends MyActivity implements View.OnClickListener {

    @BindView(R.id.heading)
    ClearEditText heading;

    @BindView(R.id.description)
    ClearEditText description;

    @BindView(R.id.date_picker)
    RelativeLayout datePicker;
    @BindView(R.id.date)
    TextView date;

    @BindView(R.id.start_time_picker)
    RelativeLayout startTimePicker;
    @BindView(R.id.start_time)
    TextView startTime;

    @BindView(R.id.end_time_picker)
    RelativeLayout endTimePicker;
    @BindView(R.id.end_time)
    TextView endTime;

    private ProgressDialog clipboardDialog;

    private String datePicked;
    private int startTimePicked, endTimePicked;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_smart_appoint;
    }

    @Override
    protected int getTitleBarId() {
        return R.id.title;
    }

    @Override
    protected void initView() {
        clipboardDialog = new ProgressDialog(this);
        clipboardDialog.setMessage("正在分析剪贴板内容");
        clipboardDialog.show();

        datePicker.setOnClickListener(this);
        startTimePicker.setOnClickListener(this);
        endTimePicker.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(final View v) {
        if (v == datePicker) {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    datePicked = year + "-" + (month + 1) + "-" + dayOfMonth;
                    date.setText(datePicked + " " + CommonUtils.dateToWeek(datePicked));
                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        } else if (v == startTimePicker || v == endTimePicker) {
            Calendar calendar = Calendar.getInstance();
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    int timeInt = hourOfDay * 2 + minute / 30;
                    String timeStr;
                    if (v == startTimePicker) {
                        timeStr = hourOfDay + ":" + (minute < 30 ? "00" : "30");
                        startTime.setText(timeStr);
                        startTimePicked = timeInt;
                    } else {
                        if (minute < 30) {
                            timeStr = hourOfDay + ":30";
                        } else {
                            timeStr = (hourOfDay + 1) + ":00";
                        }
                        endTime.setText(timeStr);
                        endTimePicked = timeInt;
                    }
                }
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
            timePickerDialog.show();
        }
    }
}
