package com.mathor.technologypolicy.domain;

/**
 * Author: mathor
 * Date : on 2017/11/5 21:26
 * 省政策实体类
 */

public class ProvincePolicy{
    private String title;
    private String url;
    private String date;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "CountryPolicy{" +
                "title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
