package com.huangtao.user.activity;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;

import com.hjq.bar.TitleBar;
import com.huangtao.user.R;
import com.huangtao.user.adapter.MeetingNoteAdapter;
import com.huangtao.user.common.Constants;
import com.huangtao.user.common.MyActivity;
import com.huangtao.user.model.MeetingNote;
import com.huangtao.user.model.MeetingNoteWrapper;
import com.huangtao.user.model.meta.MeetingNoteType;
import com.huangtao.user.network.Network;
import com.huangtao.user.widget.RecyclerViewDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MeetingNoteListActivity extends MyActivity {

    @BindView(R.id.title)
    TitleBar titleBar;

    @BindView(R.id.list)
    RecyclerView recyclerView;

    MeetingNoteAdapter meetingNoteAdapter;

    List<MeetingNoteWrapper> datas;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_meeting_note_list;
    }

    @Override
    protected int getTitleBarId() {
        return R.id.title;
    }

    @Override
    protected void initView() {
        datas = new ArrayList<>();
        meetingNoteAdapter = new MeetingNoteAdapter(datas);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setAdapter(meetingNoteAdapter);
        recyclerView.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.VERTICAL, 20, getColor(R.color.douban_gray_15_percent)));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        meetingNoteAdapter.setOnItemClickListener(new MeetingNoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                MeetingNote meetingNote = datas.get(position).getMeetingNote();
                Intent intent;
                if(meetingNote.getMeetingNoteType() == MeetingNoteType.VOICE) {
                    intent = new Intent(MeetingNoteListActivity.this, VoiceNoteActivity.class);
                } else {
                    intent = new Intent(MeetingNoteListActivity.this, HtmlNoteActivity.class);
                }
                intent.putExtra("id", datas.get(position).getMeetingNote().getId());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initData() {
        showProgressBar();
        if(getIntent().getBooleanExtra("isFromMeeting", false)) {
            titleBar.setTitle("笔记列表");

            String meetingId = getIntent().getStringExtra("meetingId");
            Network.getInstance().getMeetingNotes(Constants.uid, meetingId, null).enqueue(new Callback<List<MeetingNoteWrapper>>() {
                @Override
                public void onResponse(Call<List<MeetingNoteWrapper>> call, Response<List<MeetingNoteWrapper>> response) {
                    hideProgressBar();
                    if (response.body() != null) {
                        datas.addAll(response.body());
                        meetingNoteAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<List<MeetingNoteWrapper>> call, Throwable t) {
                    hideProgressBar();
                    toast("加载失败");
                    t.printStackTrace();
                }
            });


        } else if(getIntent().getBooleanExtra("isFromPerson", false)) {
            titleBar.setTitle("收藏笔记");

            // TODO 获取用户收藏笔记列表api

        }

    }
}
