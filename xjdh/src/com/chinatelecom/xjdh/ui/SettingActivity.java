package com.chinatelecom.xjdh.ui;

import org.androidannotations.annotations.EActivity;

import android.os.Bundle;

import com.chinatelecom.xjdh.fragment.SettingFragment_;
import com.chinatelecom.xjdh.utils.L;

@EActivity
public class SettingActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("设置");
		L.i("settng create");
		getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingFragment_()).commit();
	}
}
