package com.example.user.cram1001;

import android.app.ProgressDialog;
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class CreateMemberActivity extends AppCompatActivity {

    private EditText AccountInput,PasswordInput,NameInput,MinorInput,ClassInput;
    private ProgressDialog mProgressDialog;
    private String RegID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_member);

        AccountInput = (EditText) findViewById(R.id.createid);
        PasswordInput = (EditText) findViewById(R.id.createpwd);
        NameInput = (EditText) findViewById(R.id.createname);
        MinorInput = (EditText) findViewById(R.id.beaconminor);
        ClassInput = (EditText)findViewById(R.id.createrclass);

        Button CreateButton = (Button) findViewById(R.id.test);
        CreateButton.setOnClickListener(CreateMemberListener);
/*
        String currentRegId = getGcmRegId();
        if (TextUtils.isEmpty(currentRegId)) {
            registration();
        }*/

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Wait...");

    }

    /*
    // ----------------- 測試GCM
    public String getGcmRegId() {
        return PreferenceManager.getDefaultSharedPreferences(this).getString("registration_id", null);
    }
    public void registration() {
        GCMRegistrationTask task = new GCMRegistrationTask();
        task.execute();
    }
    private class GCMRegistrationTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            Log.d(TAG, "Registering");
            GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
            try {
                return gcm.register(SENDER_ID);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                RegID = result;
            }
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////
    */
    private View.OnClickListener CreateMemberListener = new View.OnClickListener() {
        private String AddRegid,AddStatus;
        public void onClick(View v) {

            try {
                AddRegid = "0";
                AddStatus = "孩子尚未抵達安親班唷";
                //螢幕擷取三項資料後上傳DB
                String strAccount = URLEncoder.encode(AccountInput.getEditableText().toString(), "UTF-8");
                String strPassword = URLEncoder.encode(PasswordInput.getEditableText().toString(), "UTF-8");
                String strName = URLEncoder.encode(NameInput.getEditableText().toString(), "UTF-8");
                String strminor = URLEncoder.encode(MinorInput.getEditableText().toString(), "UTF-8");
                String strclass = URLEncoder.encode(ClassInput.getEditableText().toString(), "UTF-8");
                String strregid = URLEncoder.encode(AddRegid.toString(), "UTF-8");
                String strstatus = URLEncoder.encode(AddStatus.toString(), "UTF-8");
                mProgressDialog.show();

                String url = "https://cramschoollogin.herokuapp.com/api/insert?user=" + strAccount + "&password=" + strPassword +  "&name=" + strName + "&minor=" + strminor +"&room=" + strclass + "&regid=" + strregid + "&status=" + strstatus;
                StringRequest request = new StringRequest(Request.Method.GET, url, mOnAddSuccessListener, mOnErrorListener);
                NetworkManager.getInstance(CreateMemberActivity.this).request(null, request);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    };

    protected Response.Listener<String> mOnAddSuccessListener = new Response.Listener<String>() {

        @Override
        public void onResponse(String response) {
            mProgressDialog.dismiss();
            AccountInput.setText("");
            PasswordInput.setText("");
            NameInput.setText("");
            ClassInput.setText("");
            Toast.makeText(CreateMemberActivity.this, "新增成功", Toast.LENGTH_LONG).show();

            //結束頁面
            CreateMemberActivity.this.finish();
        }
    };

    protected Response.ErrorListener mOnErrorListener = new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError err) {
            mProgressDialog.dismiss();
            Toast.makeText(CreateMemberActivity.this, "Err " + err.toString(), Toast.LENGTH_LONG).show();
        }
    };



}

