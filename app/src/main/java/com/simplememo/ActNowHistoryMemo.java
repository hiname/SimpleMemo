package com.simplememo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by USER on 2016-12-06.
 */
public class ActNowHistoryMemo extends Activity{
	EditText editText1;
	Button btnRestore;
	MemoData memoData = MemoData.getInstance();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.now_history_memo);
		editText1 = (EditText) findViewById(R.id.editText1);
		editText1.setText(memoData.getNowHistoryItem().split(MemoData.TAG_MEMO_END)[0]);
		btnRestore = (Button) findViewById(R.id.btnRestore);
		btnRestore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Debug.d("복원하기");
				memoData.restoreNowHistorySelect();
				finish();
				ActHistoryList.act.finish();

			}
		});

	}
}
