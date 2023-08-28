package com.java.wanghaoran.service;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.java.wanghaoran.R;

public final class PictureManager {
    private static PictureManager instance = new PictureManager();

    public PictureManager getInstance(){
        return instance;
    }

    public static void loadPictureWithPlaceHolder(Context context, String url, ImageView picture) {
        Glide.with(context).asBitmap().error(R.drawable.news_placeholder).placeholder(R.drawable.ic_loading).load(url).into(picture);
    }

    public static void loadPictureWithoutPlaceHolder(Context context, String url, ImageView picture) {
        Glide.with(context).asBitmap().error(R.drawable.news_placeholder).load(url).into(picture);
    }
}
