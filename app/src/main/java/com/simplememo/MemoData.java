package com.simplememo;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by USER on 2016-11-28.
 */
public class MemoData {
	private MemoData() {
		if (!isInitLoad) {
			isInitLoad = true;
			loadByFile();
		}
	}

	private static class Singleton {
		private static MemoData instance = new MemoData();
	}
	
	public static MemoData getInstance() {
		return Singleton.instance;
	}

	// String saveFileName = "spf";
	// SharedPreferences spf;
	String dataPackKey = "dataPackKey";
	public static final String TAG_MEMO_SPLITER = "\n#mSplit#\n";
	boolean isInitLoad = false;
	// Context context;

	String saveDir = Environment.getExternalStorageDirectory() + "/simplememo";
	//
	String saveFileName = "saveMemoData.txt";
	String saveFileFullPath = saveDir + "/" + saveFileName;
	//
	String memoIdFileName = "memoId.txt";
	String memoIdFileFullPath = saveDir + "/" + memoIdFileName;
	//
	String memoHistoryFileName = "memoHistory.txt";
	String memoHistoryFileFullPath = saveDir + "/" + memoHistoryFileName;

	ListUpdate listUpdate;

	public void setListUpdate(ListUpdate listUpdate) {
		this.listUpdate = listUpdate;
	}

