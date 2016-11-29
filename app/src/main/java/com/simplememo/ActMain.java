package com.simplememo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.text.Layout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ActMain extends Activity {
	boolean isMove = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 startActivity(new Intent(this, ActMemoList.class));
		 finish();

		if (true) return;
		String[] arr = {
			"0", "1", "2", "3", "4",
			"5", "6", "7", "8", "9",
			"9", "9",
		};
		ArrayAdapter<String> ad = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arr);


		setContentView(R.layout.memo_list);
		ListView lv = (ListView) findViewById(R.id.listView1);
		lv.setAdapter(ad);
		lv.setFastScrollEnabled(true);

		//
//		lv.setScrollbarFadingEnabled(false);
//		// lv.setFastScrollStyle(R.style.scrollbar_shape_style);
//		lv.setScrollBarStyle(R.style.scrollbar_shape_style);
//
//		setContentView(lv);

////		lv.setAdapter(ad);
//
//		setContentView(R.layout.activity_act_main);
//		ListView listView1 = (ListView) findViewById(R.id.listView1);
//		listView1.setAdapter(ad);
//		listView1.setFastScrollAlwaysVisible(true);
//		listView1.setFastScrollEnabled(true);

	}

}
