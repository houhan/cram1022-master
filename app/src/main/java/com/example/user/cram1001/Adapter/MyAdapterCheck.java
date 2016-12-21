package com.example.user.cram1001.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.user.cram1001.R;


import java.util.ArrayList;

/**
 * Created by user on 2016/10/8.
 */

public class MyAdapterCheck extends BaseAdapter {

    private LayoutInflater myInflater;
    // CharSequence[] list = null;
    private ArrayList<ContentCheck> contentChecks;
    public MyAdapterCheck(Context ctxt, ArrayList<ContentCheck> contentChecks) {
        myInflater = LayoutInflater.from(ctxt);
        this.contentChecks=contentChecks;
        // this.list = list;

    }

    @Override
    public int getCount() {
        return  this.contentChecks.size();
    }

    @Override
    public Object getItem(int position) {
        if(contentChecks!=null && contentChecks.size()>position && position>=0) {
            return contentChecks.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //自訂類別，表達個別listItem中的view物件集合。
        MyAdapterCheck.ViewTag2 viewTag2;

        if (convertView == null) {
            //取得listItem容器 view
            convertView = myInflater.inflate(R.layout.adapter_check, null);

            //建構listItem內容view
            viewTag2 = new MyAdapterCheck.ViewTag2(
                    (TextView) convertView.findViewById(
                            R.id.name),
                    (TextView) convertView.findViewById(R.id.studentclass),
                   (TextView) convertView.findViewById(R.id.checktext)
            );

            //設置容器內容
            convertView.setTag(viewTag2);
        } else {
            viewTag2= (MyAdapterCheck.ViewTag2) convertView.getTag();
        }
        ContentCheck contentCheck=contentChecks.get( position);
        viewTag2.name.setText(contentCheck.name);
        viewTag2.room.setText(contentCheck.room);
        viewTag2.textcheck.setText(contentCheck.textcheck);

/*
        //設定內容圖案
        switch(position){
            case MyListView.MyListView_camera:
                viewTag.icon.setBackgroundResource(R.drawable.ic_launcher_camera);
                break;
            case MyListView.MyListView_album:
                viewTag.icon.setBackgroundResource(R.drawable.ic_launcher_gallery);
                break;
            case MyListView.MyListView_map:
                viewTag.icon.setBackgroundResource(R.drawable.ic_launcher_maps);
                break;
        }
        */
        //設定內容文字
        //  viewTag.title.setText(list[position]);

        return convertView;
    }
    private ArrayList<Integer> mList;
    public void removeItem(int index){
        mList.remove(index);
    }

    //自訂類別，表達個別listItem中的view物件集合。
    class ViewTag2 {
        TextView name;
        TextView room;
        TextView textcheck;



        public ViewTag2(TextView nname,TextView nroom, TextView narrive) {
            this.name = nname;
            this.room = nroom;
           // this.regid = nregid;
            this.textcheck = narrive;

        }
    }


}
