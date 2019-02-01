package com.huangtao.user.activity;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.huangtao.user.R;
import com.huangtao.user.common.Constants;
import com.huangtao.user.common.MyActivity;
import com.huangtao.user.model.User;
import com.huangtao.user.network.FileManagement;
import com.huangtao.user.network.Network;
import com.huangtao.widget.CountdownView;

import java.io.File;

import butterknife.BindView;
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

    private static final String PATH_REGISTER = Constants.SAVE_IMG_DIR + "register.jpg";

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

            String enterpriseId = enterprise.getText().toString();
            String phone = mPhoneView.getText().toString();
            String fileName = enterpriseId + phone;

            File face = new File(PATH_REGISTER);

            boolean faceResult = FileManagement.upload(getApplicationContext(), face, fileName + ".jpg");
            Log.i("upload", "face upload complete " + faceResult);

            if(!faceResult){
                toast("注册失败");
                hideProgressBar();
                return;
            }

            Network.getInstance().register(enterpriseId, phone, mPasswordView1.getText().toString
                    (), realName.getText().toString(), fileName + ".jpg", fileName).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    hideProgressBar();
                    User user = response.body();
                    if (user != null) {
                        toast("注册成功");
                        finish();
                    } else {
                        toast("注册失败");
                        Log.e("register", response.toString());
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
            Intent intent = new Intent();
            // 指定开启系统相机的Action
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            // 根据文件地址创建文件
            File file = new File(PATH_REGISTER);
            if (file.exists()) {
                file.delete();
            }
            // 把文件地址转换成Uri格式
            Uri uri = FileProvider.getUriForFile(RegisterActivity.this, "com.huangtao.user.fileprovider", file);
            // 设置系统相机拍摄照片完成后图片文件的存放地址
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 0) {
            face.setEnabled(false);
            mCommitView.setEnabled(true);
        }
    }

    @Override
    protected void onDestroy() {
//        mEditTextInputHelper.removeViews();
        super.onDestroy();
    }
}