package com.simplememo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ActNowMemo extends Activity{
	
	private EditText editText1;
	MemoData memoData = MemoData.getInstance();
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.now_memo);

		editText1 = (EditText) findViewById(R.id.editText1);
		// editText1.setSingleLine(false);
		if (memoData.getNowMode().equals(MemoData.MODE_EDIT))
			editText1.setText(memoData.getNowSelectData());

		editText1.requestFocus();
		//
//		new Handler().postDelayed(new Runnable() {
//			@Override
//			public void run() {
//				// keyborad show
//				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//				imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
//			}
//		}, 2000);

		findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				save();
			}
		});
		findViewById(R.id.btnSaveAndFin).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				save(); finish();
			}
		});
	}
	
	@Override
	public void onBackPressed(){
		// super.onBackPressed();
		exit();
	}

	@Override
	protected void onUserLeaveHint() {
		super.onUserLeaveHint();
		keyBoardHide();
		exit();
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

		memoData.setNowData(editText1.getText().toString());
		Toast.makeText(ActNowMemo.this, "저장 됐습니다.", Toast.LENGTH_SHORT).show();
	}

	private void exit() {
		final String inputStr = editText1.getText().toString();
		if (inputStr.equals(memoData.getNowSelectData()) //
				|| inputStr.equals("")) {
			finish();
		} else {
			// Toast.makeText(this, "저장할?", Toast.LENGTH_SHORT).show();
			AlertDialog.Builder alert_confirm = new AlertDialog.Builder(this);
			alert_confirm
					.setMessage("저장 하시겠습니까?")
					.setCancelable(false)
					.setPositiveButton("저장",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							save();
							ActNowMemo.this.finish();
						}
					})
					.setNegativeButton("안함",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							ActNowMemo.this.finish();
							return;
						}
					});
			final AlertDialog alert = alert_confirm.create();
			alert.setOnShowListener(new DialogInterface.OnShowListener() {
				@Override
				public void onShow(DialogInterface dialog) {
					Button positive = alert.getButton(AlertDialog.BUTTON_POSITIVE);
					positive.setFocusable(true);
					positive.setFocusableInTouchMode(true);
					positive.requestFocus();
				}
			});
			alert.show();
		}
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
}
