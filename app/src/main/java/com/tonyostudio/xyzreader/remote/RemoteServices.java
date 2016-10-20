package com.tonyostudio.xyzreader.remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by tonyofrancis on 10/9/16.
 */

public final class RemoteServices {

    private RemoteServices() {}

    public static ArticleService getArticleService() {

        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ArticleService.BASE_URL)
                .build()
                .create(ArticleService.class);
    }
}
