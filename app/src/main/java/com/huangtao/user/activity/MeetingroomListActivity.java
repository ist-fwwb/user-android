package com.huangtao.user.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;

import com.huangtao.user.R;
import com.huangtao.user.adapter.MeetingroomListAdapter;
import com.huangtao.user.common.MyActivity;
import com.huangtao.user.model.MeetingRoom;
import com.huangtao.user.network.Network;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MeetingroomListActivity extends MyActivity {

    @BindView(R.id.list)
    RecyclerView recyclerView;

    MeetingroomListAdapter meetingroomListAdapter;

    List<MeetingRoom> datas;

    BroadcastReceiver receiver;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_meetingroom_list;
    }

    @Override
    protected int getTitleBarId() {
        return R.id.title;
    }

    @Override
    protected void initView() {
        datas = new ArrayList<>();
        meetingroomListAdapter = new MeetingroomListAdapter(datas);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setAdapter(meetingroomListAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration
                .VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected void initData() {
        showProgressBar();

        Network.getInstance().queryMeetingroom(null, null).enqueue(new Callback<List<MeetingRoom>>() {
            @Override
            public void onResponse(Call<List<MeetingRoom>> call, Response<List<MeetingRoom>> response) {
                hideProgressBar();
                if (response.body() != null) {
                    datas.addAll(response.body());
                    meetingroomListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<MeetingRoom>> call, Throwable t) {
                hideProgressBar();
                toast("加载失败");
                t.printStackTrace();
            }
        });

        meetingroomListAdapter.setOnItemClickListener(new MeetingroomListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(MeetingroomListActivity.this, MeetingRoomActivity.class);
                intent.putExtra("meetingroom", datas.get(position));
                startActivity(intent);
            }
        });

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("appointSuccess");
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                finish();
            }
        };
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
