package com.music.play.fragment;

import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.lzy.okgo.OkGo;
import com.music.play.activity.PlayMusicActivity;
import com.music.play.adapter.HomeListAdapter;
import com.music.play.api.ApiConstants;
import com.music.play.base.BaseFragment;
import com.music.play.databinding.FragmentRecordBinding;
import com.music.play.entity.MusicInfo;
import com.music.play.entity.MusicListInfo;
import com.music.play.entity.UserInfo;
import com.music.play.http.HttpStringCallback;
import com.music.play.service.AudioPlayer;
import com.music.play.utils.GsonUtils;


/**
 * 浏览记录
 */
public class RecordFragment extends BaseFragment<FragmentRecordBinding> {
    private HomeListAdapter mHomeListAdapter;

    @Override
    protected FragmentRecordBinding getViewBinding() {
        return FragmentRecordBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setListener() {

        mHomeListAdapter = new HomeListAdapter();
        mBinding.recyclerView.setAdapter(mHomeListAdapter);


        //点击事件
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

    }

    @Override
    public void onResume() {
        super.onResume();
        queryRecordListData();
    }

    private void queryRecordListData() {
        OkGo.<String>get(ApiConstants.RECORD_MUSIC_LIST_URL)
                .params("username", ApiConstants.getUserInfo().getUsername())
                .execute(new HttpStringCallback(null) {
                    @Override
                    protected void onSuccess(String msg, String response) {
                        MusicListInfo musicListInfo = GsonUtils.parseJson(response, MusicListInfo.class);
                        if (null != mHomeListAdapter) {
                            mHomeListAdapter.setList(musicListInfo.getList());
                        }
                    }

                    @Override
                    protected void onError(String response) {

                    }
                });

    }


}