package com.huangtao.user.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.hjq.bar.TitleBar;
import com.huangtao.user.R;
import com.huangtao.user.adapter.AddressBookAdapter;
import com.huangtao.user.common.MyActivity;
import com.huangtao.user.model.Meeting;
import com.huangtao.user.model.User;
import com.huangtao.user.network.Network;
import com.huangtao.user.widget.SideBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressBookActivity extends MyActivity implements SideBar.OnLetterSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.title)
    TitleBar title;

    @BindView(R.id.lv_contacts_list)
    ListView mListContact;
    @BindView(R.id.side_bar)
    SideBar mSideBar;
    @BindView(R.id.tv_dialog)
    TextView mTextChar;

    Meeting meeting;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_address_book;
    }

    @Override
    protected int getTitleBarId() {
        return R.id.title;
    }

    @Override
    protected void initView() {
        showProgressBar();

        boolean isAddressBook = getIntent().getBooleanExtra("isAddressBook", false);
        if (isAddressBook) {
            mListContact.setAdapter(new AddressBookAdapter(this, title, null, true));
            title.setTitle("企业通讯录");
            title.getRightView().setVisibility(View.GONE);
        } else {
            meeting = (Meeting) getIntent().getSerializableExtra("meeting");
            mListContact.setAdapter(new AddressBookAdapter(this, title, meeting.getAttendants().keySet(), false));
            title.getRightView().setEnabled(false);
        }

        mSideBar.setOnLetterSelectedListener(this);
        mSideBar.setDialog(mTextChar);
    }

    @Override
    protected void initData() {
        Network.getInstance().queryUser(null, null).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                ((AddressBookAdapter) mListContact.getAdapter()).addData(response.body());
                hideProgressBar();
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                t.printStackTrace();
                hideProgressBar();
                toast("获取联系人失败");
            }
        });
    }

    @Override
    public void onLetterSelected(String letter) {
        mTextChar.setText(letter);
        AddressBookAdapter adapter = (AddressBookAdapter) mListContact.getAdapter();
        int firstPosition = -1;
        char c = letter.charAt(0);
        do {
            firstPosition = adapter.getFirstPositionByHeaderChar(c);
            if (firstPosition == -1) {
                c++;
            }
        } while (firstPosition == -1);
        mListContact.setSelection(firstPosition);
        // Log.e(TAG, String.valueOf(c) + " pos=" + firstPosition);
    }

    /**
     * 提交点击事件
     * @param v
     */
    @Override
    public void onRightClick(View v) {
        super.onRightClick(v);

        final List<User> users = ((AddressBookAdapter) mListContact.getAdapter()).getSelectedUsers();
        if (users.size() > 0) {
            showProgressBar();

            List<String> uids = new ArrayList<>();
            for(User user : users) {
                uids.add(user.getId());
            }

            Network.getInstance().addAttendants(meeting.getId(), uids).enqueue(new Callback<Meeting>() {
                @Override
                public void onResponse(Call<Meeting> call, Response<Meeting> response) {
                    if(response.body() != null){
                        Intent intent = new Intent();
                        intent.putExtra("meeting", response.body());
                        setResult(RESULT_OK, intent);
                        toast("添加成功");
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<Meeting> call, Throwable t) {

                }
            });
        }
    }
}
