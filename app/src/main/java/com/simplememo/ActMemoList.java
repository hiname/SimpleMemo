package com.simplememo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class ActMemoList extends Activity implements ListUpdate{

	private final String TAG_CLASS_NAME = ActMemoList.this.getClass().getSimpleName();

	MemoData memoData = MemoData.getInstance();
	ListView lvMemoList;
	ArrayAdapter arrayAdapter;
	// int nowSelect = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG_CLASS_NAME, new Exception().getStackTrace()[0].getMethodName());
		// memoData.setContext(this);
		setContentView(R.layout.memo_list);
		lvMemoList = (ListView) findViewById(R.id.listView1);
		lvMemoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				memoData.setNowSelect(position);
				memoData.setNowMode(MemoData.MODE_EDIT);
				startActivityForResult(new Intent(ActMemoList.this, ActNowMemo.class), 0);
				// startActivity(new Intent(ActMemoList.this, ActMemoView.class));
			}
		});

		lvMemoList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				final int selPos = position;
				AlertDialog.Builder builder = new AlertDialog.Builder(ActMemoList.this);
				builder
						.setMessage("삭제 하시겠습니까?\n→ " + arrayAdapter.getItem(selPos) + " ←")
						.setCancelable(false)
						.setPositiveButton("삭제함",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										memoData.remove(selPos);
										reloadList();
									}
								})
						.setNegativeButton("안함",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										return;
									}
								});
				builder.setCancelable(true);
				builder.create().show();
				return false;
			}
		});

		findViewById(R.id.btnAddMemo).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(ActMemoList.this, ActAddNowMemoStart.class));
			}
		});

		final Button btnServerSave = (Button) findViewById(R.id.btnServerSave);
		btnServerSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				btnServerSave.setEnabled(false);

				String memoDataPack = memoData.getMemoDataPack();
				String historyDataPack = memoData.getHistoryDataPack();

				String dataPack = MemoData.TAG_MEMO_DATA + memoDataPack + MemoData.TAG_MEMO_DATA;
				dataPack += MemoData.TAG_HISTORY + historyDataPack + MemoData.TAG_HISTORY;

				memoData.dbInsertMemoData(dataPack);
				Toast.makeText(ActMemoList.this, "모두 전송 됐습니다.\n→" + dataPack + "←", Toast.LENGTH_SHORT).show();

				btnServerSave.setEnabled(true);
			}
		});

		findViewById(R.id.btnServerLoad).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(ActMemoList.this, ActDBList.class));
			}
		});

		reloadList();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void reloadList() {
		Log.d(TAG_CLASS_NAME, new Exception().getStackTrace()[0].getMethodName());
		String[] memoTitles = memoData.getMemoTitles();
		if (memoTitles == null)
			memoTitles = new String[0];
		arrayAdapter = new ArrayAdapter(this, R.layout.list_element, memoTitles);
		lvMemoList.setAdapter(arrayAdapter);
		arrayAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onResume() {
		Log.d(TAG_CLASS_NAME, new Exception().getStackTrace()[0].getMethodName());
		super.onResume();
		reloadList();
	}

	@Override
	protected void onPause() {
		memoData.saveInFile();
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG_CLASS_NAME, new Exception().getStackTrace()[0].getMethodName());
	}
}
