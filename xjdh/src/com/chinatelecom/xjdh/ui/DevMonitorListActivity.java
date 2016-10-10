package com.chinatelecom.xjdh.ui;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.tool.BluetoothTool;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
/**
 * @author peter
 * 
 */
@EActivity(R.layout.test_device_list)
public class DevMonitorListActivity extends BaseActivity {

	@Extra
	public String model;
	@ViewById(R.id.lvDevMode)
	ListView lvDevMode;
	
	@ViewById(R.id.tvModel)
	TextView tvModel;
	
	String[] devModeArray = new String[]{"DI/AI数据","智能设备测试","网络测试","系统设置","重启程序","重启板子","退出设置"};
	ArrayAdapter<String> mAdapter;


	@AfterViews
	void Show() {
		tvModel.setText(model);
		
		mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, devModeArray);
		lvDevMode.setAdapter(mAdapter);
	}

	@ItemClick(R.id.lvDevMode)
	void onDevModelicked(int pos) {
		switch(pos)
		{
		case 0:
			SMDDeviceMonitor_.intent(this).model(model).start();
			break;
		case 1:
			SPDevTestActivity_.intent(this).start();
			//smart device test
			break;
		case 2:
			//network test
			NetworkTestActivity_.intent(this).start();
			break;
		case 3:
			BoardSettingActivity_.intent(this).start();
			break;
		case 4:
		{
			//quit
			new AlertDialog.Builder(this)
	        .setIcon(android.R.drawable.ic_dialog_alert)
	        .setTitle("重启采集程序")
	        .setMessage("请确认重启采集程序以使用新设置?")
	        .setPositiveButton("是", new DialogInterface.OnClickListener()
		    {
		        @Override
		        public void onClick(DialogInterface dialog, int which) {
		        	BluetoothTool.SendCmd("{\"cmd\": \"quit\"}\r\n");
		            finish();
		        }
	
		    })
		    .setNegativeButton("否", null)
		    .show();
			break;
		}
		case 5:
		{
			//reboot
			new AlertDialog.Builder(this)
	        .setIcon(android.R.drawable.ic_dialog_alert)
	        .setTitle("重启采集板")
	        .setMessage("请确认重启采集板以使用新设置?")
	        .setPositiveButton("是", new DialogInterface.OnClickListener()
		    {
		        @Override
		        public void onClick(DialogInterface dialog, int which) {
		        	BluetoothTool.SendCmd("{\"cmd\": \"reboot\"}\r\n");
		            finish();
		        }
	
		    })
		    .setNegativeButton("否", null)
		    .show();
			break;
		}
		case 6:
			BluetoothTool.Close();
			finish();
			break;
		}
	}
}
