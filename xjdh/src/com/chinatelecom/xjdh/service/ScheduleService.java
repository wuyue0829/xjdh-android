package com.chinatelecom.xjdh.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.androidannotations.annotations.EService;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.rest.RestService;
import org.codehaus.jackson.map.ObjectMapper;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.IBinder;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.app.AppManager;
import com.chinatelecom.xjdh.bean.AlarmItem;
import com.chinatelecom.xjdh.bean.AlarmResp;
import com.chinatelecom.xjdh.bean.ApiResponse;
import com.chinatelecom.xjdh.receiver.AppBroadcastReceiver;
import com.chinatelecom.xjdh.receiver.AppBroadcastReceiver.EventHandler;
import com.chinatelecom.xjdh.rest.client.ApiRestClientInterface;
import com.chinatelecom.xjdh.ui.AlarmActivity_;
import com.chinatelecom.xjdh.utils.L;
import com.chinatelecom.xjdh.utils.PreferenceConstants;
import com.chinatelecom.xjdh.utils.PreferenceUtils;
import com.chinatelecom.xjdh.utils.SharedConst;

@EService
public class ScheduleService extends Service implements EventHandler {

	@SystemService
	NotificationManager mNotificationManager;
	private Intent mNotificationIntent;

	public static String ALARM_ACTIVITY_RECEIVER_ACTION = "com.chinatelecom.xjdh.ALARM_ACTIVITY_ACTION";
	BroadcastReceiver alarmActivityReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			clearAllNotifications();
		}
	};

	private double currentMaxAlarmId = 0;
	protected static int SERVICE_NOTIFICATION = 1;
	private Map<Integer, Integer> mNotificationCount = new HashMap<>(0);
	@RestService
	ApiRestClientInterface mApiClient;
	private Timer timer;
	private static String citycode = "", countycode = "", substationId = "", roomId = "", level = "", model = "", startdatetime = "", enddatetime = "";

	public static onNewAlarmServiceListener getNewAlarmServiceListener() {
		return ScheduleService.newAlarmListener;
	}

	public static void setOnNewAlarmServiceListener(onNewAlarmServiceListener newAlarmListener) {
		ScheduleService.newAlarmListener = newAlarmListener;
	}

	private static onNewAlarmServiceListener newAlarmListener;

	public interface onNewAlarmServiceListener {
		void onHasNewAlarm(String latestId);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mNotificationIntent = new Intent(this, AlarmActivity_.class);
		mNotificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ALARM_ACTIVITY_RECEIVER_ACTION);
		registerReceiver(alarmActivityReceiver, intentFilter);
		AppBroadcastReceiver.mListeners.add(this);
		// 开启定时器，每隔20秒刷新一次
		if (timer == null) {
			timer = new Timer();
			timer.scheduleAtFixedRate(new RefreshTask(), 0, 20000);
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		AppBroadcastReceiver.mListeners.remove(this);
		unregisterReceiver(alarmActivityReceiver);
		timer.cancel();
		timer = null;
		super.onDestroy();
	}

	class RefreshTask extends TimerTask {

		@Override
		public void run() {
			L.i("ScheduleService timer task run");
			String latestId = "-1";
			String token = PreferenceUtils.getPrefString(getApplicationContext(), PreferenceConstants.ACCESSTOKEN, "");
			mApiClient.setHeader(SharedConst.HTTP_AUTHORIZATION, token);
			try {
				ApiResponse apiResp = mApiClient.getAlarmList(citycode, countycode, substationId, roomId, level, model, startdatetime, enddatetime, "0",
						String.valueOf(1), "-1");
				if (apiResp.getRet() == 0) {
					ObjectMapper mapper = new ObjectMapper();
					AlarmResp alarmResp = mapper.readValue(apiResp.getData(), AlarmResp.class);
					if (alarmResp.getAlarmlist().length > 0) {
						AlarmItem alarmItem = alarmResp.getAlarmlist()[0];
						latestId = alarmItem.getId();
						if (Double.parseDouble(alarmItem.getId()) > currentMaxAlarmId) {
							if (currentMaxAlarmId != 0 && !AppManager.getAppManager().isCurrentActivity(AlarmActivity_.class)) {
								sendAlarmNotification(alarmItem);
							}
							currentMaxAlarmId = Double.parseDouble(alarmItem.getId());
						}
					}
				}
			} catch (Exception e) {
				L.e(e.toString());
			}
			if (newAlarmListener != null)
				newAlarmListener.onHasNewAlarm(latestId);
		}
	}

	public static void SetRequestParams(String citycode, String countycode, String substationId, String roomId, String level, String model,
			String startdatetime, String enddatetime, String offset, String count, String lastId) {
		ScheduleService.citycode = citycode;
		ScheduleService.countycode = countycode;
		ScheduleService.substationId = substationId;
		ScheduleService.roomId = roomId;
		ScheduleService.level = level;
		ScheduleService.model = model;
		ScheduleService.startdatetime = startdatetime;
		ScheduleService.enddatetime = enddatetime;
	}

	private void sendAlarmNotification(AlarmItem alarmItem) {
		Notification.Builder mNotificationBuilder = new Notification.Builder(this).setSmallIcon(R.drawable.logo)
				.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo)).setPriority(Notification.PRIORITY_MAX).setAutoCancel(true);
		int totalCount = 0;
		int count = 0;
		if (mNotificationCount.containsKey(alarmItem.getLevel())) {
			count = mNotificationCount.get(alarmItem.getLevel());
		}
		mNotificationCount.put(alarmItem.getLevel(), count + 1);

		for (Integer c : mNotificationCount.values()) {
			totalCount += c;
		}
		if (totalCount == 1) {
			boolean isVibrate = PreferenceUtils.getPrefBoolean(this, getString(R.string.new_message_vibrate), true);
			boolean isSound = PreferenceUtils.getPrefBoolean(this, getString(R.string.new_message_sound), true);
			if (isVibrate && isSound) {
				mNotificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND);
			} else if (isVibrate) {
				mNotificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
			} else if (isSound) {
				mNotificationBuilder.setDefaults(Notification.DEFAULT_SOUND);
			}
			mNotificationBuilder.setContentTitle("收到新的告警");
			mNotificationBuilder.setTicker(alarmItem.getSubject());
			mNotificationBuilder.setContentText(alarmItem.getSubject());
		} else {
			mNotificationBuilder.setContentTitle("收到 " + totalCount + " 条新的告警");
			mNotificationBuilder.setContentText("最新告警：" + alarmItem.getSubject());
			mNotificationBuilder.setTicker("最新告警：" + alarmItem.getSubject());
			mNotificationBuilder.setNumber(totalCount);
		}
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, mNotificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		mNotificationBuilder.setContentIntent(pendingIntent);
		mNotificationBuilder.setOngoing(true);
		mNotificationBuilder.setOnlyAlertOnce(true);
		mNotificationManager.notify(1, mNotificationBuilder.build());
	}

	public void clearAllNotifications() {
		mNotificationCount.clear();
		mNotificationManager.cancelAll();
	}

	@Override
	public void onNetChange() {

	}
}
