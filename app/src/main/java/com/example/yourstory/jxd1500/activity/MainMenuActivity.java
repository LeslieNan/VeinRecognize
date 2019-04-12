package com.example.yourstory.jxd1500.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridView;

import com.example.yourstory.jxd1500.R;
import com.example.yourstory.jxd1500.adapter.GridViewAdapter;
import com.example.yourstory.jxd1500.adapter.ViewPagerAdapter;
import com.example.yourstory.jxd1500.bean.MenuBean;
import com.example.yourstory.jxd1500.view.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainMenuActivity extends AppCompatActivity {


    public static int item_grid_num = 12;//每一页中GridView中item的数量
    public static int number_columns = 4;//gridview一行展示的数目
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.indicator)
    CirclePageIndicator indicator;

    private ViewPagerAdapter mAdapter;
    private List<MenuBean> dataList;
    private List<GridView> gridList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        ButterKnife.bind(this);
        initViews();
        initDatas();
    }

    private void initViews() {
        //初始化ViewPager
        mAdapter = new ViewPagerAdapter();
        viewPager.setAdapter(mAdapter);
        dataList = new ArrayList<>();
        //圆点指示器
        indicator.setVisibility(View.VISIBLE);
        indicator.setViewPager(viewPager);
    }

    private void initDatas() {
        if (dataList.size() > 0) {
            dataList.clear();
        }
        if (gridList.size() > 0) {
            gridList.clear();
        }
        //初始化数据
        addMenuBean();
        //计算viewpager一共显示几页
        int pageSize = dataList.size() % item_grid_num == 0
                ? dataList.size() / item_grid_num
                : dataList.size() / item_grid_num + 1;
        for (int i = 0; i < pageSize; i++) {
            GridView gridView = new GridView(this);
            gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));         //取消item的点击效果
            GridViewAdapter adapter = new GridViewAdapter(this, dataList, i);
            gridView.setNumColumns(number_columns);
            gridView.setAdapter(adapter);
            gridList.add(gridView);
        }
        mAdapter.add(gridList);
    }


    private void addMenuBean() {
        MenuBean bean1 = new MenuBean();
        bean1.name = "灵犀终端";
        bean1.img = R.mipmap.menuicon2;
        dataList.add(bean1);
        MenuBean bean2 = new MenuBean();
        bean2.name = "用户注册";
        bean2.img = R.mipmap.menuicon13;
        dataList.add(bean2);
        MenuBean bean3 = new MenuBean();
        bean3.name = "用户管理";
        bean3.img = R.mipmap.menuicon6;
        dataList.add(bean3);
        MenuBean bean4 = new MenuBean();
        bean4.name = "工作模式";
        bean4.img = R.mipmap.menuicon7;
        dataList.add(bean4);
        MenuBean bean5 = new MenuBean();
        bean5.name = "认证设置";
        bean5.img = R.mipmap.menuicon8;
        dataList.add(bean5);
        MenuBean bean6 = new MenuBean();
        bean6.name = "密码设置";
        bean6.img = R.mipmap.menuicon10;
        dataList.add(bean6);
        MenuBean bean7 = new MenuBean();
        bean7.name = "认证服务器";
        bean7.img = R.mipmap.menuicon9;
        dataList.add(bean7);
        MenuBean bean8 = new MenuBean();
        bean8.name = "时间设置";
        bean8.img = R.mipmap.menuicon3;
        dataList.add(bean8);
        MenuBean bean9 = new MenuBean();
        bean9.name = "韦根设置";
        bean9.img = R.mipmap.menuicon11;
        dataList.add(bean9);
        MenuBean bean10 = new MenuBean();
        bean10.name = "数据导出";
        bean10.img = R.mipmap.menuicon4;
        dataList.add(bean10);
        MenuBean bean11 = new MenuBean();
        bean11.name = "系统升级";
        bean11.img = R.mipmap.menuicon12;
        dataList.add(bean11);
        MenuBean bean12 = new MenuBean();
        bean12.name = "网络设置";
        bean12.img = R.mipmap.menuicon1;
        dataList.add(bean12);
        MenuBean bean13 = new MenuBean();
        bean13.name = "系统信息";
        bean13.img = R.mipmap.menuicon14;
        dataList.add(bean13);
        MenuBean bean14 = new MenuBean();
        bean14.name = "高级设置";
        bean14.img = R.mipmap.menuicon15;
        dataList.add(bean14);

    }


}
