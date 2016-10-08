package com.example.mori.raintest2;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

/**
 * Created by rikuya on 2016/08/31.
 */
public class BitmapSyncTask extends AsyncTask<String, Void, Bitmap> {

    private ImageView imageView; // アイコンを表示するビュー

    // コンストラクタ
    public BitmapSyncTask(ImageView imageView) {
        this.imageView = imageView;
    }

    // バックグラウンドで実行する処理
    @Override
    protected Bitmap doInBackground(String... filepath) {
        Bitmap image;

//        BitmapFactory.Options bmOp = new BitmapFactory.Options();
//        bmOp.inSampleSize = 16;

        image = BitmapFactory.decodeFile(filepath[0]);

        return image;
    }

    // メインスレッドで実行する処理
    @Override
    protected void onPostExecute(Bitmap result) {
        this.imageView.setImageBitmap(result);
        this.imageView.setVisibility(View.VISIBLE);
    }
}