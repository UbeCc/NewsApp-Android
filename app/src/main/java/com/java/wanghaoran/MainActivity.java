package com.java.wanghaoran;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Logger", "Initialized");
        super.onCreate(savedInstanceState);
        // 启动登录界面
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        // 关闭当前活动
        finish();
    }
}
