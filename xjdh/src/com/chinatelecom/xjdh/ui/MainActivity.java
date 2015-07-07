package com.chinatelecom.xjdh.ui;

import java.util.ArrayList;
import java.util.HashMap;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.codehaus.jackson.map.ObjectMapper;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.app.AppContext;
import com.chinatelecom.xjdh.app.AppContext_;
import com.chinatelecom.xjdh.app.AppManager;
import com.chinatelecom.xjdh.bean.ApiResponse;
import com.chinatelecom.xjdh.bean.LoginResponse;
import com.chinatelecom.xjdh.bean.OauthParam;
import com.chinatelecom.xjdh.bean.OauthRespose;
import com.chinatelecom.xjdh.receiver.AppBroadcastReceiver;
import com.chinatelecom.xjdh.receiver.AppBroadcastReceiver.EventHandler;
import com.chinatelecom.xjdh.rest.client.ApiRestClientInterface;
import com.chinatelecom.xjdh.rest.client.OauthRestClientInterface;
import com.chinatelecom.xjdh.service.ScheduleService_;
import com.chinatelecom.xjdh.utils.CryptoUtils;
import com.chinatelecom.xjdh.utils.DialogUtils;
import com.chinatelecom.xjdh.utils.FileUtils;
import com.chinatelecom.xjdh.utils.L;
import com.chinatelecom.xjdh.utils.PreferenceConstants;
import com.chinatelecom.xjdh.utils.PreferenceUtils;
import com.chinatelecom.xjdh.utils.SharedConst;
import com.chinatelecom.xjdh.utils.T;
import com.chinatelecom.xjdh.utils.UpdateManager;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity implements EventHandler {
	// main board
	@ViewById(R.id.main_gridview)
	GridView mMainGrid;
	@Extra("isDoLogin")
	boolean isDoLogin = false;
	SimpleAdapter mMainGridAdapter;
	int[] main_image_array = { R.drawable.ic_compass, R.drawable.ic_monitor, R.drawable.ic_bell, R.drawable.ic_chart, R.drawable.ic_message,
			R.drawable.ic_help, R.drawable.ic_user, R.drawable.ic_setting };
	String[] main_name_array = { "地图", "实时监控", "告警处理", "数据报表", "消息中心", "帮助中心", "用户中心", "设置" };
	@RestService
	ApiRestClientInterface mApiClient;
	// menu
	View mSysMenuView;
	Dialog mSysMenuDialog;// menu菜单Dialog
	GridView mSysMenuGrid;
	SimpleAdapter menuAdapter;
	int[] menu_image_array = { R.drawable.index_btn_help, R.drawable.index_btn_feedback, R.drawable.index_btn_update, R.drawable.index_btn_exit };
	String[] menu_name_array = { "帮助", "意见反馈", "检查更新", "退出" };

	@AfterViews
	void initData() {
		ArrayList<HashMap<String, Object>> menuData = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < main_name_array.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("itemImage", main_image_array[i]);
			map.put("itemText", main_name_array[i]);
			menuData.add(map);
		}
		mMainGridAdapter = new SimpleAdapter(this, menuData, R.layout.main_grid_item, new String[] { "itemImage", "itemText" }, new int[] {
				R.id.menuitem_image, R.id.menuitem_text });
		mMainGrid.setAdapter(mMainGridAdapter);
		setTitle("首页");
		if (!AppContext_.getInstance().isNetworkConnected()) {
			T.showLong(this, "网络未连接，无法登陆");
		}
		pDialog = new ProgressDialog(this);
		pDialog.setCancelable(false);
		pDialog.setMessage("正在验证登录，请稍后...");

		if (isDoLogin) {
			pDialog.show();
			doLogin();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@ItemClick(R.id.main_gridview)
	void onMainGridClicked(int position) {
		switch (position) {
		case 0:
			LocationDemo_.intent(this).start();
			break;
		case 1:
			MonitorActivity_.intent(this).start();
			break;
		case 2:
			AlarmActivity_.intent(this).start();
			break;
		case 3:
			ChartActivity_.intent(this).start();
			break;
		case 4:
			T.showLong(this, "此功能还未开发完成");
			break;
		case 5:
			T.showLong(this, "此功能还未开发完成");
			break;
		case 6:
			UserDetailActivity_.intent(this).start();
			break;
		case 7:
			SettingActivity_.intent(this).start();
			break;
		default:
			break;
		}
	}

	void createMenu() {
		mSysMenuView = View.inflate(this, R.layout.systemmenu, null);
		mSysMenuDialog = new Dialog(this, R.style.MENU_Dialog_Fullscreen);
		mSysMenuDialog.setContentView(mSysMenuView);
		mSysMenuDialog.setCancelable(true);
		mSysMenuDialog.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_MENU)// 监听按键
					dialog.dismiss();
				return false;
			}
		});
		mSysMenuGrid = (GridView) mSysMenuView.findViewById(R.id.menu_gridview);
		mSysMenuView.findViewById(R.id.menu_fill_view).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mSysMenuDialog.dismiss();
			}
		});
		ArrayList<HashMap<String, Object>> menuData = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < menu_name_array.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("itemImage", menu_image_array[i]);
			map.put("itemText", menu_name_array[i]);
			menuData.add(map);
		}
		menuAdapter = new SimpleAdapter(this, menuData, R.layout.systemmenu_item, new String[] { "itemImage", "itemText" }, new int[] { R.id.menuitem_image,
				R.id.menuitem_text });
		mSysMenuGrid.setAdapter(menuAdapter);
		mSysMenuGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				mSysMenuDialog.dismiss();
				switch (position) {
				case 0:
					T.showLong(MainActivity.this, "此功能还未开发完成");
					break;
				case 1:
					FeedBackActivity_.intent(MainActivity.this).start();
					break;
				case 2:
					UpdateManager.getUpdateManager().checkAppUpdate(MainActivity.this, true);
					break;
				case 3:
					DialogUtils.buildExitDialog(MainActivity.this);
					break;
				default:
					break;
				}
			}
		});
	}

	// @Override
	// public boolean onMenuOpened(int featureId, Menu menu) {
	// if (mSysMenuDialog == null) {
	// mSysMenuDialog = new Dialog(this, R.style.MENU_Dialog_Fullscreen);
	// mSysMenuDialog.setContentView(mSysMenuView);
	// } else {
	// Window window = mSysMenuDialog.getWindow();
	// window.setGravity(Gravity.BOTTOM);
	// mSysMenuDialog.show();
	// }
	// return false;
	// }

	@Override
	public void onBackPressed() {
		moveTaskToBack(true);
	}

	@Override
	protected void onDestroy() {

		if (AppContext_.isServiceRunning(this, ScheduleService_.class.getName())
				&& !PreferenceUtils.getPrefBoolean(this, getResources().getString(R.string.new_message_background), false))
			ScheduleService_.intent(this).stop();
		super.onDestroy();
	}

	@Background
	void getFilterData() {
		try {
			ApiResponse apiResp = mApiClient.getAreaData();
			if (apiResp.getRet() == 0) {
				FileUtils.setToData(this, SharedConst.FILE_AREA_JSON, apiResp.getData().getBytes());
			}
		} catch (Exception e) {
			L.e(e.toString());
		}

		try {
			ApiResponse apiResp = mApiClient.getDevModelData();
			if (apiResp.getRet() == 0) {
				FileUtils.setToData(this, SharedConst.FILE_MODEL_JSON, apiResp.getData().getBytes());
			}
		} catch (Exception e) {
			L.e(e.toString());
		}
	}

	@RestService
	OauthRestClientInterface mOauthClient;
	ProgressDialog pDialog;
	boolean isLogined = false;

	@Background(delay = 1000)
	void doLogin() {
		String password = PreferenceUtils.getPrefString(this, PreferenceConstants.PASSWORD, "");
		String account = PreferenceUtils.getPrefString(this, PreferenceConstants.ACCOUNT, "");
		try {
			String pwdStr = CryptoUtils.decrypt(SharedConst.PASSWORD_CRYPRO_SEED, SharedConst.CLIENT_PASSWORD);
			OauthParam param = new OauthParam(SharedConst.CLIENT_ID, pwdStr, SharedConst.CLIENT_REDIRECT_URL, account, password);
			LoginResponse resp = mOauthClient.login(param);
			onLoginResult(resp);
			return;
		} catch (Exception e) {
			L.e(e.toString());
		}
		onLoginResult(new LoginResponse(-1, "", ""));
	}

	@UiThread
	void onLoginResult(LoginResponse resp) {
		pDialog.dismiss();
		if (resp.getRet() == 0) {
			isLogined = true;
			ObjectMapper mapper = new ObjectMapper();
			try {
				OauthRespose mOauthResp = mapper.readValue(resp.getResponse(), OauthRespose.class);
				PreferenceUtils.setPrefString(this, PreferenceConstants.ACCESSTOKEN, mOauthResp.getAccess_token());
				PreferenceUtils.setPrefInt(this, PreferenceConstants.EXPIRES, mOauthResp.getExpires());
				PreferenceUtils.setPrefInt(this, PreferenceConstants.EXPIRE_IN, mOauthResp.getExpires_in());
				PreferenceUtils.setPrefString(this, PreferenceConstants.REFRESHTOKEN, mOauthResp.getRefresh_token());
				mApiClient.setHeader(SharedConst.HTTP_AUTHORIZATION, mOauthResp.getAccess_token());
				if (!AppContext_.isServiceRunning(this, ScheduleService_.class.getName()))
					ScheduleService_.intent(this).start();
				if (AppContext.getInstance().isNetworkConnected())
					UpdateManager.getUpdateManager().checkAppUpdate(this, false);
				getFilterData();
				return;
			} catch (Exception e) {
				L.e(e.toString());
			}
		} else if (resp.getRet() == 2) {
			T.showLong(this, "账户名或密码有误");
			String account = PreferenceUtils.getPrefString(this, PreferenceConstants.ACCOUNT, "");
			PreferenceUtils.clearPreference(this, PreferenceManager.getDefaultSharedPreferences(this));
			PreferenceUtils.setPrefString(this, PreferenceConstants.ACCOUNT, account);
			AppManager.getAppManager().finishAllActivity();
			LoginActivity_.intent(this).start();
			return;
		}
		T.showShort(this, "登陆失败");
	}

	@Override
	protected void onResume() {
		AppBroadcastReceiver.mListeners.add(this);
		super.onResume();
	}

	@Override
	protected void onPause() {
		AppBroadcastReceiver.mListeners.remove(this);
		super.onPause();
	}

	@Override
	public void onNetChange() {
		if (AppContext_.getInstance().isNetworkConnected()) {
			T.showLong(this, getResources().getString(R.string.network_up));
			if (!isLogined)
				doLogin();
		} else {
			T.showLong(this, getResources().getString(R.string.network_down));
		}

	}
}
