package com.bwie.pddapp.widget;

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;

import com.bwie.pddapp.R;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Description:
 * Created by hdz on 2017/6/13.
 */

public abstract class CoverAdapter<T> {

    public abstract void onDisplayImage(Context context, ImageView imageView, T t);

    public ImageView generateImageView(Context context) {
        //可以在这里更改Imageview的类型
        CircleImageView circleImageView = new CircleImageView(context);
//        ImageView imageView = new ImageView(context);
        circleImageView.setBorderColor(Color.parseColor("#dcdcdc"));
        circleImageView.setBorderWidth(4);
        return circleImageView;
    }
}
