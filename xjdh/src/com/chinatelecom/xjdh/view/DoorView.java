package com.chinatelecom.xjdh.view;

import java.util.Timer;
import java.util.TimerTask;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.bean.ApiResponse;
import com.chinatelecom.xjdh.bean.DoorOperation;
import com.chinatelecom.xjdh.rest.client.ApiRestClientInterface;
import com.chinatelecom.xjdh.utils.T;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author peter
 * 
 */
@EViewGroup(R.layout.door_view)
public class DoorView extends LinearLayout {

	public DoorView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public String Name;

	public String DataId;

	public int CanOpen;

	public void SetPara(String inName, String inDataId, int inCanOpen)
	{
		Name = inName;
		DataId = inDataId;
		CanOpen = inCanOpen;
		Show();
	}
	@ViewById
	TextView tvName,tvStatus;
	
	@ViewById(R.id.btnRemoteOpen)
	Button btnRemoteOpen;
	@ViewById(R.id.btnForceOpen)
	Button btnForceOpen;

	@RestService
	ApiRestClientInterface mApiClient;
	private Timer timer;
	
	ProgressDialog pDialog;
	
	void Show() {
		tvName.setText(Name);
		pDialog = new ProgressDialog(super.getContext());
		pDialog.setMessage(getResources().getString(R.string.progress_loading_msg));
		//pDialog.show();
		
		if (CanOpen == 0) {
			btnRemoteOpen.setVisibility(View.GONE);
			btnForceOpen.setVisibility(View.GONE);
		}
		// Start Timer to refresh status
		if (timer == null) {
			timer = new Timer();
			timer.scheduleAtFixedRate(new RefreshTask(), 0, 10000);
		}
	}
	
	class RefreshTask extends TimerTask {

		@Override
		public void run() {
			try
			{
				ApiResponse ret = mApiClient.GetDoorStatus(DataId);
				if(ret.getData().equals("1"))
				{
					ShowStatus("打开");
				}else{
					ShowStatus("关闭");
				}
			}catch(Exception e)
			{
				
			}
		}
	}
	
	@UiThread
	void ShowStatus(String status)
	{
		tvStatus.setText(status);
	}
	@Click(R.id.btnRemoteOpen)
	public void OnRemoteOpen() {
		final EditText inputServer = new EditText(super.getContext());
		final Context tContext = super.getContext();
		inputServer.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        inputServer.setPadding(20, 5, 5, 20);
		final AlertDialog.Builder builder = new AlertDialog.Builder(super.getContext());
        builder.setTitle("请输入情况说明").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
                        .setNegativeButton("取消", null);
        builder.setPositiveButton("提交", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                        String reason = inputServer.getText().toString();
                        reason = reason.trim();
                        if (reason.length() == 0) {
                                T.showLong(tContext, "请输入情况说明");
                                return;
                        }
                        pDialog.setMessage("发送命令中...");
                		pDialog.show();
                        doOpenDoor("远程开门", reason);
                }
        });
        builder.show();
	}
	
	@Background
	void doOpenDoor(String action, String message)
	{
		try
		{
			DoorOperation op = new DoorOperation();
			op.setData_id(DataId);
			op.setAction(action);
			op.setMessage(message);
			ApiResponse ret = mApiClient.OpenDoor(op);
			if(ret.getRet() == 0)
			{
				ShowResult(0);
				return;
			}			
		}catch(Exception e)
		{
			
		}
		ShowResult(1);
	}

	@UiThread
	void ShowResult(int ret)
	{
		pDialog.dismiss();
		if(ret == 0)
		{
			T.showLong(super.getContext(), "开门成功");
		}else{
			T.showLong(super.getContext(), "开门失败");
		}
	}
	@Click(R.id.btnForceOpen)
	public void OnForceOpen() {
		final EditText inputServer = new EditText(super.getContext());
        inputServer.setPadding(20, 5, 5, 20);
		final AlertDialog.Builder builder = new AlertDialog.Builder(super.getContext());
		final Context tContext = super.getContext();
        builder.setTitle("请输入情况说明").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
                        .setNegativeButton("取消", null);
        builder.setPositiveButton("提交", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                        String reason = inputServer.getText().toString();
                        reason = reason.trim();
                        if (reason.length() == 0) {
                                T.showLong(tContext, "请输入情况说明");
                                builder.show();
                                return;
                        }
                        pDialog.setMessage("发送命令中...");
                		pDialog.show();
                        doOpenDoor("强制开门", reason);
                }
        });
        builder.show();
	}
}
