package com.OP.weatherforecast;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class WelcomeActivity extends AppCompatActivity {
    private boolean lag = true;
    public boolean isFirstRun;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //判断是否第一次使用APP
        SharedPreferences sharedPreferences = this.getSharedPreferences("share", MODE_PRIVATE);
        isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
        editor = sharedPreferences.edit();
        editor.putBoolean("isFirstRun", false);
        editor.commit();

        setContentView(R.layout.activity_welcome);
        new Handler().postDelayed(() -> {
            if (lag) {
                finish();
                Toast.makeText(WelcomeActivity.this, "刷新成功", Toast.LENGTH_LONG).show();
                Intent intent;
                if (isFirstRun) {
                    intent = new Intent(WelcomeActivity.this, MainActivity.class);
                } else {
                    intent = new Intent(WelcomeActivity.this, WeatherActivity.class);
                }
                startActivity(intent);
            }

        }, 3000);

        //给按钮添加监听事件，当点击时，直接进入主页面
        Button button = (Button) findViewById(R.id.button);

        button.setOnClickListener(view -> {
            finish();
            Intent intent;
            if (isFirstRun) {
                intent = new Intent(WelcomeActivity.this, MainActivity.class);
            } else {
                intent = new Intent(WelcomeActivity.this, WeatherActivity.class);
            }
            startActivity(intent);
            lag = false;
        });
    }
}
