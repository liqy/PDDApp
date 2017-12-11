package com.bwie.pddapp.data;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.bwie.pddapp.R;

import java.util.List;

/**
 * Created by liqy on 2017/12/11.
 */

public class GoodsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Goods> list;

    public GoodsAdapter(List<Goods> list) {
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.goods_item, parent, false);
        return new GoodsViewHolder(view);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        GoodsViewHolder goodsViewHolder = (GoodsViewHolder) holder;
        goodsViewHolder.textView.setText("位置：" + position);

    }

//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
//        super.onBindViewHolder(holder, position, payloads);
//    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class GoodsViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public GoodsViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.title);
        }
    }

}
