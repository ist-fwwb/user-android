package com.huangtao.user.ui.adapter;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.huangtao.base.BaseFragmentAdapter;
import com.huangtao.user.common.MyLazyFragment;
import com.huangtao.user.ui.fragment.TestFragmentA;
import com.huangtao.user.ui.fragment.TestFragmentB;
import com.huangtao.user.ui.fragment.TestFragmentC;
import com.huangtao.user.ui.fragment.TestFragmentD;

import java.util.List;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject
 *    time   : 2018/10/18
 *    desc   : 主页界面 ViewPager + Fragment 适配器
 */
public final class HomeFragmentAdapter extends BaseFragmentAdapter<MyLazyFragment> {

    public HomeFragmentAdapter(FragmentActivity activity) {
        super(activity);
    }

    @Override
    protected void init(FragmentManager manager, List<MyLazyFragment> list) {
        list.add(TestFragmentA.newInstance());
        list.add(TestFragmentB.newInstance());
        list.add(TestFragmentC.newInstance());
        list.add(TestFragmentD.newInstance());
    }
}