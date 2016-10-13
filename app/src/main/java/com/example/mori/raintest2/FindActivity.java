package com.example.mori.raintest2;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class FindActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener,View.OnClickListener{
    MapFragment mf;
    private GoogleMap mMap;
    LocationManager locationManager;
    double latitude;
    double longitude;
    private DrawerLayout mDrawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        ImageButton main = (ImageButton) findViewById(R.id.button_main2);
        main.setOnClickListener(this);
        ImageButton log = (ImageButton) findViewById(R.id.button_log2);
        log.setOnClickListener(this);
        ImageButton weather = (ImageButton) findViewById(R.id.button_weather2);
        weather.setOnClickListener(this);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 50, this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        LatLng mypos = new LatLng(latitude,longitude);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);


//        mMap.addMarker(new MarkerOptions().position(mypos).title("現在地"));

        CameraUpdate cUpdate = CameraUpdateFactory.newLatLngZoom(mypos,15);

//        mMap.addMarker(new MarkerOptions().position(mypos).title("現在地"));
        MarkerOptions options = new MarkerOptions();
        options.position(mypos);
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.marker);
        options.icon(icon);

        options.title("現在地");
        options.snippet("れいんちゃんだよー");
        mMap.addMarker(options);
        mMap.moveCamera(cUpdate);


    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    //drawer内のボタン（メニュー）管理
    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.button_main2:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                mDrawer.closeDrawers();
                finish();
                break;
            case R.id.button_log2:
                intent = new Intent(this, LogActivity.class);
                startActivity(intent);
                mDrawer.closeDrawers();
                finish();
                break;
            case R.id.button_weather2:
                intent = new Intent(this, WeatherActivity.class);
                startActivity(intent);
                mDrawer.closeDrawers();
                finish();
                break;
        }
    }

    //戻るボタン無効
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction()==KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_BACK:
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

}
