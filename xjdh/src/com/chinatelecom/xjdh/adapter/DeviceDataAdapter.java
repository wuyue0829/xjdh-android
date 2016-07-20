package com.chinatelecom.xjdh.adapter;

import com.chinatelecom.xjdh.bean.BoardJsonData;
import com.chinatelecom.xjdh.view.PortView_;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


public class DeviceDataAdapter extends BaseAdapter{
	private Context mContext;
	public DeviceDataAdapter(Context mContext) {
		super();
		this.mContext = mContext;
	}
	
	String model;
	
	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	BoardJsonData boardData;

	public BoardJsonData getBoardData() {
		return boardData;
	}

	public void setBoardData(BoardJsonData boardData) {
		this.boardData = boardData;
	}



	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(model.equals("302A"))
		{
			return 6 + 4;
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = PortView_.build(mContext);
		} 
		PortView_ portView = (PortView_)convertView;
		if(model.equals("302A"))
		{
			if(position < 6)
			{
				//di
				int value = boardData.getDi()[position] > 48 ? boardData.getDi()[position] - 48 : 0;
				portView.setTexts("DI" + Integer.toString(position), "值:" + Integer.toString(value), "烟感:" + (value == 1?"正常":"告警"), "水浸:" + (value == 1?"正常":"告警"));
			}else if(position >=6 && position < 10){
				//ai
				int value = boardData.getAi()[position-6];
				if(value < 200)
					value = 0;
				float realV = ((float)value * 5 )/10000;
				float tempV = ((float)value * 120)/10000 - 40;
				float humidV = ((float)value * 100)/10000;
				portView.setTexts("AI" + Integer.toString(position), "值:" + String.format("%.2fV",realV), "温度:" + String.format("%.2f",tempV), "湿度:" + String.format("%.2f",humidV));
			}
		}
		return convertView;
	}
	
}
