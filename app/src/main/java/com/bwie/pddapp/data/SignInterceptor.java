package com.bwie.pddapp.data;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by liqy on 2017/12/11.
 */

public class SignInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {

        Request oldRequest = chain.request();

        HttpUrl.Builder httpUrl = oldRequest.url().newBuilder()
                .scheme(oldRequest.url().scheme())
                .host(oldRequest.url().host())
                .addQueryParameter("pdduid", "3470667255");
        ;

        Request newRequest = oldRequest.newBuilder()
                .method(oldRequest.method(), oldRequest.body())
                .url(httpUrl.build())
                .build();

        return chain.proceed(newRequest);
    }
}
