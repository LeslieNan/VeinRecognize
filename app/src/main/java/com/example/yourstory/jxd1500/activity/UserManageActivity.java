package com.example.yourstory.jxd1500.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yourstory.jxd1500.R;
import com.example.yourstory.jxd1500.adapter.UserMgRVAdapter;
import com.example.yourstory.jxd1500.bean.SelectedItemBean;
import com.example.yourstory.jxd1500.db.UserDao;
import com.example.yourstory.jxd1500.db.VeinDBDao;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserManageActivity extends AppCompatActivity {


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.et_user_search)
    EditText etUserSearch;
    @BindView(R.id.btn_user_delete)
    Button btnUserDelete;
    @BindView(R.id.btn_user_alter)
    Button btnUserAlter;
    @BindView(R.id.rv_user)
    RecyclerView rvUser;
    @BindView(R.id.tv_usermg_count)
    TextView tvUsermgCount;

    private UserMgRVAdapter userMgRVAdapter;
    private UserDao userDao;
    private VeinDBDao veinDBDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_manage);
        ButterKnife.bind(this);
        initData();
        initEditText();
    }

    private void initData() {
        //recycler
        userDao = new UserDao(this);
        veinDBDao = new VeinDBDao();
        Cursor cursor = userDao.selectAllUser();
        userMgRVAdapter = new UserMgRVAdapter(this, cursor);
        rvUser.setAdapter(userMgRVAdapter);
        rvUser.setLayoutManager(new LinearLayoutManager(this));
        //统计
        tvUsermgCount.setText("用户数：" + userDao.selectCountUser()
                + "人    指静脉数：" + veinDBDao.selectCountVein());
    }

    @Override
    protected void onStart() {
        super.onStart();
        refreshAdapter();
    }

    private void refreshAdapter() {
        //刷新listview的adapter
        userMgRVAdapter.refreshList(userDao.selectAllUser());
        //刷新统计数
        tvUsermgCount.setText("用户数：" + userDao.selectCountUser()
                + "人    指静脉数：" + veinDBDao.selectCountVein());
    }


    /**
     * EditText的监听事件
     */
    private void initEditText() {
        etUserSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            //点击输入法确认键后执行
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                String searchID = etUserSearch.getText().toString();
                if (searchID.equals("")) {
                    //显示所有用户
                    userMgRVAdapter.refreshList(userDao.selectAllUser());
                    return false;
                }
                Cursor cursor = userDao.selectByID(searchID);
                if (cursor != null) {
                    userMgRVAdapter.refreshList(cursor);
                } else {
                    Toast.makeText(UserManageActivity.this, "该id不存在", Toast.LENGTH_SHORT).show();
                }
                //返回true表示点击确认后不删除text
                return true;
            }
        });

    }

    @OnClick({R.id.iv_back, R.id.btn_user_delete, R.id.btn_user_alter})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_user_delete:
                List<SelectedItemBean> selectedDeleteItemList = userMgRVAdapter.getSelectedItem();
                //若没有选定则直接消费此次点击
                if (selectedDeleteItemList.size() == 0) {
                    return;
                }
                List<String> selectedIDList = new ArrayList<>();
                for (int i = 0; i < selectedDeleteItemList.size(); i++) {
                    selectedIDList.add(selectedDeleteItemList.get(i).userid);
                }
                //从设备创建的数据库中删除
                for (int i = 0; i < selectedDeleteItemList.size(); i++) {
                    veinDBDao.deleteThreeVeinByID(selectedDeleteItemList.get(i).userid, selectedDeleteItemList.get(i).group);
                }
                //从数据库中删除
                userDao.deleteUserByID(selectedIDList);
                refreshAdapter();
                break;
            case R.id.btn_user_alter:
                List<SelectedItemBean> selectedAlterItemList = userMgRVAdapter.getSelectedItem();
                if (selectedAlterItemList.size() == 1) {
                    Intent intent = new Intent(this, AlterUserActivity.class);
                    intent.putExtra("selectedID", selectedAlterItemList.get(0).userid);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "请选择一条用户", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
