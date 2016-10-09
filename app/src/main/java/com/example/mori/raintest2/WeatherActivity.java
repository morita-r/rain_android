package com.example.mori.raintest2;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.FloatProperty;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class WeatherActivity extends FragmentActivity implements OnMapReadyCallback,View.OnClickListener, LocationListener {

    private DrawerLayout mDrawer;
    LocationManager mLocationManager;
    private double latitude;
    private double longitude;
    private GoogleMap mMap;
    ArrayList<String> weather = new ArrayList<>();
    ArrayList<Float> temp = new ArrayList<>();
    ArrayList<Float> rain = new ArrayList<>();
    ArrayList<String> dt = new ArrayList<>();
    Context context;
    ImageView image_rain;
    TextView text_city;

    final static int MYPOS = 0;
    final static int SAPPORO = 1;
    final static int SENDAI = 2;
    final static int SHINJUKU = 3;
    final static int NAGOYA = 4;
    final static int OSAKA = 5;
    final static int HIROSHIMA = 6;
    final static int FUKUOKA = 7;
    final static int URAZIO = 8;
    final static int PUSAN = 9;


    final static boolean demo = true;//デモ時、位置情報の取得をカット
//    final static boolean demo = false;//ズルしない

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        context = this;

        image_rain= (ImageView)findViewById(R.id.image_rain_chan);
//        image_rain.setVisibility(View.INVISIBLE);
        image_rain.setBackgroundResource(R.drawable.rain_anim);
        AnimationDrawable anim = (AnimationDrawable)image_rain.getBackground();
        anim.start();

        ImageButton main = (ImageButton) findViewById(R.id.button_main2);
        main.setOnClickListener(this);
        ImageButton log = (ImageButton) findViewById(R.id.button_log2);
        log.setOnClickListener(this);
        ImageButton find = (ImageButton) findViewById(R.id.button_find2);
        find.setOnClickListener(this);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if(demo){//デモ用
//            demo_method();
        }else{//真面目なやつ
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            criteria.setPowerRequirement(Criteria.POWER_LOW);
            String provider = mLocationManager.getBestProvider(criteria, true);
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
            mLocationManager.requestLocationUpdates(provider, 60000, 0, this);
        }


    }

    private void demo_method() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        latitude = Double.parseDouble(sp.getString("demo_latitude","36.108528"));
        longitude = Double.parseDouble(sp.getString("demo_longitude","140.0997056"));
        Log.d("pos","latitude:" + latitude + ", longitude:" + longitude);

        final LatLng mypos = new LatLng(latitude,longitude);
        LatLng sapporo = new LatLng(43.06417,141.34694);
        LatLng sendai = new LatLng(38.26889,140.87194);
        LatLng sinjuku = new LatLng(35.68944,139.69167);
        LatLng nagoya = new LatLng(35.18028,136.90667);
        LatLng osaka = new LatLng(34.68639,135.52);
        LatLng hiroshima = new LatLng(34.39639,132.45944);
        LatLng fukuoka = new LatLng(33.60639,130.41806);
        LatLng urazio = new LatLng(43.172869,132.013510);
        LatLng pusan = new LatLng(35.159879,129.055995);


//        LatLng map_center = new LatLng(36.14168,137.58191);//長野県松本市

//        CameraUpdate cUpdate = CameraUpdateFactory.newLatLngZoom(map_center, ((float) 5.7));


