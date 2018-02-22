package com.mathor.technologypolicy.domain;

/**
 * Author: mathor
 * Date : on 2017/11/9 17:07
 */

public class Technology {
    
    private String title;
    private String date;
    private String url;

    public Technology() {
    }

    public Technology(String title, String date, String url) {
        this.title = title;
        this.date = date;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Technology{" +
                "title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
