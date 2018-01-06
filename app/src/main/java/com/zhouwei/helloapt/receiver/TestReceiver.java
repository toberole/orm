package com.zhouwei.helloapt.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by zhouwei on 2018/1/6.
 */

public class TestReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("AAAA", "Receiver: " + this);
        Log.i("AAAA", "onReceive Thread name: " + Thread.currentThread().getName());
    }
}
