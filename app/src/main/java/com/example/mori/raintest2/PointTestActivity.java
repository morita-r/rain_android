package com.example.mori.raintest2;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by mori on 2016/10/14.
 */
public class PointTestActivity extends Activity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,View.OnClickListener, com.google.android.gms.location.LocationListener{
    TextView text_before_lati;
    TextView text_before_longi;
    TextView text_now_lati;
    TextView text_now_longi;
    TextView text_before_rain;
    TextView text_now_rain;
    Button button_get;
    final int INTERVAL = 10;
    final int FASTEST_INTERVAL = 5;

    double latitude;
    double longitude;
    double b_latitude;
    double b_longitude;
    float rain;
    float b_rain;

    private GoogleApiClient mGoogleApiClient;
    private boolean mResolvingError = false;

    private Context mActivity;

    private FusedLocationProviderApi fusedLocationProviderApi;

    private LocationRequest locationRequest;
    private Location location;
    private long lastLocationTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mActivity = this;

        text_before_lati = (TextView) findViewById(R.id.text_before_lati);
        text_before_longi = (TextView) findViewById(R.id.text_before_longi);
        text_now_lati = (TextView) findViewById(R.id.text_now_lati);
        text_now_longi = (TextView) findViewById(R.id.text_now_longi);
        text_before_rain = (TextView) findViewById(R.id.rain_before);
        text_now_rain = (TextView) findViewById(R.id.rain_now);
        button_get = (Button)findViewById(R.id.button_get);
        button_get.setOnClickListener(this);


        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);


        fusedLocationProviderApi = LocationServices.FusedLocationApi;

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        //テスト OK.
        float result[] = new float[1];
        Location.distanceBetween(36.095784,140.097459,36.085784,140.397459,result);
        Log.d("distance",String.valueOf(result[0]));


    }

    private void startFusedLocation(){
        if (!mResolvingError)// Connect the client.
            mGoogleApiClient.connect();
    }

    private void stopFusedLocation(){
        mGoogleApiClient.disconnect();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
        stopFusedLocation();
    }

    public int get_point(double b_lat, double b_lon, double lat, double lon, float b_rain, float rain){
        int point=0;
        //距離取得
        float result[] = new float[1];
        Location.distanceBetween(b_lat,b_lon,lat,lon,result);
        Log.d("distance",String.valueOf(result[0]));
        point = (int) (result[0]*(b_rain+rain));



        return point;
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location currentLocation = fusedLocationProviderApi.getLastLocation(mGoogleApiClient);

        if (currentLocation != null && currentLocation.getTime() > 20000) {
            location = currentLocation;
            text_now_lati.setText((String.valueOf(currentLocation.getLatitude())));
            text_now_longi.setText((String.valueOf(currentLocation.getLongitude())));
            latitude = currentLocation.getLatitude();
            longitude = currentLocation.getLongitude();

            HttpTask task1 = new HttpTask(
                    this,"GET","4bcb8214ded99c452448cd64523bf70c",10,latitude,longitude,
                    new HttpTask.AsyncTaskCallback(){
                        @Override
                        public void postExecute(String result) {
//                        getData(SAPPORO);
                            //現在の雨量をプリファレンスに保存しておいて、ここで拾ってくる
                            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mActivity);
                            rain = sp.getFloat("POINT_RAIN",0);
                            Log.d("http","雨量現在値取得");
                        }
                    });
            task1.execute();


        } else {
            // バックグラウンドから戻ってしまうと例外が発生する場合がある
            try {
                //
                fusedLocationProviderApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
                // Schedule a Thread to unregister location listeners
                Executors.newScheduledThreadPool(1).schedule(new Runnable() {
                    @Override
                    public void run() {
                        fusedLocationProviderApi.removeLocationUpdates(mGoogleApiClient, PointTestActivity.this);
                    }
                }, 60000, TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                e.printStackTrace();
                //MainActivityに戻す
                finish();
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("location","更新");
        text_before_lati = text_now_lati;
        text_before_longi = text_now_longi;
        text_before_rain = text_now_rain;
        b_latitude = latitude;
        b_longitude = longitude;


        text_now_lati.setText((String.valueOf(location.getLatitude())));
        text_now_longi.setText((String.valueOf(location.getLongitude())));
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        HttpTask task1 = new HttpTask(
                this,"GET","4bcb8214ded99c452448cd64523bf70c",10,latitude,longitude,
                new HttpTask.AsyncTaskCallback(){
                    @Override
                    public void postExecute(String result) {
//                        getData(SAPPORO);
                        //現在の雨量をプリファレンスに保存しておいて、ここで拾ってくる
                        b_rain = rain;
                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mActivity);
                        rain = sp.getFloat("POINT_RAIN",0);
                        Log.d("http","雨量更新完了");
                        get_point(b_latitude,b_longitude,latitude,longitude,b_rain,rain);
                    }
                });
        task1.execute();


    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
/*        if (mResolvingError) {
            // Already attempting to resolve an error.
            return;
        } else {
            mResolvingError = true;
        }
        */
        if(!mResolvingError)
            mResolvingError = true;

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.button_get){
            startFusedLocation();
        }

    }
}
