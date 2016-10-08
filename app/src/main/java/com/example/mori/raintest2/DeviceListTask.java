package com.example.mori.raintest2;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by mori on 2016/08/31.
 */
public class DeviceListTask {
    public boolean hantei = true;
    private ListView mListView;
    private ArrayAdapter<String> mAdapter;
    private ArrayList<String> mList;
    private ArrayList<String> mList2;
    private BluetoothAdapter mBluetoothAdapter;
    static volatile boolean runFlag = true;
    private int count = 1;
    private Context mContext;


    public DeviceListTask(Context context){
        this.mContext = context;
        mList = new ArrayList<String>();
        mList2 = new ArrayList<String>();

    }

    public void taskGO(){
        IntentFilter bluetoothFilter = new IntentFilter();
        bluetoothFilter.addAction(BluetoothDevice.ACTION_NAME_CHANGED);
        bluetoothFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        bluetoothFilter.addAction(BluetoothDevice.ACTION_FOUND);
        mContext.registerReceiver(mBluetoothSearchReceiver, bluetoothFilter);

        mBluetoothAdapter = mBluetoothAdapter.getDefaultAdapter();

        final Thread t = new Thread(new Runnable() {
            public void run() {

                while(runFlag){
                    try {
                        // ここに繰り返し処理を書く
                        Log.d("action.","a");
                        mBluetoothAdapter.startDiscovery(); //検索開始
                        Thread.sleep(12000);
                        mBluetoothAdapter.cancelDiscovery();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        //onDestroy();
                    }
                }
            }
        });
        t.start();
    }

    private BroadcastReceiver mBluetoothSearchReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(final Context context, Intent intent) {
            mContext = context;
            if(BluetoothDevice.ACTION_FOUND.equals(intent.getAction())){
                Log.d("a","~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                // 取得したbluetooth情報を取得
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(device.getName()!=null){
                    //&& device.getName().startsWith("BLE")) {
                    // mAdapter.clear();
                    String st = device.getName();
                   /* Log.d("a","1");
                    mAdapter.add(device.getName());
                    Log.d("a","2");
                    mAdapter.add(device.getAddress());*/
                    if(mList.contains(st)!=true) {
                        Log.d("a", "3");
                        mList.add(device.getName());
                        Log.d("a", "4");
                        mList.add(device.getAddress());
                    }
                }
                Log.d("a","5");
//                mAdapter.notifyDataSetChanged();
                Log.d("a","6");
//                mListView.smoothScrollToPosition(mAdapter.getCount());
            }

            //検索始まるとき
            /*if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(intent.getAction())){

            }*/

            //検索終了のとき
            if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(intent.getAction())&&mList.size()!=0){
                Log.d("a","owari");

                if(count==1){
                    for(String s:mList){
                        mList2.add(s);
                    }
                    count++;
                }

                ArrayList<String>tempmList = new ArrayList<>(mList);
                ArrayList<String>tempmList2 = new ArrayList<>(mList2);
                ArrayList<String>tempmList3 = new ArrayList<>(mList);
                ArrayList<String>tempmList4 = new ArrayList<>(mList2);

                boolean a = tempmList.removeAll(tempmList2);
                boolean b = tempmList4.removeAll(tempmList3);
                if(a!=false) {
                    Log.d("aaaaa", "mListremove");
                    for (int i=0;i<tempmList.size();i++) {
                        if(tempmList.get(i).startsWith("BLE")) {

                            //ここに現れたデバイスは新しくキャッチした別デバイス！
                            //位置情報とMACアドレス送る
                            Log.d("new name", tempmList.get(i));
                            Log.d("new address",tempmList.get(i+1));
                        }
                    }
                }


                if(b!=false){
                    Log.d("bbbbb","mList2remove");

                    for(String s:tempmList4){
                        if(s.equals("BLESerial2")){
                            handler.sendEmptyMessage(0);
/*                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //ここにマッチしたときポップアップする
                                    //表示するメッセージを作成
                                    handler.sendEmptyMessage(0);
                                }
                            });
*/
                            Log.d("vanish",s);
                        }
                    }

                }
                mList2.clear();
                for(String s:mList){
                    //if(mList2.contains(s)!=true) {
                    mList2.add(s);
                    //}
                }
                //mList2 = new ArrayList<>(mList);
                mList.clear();
//                mAdapter.clear();
            }

        }
    };

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            // Toast.makeText(mContext,"れいんちゃん、パクられたんとちゃうか?",Toast.LENGTH_LONG).show();
            new AlertDialog.Builder(mContext)
                    .setTitle("ヤバイ！！！")
                    .setMessage("れいんちゃん、パクられたんとちゃうか?")
                    .setPositiveButton("OK", null)
                    .show();

        }
    };

}
