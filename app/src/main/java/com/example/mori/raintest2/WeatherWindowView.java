package com.example.mori.raintest2;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by mori on 2016/08/29.
 */
public class WeatherWindowView extends LinearLayout {
    public WeatherWindowView(Context context, AttributeSet attr) {
        super(context);

        View view = LayoutInflater.from(context).inflate(R.layout.weather_window,this);
    }
}
