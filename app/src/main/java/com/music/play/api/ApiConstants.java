package com.music.play.api;

import com.music.play.entity.UserInfo;

/**
 * desc   :
 */
public class ApiConstants {
    public final static String BASE_URL = "http://192.168.14.13:8080";

    public static UserInfo sUserInfo;

    public static UserInfo getUserInfo() {
        return sUserInfo;
    }

    public static void setUserInfo(UserInfo userInfo) {
        sUserInfo = userInfo;
    }

    //注册
    public final static String REGISTER_URL = BASE_URL + "/user/register";

    //登录
    public final static String LOGIN_URL = BASE_URL + "/user/login";

    //修改密码
    public final static String EDIT_URL = BASE_URL + "/user/edit";


    //根据音乐类型获取音乐
    public final static String QUERY_MUSIC_URL = BASE_URL + "/user/queryMusicList";

    //获取音乐分类
    public final static String QUERY_MUSIC_TYPE_URL = BASE_URL + "/user/queryMusicTypeList";

    //删除音乐分类
    public final static String DEL_MUSIC_TYPE_URL = BASE_URL + "/user/delMusicType";

    //编辑音乐分类
    public final static String EDIT_MUSIC_TYPE = BASE_URL + "/user/editMusicType";

    //修改音乐信息
    public final static String EDIT_MUSIC_URL = BASE_URL + "/user/editMusic";

    //删除音乐
    public final static String DEL_MUSIC_URL = BASE_URL + "/user/delMusic";

    //添加音乐分类
    public final static String ADD_MUSIC_TYPE_URL = BASE_URL + "/user/addMusicType";

    //获取平台注册用户
    public final static String USER_LIST_URL = BASE_URL + "/user/queryUserList";

    //删除平台注册用户
    public final static String DEL_USER_URL = BASE_URL + "/user/delUser";


    //查找浏览记录
    public final static String RECORD_MUSIC_LIST_URL = BASE_URL + "/user/queryMusicRecordList";

    //添加浏览记录
    public final static String ADD_RECORD_MUSIC_URL = BASE_URL + "/user/addMusicRecord";



}