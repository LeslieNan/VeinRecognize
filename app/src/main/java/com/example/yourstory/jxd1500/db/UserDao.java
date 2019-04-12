package com.example.yourstory.jxd1500.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.yourstory.jxd1500.bean.UserBean;

import java.util.List;

/**
 * Created by lenovo on 2019/3/23.
 */

public class UserDao {

    private UserDBHelper dbHelper;
    private SQLiteDatabase db;


    public UserDao(Context c) {
        dbHelper = UserDBHelper.getInstance(c);
        db = dbHelper.getWritableDatabase();
    }

    public void addUser(String name, String group, String password) {
        String addSql = "insert into User(name,groups,password)" +
                "values(?,?,?)";
        db.execSQL(addSql, new String[]{name, group, password});
    }

    public void addUserWithID(String id, String name, String group, String password, int count) {
        String addSql = "insert into User(id,name,groups,password,fingerCount)" +
                "values(?,?,?,?,?)";
        db.execSQL(addSql, new String[]{id, name, group, password, String.valueOf(count)});
    }

    public void insertAllUser(List<UserBean> userBeans) {
        //开启事务
        db.beginTransaction();
        for (int i = 0; i < userBeans.size(); i++) {
            String insertSql = "insert into User " +
                    "(id,name,groups,password,fingerCount) " +
                    "values(?,?,?,?,?)";
            UserBean user = userBeans.get(i);
            db.execSQL(insertSql, new String[]{user.id, user.name, user.group, user.password, user.fingerCount});
        }
        //设置事务成功，结束事务
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public int selectCountUser() {
        String selectSql = "select count(id) as count " +
                "from User ";
        Cursor cursor = db.rawQuery(selectSql, null);
        cursor.moveToFirst();
        return cursor.getInt(cursor.getColumnIndex("count"));
    }

    /**
     * 检查数据库中是否有id
     *
     * @param id
     * @return
     */
    public boolean recheckByID(String id) {
        String selectSql = "select id " +
                "from User " +
                "where id=?";
        Cursor cursor = db.rawQuery(selectSql, new String[]{id});
        if (cursor.moveToFirst()) {
            return true;
        } else {
            return false;
        }
    }

    public String selectNameByID(String id) {
        String selectSql = "select name " +
                "from User " +
                "where id=?";
        Cursor cursor = db.rawQuery(selectSql, new String[]{id});
        while (cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex("name"));
        }
        return "";
    }

    public Cursor selectByID(String id) {
        String selectSql = "select id,name,groups " +
                "from User " +
                "where id=?";
        Cursor cursor = db.rawQuery(selectSql, new String[]{id});
        if (cursor.moveToFirst()) {
            return cursor;
        }
        return null;
    }

    public Cursor selectAllInfoByID(String id) {
        String selectSql = "select * " +
                "from User " +
                "where id=?";
        Cursor cursor = db.rawQuery(selectSql, new String[]{id});
        if (cursor.moveToFirst()) {
            return cursor;
        }
        return null;
    }

    public String checkIDAndPsw(String id, String psw) {
        String selectSql = "select name " +
                "from User " +
                "where id=? and password=?";
        Cursor cursor = db.rawQuery(selectSql, new String[]{id, psw});
        if (cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex("name"));
        } else {
            return null;
        }
    }

    public Cursor selectAllUser() {
        String selectSql = "select * " +
                "from User ";
        return db.rawQuery(selectSql, new String[]{});
    }

    public void deleteUserByID(List<String> ids) {
        StringBuilder deleteSql = new StringBuilder("delete from User " +
                "where id in (");
        for (int i = 0; i < ids.size(); i++) {
            deleteSql.append(ids.get(i) + ",");
        }
        deleteSql.deleteCharAt(deleteSql.length() - 1);
        deleteSql.append(")");
        db.execSQL(String.valueOf(deleteSql));
    }

    public void updateUserInfo(String id, String name, String group, String psw, int fingerCount) {
        String alterSql = "update User set name=?,groups=?,password=?,fingerCount=? " +
                "where id=?";
        db.execSQL(alterSql, new String[]{name, group, psw, String.valueOf(fingerCount), id});
    }


}
