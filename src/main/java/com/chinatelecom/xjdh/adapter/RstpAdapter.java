package com.chinatelecom.xjdh.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.bean.RtspUrl;
import com.chinatelecom.xjdh.ui.RtspVideoPlayActivity;
import com.chinatelecom.xjdh.ui.SurveillanceActivity_;
import com.chinatelecom.xjdh.utils.L;

import java.util.ArrayList;
import java.util.List;

public class RstpAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
	private Context context;// 运行上下文
	private LayoutInflater inflater;
	public List<RtspUrl> jsonList = new ArrayList<RtspUrl>();

	// public List<String> pathList = new ArrayList<>();
	public RstpAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		// this.mVideoUrl = mVideoUrl;
		inflater = LayoutInflater.from(context);
	}

	public void setItemList(List<RtspUrl> pathList) {
		this.jsonList.clear();
		this.jsonList.addAll(pathList);
		this.notifyDataSetChanged();
	}
	public void clearList(){
		this.jsonList.clear();
		this.notifyDataSetChanged();
	}
	public RtspUrl getItem(int position) {
		// TODO Auto-generated method stub
		return jsonList.get(position);
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return jsonList.size();
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
		// TODO Auto-generated method stub
		ItemViewHolder newholder = (ItemViewHolder) holder;
		L.d("/////////" + jsonList.get(position).toString());
		newholder.tvType.setText(jsonList.get(position).getName());
		newholder.num.setText(String.valueOf(position +1));

	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.area_list_item, parent, false);
		ItemViewHolder mItemViewHolder = new ItemViewHolder(view);
		return mItemViewHolder;
	}

	class ItemViewHolder extends RecyclerView.ViewHolder {
		private TextView tvType,num;

		public ItemViewHolder(View itemView) {
			super(itemView);
			tvType = (TextView) itemView.findViewById(R.id.tv_info);
			num = (TextView) itemView.findViewById(R.id.tv_num);
			itemView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
//					playRstpByPhone();
//					playRstpByVideoView();
					SurveillanceActivity_.intent(context).URL(jsonList.get(getPosition()).getUrl()).start();
				}
			});
		}

		public void playRstpByVideoView(){
			String url = jsonList.get(getPosition()).getUrl();
			Log.v("URL:::::::::before", url);
			Intent intent = new Intent(context, RtspVideoPlayActivity.class);
			intent.putExtra("videoUrl", url);
			context.startActivity(intent);
		}
		private void playRstpByPhone() {
			//Uri uri = Uri.parse("rtsp://202.100.171.188:13504/ec321115763a7f5ce5e2");
			String url = jsonList.get(getPosition()).getUrl();
			Log.v("URL:::::::::", url);
			Uri uri = Uri.parse(url);
			Intent intent = new Intent(Intent.ACTION_VIEW);
			Log.v("URI:::::::::", uri.toString());
			intent.setDataAndType(uri, "video/*");
			context.startActivity(intent);
		}
	}
}