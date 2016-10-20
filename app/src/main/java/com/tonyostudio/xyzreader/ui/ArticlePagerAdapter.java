package com.tonyostudio.xyzreader.ui;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.tonyostudio.xyzreader.entity.Article;

import java.util.List;

/**
 * Created by tonyofrancis on 10/19/16.
 */

public final class ArticlePagerAdapter extends FragmentStatePagerAdapter {

    private List<Article> articles;

    public ArticlePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        int articleId = articles.get(position).getId();
        return ArticleDetailFragment.newInstance(articleId);
    }

    @Override
    public int getCount() {

        if (articles == null) {
            return 0;
        }

        return articles.size();
    }

    public void swapData(@Nullable List<Article> articles) {
        this.articles = articles;
        notifyDataSetChanged();
    }
}