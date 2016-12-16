package com.example.user.cram1001;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.example.user.cram1001.Adapter.ContentCheck;
import com.example.user.cram1001.Adapter.MyAdapterCheck;
import com.example.user.cram1001.Fcm.GCMUtils;
import com.example.user.cram1001.volleymgr.NetworkManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.URLEncoder;
import java.util.ArrayList;


public class CheckActivity extends AppCompatActivity {

    private BluetoothAdapter mBluetoothAdapter; //宣告一個要放入藍芽資訊的堆疊

    private ArrayList<ContentCheck> contentCheck = new ArrayList<ContentCheck>();
    private ListView listView;
    private MyAdapterCheck myAdapter;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_check);
        setContentView(R.layout.list_check);

        StringRequest request = new StringRequest(Request.Method.GET, "https://cramschoollogin.herokuapp.com/api/querystudentname", mResponseListener, mErrorListener);
        NetworkManager.getInstance(this).request(null, request);

        listView = (ListView) findViewById(R.id.listviewcheck);
        myAdapter = new MyAdapterCheck(this, contentCheck);
        listView.setAdapter(myAdapter);
        listView.setOnItemLongClickListener(NotifiListener);
        //mHandler1.postDelayed(mDetectRunnable1, 30000);

        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE); //宣告藍芽資訊接收管理
        mBluetoothAdapter = bluetoothManager.getAdapter(); //將接收的資訊放入堆疊
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {  //堆疊是空的 or 堆疊disabled
            Intent enableBluetooth = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 1);
        }
        mBluetoothAdapter.startLeScan(mLeScanCallback); //開始接收藍芽資訊

    }

    private Response.ErrorListener mErrorListener = new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("Error", error.toString());
        }
    };

    protected Response.Listener<String> mResponseListener = new Response.Listener<String>() {
        private String NName, RRoom, RRegid;

        @Override
        public void onResponse(String response) {

            try {
                JSONArray array = new JSONArray(response);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);

                    NName = obj.getString("name");
                    RRoom = obj.getString("room");
                    ContentCheck contentC = new ContentCheck(NName, RRoom, "");
                    contentC.regid = obj.getString("regid");
                    contentCheck.add(contentC);
                }
                myAdapter.notifyDataSetChanged();
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
    };

    private AdapterView.OnItemLongClickListener NotifiListener = new AdapterView.OnItemLongClickListener(){
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
            final ContentCheck contentC = (ContentCheck) parent.getAdapter().getItem(position);
            new AlertDialog.Builder(CheckActivity.this)
                    .setMessage("是否通知家長?")
                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                        private String SStatus;
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // String strmsg = URLEncoder.encode(MSG);
                            String url = "https://cramschoollogin.herokuapp.com/api/sendfcm?to=" + contentC.regid;
                            StringRequest request = new StringRequest(Request.Method.GET, url, mOnAddSuccessListener, mOnErrorListener);
                            NetworkManager.getInstance(CheckActivity.this).request(null, request);

                            SStatus = "小孩已安全到達安親班囉！";
                            String strstatus = URLEncoder.encode(SStatus);
                            String url2 = "https://cramschoollogin.herokuapp.com/api/insertstatus?regid=" + contentC.regid + "&sstatus=" + strstatus ;
                            StringRequest request2 = new StringRequest(Request.Method.GET, url2, mOnAddSuccessListener, mOnErrorListener);
                            NetworkManager.getInstance(CheckActivity.this).request(null, request2);
                        }
                    })

                    .setNegativeButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();

            return false;
        }

    };
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {   //接收到藍芽資訊然後針對接收到的資料進行解析
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi,
                             final byte[] scanRecord) {
            int startByte = 2;
            boolean patternFound = false;
            //ibeacon
            while (startByte <= 5) {
                if (((int) scanRecord[startByte + 2] & 0xff) == 0x02 && // Identifies
                        // an
                        // iBeacon
                        ((int) scanRecord[startByte + 3] & 0xff) == 0x15) { // Identifies
                    // correct
                    // data
                    // length
                    patternFound = true;
                    break;
                }
                startByte++;
            }
            // 收到beacon資料
            if (patternFound) {
                // uuid 16位元
                byte[] uuidBytes = new byte[16];
                System.arraycopy(scanRecord, startByte + 4, uuidBytes, 0, 16);
                String hexString = bytesToHex(uuidBytes);

                // ibeaconUUID
                String uuid = hexString.substring(0, 8) + "-"
                        + hexString.substring(8, 12) + "-"
                        + hexString.substring(12, 16) + "-"
                        + hexString.substring(16, 20) + "-"
                        + hexString.substring(20, 32);

                // ibeaconmajor id
                int major = (scanRecord[startByte + 20] & 0xff) * 0x100
                        + (scanRecord[startByte + 21] & 0xff);

                // ibeaconminor id
                int minor = (scanRecord[startByte + 22] & 0xff) * 0x100
                        + (scanRecord[startByte + 23] & 0xff);

                String stringValue = Integer.toString(minor);

                String ibeaconName = device.getName();
                String mac = device.getAddress();

                int txPower = (scanRecord[startByte + 24]);
                Log.d("BLE",bytesToHex(scanRecord));
                Log.d("BLE", "Name:" + ibeaconName + "\nMac:" + mac
                        + " \nUUID:" + uuid + "\nMajor:" + major + "\nMinor:"
                        + minor + "\nTxPower:" + txPower + "\nrssi:" + rssi);

                Log.d("BLE","distance:"+calculateAccuracy(txPower,rssi));
                mHandler.postDelayed(mDetectRunnable, 1000);

                //mCheckBox.setChecked(false);
                //mCheckBox1.setChecked(false);

                switch (minor) {
                    case 8:
                        ContentCheck chk = (ContentCheck) myAdapter.getItem(0);
                        chk.textcheck = "ARRIVE";
                        myAdapter.notifyDataSetChanged();
                        break;
                    case 12:
                        ContentCheck chk1 = (ContentCheck) myAdapter.getItem(1);
                        chk1.textcheck = "ARRIVE";
                        myAdapter.notifyDataSetChanged();
                        break;
                    case 9:
                        ContentCheck chk2 = (ContentCheck) myAdapter.getItem(2);
                        chk2.textcheck = "ARRIVE";
                        myAdapter.notifyDataSetChanged();
                        break;
                    case 1:
                        ContentCheck chk3 = (ContentCheck) myAdapter.getItem(3);
                        chk3.textcheck = "ARRIVE";
                        myAdapter.notifyDataSetChanged();
                        break;
                    case 13:
                        ContentCheck chk4 = (ContentCheck) myAdapter.getItem(4);
                        chk4.textcheck = "ARRIVE";
                        myAdapter.notifyDataSetChanged();
                        break;
                    case 10:
                        ContentCheck chk5 = (ContentCheck) myAdapter.getItem(5);
                        chk5.textcheck = "ARRIVE";
                        myAdapter.notifyDataSetChanged();
                        break;
                    default:
                        break;
                }
            }


        }
    };

    protected Response.Listener<String> mOnAddSuccessListener = new Response.Listener<String>() {

        @Override
        public void onResponse(String response) {
            Toast.makeText(CheckActivity.this, "通知已送出", Toast.LENGTH_LONG).show();
        }
    };

    protected Response.ErrorListener mOnErrorListener = new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError err) {
            Toast.makeText(CheckActivity.this, "Err " + err.toString(), Toast.LENGTH_LONG).show();
        }
    };
    private Handler mHandler = new Handler() {
    };
    private Handler mHandler1 = new Handler() {

    };
    private Runnable mDetectRunnable = new Runnable() {  //監聽器

        @SuppressLint("NewApi")
        @Override
        public void run() {
            // TODO detect logic
            mHandler.postDelayed(this,1000);
        }

    };



    static final char[] hexArray = "0123456789ABCDEF".toCharArray();

    private static String bytesToHex(byte[] bytes) {  //位元轉換
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    protected static double calculateAccuracy(int txPower, double rssi) {   //計算距離 (沒記錯的話)
        if (rssi == 0) {
            return -1.0; // if we cannot determine accuracy, return -1.
        }

        double ratio = rssi * 1.0 / txPower;
        if (ratio < 1.0) {
            return Math.pow(ratio, 10);
        } else {
            double accuracy = (0.89976) * Math.pow(ratio, 7.7095) + 0.111;
            return accuracy;
        }
    };

}