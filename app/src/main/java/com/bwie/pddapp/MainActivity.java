package com.bwie.pddapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.bwie.pddapp.data.AvatarList;
import com.bwie.pddapp.data.Goods;
import com.bwie.pddapp.data.GoodsAdapter;
import com.bwie.pddapp.data.NewsList;
import com.bwie.pddapp.data.PddApi;
import com.bwie.pddapp.data.RequestParams;
import com.bwie.pddapp.data.ResponseData;
import com.bwie.pddapp.data.RetrofitHelper;
import com.bwie.pddapp.data.RxSchedulers;
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

public class MainActivity extends AppCompatActivity {

    RecyclerView recycler;
    GoodsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        getData2();
    }

    private void getData2() {

        final PddApi api = RetrofitHelper.getAPI();

        Flowable<ArrayList<NewsList>> head = api.avatars_subjects()
                .map(new Function<ResponseData<ArrayList<Goods>>, ArrayList<NewsList>>() {
                    @Override
                    public ArrayList<NewsList> apply(ResponseData<ArrayList<Goods>> data) throws Exception {
                        ArrayList<NewsList> list = new ArrayList<>();

                        if (data.avatars != null)
                            list.add(new NewsList(NewsList.HEAD, data.avatars));

                        if (data.goods_list != null)
                            list.add(new NewsList(NewsList.HEAD, data.goods_list));


                        list.add(new NewsList(NewsList.LABEL));

                        return list;
                    }
                });

        final Flowable<ArrayList<NewsList>> flowList = api.list(Urls.HNEW_LIST)
                .map(new Function<ResponseData<ArrayList<Goods>>, ArrayList<NewsList>>() {
                    @Override
                    public ArrayList<NewsList> apply(ResponseData<ArrayList<Goods>> data) throws Exception {
                        ArrayList<NewsList> list = new ArrayList<>();
                        if (data.goods_list != null) {
                            ArrayList<Goods> goodsList = data.goods_list;
                            for (Goods g : goodsList) {
                                list.add(new NewsList(NewsList.ITEM, g));
                            }
                        }
                        return list;
                    }
                });


        Flowable.concat(head, flowList)
                .compose(RxSchedulers.<ArrayList<NewsList>>io_main())
                .subscribe(new Consumer<ArrayList<NewsList>>() {
                    @Override
                    public void accept(ArrayList<NewsList> newsLists) throws Exception {
                        Log.d(getLocalClassName(), newsLists.toString());
                        adapter.setDataList(newsLists);

                    }
                });


    }

    private void getData1() {
        final PddApi api = RetrofitHelper.getAPI();

        api.list(Urls.HNEW_LIST)
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

                                    }
                                });
                    }
                });
    }


    /**
     * 初始化界面
     */
    private void initView() {
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

        recycler = (RecyclerView) findViewById(R.id.recycler);
        recycler.setLayoutManager(manager);

        adapter = new GoodsAdapter(MainActivity.this);
        recycler.setAdapter(adapter);

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
