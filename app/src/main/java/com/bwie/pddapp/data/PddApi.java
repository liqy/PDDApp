package com.bwie.pddapp.data;

import java.util.ArrayList;

import io.reactivex.Flowable;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by liqy on 2017/12/11.
 */

public interface PddApi {

    @GET(Urls.HNEW_LIST)
    Flowable<ResponseData<ArrayList<Goods>>> list();

}
