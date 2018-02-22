package com.mathor.technologypolicy.domain;

import java.util.ArrayList;

/**
 * Author: mathor
 * Date : on 2017/11/12 14:25
 */

public class Search {
    private int total;//结果总数
    private int pageIndex;//当前页
    private int pageCount;//总共页数
    private boolean Success;//请求是否成功
    private ArrayList<Data> Data;//请求到的内容

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public boolean isSuccess() {
        return Success;
    }

    public void setSuccess(boolean success) {
        Success = success;
    }

    public ArrayList<Search.Data> getData() {
        return Data;
    }

    public void setData(ArrayList<Search.Data> data) {
        Data = data;
    }

    @Override
    public String toString() {
        return "Search{" +
                "total=" + total +
                ", pageIndex=" + pageIndex +
                ", pageCount=" + pageCount +
                ", Success=" + Success +
                ", Data=" + Data +
                '}';
    }

    public class Data{

        private String clsid;
        private String content;//概要
        private long date;//日期
        private String description;
        private String title;//标题
        private String type;//类型
        private String url;//链接
        private String xdrmc;

        public String getClsid() {
            return clsid;
        }

        public void setClsid(String clsid) {
            this.clsid = clsid;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public long getDate() {
            return date;
        }

        public void setDate(long date) {
            this.date = date;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getXdrmc() {
            return xdrmc;
        }

        public void setXdrmc(String xdrmc) {
            this.xdrmc = xdrmc;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "clsid='" + clsid + '\'' +
                    ", content='" + content + '\'' +
                    ", date=" + date +
                    ", description='" + description + '\'' +
                    ", title='" + title + '\'' +
                    ", type='" + type + '\'' +
                    ", url='" + url + '\'' +
                    ", xdrmc='" + xdrmc + '\'' +
                    '}';
        }
    }
}
