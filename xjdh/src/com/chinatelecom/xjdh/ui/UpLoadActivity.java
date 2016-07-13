package com.chinatelecom.xjdh.ui;

import java.sql.SQLException;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OrmLiteDao;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.db.DatabaseHelper;
import com.chinatelecom.xjdh.model.FileNameGPSTable;
import com.chinatelecom.xjdh.utils.L;
import com.j256.ormlite.dao.Dao;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;

@EActivity(R.layout.activity_upload)
public class UpLoadActivity extends Activity {
	private List<FileNameGPSTable> data;
	private ProgressDialog pDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		pDialog = new ProgressDialog(this);
		pDialog.setMessage("提交数据中...");
	}
	@OrmLiteDao(helper = DatabaseHelper.class, model = FileNameGPSTable.class)
	Dao<FileNameGPSTable, Integer> sDao;

	@AfterViews
	void initData() {
		getData();
	}

	/**
	 * 获取数据
	 */
	private void getData() {
		try {
			data = sDao.queryForAll();
			if (data == null) {
				return;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		L.e("888888888888888888" + data.get(0).getLatitude()+"          "+data.get(0).getFileName());
	}
}
