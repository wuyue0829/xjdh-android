package com.chinatelecom.xjdh.ui;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import com.chinatelecom.xjdh.R;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {

	@AfterViews
	void initData() {
		setTitle(getResources().getString(R.string.app_name));
	}
}
