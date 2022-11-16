package com.music.play.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * desc   :
 */
public class GsonUtils {

    private static Gson gson;

    public static Gson buildGson() {
        if (gson == null) {
            gson = new GsonBuilder()
                    .disableHtmlEscaping()
                    .create();
        }
        return gson;
    }

    public static <T> T parseJson(String json, Class<T> type) {
        T result = null;
        if (!TextUtils.isEmpty(json)) {
            Gson gson = buildGson();
            try {
                result = gson.fromJson(json, type);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                gson = null;
            }
        }
        return result;
    }

    /**
     * 根据对象返回json  过滤空值字段
     */
    public static String toJson(Object object) {
        return buildGson().toJson(object);
    }
}
