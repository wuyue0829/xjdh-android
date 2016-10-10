package com.chinatelecom.xjdh.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.bean.ApiResponse;
import com.chinatelecom.xjdh.bean.DevItem;
import com.chinatelecom.xjdh.bean.DevTypeItem;
import com.chinatelecom.xjdh.rest.client.ApiRestClientInterface;
import com.chinatelecom.xjdh.rest.client.ApiRestClientInterfaceV1;
import com.chinatelecom.xjdh.utils.L;
import com.chinatelecom.xjdh.utils.PreferenceConstants;
import com.chinatelecom.xjdh.utils.PreferenceUtils;
import com.chinatelecom.xjdh.utils.SharedConst;
import com.chinatelecom.xjdh.utils.URLs;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

/**
 * @author peter
 * 
 */
@EActivity(R.layout.normal_list_view)
public class RoomDevListActivity extends BaseActivity {

	@ViewById(R.id.lv_items)
	ListView mLvDevType;
	@ViewById(R.id.tv_refresh)
	TextView mTvRefresh;
	@RestService
	ApiRestClientInterface mApiClient;
	
	@RestService
	ApiRestClientInterfaceV1 mApiClientV1;
	
	@Extra("roomcode")
	String mRoomCode;
	@Extra("roomname")
	String mRoomName;
	List<DevTypeItem> mDevTypeList = new ArrayList<>();
	List<Map<String, Object>> listItem = new ArrayList<Map<String, Object>>();
	SimpleAdapter mDevTypeAdapter;
	ProgressDialog pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String token = PreferenceUtils.getPrefString(this, PreferenceConstants.ACCESSTOKEN, "");
		mApiClient.setHeader(SharedConst.HTTP_AUTHORIZATION, token);

		pDialog = new ProgressDialog(this);
		pDialog.setMessage(getResources().getString(R.string.progress_loading_msg));
	}

	@AfterViews
	void bindData() {
		setTitle(mRoomName + "-设备类型列表");
		mDevTypeAdapter = new SimpleAdapter(this, listItem, R.layout.area_list_item, new String[] { "num", "name" },
				new int[] { R.id.tv_num, R.id.tv_info });
		mLvDevType.setAdapter(mDevTypeAdapter);
		pDialog.show();
		getRoomDeviceList();
	}

	@Background
	void getRoomDeviceList() {
		try {
			ApiResponse apiResp = mApiClientV1.get_room_dev_list(mRoomCode, "");
			L.e("12121212121213434535465768" + apiResp.getData());
			if (apiResp.getRet() == 0) {
				ObjectMapper mapper = new ObjectMapper();
				List<DevTypeItem> l = mapper.readValue(apiResp.getData(), new TypeReference<List<DevTypeItem>>() {
					
				});
				mDevTypeList.clear();
				mDevTypeList.addAll(l);
				int index = 1;
				for (DevTypeItem devTypeItem : l) {
					Map<String, Object> item = new HashMap<>();
					item.put("num", String.valueOf(index++));
					item.put("name", devTypeItem.getName());
					listItem.add(item);
				}
			}
		} catch (Exception e) {
			L.e(e.toString());
		}
		updateRoomDevListView();
	}

	@UiThread
	void updateRoomDevListView() {
		pDialog.dismiss();
		mDevTypeAdapter.notifyDataSetChanged();
	}

	private static String[] WEBVIEW_MODEL = { "ad", "di", "imem12", "motor_battery" };

	@ItemClick(R.id.lv_items)
	void onRoomItemClicked(int pos) {
		List<DevItem> devList = Arrays.asList(mDevTypeList.get(pos).getDevlist());
		String[] dataId = new String[devList.size()];
		for (int i = 0; i < devList.size(); i++) {
			DevItem devItem = devList.get(i);
			dataId[i] = devItem.getData_id();
		}
		if (mDevTypeList.get(pos).getType().equals("door"))
		{
			DoorActivity_.intent(this).Name(mDevTypeList.get(pos).getDevlist()[0].getName()).DataId(mDevTypeList.get(pos).getDevlist()[0].getData_id()).CanOpen(mDevTypeList.get(pos).getDevlist()[0].getCan_open()).start();
		}else if (Arrays.asList(WEBVIEW_MODEL).contains(mDevTypeList.get(pos).getType())) {
			WebViewActivity_.intent(this)
					.originalUrl(URLs.WAP_BASE_URL + "/loadrealtime?room_code=" + mRoomCode + "&model="
							+ mDevTypeList.get(pos).getType() + "&access_token="
							+ mApiClient.getHeader(SharedConst.HTTP_AUTHORIZATION))
					.title(mDevTypeList.get(pos).getName()).start();
		} else {
			RealtimeActivity_.intent(this).devTypeItem(mDevTypeList.get(pos)).start();
		}
//		Toast.makeText(this, "------------"+mDevTypeList.get(pos).getType(), 0).show();
	}
}
