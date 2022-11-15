package com.music.play.fragment;

import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.lzy.okgo.OkGo;
import com.music.play.activity.MusicTypeListActivity;
import com.music.play.adapter.TypeListAdapter;
import com.music.play.api.ApiConstants;
import com.music.play.base.BaseFragment;
import com.music.play.databinding.FragmentTypeBinding;
import com.music.play.entity.DataBean;
import com.music.play.entity.MusicInfo;
import com.music.play.entity.MusicListInfo;
import com.music.play.entity.MusicTypeInfo;
import com.music.play.entity.MusicTypeListInfo;
import com.music.play.http.HttpStringCallback;
import com.music.play.utils.GsonUtils;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;

/**
 * 分类
 */
public class TypeFragment extends BaseFragment<FragmentTypeBinding> {
    private TypeListAdapter mListAdapter;

    @Override
    protected FragmentTypeBinding getViewBinding() {
        return FragmentTypeBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setListener() {
        mListAdapter = new TypeListAdapter();
        //列表点击事件
        mListAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                MusicTypeInfo musicTypeInfo = mListAdapter.getData().get(position);
                Intent intent = new Intent(getActivity(), MusicTypeListActivity.class);
                intent.putExtra("music_type", musicTypeInfo.getType_name());
                startActivity(intent);
            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        mBinding.recyclerView.setLayoutManager(gridLayoutManager);
        mBinding.recyclerView.setAdapter(mListAdapter);
    }

    @Override
    protected void initData() {


        queryMusicTypeListData();
    }

    /**
     * 获取音乐分类
     */
    private void queryMusicTypeListData() {
        OkGo.<String>get(ApiConstants.QUERY_MUSIC_TYPE_URL)
                .params("music_type", "热门音乐")
                .execute(new HttpStringCallback(getActivity()) {
                    @Override
                    protected void onSuccess(String msg, String response) {
                        MusicTypeListInfo musicTypeListInfo = GsonUtils.parseJson(response, MusicTypeListInfo.class);
                        mListAdapter.setList(musicTypeListInfo.getList());

                    }

                    @Override
                    protected void onError(String response) {

                    }
                });
    }
}