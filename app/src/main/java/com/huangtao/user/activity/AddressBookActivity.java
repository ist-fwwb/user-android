package com.huangtao.user.activity;

import android.widget.ListView;
import android.widget.TextView;

import com.hjq.bar.TitleBar;
import com.huangtao.user.R;
import com.huangtao.user.adapter.AddressBookAdapter;
import com.huangtao.user.common.MyActivity;
import com.huangtao.user.model.User;
import com.huangtao.user.network.Network;
import com.huangtao.user.widget.SideBar;

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
        mListContact.setAdapter(new AddressBookAdapter(this, title));
        mSideBar.setOnLetterSelectedListener(this);

        mSideBar.setDialog(mTextChar);

        title.getRightView().setClickable(false);
    }

    @Override
    protected void initData() {
        Network.getInstance().queryUser(null, null).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                ((AddressBookAdapter) mListContact.getAdapter()).addData(response.body());
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                t.printStackTrace();
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
}
