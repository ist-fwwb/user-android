package com.huangtao.user.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.huangtao.user.R;
import com.huangtao.user.common.Constants;
import com.huangtao.user.common.MyActivity;
import com.huangtao.user.helper.CalenderUtils;
import com.huangtao.user.helper.CommonUtils;
import com.huangtao.user.model.LexerResult;
import com.huangtao.user.model.Meeting;
import com.huangtao.user.model.QueueNode;
import com.huangtao.user.model.TimeRange;
import com.huangtao.user.model.meta.MeetingRoomUtils;
import com.huangtao.user.model.meta.MeetingType;
import com.huangtao.user.model.meta.Size;
import com.huangtao.user.network.Network;
import com.huangtao.widget.ClearEditText;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    private String[] items = {"空调", "黑板", "桌子", "投影仪", "电源", "无线网络", "有线网络", "电视"};
    private MeetingRoomUtils[] itemsEnum = {MeetingRoomUtils.AIRCONDITIONER, MeetingRoomUtils
            .BLACKBOARD, MeetingRoomUtils.TABLE, MeetingRoomUtils.PROJECTOR, MeetingRoomUtils
            .POWERSUPPLY};

    private ProgressDialog clipboardDialog;

    private String datePicked;
    private int startTimePicked = -1, endTimePicked = -1;
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

        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                analyzeClipboard();
            }
        }, 1000);
    }

    /**
     * NLP分析剪贴板内容，尽可能填充页面
     */
    private void analyzeClipboard() {
        String clipboardText = CommonUtils.getClipboardText(this);
        if(clipboardText.isEmpty()){
            clipboardDialog.dismiss();
            return;
        }

        Network.getLexerInstance().lexer(clipboardText).enqueue(new Callback<LexerResult>() {
            @Override
            public void onResponse(Call<LexerResult> call, Response<LexerResult> response) {
                clipboardDialog.dismiss();

                LexerResult lexerResult = response.body();
                heading.setText(lexerResult.getHeading());
                description.setText(lexerResult.getDescription());

                if (lexerResult.getDate() != null) {
                    datePicked = lexerResult.getDate();
                    date.setText(datePicked);
                }

                if (lexerResult.getStartTime() >= 0) {
                    startTimePicked = lexerResult.getStartTime();
                    startTime.setText(CommonUtils.getFormatTime(startTimePicked));
                }

                if (lexerResult.getEndTime() >= 0) {
                    endTimePicked = lexerResult.getEndTime();
                    endTime.setText(CommonUtils.getFormatTime(endTimePicked));
                }

                if (lexerResult.getUtils() != null) {
                    equipmentPicked.addAll(lexerResult.getUtils());
                    equipment.setText(getEquipmentText());
                }
            }

            @Override
            public void onFailure(Call<LexerResult> call, Throwable t) {
                clipboardDialog.dismiss();
            }
        });
    }

    @Override
    public void onClick(final View v) {
        if (v == datePicker) {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    month++;
                    datePicked = year + "-" + (month < 10 ? "0" : "") + month + "-" + (dayOfMonth < 10 ? "0" : "") + dayOfMonth;
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

                        equipment.setText(getEquipmentText());
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
        if (datePicked == null || startTimePicked < 0 || endTimePicked < 0) {
            toast("日期、时间为必填项");
            return;
        }

        showProgressBar();

        Meeting meeting = new Meeting();
        meeting.setHostId(Constants.user.getId());
        meeting.setHeading(heading.getText().toString());
        meeting.setDescription(description.getText().toString());
        meeting.setDate(datePicked);
        meeting.setStartTime(startTimePicked);
        meeting.setEndTime(endTimePicked);
        meeting.setNeedSignIn(radioGroup.getCheckedRadioButtonId() == R.id.radio_sign);
        // TODO 暂时默认common
        meeting.setType(MeetingType.COMMON);

        Log.i("appoint", meeting.toString());

        Network.getInstance().appointMeetingroomIntelligent(equipmentPicked, sizePicked, meeting).enqueue(new Callback<Meeting>() {
            @Override
            public void onResponse(Call<Meeting> call, Response<Meeting> response) {
                hideProgressBar();
                Log.i("appoint", String.valueOf(response.body() == null));

                if(response.body() != null) {
                    Meeting result = response.body();
                    if (result.getId() != null) {
                        final Intent intent = new Intent(SmartAppointActivity.this, MeetingActivity.class);
                        intent.putExtra("id", result.getId());

                        showDialog("预定成功！", "确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(intent);
                                finish();
                            }
                        });

                        // 写入系统calender
                        CalenderUtils.addCalendarEvent(SmartAppointActivity.this, result
                                        .getHeading(), result.getDescription(), result.getDate(),
                                result.getStartTime(), result.getEndTime(), result.getLocation());
                    } else {
                        showDialog("暂无符合要求的会议室", "是否进入等待队列？当有会议室空闲时将立刻为您预定", "进入", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                addQueue();
                            }
                        }, "取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<Meeting> call, Throwable t) {
                hideProgressBar();
                t.printStackTrace();
            }
        });
    }

    private String getEquipmentText() {
        String str = "";
        for (int i = 0; i < itemsEnum.length; i++) {
            if (equipmentPicked.contains(itemsEnum[i])) {
                str += items[i];
                str += ";";
            }
        }

        if (!str.isEmpty())
            return str.substring(0, str.length() - 1);

        return "未选择";
    }

    private void addQueue(){
        showProgressBar();

        QueueNode queueNode = new QueueNode(Constants.uid, "1", new TimeRange(startTimePicked,
                endTimePicked), datePicked, sizePicked, radioGroup.getCheckedRadioButtonId() == R
                .id.radio_sign, heading.getText().toString(), description.getText().toString(),
                equipmentPicked);
        Network.getInstance().addQueueNode(queueNode).enqueue(new Callback<QueueNode>() {
            @Override
            public void onResponse(Call<QueueNode> call, final Response<QueueNode> response) {
                hideProgressBar();
                if(response.body() != null) {
                    showDialog("加入队列成功！", "查看", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(SmartAppointActivity.this, QueueActivity.class);
                            intent.putExtra("id", response.body().getId());
                            startActivityFinish(intent);
                            finish();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<QueueNode> call, Throwable t) {
                hideProgressBar();
                showDialog("排队失败", "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
            }
        });

    }
}
