package com.chinatelecom.xjdh.ui;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.app.AppManager;

public class BaseActivity extends ActionBarActivity {
	public ActionBar mActionBar;
	public boolean isInit = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActionBar = getSupportActionBar();
		mActionBar.setDisplayShowCustomEnabled(true);
		mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		mActionBar.setCustomView(getLayoutInflater().inflate(R.layout.actionbar_title, null), new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,
				ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER));
		if (!this.getClass().equals(MainActivity_.class)) {
			mActionBar.setDisplayHomeAsUpEnabled(true);
			mActionBar.setDisplayShowHomeEnabled(false);
			mActionBar.setHomeButtonEnabled(true);
		}
		AppManager.getAppManager().addActivity(this);
	}

	void setTitle(String title) {
		View abView = mActionBar.getCustomView();
		((TextView) abView.findViewById(R.id.ab_title)).setText(title);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 结束Activity&从堆栈中移除
		AppManager.getAppManager().finishActivity(this);
	}

	@Override
	protected void onResume() {
		if (mListeners.size() > 0)
			for (BackPressHandler handler : mListeners) {
				handler.activityOnResume();
			}
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mListeners.size() > 0)
			for (BackPressHandler handler : mListeners) {
				handler.activityOnPause();
			}
	}

	public static ArrayList<BackPressHandler> mListeners = new ArrayList<BackPressHandler>();

	public static abstract interface BackPressHandler {
		public abstract void activityOnResume();

		public abstract void activityOnPause();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			if (!this.getClass().equals(MainActivity_.class)) {
				finish();
				return true;
			} else {
				return super.onOptionsItemSelected(item);
			}
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
