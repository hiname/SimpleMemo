package com.simplememo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.Arrays;

public class ActMain extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		startActivity(new Intent(this, ActMemoList.class));
//		finish();
		TextView tv = new TextView(this);
		setContentView(tv);
		HttpPost httpPost = new HttpPost(MemoData.DB_URL);
//		String getPost = httpPost.postURL("http://hihost.dothome.co.kr/SimpleMemo/jsonTest.php");
//		tv.setText(getPost);
//		Log.d("d", "getPost : " + getPost);
//		String[][] printList = httpPost.jsonToArray(getPost);
//		String print = "";
//		print += "printList.len : " + printList.length;
//		for (String[] prr : printList) {
//			print += Arrays.toString(prr) + "\n";
//		}
//		Log.d("d", "print" + print);
//		tv.setText(print);

		Log.d("d", "colList : " + Arrays.toString(MemoData.getInstance().dbGetColNameList()));
	}
}
