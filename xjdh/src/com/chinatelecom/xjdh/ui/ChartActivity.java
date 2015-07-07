package com.chinatelecom.xjdh.ui;

import java.util.ArrayList;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.codehaus.jackson.map.ObjectMapper;

import android.app.Dialog;
import android.graphics.Typeface;
import android.widget.TextView;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.bean.AlarmChartsItem;
import com.chinatelecom.xjdh.bean.AlarmChartsResp;
import com.chinatelecom.xjdh.bean.ApiResponse;
import com.chinatelecom.xjdh.rest.client.ApiRestClientInterface;
import com.chinatelecom.xjdh.utils.DialogUtils;
import com.chinatelecom.xjdh.utils.L;
import com.chinatelecom.xjdh.utils.PreferenceConstants;
import com.chinatelecom.xjdh.utils.PreferenceUtils;
import com.chinatelecom.xjdh.utils.SharedConst;
import com.chinatelecom.xjdh.utils.T;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Highlight;
import com.github.mikephil.charting.utils.LargeValueFormatter;

@EActivity(R.layout.activity_barchart)
public class ChartActivity extends BaseActivity {

	@RestService
	ApiRestClientInterface mApiClient;
	@ViewById(R.id.alarm_barchart_title)
	TextView mBarChartTitle;
	@ViewById(R.id.alarm_barchart)
	HorizontalBarChart mBarChart;
	String[] ALARM_CHART_LABELS = new String[] { "一级告警", "二级告警", "三级告警", "四级告警" };
	ArrayList<String> xVals = new ArrayList<String>();
	Dialog pDialog;
	private Typeface mTf;

	@AfterViews
	void bindData() {
		setTitle("报表");
		pDialog = DialogUtils.createLoadingDialog(this, null);
		mApiClient.setHeader(SharedConst.HTTP_AUTHORIZATION, PreferenceUtils.getPrefString(this, PreferenceConstants.ACCESSTOKEN, ""));
		mBarChart.setDescription("");
		mBarChart.setLogEnabled(false);
		mBarChart.setMaxVisibleValueCount(20);
		mBarChart.setDrawValuesForWholeStack(true);
		mBarChart.setPinchZoom(false);
		mBarChart.setDrawBarShadow(false);
		mBarChart.setDrawValueAboveBar(false);
		mTf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
		XAxis xLabels = mBarChart.getXAxis();
		xLabels.setPosition(XAxisPosition.BOTTOM);
		xLabels.setDrawGridLines(false);
		xLabels.setTypeface(mTf);
		YAxis leftAxis = mBarChart.getAxisLeft();
		leftAxis.setTypeface(mTf);
		leftAxis.setValueFormatter(new LargeValueFormatter());
		leftAxis.setDrawGridLines(false);
		leftAxis.setSpaceTop(30f);
		mBarChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

			@Override
			public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
				BarEntry entry = (BarEntry) e;
				int stackIndex = h.getStackIndex();
				int xIndex = h.getXIndex();
				T.showLong(getApplicationContext(), xVals.get(xIndex) + "（" + ALARM_CHART_LABELS[stackIndex] + "）：" + (int) entry.getVals()[stackIndex] + "个");
			}

			@Override
			public void onNothingSelected() {

			}
		});

		Legend l = mBarChart.getLegend();
		l.setPosition(LegendPosition.RIGHT_OF_CHART_INSIDE);
		l.setFormSize(8f);
		l.setFormToTextSpace(4f);
		l.setTypeface(mTf);
		l.setYOffset(0f);
		l.setYEntrySpace(0f);
		pDialog.show();
		getChartsData();
	}

	@Background
	void getChartsData() {
		try {
			ApiResponse mApiResp = mApiClient.getAlarmChartsData();
			if (mApiResp.getRet() == 0) {
				ObjectMapper mapper = new ObjectMapper();
				AlarmChartsResp alarmChartsResp = mapper.readValue(mApiResp.getData(), AlarmChartsResp.class);
				onResult(true, alarmChartsResp);
				return;
			}
		} catch (Exception e) {
			L.e(e.toString());
		}
		onResult(false, null);
	}

	@UiThread
	void onResult(boolean isSuccess, AlarmChartsResp alarmChartsResp) {
		pDialog.dismiss();
		if (isSuccess) {
			mBarChartTitle.setText(alarmChartsResp.getTitle());

			ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
			int xIndex = 0;
			for (AlarmChartsItem item : alarmChartsResp.getAlarmChartsDataList()) {
				xVals.add(item.getName());
				yVals1.add(new BarEntry(new float[] { item.getAlarm_level_1(), item.getAlarm_level_2(), item.getAlarm_level_3(), item.getAlarm_level_4() },
						xIndex++));
			}
			BarDataSet barDataSet = new BarDataSet(yVals1, "");
			barDataSet.setColors(new int[] { getResources().getColor(R.color.red), getResources().getColor(R.color.orange),
					getResources().getColor(R.color.yellow), getResources().getColor(R.color.blue) });
			barDataSet.setStackLabels(ALARM_CHART_LABELS);
			ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
			dataSets.add(barDataSet);
			BarData data = new BarData(xVals, dataSets);
			data.setValueTypeface(mTf);
			data.setValueTextSize(12f);
			mBarChart.setData(data);
			mBarChart.invalidate();
			mBarChart.animateXY(3000, 3000);
		} else {
			T.showLong(this, "获取数据失败");
		}
	}
}
