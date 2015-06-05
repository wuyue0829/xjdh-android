package com.chinatelecom.xjdh.app;

import org.androidannotations.annotations.EActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.ui.BaseActivity;
import com.chinatelecom.xjdh.ui.LoginActivity_;
import com.chinatelecom.xjdh.ui.MainActivity_;
import com.chinatelecom.xjdh.utils.PreferenceConstants;
import com.chinatelecom.xjdh.utils.PreferenceUtils;

@EActivity
public class AppStart extends BaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActionBar.hide();

		final View view = View.inflate(this, R.layout.start, null);
		setContentView(view);
		// 渐变展示启动屏
		AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
		aa.setDuration(1000);
		view.startAnimation(aa);
		aa.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation arg0) {
				redirectTo();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationStart(Animation animation) {
			}

		});
	}

	/**
	 * 跳转到...
	 */
	private void redirectTo() {
		String password = PreferenceUtils.getPrefString(this, PreferenceConstants.PASSWORD, "");
		String accessToken = PreferenceUtils.getPrefString(this, PreferenceConstants.ACCESSTOKEN, "");
		if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(accessToken)) {
			MainActivity_.intent(this).start();
		} else {
			LoginActivity_.intent(this).start();
		}
		finish();
	}
}