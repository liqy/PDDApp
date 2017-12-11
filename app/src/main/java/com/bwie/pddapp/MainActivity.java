package com.bwie.pddapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.bwie.pddapp.data.Goods;
import com.bwie.pddapp.data.PddApi;
import com.bwie.pddapp.data.ResponseData;
import com.bwie.pddapp.data.RxSchedulers;
import com.bwie.pddapp.data.SignInterceptor;
import com.bwie.pddapp.data.Urls;

import org.reactivestreams.Publisher;

import java.util.ArrayList;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.FlowableTransformer;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client=new OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(new SignInterceptor())
                .build();

        Retrofit retrofit=new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .baseUrl(Urls.HOST_V3)
                .build();

        PddApi api=retrofit.create(PddApi.class);

        api.list()
        .compose(RxSchedulers.<ResponseData<ArrayList<Goods>>>io_main())
        .compose(this.<ArrayList<Goods>>handleResult())
        .subscribe(new Consumer<ArrayList<Goods>>() {
            @Override
            public void accept(ArrayList<Goods> goods) throws Exception {
                Log.d(getLocalClassName(),goods.toString());
            }
        })
        ;
    }

    public <T> FlowableTransformer<ResponseData<T>, T> handleResult() {
        return new FlowableTransformer<ResponseData<T>, T>() {
            @Override
            public Publisher<T> apply(Flowable<ResponseData<T>> upstream) {
                return upstream.flatMap(new Function<ResponseData<T>, Publisher<T>>() {
                    @Override
                    public Publisher<T> apply(ResponseData<T> tBaseResponse) throws Exception {
                        if (tBaseResponse.goods_list != null) {
                            return createData(tBaseResponse.goods_list);
                        } else {
                            return Flowable.error(new Exception(tBaseResponse.getMsg()));
                        }
                    }
                });
            }
        };
    }

    private <T> Flowable<T> createData(final T t) {
        return Flowable.create(new FlowableOnSubscribe<T>() {
            @Override
            public void subscribe(FlowableEmitter<T> e) throws Exception {
                try {
                    e.onNext(t);
                    e.onComplete();
                } catch (Exception exc) {
                    e.onError(exc);
                }
            }
        }, BackpressureStrategy.BUFFER);
    }
}
