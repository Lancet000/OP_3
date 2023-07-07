package com.OP.weatherforecast.data;

import java.util.ArrayList;
import java.util.List;

// TODO:对应JSON数据，挑有用的内容就行

public class Weather {

    private String city;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    private int status;
    private String cityCode;
    private String shidu;
    private String wendu;
    private double pm25;
    private String kongqi;
    private String upDate;

    private String type;
    private String fl;

    public String getFl() {
        return fl;
    }

    public void setFl(String fl) {
        this.fl = fl;
    }

    public List<WeatherFor> forecast;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Weather(){
        forecast = new ArrayList<>();
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getShidu() {
        return shidu;
    }

    public void setShidu(String shidu) {
        this.shidu = shidu;
    }

    public String getWendu() {
        return wendu;
    }

    public void setWendu(String wendu) {
        this.wendu = wendu;
    }

    public double getPm25() {
        return pm25;
    }

    public void setPm25(double pm25) {
        this.pm25 = pm25;
    }

    public String getKongqi() {
        return kongqi;
    }

    public void setKongqi(String kongqi) {
        this.kongqi = kongqi;
    }

    public String getUpDate() {
        return upDate;
    }

    public void setUpDate(String upDate) {
        this.upDate = upDate;
    }
}
