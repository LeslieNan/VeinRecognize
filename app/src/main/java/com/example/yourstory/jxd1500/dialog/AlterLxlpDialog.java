package com.example.yourstory.jxd1500.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.yourstory.jxd1500.R;
import com.example.yourstory.jxd1500.totp.PasscodeGenerator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lenovo on 2019/4/1.
 */

public class AlterLxlpDialog extends Dialog {

    @BindView(R.id.et_alterlxlp)
    EditText etAlterpsw;
    @BindView(R.id.btn_alterpsw)
    Button btnAlterpsw;
    private Activity activity;

    public AlterLxlpDialog(@NonNull Context context) {
        super(context);
        this.activity = (Activity) context;
    }

    public AlterLxlpDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.activity = (Activity) context;
    }

    public AlterLxlpDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.activity = (Activity) context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alter_lxlp_dialog);
        ButterKnife.bind(this);
        /*
        * 获取窗口对象及参数对象以修改对话框的布局设置, 可以直接调用getWindow(),表示获得这个Activity的Window
        * 对象,这样这可以以同样的方式改变这个Activity的属性.
        */
        Window dialogWindow = this.getWindow();
        WindowManager m = activity.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        // p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
        // p.width = (int) (d.getWidth() * 0.6); // 宽度设置为屏幕的0.8
        dialogWindow.setAttributes(p);

        setCanceledOnTouchOutside(true);       //设置外部点击消失
    }


    @OnClick(R.id.btn_alterpsw)
    public void onViewClicked() {
        String key = etAlterpsw.getText().toString();
        PasscodeGenerator.setAuthKey(key);
    }
}
