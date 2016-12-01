package com.simplememo;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ActNowMemo extends Activity {
	private EditText editText1;
	private MemoData memoData = MemoData.getInstance();
	private TextView tvCreateDateTime, tvModifyDateTime;

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.now_memo);
		editText1 = (EditText) findViewById(R.id.editText1);
		tvCreateDateTime = (TextView) findViewById(R.id.tvCreateDateTime);
		tvModifyDateTime = (TextView) findViewById(R.id.tvModifyDateTime);
		// editText1.setSingleLine(false);
		if (memoData.getNowMode().equals(MemoData.MODE_EDIT)) {
			editText1.setText(memoData.getNowSelectMemo());
			// editText1.setFocusable(true);
			tvCreateDateTime.setText("만든시간 : " + memoData.getNowSelectCreateDateTime());
			tvModifyDateTime.setText("수정시간 : " + memoData.getNowSelectModifyDateTime());
			final int getInptType = editText1.getInputType();
			editText1.setVisibility(View.INVISIBLE);
			editText1.setInputType(InputType.TYPE_NULL);
			editText1.post(new Runnable() {
				@Override
				public void run() {
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							editText1.setInputType(getInptType);
							editText1.setVisibility(View.VISIBLE);
						}
					}, 150);
				}
			});
		}

		findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setMemo();
			}
		});
		findViewById(R.id.btnSaveAndFin).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setMemo();
				finish();
			}
		});

		final Button btnSendDB = (Button) findViewById(R.id.btnSendDB);
		btnSendDB.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final String inputStr = editText1.getText().toString();
				new AsyncTask<Void, Void, Void>(){

					@Override
					protected void onPreExecute() {
						super.onPreExecute();
						btnSendDB.setEnabled(false);
					}

					@Override
					protected Void doInBackground(Void... params) {
						memoData.dbInsert(inputStr);
						return null;
					}

					@Override
					protected void onPostExecute(Void aVoid) {
						super.onPostExecute(aVoid);
						btnSendDB.setEnabled(true);
						Toast.makeText(ActNowMemo.this, "전송 됐습니다.\n→" + inputStr + "←", Toast.LENGTH_SHORT).show();
					}
				}.execute();
			}
		});
	}

	private void keyBoardHide() {
		InputMethodManager immhide = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
		immhide.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
	}

	private void setMemo() {
		String mode = memoData.getNowMode();
		if (mode.equals(MemoData.MODE_MEMO_ADD)) {
			memoData.setNowSelect(memoData.getSize());
			memoData.addBlank();
			memoData.setNowMode(MemoData.MODE_MEMO_ADD_EDIT);
		}
		String srcStr = memoData.getNowSelectMemo();
		String inputStr = editText1.getText().toString();

		Toast.makeText(ActNowMemo.this, "저장 됐습니다.\n→" + inputStr.replace(srcStr, "") + "←", Toast.LENGTH_SHORT).show();
		memoData.setNowData(inputStr);
		memoData.fileToSave();
	}

	@Override
	protected void onPause() {
		super.onPause();

		final String inputStr = editText1.getText().toString();
		if (!inputStr.equals("")
				&& !inputStr.equals(memoData.getNowSelectMemo()))
			setMemo();
		finish();
	}

	@Override
	public void finish() {

//		if (memoData.getNowMode().equals(MemoData.MODE_MEMO_ADD_EDIT)) {
//			Intent intent = new Intent(this, ActMemoList.class);
//			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(intent);
//		}
		super.finish();
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();

	}
}
