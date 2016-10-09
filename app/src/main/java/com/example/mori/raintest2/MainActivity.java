package com.example.mori.raintest2;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.Random;

public class MainActivity extends Activity implements View.OnClickListener {
    private DrawerLayout mDrawer;
    SoundPool sp;
    ImageView fukidashi;
    AnimationDrawable anim;
    ImageView image_machi;
    ImageView image_yorokobi;
    ImageView image_okori;
    Thread thread;
    int animation_frag = 0;
    int OPPAI1;
    int OPPAI2;
    int OPPAI3;
    int TIKUBI1;
    int TIKUBI2;
    int YOROKOBI1;
    int YOROKOBI2;
    int CLEAR;
    int CLOUDS;
    int RAIN;
    int SNOW;
    int siru;
    int weather_id;
    boolean TIKUBI_FRAG = false;

    private BluetoothAdapter Bt = BluetoothAdapter.getDefaultAdapter();
    private static final int REQUEST_ENABLE_BLUETOOTH = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton log = (ImageButton)findViewById(R.id.button_log2);
        log.setOnClickListener(this);
        ImageButton find = (ImageButton)findViewById(R.id.button_find2);
        find.setOnClickListener(this);
        final ImageButton weather = (ImageButton)findViewById(R.id.button_weather2);
        weather.setOnClickListener(this);
        fukidashi = (ImageView)findViewById(R.id.fukidashi);

        //デモなのでやらない
/*
        //Bluetooth関係
        Bt = Bt.getDefaultAdapter();
        if (Bt == null) {
            //Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        boolean btEnable = Bt.isEnabled();
        if(btEnable == true){
            //BluetoothがONだった場合の処理
            DeviceListTask dlTask = new DeviceListTask(this);
            dlTask.taskGO();
        }else{
            //OFFだった場合、ONにすることを促すダイアログを表示する画面に遷移
            Intent btOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(btOn, REQUEST_ENABLE_BLUETOOTH);
        }
*/

        //sound関係
        sp = new SoundPool(1, AudioManager.STREAM_MUSIC,0);
        OPPAI1 = sp.load(this,R.raw.oppai1,0);
        OPPAI2 = sp.load(this,R.raw.oppai2,0);
        OPPAI3 = sp.load(this,R.raw.oppai3,0);
        TIKUBI1 = sp.load(this,R.raw.tikubi,0);
        TIKUBI2 = sp.load(this,R.raw.tikubi2,0);
        YOROKOBI1 = sp.load(this,R.raw.yorokobi1,0);
        YOROKOBI2 = sp.load(this,R.raw.yorokobi2,0);
        CLEAR = sp.load(this,R.raw.cleare,0);
        CLOUDS = sp.load(this,R.raw.clouds,0);

        RAIN = sp.load(this,R.raw.rain,0);
        SNOW = sp.load(this,R.raw.snow,0);
        siru = sp.load(this,R.raw.siru,0);


        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if(animation_frag != 0)//れいんちゃん動作中はreturn
                        return false;

