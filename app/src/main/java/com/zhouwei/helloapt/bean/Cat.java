package com.zhouwei.helloapt.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhouwei on 2018/1/3.
 */

public class Cat implements Parcelable {
    public String name;
    public int age;
    public String gender;

    public Cat() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.age);
        dest.writeString(this.gender);
    }

    protected Cat(Parcel in) {
        this.name = in.readString();
        this.age = in.readInt();
        this.gender = in.readString();
    }

    public static final Parcelable.Creator<Cat> CREATOR = new Parcelable.Creator<Cat>() {
        @Override
        public Cat createFromParcel(Parcel source) {
            return new Cat(source);
        }

        @Override
        public Cat[] newArray(int size) {
            return new Cat[size];
        }
    };
}
