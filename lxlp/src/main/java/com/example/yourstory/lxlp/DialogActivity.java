package com.example.yourstory.lxlp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yourstory.lxlp.totp.PasscodeGenerator;

public class DialogActivity extends AppCompatActivity {


    private EditText etNew;
    private EditText etSure;
    private TextView tvSure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        initView();
    }

    private void initView() {
        etNew = findViewById(R.id.et_newkey);
        etSure = findViewById(R.id.et_surekey);
        tvSure = findViewById(R.id.tv_sure);
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeKey();
            }
        });
        setEditText();
    }

    /**
     * 设置密钥框不能输入中文
     */
    private void setEditText(){
        etNew.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    for (int i = 0; i < s.length(); i++) {
                        char c = s.charAt(i);
                        if (c >= 0x4e00 && c <= 0X9fff) { // 根据字节码判断
                            // 如果是中文，则清除输入的字符，否则保留
                            s.delete(i,i+1);
                        }
                    }
                }
            }
        });
    }


    private void changeKey() {
        String newKey = etNew.getText().toString();
        String sureKey = etSure.getText().toString();
        if (!newKey.equals(sureKey)) {
            Toast.makeText(this, "密钥两次输入不同，请重新输入", Toast.LENGTH_SHORT).show();
        } else {
            PasscodeGenerator.setAuthKey(newKey);
            finish();
        }

    }
}
