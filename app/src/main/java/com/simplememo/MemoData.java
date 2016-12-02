package com.simplememo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
	public static final String TAG_MEMO_SPLITER = "\n#newline#\n";
	boolean isInitLoad = false;
	Context context;

	String saveDir = Environment.getExternalStorageDirectory() + "/simplememo";
	String saveFileName = "fileToSave.txt";
	String saveFileFullPath = saveDir + "/" + saveFileName;

	ListUpdate listUpdate;

	public void setListUpdate(ListUpdate listUpdate) {
		this.listUpdate = listUpdate;
	}

	public void fileToLoad() {
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

		String dataList[] = dataPack.split(TAG_MEMO_SPLITER);
		for (String data : dataList) add(data);
	}

	public void fileToSave() {
		String dataPack = toDataPack();
//		SharedPreferences.Editor ed = spf.edit();
//		ed.putString(dataPackKey, dataPack);
//		ed.commit();

		FileMgr.saveFileText(saveFileFullPath, dataPack, FileMgr.ENC_UTF8, false);
	}

	public String toDataPack() {
		String dataPack = "";
		for (String data : arrayList) {
			dataPack += data + TAG_MEMO_SPLITER;
		}
		if (dataPack.length() > TAG_MEMO_SPLITER.length()) {
			dataPack = dataPack.substring(0, dataPack.length() - TAG_MEMO_SPLITER.length());
		}
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
			fileToLoad();
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

	String dateTimeFormatPattern = "yy-MM-dd HH:mm:ss";
	SimpleDateFormat dateTimeFormat = new SimpleDateFormat(dateTimeFormatPattern);

	public void addBlank() {
		add("blank");
	}

	public static final String TAG_CREATE_DATE_TIME = "#create#";
	public static final String TAG_MODIFY_DATE_TIME = "#modify#";

	public void add(String inputStr) {
		Log.d("d", "add str : " + inputStr);
		Log.d("d", "!str.contains(TAG_CREATE_DATE_TIME) : " + !inputStr.contains(TAG_CREATE_DATE_TIME));
		String memoInfo = "";
		if (!inputStr.contains(TAG_CREATE_DATE_TIME)) {
			String dateTimeStr = dateTimeFormat.format(new Date());
			memoInfo = TAG_CREATE_DATE_TIME + dateTimeStr + TAG_CREATE_DATE_TIME;
			memoInfo += getModifyDateTime();
		}

		arrayList.add(inputStr + TAG_MEMO_END + memoInfo);
	}

	public int getSize() {
		return arrayList.size();
	}

	public void remove(int idx) {
		arrayList.remove(idx);
	}

	public static final String TAG_MEMO_END = "#memoEnd#";

	public void set(int idx, String str) {
		String getInfoData = get(idx).split(TAG_MEMO_END)[1];
		String modify = TAG_MODIFY_DATE_TIME + StringMgr.extTagData(getInfoData, TAG_MODIFY_DATE_TIME) + TAG_MODIFY_DATE_TIME;
		getInfoData = getInfoData.replace(modify, getModifyDateTime());
		arrayList.set(idx, str + TAG_MEMO_END + getInfoData);
	}

	public String getModifyDateTime() {
		return TAG_MODIFY_DATE_TIME + dateTimeFormat.format(new Date()) + TAG_MODIFY_DATE_TIME;
	}

	public String get(int idx) {
		return arrayList.get(idx);
	}

	public String getMemo(int idx) {
		return arrayList.get(idx).split(TAG_MEMO_END)[0];
	}

	public String getInfo(int idx) {
		return arrayList.get(idx).split(TAG_MEMO_END)[1];
	}

	int titleLen = 8;

	public String[] getTitles() {
		int size = arrayList.size();
		if (size <= 0) return null;
		String[] titles = new String[size];
		for (int i = 0; i < size; i++) {
			String nowMemo = getMemo(i);
			if (nowMemo.length() > titleLen) {
				nowMemo = nowMemo.substring(0, titleLen) + "...";
			} else if (nowMemo.length() > 2 && nowMemo.contains("\n")) {
				nowMemo = nowMemo.split("\n")[0] + "...";
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
		return getMemo(nowSelect);
	}

	public String getNowSelectCreateDateTime() {
		int size = getSize();
		if (nowSelect == -1 || size == 0 || size <= nowSelect)
			return null;

		return getCreateDateTime(nowSelect);
	}

	public String getNowSelectModifyDateTime() {
		int size = getSize();
		if (nowSelect == -1 || size == 0 || size <= nowSelect)
			return null;

		return getModifyDateTime(nowSelect);
	}

	public String getCreateDateTime(int idx) {
		String info = getInfo(idx);
		return StringMgr.extTagData(info, TAG_CREATE_DATE_TIME);
	}

	public String getModifyDateTime(int idx) {
		String info = getInfo(idx);
		return StringMgr.extTagData(info, TAG_MODIFY_DATE_TIME);
	}

	public void setNowSelect(int idx) {
		nowSelect = idx;
	}

	public void setNowData(String str) {
		set(nowSelect, str);
	}

	public static final String DB_URL = "http://hihost.dothome.co.kr/SimpleMemo";
	private final HttpPost httpPost = new HttpPost(DB_URL);

	public void dbInsertMemoData(String varQuery) {
		String chArr = toCharCode(varQuery);
		httpPost.insert("memo=" + chArr);
	}

	public void dbDeleteMemoData(String id) {
		httpPost.delete("id=" + id);
	}

	private String toCharCode(String str) {
		char[] charCodeArr = str.toCharArray();
		String charCodePack = Integer.toString((int) charCodeArr[0]);

		for (int i = 1; i < charCodeArr.length; i++) {
			charCodePack += "," + (int) charCodeArr[i];
		}

		return charCodePack;
	}

	public String dbSelectOrigin() {
		return httpPost.selectAllOrigin();
	}

	public String[] dbGetColNameList() {
		String colNameJson = httpPost.getColNameList();
		return httpPost.colNameJsonToArray(colNameJson);
	}

	public String[][] dbSelectAllArray() {
		return httpPost.selectAllJsonToArray(httpPost.selectAllOrigin());
	}

	public String[][] dbSelectAllArrayCharToStr() {
		String[][] array = httpPost.selectAllJsonToArray(httpPost.selectAllOrigin());
		for (int i = 0; i < array.length; i++) {
			array[i][2] = toStr(array[i][2]);
		}
		return array;
	}

	private String toStr(String charCodePack) {
		String str = "";
		for (String charCode : charCodePack.split(",")) {
			str += (char)Integer.parseInt(charCode);
		}
		return str;
	}
}
