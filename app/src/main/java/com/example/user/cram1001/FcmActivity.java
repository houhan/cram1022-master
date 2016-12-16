package com.example.user.cram1001;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.user.cram1001.volleymgr.NetworkManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Scanner;

public class FcmActivity extends AppCompatActivity {

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_fcm);

                Button SendfcmBillButton = (Button) findViewById(R.id.fcmsend);
                SendfcmBillButton.setOnClickListener(SendfcmListener);
    }
    private View.OnClickListener SendfcmListener = new View.OnClickListener() {

        public void onClick(View v) {

            try {
                String regid="cnCd2iNf2bY:APA91bFnSdYbiCf33YA4WEWPuJ2nRKqT68MqpXUoRyvrEjBKXwotp1Q3hg0SgzolqPNACyxEFzTUCn3865WXJH1LbAvqjapRqa-tvjw7ptfg7AWQJlgbHPKY08FNk1JVYNTqShHiTn8L";
                String MSG="小孩已抵達安親班";
                //螢幕擷取三項資料後上傳DB
                String strregid = URLEncoder.encode(regid.toString(), "UTF-8");
             //  String strmsg = URLEncoder.encode(MSG.toString(), "UTF-8");
             //   String strtitle = URLEncoder.encode("HOUHAN".toString(), "UTF-8");

                String url = "https://cramschoollogin.herokuapp.com/api/sendfcm?to=" +regid ;
                StringRequest request = new StringRequest(Request.Method.GET, url, mOnAddSuccessListener, mOnErrorListener);
                NetworkManager.getInstance(FcmActivity.this).request(null, request);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    };

    protected Response.Listener<String> mOnAddSuccessListener = new Response.Listener<String>() {

        @Override
        public void onResponse(String response) {

            Toast.makeText(FcmActivity.this, "新增成功", Toast.LENGTH_LONG).show();

        }
    };

    protected Response.ErrorListener mOnErrorListener = new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError err) {

            Toast.makeText(FcmActivity.this, "Err " + err.toString(), Toast.LENGTH_LONG).show();
        }
    };
//
//    public String addNotificationKey(
//    String senderId, String userEmail, String registrationId, String idToken)
//            throws IOException, JSONException
//
//    {
//        URL url = new URL("https://android.googleapis.com/gcm/googlenotification");
//        HttpURLConnection con = (HttpURLConnection) url.openConnection();
//        con.setDoOutput(true);
//        senderId = "cnCd2iNf2bY:APA91bFnSdYbiCf33YA4WEWPuJ2nRKqT68MqpXUoRyvrEjBKXwotp1Q3hg0SgzolqPNACyxEFzTUCn3865WXJH1LbAvqjapRqa-tvjw7ptfg7AWQJlgbHPKY08FNk1JVYNTqShHiTn8L";
//        // HTTP request header
//        con.setRequestProperty("project_id", senderId);
//        con.setRequestProperty("Content-Type", "application/json");
//        con.setRequestProperty("Accept", "application/json");
//        con.setRequestMethod("POST");
//        con.connect();
//
//        // HTTP request
//        JSONObject data = new JSONObject();
//        data.put("operation", "add");
//        data.put("notification_key_name", userEmail);
//        data.put("registration_ids", new JSONArray(Arrays.asList(registrationId)));
//        data.put("id_token", senderId);
//
//        OutputStream os = con.getOutputStream();
//        os.write(data.toString().getBytes("UTF-8"));
//        os.close();
//
//        // Read the response into a string
//        InputStream is = con.getInputStream();
//        String responseString = new Scanner(is, "UTF-8").useDelimiter("\\A").next();
//        is.close();
//
//        // Parse the JSON string and return the notification key
//        JSONObject response = new JSONObject(responseString);
//        return response.getString("notification_key");
//    }
}