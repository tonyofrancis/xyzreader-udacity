package com.tonyostudio.xyzreader.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tonyostudio.xyzreader.R;
import com.tonyostudio.xyzreader.entity.Article;
import com.tonyostudio.xyzreader.util.AppModule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmChangeListener;

/**
 * A fragment representing a single Article detail screen. This fragment is
 * either contained in a {@link ArticleListActivity} in two-pane mode (on
 * tablets) or a {@link ArticleDetailActivity} on handsets.
 */
public class ArticleDetailFragment extends Fragment {

    public static final String ARTICLE_ID = "article_id";

    @BindView(R.id.content_text_view)
    TextView contentTextView;
    @BindView(R.id.title_text_view)
    TextView titleTextView;
    @BindView(R.id.poster) AspectRatioImageView posterImageView;
    @BindView(R.id.author_text_view)
    TextView authorTextView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar_layout) CollapsingToolbarLayout collapsingToolbarLayout;

    private Article article;

    public static ArticleDetailFragment newInstance(int articleId) {
        Bundle arguments = new Bundle();
        arguments.putInt(ARTICLE_ID, articleId);
        ArticleDetailFragment fragment = new ArticleDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_article_detail,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        setUpToolbar();

        int articleId = getArguments().getInt(ARTICLE_ID);

        article = Realm.getDefaultInstance().where(Article.class)
                .equalTo("id",articleId)
                .findFirstAsync();

    }

    private void setUpToolbar() {
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();

        toolbar.setNavigationOnClickListener(view -> appCompatActivity.finish());
        appCompatActivity.setSupportActionBar(toolbar);

        ActionBar actionBar = appCompatActivity.getSupportActionBar();

        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        article.addChangeListener(articleChangeListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        article.removeChangeListener(articleChangeListener);
    }

    private RealmChangeListener<Article> articleChangeListener = loadedArticle -> {

        collapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(getContext(),android.R.color.transparent));
        collapsingToolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(getContext(),android.R.color.white));
        collapsingToolbarLayout.setTitle(article.getTitle());


        contentTextView.setText(Html.fromHtml(loadedArticle.getBody()));
        titleTextView.setText(article.getTitle());

        String imageDes = getContext().getString(R.string.article_image_content_desc,article.getTitle());
        posterImageView.setContentDescription(imageDes);

        posterImageView.setAspectRatio((float)article.getAspect_ratio());

        AppModule.picasso(getContext()).load(loadedArticle.getPhoto()).into(posterImageView);

        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).parse(loadedArticle.getPublished_date());
            String formattedDate = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(date);
            String createdString = getString(R.string.date_author,formattedDate,loadedArticle.getAuthor());
            authorTextView.setText(createdString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    };



    @OnClick(R.id.action)
    public void shareButtonClicked(View view) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");

        intent.putExtra(Intent.EXTRA_TEXT,article.getBody());
        intent.putExtra(Intent.EXTRA_SUBJECT,article.getTitle());

        startActivity(Intent.createChooser(intent,getString(R.string.action_share)));
    }
}
