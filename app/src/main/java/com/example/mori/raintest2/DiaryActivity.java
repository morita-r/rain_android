package com.example.mori.raintest2;

import android.app.Activity;

import android.content.SharedPreferences;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.FrameLayout;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by rikuya on 2016/08/26.
 */

public class DiaryActivity extends Activity implements View.OnClickListener, Camera2BasicFragment.OnCameraFragmentListener {
    private DrawerLayout mDrawer;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        if (null == savedInstanceState) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, Camera2BasicFragment.newInstance())
                    .commit();
            FrameLayout c = (FrameLayout) findViewById(R.id.container);
            c.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCameraFragmentEvent(final String path) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                registerAndroidDB(path);
                SharedPreferences diary = getSharedPreferences("diary", MODE_PRIVATE);
                SharedPreferences.Editor e = diary.edit();
                e.putString("path", path);
                e.commit();
                finish();
            }
        });

    }


    private void registerAndroidDB(String path) {
        // アンドロイドのデータベースへ登録
        // (登録しないとギャラリーなどにすぐに反映されないため)
       String[] p = {path};
        System.out.println(path);
        String[] mimeType = {"image/jpeg"};
        MediaScannerConnection.scanFile(getApplicationContext(), p, mimeType, null);

   }

    @Override
    public void onClick(View view) {


    }


}