	public void loadByFile() {
		File saveFile = new File(saveFileFullPath);
		if (!saveFile.exists()) {
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

		addMemoData(dataPack);

		File memoIdFile = new File(memoIdFileFullPath);
		if (!memoIdFile.exists()) {
			FileMgr.saveFileText(memoIdFileFullPath, Integer.toString(memoId), FileMgr.ENC_UTF8, false);
		}
		memoId = Integer.parseInt(FileMgr.loadFileTextArray(memoIdFileFullPath, FileMgr.ENC_UTF8)[0].trim());

		File memoHistoryFile = new File(memoHistoryFileFullPath);
		if (!memoHistoryFile.exists()) {
			FileMgr.saveFileText(memoHistoryFileFullPath, "메모 수정 내역", FileMgr.ENC_UTF8, false);
		}
		memoHistory = new ArrayList<String>(Arrays.asList(FileMgr.loadFileTextArray(memoHistoryFileFullPath, FileMgr.ENC_UTF8)));
	}

	public String getNowId() {
		String nowMemoData = memoDataList.get(nowSelect);
		return StringMgr.extTagData(nowMemoData, TAG_MEMO_ID);
	}

	public ArrayList<String> getNowSelectHistoryList() {
		return getHistoryList(getNowId());
	}

	private ArrayList<String> nowSelectHistoryList  = new ArrayList<String>();
	private ArrayList<Integer> nowSelectHistoryListOfIdx = new ArrayList<Integer>();

	public String getNowHistoryItem() {
		return nowSelectHistoryList.get(nowHistorySelect);
	}

	public void setNowHistorySelect(int nowHistorySelect) {
		this.nowHistorySelect = nowHistorySelect;
	}

	public void removeNowHistorySelect() {
		removeMemoHistory(nowSelectHistoryListOfIdx.get(nowHistorySelect));
	}

	public void removeMemoHistory(int idx) {
		memoHistory.remove(idx);
	}

	public void restoreNowHistorySelect() {
		modifyNowData(getNowHistoryItem());
	}

	public ArrayList<String> getHistoryList(String id) {
		nowSelectHistoryList.clear();
		nowSelectHistoryListOfIdx.clear();
		int idx = 0;
		for (String history : memoHistory) {
			if (id.equals(StringMgr.extTagData(history, TAG_MEMO_ID))) {
				nowSelectHistoryList.add(history);
				nowSelectHistoryListOfIdx.add(idx);
			}
			idx++;
		}
		return nowSelectHistoryList;
	}

	public int getNowSelectHistoryCount() {
		return getHistoryIdCount(getNowId());
	}

	ArrayList<String> memoHistory = new ArrayList<String>();

	public int getHistoryIdCount(String id) {
		int count = 0;
		for (String history : memoHistory) {
			if (id.equals(StringMgr.extTagData(history, TAG_MEMO_ID))) {
				count++;
			}
		}
		return count;
	}

	public void addMemoData(String dataPack) {
		for (String data : dataPack.split(TAG_MEMO_SPLITER)) { 
			add(data);
		}
	}

	public void saveInFile() {
		String dataPack = toDataPack();
//		SharedPreferences.Editor ed = spf.edit();
//		ed.putString(dataPackKey, dataPack);
//		ed.commit();

		FileMgr.saveFileText(saveFileFullPath, dataPack, FileMgr.ENC_UTF8, false);
		FileMgr.saveFileText(saveDir + "/backup/back" + new SimpleDateFormat("yy-MM-dd HH;mm;ss").format(new Date()) + ".txt" , dataPack, FileMgr.ENC_UTF8, false);
		FileMgr.saveFileText(memoIdFileFullPath, Integer.toString(memoId), FileMgr.ENC_UTF8, false);
		FileMgr.saveFileText(memoHistoryFileFullPath, memoHistory, FileMgr.ENC_UTF8, false);
		Debug.d("memoHistoryOfSaveReady.size : " + memoHistoryOfSaveReady.size());

//		if (memoHistoryOfSaveReady.size() > 0) {
//			FileMgr.saveFileText(memoHistoryFileFullPath, memoHistoryOfSaveReady, FileMgr.ENC_UTF8, true);
//			memoHistoryOfSaveReady.clear();
//		}
	}

	public String toDataPack() {
		String dataPack = "";
		for (String data : memoDataList) {
			dataPack += data + TAG_MEMO_SPLITER;
		}
		if (dataPack.length() > TAG_MEMO_SPLITER.length()) {
			dataPack = dataPack.substring(0, dataPack.length() - TAG_MEMO_SPLITER.length());
		}
		return dataPack;
	}

	private ArrayList<String> memoDataList = new ArrayList<String>();
	private int nowSelect = -1;
	private int nowHistorySelect = -1;
	static String nowSelectKey = "nowSelectKey";
	static String MODE_EDIT = "MODE_EDIT";
	static String MODE_MEMO_ADD = "MODE_MEMO_ADD";
	static String MODE_MEMO_ADD_EDIT = "MODE_MEMO_ADD_EDIT";
	static String MODE_HISTORY_LIST = "MODE_HISTORY_LIST";

	private String nowMode = MODE_MEMO_ADD;

	public String[] getArray() {
		return memoDataList.toArray(new String[memoDataList.size()]);
	}

	public String getNowMode() {
		return nowMode;
	}

	ArrayList<String> modeList = new ArrayList<String>();

	public void setNowMode(String mode) {
		// modeList.add(mode);
		nowMode = mode;
	}

	public void setPrevMode() {
		modeList.remove(modeList.size() - 1);
		nowMode = modeList.get(modeList.size() - 1);
	}

	public int getNowSelect() {
		return nowSelect;
	}

	String dateTimeFormatPattern = "yy-MM-dd HH:mm:ss";
	SimpleDateFormat dateTimeFormat = new SimpleDateFormat(dateTimeFormatPattern);

	public void addBlank() {
		add("blank");
	}

	public static final String TAG_MEMO_ID = "#id#";
	public static final String TAG_CREATE_DATE_TIME = "#create#";
	public static final String TAG_MODIFY_DATE_TIME = "#modify#";

	public void add(String inputStr) {
		Log.d("d", "add str : " + inputStr);
		String memoInfo = "";
		if (!inputStr.contains(TAG_MEMO_ID)) {
			memoInfo = generateId() + generateCreateDateTime() + generateModifyDateTime();
		}

		memoDataList.add(inputStr + TAG_MEMO_END + memoInfo);
	}

	public int getSize() {
		return memoDataList.size();
	}

	public void remove(int idx) {
		memoDataList.remove(idx);
	}

	public static final String TAG_MEMO_END = "#memoEnd#";

	ArrayList<String> memoHistoryOfSaveReady = new ArrayList<String>();

	public void modifyData(int idx, String str) {
		String getInfoData = get(idx).split(TAG_MEMO_END)[1];
		String modify = TAG_MODIFY_DATE_TIME + StringMgr.extTagData(getInfoData, TAG_MODIFY_DATE_TIME) + TAG_MODIFY_DATE_TIME;
		getInfoData = getInfoData.replace(modify, generateModifyDateTime());
		String memo = str + TAG_MEMO_END + getInfoData;
		memoDataList.set(idx, memo);
		Debug.d("addMemoHistory : " + memo);
		memoHistoryOfSaveReady.add(memo);
		memoHistory.add(memo);
	}

	int memoId;

	public String generateId() {
		memoId++;
		return TAG_MEMO_ID + memoId + TAG_MEMO_ID;
	}

	public String generateCreateDateTime() {
		return TAG_CREATE_DATE_TIME + dateTimeFormat.format(new Date()) + TAG_CREATE_DATE_TIME;
	}
	
	public String generateModifyDateTime() {
		return TAG_MODIFY_DATE_TIME + dateTimeFormat.format(new Date()) + TAG_MODIFY_DATE_TIME;
	}

	public String get(int idx) {
		return memoDataList.get(idx);
	}

	public String getMemo(int idx) {
		return memoDataList.get(idx).split(TAG_MEMO_END)[0];
	}

	public String getInfo(int idx) {
		return memoDataList.get(idx).split(TAG_MEMO_END)[1];
	}

	int titleShowLength = 8;

	public String[] getMemoTitles() {
		int size = memoDataList.size();
		if (size <= 0) return null;
		String[] titles = new String[size];
		for (int i = 0; i < size; i++) {
			String nowMemo = getMemo(i);
			if (nowMemo.length() > titleShowLength) {
				nowMemo = nowMemo.substring(0, titleShowLength) + "...";
			} else if (nowMemo.length() > 2 && nowMemo.contains("\n")) {
				nowMemo = nowMemo.split("\n")[0] + "...";
			}

			titles[i] = nowMemo;
		}
		return titles;
	}

	public void clear() {
		memoDataList.clear();
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

	public void modifyNowData(String str) {
		modifyData(nowSelect, str);
	}

	public static final String DB_URL = "http://hihost.dothome.co.kr/SimpleMemo";
	private final HttpPost httpPost = new HttpPost(DB_URL, "SimpleMemo");

	public void dbInsertMemoData(String varQuery) {
		String chArr = toCharCode(varQuery);
		httpPost.insertMemo(chArr);
	}

	public void dbDeleteMemoData(String id) {
		httpPost.deleteById(id);
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
		return httpPost.jsonToArrayOfColName(colNameJson);
	}

	public String[][] dbSelectAllArray() {
		return httpPost.jsonToArrayOfSelectAll(httpPost.selectAllOrigin());
	}

	public String[][] dbSelectAllArrayCharToStr() {
		String[][] array = httpPost.jsonToArrayOfSelectAll(httpPost.selectAllOrigin());
		for (int i = 0; i < array.length; i++) {
			array[i][2] = toStr(array[i][2]);
		}
		return array;
	}

	private String toStr(String charCodePack) {
		String str = "";
		for (String charCode : charCodePack.split(",")) {
			if (charCode.matches("-?\\d+"))
				str += (char)Integer.parseInt(charCode);
			else
				str += charCode;
		}
		return str;
	}
}
