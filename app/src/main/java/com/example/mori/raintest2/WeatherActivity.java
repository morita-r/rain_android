package com.example.mori.raintest2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


public class WeatherActivity extends Activity implements View.OnClickListener {
private DrawerLayout mDrawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        ImageButton main = (ImageButton) findViewById(R.id.button_main2);
        main.setOnClickListener(this);
        ImageButton log = (ImageButton) findViewById(R.id.button_log2);
        log.setOnClickListener(this);
        ImageButton find = (ImageButton) findViewById(R.id.button_find2);
        find.setOnClickListener(this);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);


    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
           case R.id.button_log2:
                intent = new Intent(this, LogActivity.class);
                startActivity(intent);
                mDrawer.closeDrawers();
                break;
            case R.id.button_find2:
                intent = new Intent(this, FindActivity.class);
                startActivity(intent);
                mDrawer.closeDrawers();
                break;
            case R.id.button_main2:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                mDrawer.closeDrawers();
                break;
        }
    }
}