package com.simplememo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Arrays;

public class ActMain extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		startActivity(new Intent(this, ActMemoList.class));
		finish();

		// HttpPost httpPost = new HttpPost(MemoData.DB_URL, "SimpleMemo");
		// String sqlQuery = "sqlQuery=SELECT * FROM SimpleMemo";
		// String print = httpPost.postURL("http://hihost.dothome.co.kr/SimpleMemo/sendQuery.php", sqlQuery);
		// Log.d("d", "print : " + print);

		// Debug.d(httpPost.selectAllOrigin());

	}

}
