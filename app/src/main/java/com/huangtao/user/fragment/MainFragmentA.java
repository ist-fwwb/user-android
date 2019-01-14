package com.huangtao.user.fragment;

import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.hjq.bar.TitleBar;
import com.huangtao.user.R;
import com.huangtao.user.activity.MeetingroomListActivity;
import com.huangtao.user.common.MyLazyFragment;
import com.huangtao.user.widget.XCollapsingToolbarLayout;

import butterknife.BindView;

public class MainFragmentA extends MyLazyFragment
        implements XCollapsingToolbarLayout.OnScrimsListener, View.OnClickListener {

    @BindView(R.id.abl_test_bar)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.ctl_test_bar)
    XCollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.t_test_title)
    Toolbar mToolbar;
    @BindView(R.id.tb_test_a_bar)
    TitleBar mTitleBar;

    @BindView(R.id.tv_test_search)
    TextView mSearchView;

    @BindView(R.id.appoint)
    LinearLayout appoint;
    @BindView(R.id.appoint_smart)
    LinearLayout appointSmart;
    @BindView(R.id.add_meeting)
    LinearLayout addMeeting;

    public static MainFragmentA newInstance() {
        return new MainFragmentA();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main_home;
    }

    @Override
    protected int getTitleBarId() {
        return R.id.tb_test_a_bar;
    }

    @Override
    protected void initView() {
        // 给这个ToolBar设置顶部内边距，才能和TitleBar进行对齐
        ImmersionBar.setTitleBar(getFragmentActivity(), mToolbar);

        //设置渐变监听
        mCollapsingToolbarLayout.setOnScrimsListener(this);

        mSearchView.setOnClickListener(this);
        appoint.setOnClickListener(this);
        appointSmart.setOnClickListener(this);
        addMeeting.setOnClickListener(this);
    }

    @Override
    protected void initData() {

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

    /**
     * {@link XCollapsingToolbarLayout.OnScrimsListener}
     */
    @Override
    public void onScrimsStateChange(boolean shown) {
        // CollapsingToolbarLayout 发生了渐变
        if (shown) {
            mSearchView.setBackgroundResource(R.drawable.bg_home_search_bar_gray);
            getStatusBarConfig().statusBarDarkFont(true).init();
        }else {
            mSearchView.setBackgroundResource(R.drawable.bg_home_search_bar_transparent);
            getStatusBarConfig().statusBarDarkFont(false).init();
        }
    }


    @Override
    public void onClick(View v) {
        if (v == mSearchView) {

        } else if (v == appoint) {
            startActivity(MeetingroomListActivity.class);
        } else if (v == appointSmart) {

        } else if (v == addMeeting) {

        }
    }
}