package com.tonyostudio.xyzreader.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by tonyofrancis on 10/9/16.
 */

public class Article extends RealmObject {

    @PrimaryKey
    private int id;
    private String photo;
    private String thumb;
    private double aspect_ratio;
    private String author;
    private String title;
    private String published_date;
    private String body;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public double getAspect_ratio() {
        return aspect_ratio;
    }

    public void setAspect_ratio(double aspect_ratio) {
        this.aspect_ratio = aspect_ratio;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublished_date() {
        return published_date;
    }

    public void setPublished_date(String published_date) {
        this.published_date = published_date;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
