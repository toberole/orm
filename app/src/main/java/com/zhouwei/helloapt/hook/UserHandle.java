package com.zhouwei.helloapt.hook;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Created by zhouwei on 2017/12/26.
 */

public class UserHandle implements Handler.Callback {
    private Handler originHandler;
    private int action;

    public UserHandle(Handler handler, int action) {
        this.originHandler = handler;
        this.action = action;
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == action) {
            Log.i("[app]", "Activity start");
        }
        originHandler.handleMessage(msg);
        return false;
    }
}
