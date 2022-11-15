package com.music.play.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.music.play.R;
import com.music.play.entity.MusicTypeInfo;

/**
 * desc   :
 */
public class ClassifyManagerListAdapter extends BaseQuickAdapter<MusicTypeInfo, BaseViewHolder> {
    public ClassifyManagerListAdapter() {
        super(R.layout.classify_list_item);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, MusicTypeInfo musicTypeInfo) {
        baseViewHolder.setText(R.id.type_name, musicTypeInfo.getType_name());

    }
}
