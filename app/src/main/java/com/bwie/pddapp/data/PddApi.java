package com.bwie.pddapp.data;

import com.google.gson.JsonObject;

import java.util.ArrayList;

import io.reactivex.Flowable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by liqy on 2017/12/11.
 */

public interface PddApi {

    @GET(Urls.HNEW_LIST)
    Flowable<ResponseData<ArrayList<Goods>>> list();

    @POST("goods/local_groups")
    Flowable<JsonObject> local_groups(@Body RequestParams params);

}
