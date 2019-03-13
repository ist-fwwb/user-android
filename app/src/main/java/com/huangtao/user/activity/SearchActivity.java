package com.huangtao.user.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.StringUtils;
import com.huangtao.user.R;
import com.huangtao.user.adapter.SearchResultAdapter;
import com.huangtao.user.common.MyActivity;
import com.huangtao.user.helper.CommonUtils;
import com.huangtao.user.model.SearchResult;
import com.huangtao.user.model.SearchResultItem;
import com.huangtao.user.model.meta.MeetingNoteType;
import com.huangtao.user.model.meta.SearchResultType;
import com.huangtao.user.network.Network;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends MyActivity implements View.OnClickListener {

    @BindView(R.id.edittext_search)
    EditText editText;

    @BindView(R.id.search)
    TextView search;

    @BindView(R.id.list)
    RecyclerView recyclerView;

    SearchResultAdapter searchResultAdapter;
    List<SearchResultItem> datas;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected int getTitleBarId() {
        return R.id.title;
    }

    @Override
    protected void initView() {
        getStatusBarConfig().statusBarDarkFont(false).init();
        search.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        datas = new ArrayList<>();
        searchResultAdapter = new SearchResultAdapter(this, datas);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setAdapter(searchResultAdapter);
        searchResultAdapter.setListener(new SearchResultAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(SearchResultItem item) {
                Intent intent;
                switch (item.getType()) {
                    case MEETING:
                        intent = new Intent(SearchActivity.this, MeetingActivity.class);
                        intent.putExtra("id", item.getMeeting().getId());
                        startActivity(intent);
                        break;
                    case NOTE:
                        if(item.getNote().getMeetingNoteType() == MeetingNoteType.VOICE)
                            intent = new Intent(SearchActivity.this, VoiceNoteActivity.class);
                        else
                            intent = new Intent(SearchActivity.this, HtmlNoteActivity.class);
                        intent.putExtra("id", item.getNote().getId());
                        startActivity(intent);
                        break;
                    case USER:
                        final List<String> contactInformation = new ArrayList<>();
                        if(!StringUtils.isEmpty(item.getUser().getName())){
                            contactInformation.add(item.getUser().getName());
                        }

                        if(!StringUtils.isEmpty(item.getUser().getPhone())){
                            contactInformation.add(item.getUser().getPhone());
                        }

                        AlertDialog.Builder listDialog = new AlertDialog.Builder(SearchActivity.this);
                        listDialog.setItems(contactInformation.toArray(new String[0]), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                CommonUtils.setClipboardText(SearchActivity.this, contactInformation.get(which));
                                Toast.makeText(SearchActivity.this, "已复制到剪贴板", Toast.LENGTH_SHORT).show();
                            }
                        });
                        listDialog.show();
                        break;
                    case MEETINGROOM:
                        intent = new Intent(SearchActivity.this, MeetingRoomActivity.class);
                        intent.putExtra("meetingroom", item.getMeetingRoom());
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v == search){
            if (!TextUtils.isEmpty(editText.getText().toString())) {
                showProgressBar();
                Network.getInstance().search(editText.getText().toString()).enqueue(new Callback<SearchResult>() {
                    @Override
                    public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                        if(response.body() != null && response.body().getTotalCount() > 0) {
                            datas.clear();

                            for (int i = 0; i < response.body().getMeetings().size(); i++) {
                                SearchResultItem item = new SearchResultItem(SearchResultType.MEETING);
                                item.setMeeting(response.body().getMeetings().get(i));
                                if(i == 0) {
                                    item.setFirst(true);
                                }
                                datas.add(item);
                            }

                            for (int i = 0; i < response.body().getMeetingNotes().size(); i++) {
                                SearchResultItem item = new SearchResultItem(SearchResultType.NOTE);
                                item.setNote(response.body().getMeetingNotes().get(i));
                                if(i == 0) {
                                    item.setFirst(true);
                                }
                                datas.add(item);
                            }

                            for (int i = 0; i < response.body().getUsers().size(); i++) {
                                SearchResultItem item = new SearchResultItem(SearchResultType.USER);
                                item.setUser(response.body().getUsers().get(i));
                                if(i == 0) {
                                    item.setFirst(true);
                                }
                                datas.add(item);
                            }

                            for (int i = 0; i < response.body().getMeetingRooms().size(); i++) {
                                SearchResultItem item = new SearchResultItem(SearchResultType.MEETINGROOM);
                                item.setMeetingRoom(response.body().getMeetingRooms().get(i));
                                if(i == 0) {
                                    item.setFirst(true);
                                }
                                datas.add(item);
                            }

                            searchResultAdapter.notifyDataSetChanged();
                        }
                        hideProgressBar();
                    }

                    @Override
                    public void onFailure(Call<SearchResult> call, Throwable t) {
                        hideProgressBar();
                        t.printStackTrace();
                    }
                });
            }
        }
    }
}
