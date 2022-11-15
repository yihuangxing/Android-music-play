package com.music.play.entity;


import com.music.play.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataBean {
    public Integer imageRes;
    public String imageUrl;
    public String title;
    public int viewType;

    public DataBean(Integer imageRes, String title, int viewType) {
        this.imageRes = imageRes;
        this.title = title;
        this.viewType = viewType;
    }

    public DataBean(String imageUrl, String title, int viewType) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.viewType = viewType;
    }


    public static List<DataBean> getTestData2() {
        List<DataBean> list = new ArrayList<>();
        list.add(new DataBean(R.mipmap.image7, "听风.赏雨", 3));
        list.add(new DataBean(R.mipmap.image8, "迪丽热巴.迪力木拉提", 1));
        list.add(new DataBean(R.mipmap.image9, "爱美.人间有之", 3));
        list.add(new DataBean(R.mipmap.image10, "洋洋洋.气质篇", 1));
        list.add(new DataBean(R.mipmap.image11, "生活的态度", 3));
        return list;
    }
    public static List<DataBean> getTestData3() {
        List<DataBean> list = new ArrayList<>();
        list.add(new DataBean(R.mipmap.music_bg_2, "迪丽热巴.迪力木拉提", 1));
        list.add(new DataBean(R.mipmap.music_bg_3, "迪丽热巴.迪力木拉提", 1));
        list.add(new DataBean(R.mipmap.music_bg_1, "听风.赏雨", 3));
        list.add(new DataBean(R.mipmap.music_bg_4, "迪丽热巴.迪力木拉提", 1));
        return list;
    }









}
