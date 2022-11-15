package com.music.play.activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.music.play.base.BaseActivity;
import com.music.play.databinding.ActivityMainBinding;
import com.music.play.fragment.HomeFragment;
import com.music.play.fragment.TypeFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity<ActivityMainBinding> {
    private String[] titles = {"推荐", "分类"};
    private List<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected ActivityMainBinding getViewBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void initData() {
        //造数据
        fragmentList.add(new HomeFragment());
        fragmentList.add(new TypeFragment());

        //如果处理成懒加载的话，其实很简单，只要是这个方法setOffscreenPageLimit不去设置，就可以了。
//        mBinding.viewPager.setOffscreenPageLimit(fragmentList.size());
        mBinding.viewPager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getItemCount() {
                return fragmentList.size();
            }
        });
        mBinding.tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mBinding.viewPager.setCurrentItem(tab.getPosition(), false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(mBinding.tabs, mBinding.viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(titles[position]);
            }
        });
        //这句话很重要
        tabLayoutMediator.attach();

    }
}