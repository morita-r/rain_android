package com.example.mori.raintest2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rikuya on 2016/08/30.
 */

public class DiaryArrayAdapter extends ArrayAdapter<Diary> implements Filterable{
    private int resourceId;
    private List<Diary> items;
    private LayoutInflater inflater;
    private Filter filter;


    public DiaryArrayAdapter(Context context, int resource , List<Diary> items) {
        super(context, resource,items);
        this.resourceId = resource;
        this.items = items;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final DiaryLayout view;
        if (convertView != null) {
            view = (DiaryLayout)convertView;
        } else {
            view = (DiaryLayout)this.inflater.inflate(this.resourceId, null);
        }

        try {
            view.bindView(getItem(getCount() - 1 - position));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return view;
    }
}
