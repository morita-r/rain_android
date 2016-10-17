package com.example.mori.raintest2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by rikuya on 2016/10/13.
 */

public class BaseActivity extends Activity implements View.OnClickListener {

    //BaseView
    View mView;

    DrawerLayout mDrawerLayout;


    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        mView = View.inflate(this, R.layout.base_layout, null);
        mDrawerLayout = (DrawerLayout) mView.findViewById(R.id.base_layout);
        setMenu(MenuType.Default);
    }

    @Override
    public void onClick(View view) {

        Intent intent;
        switch (view.getId()) {
            case R.id.button_main2:
                intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                mDrawerLayout.closeDrawers();
                finish();
                break;
            case R.id.button_log2:
                intent = new Intent(this, LogActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                mDrawerLayout.closeDrawers();
                finish();
                break;
            case R.id.button_find2:
                intent = new Intent(this, FindActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                mDrawerLayout.closeDrawers();
                finish();
                break;
            case R.id.button_weather2:
                intent = new Intent(this, WeatherActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                mDrawerLayout.closeDrawers();
                finish();
                break;
        }

    }

    public void setView(View view) {
        FrameLayout containerView = (FrameLayout) mView.findViewById(R.id.container);
        containerView.addView(view);
        System.out.println("base");
        setContentView(mView);
    }

    private void setMenu(MenuType type) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        LinearLayout menuLayout = (LinearLayout) mView.findViewById(R.id.drawer_menu);
        switch (type) {
            case Default:
                LinearLayout menu = (LinearLayout) inflater.inflate(R.layout.list_menu_default, null);
                menuLayout.addView(menu);
                (menu.findViewById(R.id.button_find2)).setOnClickListener(this);
                (menu.findViewById(R.id.button_log2)).setOnClickListener(this);
                (menu.findViewById(R.id.button_main2)).setOnClickListener(this);
                (menu.findViewById(R.id.button_weather2)).setOnClickListener(this);
                break;
            default:
                break;
        }
    }

    public enum MenuType {
        Default
    }


}