//        options.title("現在地");
//        mMap.moveCamera(cUpdate);
        MarkerOptions options = new MarkerOptions();
        MarkerOptions o_sapporo = new MarkerOptions();
        MarkerOptions o_sendai = new MarkerOptions();
        MarkerOptions o_sinjuku = new MarkerOptions();
        MarkerOptions o_nagoya = new MarkerOptions();
        MarkerOptions o_osaka = new MarkerOptions();
        MarkerOptions o_hiroshima = new MarkerOptions();
        MarkerOptions o_fukuoka = new MarkerOptions();
        MarkerOptions o_urazio = new MarkerOptions();
        MarkerOptions o_pusan = new MarkerOptions();



        o_sapporo.icon(BitmapDescriptorFactory.defaultMarker(70));
        o_sendai.icon(BitmapDescriptorFactory.defaultMarker(70));
        o_sinjuku.icon(BitmapDescriptorFactory.defaultMarker(70));
        o_nagoya.icon(BitmapDescriptorFactory.defaultMarker(70));
        o_osaka.icon(BitmapDescriptorFactory.defaultMarker(70));
        o_hiroshima.icon(BitmapDescriptorFactory.defaultMarker(70));
        o_fukuoka.icon(BitmapDescriptorFactory.defaultMarker(70));
        o_urazio.icon(BitmapDescriptorFactory.defaultMarker(70));
        o_pusan.icon(BitmapDescriptorFactory.defaultMarker(70));

        options.position(mypos);
        o_sapporo.position(sapporo);
        o_sendai.position(sendai);
        o_sinjuku.position(sinjuku);
        o_nagoya.position(nagoya);
        o_osaka.position(osaka);
        o_hiroshima.position(hiroshima);
        o_fukuoka.position(fukuoka);
        o_urazio.position(urazio);
        o_pusan.position(pusan);

        mMap.addMarker(options);
        mMap.addMarker(o_sapporo);
        mMap.addMarker(o_sendai);
        mMap.addMarker(o_sinjuku);
        mMap.addMarker(o_nagoya);
        mMap.addMarker(o_osaka);
        mMap.addMarker(o_hiroshima);
        mMap.addMarker(o_fukuoka);
        mMap.addMarker(o_urazio);
        mMap.addMarker(o_pusan);

        HttpTask task = new HttpTask(
                this,"GET","4bcb8214ded99c452448cd64523bf70c",MYPOS,latitude,longitude,
                new HttpTask.AsyncTaskCallback(){
                    @Override
                    public void postExecute(String result) {
                        getData(MYPOS);
                        image_rain.setVisibility(View.VISIBLE);
                        text_city = (TextView)findViewById(R.id.text_city);
                        text_city.setText("現在地（つくば市）の天気");
                        Log.d("http","現在地finish");
                    }
                });
        task.execute();
        HttpTask task1 = new HttpTask(
                this,"GET","4bcb8214ded99c452448cd64523bf70c",SAPPORO,sapporo.latitude,sapporo.longitude,
                new HttpTask.AsyncTaskCallback(){
                    @Override
                    public void postExecute(String result) {
//                        getData(SAPPORO);
                        Log.d("http","札幌finish");
                    }
                });
        task1.execute();
        HttpTask task2 = new HttpTask(
                this,"GET","4bcb8214ded99c452448cd64523bf70c",SENDAI,sendai.latitude,sendai.longitude,
                new HttpTask.AsyncTaskCallback(){
                    @Override
                    public void postExecute(String result) {
//                        getData(SENDAI);
                        Log.d("http","仙台finish");
                    }
                });
        task2.execute();
        HttpTask task3 = new HttpTask(
                this,"GET","4bcb8214ded99c452448cd64523bf70c",SHINJUKU,sinjuku.latitude,sinjuku.longitude,
                new HttpTask.AsyncTaskCallback(){
                    @Override
                    public void postExecute(String result) {
//                        getData(SHINJUKU);
                        Log.d("http","新宿finish");
                    }
                });
        task3.execute();
        HttpTask task4 = new HttpTask(
                this,"GET","4bcb8214ded99c452448cd64523bf70c",NAGOYA,nagoya.latitude,nagoya.longitude,
                new HttpTask.AsyncTaskCallback(){
                    @Override
                    public void postExecute(String result) {
//                        getData(NAGOYA);
                        Log.d("http","名古屋finish");
                    }
                });
        task4.execute();
        HttpTask task5 = new HttpTask(
                this,"GET","4bcb8214ded99c452448cd64523bf70c",OSAKA,osaka.latitude,osaka.longitude,
                new HttpTask.AsyncTaskCallback(){
                    @Override
                    public void postExecute(String result) {
//                        getData(OSAKA);
                        Log.d("http","大阪finish");
                    }
                });
        task5.execute();
        HttpTask task6 = new HttpTask(
                this,"GET","4bcb8214ded99c452448cd64523bf70c",HIROSHIMA,hiroshima.latitude,hiroshima.longitude,
                new HttpTask.AsyncTaskCallback(){
                    @Override
                    public void postExecute(String result) {
//                        getData(HIROSHIMA);
                        Log.d("http","広島finish");
                    }
                });
        task6.execute();
        HttpTask task7 = new HttpTask(
                this,"GET","4bcb8214ded99c452448cd64523bf70c",FUKUOKA,fukuoka.latitude,fukuoka.longitude,
                new HttpTask.AsyncTaskCallback(){
                    @Override
                    public void postExecute(String result) {
//                        getData(FUKUOKA);
                        Log.d("http","福岡finish");
                    }
                });
        task7.execute();
        HttpTask task8 = new HttpTask(
                this,"GET","4bcb8214ded99c452448cd64523bf70c",URAZIO,urazio.latitude,urazio.longitude,
                new HttpTask.AsyncTaskCallback(){
                    @Override
                    public void postExecute(String result) {
//                        getData(FUKUOKA);
                        Log.d("http","福岡finish");
                    }
                });
        task8.execute();
        HttpTask task9 = new HttpTask(
                this,"GET","4bcb8214ded99c452448cd64523bf70c",PUSAN,pusan.latitude,pusan.longitude,
                new HttpTask.AsyncTaskCallback(){
                    @Override
                    public void postExecute(String result) {
//                        getData(FUKUOKA);
                        Log.d("http","福岡finish");
                    }
                });
        task9.execute();


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(marker.getId().equals("m0")){//現在地
                    text_city.setText("現在地（つくば市）の天気");
                    getData(MYPOS);
                    Log.d("touch","現在地");
                }else if(marker.getId().equals("m1")){//札幌
                    text_city.setText("札幌の天気");
                    getData(SAPPORO);
                    Log.d("touch","札幌");
                }else if(marker.getId().equals("m2")){//仙台
                    text_city.setText("仙台の天気");
                    getData(SENDAI);
                    Log.d("touch","仙台");
                }else if(marker.getId().equals("m3")){//新宿
                    text_city.setText("新宿の天気");
                    getData(SHINJUKU);
                    Log.d("touch","現在地");
                }else if(marker.getId().equals("m4")){//名古屋
                    text_city.setText("名古屋の天気");
                    getData(NAGOYA);
                    Log.d("touch","現在地");
                }else if(marker.getId().equals("m5")){//大阪
                    text_city.setText("大阪の天気");
                    getData(OSAKA);
                    Log.d("touch","現在地");
                }else if(marker.getId().equals("m6")){//広島
                    text_city.setText("広島の天気");
                    getData(HIROSHIMA);
                    Log.d("touch","現在地");
                }else if(marker.getId().equals("m7")){//福岡
                    text_city.setText("福岡の天気");
                    getData(FUKUOKA);
                    Log.d("touch","現在地");
                }else if(marker.getId().equals("m8")){//福岡
                    text_city.setText("ウラジオストクの天気");
                    getData(URAZIO);
                    Log.d("touch","現在地");
                }else if(marker.getId().equals("m9")){//福岡
                    text_city.setText("부산の天気");
                    getData(PUSAN);
                    Log.d("touch","現在地");
                }

                return false;
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
    }

    //drawer内のボタン（メニュー）管理
        @Override
        public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
           case R.id.button_log2:
                intent = new Intent(this, LogActivity.class);
                startActivity(intent);
                mDrawer.closeDrawers();
               finish();
                break;
            case R.id.button_find2:
                intent = new Intent(this, FindActivity.class);
                startActivity(intent);
                mDrawer.closeDrawers();
                finish();
                break;
            case R.id.button_main2:
                intent = new Intent(this, MainActivity.class);
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


    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        Log.d("pos","latitude:" + latitude + ", longitude:" + longitude);

        final LatLng mypos = new LatLng(latitude,longitude);
        LatLng sapporo = new LatLng(43.06417,141.34694);
        LatLng sendai = new LatLng(38.26889,140.87194);
        LatLng sinjuku = new LatLng(35.68944,139.69167);
        LatLng nagoya = new LatLng(35.18028,136.90667);
        LatLng osaka = new LatLng(34.68639,135.52);
        LatLng hiroshima = new LatLng(34.39639,132.45944);
        LatLng fukuoka = new LatLng(33.60639,130.41806);

