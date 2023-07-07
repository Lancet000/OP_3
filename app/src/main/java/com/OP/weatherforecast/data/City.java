package com.OP.weatherforecast.data;

import android.content.Context;

import androidx.annotation.NonNull;

import com.OP.weatherforecast.util.Utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class City {
    private static volatile Map<String, City> data;

    public static Map<String, City> getData(Context context) {
        if (data == null) {
            try {
                InputStream inputStream = context.getAssets().open("city.json");
                StringBuilder stringBuilder = new StringBuilder();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                bufferedReader.close();
                inputStream.close();
                data = Utility.handleCityResponse(stringBuilder.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    private int id;
    private int pid;
    private String city_code;
    private String city_name;

    public List<City> sons = new LinkedList<>();

    public City() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getCity_code() {
        return city_code;
    }

    public void setCity_code(String city_code) {
        this.city_code = city_code;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    @NonNull
    @Override
    public String toString() {
        return city_name;
    }
}
