package com.example.yourstory.jxd1500.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.FrameLayout;

import com.example.yourstory.jxd1500.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lenovo on 2019/4/3.
 */

public class ScreenDialog extends Dialog {


    @BindView(R.id.screen)
    FrameLayout screen;

    public ScreenDialog(@NonNull Context context) {
        super(context);
    }

    public ScreenDialog(@NonNull Context context, int themeResId, OnCancelListener cancelListener) {
        super(context, themeResId);
        setOnCancelListener(cancelListener);
    }

    public ScreenDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_dialog);
        ButterKnife.bind(this);

    }


    @OnClick(R.id.screen)
    public void onViewClicked() {
        cancel();
    }
}
