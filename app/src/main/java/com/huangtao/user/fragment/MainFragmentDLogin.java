package com.huangtao.user.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.hjq.bar.TitleBar;
import com.huangtao.user.R;
import com.huangtao.user.common.Constants;
import com.huangtao.user.common.MyLazyFragment;
import com.huangtao.user.helper.CommonUtils;
import com.huangtao.user.model.meta.Type;
import com.huangtao.user.network.FileManagement;
import com.huangtao.user.widget.XCollapsingToolbarLayout;

import java.io.File;

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
    @BindView(R.id.unactive)
    TextView unactive;
    @BindView(R.id.company)
    TextView company;
    @BindView(R.id.phone)
    TextView phone;

    @BindView(R.id.logout)
    RelativeLayout logout;

    private HeadHandler headHandler = new HeadHandler();


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
            if(Constants.user.getType().equals(Type.UNACTIVATE)){
                unactive.setVisibility(View.VISIBLE);
            }

            final String fileName = Constants.user.getFaceFile();
            File head = new File(Constants.HEAD_DIR + fileName);
            if(!head.exists()){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        boolean result = FileManagement.download(getFragmentActivity().getApplicationContext(),
                                fileName, Constants.HEAD_DIR);
                        if(result){
                            headHandler.sendEmptyMessage(0);
                        }
                    }
                }).start();
            } else {
                refreshHead(head);
            }
        }

        logout.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    /**
     * {@link View.OnClickListener}
     */
    @Override
    public void onClick(View v) {
        if (v == logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getFragmentActivity());
            builder.setMessage("确定要退出登录吗？");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    CommonUtils.logout(getFragmentActivity());
                    Intent intent = new Intent("login");
                    intent.putExtra("isLogin", false);
                    getFragmentActivity().sendBroadcast(intent);
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
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

    private void refreshHead(File file){
        head.setImageBitmap(CommonUtils.getLoacalBitmap(file.getAbsolutePath()));
    }

    @SuppressLint("HandlerLeak")
    private class HeadHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String fileName = Constants.user.getFaceFile();
            File head = new File(Constants.HEAD_DIR + fileName);
            if (head.exists())
                refreshHead(head);
        }
    }

}