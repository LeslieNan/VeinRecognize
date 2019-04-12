package com.example.yourstory.jxd1500.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yourstory.jxd1500.MainActivity;
import com.example.yourstory.jxd1500.R;
import com.example.yourstory.jxd1500.activity.AdvancedSettingActivity;
import com.example.yourstory.jxd1500.activity.RecognizeModeActivity;
import com.example.yourstory.jxd1500.activity.DataExportActivity;
import com.example.yourstory.jxd1500.activity.MainMenuActivity;
import com.example.yourstory.jxd1500.activity.PswSetActivity;
import com.example.yourstory.jxd1500.activity.RegisterActivity;
import com.example.yourstory.jxd1500.activity.ServerSetActivity;
import com.example.yourstory.jxd1500.activity.NetSetActivity;
import com.example.yourstory.jxd1500.activity.SystemInformationActivity;
import com.example.yourstory.jxd1500.activity.TimeSettingActivity;
import com.example.yourstory.jxd1500.activity.UserManageActivity;
import com.example.yourstory.jxd1500.activity.WgActivity;
import com.example.yourstory.jxd1500.activity.WorkingModeActivity;
import com.example.yourstory.jxd1500.bean.MenuBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MQ on 2016/11/11.
 */

public class GridViewAdapter extends BaseAdapter {
    private List<MenuBean> dataList;
    private Activity activity;
    private int page;

    public GridViewAdapter(Activity activity, List<MenuBean> datas, int page) {
        dataList = new ArrayList<>();
        this.activity = activity;
        this.page = page;
        //start end分别代表要显示的数组在总数据List中的开始和结束位置
        int start = page * MainMenuActivity.item_grid_num;
        int end = start + MainMenuActivity.item_grid_num;
        while ((start < datas.size()) && (start < end)) {
            dataList.add(datas.get(start));
            start++;
        }
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int i) {
        return dataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View itemView, ViewGroup viewGroup) {
        ViewHolder mHolder;
        if (itemView == null) {
            mHolder = new ViewHolder();
            itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_gridview_menu, viewGroup, false);
            mHolder.iv_img = (ImageView) itemView.findViewById(R.id.iv_img);
            mHolder.tv_text = (TextView) itemView.findViewById(R.id.tv_text);
            itemView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) itemView.getTag();
        }
        MenuBean bean = dataList.get(i);
        if (bean != null) {
            mHolder.iv_img.setImageResource(bean.img);
            mHolder.tv_text.setText(bean.name);
            //图片点击事件
            mHolder.iv_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startMenuActivity(i);
                }
            });
        }
        return itemView;
    }

    /**
     * 根据position启动menu的服务
     *
     * @param menuPosition
     */
    private void startMenuActivity(int menuPosition) {
        Intent intent = null;
        menuPosition += page * 12;
        switch (menuPosition) {
            case 0:
                intent = new Intent(activity, MainActivity.class);
                break;
            case 1:
                intent = new Intent(activity, RegisterActivity.class);
                break;
            case 2:
                intent = new Intent(activity, UserManageActivity.class);
                break;
            case 3:
                intent = new Intent(activity, WorkingModeActivity.class);
                break;
            case 4:
                intent = new Intent(activity, RecognizeModeActivity.class);
                break;
            case 5:
                intent = new Intent(activity, PswSetActivity.class);
                break;
            case 6:
                intent = new Intent(activity, ServerSetActivity.class);
                break;
            case 7:
                intent = new Intent(activity, TimeSettingActivity.class);
                break;
            case 8:
                intent = new Intent(activity, WgActivity.class);
                break;
            case 9:
                intent = new Intent(activity, DataExportActivity.class);
                break;
            case 10:
                intent = new Intent(activity, MainActivity.class);
                break;
            case 11:
                intent = new Intent(activity, NetSetActivity.class);
                break;
            case 12:
                intent = new Intent(activity, SystemInformationActivity.class);
                break;
            case 13:
                intent = new Intent(activity, AdvancedSettingActivity.class);
                break;

        }
        activity.startActivity(intent);
    }

    private class ViewHolder {
        private ImageView iv_img;
        private TextView tv_text;
    }
}
