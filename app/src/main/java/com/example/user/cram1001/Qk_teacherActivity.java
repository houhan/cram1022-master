package com.example.user.cram1001;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.user.cram1001.Adapter.ContentCheck;
import com.example.user.cram1001.Adapter.ContentQk;
import com.example.user.cram1001.Adapter.ContentTest;
import com.example.user.cram1001.Adapter.MyAdapter;
import com.example.user.cram1001.Adapter.MyAdapterQk;
import com.example.user.cram1001.volleymgr.NetworkManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Qk_teacherActivity extends AppCompatActivity {
    private ArrayList<ContentQk> contentQk = new ArrayList<ContentQk>();
    private ListView listView;
    private MyAdapterQk myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qk_teacher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//回前頁

        listView = (ListView) findViewById(R.id.listqk);
        myAdapter = new MyAdapterQk(this, contentQk);
        listView.setAdapter(myAdapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//        });
        querymyAdapter();

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final ContentQk contentQ = (ContentQk) parent.getAdapter().getItem(position);
                new AlertDialog.Builder(Qk_teacherActivity.this)
                        .setTitle("是否要刪除")
//                        .setMessage("Want to delete " + position + " item?")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                StringRequest request = new StringRequest(Request.Method.GET, "https://cramschoollogin.herokuapp.com/api/deleteqk?_id=" + contentQ.id, mOnDeleteSuccessListener, mOnErrorListener);
                                NetworkManager.getInstance(Qk_teacherActivity.this).request(null, request);
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
        });
    }

    private void querymyAdapter(){
        StringRequest request = new StringRequest(Request.Method.GET, "https://cramschoollogin.herokuapp.com/api/queryqk", mResponseListener, mErrorListener);
        NetworkManager.getInstance(this).request(null, request);
    }

    private Response.Listener<String> mResponseListener = new Response.Listener<String>() {
        private String idd,namee, datee, reasonn,pss;
        public void onResponse(String response) {

            try {
                JSONArray array = new JSONArray(response);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);

                    namee= obj.getString("name");
                    datee = obj.getString("date");
                    reasonn = obj.getString("reson");
                    pss = obj.getString("ps");

                    ContentQk contentQ = new ContentQk(namee,datee,reasonn,pss);
                    contentQ.id= obj.getString("_id");
                    contentQk.add(contentQ);
                }
                myAdapter.notifyDataSetChanged();
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
    };
//        @Override
//        public void onResponse(String string) {
//            Log.d("Response", string);
//            // contentTest=new ArrayList<ContentTest>();
//            try {
//
//                JSONArray ary = new JSONArray(string);
//                StringBuilder ids = new StringBuilder();
//                StringBuilder names = new StringBuilder();
//                StringBuilder dates = new StringBuilder();
//                StringBuilder titles = new StringBuilder();
//                StringBuilder contents = new StringBuilder();
//                for (int i = 0; i < ary.length(); i++) {
//                    JSONObject json = ary.getJSONObject(i);
//                    String id = json.getString("_id");
//                    String name = json.getString("name");
//                    String date = json.getString("date");
//                    String reason = json.getString("reson");
//                    String ps = json.getString("ps");
//                    ContentQk contentQ = new ContentQk(name, date, reason, ps);
//                    contentQk.add(contentQ);
//                }
//                myAdapter.notifyDataSetChanged();
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    };

    private Response.ErrorListener mErrorListener = new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("Error", error.toString());
        }
    };

/*
    private Response.Listener<String> mOnDeleteSuccessListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
        }
    };
    private ErrorListener mOnErrorListener = new ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError err) {
            Toast.makeText(BillboardActivity.this, err.toString(), Toast.LENGTH_LONG).show();
        }
    };*/

    private Response.Listener<String> mOnDeleteSuccessListener = new Response.Listener<String>() {

        @Override
        public void onResponse(String response) {
//            Intent intent = new Intent(MainActivity.this, Home_teacherActivity.class);
//            intent.setClass(MainActivity.this, Home_teacherActivity.class);
            Intent intent=new Intent (Qk_teacherActivity.this , Qk_teacherActivity.class);
            startActivity(intent);
            Qk_teacherActivity.this.finish();
        }

    };

    private Response.ErrorListener mOnErrorListener = new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError err) {
            Toast.makeText(Qk_teacherActivity.this, err.toString(), Toast.LENGTH_LONG).show();
        }
    };
    //回前頁
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        setResult(RESULT_OK, intent);
        finish();
    }


}