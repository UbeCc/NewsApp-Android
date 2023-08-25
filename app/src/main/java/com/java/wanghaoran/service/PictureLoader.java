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
        switch (url.hashCode() % 5){
            case 0:
                Glide.with(context).asBitmap().error(R.drawable.news).load(url).into(picture);break;
            case 1:
                Glide.with(context).asBitmap().error(R.drawable.news).load(url).into(picture);break;
            case 2:
                Glide.with(context).asBitmap().error(R.drawable.news).load(url).into(picture);break;
            case 3:
                Glide.with(context).asBitmap().error(R.drawable.news).load(url).into(picture);break;
            default:
                Glide.with(context).asBitmap().error(R.drawable.news).load(url).into(picture);break;
        }
        // Glide.with(context).asBitmap().error(R.drawable.loading).load(url).into(picture);break;
    }

    public static void loadPictureWithPlaceHolder(Context context, String url, ImageView picture) {

        Glide.with(context).asBitmap().error(R.drawable.news).placeholder(R.drawable.loading).load(url).centerCrop().into(picture);
        switch (url.hashCode() % 5){
            case 0:
                Glide.with(context).asBitmap().error(R.drawable.news).placeholder(R.drawable.loading).load(url).into(picture);break;
            case 1:
                Glide.with(context).asBitmap().error(R.drawable.news).placeholder(R.drawable.loading).load(url).into(picture);break;
            case 2:
                Glide.with(context).asBitmap().error(R.drawable.news).placeholder(R.drawable.loading).load(url).into(picture);break;
            case 3:
                Glide.with(context).asBitmap().error(R.drawable.news).placeholder(R.drawable.loading).load(url).into(picture);break;
            default:
                Glide.with(context).asBitmap().error(R.drawable.news).placeholder(R.drawable.loading).load(url).into(picture);break;
        }

    }

    public PictureLoader getInstance(){
        return instance;
    }
}
