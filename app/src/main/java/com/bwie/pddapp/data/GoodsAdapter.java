package com.bwie.pddapp.data;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bwie.pddapp.R;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liqy on 2017/12/11.
 */

public class GoodsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private int windowsHeight;
    private int windowsWight;

    public Activity activity;


    List<NewsList> list;

    public void setDataList(List<NewsList> list) {
        int positionStart = this.list.size();
        this.list.addAll(list);
        int itemCount = list.size();
        notifyItemRangeChanged(positionStart, itemCount);

    }

    public GoodsAdapter(Activity context) {
        this.list = new ArrayList<>();
        //获取屏幕高宽
        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
        windowsHeight = metric.heightPixels;
        windowsWight = metric.widthPixels;

        this.activity = context;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        if (viewType == NewsList.HEAD) {
            return new AvatarViewHolder(getTypeView(context, parent, R.layout.avatar_item));
        } else if (viewType == NewsList.LABEL) {
            return new LabelViewHolder(getTypeView(context, parent, R.layout.label_item));
        } else {
            return new GoodsViewHolder(getTypeView(context, parent, R.layout.goods_item));
        }
    }

    private View getTypeView(Context context, ViewGroup parent, int res) {
        return LayoutInflater.from(context).inflate(res, parent, false);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        NewsList newsList = list.get(position);

        if (holder instanceof GoodsViewHolder) {
            Goods goods = newsList.goods;
            GoodsViewHolder goodsViewHolder = (GoodsViewHolder) holder;
            goodsViewHolder.textView.setText(goods.goods_name);
            goodsViewHolder.imageView.setImageURI(goods.hd_thumb_url);

            if (goods.avatarLists != null) {

                ArrayList<String> list = new ArrayList<>();
                for (GroupAvatar groupAvatar : goods.avatarLists.list) {
                    list.add(groupAvatar.avatar);
                }
            }
        } else if (holder instanceof AvatarViewHolder) {
            AvatarViewHolder avatarViewHolder = (AvatarViewHolder) holder;
            if (position == 1) {
                avatarViewHolder.head_label.setText("精选专题");
            } else {
                avatarViewHolder.head_label.setText("邻里团");
            }

        } else if (holder instanceof LabelViewHolder) {

            LabelViewHolder labelViewHolder = (LabelViewHolder) holder;

        }

    }

//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
//        super.onBindViewHolder(holder, position, payloads);
//    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
            case 1:
                return NewsList.HEAD;
            case 2:
                return NewsList.LABEL;
            default:
                return NewsList.ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class GoodsViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        SimpleDraweeView imageView;

        public GoodsViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.title);
            imageView = (SimpleDraweeView) itemView.findViewById(R.id.thumb_image);
        }
    }

    class LabelViewHolder extends RecyclerView.ViewHolder {

        TextView label;

        public LabelViewHolder(View itemView) {
            super(itemView);
            label = (TextView) itemView.findViewById(R.id.label);
        }
    }

    class AvatarViewHolder extends RecyclerView.ViewHolder {
        TextView head_label;
        TextView head_desc;

        public AvatarViewHolder(View itemView) {
            super(itemView);
            head_desc = (TextView) itemView.findViewById(R.id.head_desc);
            head_label = (TextView) itemView.findViewById(R.id.head_label);
        }
    }


}
