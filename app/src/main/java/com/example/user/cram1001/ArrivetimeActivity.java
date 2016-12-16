package com.example.user.cram1001;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.user.cram1001.volleymgr.NetworkManager;

import java.net.URLEncoder;

public class ArrivetimeActivity extends AppCompatActivity {
private EditText Hours,mins;
    private String UNAME,Hourss,minss;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrivetime);
        Intent intent = this.getIntent();
        UNAME = intent.getStringExtra("UNAME");
        Button button22 = (Button) findViewById(R.id.button4);//取得按鈕
        button22.setOnClickListener(SendFcmListener);

        Hours = (EditText) findViewById(R.id.editText2);
        Hourss = Hours.getEditableText().toString();
        mins = (EditText) findViewById(R.id.editText3);
        minss = mins.getEditableText().toString();
    }

    private View.OnClickListener SendFcmListener = new View.OnClickListener() {
        private String message,DDtime;
        public void onClick(View v) {

            message = UNAME + " 的家長大約於 " + Hourss + "小時" + minss + "分鐘 後抵達安親班";

            String regid = "dh8BlEL1awI:APA91bFe0tg951Z1WdU2CtAn9dzpmveZebgFGKh0KG1RzlxJve7czYGrhwDHGLTJhiyjL0Wp4xUiEiglGb67Vz-YjRwArnM_nP7Oc189L2DrjbRvUEWELdsa3RNlkDS3ZVGkJLKgbrcm";
            String strmsg = URLEncoder.encode(message);

            String url = "https://cramschoollogin.herokuapp.com/api/sendfcmarrive?to=" + regid + "&message=" + strmsg;
            StringRequest request = new StringRequest(Request.Method.GET, url, mOnSuccessListener, mOnListener);
            NetworkManager.getInstance(ArrivetimeActivity.this).request(null, request);

        }
    };
    protected Response.Listener<String> mOnSuccessListener = new Response.Listener<String>() {

        @Override
        public void onResponse(String response){

            Toast.makeText(ArrivetimeActivity.this, "已通知老師", Toast.LENGTH_LONG).show();
            ArrivetimeActivity.this.finish();
        }
    };
    protected Response.ErrorListener mOnListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError err) {
            Toast.makeText(ArrivetimeActivity.this, "通知失敗 " + err.toString(), Toast.LENGTH_LONG).show();
        }
    };
}
