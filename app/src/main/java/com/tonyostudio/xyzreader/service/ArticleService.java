package com.tonyostudio.xyzreader.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tonyostudio.xyzreader.entity.Article;
import com.tonyostudio.xyzreader.remote.RemoteServices;

import java.io.IOException;
import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Response;


/**
 * Created by tonyofrancis on 10/10/16.
 */

public class ArticleService extends IntentService {
    public static final String UPDATE_COMPLETE_ACTION = "com.example.xyzreader.service.ArticleService.UPDATE_COMPLETE";

    public ArticleService() {
        super("ArticleService");
    }

    public static Intent getIntent(@NonNull Context context) {
        return new Intent(context,ArticleService.class);
    }

    @NonNull
    public static IntentFilter getBroadcastCompleteIntentFilter() {
        return new IntentFilter(ArticleService.UPDATE_COMPLETE_ACTION);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        List<Article> articles = fetchArticleData();

        if(articles != null) {
            saveArticlesToDatabase(articles);
        }

        sendCompleteBroadcast();
    }

    private void saveArticlesToDatabase(@NonNull List<Article> articles) {
        Realm.getDefaultInstance().executeTransaction(realm -> realm.copyToRealmOrUpdate(articles));
    }

    @Nullable
    private List<Article> fetchArticleData() {

        List<Article> articles = null;
        Call<List<Article>> articleCall = RemoteServices.getArticleService().fetch();

        try {
            Response<List<Article>> response = articleCall.execute();
            articles = response.body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return articles;
    }

    private void sendCompleteBroadcast() {
        Intent intent = new Intent(UPDATE_COMPLETE_ACTION);
        sendBroadcast(intent);
    }

}
