package com.example.yourstory.jxd1500.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lenovo on 2019/4/2.
 */

public class SignDBHelper extends SQLiteOpenHelper {


    private static final String CREATE_SIGN = "create table Sign (\n" +
            "userid integer,\n" +
            "time text,\n" +
            "type integer)";

    private static SignDBHelper mHelper = null;


    private SignDBHelper(Context context) {
        super(context, CREATE_SIGN, null, 1);
    }


    /**
     * 单例模式
     *
     * @param context
     * @return
     */
    public static synchronized SignDBHelper getInstance(Context context) {
        if (mHelper == null) {
            mHelper = new SignDBHelper(context);
        }
        return mHelper;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_SIGN);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
