package com.music.play.activity;

import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.music.play.api.ApiConstants;
import com.music.play.base.BaseActivity;
import com.music.play.databinding.ActivityMainBinding;
import com.music.play.fragment.HomeFragment;
import com.music.play.fragment.TypeFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity<ActivityMainBinding> {
    private String[] titles = {"音乐推荐", "音乐分类"};
    private List<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected ActivityMainBinding getViewBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setListener() {

        mBinding.avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, MineActivity.class), 20002);
            }
        });

    }


    @Override
    protected void initData() {
        if (ApiConstants.getUserInfo() != null) {
            mBinding.username.setText(ApiConstants.getUserInfo().getUsername());
        }
        //造数据
        fragmentList.add(new HomeFragment());
        fragmentList.add(new TypeFragment());
//        fragmentList.add(new RecordFragment());

        //如果处理成懒加载的话，其实很简单，只要是这个方法setOffscreenPageLimit不去设置，就可以了。
//        mBinding.viewPager.setOffscreenPageLimit(fragmentList.size());
        mBinding.viewPager.setUserInputEnabled(false);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 20002) {
            finish();
        }
    }
}