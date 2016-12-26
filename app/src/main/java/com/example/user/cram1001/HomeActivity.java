package com.example.user.cram1001;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.cram1001.Fcm.GCMUtils;
import com.example.user.cram1001.volleymgr.NetworkManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.net.URLEncoder;

public class HomeActivity extends AppCompatActivity {
    private String UID,UUSER,UCLASS,UNAME,UStatus;
    private TextView UIDtest,CLASS,UUNAME;
    private static final String TAG = "MyFirebaseIIDService";
    private ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //抓取上一頁資料
        Intent intent = this.getIntent();

        UNAME = intent.getStringExtra("UNAME");
        UUSER = intent.getStringExtra("UUSER");
        UCLASS = intent.getStringExtra("UClass");
        UStatus = intent.getStringExtra("UStatus");

        //UNAME = intent.getStringExtra("UNAME");

        UUNAME = (TextView) findViewById(R.id.UIDtest);
        UUNAME.setText(UNAME);
        CLASS = (TextView) findViewById(R.id.Uclass);
        CLASS.setText(UCLASS);

//        Intent intent2 = new Intent(HomeActivity.this, QkActivity.class);
//        intent2.setClass(HomeActivity.this, QkActivity.class);
//        intent2.putExtra("UNAME", UNAME);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please wait");
        mProgressDialog.setCanceledOnTouchOutside(false);


        Button button = (Button) findViewById(R.id.billboard);//取得按鈕
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(HomeActivity.this, BillboardActivity.class);
                HomeActivity.this.startActivity(intent);
            }
        });//將這個Listener綑綁在這個Button


        Button button2 = (Button) findViewById(R.id.qk);//取得按鈕
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, QkActivity.class);
                intent.setClass(HomeActivity.this, QkActivity.class);
                intent.putExtra("UNAME", UNAME);
                startActivity(intent);
            }
        });


       Button button3 = (Button) findViewById(R.id.Catch);//取得按鈕
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(HomeActivity.this, PublicActivity.class);
                intent.putExtra("UNAME", UNAME);
                HomeActivity.this.startActivity(intent);
            }
        });




        Button button5 = (Button) findViewById(R.id.attendance);//取得按鈕
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(HomeActivity.this, CheckParentsActivity.class);
                intent.putExtra("UNAME", UNAME);
                intent.putExtra("UClass", UCLASS);
                intent.putExtra("UStatus",UStatus);
                startActivity(intent);

            }
        });
//上傳token id
        String token = GCMUtils.getSavedToken(this);
        if (TextUtils.isEmpty(token)) {
            AsyncTask<Void, Void, Void> getTokenTask = new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... params) {
                    String token = GCMUtils.getGCMToken(HomeActivity.this);
                    if (!TextUtils.isEmpty(token)) {
                        GCMUtils.saveToken(HomeActivity.this, token);
                        Log.d("Token",token);
                        String strUser = URLEncoder.encode(UUSER);
                        String strToken = URLEncoder.encode(token);
                        String url = "https://cramschoollogin.herokuapp.com/api/insertRegId?user=" + strUser + "&regid=" + strToken;
                        StringRequest request = new StringRequest(Request.Method.GET, url, tokenAddSuccessListener, tokenErrorListener);
                        NetworkManager.getInstance(HomeActivity.this).request(null, request);

                    }
                    return null;
                }
            };
            getTokenTask.execute();
        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    protected Response.Listener<String> tokenAddSuccessListener = new Response.Listener<String>() {

        @Override
        public void onResponse(String response) {

        }
    };

    protected Response.ErrorListener tokenErrorListener = new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError err) {

            Toast.makeText(HomeActivity.this, "Err " + err.toString(), Toast.LENGTH_LONG).show();
        }
    };

}