package com.bwie.pddapp.data;

import com.google.gson.Gson;

/**
 * Created by liqy on 2017/12/11.
 */

public class GroupAvatar {

    /**
     * group_order_id : 314285128840331005
     * avatar : http://avatar.yangkeduo.com/a/fcf8c117d6d7050b4efeb5c0f8f66eae50c68914-1512012092?x-oss-process=image/resize,w_100
     */

    public String group_order_id;
    public String avatar;

    @Override
    public String toString() {
        return "GroupAvatar{" +
                "group_order_id='" + group_order_id + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
