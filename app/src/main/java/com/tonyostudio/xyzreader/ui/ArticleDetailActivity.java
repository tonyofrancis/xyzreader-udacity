package com.tonyostudio.xyzreader.ui;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.tonyostudio.xyzreader.R;
import com.tonyostudio.xyzreader.entity.Article;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * An activity representing a single Article detail screen, letting you swipe between articles.
 */
public class ArticleDetailActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "article_id";
    public static final String READING_LOCATION = "reading_location";

    @BindView(R.id.pager)ViewPager viewPager;

    private RealmResults<Article> articles;
    private ArticlePagerAdapter adapter;

    private int restoredPagerPosition;

    @NonNull
    public static Intent getIntent(@NonNull Context context, int id) {
        Intent intent = new Intent(context,ArticleDetailActivity.class);
        intent.putExtra(EXTRA_ID,id);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);
        ButterKnife.bind(this);
        restoredPagerPosition = -1;

        adapter = new ArticlePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        articles = Realm.getDefaultInstance()
                .where(Article.class)
                .findAllAsync();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        restoredPagerPosition = savedInstanceState.getInt(READING_LOCATION,0);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        int position = viewPager.getCurrentItem();
        outState.putInt(READING_LOCATION,position);
    }

    @Override
    protected void onResume() {
        super.onResume();
        articles.addChangeListener(articleChangeListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        articles.removeChangeListener(articleChangeListener);
    }

    private RealmChangeListener<RealmResults<Article>> articleChangeListener = articles -> {
        adapter.swapData(articles);
        setViewPagerPosition();
    };

    private void setViewPagerPosition() {
        int viewPagerPosition;

        if(restoredPagerPosition != -1) {
            viewPagerPosition = restoredPagerPosition;
        } else {
            int articleId = getIntent().getIntExtra(EXTRA_ID,-1);
            viewPagerPosition = getArticlePosition(articleId);
        }

        viewPager.setCurrentItem(viewPagerPosition,false);
    }

    private int getArticlePosition(int articleId) {

        for (int x = 0; x < articles.size(); x++) {
            Article article = articles.get(x);

            if(article.getId() == articleId) {
                return x;
            }
        }

        return 0;
    }
}
