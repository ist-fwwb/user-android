package com.huangtao.user.activity;

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
            finish();
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

        }
    }
}
