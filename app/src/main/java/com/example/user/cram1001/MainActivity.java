package com.example.user.cram1001;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.support.v7.widget.ShareActionProvider;
import android.test.ServiceTestCase;
import android.text.TextUtils;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.Response.Listener;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.user.cram1001.Fcm.GCMUtils;
import com.example.user.cram1001.Fcm.MyInstanceIDService;
import com.example.user.cram1001.volleymgr.NetworkManager;

import java.security.MessageDigest;
import java.util.Set;
//import java.net.URL;


public class MainActivity extends AppCompatActivity {
    private EditText AccountInput, PasswordInput;
    private ProgressDialog mProgressDialog, nProgressDialog, oProgressDialog;
    private CheckBox check1,check2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AccountInput = (EditText) findViewById(R.id.editID);
        PasswordInput = (EditText) findViewById(R.id.editPWD);

        // 取得帳號密碼
        String strUserName = AccountInput.getText().toString();
        String strPassword = PasswordInput.getText().toString();
        String strTeacher = PasswordInput.getText().toString();

//        Button buttontest = (Button) findViewById(R.id.create);//取得按鈕
//        // buttontest.setOnClickListener(TestListener);
//        buttontest.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(MainActivity.this, TestActivity.class);
//                MainActivity.this.startActivity(intent);
//
//            }
//        });//將這個Listener綑綁在這個Button

        Button LogInButton = (Button) findViewById(R.id.button);


        oProgressDialog = new ProgressDialog(this);
        oProgressDialog.setMessage("帳號錯誤");
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("確認登入中");
        nProgressDialog = new ProgressDialog(this);
        nProgressDialog.setMessage("密碼錯誤");

        check1 =(CheckBox)findViewById(R.id.checkBox8);
        //check2 =(CheckBox)findViewById(R.id.checkBox9);
        //SharedPreferences將name 和 pass 記錄起來 每次進去軟體時 開始從中讀取資料 放入login_name，login_password中
        SharedPreferences remdname=getPreferences(MainActivity.MODE_PRIVATE);
        String name_str=remdname.getString("name", "");
        String pass_str=remdname.getString("pass", "");
        AccountInput.setText(name_str);
        PasswordInput.setText(pass_str);
        boolean isProtecting = remdname.getBoolean("isProtected", false);//每次进来的时候读取保存的数据
        if (isProtecting) {
            check1.setChecked(true);
        }
        check1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {

                    SharedPreferences remdname=getPreferences(MainActivity.MODE_PRIVATE);
                    SharedPreferences.Editor edit=remdname.edit();

                    edit.putString("name", AccountInput.getText().toString());
                    edit.putString("pass", PasswordInput.getText().toString());
                    edit.putBoolean("isProtected", true);
                    edit.commit();


                }
                if(!isChecked)
                {
                    SharedPreferences remdname=getPreferences(MainActivity.MODE_PRIVATE);
                    SharedPreferences.Editor edit=remdname.edit();
                    edit.putBoolean("isProtected", false);
                    edit.putString("name", "");
                    edit.putString("pass", "");
                    edit.commit();
                }
            }
        });
        //5.登錄按鈕事件保存第一次的SharedPreferences
        LogInButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //在這寫登錄後的事件內容
                if (check1.isChecked())//檢測使用者名密碼
                {
                    SharedPreferences remdname = getPreferences(MainActivity.MODE_PRIVATE);
                    SharedPreferences.Editor edit = remdname.edit();
                    edit.putString("name", AccountInput.getText().toString());
                    edit.putString("pass", PasswordInput.getText().toString());
                    edit.putBoolean("isProtected", true);
                    edit.commit();

                }
            }
        });
        LogInButton.setOnClickListener(LogInListener);

