package com.simplememo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
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
	ListView listView1;
	ArrayAdapter arrayAdapter;
	// int nowSelect = -1;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG_CLASS_NAME, new Exception().getStackTrace()[0].getMethodName());
		// memoData.setContext(this);
		setContentView(R.layout.memo_list);
		listView1 = (ListView) findViewById(R.id.listView1);
		listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				memoData.setNowSelect(position);
				memoData.setNowMode(MemoData.MODE_EDIT);
				startActivityForResult(new Intent(ActMemoList.this, ActNowMemo.class), 0);
				// startActivity(new Intent(ActMemoList.this, ActMemoView.class));
			}
		});

		listView1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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

		findViewById(R.id.btnServerSave).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new AsyncTask<Object, Object, Object>() {
					@Override
					protected Object doInBackground(Object... params) {
						return null;
					}
				};
			}
		});

		final Button btnServerSave = (Button) findViewById(R.id.btnServerSave);
		btnServerSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final String dataPack = memoData.toDataPack();
				new AsyncTask<Void, Void, Void>() {
					@Override
					protected void onPreExecute() {
						super.onPreExecute();
						btnServerSave.setEnabled(false);
					}

					@Override
					protected Void doInBackground(Void... params) {
						memoData.dbInsertMemoData(dataPack);
						return null;
					}

					@Override
					protected void onPostExecute(Void aVoid) {
						super.onPostExecute(aVoid);
						btnServerSave.setEnabled(true);
						Toast.makeText(ActMemoList.this, "모두 전송 됐습니다.\n→" + dataPack + "←", Toast.LENGTH_SHORT).show();
					}
				}.execute();
			}
		});

		final Button btnServerLoad = (Button) findViewById(R.id.btnServerLoad);
		btnServerLoad.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(ActMemoList.this, ActLoadList.class));
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
		String[] memoTitles = memoData.getTitles();
		if (memoTitles == null)
			memoTitles = new String[0];
		arrayAdapter = new ArrayAdapter(this, R.layout.list_element, memoTitles);
		listView1.setAdapter(arrayAdapter);
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
