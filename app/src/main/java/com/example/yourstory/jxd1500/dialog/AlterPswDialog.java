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
import com.example.yourstory.jxd1500.util.SystemSettingUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lenovo on 2019/4/1.
 */

public class AlterPswDialog extends Dialog {

    @BindView(R.id.et_alterpsw)
    EditText etAlterpsw;
    @BindView(R.id.et_agalterpsw)
    EditText etAgalterpsw;
    @BindView(R.id.btn_alterpsw)
    Button btnAlterpsw;
    private Activity activity;

    public AlterPswDialog(@NonNull Context context) {
        super(context);
        this.activity = (Activity) context;
    }

    public AlterPswDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.activity = (Activity) context;
    }

    protected AlterPswDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.activity = (Activity) context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alter_psw_dialog);
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
        SystemSettingUtil.setPswKey(key);
    }
}
