package com.example.mori.raintest2;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by mori on 2016/08/28.
 */
public class MainHttpTask extends AsyncTask<Void, Void, String> {
    private CallBackTask callbacktask;
    private String type;
    private String mUrl;
    float min_temp;
    float max_temp;
    String weather;
    private int id;
    private Activity mParentActivity;

    public interface AsyncTaskCallback {
        void postExecute(String result);
    }

    private AsyncTaskCallback callback = null;


    public MainHttpTask(Activity parentActivity, String type, String api, int id, double latitude, double longitude, AsyncTaskCallback _callback){
        this.id = id;
        this.callback = _callback;
        mParentActivity = parentActivity;
        this.type = type.toUpperCase();
        this.mUrl = "http://api.openweathermap.org/data/2.5/forecast/daily?lat=" + Double.toString(latitude)
                + "&amp;lon=" + Double.toString(longitude)
                +"&amp;mode=json&units=metric&amp;cnt=1"
                +"&APPID="+ api;
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
        Log.d("http","天気取得中");
        HttpURLConnection http = null;
        InputStream in = null;
        String src = null;
        try {
            URL url = new URL(mUrl);

            http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(type);
            http.setDoInput(true);
            http.setRequestProperty("Content-Type","application/json; charset=utf-8");
            http.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(http.getInputStream(), "UTF-8"));
            src = reader.readLine();
            JSONObject rootObj = new JSONObject(src);

            JSONArray listArray = rootObj.getJSONArray("list");
            JSONObject OBJ = listArray.getJSONObject(0);
            JSONArray weatherArray = OBJ.getJSONArray("weather");
            JSONObject weatherObj = weatherArray.getJSONObject(0);
            String weather = weatherObj.getString("main");//Clear,Clouds,Rain
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mParentActivity);
            sp.edit().putString("main_weather",weather).apply();//  ex)weather4,Rain
            Log.d("http","finish");
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
