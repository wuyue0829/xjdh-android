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
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

@EActivity(R.layout.normal_seach_view)
public class MonSeachActivity extends BaseActivity {
	@ViewById(R.id.lv_items)
	ListView mLvSubstation;
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
	int offsets = 0;
	private int offset;
	private int count;
	private int totalItemCount;
	private int lastViewItem;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("实时监控");
		String token = PreferenceUtils.getPrefString(this, PreferenceConstants.ACCESSTOKEN, "");
		mApiClient.setHeader(SharedConst.HTTP_AUTHORIZATION, token);
		mSubstationAdapter = new SubstationListAdapter(this);
	}

	@AfterViews
	void bindData() {

		mLvSubstation.setAdapter(mSubstationAdapter);

		mLvSubstation.setOnScrollListener(new OnScrollListener() {

			private int totalItemCount;

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				if (totalItemCount == lastViewItem && scrollState == SCROLL_STATE_IDLE) {
					if (seachApi&& station!="") {
						
						searchData(station, offsets+=10);
					}else {
					offset+=10;
					getData(true, offset);
					}
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				lastViewItem = firstVisibleItem + visibleItemCount;
				this.totalItemCount = totalItemCount;
			}
		});

		if (mSubstationList.size() == 0) {
		
				offset = 0;
				getData(true, offset+=10);
			
			
		}

		/**
		 * 下拉刷新
		 */
		mSrlAlarm.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				station = et_search.getText().toString();
				if (station.equals(""))
					seachApi = false;
				else
					seachApi = true;
				if (seachApi) {
					count = 0;
					mSrlAlarm.setRefreshing(false);
					mSubstationListSeach.clear();
					searchData(station, count);
				} else {
					offset = 0;
					mSrlAlarm.setRefreshing(false);
					mSubstationList.clear();
					getData(true, offset += 10);
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
			}
		} catch (Exception e) {
			L.e(e.toString());
		}

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
			}
		} catch (Exception e) {
			L.e(e.toString());
		}
	}
	private int num=0;
	@Click(R.id.btn_seach)
	void getSeachData() {
		seachApi = true;
		// pDialog.show();
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
	void notifld() {
		mSubstationAdapter.updateListView(mSubstationList);
	}

	@UiThread
	void notifldSeach() {
		mSubstationAdapter.updateListView(mSubstationListSeach);
	}

	@ItemClick(R.id.lv_items)
	void onSustationListViewClicked(SubstationItem item) {
		RoomListActivity_.intent(this).substationItem(item).start();
	}

}