//        buttontest.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//
//            }
//        });

        //  buttontest.setOnClickListener(TestListener);
    }
    private View.OnClickListener LogInListener = new View.OnClickListener() {
        public void onClick(View v) {
            try {
                String strAccount = URLEncoder.encode(AccountInput.getEditableText().toString(), "UTF-8");

                mProgressDialog.show();
                //String url = "https://cramtest.herokuapp.com/api/checkaccount?user=" + strAccount;
                String url = "https://cramschoollogin.herokuapp.com/api/checkaccount?user=" + strAccount;
                StringRequest request = new StringRequest(Request.Method.GET, url, AccountSuccessListener, AccountErrorListener);
                NetworkManager.getInstance(MainActivity.this).request(null, request);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    };

    protected Response.Listener<String> AccountSuccessListener = new Response.Listener<String>() {
        private String user,name,UNAME,UNMAE2;

        @Override
        public void onResponse(String response) {
            try {

                JSONArray array = new JSONArray(response);

                String strAccount = URLEncoder.encode(AccountInput.getEditableText().toString(),"UTF-8");

                int length = array.length();
                for (int i = 0; i < length; i++) {
                    JSONObject obj = array.getJSONObject(i);
                    user = obj.getString("user");

                    if (user.equals("1")) {
                        String url = "https://cramschoollogin.herokuapp.com/api/query?user=" + strAccount ;
                        StringRequest request = new StringRequest(Request.Method.GET, url, LoginSuccessListener, LoginErrorListener);
                        NetworkManager.getInstance(MainActivity.this).request(null, request);
                    }
                    else
                    {
                        mProgressDialog.dismiss();
                        oProgressDialog.show();
                    }
                }

            } catch (JSONException e1) {
                e1.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };
    protected ErrorListener AccountErrorListener = new ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError err) {
            mProgressDialog.dismiss();
            Toast.makeText(MainActivity.this, "Err " + err.toString(), Toast.LENGTH_LONG).show();
        }
    };
    protected Listener<String> LoginSuccessListener = new Listener<String>() {
        private String DataPassword,UID,minor,UNAME,UUSER,UClass,UStatus;

        @Override
        public void onResponse(String response) {

            try {
                JSONArray array = new JSONArray(response);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    DataPassword = obj.getString("password");
                    //  minor = obj.getString("minor");
                    UID = obj.getString("_id");
                    UNAME = obj.getString("name");
                    UUSER = obj.getString("user");
                    UClass = obj.getString("room");
                    UStatus = obj.getString("sstatus");

                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            } finally {
                mProgressDialog.dismiss();
            }
            //String strPassword = PasswordInput.getEditableText().toString();
            //String strPassword = md5(PasswordInput.getEditableText().toString());
            String strPassword = PasswordInput.getEditableText().toString();
            //將抓下來的密碼與輸入密碼比較
            if (DataPassword.equals(strPassword)&(UUSER.equals("teacher") || UUSER.equals("teacher2"))) {
                mProgressDialog.dismiss();
                Intent intent = new Intent(MainActivity.this, Home_teacherActivity.class);
                intent.setClass(MainActivity.this, Home_teacherActivity.class);
                intent.putExtra("UUSER", UUSER);
                intent.putExtra("UNAME", UNAME);
                intent.putExtra("UClass", UClass);

                startActivity(intent);

            }
            else if (DataPassword.equals(strPassword))
            {

                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                intent.setClass(MainActivity.this, HomeActivity.class);
                intent.putExtra("UUSER", UUSER);
                intent.putExtra("UNAME", UNAME);
                intent.putExtra("UClass", UClass);
                intent.putExtra("UStatus", UStatus);
//            Intent intent2 = new Intent(MainActivity.this, HomeActivity.class);
//            intent2.setClass(MainActivity.this, HomeActivity.class);
//            intent2.putExtra("UNAME", UNAME);
//            Intent intent3 = new Intent(MainActivity.this, HomeActivity.class);
//            intent3.setClass(MainActivity.this, HomeActivity.class);
//            intent3.putExtra("UClass", UClass);

                startActivity(intent);
            }

            else {
                nProgressDialog.show();
            }

        }
    };
//
//    public static String md5(String str)
//    {
//        MessageDigest md5 = null;
//        try
//        {
//            md5 = MessageDigest.getInstance("MD5");
//        }catch(Exception e)
//        {
//            e.printStackTrace();
//            return "";
//        }
//
//        char[] charArray = str.toCharArray();
//        byte[] byteArray = new byte[charArray.length];
//
//        for(int i = 0; i < charArray.length; i++)
//        {
//            byteArray[i] = (byte)charArray[i];
//        }
//        byte[] md5Bytes = md5.digest(byteArray);
//
//        StringBuffer hexValue = new StringBuffer();
//        for( int i = 0; i < md5Bytes.length; i++)
//        {
//            int val = ((int)md5Bytes[i])&0xff;
//            if(val < 16)
//            {
//                hexValue.append("0");
//            }
//            hexValue.append(Integer.toHexString(val));
//        }
//        return hexValue.toString();
//    }


    protected ErrorListener LoginErrorListener = new ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError err) {
            mProgressDialog.dismiss();
            Toast.makeText(MainActivity.this, "Err " + err.toString(), Toast.LENGTH_LONG).show();
        }
    };
    protected ErrorListener LoginAccountErrorListener = new ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError err) {
            mProgressDialog.dismiss();
            Toast.makeText(MainActivity.this, "Err " + err.toString(), Toast.LENGTH_LONG).show();
        }
    };



}