package com.zhang.definitionview_demo.viewgroup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SimpleAdapter;

import com.zhang.definitionview_demo.R;
import com.zhang.definitionview_demo.view.FlingRemovedListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListActivity extends AppCompatActivity {

    private FlingRemovedListView lv;

    List<Map<String, String>> list = new ArrayList<Map<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        initView();
        init();
    }

    private void initView() {
        lv = (FlingRemovedListView) findViewById(R.id.lv);
    }

    private void init() {
        for (int i = 0; i < 50; i++) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("name", "灵台方寸山" + i);
            map.put("company", "斜月三星洞");
            list.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.item_list, new String[] { "name", "company" },
                new int[] { R.id.name, R.id.company });
        lv.setAdapter(adapter);
    }

}
