package com.chinatelecom.xjdh.ui;

import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.bean.AlarmItem;
import com.chinatelecom.xjdh.bean.ApiResponse;
import com.chinatelecom.xjdh.bean.DevTypeItem;
import com.chinatelecom.xjdh.rest.client.ApiRestClientInterface;
import com.chinatelecom.xjdh.utils.L;
import com.chinatelecom.xjdh.utils.PreferenceConstants;
import com.chinatelecom.xjdh.utils.PreferenceUtils;
import com.chinatelecom.xjdh.utils.SharedConst;
import com.chinatelecom.xjdh.utils.T;
import com.chinatelecom.xjdh.utils.URLs;

@EActivity(R.layout.activity_alarm_detail)
public class AlarmDetailActivity extends BaseActivity {
	@ViewById(R.id.btn_monitor)
	Button btnMonitor;
	@ViewById(R.id.tv_alarm_detail_add_datetime)
	TextView tvAlarmDetailAddDatetime;
	@ViewById(R.id.tv_alarm_detail_city)
	TextView tvAlarmDetailCity;
	@ViewById(R.id.tv_alarm_detail_content)
	TextView tvAlarmDetailContent;
	@ViewById(R.id.tv_alarm_detail_county)
	TextView tvAlarmDetailCounty;
	@ViewById(R.id.tv_alarm_detail_dev_name)
	TextView tvAlarmDetailDevName;
	@ViewById(R.id.tv_alarm_detail_level)
	TextView tvAlarmDetailLevel;
	@ViewById(R.id.tv_alarm_detail_model)
	TextView tvAlarmDetailModel;
	@ViewById(R.id.tv_alarm_detail_room)
	TextView tvAlarmDetailRoom;
	@ViewById(R.id.tv_alarm_detail_status)
	TextView tvAlarmDetailStatus;
	@ViewById(R.id.tv_alarm_detail_substation)
	TextView tvAlarmDetailSubstation;
	@Extra("alarmItem")
	AlarmItem alarmItem;
	@RestService
	ApiRestClientInterface mApiClient;
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
		setTitle("告警详情");
		tvAlarmDetailAddDatetime.setText(alarmItem.getAdded_datetime());
		tvAlarmDetailCity.setText(alarmItem.getCity());
		tvAlarmDetailContent.setText(alarmItem.getSubject());
		tvAlarmDetailCounty.setText(alarmItem.getCounty());
		tvAlarmDetailDevName.setText(alarmItem.getDev_name());
		String level = "一级";
		switch (alarmItem.getLevel()) {
		case 1:
			level = "一级";
			break;
		case 2:
			level = "二级";
			break;
		case 3:
			level = "三级";
			break;
		case 4:
			level = "四级";
			break;
		default:
			break;
		}

		tvAlarmDetailLevel.setText(level);

		String modelKey = alarmItem.getDev_model();
		if (SharedConst.DEV_MODEL_MAP.containsKey(modelKey)) {
			tvAlarmDetailModel.setText(SharedConst.DEV_MODEL_MAP.get(modelKey));
		} else {
			tvAlarmDetailModel.setText("其他设备类型");
		}
		if (modelKey.equalsIgnoreCase("smd_device"))
			btnMonitor.setVisibility(View.GONE);
		tvAlarmDetailRoom.setText(alarmItem.getRoom_name());
		if (alarmItem.getStatus().equalsIgnoreCase("unsloved")) {
			tvAlarmDetailStatus.setText("未处理");
			tvAlarmDetailStatus.setTextColor(Color.RED);
		} else if (alarmItem.getStatus().equalsIgnoreCase("sloving")) {
			tvAlarmDetailStatus.setText("处理中");
			tvAlarmDetailStatus.setTextColor(Color.YELLOW);
		} else if (alarmItem.getStatus().equalsIgnoreCase("sloved")) {
			tvAlarmDetailStatus.setText("处理完成");
			tvAlarmDetailStatus.setTextColor(Color.GREEN);
		}
		tvAlarmDetailSubstation.setText(alarmItem.getSubstation_name());
	}

	@Click(R.id.btn_monitor)
	void onBtnMonitorClicked() {
		String type = "", typeName = "";
		if (alarmItem.getDev_model().equalsIgnoreCase("water") || alarmItem.getDev_model().equalsIgnoreCase("smoke")) {
			type = "di";
			typeName = "开关量";
		} else if (alarmItem.getDev_model().equalsIgnoreCase("temperature") || alarmItem.getDev_model().equalsIgnoreCase("humid")) {
			type = "ad";
			typeName = "模拟量";
		} else if (alarmItem.getDev_model().equalsIgnoreCase("imem_12")) {
			type = "imem12";
			typeName = "智能电表";
		}
		if (type.equalsIgnoreCase("ad") || type.equalsIgnoreCase("di") || type.equalsIgnoreCase("imem12")) {
			WebViewActivity_
					.intent(this)
					.originalUrl(
							URLs.WAP_BASE_URL + "/loadrealtime?room_code=" + alarmItem.getRoom_code() + "&model=" + type + "&access_token="
									+ mApiClient.getHeader(SharedConst.HTTP_AUTHORIZATION)).title(typeName).start();
		} else {
			pDialog.show();
			getData();
		}
	}

	@Background
	void getData() {
		try {
			ApiResponse apiResp = mApiClient.getRoomDeviceList(alarmItem.getRoom_code(), alarmItem.getDev_model());
			if (apiResp.getRet() == 0) {
				ObjectMapper mapper = new ObjectMapper();
				List<DevTypeItem> l = mapper.readValue(apiResp.getData(), new TypeReference<List<DevTypeItem>>() {
				});
				if (l.size() > 0) {
					onResult(true, l.get(0));
					return;
				}
			}
		} catch (Exception e) {
			L.e(e.toString());
		}
		onResult(false, null);
	}

	@UiThread
	void onResult(boolean isSuccess, DevTypeItem devTypeItem) {
		pDialog.dismiss();
		if (isSuccess) {
			RealtimeActivity_.intent(this).devTypeItem(devTypeItem).start();
		} else {
			T.showLong(this, "加载数据失败");
		}
	}
}
