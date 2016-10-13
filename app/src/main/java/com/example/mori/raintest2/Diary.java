package com.example.mori.raintest2;

import android.app.Activity;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.logging.SimpleFormatter;

import static android.content.Context.MODE_APPEND;

/**
 * Created by rikuya on 2016/08/29.
 */

public class Diary {

    private String comment;
    private String fileName;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd, E, HH:mm:ss");
    private String date;
    private String location;

    Diary(String comment, String fileName, String date){
        this.comment = comment;
        this.fileName = fileName;
        this.date = date;
        this.location = "茨城県つくば市天王台1-1-1";
    }

    public String getComment() {
        return comment;
    }

    public String getDate() {
        return date;
    }

    public String getFileName() {
        return fileName;
    }

    public String getLocation(){ return location;}

    public boolean saveDiary(Activity activity){
        String saveText = comment+":"+fileName+":"+date;
        try{
            OutputStream out = activity.openFileOutput("diary.txt",MODE_APPEND);
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(out,"UTF-8"));
            writer.append(saveText);
            writer.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        return true;
    }

}
