package com.huangtao.user.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hjq.toast.ToastUtils;
import com.huangtao.user.R;
import com.huangtao.user.common.Constants;
import com.huangtao.user.common.MyActivity;
import com.huangtao.user.helper.EditTextInputHelper;
import com.huangtao.user.model.User;
import com.huangtao.user.network.Network;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends MyActivity
        implements View.OnClickListener {

    @BindView(R.id.et_login_phone)
    EditText mPhoneView;
    @BindView(R.id.et_login_password)
    EditText mPasswordView;
    @BindView(R.id.btn_login_commit)
    Button mCommitView;

    private EditTextInputHelper mEditTextInputHelper;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected int getTitleBarId() {
        return R.id.tb_login_title;
    }

    @Override
    protected void initView() {
        mCommitView.setOnClickListener(this);
        mEditTextInputHelper = new EditTextInputHelper(mCommitView);
        mEditTextInputHelper.addViews(mPhoneView, mPasswordView);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onRightClick(View v) {
        // 跳转到注册界面
        startActivity(RegisterActivity.class);
    }

    @Override
    protected void onDestroy() {
        mEditTextInputHelper.removeViews();
        super.onDestroy();
    }

    /**
     * {@link View.OnClickListener}
     */
    @Override
    public void onClick(View v) {
        if (v == mCommitView) {
            if (mPhoneView.getText().toString().length() != 11) {
                ToastUtils.show(getResources().getString(R.string.phone_input_error));
            }

            showProgressBar();

            User user = new User();
            user.setPhone(mPhoneView.getText().toString());
            user.setPassword(mPasswordView.getText().toString());

            Network.getInstance().login(user).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    hideProgressBar();
                    User result = response.body();
                    if(result != null){
                        Constants.user = result;
                        toast("登录成功");
                        // TODO 发送广播，通知首页修改ui
                        finish();
                    } else {
                        toast("登录失败");
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    hideProgressBar();
                    toast("登录失败");
                }
            });

        }
    }
}