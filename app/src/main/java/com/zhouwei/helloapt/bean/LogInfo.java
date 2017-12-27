package com.zhouwei.helloapt.bean;

import android.content.ContentValues;

import com.zhouwei.DBEntity;
import com.zhouwei.Property;
import com.zhouwei.helloapt.db.Saveable;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by zhouwei on 2017/12/26.
 */
@DBEntity
public class LogInfo extends Saveable {
    private static final String FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final SimpleDateFormat formater = new SimpleDateFormat(FORMAT);

    @Property
    private String tag;

    @Property
    private String msg;

    @Property
    private String thread;

    @Property
    private String time;

    @Property
    private String formatTime;

    @Property
    private String extraInfo;

    public String getFormatTime() {
        return formatTime;
    }

    public void setFormatTime(String formatTime) {
        this.formatTime = formatTime;
    }

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

    public String getThread() {
        return thread;
    }

    public void setThread(String thread) {
        this.thread = thread;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    @Override
    public void newInstance(ContentValues values) {
        setTag(String.valueOf(values.get("tag")));
        setMsg(String.valueOf(values.get("msg")));
        setThread(String.valueOf(values.get("thread")));

        setTime(String.valueOf(values.get("time")));
        setFormatTime(String.valueOf(values.get("formatTime")));

        setExtraInfo(String.valueOf(values.get("extraInfo")));
    }

    @Override
    public void save(ContentValues values) {
        values.put("tag", tag);
        values.put("msg", msg);
        values.put("thread", thread);

        Date date = new Date();
        values.put("time", date.getTime() + "");
        values.put("formatTime", formater.format(date));

        values.put("extraInfo", extraInfo);
    }

    @Override
    public String toString() {
        return "LogInfo{" +
                "tag='" + tag + '\'' +
                ", msg='" + msg + '\'' +
                ", thread='" + thread + '\'' +
                ", time='" + time + '\'' +
                ", extraInfo='" + extraInfo + '\'' +
                ", formatTime='" + formatTime + '\'' +
                '}';
    }
}