                    rain_touch((int) motionEvent.getX(),(int) motionEvent.getY());
                }
                return false;
            }
        });




        MainHttpTask task = new MainHttpTask(
                this,"GET","4bcb8214ded99c452448cd64523bf70c",0,36.108528,140.0997056,
                new MainHttpTask.AsyncTaskCallback(){
                    @Override
                    public void postExecute(String result) {
                        //main画面での処理
//                        text_city.setText("現在地（つくば市）の天気");
                        Log.d("callback","check");
                        weather_draw();
                    }
                });
        task.execute();


        //れいんちゃんアニメーション関係
        ImageView back = (ImageView)findViewById(R.id.back_anim);
        back.setBackgroundResource(R.drawable.back_anim);
        AnimationDrawable anim_back = (AnimationDrawable)back.getBackground();
        anim_back.start();



        //れいんちゃん通常時
        image_machi= (ImageView)findViewById(R.id.rain_machi);
        image_machi.setBackgroundResource(R.drawable.rain_machi_anim);
        anim = (AnimationDrawable)image_machi.getBackground();
        anim.start();

        //れいんちゃんy喜び
        image_yorokobi = (ImageView)findViewById(R.id.rain_yorokobi);
        image_yorokobi.setBackgroundResource(R.drawable.rain_yorokobi_anim);
        image_yorokobi.setVisibility(View.INVISIBLE);

        //れいんちゃん怒り
        image_okori = (ImageView)findViewById(R.id.rain_okori);
        image_okori.setBackgroundResource(R.drawable.rain_okori_anim);
        image_okori.setVisibility(View.INVISIBLE);

    }


    //drawer内のボタン（メニュー）管理
    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.button_log2:
                intent = new Intent(this,LogActivity.class);
                startActivity(intent);
                mDrawer.closeDrawers();
                finish();
                break;
            case R.id.button_find2:
                intent = new Intent(this,FindActivity.class);
                startActivity(intent);
                mDrawer.closeDrawers();
                finish();
                break;
            case R.id.button_weather2:
                intent = new Intent(this,WeatherActivity.class);
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

    //ホーム画面の吹き出しの天気画像管理
    public void weather_draw() {
        Log.d("get", "weather");
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
//        String weather = sp.getString("main_weather", "Clouds");
        String weather = "Clear";
        Log.d("weather", weather);
        if (weather.equals("Clear")) {
            fukidashi.setImageResource(R.drawable.clear);
            weather_id = 0;
        } else if (weather.equals("Clouds")) {
            fukidashi.setImageResource(R.drawable.clouds);
            weather_id = 1;
        }else if(weather.equals("Rain")) {
            Log.d("Rain","true");
            fukidashi.setImageResource(R.drawable.rain);
            weather_id = 2;
        }else if(weather.equals("Snow")) {
            fukidashi.setImageResource(R.drawable.snow);
            weather_id = 3;
        }
        fukidashi.setVisibility(View.VISIBLE);
    }

    //れいんちゃんanimation管理
    private void animation_change(){
        switch (animation_frag) {
            case 1://喜び->戻る
                image_okori.setVisibility(View.INVISIBLE);
                image_yorokobi.setVisibility(View.INVISIBLE);
                image_machi.setVisibility(View.VISIBLE);
                anim = (AnimationDrawable) image_machi.getBackground();
                anim.start();
                animation_frag = 0;
                break;
            case 2://怒り→戻る
                image_okori.setVisibility(View.INVISIBLE);
                image_yorokobi.setVisibility(View.INVISIBLE);
                image_machi.setVisibility(View.VISIBLE);
                anim = (AnimationDrawable) image_machi.getBackground();
                anim.start();
                animation_frag = 0;
                break;
            case 3:
                animation_frag = 0;
        }
    }

    //れいんちゃんタッチ管理
    private void rain_touch(int get_X, int get_Y){
        Log.d("TouchEvent", "X:" + get_X + ",Y:" + get_Y);

        if(530 < get_X && get_X < 880 && 850 < get_Y && get_Y < 1000) {//おっぱい 怒る
            if (535 < get_X && get_X < 555 && 925 < get_Y && get_Y < 945) {//右乳首 乳首は喜ぶ
                animation_frag = 1;
                //ピンポーン流す
                if(TIKUBI_FRAG) {
                    sp.play(TIKUBI2, 1, 1, 0, 0, 1);
                    TIKUBI_FRAG = false;
                }else {
                    sp.play(TIKUBI1, 1, 1, 0, 0, 1);
                    TIKUBI_FRAG = true;
                }
            }else if(760 < get_X && get_X < 780 && 945 < get_Y && get_Y < 965) {//左乳首
                animation_frag = 1;
                //ピンポーン流す
                if(TIKUBI_FRAG) {
                    sp.play(TIKUBI2, 1, 1, 0, 0, 1);
                    TIKUBI_FRAG = false;
                }else {
                    sp.play(TIKUBI1, 1, 1, 0, 0, 1);
                    TIKUBI_FRAG = true;
                }
            }else {
                //怒った声流す
                animation_frag = 2;
                TIKUBI_FRAG = false;
                Random rnd = new Random();
                int i = rnd.nextInt(100);
                if(i < 45)
                    sp.play(OPPAI1, 1, 1, 0, 0, 1);
                else if(i < 90)
                    sp.play(OPPAI2, 1, 1, 0, 0, 1);
                else
                    sp.play(OPPAI3, 1, 1, 0, 0, 1);
            }
        }else if(490 < get_X && get_X < 920 && 100 < get_Y && get_Y < 525){//顔　喜ぶ
            animation_frag = 1;
            TIKUBI_FRAG = false;
            Random rnd = new Random();
            int i = rnd.nextInt(100);
            if(i < 50)
                sp.play(YOROKOBI1, 1, 1, 0, 0, 1);
            else
                sp.play(YOROKOBI2, 1, 1, 0, 0, 1);
        }else if(1250 < get_X &&  get_Y < 200) {//天気　声だけ流す
            TIKUBI_FRAG = false;
            animation_frag = 3;
            switch (weather_id){
                case 0:
                    sp.play(CLEAR, 1, 1, 0, 0, 1);
                    break;
                case 1:
                    sp.play(CLOUDS, 1, 1, 0, 0, 1);
                    break;
                case 2:
                    sp.play(RAIN, 1, 1, 0, 0, 1);
                    break;
                case 3:
                    sp.play(SNOW, 1, 1, 0, 0, 1);
                    break;
            }
        }else if(400 < get_X && get_X < 1100 && 1150 < get_Y && get_Y < 1600) {
            animation_frag = 3;
            sp.play(siru, 1, 1, 0, 0, 1);
        }else
            TIKUBI_FRAG = false;

        switch(animation_frag) {
            case 0://待ってるだけ
                break;
            case 1://喜ぶ->待つ
                image_machi.setVisibility(View.INVISIBLE);
                image_okori.setVisibility(View.INVISIBLE);
                image_yorokobi.setVisibility(View.VISIBLE);
                anim = (AnimationDrawable) image_yorokobi.getBackground();
                anim.start();
                break;
            case 2://怒る→待つ
                image_yorokobi.setVisibility(View.INVISIBLE);
                image_machi.setVisibility(View.INVISIBLE);
                image_okori.setVisibility(View.VISIBLE);
                anim = (AnimationDrawable) image_okori.getBackground();
                anim.start();
                break;
        }
        thread =
                (new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            switch(animation_frag){
                                case 0://待ってるだけ
                                    break;
                                case 1://喜ぶ->待つ
                                    Thread.sleep(3500);
                                    handler.sendEmptyMessage(0);
                                    break;
                                case 2://怒る→待つ
                                    Thread.sleep(3500);
                                    handler.sendEmptyMessage(0);
                                    break;
                                case 3://しゃべる
                                    Thread.sleep(3500);
                                    handler.sendEmptyMessage(0);
                                    break;
                            }
                        } catch (InterruptedException e) {
                        }
                    }
                }));
        thread.start();
    }

    //BluetoothをONにするintentから戻ると呼ばれるmethod
    @Override
    protected void onActivityResult(int requestCode, int ResultCode, Intent date){
        if(requestCode == REQUEST_ENABLE_BLUETOOTH){
            if(ResultCode == Activity.RESULT_OK){
                //BluetoothがONにされた場合の処理
                Log.d("","BluetoothをONにしてもらえました。");
                DeviceListTask dlTask = new DeviceListTask(this);
                dlTask.taskGO();
            }else{
                Log.d("","BluetoothがONにしてもらえませんでした。");
                finish();
            }
        }
    }

    //animation動作中はスレッド動かして時間計ってタッチ無効にしてるから、コールバックでGUIいじる
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            animation_change();
        }
    };

    //れいんちゃん誘拐通知的な
    private void rain_pakuri(){
        new AlertDialog.Builder(this)
                .setTitle("ヤバイ！！！")
                .setMessage("れいんちゃん、パクられたんとちゃうか?")
                .setPositiveButton("OK", null)
                .show();

    }
}
