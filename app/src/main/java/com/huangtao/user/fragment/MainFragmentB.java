package com.huangtao.user.fragment;

import android.view.View;

import com.huangtao.user.R;
import com.huangtao.user.common.MyLazyFragment;
import com.huangtao.widget.CountdownView;

import butterknife.BindView;

public class MainFragmentB extends MyLazyFragment
        implements View.OnClickListener {

    @BindView(R.id.cv_test_countdown)
    CountdownView mCountdownView;

    public static MainFragmentB newInstance() {
        return new MainFragmentB();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test_b;
    }

    @Override
    protected int getTitleBarId() {
        return R.id.tb_test_b_title;
    }

    @Override
    protected void initView() {
        mCountdownView.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    /**
     * {@link View.OnClickListener}
     */
    @Override
    public void onClick(View v) {
        if (v == mCountdownView) {
            toast(getResources().getString(R.string.countdown_code_send_succeed));
        }
    }

    @Override
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
    }
}