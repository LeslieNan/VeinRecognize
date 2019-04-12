package com.example.yourstory.jxd1500.dialog;

import android.app.Activity;
import android.util.AttributeSet;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.yourstory.jxd1500.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by lenovo on 2019/3/23.
 */

public class InputVeinWindow extends PopupWindow {

    @BindView(R.id.tv_dialog_text)
    TextView tvDialogText;
    @BindView(R.id.btn_dialog_cancle)
    Button btnDialogCancle;
    private Activity mActivity;

    private View view;


    public InputVeinWindow(Activity context, AttributeSet attrs) {
        super(context, attrs);
        this.mActivity = context;
        this.view = LayoutInflater.from(mActivity).inflate(R.layout.input_finger_dialog, null);
   /*
   * 获取圣诞框的窗口对象及参数对象以修改对话框的布局设置, 可以直接调用getWindow(),表示获得这个Activity的Window
   * 对象,这样这可以以同样的方式改变这个Activity的属性.
   */
        Window dialogWindow = mActivity.getWindow();

        WindowManager m = mActivity.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值

        this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        this.setWidth((int) (d.getWidth() * 0.8));

        // 设置弹出窗体可点击
        this.setFocusable(true);
    }


    @OnClick(R.id.btn_dialog_cancle)
    public void onViewClicked() {

    }
}
