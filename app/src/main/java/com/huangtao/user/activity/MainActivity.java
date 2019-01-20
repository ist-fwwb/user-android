package com.huangtao.user.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;

import com.huangtao.user.R;
import com.huangtao.user.adapter.MainFragmentAdapter;
import com.huangtao.user.common.Constants;
import com.huangtao.user.common.MyActivity;
import com.huangtao.user.fragment.MainFragmentDLogin;
import com.huangtao.user.helper.ActivityStackManager;
import com.huangtao.user.helper.DoubleClickHelper;
import com.huangtao.user.model.User;
import com.huangtao.user.network.Network;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends MyActivity implements
        ViewPager.OnPageChangeListener, BottomNavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.vp_home_pager)
    ViewPager mViewPager;
    @BindView(R.id.bv_home_navigation)
    BottomNavigationView mBottomNavigationView;

    private MainFragmentAdapter mPagerAdapter;

    private BroadcastReceiver receiver;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected int getTitleBarId() {
        return 0;
    }

    @Override
    protected void initView() {
        mViewPager.addOnPageChangeListener(this);

        // 不使用图标默认变色
        mBottomNavigationView.setItemIconTintList(null);
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    protected void initData() {
        mPagerAdapter = new MainFragmentAdapter(this);
        mViewPager.setAdapter(mPagerAdapter);

        // 限制页面数量
        mViewPager.setOffscreenPageLimit(mPagerAdapter.getCount());

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("login");
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mPagerAdapter.refresh(3, MainFragmentDLogin.newInstance());
                Log.i("login", "refresh");
            }
        };
        registerReceiver(receiver, intentFilter);

        if(Constants.uid != null){
            Network.getInstance().queryUserById(Constants.uid).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if(response.body() != null)
                        Constants.user = response.body();
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });
        }
    }

    /**
     * {@link ViewPager.OnPageChangeListener}
     */

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                mBottomNavigationView.setSelectedItemId(R.id.menu_home);
                break;
            case 1:
                mBottomNavigationView.setSelectedItemId(R.id.home_meeting);
                break;
            case 2:
                mBottomNavigationView.setSelectedItemId(R.id.home_message);
                break;
            case 3:
                mBottomNavigationView.setSelectedItemId(R.id.home_me);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {}

    /**
     * {@link BottomNavigationView.OnNavigationItemSelectedListener}
     */

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_home:
                mViewPager.setCurrentItem(0);
                return true;
            case R.id.home_meeting:
                mViewPager.setCurrentItem(1);
                return true;
            case R.id.home_message:
                mViewPager.setCurrentItem(2);
                return true;
            case R.id.home_me:
                mViewPager.setCurrentItem(3);
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (DoubleClickHelper.isOnDoubleClick()) {
            //移动到上一个任务栈，避免侧滑引起的不良反应
            moveTaskToBack(false);
            getHandler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    // 进行内存优化，销毁掉所有的界面
                    ActivityStackManager.getInstance().finishAllActivities();
                }
            }, 300);
        } else {
            toast(getResources().getString(R.string.home_exit_hint));
        }
    }

    @Override
    protected void onDestroy() {
        mViewPager.removeOnPageChangeListener(this);
        mViewPager.setAdapter(null);
        mBottomNavigationView.setOnNavigationItemSelectedListener(null);
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    public boolean isSupportSwipeBack() {
        // 不使用侧滑功能
        return !super.isSupportSwipeBack();
    }

}
