package com.bwie.pddapp.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liqy on 2017/12/12.
 */

public class NewsList {

    public static final int HEAD = 0;
    public static final int LABEL = 1;
    public static final int ITEM = 2;

    public int type;
    public ArrayList<Goods> goodsList;
    public List<String> avatars;
    public Goods goods;

    @Override
    public String toString() {
        return "NewsList{" +
                "type=" + type +
                ", goodsList=" + goodsList +
                ", avatars=" + avatars +
                ", goods=" + goods +
                '}';
    }

    public NewsList(int type) {
        this.type = type;
    }

    public NewsList(int type, Goods goods) {
        this.type = type;
        this.goods = goods;
    }

    public NewsList(int type, List<String> avatars) {
        this.type = type;
        this.avatars = avatars;
    }

    public NewsList(int type, ArrayList<Goods> goodsList) {
        this.type = type;
        this.goodsList = goodsList;
    }
}
