package com.chinatelecom.xjdh.ui;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.codehaus.jackson.map.ObjectMapper;

import android.app.ProgressDialog;
import android.text.TextUtils;
import android.widget.CheckBox;
import android.widget.EditText;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.bean.LoginResponse;
import com.chinatelecom.xjdh.bean.OauthParam;
import com.chinatelecom.xjdh.bean.OauthRespose;
import com.chinatelecom.xjdh.rest.client.ApiRestClientInterface;
import com.chinatelecom.xjdh.rest.client.OauthRestClientInterface;
import com.chinatelecom.xjdh.utils.CryptoUtils;
import com.chinatelecom.xjdh.utils.L;
import com.chinatelecom.xjdh.utils.PreferenceConstants;
import com.chinatelecom.xjdh.utils.PreferenceUtils;
import com.chinatelecom.xjdh.utils.SharedConst;
import com.chinatelecom.xjdh.utils.T;

@EActivity(R.layout.activity_login)
public class LoginActivity extends BaseActivity {

	@ViewById(R.id.et_login_password)
	EditText mPasswordEt;
	@ViewById(R.id.et_login_user_name)
	EditText mAccountEt;
	@ViewById(R.id.cb_is_remeber)
	CheckBox mAutoSavePasswordCK;
	@RestService
	OauthRestClientInterface mOauthClient;
	@RestService
	ApiRestClientInterface mApiClient;
	ProgressDialog pDialog;

	String mAccount;
	String mPassword;

	@AfterViews
	void initData() {
		mAccountEt.setText(PreferenceUtils.getPrefString(this, PreferenceConstants.ACCOUNT, ""));
		mActionBar.hide();
	}

	@Click(R.id.btn_login)
	void onLoginClicked() {
		pDialog = new ProgressDialog(this);
		pDialog.setMessage("正在登录，请稍后...");
		pDialog.show();
		mAccount = mAccountEt.getText().toString();
		mPassword = mPasswordEt.getText().toString();
		if (TextUtils.isEmpty(mAccount) && TextUtils.isEmpty(mPassword)) {
			T.showLong(this, "请输入登录用户名和密码");
			return;
		} else if (TextUtils.isEmpty(mAccount)) {
			T.showLong(this, "请输入登录用户名");
			return;
		} else if (TextUtils.isEmpty(mPassword)) {
			T.showLong(this, "请输入登录密码");
			return;
		}
		doLogin();
	}

	@Background
	void doLogin() {
		try {
			String pwdStr = CryptoUtils.decrypt(SharedConst.PASSWORD_CRYPRO_SEED, SharedConst.CLIENT_PASSWORD);
			OauthParam param = new OauthParam(SharedConst.CLIENT_ID, pwdStr, SharedConst.CLIENT_REDIRECT_URL, mAccount, CryptoUtils.MD5(mPassword, true));
			LoginResponse resp = mOauthClient.login(param);
			loginResult(resp);
			return;
		} catch (Exception e) {
			L.e(e.toString());
		}
		loginResult(new LoginResponse(-1, "", ""));
	}

	@UiThread
	void loginResult(LoginResponse resp) {
		if (pDialog != null && pDialog.isShowing())
			pDialog.dismiss();
		if (resp.getRet() == 0) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				OauthRespose mOauthResp = mapper.readValue(resp.getResponse(), OauthRespose.class);
				PreferenceUtils.setPrefString(this, PreferenceConstants.ACCESSTOKEN, mOauthResp.getAccess_token());
				PreferenceUtils.setPrefInt(this, PreferenceConstants.EXPIRES, mOauthResp.getExpires());
				PreferenceUtils.setPrefInt(this, PreferenceConstants.EXPIRE_IN, mOauthResp.getExpires_in());
				PreferenceUtils.setPrefString(this, PreferenceConstants.REFRESHTOKEN, mOauthResp.getRefresh_token());
				mApiClient.setHeader(SharedConst.HTTP_AUTHORIZATION, mOauthResp.getAccess_token());
				save2Preferences();
				MainActivity_.intent(this).isDoLogin(false).start();
				finish();
				return;
			} catch (Exception e) {
				L.e(e.toString());
			}
		} else if (resp.getRet() == 2) {
			T.showLong(this, "账户名或密码有误");
			return;
		}
		T.showShort(this, "登陆失败");
	}

	private void save2Preferences() {
		boolean isAutoSavePassword = mAutoSavePasswordCK.isChecked();
		PreferenceUtils.setPrefString(this, PreferenceConstants.ACCOUNT, mAccount);// 帐号是一直保存的
		if (isAutoSavePassword)
			PreferenceUtils.setPrefString(this, PreferenceConstants.PASSWORD, CryptoUtils.MD5(mPassword, true));
		else
			PreferenceUtils.setPrefString(this, PreferenceConstants.PASSWORD, "");
	}
}
