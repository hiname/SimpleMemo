package com.simplememo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

public class ActNowMemo extends Activity {
	private EditText editText1;
	MemoData memoData = MemoData.getInstance();

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("d", "ActNowMemo_onCreate");
		setContentView(R.layout.now_memo);
		editText1 = (EditText) findViewById(R.id.editText1);
		// editText1.setSingleLine(false);
		if (memoData.getNowMode().equals(MemoData.MODE_EDIT)) {
			editText1.setText(memoData.getNowSelectMemo());
			// editText1.setFocusable(true);
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
				save();
			}
		});
		findViewById(R.id.btnSaveAndFin).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				save();
				finish();
			}
		});
	}

	private void keyBoardHide() {
		InputMethodManager immhide = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
		immhide.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
	}

	private void save() {
		String mode = memoData.getNowMode();
		Log.d("d", "mode : " + mode);
		Log.d("d", "└memoData.getSize() : " + memoData.getSize());
		if (mode.equals(MemoData.MODE_MEMO_ADD)) {
			memoData.setNowSelect(memoData.getSize());
			memoData.add("");
			memoData.setNowMode(MemoData.MODE_MEMO_ADD_EDIT);
		}
		Log.d("d", "memoData.getSize() : " + memoData.getSize());
		String srcStr = memoData.getNowSelectMemo();
		String inputStr = editText1.getText().toString();

		Toast.makeText(ActNowMemo.this, "저장 됐습니다.\n→" + inputStr.replace(srcStr, "") + "←", Toast.LENGTH_SHORT).show();
		memoData.setNowData(inputStr);
	}

	@Override
	protected void onPause() {
		super.onPause();
		final String inputStr = editText1.getText().toString();
		if (!inputStr.equals("")
				&& !inputStr.equals(memoData.getNowSelectMemo()))
			save();
		finish();
	}

	@Override
	public void finish() {
		Log.d("d", "");
//		if (memoData.getNowMode().equals(MemoData.MODE_MEMO_ADD_EDIT)) {
//			Intent intent = new Intent(this, ActMemoList.class);
//			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(intent);
//		}
		super.finish();
	}
}
