package com.example.mori.raintest2;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by mori on 2016/08/28.
 */
public class HttpTask extends AsyncTask<Void, Void, String> {
    private CallBackTask callbacktask;
    private String type;
    private String mUrl;
    float min_temp;
    float max_temp;
    String weather;
    private int id;
    private Activity mParentActivity;
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
    final static int POINT = 10;//雨量によるポイント取得用


    public interface AsyncTaskCallback {
        void postExecute(String result);
    }

    private AsyncTaskCallback callback = null;


    public HttpTask(Activity parentActivity,String type, String api, int id,double latitude, double longitude, AsyncTaskCallback _callback){
        this.id = id;
        this.callback = _callback;
        mParentActivity = parentActivity;
        this.type = type.toUpperCase();
        if(id != POINT) {
            this.mUrl = "http://api.openweathermap.org/data/2.5/forecast/weather?lat=" + Double.toString(latitude)
                    + "&amp;lon=" + Double.toString(longitude)
                    + "&amp;mode=json&units=metric&amp;cnt=11"
                    + "&APPID=" + api;
        }else{//ポイント取得時は、予報じゃなくて現在の情報を用いるため少し変わる
            this.mUrl = "http://api.openweathermap.org/data/2.5/weather?lat=" + Double.toString(latitude)
                    + "&amp;lon=" + Double.toString(longitude)
                    + "&amp;mode=json&units=metric&APPID=" + api;
        }
    }

    public void setApi(String api)
    {
//        this.api = api;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    @Override
    protected void onPreExecute(){
    }

    @Override
    protected String doInBackground(Void... arg0){
        return exec_http();
    }

    @Override
    protected void onPostExecute(String result){
        super.onPostExecute(result);
        callback.postExecute(result);
    }

    private String exec_http(){
        HttpURLConnection http = null;
        InputStream in = null;
        String src = null;
        try {
            //http connect to URL
  //          URL url = new URL(api);
            URL url = new URL(mUrl);

            http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(type);
            http.setDoInput(true);
            //http.setDoOutput(true);
            http.setRequestProperty("Content-Type","application/json; charset=utf-8");
            http.connect();
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mParentActivity);


            BufferedReader reader = new BufferedReader(new InputStreamReader(http.getInputStream(), "UTF-8"));
            src = reader.readLine();
            JSONObject rootObj = new JSONObject(src);
            if(id == POINT){
                JSONArray weatherArray = rootObj.getJSONArray("weather");
                JSONObject weatherObj = weatherArray.getJSONObject(0);
                String weather = weatherObj.getString("main");//Clear,Clouds,Rain
//                Log.d("http","weather:"+weather);
                String Rain = "Rain";
                if(weather.equals(Rain)) {
                    JSONObject rainObj = rootObj.getJSONObject("rain");
                    float rain = (float) rainObj.getDouble("3h");
                    sp.edit().putFloat("POINT_RAIN", rain).apply();//  ex)rain2,0.64ミリ/3h
                    Log.d("point_rain",Float.toString(rain));
                }else{
                    sp.edit().putFloat("POINT_RAIN", 0).apply();
                    Log.d("point_rain","0");
                }
            }else {
                JSONArray listArray = rootObj.getJSONArray("list");

                int i;
                int object_num;
                String pref_name;
//            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mParentActivity);
                for (i = 0; i < 8; i++) {
                    object_num = i + 3;
                    JSONObject OBJ = listArray.getJSONObject(object_num);
                    long dt = OBJ.getLong("dt");
//                Log.d("http","dt:"+dt);

                    JSONObject OBJ_main = OBJ.getJSONObject("main");
                    float temp = (float) OBJ_main.getDouble("temp");
//                Log.d("http","temp:"+temp);

                    JSONArray weatherArray = OBJ.getJSONArray("weather");
                    JSONObject weatherObj = weatherArray.getJSONObject(0);
                    String weather = weatherObj.getString("main");//Clear,Clouds,Rain
//                Log.d("http","weather:"+weather);
                    String Rain = "Rain";
                    String city_name;
                    switch (id) {
                        case MYPOS:
                            city_name = "MYPOS_";
                            break;
                        case SAPPORO:
                            city_name = "SAPPORO_";
                            break;
                        case SENDAI:
                            city_name = "SENDAI_";
                            break;
                        case SHINJUKU:
                            city_name = "SHINJUKU_";
                            break;
                        case NAGOYA:
                            city_name = "NAGOYA_";
                            break;
                        case OSAKA:
                            city_name = "OSAKA_";
                            break;
                        case HIROSHIMA:
                            city_name = "HIROSHIMA_";
                            break;
                        case PUSAN:
                            city_name = "PUSAN_";
                            break;
                        case URAZIO:
                            city_name = "URAZIO_";
                            break;
                        default:
                            city_name = "FUKUOKA_";
                            break;
                    }


                    if (weather.equals(Rain)) {
                        JSONObject rainObj = OBJ.getJSONObject("rain");
                        float rain = (float) rainObj.getDouble("3h");
                        sp.edit().putLong(city_name + "dt" + Integer.toString(i), dt).apply();//  ex)dt1,1472439600
                        sp.edit().putFloat(city_name + "temp" + Integer.toString(i), temp).apply();//  ex)temp2,24.62
                        sp.edit().putString(city_name + "weather" + Integer.toString(i), weather).apply();//  ex)weather4,Rain
                        sp.edit().putFloat(city_name + "rain" + Integer.toString(i), rain).apply();//  ex)rain2,0.64ミリ/3h
                        Log.d("temp0", "id:" + id + ", temp:" + temp);
//                    Log.d("http","rain:"+rain);
                    } else {//雨は降らない
                        sp.edit().putLong(city_name + "dt" + Integer.toString(i), dt).apply();//  ex)dt1,1472439600
                        sp.edit().putFloat(city_name + "temp" + Integer.toString(i), temp).apply();//  ex)temp2,24.62
                        sp.edit().putString(city_name + "weather" + Integer.toString(i), weather).apply();//  ex)weather4,Rain
                        sp.edit().putFloat(city_name + "rain" + Integer.toString(i), 0).apply();//  ex)rain2,0.64ミリ/3h
//                    Log.d("http","rain:"+0);
                    }
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                if(http!=null) {
                    http.disconnect();
                }
                if(in != null){
                    in.close();
                }
            }catch (Exception ignore){

            }
        }
        return src;
    }

    public void setOnCallBack(CallBackTask _cbj) {
        callbacktask = _cbj;
    }

    public static class CallBackTask {
        public void CallBack(String result) {
        }
    }
}
