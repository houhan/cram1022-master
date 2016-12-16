package com.example.user.cram1001;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class PublicActivity extends AppCompatActivity {
    private Button button, dialogButton;
    private String UNAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_position_public);

        Intent intent = this.getIntent();
        UNAME = intent.getStringExtra("UNAME");

        button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(PublicActivity.this, MapsActivity.class);
                intent.putExtra("UNAME", UNAME);

                PublicActivity.this.startActivity(intent);
            }
        });//將這個Listener綑綁在這個Button

        dialogButton = (Button) findViewById(R.id.button3);//取得按鈕
//        dialogButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent();
//                intent.setClass(PublicActivity.this, ArrivetimeActivity.class);
//                intent.putExtra("UNAME", UNAME);
//
//                PublicActivity.this.startActivity(intent);
//            }
//        });//將這個Listener綑綁在這個Button
        final AlertDialog alertDialog = getAlertDialog("若選擇不公開，老師將無法得知您預計到達的時間！", "確定不公開？");
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                alertDialog.show();
            }
        });//將這個Listener綑綁在這個Button

    }

    private AlertDialog getAlertDialog(String title,String message){
        //產生一個Builder物件
        Builder builder = new AlertDialog.Builder(PublicActivity.this);
        //設定Dialog的標題
        builder.setTitle(title);
        //設定Dialog的內容
        builder.setMessage(message);
        //設定Positive按鈕資料
        builder.setPositiveButton("確認", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //按下按鈕時顯示快顯
                Toast.makeText(PublicActivity.this, "已退出", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(PublicActivity.this, HomeActivity.class);
                PublicActivity.this.finish();
            }
        });
        //設定Negative按鈕資料
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //按下按鈕時顯示快顯
                Toast.makeText(PublicActivity.this, "取消", Toast.LENGTH_SHORT).show();
            }
        });
        //利用Builder物件建立AlertDialog
        return builder.create();
    }
}
