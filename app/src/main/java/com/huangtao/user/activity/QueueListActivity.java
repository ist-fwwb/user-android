package com.huangtao.user.activity;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;

import com.huangtao.user.R;
import com.huangtao.user.adapter.QueueListAdapter;
import com.huangtao.user.common.Constants;
import com.huangtao.user.common.MyActivity;
import com.huangtao.user.model.QueueNode;
import com.huangtao.user.network.Network;
import com.huangtao.user.widget.RecyclerViewDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QueueListActivity extends MyActivity {

    @BindView(R.id.list)
    RecyclerView recyclerView;

    QueueListAdapter queueListAdapter;

    List<QueueNode> datas;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_queue_list;
    }

    @Override
    protected int getTitleBarId() {
        return R.id.title;
    }

    @Override
    protected void initView() {
        datas = new ArrayList<>();
        queueListAdapter = new QueueListAdapter(datas);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setAdapter(queueListAdapter);
        recyclerView.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.VERTICAL, 20, getColor(R.color.douban_gray_15_percent)));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        queueListAdapter.setListener(new QueueListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(QueueListActivity.this, QueueActivity.class);
                intent.putExtra("id", datas.get(position).getId());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initData() {
        showProgressBar();
        Network.getInstance().getQueueNodes(Constants.uid).enqueue(new Callback<List<QueueNode>>() {
            @Override
            public void onResponse(Call<List<QueueNode>> call, Response<List<QueueNode>> response) {
                hideProgressBar();
                if (response.body() != null) {
                    datas.clear();
                    datas.addAll(response.body());
                    queueListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<QueueNode>> call, Throwable t) {
                hideProgressBar();
                toast("请求失败");
                t.printStackTrace();
            }
        });
    }
}
