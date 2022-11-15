package com.music.play.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.music.play.R;
import com.music.play.entity.MusicInfo;

/**
 * desc   :
 */
public class HomeListAdapter extends BaseQuickAdapter<MusicInfo, BaseViewHolder> {
    public HomeListAdapter() {
        super(R.layout.home_item_list);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, MusicInfo musicInfo) {
        baseViewHolder.setText(R.id.music_title, musicInfo.getMusic_title());
        baseViewHolder.setText(R.id.music_type, musicInfo.getMusic_type());
    }
}