package com.huangtao.user.activity;

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
import com.huangtao.user.network.model.ApiMeetingrooms;

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

        Network.getInstance().queryMeetingroom(1, 20, null, null).enqueue(new Callback<ApiMeetingrooms>() {
            @Override
            public void onResponse(Call<ApiMeetingrooms> call, Response<ApiMeetingrooms> response) {
                hideProgressBar();
                if (response.body() != null && response.body().getContent() != null) {
                    datas.addAll(response.body().getContent());
                    meetingroomListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ApiMeetingrooms> call, Throwable t) {
                hideProgressBar();
                toast("加载失败");
            }
        });

        meetingroomListAdapter.setOnItemClickListener(new MeetingroomListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }
        });

    }
}
