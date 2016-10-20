package com.tonyostudio.xyzreader.remote;


import com.tonyostudio.xyzreader.entity.Article;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by tonyofrancis on 10/9/16.
 */

public interface ArticleService {

    String BASE_URL = "https://dl.dropboxusercontent.com/";

    @GET("u/231329/xyzreader_data/data.json")
    Call<List<Article>> fetch();
}
