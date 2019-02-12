package com.huangtao.user.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.huangtao.user.R;
import com.huangtao.user.common.Constants;
import com.huangtao.user.common.MyActivity;
import com.huangtao.user.helper.CalenderUtils;
import com.huangtao.user.model.Meeting;
import com.huangtao.user.model.MeetingRoom;
import com.huangtao.user.model.meta.MeetingType;
import com.huangtao.user.network.Network;
import com.huangtao.widget.ClearEditText;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointActivity extends MyActivity {

    @BindView(R.id.heading)
    ClearEditText heading;

    @BindView(R.id.description)
    ClearEditText description;

    @BindView(R.id.location)
    TextView location;

    @BindView(R.id.time)
    TextView time;

    @BindView(R.id.radio_group_sign)
    RadioGroup radioGroup;

    @BindView(R.id.submit)
    Button submit;

    String date, week;
    int start, end;

    MeetingRoom meetingRoom;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_appoint;
    }

    @Override
    protected int getTitleBarId() {
        return R.id.title;
    }

    @Override
    protected void initView() {
        radioGroup.check(R.id.radio_sign);
    }

    @Override
    protected void initData() {
        meetingRoom = (MeetingRoom) getIntent().getSerializableExtra("meetingroom");
        date = getIntent().getStringExtra("date");
        week = getIntent().getStringExtra("week");
        start = getIntent().getIntExtra("start", 0);
        end = getIntent().getIntExtra("end", 0);

        location.setText(meetingRoom.getLocation());

        String first = start / 2 + ":" + ((start + 1) % 2 == 0 ? "30" : "00");
        String last = end / 2 + ":" + ((end + 1) % 2 == 0 ? "30" : "00");
        time.setText(date + " " + week + " " + first + "-" + last);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressBar();

                Meeting meeting = new Meeting();
                meeting.setHeading(heading.getText().toString());
                meeting.setDescription(description.getText().toString());
                meeting.setRoomId(meetingRoom.getId());
                //TODO 粗暴
                meeting.setDate("2019-" + date.replace(".", "-"));
                meeting.setLocation(meetingRoom.getLocation());
                meeting.setStartTime(start);
                meeting.setEndTime(end);
                meeting.setHostId(Constants.uid);
                meeting.setNeedSignIn(radioGroup.getCheckedRadioButtonId() == R.id.radio_sign);
                meeting.setType(MeetingType.COMMON);

                Network.getInstance().appointMeetingroom(meeting).enqueue(new Callback<Meeting>() {
                    @Override
                    public void onResponse(Call<Meeting> call, Response<Meeting> response) {
                        hideProgressBar();

                        if(response.body() != null) {
                            Meeting result = response.body();

                            final Intent intent = new Intent(AppointActivity.this, MeetingActivity.class);
                            intent.putExtra("id", result.getId());

                            showDialog("预定成功！", "确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(intent);

                                    // 前面的activity全都finish
                                    Intent finishIntent = new Intent("appointSuccess");
                                    sendBroadcast(finishIntent);

                                    finish();
                                }
                            });

                            // 写入系统calender
                            CalenderUtils.addCalendarEvent(AppointActivity.this, result
                                    .getHeading(), result.getDescription(), result.getDate(),
                                    result.getStartTime(), result.getEndTime());
                        }

                    }

                    @Override
                    public void onFailure(Call<Meeting> call, Throwable t) {
                        hideProgressBar();
                        toast("预定失败");
                        t.printStackTrace();
                    }
                });
            }
        });
    }
}
