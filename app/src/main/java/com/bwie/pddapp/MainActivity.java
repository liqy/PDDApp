package com.bwie.pddapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.bwie.pddapp.data.AvatarList;
import com.bwie.pddapp.data.Goods;
import com.bwie.pddapp.data.GoodsAdapter;
import com.bwie.pddapp.data.PddApi;
import com.bwie.pddapp.data.RequestParams;
import com.bwie.pddapp.data.ResponseData;
import com.bwie.pddapp.data.RxSchedulers;
import com.bwie.pddapp.data.SignInterceptor;
import com.bwie.pddapp.data.Urls;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.reactivestreams.Publisher;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(new SignInterceptor())
                .build();

        final Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .baseUrl(Urls.HOST_V3)
                .build();

        final PddApi api = retrofit.create(PddApi.class);

        api.list()
                .compose(RxSchedulers.<ResponseData<ArrayList<Goods>>>io_main())
                .compose(this.<ArrayList<Goods>>handleResult())
                .subscribe(new Consumer<ArrayList<Goods>>() {
                    @Override
                    public void accept(final ArrayList<Goods> goods) throws Exception {
                        Log.d(getLocalClassName(), goods.toString());

                        List<Long> goods_ids = new ArrayList<>();

                        for (Goods g : goods) {
                            goods_ids.add(g.goods_id);
                        }
                        RequestParams params = new RequestParams();
                        params.goods_ids = goods_ids;

                        api.local_groups(params)
                                .map(new Function<JsonObject, Map<String, AvatarList>>() {
                                    @Override
                                    public Map<String, AvatarList> apply(JsonObject jsonObject) throws Exception {
                                        jsonObject.remove("server_time");
                                        Type type = new TypeToken<Map<String, AvatarList>>() {
                                        }.getType();
                                        return new Gson().fromJson(jsonObject, type);
                                    }
                                })
                                .map(new Function<Map<String, AvatarList>, ArrayList<Goods>>() {
                                    @Override
                                    public ArrayList<Goods> apply(Map<String, AvatarList> stringAvatarListMap) throws Exception {
                                        for (Goods g : goods) {
                                            g.avatarLists = stringAvatarListMap.get(g.getGoodsId());
                                        }
                                        return goods;
                                    }
                                })
                                .compose(RxSchedulers.<ArrayList<Goods>>io_main())
                                .subscribe(new Consumer<ArrayList<Goods>>() {
                                    @Override
                                    public void accept(ArrayList<Goods> goods) throws Exception {
                                        Log.d(getLocalClassName(), goods.toString());

                                        GoodsAdapter adapter=new GoodsAdapter(goods);

                                        recycler.setAdapter(adapter);
                                    }
                                });
                    }
                });


        //初始化界面
        GridLayoutManager manager = new GridLayoutManager(this, 6);

        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (position) {
                    case 0:
                    case 1:
                        return 3;
                    case 2:
                        return 6;
                    default:
                        return 3;
                }
            }
        });

        recycler=(RecyclerView)findViewById(R.id.recycler);
        recycler.setLayoutManager(manager);



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
