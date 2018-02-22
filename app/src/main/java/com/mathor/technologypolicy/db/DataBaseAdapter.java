package com.mathor.technologypolicy.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mathor.technologypolicy.domain.UserInfo;

/**
 * 数据库操作类
 */
public class DataBaseAdapter {

    private DataBaseHelper mHelper;

    public DataBaseAdapter(Context context) {
        mHelper = new DataBaseHelper(context);
    }

    /**
     * 添加
     *
     * @param user
     */
    public void add(UserInfo user) {

        SQLiteDatabase database = mHelper.getWritableDatabase();
        ContentValues contentValue = new ContentValues();
        contentValue.put(InfoMateData.User.NAME, user.getName());
        contentValue.put(InfoMateData.User.PHONE_NUM, user.getPhone_num());
        contentValue.put(InfoMateData.User.ICON_PATH, user.getIcon_path());
        contentValue.put(InfoMateData.User.TUISONG_TAG,user.getTuisong_tag());
        database.insert(InfoMateData.User.TABLE_NAME, null, contentValue);

        database.close();
    }

//    /**
//     * 根据id删除对应的数据
//     *
//     * @param id
//     */
//    public void delete(int id) {
//
//        SQLiteDatabase database = mHelper.getWritableDatabase();
//
//        String whereClause = InfoMateData.StudentsInfo._ID + "= ?";
//        String[] whereArgs = {String.valueOf(id)};
//        database.delete(InfoMateData.StudentsInfo.TABLE_NAME, whereClause, whereArgs);
//        database.close();
//    }

    /**
     * 更新数据
     *
     * @param info
     */
    public void updateByPhone_num(UserInfo info) {

        SQLiteDatabase database = mHelper.getWritableDatabase();
        ContentValues contentValue = new ContentValues();
        contentValue.put(InfoMateData.User.NAME, info.getName());
        contentValue.put(InfoMateData.User.PHONE_NUM, info.getPhone_num());
        contentValue.put(InfoMateData.User.ICON_PATH, info.getIcon_path());
        contentValue.put(InfoMateData.User.TUISONG_TAG,info.getTuisong_tag());
        String whereClause = InfoMateData.User.PHONE_NUM + "= ?";
        String[] whereArgs = {String.valueOf(info.getPhone_num())};
        database.update(InfoMateData.User.TABLE_NAME, contentValue, whereClause, whereArgs);
        database.close();
    }

    /**
     * 根据id查找单个对象
     *
     * @param phone_num
     * @return
     */
    public UserInfo findByPhone_num(String phone_num) {

        SQLiteDatabase database = mHelper.getReadableDatabase();

        Cursor cursor = database.query(InfoMateData.User.TABLE_NAME, null, InfoMateData.User.PHONE_NUM + "=?", new String[]{phone_num}, null, null, null);
        UserInfo userInfo = null;
        if (cursor.moveToNext()) {

            userInfo = new UserInfo();
            userInfo.setId(cursor.getInt(cursor.getColumnIndexOrThrow(InfoMateData.User._ID)));
            userInfo.setName(cursor.getString(cursor.getColumnIndexOrThrow(InfoMateData.User.NAME)));
            userInfo.setPhone_num(cursor.getString(cursor.getColumnIndexOrThrow(InfoMateData.User.PHONE_NUM)));
            userInfo.setIcon_path(cursor.getString(cursor.getColumnIndexOrThrow(InfoMateData.User.ICON_PATH)));
            userInfo.setTuisong_tag(cursor.getString(cursor.getColumnIndexOrThrow(InfoMateData.User.TUISONG_TAG)));
        }
        cursor.close();
        database.close();
        return userInfo;
    }

//    /**
//     * 查找所有对象
//     *
//     * @return
//     */
//    public ArrayList<StudentsInfo> findAll() {
//
//        String sql = "select  * from students_tab order by _id desc";
//        SQLiteDatabase database = mHelper.getReadableDatabase();
//
//        ArrayList<StudentsInfo> studentsInfos = new ArrayList<>();
//        StudentsInfo studentsInfo = null;
//        Cursor cursor = database.rawQuery(sql, null);
//        while (cursor.moveToNext()) {
//
//            studentsInfo = new StudentsInfo();
//            studentsInfo.setId(cursor.getInt(cursor.getColumnIndexOrThrow(InfoMateData.StudentsInfo._ID)));
//            studentsInfo.setName(cursor.getString(cursor.getColumnIndexOrThrow(InfoMateData.StudentsInfo.NAME)));
//            studentsInfo.setAge(cursor.getInt(cursor.getColumnIndexOrThrow(InfoMateData.StudentsInfo.AGE)));
//            studentsInfo.setSex(cursor.getString(cursor.getColumnIndexOrThrow(InfoMateData.StudentsInfo.SEX)));
//
//            studentsInfos.add(studentsInfo);
//        }
//
//        cursor.close();
//        database.close();
//        return studentsInfos;
//    }

//    /**
//     * 根据姓名和id查询
//     *
//     * @param name
//     * @param id
//     * @return
//     */
//    public StudentsInfo findByNameAndId(String name, int id) {
//
//        SQLiteDatabase database = mHelper.getReadableDatabase();
//
//        Cursor cursor = database.query(InfoMateData.StudentsInfo.TABLE_NAME, null, InfoMateData.StudentsInfo.NAME + "=? and " + InfoMateData.StudentsInfo._ID + "=?", new String[]{name, String.valueOf(id)}, null, null, null);
//        StudentsInfo studentsInfo = null;
//        if (cursor.moveToNext()) {
//
//            studentsInfo = new StudentsInfo();
//            studentsInfo.setId(cursor.getInt(cursor.getColumnIndexOrThrow(InfoMateData.StudentsInfo._ID)));
//            studentsInfo.setName(cursor.getString(cursor.getColumnIndexOrThrow(InfoMateData.StudentsInfo.NAME)));
//            studentsInfo.setAge(cursor.getInt(cursor.getColumnIndexOrThrow(InfoMateData.StudentsInfo.AGE)));
//            studentsInfo.setSex(cursor.getString(cursor.getColumnIndexOrThrow(InfoMateData.StudentsInfo.SEX)));
//        }
//        cursor.close();
//        database.close();
//        return studentsInfo;
//    }

    /**
     * 根据姓名查询
     *
     * @param name
     * @return
     */
//    public StudentsInfo findByName(String name) {
//
//        SQLiteDatabase database = mHelper.getReadableDatabase();
//
//        Cursor cursor = database.query(InfoMateData.StudentsInfo.TABLE_NAME, null, InfoMateData.StudentsInfo.NAME + "=?", new String[]{name}, null, null, null);
//        StudentsInfo studentsInfo = null;
//        if (cursor.moveToNext()) {
//
//            studentsInfo = new StudentsInfo();
//            studentsInfo.setId(cursor.getInt(cursor.getColumnIndexOrThrow(InfoMateData.StudentsInfo._ID)));
//            studentsInfo.setName(cursor.getString(cursor.getColumnIndexOrThrow(InfoMateData.StudentsInfo.NAME)));
//            studentsInfo.setAge(cursor.getInt(cursor.getColumnIndexOrThrow(InfoMateData.StudentsInfo.AGE)));
//            studentsInfo.setSex(cursor.getString(cursor.getColumnIndexOrThrow(InfoMateData.StudentsInfo.SEX)));
//        }
//        cursor.close();
//        database.close();
//        return studentsInfo;
//    }
}
