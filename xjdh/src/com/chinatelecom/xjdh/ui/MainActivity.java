package com.chinatelecom.xjdh.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.codehaus.jackson.map.ObjectMapper;

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
import com.chinatelecom.xjdh.utils.FileUtils;
import com.chinatelecom.xjdh.utils.L;
import com.chinatelecom.xjdh.utils.PreferenceConstants;
import com.chinatelecom.xjdh.utils.PreferenceUtils;
import com.chinatelecom.xjdh.utils.SharedConst;
import com.chinatelecom.xjdh.utils.T;
import com.chinatelecom.xjdh.utils.Update;
import com.chinatelecom.xjdh.utils.UpdateManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

/**
 * @author peter
 * 
 */
@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity implements EventHandler {
	class DashboardItem {
		int imageRes;
		String name;
		Class<?> activity;

		public DashboardItem(int imageRes, String name, Class<?> activity) {
			super();
			this.imageRes = imageRes;
			this.name = name;
			this.activity = activity;
		}
	}

	// main board
	@ViewById(R.id.main_gridview)
	GridView mMainGrid;
	@Extra("isDoLogin")
	boolean isDoLogin = false;
	SimpleAdapter mMainGridAdapter;
	private List<DashboardItem> dashboardList = new ArrayList<MainActivity.DashboardItem>() {
		private static final long serialVersionUID = 6541190919019797339L;

		{
			//add(new DashboardItem(R.drawable.ic_compass, "地图", LocationDemo_.class));
			add(new DashboardItem(R.drawable.ic_monitor, "实时监控", MonSeachActivity_.class));
			add(new DashboardItem(R.drawable.ic_bell, "告警处理", AlarmActivity_.class));
			// add(new DashboardItem(R.drawable.ic_dashboard, "油机管理", null));
			add(new DashboardItem(R.drawable.ic_chart, "数据报表", ChartActivity_.class));
			add(new DashboardItem(R.drawable.ic_message, "消息中心", MessageCenterActivity_.class));
			add(new DashboardItem(R.drawable.ic_help, "帮助中心", null));
			add(new DashboardItem(R.drawable.ic_user, "用户中心", UserDetailActivity_.class));
			add(new DashboardItem(R.drawable.ic_setting, "设置", SettingActivity_.class));
			add(new DashboardItem(R.drawable.ic_bell, "预告警处理", PreAlarmActivity_.class));
			add(new DashboardItem(R.drawable.station_collect, "局站采集", StationCollectActivity_.class));
			add(new DashboardItem(R.drawable.list, "局站列表", StationListGroupingActivity_.class));
			add(new DashboardItem(R.drawable.nfc,"读取卡号", ReadNfcNumber_.class));
			add(new DashboardItem(R.drawable.ic_device,"现场测试", TestEquipmentActivity_.class));
		}
	};
	@RestService
	ApiRestClientInterface mApiClient;
	private int curVersionCode;
	private Update mUpdate;
	private String curVersionName = "";

	@AfterViews
	void initData() {
		ArrayList<HashMap<String, Object>> menuData = new ArrayList<HashMap<String, Object>>();
		for (DashboardItem item : dashboardList) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("itemImage", item.imageRes);
			map.put("itemText", item.name);
			menuData.add(map);
		}
		mMainGridAdapter = new SimpleAdapter(this, menuData, R.layout.main_grid_item,
				new String[] { "itemImage", "itemText" }, new int[] { R.id.menuitem_image, R.id.menuitem_text });
		mMainGrid.setAdapter(mMainGridAdapter);
		setTitle("首页");
		if (!AppContext.getInstance().isNetworkConnected()) {
			T.showLong(this, "网络未连接，无法登陆");
		}
		pDialog = new ProgressDialog(this);
		pDialog.setCancelable(true);
		pDialog.setMessage("正在验证登录，请稍后...");
		if (isDoLogin && !pDialog.isShowing()) {
			pDialog.show();
			doLogin();
		}

	}

	/**
	 * 重启Activity检测APP版本
	 */
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		updateApp();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		updateApp();
	}

	@ItemClick(R.id.main_gridview)
	void onMainGridClicked(int position) {
		if (position <= 12) {
			Class<?> cls = dashboardList.get(position).activity;
			if (cls != null) {
				Intent i = new Intent(this, cls);
				startActivity(i);
				return;
			}
		}
		T.showLong(this, "此功能还未开发完成");

	}

	@Override
	public void onBackPressed() {
		moveTaskToBack(true);
	}

	@Override
	protected void onDestroy() {
		if (AppContext.isServiceRunning(this, ScheduleService_.class.getName()) && !PreferenceUtils.getPrefBoolean(this,
				getResources().getString(R.string.new_message_background), false))
			ScheduleService_.intent(this).stop();
		super.onDestroy();
	}

	private String city = "all";
	private String station = "all";
	private String stationNames = "all";

	@Background
	void getFilterData() {
		int retryTimes = 0;
		while (retryTimes++ < 5) {
			try {
				ApiResponse apiResp = mApiClient.getAreaData(city, station, stationNames);
				if (apiResp.getRet() == 0) {
					FileUtils.setToData(this, SharedConst.FILE_AREA_JSON, apiResp.getData().getBytes());
					break;
				}
			} catch (Exception e) {
				L.e(e.toString());
			}
		}
		retryTimes = 0;
		while (retryTimes++ < 5) {
			try {
				ApiResponse apiResp = mApiClient.getDevModelData();
				if (apiResp.getRet() == 0) {
					FileUtils.setToData(this, SharedConst.FILE_MODEL_JSON, apiResp.getData().getBytes());
					break;
				}
			} catch (Exception e) {
				L.e(e.toString());
			}
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
			OauthParam param = new OauthParam(SharedConst.CLIENT_ID, pwdStr, SharedConst.CLIENT_REDIRECT_URL, account,
					password);
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
				if (!AppContext.isServiceRunning(this, ScheduleService_.class.getName()))
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
		if (AppContext.getInstance().isNetworkConnected()) {
			T.showLong(this, getResources().getString(R.string.network_up));
			if (!isLogined)
				doLogin();
		} else {
			T.showLong(this, getResources().getString(R.string.network_down));
		}
	}

	private long exitTime = 0;

	/**
	 * 监听手机物理键（返回）
	 */
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) { //
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				// System.exit(0);
				Intent intent = new Intent(Intent.ACTION_MAIN);// 跳到手机（home）主界面
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//
				intent.addCategory(Intent.CATEGORY_HOME);
				this.startActivity(intent);
			}
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_MENU) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 检测更新App
	 */
	public void updateApp() {
		getCurrentVersion();
		new Thread(new Runnable() {

			@Override
			public void run() {
				Update u = AppContext_.getInstance().getUpdateInfo();
				Message msg = new Message();
				if (u != null) {
					msg.what = 1;
					msg.obj = u;
				} else {
					msg.what = 0;
				}
				handler.sendMessage(msg);
			}
		}).start();
	}

	final Handler handler=new Handler(){public void handleMessage(Message msg){
	// 显示检测结果
	if(msg.what==1){mUpdate=(Update)msg.obj;if(mUpdate!=null){if(curVersionCode<mUpdate.getVersionCode()){UpdateManager.getUpdateManager().checkAppUpdate(MainActivity.this,true);// 检测更新APP版本
	}}}}};

	/**
	 * 获取当前客户端版本信息
	 */
	private void getCurrentVersion() {
		try {
			PackageInfo info = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
			curVersionName = info.versionName;
			curVersionCode = info.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace(System.err);
		}
	}
}
