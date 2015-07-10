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

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.adapter.SubstationListAdapter;
import com.chinatelecom.xjdh.bean.ApiResponse;
import com.chinatelecom.xjdh.bean.CityItem;
import com.chinatelecom.xjdh.bean.CountyItem;
import com.chinatelecom.xjdh.bean.SubstationItem;
import com.chinatelecom.xjdh.rest.client.ApiRestClientInterface;
import com.chinatelecom.xjdh.utils.DialogUtils;
import com.chinatelecom.xjdh.utils.FileUtils;
import com.chinatelecom.xjdh.utils.L;
import com.chinatelecom.xjdh.utils.PreferenceConstants;
import com.chinatelecom.xjdh.utils.PreferenceUtils;
import com.chinatelecom.xjdh.utils.SharedConst;
import com.chinatelecom.xjdh.utils.T;

@EActivity(R.layout.activity_monitor)
public class MonitorActivity extends BaseActivity {
	@ViewById(R.id.lv_substation)
	ListView mLvSubstation;
	@ViewById(R.id.tv_refresh)
	TextView mTvRefresh;
	private List<CityItem> cityList = new ArrayList<CityItem>(0);
	private List<SubstationItem> mSubstationList = new ArrayList<SubstationItem>();
	private SubstationListAdapter mSubstationAdapter;
	@RestService
	ApiRestClientInterface mApiClient;
	ProgressDialog pDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("实时监控");
		String token = PreferenceUtils.getPrefString(this, PreferenceConstants.ACCESSTOKEN, "");
		mApiClient.setHeader(SharedConst.HTTP_AUTHORIZATION, token);
		pDialog = new ProgressDialog(this);
		pDialog.setMessage(getResources().getString(R.string.progress_loading_msg));
	}

	@AfterViews
	void bindData() {
		mTvRefresh.setText("加载失败，点击重新加载");
		String cityData = new String(FileUtils.getFileData(this, SharedConst.FILE_AREA_JSON));
		try {
			ObjectMapper mapper = new ObjectMapper();
			List<CityItem> l = mapper.readValue(cityData, new TypeReference<List<CityItem>>() {
			});
			cityList.clear();
			cityList.addAll(l);
			mSubstationList.clear();
			for (CityItem cityItem : l) {
				for (CountyItem countyitem : cityItem.getCountylist()) {
					for (SubstationItem substationItem : countyitem.getSubstationlist()) {
						substationItem.setName(cityItem.getName() + "->" + countyitem.getName() + "->" + substationItem.getName());
						mSubstationList.add(substationItem);
					}
				}
			}
		} catch (Exception e) {
			L.e(e.toString());
		}
		mSubstationAdapter = new SubstationListAdapter(this, mSubstationList);
		mLvSubstation.setAdapter(mSubstationAdapter);
		if (mSubstationList.size() == 0) {
			pDialog.show();
			getData();
		}
	}

	@Background
	void getData() {
		int tryCount = 0;
		do {
			try {
				ApiResponse apiResp = mApiClient.getAreaData();
				if (apiResp.getRet() == 0) {
					FileUtils.setToData(this, SharedConst.FILE_AREA_JSON, apiResp.getData().getBytes());
					ObjectMapper mapper = new ObjectMapper();
					List<CityItem> l = mapper.readValue(apiResp.getData(), new TypeReference<List<CityItem>>() {
					});
					cityList.clear();
					cityList.addAll(l);
					mSubstationList.clear();
					for (CityItem cityItem : l) {
						for (CountyItem countyitem : cityItem.getCountylist()) {
							for (SubstationItem substationItem : countyitem.getSubstationlist()) {
								substationItem.setName(cityItem.getName() + "->" + countyitem.getName() + "->" + substationItem.getName());
								mSubstationList.add(substationItem);
							}
						}
					}
					break;
				}
			} catch (Exception e) {
				L.e(e.toString());
			}
		} while (tryCount++ < 5);
		updateSubstationListview(tryCount < 5);
	}

	@UiThread
	void updateSubstationListview(boolean isSuccess) {
		pDialog.dismiss();
		T.showLong(this, isSuccess ? "加载完成" : "加载失败");
		mTvRefresh.setVisibility(isSuccess ? View.GONE : View.VISIBLE);
		mSubstationAdapter.notifyDataSetChanged();
	}

	@Click(R.id.tv_refresh)
	void onTvRefreshClicked() {
		pDialog.show();
		getData();
	}

	@ItemClick(R.id.lv_substation)
	void onSustationListViewClicked(SubstationItem item) {
		RoomListActivity_.intent(this).substationItem(item).start();
	}
}
