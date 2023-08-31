package com.java.wanghaoran;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.java.wanghaoran.service.UserSQLiteOpenHelper;

public class LoginActivity extends AppCompatActivity{
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button changePasswdButton;
    private Button registerButton;

    private String username;
    private String password;
    private UserSQLiteOpenHelper databaseHelper;


    VideoView vv;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 找到视图和按钮
        usernameEditText = findViewById(R.id.sign_loginname);
        passwordEditText = findViewById(R.id.sign_editpasswd);
        loginButton = findViewById(R.id.sign_btn_loginsignin);
        changePasswdButton = findViewById(R.id.change_passwd);
        registerButton = findViewById(R.id.sign_btn_signup);

        databaseHelper = new UserSQLiteOpenHelper(this);


        // 设置按钮点击监听器
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = usernameEditText.getText().toString();
                password = passwordEditText.getText().toString();
                if(username == "") {
                    Utils.makeToast(LoginActivity.this, "用户名不能为空");
                    return;
                }
                if(password == "") {
                    Utils.makeToast(LoginActivity.this, "密码不能为空");
                    return;
                }
                int result = databaseHelper.checkLoginCredentials(username, password);
                if (result == 1) {
                    Utils.makeToast(LoginActivity.this, "用户名不存在，请您先注册");
                } else if(result == 2) {
                    Utils.makeToast(LoginActivity.this, "用户名密码不匹配，请您再尝试");
                }
                else if(result == 0) {
                    Utils.makeToast(LoginActivity.this, "登陆成功");
                    Log.d("Logger", "Login: " + username);
                    MainApplication.username = username;
                    MainApplication.password = password;
                    MainApplication.createDB();
                    Intent intent = new Intent(LoginActivity.this, AppActivity.class);
                    startActivity(intent);
                    // 关闭登录界面
                    finish();
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = usernameEditText.getText().toString();
                password = passwordEditText.getText().toString();
                if(databaseHelper.checkLoginCredentials(username, password) != 1) {
                    Utils.makeToast(getApplicationContext(), "用户名已存在，请您登录或修改密码");
                    return;
                }
                Utils.makeToast(getApplicationContext(), "注册成功");
                databaseHelper.addUser(username, password);
            }
        });

        changePasswdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = usernameEditText.getText().toString();
                password = passwordEditText.getText().toString();
                if(databaseHelper.checkLoginCredentials(username, password) == 1) {
                    Utils.makeToast(getApplicationContext(), "用户名未存在，请您先注册");
                    return;
                }
                Utils.makeToast(getApplicationContext(), "修改密码成功");
                databaseHelper.updatePassword(username, password);
            }
        });
    }
}