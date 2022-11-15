package com.music.play.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.music.play.R;
import com.music.play.entity.MusicTypeInfo;

/**
 * desc   :
 */
public class TypeListAdapter extends BaseQuickAdapter<MusicTypeInfo, BaseViewHolder> {
    public TypeListAdapter() {
        super(R.layout.type_item_list);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, MusicTypeInfo musicTypeInfo) {
         baseViewHolder.setText(R.id.title,musicTypeInfo.getType_name());
    }
}