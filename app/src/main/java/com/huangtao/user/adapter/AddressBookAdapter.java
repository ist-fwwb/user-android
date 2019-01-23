package com.huangtao.user.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hjq.bar.TitleBar;
import com.huangtao.user.R;
import com.huangtao.user.helper.CommonUtils;
import com.huangtao.user.model.User;
import com.huangtao.user.network.FileManagement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AddressBookAdapter extends BaseAdapter {

    private static final String TAG = AddressBookAdapter.class.getSimpleName();

    private Context context;

    private List<User> mContactList;

    private List<Integer> selectedPosition;

    private TitleBar titleBar;

    private Set<String> attendents;

    public AddressBookAdapter(Context context, TitleBar titleBar, Set<String> attendents) {
        this.context = context;
        mContactList = new ArrayList<>();
        selectedPosition = new ArrayList<>();
        this.titleBar = titleBar;
        this.attendents = attendents;
    }

    public void addData(List<User> list) {
        mContactList.addAll(list);
        Collections.sort(mContactList);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mContactList.size();
    }

    @Override
    public Object getItem(int position) {
        return mContactList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        User contact = mContactList.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address_book, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // 获取当前联系人的首字母
        char headerChar = getHeaderCharByPosition(position);
        // 获取当前联系人首字母第一次出现的位置
        int index = getFirstPositionByHeaderChar(headerChar);

        // 如果当前联系人首字母第一次出现的位置等于当前的位置，则表示该联系人是该首字母下出现的第一个联系人
        if (position == index) {
            holder.mCharCategoryText.setVisibility(View.VISIBLE);
            holder.mCharCategoryText.setText(contact.getHeaderChar());
            holder.mDivider.setVisibility(View.GONE);
        } else {
            // 默认设置字母栏不显示
            holder.mCharCategoryText.setVisibility(View.GONE);
            holder.mDivider.setVisibility(View.VISIBLE);
        }
        holder.mNameText.setText(contact.getName());

        if(selectedPosition.contains(position)){
            holder.checkBox.setImageResource(R.mipmap.ic_addressbook_check);
        } else if (!attendents.contains(contact.getId())){
            holder.checkBox.setImageResource(R.mipmap.ic_addressbook_uncheck);
        } else {
            holder.checkBox.setImageResource(R.mipmap.ic_addressbook_uncheckable);
            convertView.setEnabled(false);
        }

        Observable.just(FileManagement.getUserHead(context, contact))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        if(!s.isEmpty()){
                            holder.head.setImageBitmap(CommonUtils.getLoacalBitmap(s));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedPosition.contains(position)){
                    selectedPosition.remove((Integer) position);
                    holder.checkBox.setImageResource(R.mipmap.ic_addressbook_uncheck);
                } else {
                    selectedPosition.add(position);
                    holder.checkBox.setImageResource(R.mipmap.ic_addressbook_check);
                }

                if(selectedPosition.size() == 0) {
                    titleBar.setRightTitle("提交");
                    titleBar.getRightView().setEnabled(false);
                } else {
                    titleBar.setRightTitle("提交(" + selectedPosition.size() + ")");
                    titleBar.getRightView().setEnabled(true);
                }

            }
        });

        return convertView;
    }

    /**
     * 通过联系人的位置获取该联系人的名称的首字母
     *
     * @param position
     * @return 首字母
     */
    private char getHeaderCharByPosition(int position) {
        return mContactList.get(position).getHeaderChar().toUpperCase().charAt(0);
    }

    /**
     * 通过首字母获取显示该首字母的第一个联系人的位置：比如C，陈奕迅
     *
     * @param c
     * @return 位置，如果返回-1表示未查找到该字母
     */
    public int getFirstPositionByHeaderChar(char c) {
        for (int i = 0; i < getCount(); i++) {
            String headerChar = mContactList.get(i).getHeaderChar();
            char firstChar = headerChar.toUpperCase(Locale.CHINA).charAt(0);
            if (firstChar == c) {
                return i;
            }
        }
        return -1;
    }

    public List<User> getSelectedUsers() {
        List<User> users = new ArrayList<>();
        for (int pos : selectedPosition) {
            users.add(mContactList.get(pos));
        }
        return users;
    }


    private class ViewHolder {
        TextView mCharCategoryText;
        TextView mNameText;
        View mDivider;
        ImageView head;
        ImageView checkBox;

        ViewHolder(View view) {
            mCharCategoryText = (TextView) view.findViewById(R.id.tv_header_char);
            mNameText = (TextView) view.findViewById(R.id.tv_name);
            mDivider = view.findViewById(R.id.divider);
            head = view.findViewById(R.id.head);
            checkBox = view.findViewById(R.id.check);
        }
    }

}
