package com.example.yourstory.jxd1500.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by lenovo on 2019/4/2.
 *
 */

public class SignDao {

    private SignDBHelper dbHelper;
    private SQLiteDatabase db;


    public SignDao(Context c) {
        dbHelper = SignDBHelper.getInstance(c);
        db = dbHelper.getWritableDatabase();
    }

    public void addSign(String id, String time, int type) {
        String addSql = "insert into Sign(userid,time,type)" +
                "values(?,?,?)";
        db.execSQL(addSql, new String[]{id, time, String.valueOf(type)});
    }

    public Cursor selectAllSign() {
        String selectSql = "select * " +
                "from Sign ";
        return db.rawQuery(selectSql, new String[]{});
    }


}
