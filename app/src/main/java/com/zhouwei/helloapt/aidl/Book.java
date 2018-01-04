package com.zhouwei.helloapt.aidl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhouwei on 2018/1/3.
 */

public class Book implements Parcelable {
    public String name;
    public String des;
    public int price;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.des);
        dest.writeInt(this.price);
    }

    public Book() {
    }

    protected Book(Parcel in) {
        this.name = in.readString();
        this.des = in.readString();
        this.price = in.readInt();
    }

    public static final Parcelable.Creator<Book> CREATOR = new Parcelable.Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
}
