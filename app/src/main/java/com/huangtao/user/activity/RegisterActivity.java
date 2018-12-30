package com.huangtao.user.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.huangtao.user.R;
import com.huangtao.user.arcsoft.FaceServer;
import com.huangtao.user.arcsoft.RegisterAndRecognizeActivity;
import com.huangtao.user.common.MyActivity;
import com.huangtao.user.model.User;
import com.huangtao.user.network.Network;
import com.huangtao.widget.CountdownView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    @BindView(R.id.enterprise)
    EditText enterprise;

    @BindView(R.id.face)
    Button face;
    @BindView(R.id.btn_register_commit)
    Button mCommitView;

//    private EditTextInputHelper mEditTextInputHelper;

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

//        mEditTextInputHelper = new EditTextInputHelper(mCommitView);
//        mEditTextInputHelper.addViews(mPhoneView, mCodeView, mPasswordView1, realName);
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
                return;
            }

            showProgressBar();
            // TODO 这段写的及其丑
            File face = new File(FaceServer.SAVE_IMG_DIR + File.separator + "register.jpg");
            File feature = new File(FaceServer.SAVE_FEATURE_DIR + File.separator + "register");

            List<MultipartBody.Part> parts = new ArrayList<>();

            RequestBody requestBody1 = RequestBody.create(MediaType.parse("image/png"), face);
            MultipartBody.Part part1 = MultipartBody.Part.createFormData("faceFile", face.getName(),
                    requestBody1);
            parts.add(part1);

            RequestBody requestBody2 = RequestBody.create(MediaType.parse("image/png"), feature);
            MultipartBody.Part part2 = MultipartBody.Part.createFormData("fetureFile", feature
                            .getName(),
                    requestBody2);
            parts.add(part2);

            Network.getInstance().register(enterprise.getText().toString(), mPhoneView.getText()
                    .toString(), mPasswordView1.getText().toString(), realName.getText().toString
                    (), parts).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    User user = response.body();
                    if (user != null) {
                        hideProgressBar();
                        toast("注册成功");
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    hideProgressBar();
                    toast("注册失败");
                    t.printStackTrace();
                }
            });



        } else if (v == face) {
            startActivityForResult(new Intent(this, RegisterAndRecognizeActivity.class), new
                    ActivityCallback() {
                @Override
                public void onActivityResult(int resultCode, @Nullable Intent data) {
                    if (resultCode == RESULT_OK) {
                        face.setEnabled(false);
                        mCommitView.setEnabled(true);
                    }
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
//        mEditTextInputHelper.removeViews();
        super.onDestroy();
    }
}