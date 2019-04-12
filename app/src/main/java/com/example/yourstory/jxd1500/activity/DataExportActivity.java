package com.example.yourstory.jxd1500.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.yourstory.jxd1500.R;
import com.example.yourstory.jxd1500.bean.SignBean;
import com.example.yourstory.jxd1500.bean.UserBean;
import com.example.yourstory.jxd1500.db.SignDao;
import com.example.yourstory.jxd1500.db.UserDao;
import com.example.yourstory.jxd1500.util.ExcelUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DataExportActivity extends AppCompatActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.btn_dataexport_usedata)
    Button btnDataexportUsedata;
    @BindView(R.id.btn_dataexport_signdata)
    Button btnDataexportSigndata;
    private List<UserBean> userBeans = new ArrayList<>();
    private List<SignBean> signBeans = new ArrayList<>();
    private String[] title = {"id", "username", "group", "password", "fingerCount"};
    private String[] signTitle = {"userid", "time", "type"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_export);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.iv_back, R.id.btn_dataexport_usedata, R.id.btn_dataexport_signdata})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_dataexport_usedata:
                UserDao userDao = new UserDao(this);
                Cursor cursor = userDao.selectAllUser();
                if (cursor.moveToFirst()) {
                    do {
                        userBeans.add(new UserBean(cursor.getString(cursor.getColumnIndex("id")),
                                cursor.getString(cursor.getColumnIndex("name")),
                                cursor.getString(cursor.getColumnIndex("groups")),
                                cursor.getString(cursor.getColumnIndex("password")),
                                cursor.getString(cursor.getColumnIndex("fingerCount"))));
                    } while (cursor.moveToNext());
                } else {
                    Toast.makeText(this, "数据为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                exportUserExcel();
                break;
            case R.id.btn_dataexport_signdata:
                SignDao signDao = new SignDao(this);
                Cursor signCursor = signDao.selectAllSign();
                if (signCursor.moveToFirst()) {
                    do {
                        signBeans.add(new SignBean(signCursor.getString(signCursor.getColumnIndex("userid")),
                                signCursor.getString(signCursor.getColumnIndex("time")),
                                signCursor.getString(signCursor.getColumnIndex("type"))));
                    } while (signCursor.moveToNext());
                } else {
                    Toast.makeText(this, "数据为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                exportSignExcel();
                break;
        }
    }


    /**
     * 导出用户表excel
     */
    public void exportUserExcel() {
        File file = new File(getSDPath() + "/jxFile");
        makeDir(file);
        ExcelUtils.initExcel(file.toString() + "/用户表.xls", title);
        String fileName = getSDPath() + "/jxFile/用户表.xls";
        ExcelUtils.writeObjListToExcel(getUserData(), fileName, this);
    }

    /**
     * 将数据集合 转化成ArrayList<ArrayList<String>>
     *
     * @return
     */
    private ArrayList<ArrayList<String>> getUserData() {
        ArrayList<ArrayList<String>> userList = new ArrayList<>();
        for (int i = 0; i < userBeans.size(); i++) {
            UserBean user = userBeans.get(i);
            ArrayList<String> beanList = new ArrayList<String>();
            beanList.add(user.id);
            beanList.add(user.name);
            beanList.add(user.group);
            beanList.add(user.password);
            userList.add(beanList);
        }
        return userList;
    }


    /**
     * 导出考勤表excel
     */
    public void exportSignExcel() {
        File file = new File(getSDPath() + "/jxFile");
        makeDir(file);
        ExcelUtils.initExcel(file.toString() + "/考勤表.xls", signTitle);
        String fileName = getSDPath() + "/jxFile/考勤表.xls";
        ExcelUtils.writeObjListToExcel(getSignData(), fileName, this);
    }

    private ArrayList<ArrayList<String>> getSignData() {
        ArrayList<ArrayList<String>> signList = new ArrayList<>();
        for (int i = 0; i < signBeans.size(); i++) {
            SignBean user = signBeans.get(i);
            ArrayList<String> beanList = new ArrayList<String>();
            beanList.add(user.id);
            beanList.add(user.time);
            beanList.add(user.type);
            signList.add(beanList);
        }
        return signList;
    }


    private String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();
        }
        String dir = sdDir.toString();
        return dir;
    }

    public void makeDir(File dir) {
        if (!dir.getParentFile().exists()) {
            makeDir(dir.getParentFile());
        }
        dir.mkdir();
    }
}
