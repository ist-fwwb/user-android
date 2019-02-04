package com.huangtao.user.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.huangtao.user.R;
import com.huangtao.user.common.MyActivity;
import com.huangtao.user.helper.CommonUtils;
import com.huangtao.user.model.meta.MeetingRoomUtils;
import com.huangtao.user.model.meta.Size;
import com.huangtao.widget.ClearEditText;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

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

    @BindView(R.id.equipment_picker)
    RelativeLayout equipmentPicker;
    @BindView(R.id.equipment)
    TextView equipment;

    @BindView(R.id.size_picker)
    RelativeLayout sizePicker;
    @BindView(R.id.size)
    TextView size;

    @BindView(R.id.radio_group_sign)
    RadioGroup radioGroup;

    @BindView(R.id.submit)
    Button submit;

    private ProgressDialog clipboardDialog;

    private String datePicked;
    private int startTimePicked, endTimePicked;
    private Set<MeetingRoomUtils> equipmentPicked;
    private Size sizePicked;

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
        clipboardDialog.setMessage("正在智能分析…");
        clipboardDialog.show();

        radioGroup.check(R.id.radio_sign);

        datePicker.setOnClickListener(this);
        startTimePicker.setOnClickListener(this);
        endTimePicker.setOnClickListener(this);
        equipmentPicker.setOnClickListener(this);
        sizePicker.setOnClickListener(this);
        submit.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        equipmentPicked = new HashSet<>();
        sizePicked = Size.SMALL;

        analyzeClipboard();
    }

    /**
     * NLP分析剪贴板内容，尽可能填充页面
     */
    private void analyzeClipboard() {

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
        } else if (v == equipmentPicker) {
            showEquipmentDialog();
        } else if (v == sizePicker) {
            showSizeDialog();
        } else if (v == submit) {
            submit();
        }
    }

    private void showEquipmentDialog() {
        final String[] items = {"空调", "黑板", "桌子", "投影仪", "电源", "无线网络", "有线网络", "电视"};
        final MeetingRoomUtils[] itemsEnum = {MeetingRoomUtils.AIRCONDITIONER, MeetingRoomUtils
                .BLACKBOARD, MeetingRoomUtils.TABLE, MeetingRoomUtils.PROJECTOR, MeetingRoomUtils
                .POWERSUPPLY};
        // 设置默认选中的选项，全为false默认均未选中
        final boolean initChoiceSets[] = new boolean[8];
        for(int i=0;i<5;i++){
            initChoiceSets[i] = equipmentPicked.contains(itemsEnum[i]);
        }
        AlertDialog.Builder multiChoiceDialog = new AlertDialog.Builder(this);
        multiChoiceDialog.setTitle("请选择设备需求");
        multiChoiceDialog.setMultiChoiceItems(items, initChoiceSets,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which,
                                        boolean isChecked) {
                        if (which < itemsEnum.length) {
                            if (isChecked) {
                                equipmentPicked.add(itemsEnum[which]);
                            } else {
                                equipmentPicked.remove(itemsEnum[which]);
                            }
                        }

                        String str = "";
                        for (int i = 0; i < itemsEnum.length; i++) {
                            if (equipmentPicked.contains(itemsEnum[i])) {
                                str += items[i];
                                str += ";";
                            }
                        }

                        if(!str.isEmpty())
                            equipment.setText(str.substring(0, str.length() - 1));
                        else{
                            equipment.setText("未选择");
                        }
                    }
                });
        multiChoiceDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        multiChoiceDialog.show();
    }

    private void showSizeDialog() {
        final String[] items = {"大", "中", "小"};
        final Size[] itemsEnum = {Size.BIG, Size.MIDDLE, Size.SMALL};
        int checkedItem = 0;
        for (int i = 0; i < itemsEnum.length; i++) {
            if (itemsEnum[i] == sizePicked) {
                checkedItem = i;
                break;
            }
        }

        AlertDialog.Builder singleChoiceDialog = new AlertDialog.Builder(this);
        singleChoiceDialog.setTitle("请选择规格需求");
        singleChoiceDialog.setSingleChoiceItems(items, checkedItem,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sizePicked = itemsEnum[which];
                        size.setText(items[which]);
                        dialog.dismiss();
                    }
                });
        singleChoiceDialog.show();
    }

    private void submit() {
        showProgressBar();
    }

}
