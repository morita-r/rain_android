package com.example.mori.raintest2;

import android.app.LauncherActivity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by mori on 2016/08/29.
 */
public class WeatherArrayAdapter extends ArrayAdapter<WeatherListItem> {
    private int resourceId;
    private List<WeatherListItem> items;
    private LayoutInflater inflater;
    final static int CLEAR = 0;
    final static int CLOUDS = 1;
    final static int RAIN = 2;
    final static int SNOW = 3;

    public WeatherArrayAdapter(Context context, int resource , List<WeatherListItem> items) {
        super(context, resource,items);
        this.resourceId = resource;
        this.items = items;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView != null) {
            view = convertView;
        } else {
            view = this.inflater.inflate(this.resourceId, null);
        }

        WeatherListItem item = this.items.get(position);

        // 時間をセット
        TextView text_dt = (TextView)view.findViewById(R.id.text_dt);
        text_dt.setText(item.getDt());

        ImageView image_weather = (ImageView)view.findViewById(R.id.image_weather);
        switch(item.getWeather_image_id()){
            case CLEAR:
                image_weather.setImageResource(R.drawable.d1);
                break;
            case CLOUDS:
                image_weather.setImageResource(R.drawable.d3);
                break;
            case RAIN:
                image_weather.setImageResource(R.drawable.d9);
                break;
            case SNOW:
                image_weather.setImageResource(R.drawable.d13);
                break;
        }
        TextView text_temp = (TextView)view.findViewById(R.id.text_temp);
        text_temp.setText(item.getTemp());

        TextView text_rain = (TextView)view.findViewById(R.id.text_rain);
        text_rain.setText(item.getRain());

        return view;
    }
}
