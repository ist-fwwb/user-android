package com.huangtao.user.activity;

import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.huangtao.user.R;
import com.huangtao.user.common.MyActivity;
import com.huangtao.user.helper.CommonUtils;
import com.huangtao.user.model.QueueNode;
import com.huangtao.user.model.meta.MeetingRoomUtils;
import com.huangtao.user.network.Network;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class QueueActivity extends MyActivity implements View.OnClickListener {

    @BindView(R.id.information)
    TextView information;

    @BindView(R.id.heading)
    TextView heading;

    @BindView(R.id.description)
    TextView description;

    @BindView(R.id.date)
    TextView date;

    @BindView(R.id.time)
    TextView time;

    @BindView(R.id.equipment)
    TextView equipment;

    @BindView(R.id.size)
    TextView size;

    @BindView(R.id.sign)
    TextView sign;

    @BindView(R.id.quit)
    Button quit;

    private String informationStr = "将第一时间为您预定符合要求的空闲会议室";

    private QueueNode queueNode;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_queue;
    }

    @Override
    protected int getTitleBarId() {
        return R.id.title;
    }

    @Override
    protected void initView() {
        getStatusBarConfig().statusBarDarkFont(false).init();
        quit.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        String id;
        if ((id = getIntent().getStringExtra("id")) == null) {
            return;
        }

        showProgressBar();
        Network.getInstance().getQueueNodeById(id).enqueue(new Callback<QueueNode>() {
            @Override
            public void onResponse(Call<QueueNode> call, Response<QueueNode> response) {
                hideProgressBar();
                if(response.body() != null) {
                    queueNode = response.body();

                    StringBuilder informationSb = new StringBuilder();
                    informationSb.append("创建于 ");
                    informationSb.append(TimeUtils.millis2String(Long.parseLong(queueNode.getId())));
                    informationSb.append(" ,");
                    informationSb.append(informationStr);
                    information.setText(informationSb.toString());

                    if (queueNode.getHeading() == null || queueNode.getHeading().isEmpty()) {
                        heading.setText("无主题");
                    } else {
                        heading.setText(queueNode.getHeading());
                    }

                    if (queueNode.getDescription() == null || queueNode.getDescription().isEmpty()) {
                        description.setText("无内容");
                    } else {
                        description.setText(queueNode.getDescription());
                    }

                    date.setText(queueNode.getDate());
                    time.setText(CommonUtils.getFormatTime(queueNode.getTimeRange().getStart(), queueNode.getTimeRange().getEnd()));

                    StringBuilder utilsSb = new StringBuilder();
                    for(MeetingRoomUtils utils : queueNode.getMeetingRoomUtilsList()){
                        switch (utils){
                            case AIRCONDITIONER:
                                utilsSb.append("空调");
                                break;
                            case BLACKBOARD:
                                utilsSb.append("黑板");
                                break;
                            case TABLE:
                                utilsSb.append("桌子");
                                break;
                            case PROJECTOR:
                                utilsSb.append("投影仪");
                                break;
                            case POWERSUPPLY:
                                utilsSb.append("电源");
                                break;
                        }
                        utilsSb.append(";");
                    }
                    equipment.setText(utilsSb.length() == 0 ? "无" : utilsSb.substring(0, utilsSb.length() - 1));

                    switch (queueNode.getSize()){
                        case BIG:
                            size.setText("大");
                            break;
                        case MIDDLE:
                            size.setText("中");
                            break;
                        case SMALL:
                            size.setText("小");
                            break;
                    }

                    sign.setText(queueNode.getNeedSignIn() ? "是" : "否");

                } else {
                    toast("无数据");
                }
            }

            @Override
            public void onFailure(Call<QueueNode> call, Throwable t) {
                hideProgressBar();
                t.printStackTrace();
                toast("访问失败");
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v == quit) {
            showDialog("正在为您实时监控，确定要取消排队吗？", "退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Network.getInstance(ScalarsConverterFactory.create()).deleteQueueNodeById(queueNode.getId()).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            showDialog("取消成功", "确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            toast("取消失败");
                            t.printStackTrace();
                        }
                    });
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
