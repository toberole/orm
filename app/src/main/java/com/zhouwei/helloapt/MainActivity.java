package com.zhouwei.helloapt;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.zhouwei.helloapt.bean.APPLog;
import com.zhouwei.helloapt.bean.Student;
import com.zhouwei.helloapt.db.DaoHelper;

import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private android.widget.Button add;
    private android.widget.Button delete;
    private android.widget.Button update;
    private android.widget.Button get;
    private android.widget.Button getPart;
    private android.widget.Button saveLog;
    private android.widget.Button showLog;
    private android.widget.Button deleteLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.getPart = (Button) findViewById(R.id.getPart);
        this.get = (Button) findViewById(R.id.get);
        this.update = (Button) findViewById(R.id.update);
        this.delete = (Button) findViewById(R.id.delete);
        this.add = (Button) findViewById(R.id.add);

        add.setOnClickListener(this);
        delete.setOnClickListener(this);
        update.setOnClickListener(this);
        get.setOnClickListener(this);
        getPart.setOnClickListener(this);

        this.deleteLog = (Button) findViewById(R.id.deleteLog);
        this.showLog = (Button) findViewById(R.id.showLog);
        this.saveLog = (Button) findViewById(R.id.saveLog);

        saveLog.setOnClickListener(this);
        delete.setOnClickListener(this);
        showLog.setOnClickListener(this);

        Student student = new Student();

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.saveLog) {
            APPLog appLog = new APPLog();
            appLog.setMsg("saveLog");
            appLog.setTag("saveLog Tag");
            appLog.setTime(System.currentTimeMillis() + "");
            appLog.setThread(Thread.currentThread().getName());
            DaoHelper.getInstance().save(appLog);
        } else if (v.getId() == R.id.showLog) {
            List<APPLog> appLogs = DaoHelper.getInstance().get(APPLog.class);
            if (appLogs != null) {
                for (int i = 0; i < appLogs.size(); i++) {
                    Log.i("AAAA", appLogs.get(i).toString());
                }
            }
        } else if (v.getId() == R.id.add) {
            Student student = new Student();
            student.setAge(11);
            student.setName("xiaogong");
            student.setGender("men");

            DaoHelper.getInstance().save(student);
        } else if (v.getId() == R.id.get) {
            List<Student> students = DaoHelper.getInstance().get(Student.class);
            if (students != null) {
                for (int i = 0; i < students.size(); i++) {
                    Log.i("AAAA", students.get(i).toString());
                }
            }
        }

    }
}
