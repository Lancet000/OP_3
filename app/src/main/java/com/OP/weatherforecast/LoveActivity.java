package com.OP.weatherforecast;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.OP.weatherforecast.databinding.ActivityLoveBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoveActivity extends AppCompatActivity {
    private ActivityLoveBinding binding;
    private SharedPreferences pref;
    private ArrayAdapter<String> adapter;

    private List<String> showCity = new ArrayList<>();

    private Map<String,String> show = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoveBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        pref = getSharedPreferences("love", MODE_PRIVATE);
        show = (Map<String,String>)pref.getAll();
        showCity = new ArrayList<>(show.values());
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, showCity);
        binding.loveList.setAdapter(adapter);
        binding.loveList.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent();
            String lovecity = "101010700";
            for(String str : show.keySet()){
                if(show.get(str).equals(showCity.get(i))){
                    lovecity = str;
                }
            }
            intent.putExtra("lovecity", lovecity);
            setResult(RESULT_OK,intent);
            Toast.makeText(this, "切换城市为" + showCity.get(i), Toast.LENGTH_SHORT).show();
            finish();
        });
        binding.ButtonBack.setOnClickListener(view -> {
            finish();
        });
    }
}