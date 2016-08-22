package com.example.mori.raintest2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.Button;

/**
 * Created by mori on 2016/08/21.
 */
public class MapActivity extends Activity implements View.OnClickListener {
    private DrawerLayout mDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Button main = (Button) findViewById(R.id.button_main);
        main.setOnClickListener(this);
        Button log = (Button)findViewById(R.id.button_log);
        log.setOnClickListener(this);
        Button find = (Button)findViewById(R.id.button_find);
        find.setOnClickListener(this);
        Button weather = (Button)findViewById(R.id.button_weather);
        weather.setOnClickListener(this);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.button_main:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                mDrawer.closeDrawers();
                break;
            case R.id.button_log:
                intent = new Intent(this, LogActivity.class);
                startActivity(intent);
                mDrawer.closeDrawers();
                break;
            case R.id.button_find:
                intent = new Intent(this, FindActivity.class);
                startActivity(intent);
                mDrawer.closeDrawers();
                break;
            case R.id.button_weather:
                intent = new Intent(this, WeatherActivity.class);
                startActivity(intent);
                mDrawer.closeDrawers();
                break;
        }
    }
}