package com.example.user.cram1001;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.user.cram1001.volleymgr.NetworkManager;

public class Billboard_teacherActivity extends AppCompatActivity {

    private ListView mListView;
    private ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billboard_teacher);

        mListView = (ListView) findViewById(R.id.list1105);
        mListView.setOnItemClickListener(mOnItemClickListener);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Wait...");

        queryTodoList();


        Button button = (Button) findViewById(R.id.buttonaddbill);//取得按鈕
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Billboard_teacherActivity.this, AddBillboardActivity.class);
                startActivity(intent);
            }
        });//將這個Listener綑綁在這個Button


    }

    private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            final Todo todo = (Todo) parent.getAdapter().getItem(position);
            AlertDialog.Builder builder = new AlertDialog.Builder(Billboard_teacherActivity.this);
           //builder.setMessage(todo.content);
            builder.setPositiveButton("關閉", null);
            builder.setNegativeButton("刪除", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    StringRequest request = new StringRequest(Request.Method.GET, "https://cramschoollogin.herokuapp.com/api/deletebill?_id=" + todo._id, mOnDeleteSuccessListener, mOnErrorListener);
                    NetworkManager.getInstance(Billboard_teacherActivity.this).request(null, request);
                }
            });
            builder.show();
        }
    };


    private void queryTodoList() {
        mProgressDialog.show();
        StringRequest request = new StringRequest(Request.Method.GET, "https://cramschoollogin.herokuapp.com/api/querybillboard", mOnQuerySuccessListener, mOnErrorListener);
        NetworkManager.getInstance(this).request(null, request);
    }

    private Listener<String> mOnQuerySuccessListener = new Listener<String>() {
        @Override
        public void onResponse(String response) {
            mProgressDialog.dismiss();
            try {
                JSONArray ary = new JSONArray(response);
                int length = ary.length();

                ArrayList<Todo> datas = new ArrayList<Todo>();

                for (int i = 0; i < length; i++) {
                    JSONObject obj = ary.getJSONObject(i);
                    Todo todo = new Todo();
                    todo._id = obj.getString("_id");
                    todo.title = obj.getString("title");
                    todo.date = obj.getString("date");
                    todo.content = obj.getString("content");
                    datas.add(todo);
                }

                TodoListAdapter adapter = new TodoListAdapter(Billboard_teacherActivity.this, datas);
                mListView.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private Listener<String> mOnDeleteSuccessListener = new Listener<String>() {

        @Override
        public void onResponse(String response) {
            mProgressDialog.dismiss();
            queryTodoList();
        }
    };

    private ErrorListener mOnErrorListener = new ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError err) {
            mProgressDialog.dismiss();
            Toast.makeText(Billboard_teacherActivity.this, err.toString(), Toast.LENGTH_LONG).show();
        }
    };

    class Todo {
        String _id;
        String title;
        String date;
        String content;
    }

    class TodoListAdapter extends BaseAdapter {

        private Context mContext;
        private ArrayList<Todo> mData;

        TodoListAdapter(Context context, ArrayList<Todo> data) {
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
                convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter, null);
            }

            Todo todo = (Todo) getItem(position);

            TextView title = (TextView) convertView.findViewById(R.id.texttitle);
            title.setText(todo.title);

            TextView date = (TextView) convertView.findViewById(R.id.textdate);
            date.setText(todo.date);

            TextView content = (TextView) convertView.findViewById(R.id.textcontent);
            content.setText(todo.content);

            return convertView;
        }

    }
}
