package com.OP.weatherforecast.util;

import android.util.Log;

import androidx.annotation.NonNull;


import com.OP.weatherforecast.data.City;
import com.OP.weatherforecast.data.Weather;
import com.OP.weatherforecast.data.WeatherFor;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Utility {

    /**
     * 将返回的JSON数据解析成Weather实体类
     */
    @NonNull
    public static Weather handleWeatherResponse(String response) {
        Weather weather = new Weather();
        try {
            JSONObject jsonObject = new JSONObject(response);
            // TODO: 解析JSON
            weather.setCity(jsonObject.getJSONObject("cityInfo").getString("city"));
            weather.setStatus(jsonObject.getInt("status"));
            weather.setShidu(jsonObject.getJSONObject("data").getString("shidu"));
            weather.setWendu(jsonObject.getJSONObject("data").getString("wendu"));
            JSONArray forecast = jsonObject.getJSONObject("data").getJSONArray("forecast");
            weather.setType(forecast.getJSONObject(0).getString("type"));
            weather.setPm25(jsonObject.getJSONObject("data").getDouble("pm25"));
            weather.setFl(forecast.getJSONObject(0).getString("fl"));
            weather.setKongqi(jsonObject.getJSONObject("data").getString("quality"));
            weather.setUpDate(jsonObject.getJSONObject("cityInfo").getString("updateTime"));
            for (int i = 0; i < 7; i++) {
                JSONObject jsonObject1 = forecast.getJSONObject(i);
                WeatherFor weather1 = new WeatherFor();
                weather1.setDate(jsonObject1.getString("week"));
                weather1.setType(jsonObject1.getString("type"));
                weather1.setHigh(jsonObject1.getString("high").substring(3));
                weather1.setLow(jsonObject1.getString("low").substring(3));
                weather.forecast.add(weather1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return weather;
    }

    @NonNull
    public static Map<String, City> handleCityResponse(String response) {
        Map<String, City> cityMap = new HashMap<>();
        Map<Integer, String> tmp = new HashMap<>();
        try {
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                City city = new City();
                city.setCity_code(jsonObject.getString("city_code"));
                city.setCity_name(jsonObject.getString("city_name"));
                city.setId(jsonObject.getInt("id"));
                city.setPid(jsonObject.getInt("pid"));
                if(city.getCity_code().equals("")){
                    tmp.put(city.getId(), city.getId() + "");
                    cityMap.put(city.getId() + "", city);
                }else{
                    tmp.put(city.getId(), city.getCity_code());
                    cityMap.put(city.getCity_code(), city);
                    city.sons.add(city);
                }
            }

            for (City city : cityMap.values()) {
                if (city.getPid() != 0) {
//                    Log.d("TAG", "handleCityResponse:"+city);
                    City citytmp = cityMap.get(tmp.get(city.getPid()));
                    assert citytmp != null;
                    citytmp.sons.add(city);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return cityMap;
    }
}
