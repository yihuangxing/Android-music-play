package com.music.play.fragment;

import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.lzy.okgo.OkGo;
import com.music.play.activity.MusicTypeListActivity;
import com.music.play.activity.PlayMusicActivity;
import com.music.play.adapter.HomeListAdapter;
import com.music.play.api.ApiConstants;
import com.music.play.base.BaseFragment;
import com.music.play.databinding.FragmentHomeBinding;
import com.music.play.entity.DataBean;
import com.music.play.entity.MusicInfo;
import com.music.play.entity.MusicListInfo;
import com.music.play.http.HttpStringCallback;
import com.music.play.service.AudioPlayer;
import com.music.play.utils.GsonUtils;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;


/**
 * 首页
 */
public class HomeFragment extends BaseFragment<FragmentHomeBinding> {
    private HomeListAdapter mHomeListAdapter;

    @Override
    protected FragmentHomeBinding getViewBinding() {
        return FragmentHomeBinding.inflate(getLayoutInflater());
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

        //banner数据
        mBinding.banner.setAdapter(new BannerImageAdapter<DataBean>(DataBean.getTestData2()) {
                    @Override
                    public void onBindView(BannerImageHolder holder, DataBean data, int position, int size) {
                        holder.imageView.setImageResource(data.imageRes);
                    }
                })
                .addBannerLifecycleObserver(this)//添加生命周期观察者
                .setIndicator(new CircleIndicator(getActivity()));


        getMusicListData();

    }


    /**
     * 根据音乐类型获取音乐
     */
    private void getMusicListData() {
        OkGo.<String>get(ApiConstants.QUERY_MUSIC_URL)
                .params("music_type", "热歌精选")
                .execute(new HttpStringCallback(getActivity()) {
                    @Override
                    protected void onSuccess(String msg, String response) {
                        MusicListInfo musicListInfo = GsonUtils.parseJson(response, MusicListInfo.class);
                        mHomeListAdapter.setList(musicListInfo.getList());
                    }

                    @Override
                    protected void onError(String response) {

                    }
                });
    }


}