package com.simplememo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by USER on 2016-11-28.
 */
public class MemoData {
	private MemoData() {}

	private static class Singleton {
		private static MemoData instance = new MemoData();
	}

	// String saveFileName = "spf";
	SharedPreferences spf;
	String dataPackKey = "dataPackKey";
	// String memoSpliter = "sd";
	String memoSpliter = "\n#newline#\n";
	boolean isInitLoad = false;
	Context context;

	String saveDir = Environment.getExternalStorageDirectory() + "/simplememo";
	String saveFileName = "save.txt";
	String saveFileFullPath = saveDir + "/" + saveFileName;

	ListUpdate listUpdate;

	public void setListUpdate(ListUpdate listUpdate) {
		this.listUpdate = listUpdate;
	}

	public void load() {
		File file = new File(saveFileFullPath);
		if (!file.exists()) {
			new File(saveDir).mkdirs();
			FileMgr.saveFileText(saveFileFullPath, "메모 입력", FileMgr.ENC_UTF8, false);
		}
		clear();
		// String dataPack = spf.getString(dataPackKey, "");
		String dataPack = "";
		String[] loadLineList = FileMgr.loadFileTextArray(saveFileFullPath, FileMgr.ENC_UTF8);
		for(String loadLine : loadLineList)
			dataPack += loadLine + "\n";
		if (dataPack.length() > 2)
			dataPack = dataPack.substring(0, dataPack.length() - 1);

		Log.d("d", "load_dataPack : " + dataPack);
		String dataList[] = dataPack.split(memoSpliter);
		for (String data : dataList) add(data);
	}

	public void save() {
//		Log.d("d", "save()");
		String dataPack = toDataPack();
//		SharedPreferences.Editor ed = spf.edit();
//		ed.putString(dataPackKey, dataPack);
//		ed.commit();

		Log.d("d", "save()");
		FileMgr.saveFileText(saveFileFullPath, dataPack, FileMgr.ENC_UTF8, false);
	}

	public String toDataPack() {
		String dataPack = "";
		for (String data : arrayList) {
			dataPack += data + memoSpliter;
			Log.d("d", "dataPackStack : " + dataPack);
			Log.d("d", "└ memoSpliter : " + memoSpliter);
		}
		if (dataPack.length() > memoSpliter.length()) {
			dataPack = dataPack.substring(0, dataPack.length() - memoSpliter.length());
		}
		Log.d("d", "toDatPack_dataPack : " + dataPack);
		return dataPack;
	}

	public static MemoData getInstance() {
		return Singleton.instance;
	}

	public void setContext(Context context) {
		this.context = context;
		spf = context.getSharedPreferences(saveFileName, Context.MODE_PRIVATE);
		if (!isInitLoad) {
			isInitLoad = true;
			load();
		}
	}

	private ArrayList<String> arrayList = new ArrayList<String>();
	private int nowSelect = -1;
	static String nowSelectKey = "nowSelectKey";
	static String MODE_EDIT = "MODE_EDIT";
	static String MODE_MEMO_ADD = "MODE_MEMO_ADD";
	static String MODE_MEMO_ADD_EDIT = "MODE_MEMO_ADD_EDIT";
	private String nowMode = MODE_MEMO_ADD;

	public String[] getArray() {
		return arrayList.toArray(new String[arrayList.size()]);
	}

	public String getNowMode() {
		return nowMode;
	}

	public void setNowMode(String mode) {
		nowMode = mode;
	}

	public int getNowSelect() {
		return nowSelect;
	}

	public void add(String str) {
		arrayList.add(str);
	}

	public int getSize() {
		return arrayList.size();
	}

	public void remove(int idx) {
		arrayList.remove(idx);
	}

	public void set(int idx, String str) {
		arrayList.set(idx, str);
	}

	public String get(int idx) {
		return arrayList.get(idx);
	}

	int titleLen = 15;

	public String[] getTitles() {
		int size = arrayList.size();
		if (size <= 0) return null;
		String[] titles = new String[size];
		for (int i = 0; i < size; i++) {
			String nowMemo = arrayList.get(i);
			if (nowMemo.length() > 2 && nowMemo.contains("\n")) {
				nowMemo = nowMemo.split("\n")[0] + "...";
			}
			if (nowMemo.length() > titleLen) {
				nowMemo = nowMemo.substring(0, titleLen) + "...";
			}
			titles[i] = nowMemo;
		}
		return titles;
	}

	public void clear() {
		arrayList.clear();
	}

	public String getNowSelectMemo() {
		int size = getSize();
		if (nowSelect == -1 || size == 0 || size <= nowSelect)
			return null;
		return arrayList.get(nowSelect);
	}

	public void setNowSelect(int idx) {
		nowSelect = idx;
	}

	public void setNowData(String str) {
		Log.d("d", "setNowData : " + nowSelect + ", " + str);
		set(nowSelect, str);
	}
}
