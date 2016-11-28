package com.simplememo;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by USER on 2016-11-28.
 */
public class ActShortcutCreateNowMemoStart extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ShortcutCreate.createShortcut(this, "com.simplememo.ActNowMemoStart", "NowMemo", R.drawable.ic_launcher);
		finish();
	}
}
