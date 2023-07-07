package com.OP.weatherforecast;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.OP.weatherforecast.data.City;
import com.OP.weatherforecast.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private Map<String, City> cityMap;
    private List<City> cities;
    private final List<City> showCity = new LinkedList<>();
    private ArrayAdapter<City> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        cityMap = City.getData(this);
        cities = new ArrayList<>(cityMap.values());
        showCity.addAll(cityMap.values().stream().filter((city -> city.getPid() == 0)).collect(Collectors.toList()));
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, showCity);
        Toast.makeText(this, "长按选择城市", Toast.LENGTH_SHORT).show();
        binding.listView.setAdapter(adapter);
        binding.searchcom.setOnClickListener(view -> onClickSearch());
        binding.listView.setOnItemClickListener((adapterView, view, i, l) -> {
            City city = showCity.get(i);
            String citycode;
            if(city.sons.size() != 1){
                showCity.clear();
                if(city.getCity_code().equals("")){
                    citycode = city.getId() + "";
                }else{
                    citycode = city.getCity_code();
                }
                showCity.addAll(Objects.requireNonNull(cityMap.get(citycode)).sons);
                adapter.notifyDataSetChanged();
            }else{
                Intent intent = new Intent(this, WeatherActivity.class);
                intent.putExtra("citycode", city.getCity_code());
                Toast.makeText(this, "切换城市为" + city.getCity_name(), Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        binding.listView.setOnItemLongClickListener((adapterView, view, i, l) -> {
            City city = showCity.get(i);
            if(city.getCity_code().equals("")){
                showCity.clear();
                showCity.addAll(Objects.requireNonNull(cityMap.get(city.getId() + "")).sons);
                adapter.notifyDataSetChanged();
                return false;
            }else{
                Intent intent = new Intent(this, WeatherActivity.class);
                intent.putExtra("citycode", city.getCity_code());
                Toast.makeText(this, "切换城市为" + city.getCity_name(), Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK, intent);
                finish();
            }
            return true;
        });
    }


    private void onClickSearch() {
        Intent intent = new Intent();
        String text = binding.search.getText().toString();
        String citycode = text;
        if (text == null || text.equals("")) {
            Toast.makeText(this, "请输入一些内容再查询吧~", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean flag = false;
        if (cityMap.get(text) == null) {
            for (City city : cities) {
                if (city.getCity_name().equals(text)) {
                    if (city.getCity_code().equals("")) {
                        citycode = city.getId() + "";
                    }else{
                        citycode = city.getCity_code();
                    }
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                Toast.makeText(this, "输入了错误的城市名或城市码，请检查后再搜索", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        showCity.clear();
        showCity.addAll(cityMap.get(citycode).sons);
        adapter.notifyDataSetChanged();
    }
}