package com.zhouwei.helloapt.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Charles on 2018/1/25.
 */

public class Data {
    public static final List<TestData> datas = new ArrayList<>();

    static {
        for (int i = 0; i < 30; i++) {
            TestData data = new TestData();
            data.name = "student " + i;
            data.age = i + "";
            datas.add(data);
        }
    }
}
