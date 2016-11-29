package com.simplememo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by USER on 2016-11-29.
 */
public class Svc extends Service {
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Toast.makeText(this, "시작", Toast.LENGTH_SHORT).show();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Toast.makeText(this, "종료", Toast.LENGTH_SHORT).show();
	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
