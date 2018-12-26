package com.huangtao.user.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.huangtao.user.R;
import com.huangtao.user.common.MyActivity;
import com.huangtao.user.helper.EditTextInputHelper;
import com.hjq.toast.ToastUtils;

import butterknife.BindView;

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

//        startActivityForResult(new Intent(this, RegisterActivity.class), new ActivityCallback() {
//
//            @Override
//            public void onActivityResult(int resultCode, @Nullable Intent data) {
//                toast(String.valueOf(resultCode));
//            }
//        });
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
        }
    }
}