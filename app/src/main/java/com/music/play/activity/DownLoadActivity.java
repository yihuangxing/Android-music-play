package com.music.play.activity;


import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.arialyy.aria.core.Aria;
import com.arialyy.aria.core.download.DownloadEntity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.music.play.adapter.HomeListAdapter;
import com.music.play.base.BaseActivity;
import com.music.play.databinding.ActivityDownLoadBinding;
import com.music.play.entity.MusicInfo;
import com.music.play.service.AudioPlayer;
import com.music.play.utils.GsonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 音乐下载
 */
public class DownLoadActivity extends BaseActivity<ActivityDownLoadBinding> {
    private static final String TAG = "=======================";

    private HomeListAdapter mHomeListAdapter;
    private List<MusicInfo> mMusicInfoList = new ArrayList<MusicInfo>();

    @Override
    protected ActivityDownLoadBinding getViewBinding() {
        return ActivityDownLoadBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setListener() {
        mHomeListAdapter = new HomeListAdapter();
        mBinding.recyclerView.setAdapter(mHomeListAdapter);

        //列表点击事件
        mHomeListAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                //设置数据
                AudioPlayer.get().setMusicList(mHomeListAdapter.getData());
                MusicInfo musicInfo = mHomeListAdapter.getData().get(position);
                Intent intent = new Intent(mContext, PlayMusicActivity.class);
                intent.putExtra("musicInfo", musicInfo);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void initData() {
        getDownloadTask();
    }


    private void getDownloadTask() {
        List<DownloadEntity> list = Aria.download(this).getAllCompleteTask();
        if (list != null && list.size() > 0) {
            mMusicInfoList.clear();
            for (int i = 0; i < list.size(); i++) {
                String str = list.get(i).getStr();
                MusicInfo musicInfo = GsonUtils.parseJson(str, MusicInfo.class);
                mMusicInfoList.add(musicInfo);
            }
            mHomeListAdapter.setList(mMusicInfoList);
        }


    }
}