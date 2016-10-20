package com.tonyostudio.xyzreader.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.annotation.NonNull;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;

import java.io.File;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * Created by tonyofrancis on 10/9/16.
 */

public final class AppModule {

    private static final int PICASSO_DISK_CACHE_SIZE = 50 * 1024 * 1024;
    private static final int PICASSO_MEMORY_CACHE_SIZE = 20 * 1024 * 1024;

    private AppModule() {}

    @NonNull
    public static Picasso picasso(@NonNull Context context) {

        File cacheDir = new File(context.getCacheDir(),"picasso-cache");
        boolean cacheCreated;

        if(!cacheDir.exists()) {
           cacheCreated = cacheDir.mkdir();
        } else {
            cacheCreated = true;
        }

        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();

        if(cacheCreated) {
            okHttpBuilder.cache(new Cache(cacheDir,PICASSO_DISK_CACHE_SIZE));
        }

        OkHttpClient picassoClient = okHttpBuilder.build();

        return  new Picasso.Builder(context)
                .memoryCache(new LruCache(PICASSO_MEMORY_CACHE_SIZE))
                .downloader(new OkHttp3Downloader(picassoClient))
                .build();
    }

    public static Typeface getBrandTypeface(@NonNull AssetManager assetManager) {
        return  Typeface.createFromAsset(assetManager,"WendyOne-Regular.ttf");
    }

}
