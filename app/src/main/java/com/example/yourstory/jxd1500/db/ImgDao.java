package com.example.yourstory.jxd1500.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

/**
 * Created by lenovo on 2019/4/12.
 */

public class ImgDao {

    private ScreenImgDBHelper dbHelper;
    private SQLiteDatabase db;


    public ImgDao(Context context) {
        dbHelper = ScreenImgDBHelper.getInstance(context);
        db = dbHelper.getWritableDatabase();
    }

    public void insertUrl(List<String> urls) {
        for (String url : urls) {
            String insertUrl = "insert into ImgUrl(imgurl) " +
                    "values(?)";
            db.execSQL(insertUrl, new String[]{url});
        }
    }

}
