package com.tonyostudio.xyzreader.ui;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tonyostudio.xyzreader.R;
import com.tonyostudio.xyzreader.entity.Article;
import com.tonyostudio.xyzreader.service.ArticleService;
import com.tonyostudio.xyzreader.util.AppModule;
import com.tonyostudio.xyzreader.util.NetworkUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class ArticleListActivity extends AppCompatActivity implements ArticleAdapter.OnArticleClickListener {

    @BindView(R.id.container)
    RelativeLayout container;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.toolbar_title_text_view)
    TextView titleTextView;
    @BindView(R.id.no_article_view)
    RelativeLayout noArticleView;

    private RealmResults<Article> articles;
    private ArticleAdapter articleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        titleTextView.setTypeface(AppModule.getBrandTypeface(getAssets()));
        articleAdapter = new ArticleAdapter(AppModule.picasso(this));
        articleAdapter.setArticleClickListener(this);

        int columnSpan = getResources().getInteger(R.integer.article_list_columns);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(columnSpan,StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(articleAdapter);

        articles = Realm.getDefaultInstance()
                .where(Article.class)
                .findAllAsync();

        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent,R.color.colorSecondary);
        swipeRefreshLayout.setOnRefreshListener(this::startArticleService);
    }


    @Override
    protected void onResume() {
        super.onResume();
        articles.addChangeListener(articleChangeListener);
        registerReceiver(updateServiceReceiver, ArticleService.getBroadcastCompleteIntentFilter());

        checkNetworkConnection();
    }

    @Override
    protected void onPause() {
        super.onPause();
        articles.removeChangeListener(articleChangeListener);
        unregisterReceiver(updateServiceReceiver);
    }

    private void checkNetworkConnection() {

        if(!NetworkUtils.isNetworkAvailable(this)) {
            Snackbar snackbar = Snackbar.make(container,R.string.no_network_connection,Snackbar.LENGTH_SHORT);
            snackbar.getView().setBackgroundColor(ContextCompat.getColor(ArticleListActivity.this,R.color.colorAccent));
            snackbar.show();
        }
    }

    private void startArticleService() {
        startService(ArticleService.getIntent(this));
    }

    private RealmChangeListener<RealmResults<Article>> articleChangeListener = articles -> {

        if(articles.size() == 0) {
            showNoArticleView(true);
        } else {
            showNoArticleView(false);
        }

        articleAdapter.swapData(articles);
    };

    private void showNoArticleView(boolean showView) {

        if(showView) {
            noArticleView.setVisibility(View.VISIBLE);
        } else {
            noArticleView.setVisibility(View.GONE);
        }
    }

    private BroadcastReceiver updateServiceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().equals(ArticleService.UPDATE_COMPLETE_ACTION)) {
                swipeRefreshLayout.setRefreshing(false);
            }
        }
    };

    @Override
    public void onArticleClick(@NonNull Article article) {
        Intent intent = ArticleDetailActivity.getIntent(this,article.getId());
        startActivity(intent);
    }
}
