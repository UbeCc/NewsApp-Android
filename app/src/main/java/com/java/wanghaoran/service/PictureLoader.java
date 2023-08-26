package com.java.wanghaoran.service;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.java.wanghaoran.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

public final class PictureLoader {
    private static PictureLoader instance = new PictureLoader();

    public static void loadPictureWithoutPlaceHolder(Context context, String url, ImageView picture) {
        Glide.with(context).asBitmap().error(R.drawable.news_placeholder).load(url).into(picture);
    }

    public static void loadPictureWithPlaceHolder(Context context, String url, ImageView picture) {
        Glide.with(context).asBitmap().error(R.drawable.news_placeholder).placeholder(R.drawable.loading).load(url).into(picture);
    }

    public PictureLoader getInstance(){
        return instance;
    }
}
