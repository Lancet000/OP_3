package com.OP.weatherforecast;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.OP.weatherforecast.data.Weather;
import com.OP.weatherforecast.data.WeatherFor;
import com.OP.weatherforecast.databinding.ActivityWeatherBinding;
import com.OP.weatherforecast.util.HttpUtil;
import com.OP.weatherforecast.util.Utility;

import java.io.IOException;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {
    private ActivityWeatherBinding binding;
    private String cityCode;
    private int time = 60 * 60 * 1000;

    private final static String TAG = WeatherActivity.class.getSimpleName();
    private final static int SELECT_CITY_CODE = 0;
    private final static int SELECT_LOVE_CODE = 1;
    private SharedPreferences weatherCache;
    private SharedPreferences.Editor weatherCacheEditor;

    String cityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weatherCache = getSharedPreferences("weather", Activity.MODE_PRIVATE);
        weatherCacheEditor = weatherCache.edit();
        binding = ActivityWeatherBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        cityCode = weatherCache.getString("nowcity", "");
        // TODO:请求数据
        if (cityCode.equals("")) {
            requestWeather("101010700", true);
        } else {
            requestWeather(cityCode, true);
        }

        binding.title.navShijian.setOnClickListener(v -> {
            try {
                requestWeather(cityCode, false);
            } catch (Exception e) {
                Toast.makeText(this, "哎呀，刷新失败了，再试一次吧~", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });

        binding.title.navButton.setOnClickListener(view ->
        {
            Intent intent = new Intent(this, MainActivity.class);
            startActivityForResult(intent, SELECT_CITY_CODE);
        });

        binding.forecast.entRight.setOnClickListener(view -> {
            Intent intent = new Intent(this, LoveActivity.class);
            startActivityForResult(intent, SELECT_LOVE_CODE);
        });

        binding.forecast.entLove.setOnClickListener(view -> {
            SharedPreferences love = getSharedPreferences("love", Activity.MODE_PRIVATE);
            SharedPreferences.Editor loveeditor = love.edit();
            if (!love.getString(cityCode, "").equals("")) {
                loveeditor.remove(cityCode);
                loveeditor.commit();
                Toast.makeText(this, binding.title.titleCity.getText().toString() + "已经取消收藏啦！", Toast.LENGTH_SHORT).show();
                return;
            }
            loveeditor.putString(cityCode, cityName);
            loveeditor.commit();
            Toast.makeText(this, "已经把" + binding.title.titleCity.getText().toString() + "添加进收藏啦！", Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * 根据天气id请求城市天气信息。
     */
    public void requestWeather(String cityCode, boolean flag) {
        if (flag) {
            // TODO:先判断缓存里有没有，没有再往后使用HTTP请求
            String cache = weatherCache.getString(cityCode, null);
            if (cache != null) {
                String[] data = cache.split("%");
                // 有缓存且缓存没有过期时直接解析天气数据
                if (new Date().getTime() - Long.parseLong(data[0]) < time) { // 判断缓存是否过期
                    Weather weather = Utility.handleWeatherResponse(data[1]);
                    if (weather.getStatus() != 200) {
                        weatherCacheEditor.remove(cityCode);
                        weatherCacheEditor.commit();
                    } else {
                        Toast.makeText(WeatherActivity.this, "获取天气信息成功", Toast.LENGTH_SHORT).show();
                        showWeatherInfo(weather);
                        return;
                    }
                }
            }
        }
        // 无缓存时去服务器查询天气
        // TODO:生成获取天气API的URL
        String weatherUrl = "http://t.weather.sojson.com/api/weather/city/" + cityCode;
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                assert response.body() != null;
                String responseText = response.body().string();// 将缓存时间和数据拼接在一起
                Weather weather = Utility.handleWeatherResponse(responseText);
                responseText = new Date().getTime() + "%" + responseText;

                String finalResponseText = responseText;
                runOnUiThread(() -> {
                    if (weather.getStatus() == 200) {
                        // 将JSON数据保存在缓存中
                        weatherCacheEditor.putString(cityCode, finalResponseText);
                        weatherCacheEditor.commit();
                        showWeatherInfo(weather);
                        Toast.makeText(WeatherActivity.this, "获取天气信息成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    /**
     * 处理并展示Weather实体类中的数据。
     */

    // TODO:适配API的信息
    @SuppressLint("SetTextI18n")
    private void showWeatherInfo(Weather weather) {
        cityName = weather.getCity();
        String updateTime = weather.getUpDate();
        String degree = weather.getWendu();
        String weatherInfo = weather.getType();
        binding.title.titleCity.setText(cityName);
        binding.title.titleUpdateTime.setText(updateTime);
        binding.now.degreeText.setText(degree + "℃");
        binding.now.weatherInfoText.setText(weatherInfo);
        binding.forecast.forecastLayout.removeAllViews();
        int k = 0;  //判断循环次数
        for (WeatherFor forecast : weather.forecast) {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, binding.forecast.forecastLayout, false);
            TextView dateText = (TextView) view.findViewById(R.id.date_text);
            TextView infoText = (TextView) view.findViewById(R.id.info_text);
            TextView maxText = (TextView) view.findViewById(R.id.max_text);
            TextView minText = (TextView) view.findViewById(R.id.min_text);
            if (k == 0) {
                dateText.setText(forecast.getDate() + "  今天");
            } else if (k == 1) {
                dateText.setText(forecast.getDate() + "  明天");
            } else
                dateText.setText(forecast.getDate());
            infoText.setText(forecast.getType());

            maxText.setText(forecast.getHigh());
            minText.setText(forecast.getLow());
            binding.forecast.forecastLayout.addView(view);

            k++;
        }

        binding.aqi.flText.setText(weather.getFl());
        binding.aqi.shiduText.setText(weather.getShidu());
        binding.aqi.kongqiTxt.setText(weather.getKongqi());
        binding.aqi.pm25Txt.setText(String.format(Locale.getDefault(), "%.1f", weather.getPm25()));


        if (weatherInfo.equals("晴")) {
            binding.now.changeimage.setImageResource(R.drawable.sun);
        } else if (weatherInfo.equals("多云")) {
            binding.now.changeimage.setImageResource(R.drawable.duoyun);
        } else if (weatherInfo.equals("阴")) {
            binding.now.changeimage.setImageResource(R.drawable.yin);
        } else if (weatherInfo.equals("雨")) {
            binding.now.changeimage.setImageResource(R.drawable.zhongyu);
        } else if (weatherInfo.equals("小雨")) {
            binding.now.changeimage.setImageResource(R.drawable.xiaoyu);
        } else if (weatherInfo.equals("中雨")) {
            binding.now.changeimage.setImageResource(R.drawable.zhongyu);
        } else if (weatherInfo.equals("阵雨")) {
            binding.now.changeimage.setImageResource(R.drawable.zhenyu);
        } else if (weatherInfo.equals("雷阵雨")) {
            binding.now.changeimage.setImageResource(R.drawable.leizhenyu);
        } else if (weatherInfo.equals("大雨") | weatherInfo.equals("大雨")) {
            binding.now.changeimage.setImageResource(R.drawable.dayu);
        } else if (weatherInfo.equals("雪")) {
            binding.now.changeimage.setImageResource(R.drawable.xue);
        } else if (weatherInfo.substring(weatherInfo.length() - 1).equals("雪")) {
            binding.now.changeimage.setImageResource(R.drawable.xue);
        } else {
            binding.now.changeimage.setImageResource(R.drawable.tianqi);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SELECT_CITY_CODE:
                    assert data != null;
                    cityCode = data.getStringExtra("citycode");
                    weatherCacheEditor.putString("nowcity", cityCode);
                    weatherCacheEditor.commit();
                    requestWeather(cityCode, true);
                    break;
                case SELECT_LOVE_CODE:
                    assert data != null;
                    cityCode = data.getStringExtra("lovecity");
                    weatherCacheEditor.putString("nowcity", cityCode);
                    weatherCacheEditor.commit();
                    requestWeather(cityCode, true);
                    break;
            }
        }
    }
}
