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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class AddBillboardActivity extends AppCompatActivity {
    private EditText DateInput,TitleInput,ContentInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_billboard);
        DateInput = (EditText) findViewById(R.id.editdate);
        TitleInput = (EditText) findViewById(R.id.edittitle);
        ContentInput = (EditText) findViewById(R.id.editcontent);

        Button AddBillButton = (Button) findViewById(R.id.buttonOK);
        AddBillButton.setOnClickListener(AddBillListener);

        /*AddBillButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(AddBillboardActivity.this, BillboardActivity.class);
                AddBillboardActivity.this.startActivity(intent);
            }
        }); */
    }

    private View.OnClickListener AddBillListener = new View.OnClickListener() {

        public void onClick(View v) {

            try {

                //擷取三項資料後上傳DB
                String strDate = URLEncoder.encode(DateInput.getEditableText().toString(), "UTF-8");
                String strTitle = URLEncoder.encode(TitleInput.getEditableText().toString(), "UTF-8");
                String strContent = URLEncoder.encode(ContentInput.getEditableText().toString(), "UTF-8");

                String url = "https://cramschoollogin.herokuapp.com/api/insertbb?date=" + strDate + "&title=" + strTitle +  "&content=" + strContent ;
                StringRequest request = new StringRequest(Request.Method.GET, url, mOnAddSuccessListener, mOnErrorListener);
                NetworkManager.getInstance(AddBillboardActivity.this).request(null, request);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    };

    protected Response.Listener<String> mOnAddSuccessListener = new Response.Listener<String>() {

        @Override
        public void onResponse(String response) {

            DateInput.setText("");
            TitleInput.setText("");
            ContentInput.setText("");
            Toast.makeText(AddBillboardActivity.this, "新增成功", Toast.LENGTH_LONG).show();

            //結束頁面
            AddBillboardActivity.this.finish();

        }
    };

    protected Response.ErrorListener mOnErrorListener = new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError err) {

            Toast.makeText(AddBillboardActivity.this, "Err " + err.toString(), Toast.LENGTH_LONG).show();
        }
    };

}