//        LatLng map_center = new LatLng(36.14168,137.58191);//長野県松本市

//        CameraUpdate cUpdate = CameraUpdateFactory.newLatLngZoom(map_center, ((float) 5.7));


//        options.title("現在地");
//        mMap.moveCamera(cUpdate);
        MarkerOptions options = new MarkerOptions();
        MarkerOptions o_sapporo = new MarkerOptions();
        MarkerOptions o_sendai = new MarkerOptions();
        MarkerOptions o_sinjuku = new MarkerOptions();
        MarkerOptions o_nagoya = new MarkerOptions();
        MarkerOptions o_osaka = new MarkerOptions();
        MarkerOptions o_hiroshima = new MarkerOptions();
        MarkerOptions o_fukuoka = new MarkerOptions();



        o_sapporo.icon(BitmapDescriptorFactory.defaultMarker(70));
        o_sendai.icon(BitmapDescriptorFactory.defaultMarker(70));
        o_sinjuku.icon(BitmapDescriptorFactory.defaultMarker(70));
        o_nagoya.icon(BitmapDescriptorFactory.defaultMarker(70));
        o_osaka.icon(BitmapDescriptorFactory.defaultMarker(70));
        o_hiroshima.icon(BitmapDescriptorFactory.defaultMarker(70));
        o_fukuoka.icon(BitmapDescriptorFactory.defaultMarker(70));

        options.position(mypos);
        o_sapporo.position(sapporo);
        o_sendai.position(sendai);
        o_sinjuku.position(sinjuku);
        o_nagoya.position(nagoya);
        o_osaka.position(osaka);
        o_hiroshima.position(hiroshima);
        o_fukuoka.position(fukuoka);

        mMap.addMarker(options);
        mMap.addMarker(o_sapporo);
        mMap.addMarker(o_sendai);
        mMap.addMarker(o_sinjuku);
        mMap.addMarker(o_nagoya);
        mMap.addMarker(o_osaka);
        mMap.addMarker(o_hiroshima);
        mMap.addMarker(o_fukuoka);

        HttpTask task = new HttpTask(
                this,"GET","4bcb8214ded99c452448cd64523bf70c",MYPOS,latitude,longitude,
                new HttpTask.AsyncTaskCallback(){
                    @Override
                    public void postExecute(String result) {
                        getData(MYPOS);
                        image_rain.setVisibility(View.VISIBLE);
                        text_city = (TextView)findViewById(R.id.text_city);
                        text_city.setText("現在地（つくば市）の天気");
                        Log.d("http","現在地finish");
                    }
                });
        task.execute();
        HttpTask task1 = new HttpTask(
                this,"GET","4bcb8214ded99c452448cd64523bf70c",SAPPORO,sapporo.latitude,sapporo.longitude,
                new HttpTask.AsyncTaskCallback(){
                    @Override
                    public void postExecute(String result) {
//                        getData(SAPPORO);
                        Log.d("http","札幌finish");
                    }
                });
        task1.execute();
        HttpTask task2 = new HttpTask(
                this,"GET","4bcb8214ded99c452448cd64523bf70c",SENDAI,sendai.latitude,sendai.longitude,
                new HttpTask.AsyncTaskCallback(){
                    @Override
                    public void postExecute(String result) {
//                        getData(SENDAI);
                        Log.d("http","仙台finish");
                    }
                });
        task2.execute();
        HttpTask task3 = new HttpTask(
                this,"GET","4bcb8214ded99c452448cd64523bf70c",SHINJUKU,sinjuku.latitude,sinjuku.longitude,
                new HttpTask.AsyncTaskCallback(){
                    @Override
                    public void postExecute(String result) {
//                        getData(SHINJUKU);
                        Log.d("http","新宿finish");
                    }
                });
        task3.execute();
        HttpTask task4 = new HttpTask(
                this,"GET","4bcb8214ded99c452448cd64523bf70c",NAGOYA,nagoya.latitude,nagoya.longitude,
                new HttpTask.AsyncTaskCallback(){
                    @Override
                    public void postExecute(String result) {
//                        getData(NAGOYA);
                        Log.d("http","名古屋finish");
                    }
                });
        task4.execute();
        HttpTask task5 = new HttpTask(
                this,"GET","4bcb8214ded99c452448cd64523bf70c",OSAKA,osaka.latitude,osaka.longitude,
                new HttpTask.AsyncTaskCallback(){
                    @Override
                    public void postExecute(String result) {
//                        getData(OSAKA);
                        Log.d("http","大阪finish");
                    }
                });
        task5.execute();
        HttpTask task6 = new HttpTask(
                this,"GET","4bcb8214ded99c452448cd64523bf70c",HIROSHIMA,hiroshima.latitude,hiroshima.longitude,
                new HttpTask.AsyncTaskCallback(){
                    @Override
                    public void postExecute(String result) {
//                        getData(HIROSHIMA);
                        Log.d("http","広島finish");
                    }
                });
        task6.execute();
        HttpTask task7 = new HttpTask(
                this,"GET","4bcb8214ded99c452448cd64523bf70c",FUKUOKA,fukuoka.latitude,fukuoka.longitude,
                new HttpTask.AsyncTaskCallback(){
                    @Override
                    public void postExecute(String result) {
//                        getData(FUKUOKA);
                        Log.d("http","福岡finish");
                    }
                });
        task7.execute();


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(marker.getId().equals("m0")){//現在地
                    text_city.setText("現在地（つくば市）の天気");
                    getData(MYPOS);
                    Log.d("touch","現在地");
                }else if(marker.getId().equals("m1")){//札幌
                    text_city.setText("札幌の天気");
                    getData(SAPPORO);
                    Log.d("touch","札幌");
                }else if(marker.getId().equals("m2")){//仙台
                    text_city.setText("仙台の天気");
                    getData(SENDAI);
                    Log.d("touch","仙台");
                }else if(marker.getId().equals("m3")){//新宿
                    text_city.setText("新宿の天気");
                    getData(SHINJUKU);
                    Log.d("touch","現在地");
                }else if(marker.getId().equals("m4")){//名古屋
                    text_city.setText("名古屋の天気");
                    getData(NAGOYA);
                    Log.d("touch","現在地");
                }else if(marker.getId().equals("m5")){//大阪
                    text_city.setText("大阪の天気");
                    getData(OSAKA);
                    Log.d("touch","現在地");
                }else if(marker.getId().equals("m6")){//広島
                    text_city.setText("広島の天気");
                    getData(HIROSHIMA);
                    Log.d("touch","現在地");
                }else if(marker.getId().equals("m7")){//福岡
                    text_city.setText("福岡の天気");
                    getData(FUKUOKA);
                    Log.d("touch","現在地");
                }

                return false;
            }
        });

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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng map_center = new LatLng(36.69528,137.58191);//長野県松本市

