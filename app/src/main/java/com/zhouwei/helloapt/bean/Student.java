package com.zhouwei.helloapt.bean;

import android.content.ContentValues;
import android.util.Log;

import com.zhouwei.AutoIncrement;
import com.zhouwei.DBEntity;
import com.zhouwei.Property;
import com.zhouwei.helloapt.db.Saveable;

/**
 * Created by zhouwei on 2017/12/23.
 */

@DBEntity
public class Student extends Saveable {
    @AutoIncrement
    private int id;

    @Property
    private String name;

    @Property
    private String gender;

    @Property
    private int age;

    public Student() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public void save(ContentValues values) {
        super.save(values);
        values.remove("id");
    }

    @Override
    public void newInstance(ContentValues values) {
        try {
            setId(Integer.parseInt(values.get("id") + ""));
            setName(values.get("name") + "");
            setAge(Integer.parseInt(values.get("age") + ""));
            setGender(values.get("gender") + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                '}';
    }

    public class SS {
    }
}
