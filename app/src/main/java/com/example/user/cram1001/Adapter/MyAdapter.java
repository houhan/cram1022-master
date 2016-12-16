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
 * Created by user on 2016/8/7.
 */

public class MyAdapter extends BaseAdapter {

    private LayoutInflater myInflater;
    // CharSequence[] list = null;
    private ArrayList<ContentTest> contentTests;
    public MyAdapter(Context ctxt, ArrayList<ContentTest> contentTests) {
        myInflater = LayoutInflater.from(ctxt);
        this.contentTests=contentTests;
        // this.list = list;

    }

    @Override
    public int getCount() {
        return  this.contentTests.size();
    }

    @Override
    public Object getItem(int position) {
        return contentTests.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //自訂類別，表達個別listItem中的view物件集合。
        ViewTag viewTag;

        if (convertView == null) {
            //取得listItem容器 view
            convertView = myInflater.inflate(R.layout.adapter, null);

            //建構listItem內容view
            viewTag = new ViewTag(
                    (TextView) convertView.findViewById(
                            R.id.textdate),
                    (TextView) convertView.findViewById(
                            R.id.texttitle),
                    (TextView) convertView.findViewById(
                            R.id.textcontent)
            );

            //設置容器內容
            convertView.setTag(viewTag);
        } else {
            viewTag = (ViewTag) convertView.getTag();
        }
        ContentTest contentTest=contentTests.get( position);
        viewTag.date.setText(contentTest.date);
        viewTag.title.setText(contentTest.title);
        viewTag.content .setText(contentTest.content);
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
    class ViewTag {
        TextView date;
        TextView title;
        TextView content;

        public ViewTag(TextView ndate, TextView ntitle, TextView ncontent) {
            this.date = ndate;
            this.title = ntitle;
            this.content = ncontent;
        }
    }


}
