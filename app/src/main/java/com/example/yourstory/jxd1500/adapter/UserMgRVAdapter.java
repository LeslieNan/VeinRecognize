package com.example.yourstory.jxd1500.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yourstory.jxd1500.R;
import com.example.yourstory.jxd1500.bean.SelectedItemBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2019/3/27.
 */

public class UserMgRVAdapter extends RecyclerView.Adapter<UserMgRVAdapter.ViewHolder> {

    private Context mContext;
    private Cursor mCursor;

    private List<ViewHolder> viewHolderList = new ArrayList<>();


    public UserMgRVAdapter(Context context, Cursor cursor) {
        this.mContext = context;
        this.mCursor = cursor;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_recycler_usermg,
                parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        viewHolderList.add(holder);
        if (mCursor.moveToNext()) {
            holder.id.setText(mCursor.getString(mCursor.getColumnIndex("id")));
            holder.username.setText(mCursor.getString(mCursor.getColumnIndex("name")));
            holder.group.setText(mCursor.getString(mCursor.getColumnIndex("groups")));
        }
        holder.select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.select.isSelected()) {
                    holder.select.setSelected(false);
                } else {
                    holder.select.setSelected(true);
                }
            }
        });
    }


    public List<SelectedItemBean> getSelectedItem() {
        List<SelectedItemBean> selectedItemList = new ArrayList<>();
        for (int i = 0; i < viewHolderList.size(); i++) {
            if (viewHolderList.get(i).select.isSelected()) {
                selectedItemList.add(new SelectedItemBean(viewHolderList.get(i).id.getText().toString(),
                        viewHolderList.get(i).group.getText().toString()));
            }
        }
        return selectedItemList;
    }


    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView id;
        TextView username;
        public TextView group;
        public ImageView select;

        public ViewHolder(View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.tv_item_id);
            username = itemView.findViewById(R.id.tv_item_username);
            group = itemView.findViewById(R.id.tv_item_group);
            select = itemView.findViewById(R.id.iv_item_selected);
        }
    }


    public void refreshList(Cursor newCursor) {
        this.viewHolderList = new ArrayList<>();
        this.mCursor = newCursor;
        this.notifyDataSetChanged();
    }
}
