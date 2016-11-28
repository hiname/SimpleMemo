package com.simplememo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class ActMemoList extends Activity {
	MemoData memoData = MemoData.getInstance();
	ListView listView1;
	ArrayAdapter arrayAdapter;
	// int nowSelect = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		memoData.setContext(this);
		setContentView(R.layout.memo_list);
		listView1 = (ListView) findViewById(R.id.listView1);
		reloadList();
		listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				memoData.setNowSelect(position);
				startActivity(new Intent(ActMemoList.this, ActMemoView.class));
			}
		});

		listView1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				final int selPos = position;
				AlertDialog.Builder alert_confirm = new AlertDialog.Builder(ActMemoList.this);
				alert_confirm
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
				final AlertDialog alert = alert_confirm.create();
				alert.show();
				return false;
			}
		});




		// 선택 삭제
		// 순서 변경

		findViewById(R.id.btnAddMemo).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(ActMemoList.this, ActNowMemoStart.class));
			}
		});

	}

	private void reloadList() {
		String[] memoTitles = memoData.getTitles();
		if (memoTitles == null)
			memoTitles = new String[0];
		arrayAdapter = new ArrayAdapter(this, R.layout.list_element, memoTitles);
		listView1.setAdapter(arrayAdapter);
		arrayAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onStart() {
		super.onStart();
		reloadList();
	}

	@Override
	protected void onPause() {
		memoData.save();
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
