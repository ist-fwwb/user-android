package com.huangtao.user.fragment;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;

import com.hjq.bar.TitleBar;
import com.huangtao.user.R;
import com.huangtao.user.adapter.MessageAdapter;
import com.huangtao.user.common.Constants;
import com.huangtao.user.common.MyLazyFragment;
import com.huangtao.user.model.Message;
import com.huangtao.user.model.meta.MessageStatus;
import com.huangtao.user.network.Network;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainFragmentC extends MyLazyFragment {

    @BindView(R.id.title)
    TitleBar titleBar;

    @BindView(R.id.list)
    RecyclerView recyclerView;

    MessageAdapter messageAdapter;

    List<Message> datas;

    public static MainFragmentC newInstance() {
        return new MainFragmentC();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main_message;
    }

    @Override
    protected int getTitleBarId() {
        return R.id.title;
    }

    @Override
    protected void initView() {
        datas = new ArrayList<>();
        messageAdapter = new MessageAdapter(getFragmentActivity(), datas);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getFragmentActivity());
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setAdapter(messageAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        messageAdapter.setListener(new MessageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Message msg) {
                if(msg.getMessageStatus() == MessageStatus.NEW){
                    msg.setMessageStatus(MessageStatus.READ);
                    Network.getInstance(ScalarsConverterFactory.create()).modifyMessageStatus(msg.getId(), MessageStatus.READ).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {

                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                        }
                    });
                }
            }
        });
    }

    @Override
    protected void initData() {
        Network.getInstance().getMessages(Constants.uid).enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                if(response.body() != null) {
                    for (int i = response.body().size() - 1; i >= 0; i--) {
                        datas.add(response.body().get(i));
                    }
                    messageAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                t.printStackTrace();
                toast("加载失败");
            }
        });
    }

    @Override
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
    }
}