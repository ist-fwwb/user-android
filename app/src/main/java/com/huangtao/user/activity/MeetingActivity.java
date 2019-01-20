package com.huangtao.user.activity;

import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.huangtao.dialog.QRCodeDialog;
import com.huangtao.user.R;
import com.huangtao.user.common.Constants;
import com.huangtao.user.common.MyActivity;
import com.huangtao.user.helper.CommonUtils;
import com.huangtao.user.helper.DimensUtils;
import com.huangtao.user.model.Meeting;
import com.huangtao.user.model.User;
import com.huangtao.user.network.FileManagement;
import com.huangtao.user.network.Network;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MeetingActivity extends MyActivity {

    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.share)
    ImageView share;
    @BindView(R.id.location)
    TextView location;
    @BindView(R.id.heading)
    TextView heading;
    @BindView(R.id.status)
    TextView status;

    @BindView(R.id.content)
    TextView content;

    @BindView(R.id.host_head)
    ImageView hostHead;
    @BindView(R.id.host_name)
    TextView hostName;

    @BindView(R.id.participant_number)
    TextView participantNumber;
    @BindView(R.id.participant_container)
    GridLayout participantContainer;

    @BindView(R.id.sign)
    TextView sign;
    @BindView(R.id.type)
    TextView type;

    @BindView(R.id.number)
    TextView number;

    @BindView(R.id.enter)
    Button enter;

    Meeting meeting;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_meeting;
    }

    @Override
    protected int getTitleBarId() {
        return R.id.title;
    }

    @Override
    protected void initView() {
        getStatusBarConfig().statusBarDarkFont(false).init();
    }

    @Override
    protected void initData() {
        if ((meeting = getIntent().getParcelableExtra("meeting")) == null) {
            String id;
            if ((id = getIntent().getStringExtra("id")) == null) {
//                finish();
            }
            id = "5c41d7e41bdd3400130912d7";
            Network.getInstance().queryMeetingById(id).enqueue(new Callback<Meeting>() {
                @Override
                public void onResponse(Call<Meeting> call, Response<Meeting> response) {
                    if(response.body() != null) {
                        meeting = response.body();
                        init();
                    } else {
                        toast("返回值为空");
                    }
                }

                @Override
                public void onFailure(Call<Meeting> call, Throwable t) {
                    toast("网络请求失败");
                    t.printStackTrace();
                }
            });
        } else {
            init();
        }
    }

    /**
     * 传入meeting对象 or id后，更新ui
     */
    private void init() {
        String first = meeting.getStartTime() / 2 + ":" + ((meeting.getStartTime() + 1) % 2 == 0 ? "30" : "00");
        String last = meeting.getEndTime() / 2 + ":" + ((meeting.getEndTime() + 1) % 2 == 0 ? "30" : "00");
        time.setText(first+" - "+last);

        String month = meeting.getDate().split("-")[1];
        final String day = meeting.getDate().split("-")[2];
        date.setText(month + "/" + day + "\n" + CommonUtils.dateToWeek(meeting.getDate()));

        location.setText(meeting.getLocation());

        heading.setText(!meeting.getHeading().isEmpty() ? meeting.getHeading() : "无主题");

        String statusStr = null;
        switch (meeting.getStatus()){
            case Pending:
                statusStr = "未开始";
                break;
            case Running:
                statusStr = "进行中";
                enter.setEnabled(false);
                break;
            case Stopped:
                statusStr = "已结束";
                enter.setEnabled(false);
                break;
            case Cancelled:
                statusStr = "已取消";
                enter.setEnabled(false);
                break;
        }
        status.setText(statusStr);

        content.setText(!meeting.getDescription().isEmpty() ? meeting.getDescription() : "无内容");

        // 参会者
        participantNumber.setText("参会者：\n   (" + meeting.getAttendants().size() + ")");
        // TODO 参会者信息

        // 是否需要签到
        if(!meeting.isNeedSignIn()){
            sign.setText("否");
        }

        // 会议类型
        String typeStr = null;
        switch (meeting.getType()){
            case COMMON:
                typeStr = "普通";
                break;
            case URGECY:
                typeStr = "紧急";
                type.setTextColor(getColor(R.color.douban_red_80_percent));
                break;
        }
        type.setText(typeStr);

        // 按钮
        if(meeting.getAttendants().keySet().contains(Constants.uid)){
            enter.setText("退出会议");
            enter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        } else {
            enter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO 加入会议
                }
            });
        }

        // 会议代码
        number.setText(meeting.getAttendantNum());

        // 主持人
        Network.getInstance().queryUserById(meeting.getHostId()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.body() != null) {
                    User host = response.body();
                    hostName.setText(host.getName());

                    Observable.just(FileManagement.getUserHead(MeetingActivity.this, host))
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<String>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onNext(String s) {
                                    if(!s.isEmpty()){
                                        hostHead.setImageBitmap(CommonUtils.getLoacalBitmap(s));
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onComplete() {

                                }
                            });

                } else {
                    toast("返回值为空");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                toast("网络请求失败");
                t.printStackTrace();
            }
        });


        // 二维码
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QRCodeDialog.Builder builder = new QRCodeDialog.Builder(MeetingActivity.this);
                builder.setMessage(meeting.getAttendantNum());
                builder.setQrcode(CodeUtils.createImage(meeting.getAttendantNum(), 400, 400, null));
                builder.setWidth((int) (DimensUtils.getScreenWidth(MeetingActivity.this) * 0.7));
                builder.setHeight((int) (DimensUtils.getScreenHeight(MeetingActivity.this) * 0.5));
                builder.create().show();
            }
        });

    }
}
