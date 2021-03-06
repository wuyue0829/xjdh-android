package com.chinatelecom.xjdh.fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.codehaus.jackson.map.ObjectMapper;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.adapter.CityAdapter;
import com.chinatelecom.xjdh.bean.ApiResponse;
import com.chinatelecom.xjdh.bean.ApiResponseStationList;
import com.chinatelecom.xjdh.bean.Country;
import com.chinatelecom.xjdh.bean.UserInfo;
import com.chinatelecom.xjdh.rest.client.ApiRestClientInterface;
import com.chinatelecom.xjdh.ui.StationActyivity_;
import com.chinatelecom.xjdh.ui.UncommittActivity_;
import com.chinatelecom.xjdh.utils.L;
import com.chinatelecom.xjdh.utils.PreferenceConstants;
import com.chinatelecom.xjdh.utils.PreferenceUtils;
import com.chinatelecom.xjdh.utils.SharedConst;
import com.chinatelecom.xjdh.utils.T;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.widget.ListView;

@EFragment(R.layout.activity_download_station)
public class PendingFragment extends Fragment{
	@ViewById(R.id.lv_stationName_grouping)
	ListView lv_stationName_grouping;
	@RestService
	ApiRestClientInterface mApiClient;
	private CityAdapter adapter;
	UserInfo mUserInfo;
	@ViewById(R.id.srl_alarm)
	SwipeRefreshLayout mSrlAlarm;
	private List<Country> stList = new ArrayList<Country>();
	private final static int LOCATYPE=1;
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		String token = PreferenceUtils.getPrefString(getActivity(),PreferenceConstants.ACCESSTOKEN, "");
		mApiClient.setHeader(SharedConst.HTTP_AUTHORIZATION, token);
		adapter = new CityAdapter(getActivity());
		stList.clear();
		getUserInfo();
	}
	
	@AfterViews
	void showData() {
		mSrlAlarm.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				mSrlAlarm.setRefreshing(false);
				stList.clear();
				getUserInfo();
			}
		});
	}
	
	@Background
	void getUserInfo() {
		try {
			ApiResponse mApiResps = mApiClient.getUserInfo();
			if (mApiResps.getRet() == 0) {
				ObjectMapper mapper = new ObjectMapper();
				mUserInfo = mapper.readValue(mApiResps.getData(), UserInfo.class);
				getData(LOCATYPE,mUserInfo.getId(),LOCATYPE);
				return;
			}
		} catch (Exception e) {
			L.e(e.toString());
		}
	}

	@Background
	void getData(int type,int user_id,int locatype) {
		try {
			L.d(">>>>>>>>>>>>", String.valueOf(user_id));
			ApiResponse apiResp = mApiClient.GetLocation(type,user_id,locatype);
			L.d("22222222222222222222", apiResp.getData());
			if (apiResp.getRet() == 0) {
				ObjectMapper mapper = new ObjectMapper();
				ApiResponseStationList apiResponseStationList = mapper.readValue(apiResp.getData(),
						ApiResponseStationList.class);
				List<Country> list = Arrays.asList(apiResponseStationList.getCheckLocList());
				stList.addAll(list);
				if (apiResp.getRet() == 0) {
					ShowResponse(apiResp);
				}
			}
		} catch (Exception e) {
			L.e("Exception" + e.toString());
		}
	}

	@UiThread
	public void ShowResponse(ApiResponse apiResp) {
		if (apiResp.getRet() == 0) {
			adapter.setData(stList);
			lv_stationName_grouping.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			T.showLong(getActivity(), "加载成功");
		} else {
			T.showLong(getActivity(), apiResp.getData());
		}
	}

	@ItemClick(R.id.lv_stationName_grouping)
	void stationGroupingClicked(int position) {
		UncommittActivity_.intent(this).station_code(stList.get(position).getCountry_code()).start();
	}

}
