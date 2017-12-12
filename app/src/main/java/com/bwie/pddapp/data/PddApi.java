package com.bwie.pddapp.data;

import com.google.gson.JsonObject;

import java.util.ArrayList;

import io.reactivex.Flowable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by liqy on 2017/12/11.
 */

public interface PddApi {

    /**
     * 动态URL
     * @param url
     * @return
     */
    @GET
    Flowable<ResponseData<ArrayList<Goods>>> list(@Url String url);

    @POST("goods/local_groups")
    Flowable<JsonObject> local_groups(@Body RequestParams params);

    @GET("avatars_subjects")
    Flowable<ResponseData<ArrayList<Goods>>> avatars_subjects();



}
