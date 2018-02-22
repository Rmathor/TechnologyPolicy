package com.mathor.technologypolicy.domain;

/**
 * Author: mathor
 * Date : on 2017/12/7 15:21
 */

public class Fujian {
    private String title;
    private String url;

    public Fujian() {
    }

    public Fujian(String title, String url) {
        this.title = title;
        this.url = url;
    }

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

    @Override
    public String toString() {
        return "Fujian{" +
                "title='" + title + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
