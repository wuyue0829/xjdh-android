package com.chinatelecom.xjdh.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.chinatelecom.xjdh.fragment.AlarmListFragment_;
import com.chinatelecom.xjdh.fragment.MonitorListFragment_;

public class MainPagerAdapter extends FragmentPagerAdapter {
	public MainPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public int getCount() {
		return 2;
	}

	@Override
	public Fragment getItem(int position) {
		Fragment f = new Fragment();
		switch (position) {
		case 0:
			f = AlarmListFragment_.newInstance();
			break;
		case 1:
			f = MonitorListFragment_.newInstance();
			break;
		default:
			break;
		}
		return f;
	}

}
