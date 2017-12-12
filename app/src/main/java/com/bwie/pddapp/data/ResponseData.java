package com.bwie.pddapp.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liqy on 2017/12/11.
 */

public class ResponseData<T> {
    public String ver;
    public T goods_list;
    public List<String> avatars;

    @Override
    public String toString() {
        return "ResponseData{" +
                "ver='" + ver + '\'' +
                ", goods_list=" + goods_list +
                ", avatars=" + avatars +
                '}';
    }

    public String getMsg(){
        return ver;
    }

}
