package com.zhouwei.helloapt.bean;

import android.content.ContentValues;

import com.zhouwei.DBEntity;
import com.zhouwei.Property;
import com.zhouwei.helloapt.db.Saveable;


/**
 * Created by zhouwei on 2017/12/23.
 */

@DBEntity
public class APPLog extends Saveable {
    @Property
    private String tag;

    @Property
    private String msg;

    @Property
    private String time;

    @Property
    private String thread;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getThread() {
        return thread;
    }

    public void setThread(String thread) {
        this.thread = thread;
    }

    @Override
    public void save(ContentValues values) {
        super.save(values);
    }

    @Override
    public void newInstance(ContentValues values) {
        setTag(values.get("tag") + "");
        setTime(values.get("time") + "");
        setThread(values.get("thread") + "");
        setMsg(values.get("msg") + "");
    }

    @Override
    public String toString() {
        return "APPLog{" +
                "tag='" + tag + '\'' +
                ", msg='" + msg + '\'' +
                ", time='" + time + '\'' +
                ", thread='" + thread + '\'' +
                '}';
    }
}
