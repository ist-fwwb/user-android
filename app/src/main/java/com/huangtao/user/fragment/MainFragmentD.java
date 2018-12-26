package com.huangtao.user.fragment;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.gyf.barlibrary.ImmersionBar;
import com.hjq.bar.TitleBar;
import com.huangtao.user.R;
import com.huangtao.user.activity.LoginActivity;
import com.huangtao.user.common.MyLazyFragment;
import com.huangtao.user.widget.XCollapsingToolbarLayout;

import butterknife.BindView;

public class MainFragmentD extends MyLazyFragment
        implements View.OnClickListener, XCollapsingToolbarLayout.OnScrimsListener {

    @BindView(R.id.ctl_bar)
    XCollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.tool_bar)
    Toolbar mToolbar;
    @BindView(R.id.title_bar)
    TitleBar mTitleBar;
    @BindView(R.id.login)
    Button login;

    public static MainFragmentD newInstance() {
        return new MainFragmentD();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main_person;
    }

    @Override
    protected int getTitleBarId() {
        return R.id.title_bar;
    }

    @Override
    protected void initFragment() {
        super.initFragment();

        getStatusBarConfig().statusBarDarkFont(false).init();
    }

    @Override
    protected void initView() {
        ImmersionBar.setTitleBar(getFragmentActivity(), mToolbar);

        //设置渐变监听
        mCollapsingToolbarLayout.setOnScrimsListener(this);

        login.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    /**
     * {@link View.OnClickListener}
     */
    @Override
    public void onClick(View v) {
        if(v == login){
            startActivity(LoginActivity.class);
        }
    }

    @Override
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
    }

    @Override
    public boolean statusBarDarkFont() {
        return mCollapsingToolbarLayout.isScrimsShown();
    }

    @Override
    public void onScrimsStateChange(boolean shown) {
        // CollapsingToolbarLayout 发生了渐变
        if (shown) {
            mTitleBar.setTitle("我的");
            getStatusBarConfig().statusBarDarkFont(true).init();
        }else {
            mTitleBar.setTitle("");
            getStatusBarConfig().statusBarDarkFont(false).init();
        }
    }
}