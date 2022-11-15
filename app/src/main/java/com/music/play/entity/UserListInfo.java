package com.music.play.entity;

import java.util.List;

/**
 * desc   :
 */
public class UserListInfo {
    private List<UserInfo> list;

    public UserListInfo(List<UserInfo> list) {
        this.list = list;
    }

    public List<UserInfo> getList() {
        return list;
    }

    public void setList(List<UserInfo> list) {
        this.list = list;
    }
}
