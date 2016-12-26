package com.example.user.cram1001;

import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.user.cram1001.Adapter.ContentTest;
import com.example.user.cram1001.volleymgr.NetworkManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

public class CheckParentsActivity extends Activity{
    private String UClass,UNAME,UUSER,UStatus;
    private TextView UIDtest,CLASS,UUNAME,UUStatus;
    private SwipeRefreshLayout laySwipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_parents);
        Intent intent = this.getIntent();

        UClass = intent.getStringExtra("UClass");
        UNAME = intent.getStringExtra("UNAME");
        UUSER = intent.getStringExtra("UUSER");
        UStatus = intent.getStringExtra("UStatus");

        UUNAME = (TextView) findViewById(R.id.CID);
        UUNAME.setText(UNAME);

        UUStatus = (TextView) findViewById(R.id.status);
        initView();

//        UUStatus = (TextView) findViewById(R.id.status);
//        UUStatus.setText(Statuss);

    }

    private void initView() {
        laySwipe = (SwipeRefreshLayout) findViewById(R.id.activity_check_parents);
        laySwipe.setOnRefreshListener(onSwipeToRefresh);
        laySwipe.setColorSchemeResources(
                android.R.color.holo_red_light,
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light);

        String strName = URLEncoder.encode(UNAME);
        String url = "https://cramschoollogin.herokuapp.com/api/querystudentstatus?name=" + strName;
        StringRequest request = new StringRequest(Request.Method.GET,url, QuerySuccessListener, QueryErrorListener);
        NetworkManager.getInstance(CheckParentsActivity.this).request(null, request);
    }
//更新狀態
    private SwipeRefreshLayout.OnRefreshListener onSwipeToRefresh = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            laySwipe.setRefreshing(true);
            String strName = URLEncoder.encode(UNAME);
            String url = "https://cramschoollogin.herokuapp.com/api/querystudentstatus?name=" + strName;
            StringRequest request = new StringRequest(Request.Method.GET,url, QuerySuccessListener, QueryErrorListener);
            NetworkManager.getInstance(CheckParentsActivity.this).request(null, request);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    laySwipe.setRefreshing(false);
//                    Toast.makeText(getApplicationContext(), "Refresh done!", Toast.LENGTH_SHORT).show();
                }
            }, 3000);
        }
    };

    private Response.Listener<String> QuerySuccessListener = new Response.Listener<String>() {

        @Override
        public void onResponse(String string) {
            Log.d("Response", string);
            // contentTest=new ArrayList<ContentTest>();
            try {

                JSONArray ary = new JSONArray(string);
                StringBuilder ssstatuss = new StringBuilder();

                for (int i = 0; i < ary.length(); i++) {
                    JSONObject json = ary.getJSONObject(i);
                    String ssstatus = json.getString("sstatus");
                    UUStatus.setText(ssstatus);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private Response.ErrorListener QueryErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError err) {
            Toast.makeText(CheckParentsActivity.this, "Err " + err.toString(), Toast.LENGTH_LONG).show();
        }
    };

    public class ContentStatus {
        public String status="";

        public  ContentStatus(String nstatus){
            this.status=nstatus;
        }
    }
}