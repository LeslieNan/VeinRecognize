package com.example.yourstory.jxd1500.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lenovo on 2019/4/12.
 *
 */

public class ScreenImgDBHelper extends SQLiteOpenHelper {

    private static final String CREATE_IMGURL = "create table ImgUrl (\n" +
            "imgurl text)";

    private static ScreenImgDBHelper mHelper=null;

    public static synchronized ScreenImgDBHelper getInstance(Context context){
        if (mHelper == null) {
            mHelper = new ScreenImgDBHelper(context);
        }
        return mHelper;
    }

    private ScreenImgDBHelper(Context context) {
        super(context, CREATE_IMGURL, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_IMGURL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
