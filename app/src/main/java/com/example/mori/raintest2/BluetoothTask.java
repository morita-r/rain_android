package com.example.mori.raintest2;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by mori on 2016/08/31.
 */
public class BluetoothTask {
    private BluetoothAdapter Bt = BluetoothAdapter.getDefaultAdapter();
    private static final int REQUEST_ENABLE_BLUETOOTH = 10;
    Context mContext;

    public BluetoothTask(Context context) {
        Log.d("task", "呼んだよ");
        this.mContext = context;
    }

    public void TaskGO(){
        Log.d("task","TaskGO!");
        Bt = Bt.getDefaultAdapter();
        if (Bt == null) {
            Log.d("","null");
            //Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
//            finish(); not Activity so can not use.
            return;
        }

        boolean btEnable = Bt.isEnabled();

        if(btEnable == true){
            //BluetoothがONだった場合の処理
            Log.d("task","Bluetooth ON");
//            Intent intent = new Intent(this,DeviceListActivity.class);
//            startActivity(intent);
        }else{
            Log.d("task","Bluetooth OFF");
            //OFFだった場合、ONにすることを促すダイアログを表示する画面に遷移
            Intent btOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(btOn, REQUEST_ENABLE_BLUETOOTH);
        }
    }

    protected void onActivityResult(int requestCode, int ResultCode, Intent date){
/*
        if(requestCode == REQUEST_ENABLE_BLUETOOTH){

            if(ResultCode == Activity.RESULT_OK){
                //BluetoothがONにされた場合の処理
                Log.d("","BluetoothをONにしてもらえました。");

                Intent intent = new Intent(this,DeviceListActivity.class);
                startActivity(intent);

            }else{
                Log.d("","BluetoothがONにしてもらえませんでした。");
                finish();
            }
        }
*/
    }







}
