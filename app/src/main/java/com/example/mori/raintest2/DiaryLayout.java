package com.example.mori.raintest2;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.text.Spannable;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by rikuya on 2016/08/31.
 */

public class DiaryLayout extends LinearLayout {
    TextView commentView;
    TextView dateView;
    ImageView pictureView;
    TextView locationView;

    public DiaryLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        commentView = (TextView) findViewById(R.id.textComment);
        dateView = (TextView) findViewById(R.id.textDate);
        pictureView = (ImageView) findViewById(R.id.imagePicture);
        locationView = (TextView)findViewById(R.id.locationDiary);
    }

    public void deleteView(int position){
        this.removeView(this);
    }

    public void bindView(Diary diary) throws FileNotFoundException {
        String date = diary.getDate();
        Spannable t = Spannable.Factory.getInstance().newSpannable(date);
        UnderlineSpan us = new UnderlineSpan();
        t.setSpan(us, 0, date.length(), t.getSpanFlags(us));
        dateView.setText(t, TextView.BufferType.SPANNABLE);
     //   System.out.println(diary.getDateF());
        commentView.setText(diary.getComment());
        locationView.setText(diary.getLocation());

//        FileInputStream in = new FileInputStream(getContext().getExternalFilesDir(null) + "/" +diary.getFileName());

        BitmapSyncTask bitmapSyncTask = new BitmapSyncTask(pictureView);
        bitmapSyncTask.execute(getContext().getExternalFilesDir(null) + "/" +diary.getFileName());
/*        Bitmap bitmap = BitmapFactory.decodeStream(in);
        Bitmap b = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        bitmap.recycle();
        pictureView.draw(new Canvas(b));
        b.recycle();
*/
    }
}