package com.mathor.technologypolicy.db;

import android.provider.BaseColumns;

/**
 * 数据库对象属性
 */
public class InfoMateData {
    private InfoMateData() {
    }

    public static abstract class User implements BaseColumns {
        public static final String TABLE_NAME = "users_tab";
        public static final String NAME = "name";
        public static final String PHONE_NUM = "phone_num";
        public static final String ICON_PATH = "icon_path";
        public static final String TUISONG_TAG = "tuisong_tag";
    }
}
