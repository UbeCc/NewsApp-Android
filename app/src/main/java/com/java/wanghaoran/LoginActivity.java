package com.java.wanghaoran;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 找到视图和按钮
        usernameEditText = findViewById(R.id.sign_name);
        passwordEditText = findViewById(R.id.sign_passwd);
        loginButton = findViewById(R.id.sign_btn_signin);

        // 设置按钮点击监听器
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                Intent intent = new Intent(LoginActivity.this, AppActivity.class);
                startActivity(intent);

                // 关闭登录界面
                finish();
            }
        });
    }
}
