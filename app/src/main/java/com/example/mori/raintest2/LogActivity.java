package com.example.mori.raintest2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class LogActivity extends Activity implements View.OnClickListener {
    private DrawerLayout mDrawer;
    private Bundle s;
    private DBAdapter dbAdapter;
    private ArrayList<Diary> viewedDiaries = new ArrayList<Diary>();
    private  SharedPreferences diary;
    public EditText commentText;
    public SharedPreferences.Editor editor;
    private ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        ImageButton main = (ImageButton) findViewById(R.id.button_main2);
        main.setOnClickListener(this);
        ImageButton find = (ImageButton) findViewById(R.id.button_find2);
        find.setOnClickListener(this);
        ImageButton weather = (ImageButton) findViewById(R.id.button_weather2);
        weather.setOnClickListener(this);
        ImageButton createDiary = (ImageButton)findViewById(R.id.button_create);
        createDiary.setOnClickListener(this);
        ImageButton takePicture = (ImageButton)findViewById(R.id.button_camera);
        takePicture.setOnClickListener(this);
        commentText = (EditText)findViewById(R.id.editText2);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        dbAdapter = new DBAdapter(getApplicationContext());
        dbAdapter.open();

       // dbAdapter.DropTable();
       // dbAdapter.open();
      //  dbAdapter.deleteAllDiary();

        diary = getSharedPreferences("diary", MODE_PRIVATE);
        editor = diary.edit();
        editor.clear().commit();

        try {
            readDiaries();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        lv = (ListView)findViewById(R.id.list_diary);
        DiaryArrayAdapter a = new DiaryArrayAdapter(this, R.layout.diary_list_item, viewedDiaries);
        lv.setAdapter(a);
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),"Delete No." + String.valueOf(position) + " diary",Toast.LENGTH_LONG).show();
                dbAdapter.deleteDiary(position);
                lv.invalidate();
                return false;
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
    }


    public void readDiaries() throws FileNotFoundException {
        Cursor c = dbAdapter.getAllDiary();
        if(c.moveToFirst()) {
            do {
                String comment = c.getString(c.getColumnIndex("comment"));
                String date = c.getString(c.getColumnIndex("date"));
                String path = c.getString(c.getColumnIndex("path"));
                System.out.println(date +" : "+comment+" : "+path);
                viewedDiaries.add(new Diary(comment, path, date));
  //              addDiaryView(new Diary(comment, path, date));
            }while(c.moveToNext());
        }
    }

    public void addDiaryView(Diary diary) throws FileNotFoundException {
        viewedDiaries.add(diary);
        System.out.println(viewedDiaries);
        lv.setAdapter(new DiaryArrayAdapter(this, R.layout.diary_list_item, viewedDiaries));
        lv.invalidate();
        /*
        ListView listView = (ListView)findViewById(R.id.list_diary);
        // ScrollView に LinearLayout を追加
        View item = getLayoutInflater().inflate(R.layout.diary_list_item, null);
        listView.addView(item);

        TextView comment = (TextView)item.findViewById(R.id.textComment);
        TextView date = (TextView)item.findViewById(R.id.textDate);
        ImageView pic = (ImageView)item.findViewById(R.id.imagePicture);
        try {

            FileInputStream in = new FileInputStream(getExternalFilesDir(null) + "/" +diary.getFileName());
//this.openFileInput("/storage/emulated/0/Android/data/com.example.mori.raintest2/files/"+diary.getFileName());//getExternalFilesDir(null) + "/" + diary.getFileName());
            //InputStream in = openFileInput(diary.getFileName());

            Bitmap bitmap = BitmapFactory.decodeStream(in);
            Bitmap b = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            bitmap.recycle();
            pic.draw(new Canvas(b));
            b.recycle();
        }catch (Exception e){
            e.printStackTrace();
        }

        comment.setText(diary.getComment());
        date.setText(String.valueOf(diary.getDate()));
*/
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.button_camera:
                intent = new Intent(this, DiaryActivity.class);
                super.onResume();
                startActivity(intent);
                break;

            case R.id.button_create:
                String str = commentText.getText().toString();
                //System.out.println(String.valueOf(System.currentTimeMillis()) +":" +String.valueOf((int)System.currentTimeMillis()));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd, E, HH:mm:ss");
                Diary d = new Diary(str, diary.getString("path", "no path"), sdf.format(System.currentTimeMillis()));
                if (str != null && d.getFileName() != "no path") {

                    dbAdapter.saveDiary(d);

                    try {
                        addDiaryView(d);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    editor.clear().commit();
                }else{
                    Toast.makeText(this, "Please take a picture.", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.button_main2:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                mDrawer.closeDrawers();
                break;
            case R.id.button_find2:
                intent = new Intent(this, FindActivity.class);
                startActivity(intent);
                mDrawer.closeDrawers();
                break;
            case R.id.button_weather2:
                intent = new Intent(this, WeatherActivity.class);
                startActivity(intent);
                mDrawer.closeDrawers();
                break;
        }
    }
}