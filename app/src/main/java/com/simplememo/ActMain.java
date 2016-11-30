package com.simplememo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Layout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ActMain extends Activity {
	boolean isMove = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		startActivity(new Intent(this, ActMemoList.class));
//		finish();

		MemoData memoData = MemoData.getInstance();
		memoData.setContext(this);

		String[][] selectList = memoData.dbSelect();

		String result = "";

		for (int i = 0; i < selectList.length; i++) {
			for (int j = 0; j < selectList[i].length; j++) {
				result += selectList[i][j] + ",";
			}
			result += "\n";
		}

		Log.d("d", result);
		Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
	}

}
