package com.bwie.pddapp.data;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by liqy on 2017/12/12.
 */

public class RetrofitHelper {
    private static OkHttpClient okHttpClient;

    static {
        initOkHttpClient();
    }

    private static void initOkHttpClient() {

        if (okHttpClient == null) {
            synchronized (RetrofitHelper.class) {
                if (okHttpClient == null) {
                    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                    logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                    okHttpClient = new OkHttpClient.Builder()
                            .addInterceptor(logging)
                            .addInterceptor(new SignInterceptor())
                            .build();
                }
            }
        }
    }

    public static <T> T createAPI(Class<T> clazz, String hostUrl) {
        final Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .baseUrl(hostUrl)
                .build();
        return retrofit.create(clazz);
    }

    public static PddApi getAPI() {
        return createAPI(PddApi.class, Urls.HOST_V4);
    }

}
