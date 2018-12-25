package com.huangtao.user.adapter;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.huangtao.base.BaseFragmentAdapter;
import com.huangtao.user.common.MyLazyFragment;
import com.huangtao.user.fragment.MainFragmentA;
import com.huangtao.user.fragment.MainFragmentB;
import com.huangtao.user.fragment.MainFragmentC;
import com.huangtao.user.fragment.MainFragmentD;

import java.util.List;

public final class MainFragmentAdapter extends BaseFragmentAdapter<MyLazyFragment> {

    public MainFragmentAdapter(FragmentActivity activity) {
        super(activity);
    }

    @Override
    protected void init(FragmentManager manager, List<MyLazyFragment> list) {
        list.add(MainFragmentA.newInstance());
        list.add(MainFragmentB.newInstance());
        list.add(MainFragmentC.newInstance());
        list.add(MainFragmentD.newInstance());
    }
}