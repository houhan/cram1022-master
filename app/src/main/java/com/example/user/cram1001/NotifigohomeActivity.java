package com.example.user.cram1001;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.user.cram1001.Adapter.ContentCheck;
import com.example.user.cram1001.volleymgr.NetworkManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

public class NotifigohomeActivity extends AppCompatActivity {
    private ListView mListView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifigohome);

        mListView2 = (ListView) findViewById(R.id.gohomeview);
        mListView2.setOnItemLongClickListener(NotifigohomeListener);

        queryTodoList2();
    }

    private void queryTodoList2() {

        StringRequest request = new StringRequest(Request.Method.GET, "https://cramschoollogin.herokuapp.com/api/querystudentname", QuerySuccessListener, ErrorListener);
        NetworkManager.getInstance(this).request(null, request);
    }

    private Response.Listener<String> QuerySuccessListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                JSONArray ary = new JSONArray(response);
                int length = ary.length();

                ArrayList<NotifigohomeActivity.Todo2> datas = new ArrayList<NotifigohomeActivity.Todo2>();

                for (int i = 0; i < length; i++) {
                    JSONObject obj = ary.getJSONObject(i);
                    NotifigohomeActivity.Todo2 todo2 = new NotifigohomeActivity.Todo2();
                    todo2.name = obj.getString("name");
                    todo2.room = obj.getString("room");
                    todo2.regid2 = obj.getString("regid");
                    datas.add(todo2);
                }

                NotifigohomeActivity.TodoList2Adapter adapter = new NotifigohomeActivity.TodoList2Adapter(NotifigohomeActivity.this, datas);
                mListView2.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private Response.ErrorListener ErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError err) {
            Toast.makeText(NotifigohomeActivity.this, err.toString(), Toast.LENGTH_LONG).show();
        }
    };

    private AdapterView.OnItemLongClickListener NotifigohomeListener = new AdapterView.OnItemLongClickListener(){
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
            final Todo2 todo2 = (Todo2) parent.getAdapter().getItem(position);
            new AlertDialog.Builder(NotifigohomeActivity.this)
                    .setMessage("是否通知家長孩子已完成功課?")
                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                        private String message;
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            message ="孩子已完成功課，家長可以來接囉!";
//                             String strmsg = URLEncoder.encode(message);
                            String url = "https://cramschoollogin.herokuapp.com/api/sendfcmgohome?to=" + todo2.regid2;
                            StringRequest request = new StringRequest(Request.Method.GET, url, mOnAddSuccessListener, mOnErrorListener);
                            NetworkManager.getInstance(NotifigohomeActivity.this).request(null, request);
                        }
                    })
                    .setNegativeButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
            return false;
        }
    };

    protected Response.Listener<String> mOnAddSuccessListener = new Response.Listener<String>() {

        @Override
        public void onResponse(String response) {
            Toast.makeText(NotifigohomeActivity.this, "通知已送出", Toast.LENGTH_LONG).show();
        }
    };

    protected Response.ErrorListener mOnErrorListener = new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError err) {

           Toast.makeText(NotifigohomeActivity.this, "Err " + err.toString(), Toast.LENGTH_LONG).show();
        }
    };
    class Todo2 {
        String name;
        String room;
        String regid2;
    }

    class TodoList2Adapter extends BaseAdapter {

        private Context mContext;
        private ArrayList<NotifigohomeActivity.Todo2> mData;

        TodoList2Adapter(Context context, ArrayList<NotifigohomeActivity.Todo2> data) {
            mContext = context;
            mData = data;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint({ "SimpleDateFormat", "InflateParams" })
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_gohome, null);
            }

            NotifigohomeActivity.Todo2 todo2 = (NotifigohomeActivity.Todo2) getItem(position);

            TextView name = (TextView) convertView.findViewById(R.id.name2);
            name.setText(todo2.name);

            TextView room = (TextView) convertView.findViewById(R.id.studentclass2);
            room.setText(todo2.room);
//
//            TextView regid2 = (TextView) convertView.findViewById(R.id.id2);
//            regid2.setText(todo2.regid2);

            return convertView;
        }

    }
}
