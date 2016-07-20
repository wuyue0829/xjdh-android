package com.chinatelecom.xjdh.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.bean.RoomItem;
import com.chinatelecom.xjdh.bean.SubstationItem;
import com.chinatelecom.xjdh.rest.client.ApiRestClientInterface;
import com.chinatelecom.xjdh.utils.PreferenceConstants;
import com.chinatelecom.xjdh.utils.PreferenceUtils;
import com.chinatelecom.xjdh.utils.SharedConst;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
/**
 * @author peter
 * 
 */
@EActivity(R.layout.normal_list_view)
public class RoomListActivity extends BaseActivity {

	@ViewById(R.id.lv_items)
	ListView mLvRoom;
	@ViewById(R.id.tv_refresh)
	TextView mTvRefresh;
	@RestService
	ApiRestClientInterface mApiClient;
	@Extra("substation")
	SubstationItem substationItem;
	List<RoomItem> mRoomList = new ArrayList<>();
	SimpleAdapter mRoomAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String token = PreferenceUtils.getPrefString(this, PreferenceConstants.ACCESSTOKEN, "");
		mApiClient.setHeader(SharedConst.HTTP_AUTHORIZATION, token);
	}

	@AfterViews
	void bindData() {
		setTitle(substationItem.getName() + "机房列表");
		mRoomList.addAll(Arrays.asList(substationItem.getRoomlist()));
		List<Map<String, Object>> listItem = new ArrayList<Map<String, Object>>();
		int index = 1;
		for (RoomItem roomItem : mRoomList) {
			Map<String, Object> item = new HashMap<>();
			item.put("num", String.valueOf(index++));
			item.put("name", roomItem.getName());
			listItem.add(item);
		}
		mRoomAdapter = new SimpleAdapter(this, listItem, R.layout.area_list_item, new String[] { "num", "name" }, new int[] { R.id.tv_num, R.id.tv_info });
		mLvRoom.setAdapter(mRoomAdapter);
	}

	@ItemClick(R.id.lv_items)
	void onRoomItemClicked(int pos) {
//		RoomDevListActivity_.intent(this).mRoomCode(mRoomList.get(pos).getCode()).mRoomName(mRoomList.get(pos).getName()).start();
		RoomDevListActivity_.intent(this).mRoomCode(mRoomList.get(pos).getId()).mRoomName(mRoomList.get(pos).getName()).start();
	}
}
