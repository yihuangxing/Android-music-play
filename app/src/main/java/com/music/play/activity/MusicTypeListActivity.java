package com.music.play.activity;

import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.lzy.okgo.OkGo;
import com.music.play.adapter.HomeListAdapter;
import com.music.play.adapter.MusicTypeListAdapter;
import com.music.play.api.ApiConstants;
import com.music.play.base.BaseActivity;
import com.music.play.databinding.ActivityMusicTypeListBinding;
import com.music.play.entity.MusicInfo;
import com.music.play.entity.MusicListInfo;
import com.music.play.http.HttpStringCallback;
import com.music.play.utils.GsonUtils;

/**
 * 分类下的歌曲列表
 */
public class MusicTypeListActivity extends BaseActivity<ActivityMusicTypeListBinding> {
    private MusicTypeListAdapter mListAdapter;
    private String music_type;

    @Override
    protected ActivityMusicTypeListBinding getViewBinding() {
        return ActivityMusicTypeListBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setListener() {

        mListAdapter = new MusicTypeListAdapter();
        mBinding.recyclerView.setAdapter(mListAdapter);


        //列表点击事件
        mListAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                MusicInfo musicInfo = mListAdapter.getData().get(position);
                Intent intent = new Intent(mContext, PlayMusicActivity.class);
                intent.putExtra("musicInfo", musicInfo);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void initData() {
        music_type = getIntent().getStringExtra("music_type");
        mBinding.toolbar.setTitle(music_type);
        getMusicListData();
    }


    /**
     * 根据音乐类型获取音乐
     */
    private void getMusicListData() {
        OkGo.<String>get(ApiConstants.QUERY_MUSIC_URL)
                .params("music_type", music_type)
                .execute(new HttpStringCallback(this) {
                    @Override
                    protected void onSuccess(String msg, String response) {
                        MusicListInfo musicListInfo = GsonUtils.parseJson(response, MusicListInfo.class);
                        mListAdapter.setList(musicListInfo.getList());
                    }

                    @Override
                    protected void onError(String response) {

                    }
                });
    }
}