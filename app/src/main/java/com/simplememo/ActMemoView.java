package com.simplememo;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ActMemoView extends Activity{

	MemoData memoData = MemoData.getInstance();
	TextView tvMemoView;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.memo_view);
		tvMemoView = (TextView) findViewById(R.id.tvMemoView);
		Button btnEdit = (Button) findViewById(R.id.btnEdit);
		btnEdit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				memoData.setNowMode(MemoData.MODE_EDIT);
				startActivity(new Intent(ActMemoView.this, ActNowMemo.class));
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		tvMemoView.setText(memoData.getNowSelectData());
	}
}
