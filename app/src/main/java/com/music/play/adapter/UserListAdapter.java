package com.music.play.adapter;

import android.graphics.Color;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.music.play.R;
import com.music.play.entity.UserInfo;
import com.othershe.cornerlabelview.CornerLabelView;

/**
 * desc   :
 */
public class UserListAdapter extends BaseQuickAdapter<UserInfo, BaseViewHolder> {
    private CornerLabelView cornerLabelView;

    public UserListAdapter() {
        super(R.layout.user_list_item);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, UserInfo userInfo) {
        baseViewHolder.setText(R.id.username, userInfo.getUsername());
        cornerLabelView = baseViewHolder.getView(R.id.cornerLabelView);
        if (userInfo.getRegister_type() == 1) {
            cornerLabelView.setText("管理员");
            cornerLabelView.setBgColor(Color.parseColor("#F95A29"));
        } else {
            cornerLabelView.setText("普通用户");
            cornerLabelView.setBgColor(Color.parseColor("#009688"));
        }
    }
}
