package com.example.yourstory.jxd1500.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by lenovo on 2019/3/23.
 *
 */

public class UserDBHelper extends SQLiteOpenHelper {

    private static final String CREATE_USER = "create table User (\n" +
            "id integer primary key autoincrement,\n" +
            "name text,\n" +
            "groups integer,\n" +
            "password text,\n" +
            "fingerCount integer)";

    private static UserDBHelper mHelper = null;

    private UserDBHelper(Context context) {
        super(context, CREATE_USER, null, 1);
    }

    /**
     * 单例模式
     *
     * @param context
     * @return
     */
    public static synchronized UserDBHelper getInstance(Context context) {
        if (mHelper == null) {
            mHelper = new UserDBHelper(context);
        }
        return mHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_USER);
        Log.d("RegisterActivity", "创建成功");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
