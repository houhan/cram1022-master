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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.user.cram1001.Adapter.ContentTest;
import com.example.user.cram1001.Adapter.MyAdapter;
import com.example.user.cram1001.volleymgr.NetworkManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BillboardActivity extends AppCompatActivity {
    private ArrayList<ContentTest> contentTest = new ArrayList<ContentTest>();
    private ListView listView;
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billboard);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//回前頁

        StringRequest request = new StringRequest(Request.Method.GET, "https://cramschoollogin.herokuapp.com/api/querybillboard", mResponseListener, mErrorListener);
        NetworkManager.getInstance(this).request(null, request);

        //設定此Activity使用的res layout
        setContentView(R.layout.list);
        listView = (ListView) findViewById(R.id.listView);
        myAdapter = new MyAdapter(this, contentTest);
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

    }
    void fillData() {
    }


    private Response.Listener<String> mResponseListener = new Response.Listener<String>() {

        @Override
        public void onResponse(String string) {
            Log.d("Response", string);
            // contentTest=new ArrayList<ContentTest>();
            try {

                JSONArray ary = new JSONArray(string);
                StringBuilder ids = new StringBuilder();
                StringBuilder dates = new StringBuilder();
                StringBuilder titles = new StringBuilder();
                StringBuilder contents = new StringBuilder();
                for (int i = 0; i < ary.length(); i++) {
                    JSONObject json = ary.getJSONObject(i);
                    String _id = json.getString("_id");
                    String date = json.getString("date");
                    String title = json.getString("title");
                    String content = json.getString("content");
                    ContentTest contentS = new ContentTest( date, title, content );
                    contentTest.add(contentS);
                }
                myAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private Response.ErrorListener mErrorListener = new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("Error", error.toString());
        }
    };


    //回前頁
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        setResult(RESULT_OK, intent);
        finish();
    }


}
