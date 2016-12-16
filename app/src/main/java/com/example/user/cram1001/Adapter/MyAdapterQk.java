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
 * Created by user on 2016/11/15.
 */

public class MyAdapterQk extends BaseAdapter {
    private LayoutInflater myInflater;
    // CharSequence[] list = null;
    private ArrayList<ContentQk> contentQks;
    public MyAdapterQk(Context ctxt, ArrayList<ContentQk> contentQks) {
        myInflater = LayoutInflater.from(ctxt);
        this.contentQks=contentQks;
        // this.list = list;

    }

    @Override
    public int getCount() {
        return  this.contentQks.size();
    }

    @Override
    public Object getItem(int position) {
        return contentQks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //自訂類別，表達個別listItem中的view物件集合。
        MyAdapterQk.ViewTag3 viewTag3;

        if (convertView == null) {
            //取得listItem容器 view
            convertView = myInflater.inflate(R.layout.adapter_qk,null);

            //建構listItem內容view
            viewTag3 = new MyAdapterQk.ViewTag3(
                    (TextView) convertView.findViewById(
                            R.id.tqkname),
                    (TextView) convertView.findViewById(R.id.tqkdate),
                    (TextView) convertView.findViewById(R.id.tqkreason),
                    (TextView) convertView.findViewById(R.id.tqkps)
            );

            //設置容器內容
            convertView.setTag(viewTag3);
        } else {
            viewTag3= (MyAdapterQk.ViewTag3) convertView.getTag();
        }
        ContentQk contentQk=contentQks.get( position);
        viewTag3.name.setText(contentQk.name);
        viewTag3.date.setText(contentQk.date);
        viewTag3.reason.setText(contentQk.reason);
        viewTag3.ps.setText(contentQk.ps);
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
    class ViewTag3 {
        TextView name;
        TextView date;
        TextView reason;
        TextView ps;

        public ViewTag3(TextView nname, TextView ndate , TextView nreason , TextView nps) {
            this.name = nname;
            this.date = ndate;
            this.reason = nreason;
            this.ps = nps;

        }
    }

}

