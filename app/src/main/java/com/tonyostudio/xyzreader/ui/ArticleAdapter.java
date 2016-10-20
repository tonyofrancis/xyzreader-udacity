package com.tonyostudio.xyzreader.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tonyostudio.xyzreader.R;
import com.tonyostudio.xyzreader.entity.Article;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tonyofrancis on 10/19/16.
 */

public final class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    private List<Article> articles;
    private Picasso picasso;
    private OnArticleClickListener articleClickListener;


    ArticleAdapter(Picasso picasso) {
        this.picasso = picasso;
    }

    @Override
    public ArticleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(viewType, parent, false);
        view.setOnClickListener(itemClickListener);

        return new ViewHolder(view);
    }

    private View.OnClickListener itemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if(isListenerNull()) {
                return;
            }

            Article article = (Article) view.getTag();
            articleClickListener.onArticleClick(article);
        }
    };


    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);
        return R.layout.list_item_article;
    }

    @Override
    public void onBindViewHolder(ArticleAdapter.ViewHolder holder, int position) {

        Context context = holder.itemView.getContext();
        Article article = articles.get(position);

        holder.itemView.setTag(article);
        holder.titleView.setText(article.getTitle());
        holder.contentTextView.setText(Html.fromHtml(article.getBody()));

        String imageDes = context.getString(R.string.article_image_content_desc,article.getTitle());
        holder.thumbnailView.setContentDescription(imageDes);

        holder.thumbnailView.setAspectRatio((float)article.getAspect_ratio());

        picasso.load(article.getThumb()).into(holder.thumbnailView);
    }

    @Override
    public int getItemCount() {

        if(articles == null) {
            return 0;
        }

        return articles.size();
    }

    public void swapData(@Nullable List<Article> articles) {
        this.articles = articles;
        notifyDataSetChanged();
    }

    private boolean isListenerNull() {
        return articleClickListener == null;
    }

    public void setArticleClickListener(@Nullable OnArticleClickListener articleClickListener) {
        this.articleClickListener = articleClickListener;
    }

    public interface OnArticleClickListener {
        void onArticleClick(@NonNull Article article);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.thumbnail) AspectRatioImageView thumbnailView;
        @BindView(R.id.article_title)
        TextView titleView;
        @BindView(R.id.article_content)
        TextView contentTextView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }
}