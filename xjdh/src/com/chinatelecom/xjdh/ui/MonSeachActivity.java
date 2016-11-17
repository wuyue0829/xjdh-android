package com.chinatelecom.xjdh.ui;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.adapter.SubstationListAdapter;
import com.chinatelecom.xjdh.bean.ApiResponse;
import com.chinatelecom.xjdh.bean.SubstationItem;
import com.chinatelecom.xjdh.rest.client.ApiRestClientInterface;
import com.chinatelecom.xjdh.utils.L;
import com.chinatelecom.xjdh.utils.PreferenceConstants;
import com.chinatelecom.xjdh.utils.PreferenceUtils;
import com.chinatelecom.xjdh.utils.SharedConst;
import com.chinatelecom.xjdh.utils.T;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

@EActivity(R.layout.normal_seach_view)
public class MonSeachActivity extends BaseActivity{
	@ViewById(R.id.lv_items)
	ListView mLvSubstation;
	@ViewById(R.id.tv_new_message)
	TextView mTvRefresh;
	@ViewById(R.id.et_search)
	EditText et_search;
	@ViewById(R.id.srl_alarm)

	SwipeRefreshLayout mSrlAlarm;

	@ViewById(R.id.btn_seach)
	Button btn_seach;
	private List<SubstationItem> mSubstationListSeach = new ArrayList<SubstationItem>(0);
	private List<SubstationItem> mSubstationList = new ArrayList<SubstationItem>(0);
	private SubstationListAdapter mSubstationAdapter;
	private boolean isRefreshing = false;
	private boolean seachApi = false;
	@RestService
	ApiRestClientInterface mApiClient;
	ProgressDialog pDialog;
	LinearLayout footerView;
	TextView footerMsg;

	ApiResponse apiResp;

	private String station;
	private String sbarea = "";// 所属分区

	private int offset = 10;
	private int count = 10;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("实时监控");
		String token = PreferenceUtils.getPrefString(this, PreferenceConstants.ACCESSTOKEN, "");
		mApiClient.setHeader(SharedConst.HTTP_AUTHORIZATION, token);
		// pDialog = new ProgressDialog(this);
		// pDialog.setMessage(getResources().getString(R.string.progress_loading_msg));
		mSubstationAdapter = new SubstationListAdapter(this);
		footerView = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.listview_footer, null);
		footerMsg = (TextView) footerView.findViewById(R.id.footer_msg);
		// 点击加载更多
		footerMsg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (seachApi) {
					count+=10;
					searchData(station, count);
				} else {
					offset+=10;
					getData(false, offset);
				}
				pDialog.show();
				footerMsg.setText("加载中....");
			}
		});
		if (pDialog == null) {
			pDialog = new ProgressDialog(this);
			pDialog.setMessage("加载数据中...");
			pDialog.setCancelable(true);
		}
	}

	@AfterViews
	void bindData() {

		mLvSubstation.addFooterView(footerView);
		mLvSubstation.setAdapter(mSubstationAdapter);

		mTvRefresh.setText("加载失败，下拉刷新");
		
			if (mSubstationList.size() == 0) {
				pDialog.show();
				getData(true, offset);
			}

		/**
		 * 下拉刷新
		 */
		mSrlAlarm.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				station = et_search.getText().toString();
				if(station.equals(""))
					seachApi = false;
				else
					seachApi = true;
				if (seachApi) {
					count = 10;
					mSrlAlarm.setRefreshing(false);
					pDialog.show();
					mSubstationListSeach.clear();
					searchData(station, count);
				} else {
					offset = 10;
					mSrlAlarm.setRefreshing(false);
					pDialog.show();
					mSubstationList.clear();
					getData(true, offset);
				}

			}
		});
		
	}

	@Background
	void getData(boolean isRefreshing, int offset) {
		try {
			apiResp = mApiClient.getSubstationList(sbarea, offset);

			if (apiResp.getRet() == 0) {
				ObjectMapper mapper = new ObjectMapper();
				L.d("@@@@@@@@@@@@@@@@@@@@22", mapper.writeValueAsString(apiResp.getData().toString()));
				List<SubstationItem> item = mapper.readValue(apiResp.getData(),
						new TypeReference<List<SubstationItem>>() {
						});
				mSubstationList.clear();
				mSubstationList.addAll(item);
				notifld();
				updateAlarmListView(isRefreshing, item.size() > 0, item.size()%10 !=0);
			}
//			else {
//				updateAlarmListView(isRefreshing, true, true);
//			}
		} catch (Exception e) {
			L.e(e.toString());
		}
//		updateAlarmListView(isRefreshing, false, false);

	}

	@Background
	public void searchData(String station, int count) {
		try {
			apiResp = mApiClient.getSubstationList(station, count);

			if (apiResp.getRet() == 0) {
				ObjectMapper mapper = new ObjectMapper();
				L.d("@@@@@@@@@@@@@@@@@@@@22", mapper.writeValueAsString(apiResp.getData().toString()));
				List<SubstationItem> item = mapper.readValue(apiResp.getData(),
						new TypeReference<List<SubstationItem>>() {
						});

				mSubstationListSeach.clear();
				mSubstationListSeach.addAll(item);
				notifldSeach();
				updateAlarmListView(isRefreshing, item.size() > 0, item.size()%10  != 0);
			} 
//			else {
//				updateAlarmListView(isRefreshing, true, true);
//			}
		} catch (Exception e) {
			L.e(e.toString());
		}
//		updateAlarmListView(isRefreshing, false, false);
	}

	@Click(R.id.btn_seach)
	void getSeachData() {
		seachApi=true;
		pDialog.show();
		mSubstationListSeach.clear();
		station = et_search.getText().toString();
		if (station.equals("")) {
			offset = 0;
			getData(false, offset);
		} else {
			count = 0;
			searchData(station, count);
		}
	}

	@UiThread
	void notifld(){
		mSubstationAdapter.updateListView(mSubstationList);
	}
	@UiThread
	void notifldSeach(){
		mSubstationAdapter.updateListView(mSubstationListSeach);
	}
	
	@UiThread
	void updateAlarmListView(boolean isRefreshing, boolean isSuccess, boolean isLoadAll) {
		mSrlAlarm.setRefreshing(false);
		if (pDialog.isShowing() && pDialog.isShowing()) {
			L.d("updateAlarmListView pdialog dismiss");
			pDialog.dismiss();
		}

		if (isRefreshing) {
			if (isSuccess)
				mTvRefresh.setVisibility(View.GONE);
			else
				mTvRefresh.setText("加载失败，点击重试");
		}

		if (!isLoadAll) {
			footerMsg.setText("点击加载更多");
			footerMsg.setClickable(true);
		} else {
			T.showLong(this, "已经加载全部");
			footerMsg.setText("已经加载全部");
			footerMsg.setClickable(false);
		}
		if (isSuccess) {
			mSubstationAdapter.notifyDataSetChanged();
		}
	}

	@Click(R.id.tv_new_message)
	void onTvRefreshClicked() {
		pDialog.show();
		getData(true, 10);
	}

	@ItemClick(R.id.lv_items)
	void onSustationListViewClicked(SubstationItem item) {
		RoomListActivity_.intent(this).substationItem(item).start();
	}

}
