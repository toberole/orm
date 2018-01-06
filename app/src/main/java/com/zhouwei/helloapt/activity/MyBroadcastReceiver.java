package com.zhouwei.helloapt.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by zhouwei on 2018/1/6.
 */

public class MyBroadcastReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("AAAA", "MyBroadcastReceiver: " + this);
        Log.i("AAAA", "MyBroadcastReceiver onReceive Thread name: " + Thread.currentThread().getName());
    }
}
