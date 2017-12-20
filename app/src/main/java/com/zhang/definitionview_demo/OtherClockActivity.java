package com.zhang.definitionview_demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class OtherClockActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn1;

    private Button floatBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_clock);
        initView();
    }

    private void initView() {
        btn1 = (Button) findViewById(R.id.btn1);

        btn1.setOnClickListener(this);
        floatBtn = (Button) findViewById(R.id.floatBtn);
        floatBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btn1:
            Toast.makeText(this, "点击", Toast.LENGTH_SHORT).show();
            break;
        case R.id.floatBtn:
            Toast.makeText(this, "点击floatBtn", Toast.LENGTH_SHORT).show();
            break;
        }
    }
}
