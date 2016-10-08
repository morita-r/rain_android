package com.example.mori.raintest2;

/**
 * Created by mori on 2016/08/29.
 */
public class WeatherListItem {
    int id;
    String dt;
    int weather_image_id;
    String temp;
    String rain;
    final static int CLEAR = 0;
    final static int CLOUDS = 1;
    final static int RAIN = 2;
    final static int SNOW = 3;

    public void setData(int id, String dt, String weather_image_id, String temp, String rain){
        this.id = id;
        this.dt = dt;
        switch (weather_image_id){
            case "Clear":
                this.weather_image_id = CLEAR;
                break;
            case "Clouds":
                this.weather_image_id = CLOUDS;
                break;
            case "Rain":
                this.weather_image_id = RAIN;
                break;
            case "Snow":
                this.weather_image_id = SNOW;
                break;
        }

        this.temp = temp;
        this.rain = rain;
    }
    public int getId(){
        return id;
    }

    public String getDt(){
        return dt;
    }

    public int getWeather_image_id(){
        return weather_image_id;
    }

    public String getTemp(){
        return temp;
    }

    public String getRain(){
        return rain;
    }
}
