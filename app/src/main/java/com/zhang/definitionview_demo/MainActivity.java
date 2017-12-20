package com.zhang.definitionview_demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.bigkoo.pickerview.TimePickerView;
import com.zhang.definitionview_demo.view.PathActivity;
import com.zhang.definitionview_demo.viewgroup.ListActivity;

import java.util.Calendar;
import java.util.Date;

/**
 * 开个坑，撸一遍自定义View
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button miClockBtn;

    private Button bezierBtn;

    private Button camera3DBtn;

    private Button pathBtn;

    private Button clockBtn;

    private Button listBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
        // WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        initView();
    }

    private void initView() {
        miClockBtn = (Button) findViewById(R.id.miClockBtn);
        bezierBtn = (Button) findViewById(R.id.bezierBtn);
        camera3DBtn = (Button) findViewById(R.id.camera3DBtn);
        clockBtn = (Button) findViewById(R.id.clockBtn);
        listBtn = (Button) findViewById(R.id.listBtn);

        miClockBtn.setOnClickListener(this);
        bezierBtn.setOnClickListener(this);
        camera3DBtn.setOnClickListener(this);
        pathBtn = (Button) findViewById(R.id.pathBtn);
        pathBtn.setOnClickListener(this);
        clockBtn.setOnClickListener(this);
        listBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.miClockBtn:
            startActivity(new Intent(this, MiClockActivity.class));
            break;
        case R.id.bezierBtn:
            startActivity(new Intent(this, BezierActivity.class));
            break;
        case R.id.camera3DBtn:
            // startActivity(new Intent(this, Camera3DActivity.class));
            // 时间选择器
            TimePickerView pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {// 选中事件回调
                    // tvTime.setText(getTime(date));
                }
            }).setType(TimePickerView.Type.YEAR_MONTH_DAY).setDate(Calendar.getInstance())
                    .setLabel("", "", "", "", "", "").build();
            pvTime.show();
            break;
        case R.id.pathBtn:
            startActivity(new Intent(this, PathActivity.class));
            break;
        case R.id.clockBtn:
            startActivity(new Intent(this, OtherClockActivity.class));
            break;
        case R.id.listBtn:
            startActivity(new Intent(this, ListActivity.class));
            break;
        }
    }
}
