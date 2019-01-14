package com.huangtao.user.activity;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;

import com.huangtao.user.R;
import com.huangtao.user.adapter.MeetingroomListAdapter;
import com.huangtao.user.common.MyActivity;

import butterknife.BindView;

public class MeetingroomListActivity extends MyActivity {

    @BindView(R.id.list)
    RecyclerView recyclerView;

    MeetingroomListAdapter meetingroomListAdapter;

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
        meetingroomListAdapter = new MeetingroomListAdapter();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setAdapter(meetingroomListAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected void initData() {

    }
}
