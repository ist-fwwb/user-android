package com.huangtao.user.fragment;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.hjq.bar.TitleBar;
import com.huangtao.user.R;
import com.huangtao.user.common.Constants;
import com.huangtao.user.common.MyLazyFragment;
import com.huangtao.user.widget.XCollapsingToolbarLayout;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainFragmentDLogin extends MyLazyFragment
        implements View.OnClickListener, XCollapsingToolbarLayout.OnScrimsListener {

    @BindView(R.id.ctl_bar)
    XCollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.tool_bar)
    Toolbar mToolbar;
    @BindView(R.id.title_bar)
    TitleBar mTitleBar;
    @BindView(R.id.head)
    CircleImageView head;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.company)
    TextView company;
    @BindView(R.id.phone)
    TextView phone;


    public static MainFragmentDLogin newInstance() {
        return new MainFragmentDLogin();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main_person_login;
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

        if(Constants.user != null){
            name.setText(Constants.user.getName());
            phone.setText(Constants.user.getPhone());

//            CommonUtils.getFile(Constants.user.getFaceFile().getBytes(), Constants.HEAD_PATH, Constants.user.getId() + ".jpg");
//            head.setImageBitmap(CommonUtils.getLoacalBitmap(Constants.HEAD_PATH + File.separator + Constants.user.getId() + ".jpg"));

        }
    }

    @Override
    protected void initData() {

    }

    /**
     * {@link View.OnClickListener}
     */
    @Override
    public void onClick(View v) {

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