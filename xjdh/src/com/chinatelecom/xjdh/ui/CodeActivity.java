package com.chinatelecom.xjdh.ui;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.utils.URLs;
import com.squareup.picasso.Picasso;

import android.widget.ImageView;

@EActivity(R.layout.code_image)
public class CodeActivity extends BaseActivity {

	@ViewById(R.id.code_img)
	ImageView code_img;

	@AfterViews
	void initview(){
		Picasso.with(getApplicationContext()).load(URLs.CODE).into(code_img);
	}
	
}
