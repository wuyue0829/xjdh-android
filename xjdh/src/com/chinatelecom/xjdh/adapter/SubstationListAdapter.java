package com.chinatelecom.xjdh.adapter;

import java.util.ArrayList;
import java.util.List;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.bean.SubstationItem;
import com.chinatelecom.xjdh.utils.L;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author peter
 * 
 */
public class SubstationListAdapter extends BaseAdapter {
	private List<SubstationItem> listItems = new ArrayList<>();
	private Context context;// 运行上下文
	private LayoutInflater listContainer;// 视图容器
	static class ListItemView { // 自定义控件集合
		public TextView num;
		public TextView roomName;
	}

	public SubstationListAdapter(Context context) {
		super();
		this.context = context;
		this.listContainer = LayoutInflater.from(context);
	}

	
	public void updateListView(List<SubstationItem> mSubstationList) {
		// TODO Auto-generated method stub
		L.d("saslkjml","mSubstationAdapter.upd");
		this.listItems = mSubstationList;
		this.notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		return listItems.size();
	}

	@Override
	public Object getItem(int position) {
		return listItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ListItemView listItemView = null;
		if (convertView == null) {
			convertView = listContainer.inflate(R.layout.area_list_item, null);
			listItemView = new ListItemView();
			listItemView.num = (TextView) convertView.findViewById(R.id.tv_num);
			listItemView.roomName = (TextView) convertView.findViewById(R.id.tv_info);
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}
		SubstationItem substationItem = listItems.get(position);
		listItemView.num.setText(String.valueOf(position +1));
		listItemView.roomName.setText(substationItem.getName());
		return convertView;
	}

	public void setListItems(List<SubstationItem> items) {
		this.listItems.addAll(items);
	}
	public void addLists(List<SubstationItem> items) {
		listItems.addAll(items); 
	    }

	
}