//        LatLng map_center = new LatLng(36.14168,137.58191);//長野県松本市
        CameraUpdate cUpdate = CameraUpdateFactory.newLatLngZoom(map_center, ((float) 5.7));
        mMap.moveCamera(cUpdate);
        if(demo)
            demo_method();
    }

    public void getData(int id){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        String city_name;
        switch (id){
            case MYPOS:
                city_name = "MYPOS_";
                break;
            case SAPPORO:
                city_name= "SAPPORO_";
                break;
            case SENDAI:
                city_name= "SENDAI_";
                break;
            case SHINJUKU:
                city_name= "SHINJUKU_";
                break;
            case NAGOYA:
                city_name= "NAGOYA_";
                break;
            case OSAKA:
                city_name= "OSAKA_";
                break;
            case HIROSHIMA:
                city_name= "HIROSHIMA_";
                break;
            case PUSAN:
                city_name= "PUSAN_";
                break;
            case URAZIO:
                city_name= "URAZIO_";
                break;
            default:
                city_name= "FUKUOKA_";
                break;
        }
        Log.d("id",""+id);

        int i;
        for(i=0;i<8;i++) {
            weather.add(sp.getString(city_name+"weather"+Integer.toString(i),null));//天気
            temp.add(sp.getFloat(city_name+"temp"+Integer.toString(i),0));//気温
            rain.add(sp.getFloat(city_name+"rain"+Integer.toString(i),0));//雨量
            Date date = new Date(sp.getLong(city_name+"dt"+Integer.toString(i),0)*1000);
            SimpleDateFormat sdf = new SimpleDateFormat("kk:mm");
            sdf.format(date);
            Log.d("dt",sdf.format(date));
            dt.add(sdf.format(date));//時間
        }
        Log.d("temp0",""+temp.get(0));

        getLayoutInflater().inflate(R.layout.weather_window, null);
/*        ArrayAdapter<String> weather_adapter = new ArrayAdapter<String>(context,R.layout.weather_window);
        for(i=0;i<8;i++) {
            weather_adapter.add(weather.get(i));
        }
        */
//        text_city = (TextView)findViewById(R.id.text_city);
//        text_city.setText("現在地（つくば市)の天気");

        //ここからコピペ
        List<WeatherListItem> list1 = new ArrayList<WeatherListItem>();

        WeatherListItem item1 = new WeatherListItem();
        item1.setData(0,dt.get(0),weather.get(0),temp.get(0).toString()+"℃",rain.get(0).toString()+"mm");
        list1.add(item1);

        WeatherArrayAdapter adapter1 = new WeatherArrayAdapter(this,R.layout.weather_window_list,list1);
        ListView listView1 = (ListView)findViewById(R.id.listview1);
        listView1.setAdapter(adapter1);

        List<WeatherListItem> list2 = new ArrayList<WeatherListItem>();
        WeatherListItem item2 = new WeatherListItem();
        item2.setData(1,dt.get(1),weather.get(1),temp.get(1).toString()+"℃",rain.get(1).toString()+"mm");
        list2.add(item2);
        WeatherArrayAdapter adapter2 = new WeatherArrayAdapter(this,R.layout.weather_window_list,list2);
        ListView listView2 = (ListView)findViewById(R.id.listview2);
        listView2.setAdapter(adapter2);

        List<WeatherListItem> list3 = new ArrayList<WeatherListItem>();
        WeatherListItem item3 = new WeatherListItem();
        item3.setData(2,dt.get(2),weather.get(2),temp.get(2).toString()+"℃",rain.get(2).toString()+"mm");
        list3.add(item3);
        WeatherArrayAdapter adapter3 = new WeatherArrayAdapter(this,R.layout.weather_window_list,list3);
        ListView listView3 = (ListView)findViewById(R.id.listview3);
        listView3.setAdapter(adapter3);

        List<WeatherListItem> list4 = new ArrayList<WeatherListItem>();
        WeatherListItem item4 = new WeatherListItem();
        item4.setData(3,dt.get(3),weather.get(3),temp.get(3).toString()+"℃",rain.get(3).toString()+"mm");
        list4.add(item4);
        WeatherArrayAdapter adapter4 = new WeatherArrayAdapter(this,R.layout.weather_window_list,list4);
        ListView listView4 = (ListView)findViewById(R.id.listview4);
        listView4.setAdapter(adapter4);

        List<WeatherListItem> list5 = new ArrayList<WeatherListItem>();
        WeatherListItem item5 = new WeatherListItem();
        item5.setData(4,dt.get(4),weather.get(4),temp.get(4).toString()+"℃",rain.get(4).toString()+"mm");
        list5.add(item5);
        WeatherArrayAdapter adapter5 = new WeatherArrayAdapter(this,R.layout.weather_window_list,list5);
        ListView listView5 = (ListView)findViewById(R.id.listview5);
        listView5.setAdapter(adapter5);

        List<WeatherListItem> list6 = new ArrayList<WeatherListItem>();
        WeatherListItem item6 = new WeatherListItem();
        item6.setData(5,dt.get(5),weather.get(5),temp.get(5).toString()+"℃",rain.get(5).toString()+"mm");
        list6.add(item6);
        WeatherArrayAdapter adapter6 = new WeatherArrayAdapter(this,R.layout.weather_window_list,list6);
        ListView listView6 = (ListView)findViewById(R.id.listview6);
        listView6.setAdapter(adapter6);

        List<WeatherListItem> list7 = new ArrayList<WeatherListItem>();
        WeatherListItem item7 = new WeatherListItem();
        item7.setData(6,dt.get(6),weather.get(6),temp.get(6).toString()+"℃",rain.get(6).toString()+"mm");
        list7.add(item7);
        WeatherArrayAdapter adapter7 = new WeatherArrayAdapter(this,R.layout.weather_window_list,list7);
        ListView listView7 = (ListView)findViewById(R.id.listview7);
        listView7.setAdapter(adapter7);

        List<WeatherListItem> list8 = new ArrayList<WeatherListItem>();
        WeatherListItem item8 = new WeatherListItem();
        item8.setData(7,dt.get(7),weather.get(7),temp.get(7).toString()+"℃",rain.get(7).toString()+"mm");
        list8.add(item8);
        WeatherArrayAdapter adapter8 = new WeatherArrayAdapter(this,R.layout.weather_window_list,list8);
        ListView listView8 = (ListView)findViewById(R.id.listview8);
        listView8.setAdapter(adapter8);

        weather.clear();
        temp.clear();
        rain.clear();
        dt.clear();

    }
}