package com.bwie.pddapp.data;

import java.util.ArrayList;

/**
 * Created by liqy on 2017/12/11.
 */

public class ResponseData<T> {
    public String ver;
    public T goods_list;

    public String getMsg(){
        return ver;
    }

}
