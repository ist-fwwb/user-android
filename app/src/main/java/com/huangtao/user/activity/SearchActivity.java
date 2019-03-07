package com.huangtao.user.activity;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.huangtao.user.R;
import com.huangtao.user.common.MyActivity;
import com.huangtao.user.model.SearchResult;
import com.huangtao.user.network.Network;

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

    }

    @Override
    public void onClick(View v) {
        if(v == search){
            if (!TextUtils.isEmpty(editText.getText().toString())) {
                Network.getInstance().search(editText.getText().toString()).enqueue(new Callback<List<SearchResult>>() {
                    @Override
                    public void onResponse(Call<List<SearchResult>> call, Response<List<SearchResult>> response) {

                    }

                    @Override
                    public void onFailure(Call<List<SearchResult>> call, Throwable t) {

                    }
                });
            }
        }
    }
}
