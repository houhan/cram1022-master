package com.example.user.cram1001;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Home_teacherActivity extends AppCompatActivity {
    private String UClass,UNAME,UUSER;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_teacher);
//抓取上一頁資料
        Intent intent = this.getIntent();
        UClass = intent.getStringExtra("UClass");
        UNAME = intent.getStringExtra("UNAME");
        UUSER = intent.getStringExtra("UUSER");

        Button buttonbillboardteacher = (Button) findViewById(R.id.billboardteacher);//取得按鈕
        buttonbillboardteacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Home_teacherActivity.this, Billboard_teacherActivity.class);
                Home_teacherActivity.this.startActivity(intent);
            }
        });//將這個Listener綑綁在這個Button

        Button buttoncheck = (Button) findViewById(R.id.attendanceteacher);//取得按鈕
        buttoncheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Home_teacherActivity.this, CheckActivity.class);
//                intent.putExtra("UNAME", UNAME);
//                intent.putExtra("UClass", UClass);
//                intent.putExtra("UUSER", UUSER);
                startActivity(intent);
            }
        });

        Button buttonqk = (Button) findViewById(R.id.qkteacher);//取得按鈕
        buttonqk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Home_teacherActivity.this, Qk_teacherActivity.class);
                Home_teacherActivity.this.startActivity(intent);
            }
        });

        Button buttoncreat = (Button) findViewById(R.id.creatmember);//取得按鈕
        buttoncreat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Home_teacherActivity.this, CreateMemberActivity.class);
                Home_teacherActivity.this.startActivity(intent);
            }
        });

        Button buttongohome = (Button) findViewById(R.id.Catchteacher);//取得按鈕
        buttongohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Home_teacherActivity.this, NotifigohomeActivity.class);
                Home_teacherActivity.this.startActivity(intent);
            }
        });
    }
}