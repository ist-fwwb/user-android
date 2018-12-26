package com.huangtao.user.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.huangtao.user.R;
import com.huangtao.user.arcsoft.RegisterAndRecognizeActivity;
import com.huangtao.user.common.MyActivity;
import com.huangtao.user.helper.EditTextInputHelper;
import com.huangtao.widget.CountdownView;

import butterknife.BindView;

public class RegisterActivity extends MyActivity
        implements View.OnClickListener {

    @BindView(R.id.et_register_phone)
    EditText mPhoneView;
    @BindView(R.id.cv_register_countdown)
    CountdownView mCountdownView;

    @BindView(R.id.et_register_code)
    EditText mCodeView;

    @BindView(R.id.et_register_password1)
    EditText mPasswordView1;
    @BindView(R.id.real_name)
    EditText realName;

    @BindView(R.id.face)
    Button face;
    @BindView(R.id.btn_register_commit)
    Button mCommitView;

    private EditTextInputHelper mEditTextInputHelper;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected int getTitleBarId() {
        return R.id.tb_register_title;
    }

    @Override
    protected void initView() {
        mCountdownView.setOnClickListener(this);
        face.setOnClickListener(this);
        mCommitView.setOnClickListener(this);

        mEditTextInputHelper = new EditTextInputHelper(mCommitView);
        mEditTextInputHelper.addViews(mPhoneView, mCodeView, mPasswordView1, realName);
    }

    @Override
    protected void initData() {
//        getHandler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                finishResult(RESULT_OK);
//            }
//        }, 2000);
    }

    /**
     * {@link View.OnClickListener}
     */
    @Override
    public void onClick(View v) {
        if (v == mCountdownView) { //获取验证码
            if (mPhoneView.getText().toString().length() != 11) {
                // 重置验证码倒计时控件
                mCountdownView.resetState();
                toast(getResources().getString(R.string.phone_input_error));
                return;
            }
            toast(getResources().getString(R.string.countdown_code_send_succeed));
        } else if (v == mCommitView) { //提交注册
            if (mPhoneView.getText().toString().length() != 11) {
                toast(getResources().getString(R.string.phone_input_error));
            }
        } else if (v == face){
            startActivity(RegisterAndRecognizeActivity.class);
        }
    }

    @Override
    protected void onDestroy() {
        mEditTextInputHelper.removeViews();
        super.onDestroy();
    }
}