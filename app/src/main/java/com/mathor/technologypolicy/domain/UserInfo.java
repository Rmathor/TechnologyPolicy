package com.mathor.technologypolicy.domain;

/**
 * Author: mathor
 * Date : on 2017/11/21 13:07
 */

public class UserInfo {

    private int id;
    private String name;
    private String phone_num;
    private String icon_path;
    private String tuisong_tag;

    public UserInfo(){

    }

    public UserInfo(int id,String name, String phone_num, String icon_path,String tuisong_tag) {
        this.id = id;
        this.name = name;
        this.phone_num = phone_num;
        this.icon_path = icon_path;
        this.tuisong_tag = tuisong_tag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_num() {
        return phone_num;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }

    public String getIcon_path() {
        return icon_path;
    }

    public void setIcon_path(String icon_path) {
        this.icon_path = icon_path;
    }

    public String getTuisong_tag() {
        return tuisong_tag;
    }

    public void setTuisong_tag(String tuisong_tag) {
        this.tuisong_tag = tuisong_tag;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone_num='" + phone_num + '\'' +
                ", icon_path='" + icon_path + '\'' +
                ", tuisong_tag='" + tuisong_tag + '\'' +
                '}';
    }
}
