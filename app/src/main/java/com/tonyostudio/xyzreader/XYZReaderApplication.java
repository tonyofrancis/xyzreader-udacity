package com.tonyostudio.xyzreader;

import android.app.Application;

import com.tonyostudio.xyzreader.service.ArticleService;

import io.realm.Realm;

/**
 * Created by tonyofrancis on 10/10/16.
 */

public class XYZReaderApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        startArticleFetchService();
    }

    private void startArticleFetchService() {
        startService(ArticleService.getIntent(this));
    }
}
