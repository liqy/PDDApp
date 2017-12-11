package com.bwie.pddapp.data;

import com.google.gson.Gson;

/**
 * Created by liqy on 2017/12/11.
 */

public class Goods {

    public int normal_price;
    public int cnt;
    public String thumb_url;
    public int event_type;
    public String country;
    public String short_name;
    public String allowed_region;
    public int time;
    public String image_url;
    public int market_price;
    public String goods_name;
    public String hd_thumb_url;
    public int quantity;
    public boolean is_onsale;
    public int goods_id;
    public int is_app;
    public int mall_id;
    public int region_limit;

    @Override
    public String toString() {
        return "Goods{" +
                "normal_price=" + normal_price +
                ", cnt=" + cnt +
                ", thumb_url='" + thumb_url + '\'' +
                ", event_type=" + event_type +
                ", country='" + country + '\'' +
                ", short_name='" + short_name + '\'' +
                ", allowed_region='" + allowed_region + '\'' +
                ", time=" + time +
                ", image_url='" + image_url + '\'' +
                ", market_price=" + market_price +
                ", goods_name='" + goods_name + '\'' +
                ", hd_thumb_url='" + hd_thumb_url + '\'' +
                ", quantity=" + quantity +
                ", is_onsale=" + is_onsale +
                ", goods_id=" + goods_id +
                ", is_app=" + is_app +
                ", mall_id=" + mall_id +
                ", region_limit=" + region_limit +
                '}';
    }
}
