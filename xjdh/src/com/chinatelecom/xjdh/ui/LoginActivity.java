package com.chinatelecom.xjdh.ui;

import java.io.IOException;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import android.app.Dialog;
import android.text.TextUtils;
import android.widget.CheckBox;
import android.widget.EditText;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.bean.LoginResponse;
import com.chinatelecom.xjdh.bean.OauthParam;
import com.chinatelecom.xjdh.bean.OauthRespose;
import com.chinatelecom.xjdh.utils.CryptoUtils;
import com.chinatelecom.xjdh.utils.DialogUtil;
import com.chinatelecom.xjdh.utils.L;
import com.chinatelecom.xjdh.utils.PreferenceConstants;
import com.chinatelecom.xjdh.utils.PreferenceUtils;
import com.chinatelecom.xjdh.utils.SharedConst;
import com.chinatelecom.xjdh.utils.T;
import com.chinateltecom.xjdh.rest.client.ApiRestClientInterface;
import com.chinateltecom.xjdh.rest.client.OauthRestClientInterface;

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
	OauthRespose mOauthResp;
	Dialog loginDlg;

	String mAccount;
	String mPassword;

	@AfterViews
	void initData() {
		mActionBar.hide();
	}

	@Click(R.id.btn_login)
	void onLoginClicked() {
		loginDlg = DialogUtil.createLoadingDialog(this, null);
		loginDlg.show();
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
		loginResult(new LoginResponse(1, "", ""));
	}

	@UiThread
	void loginResult(LoginResponse resp) {
		if (loginDlg != null && loginDlg.isShowing())
			loginDlg.dismiss();
		if (resp.getRet() == 0) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				mOauthResp = mapper.readValue(resp.getResponse(), OauthRespose.class);
				PreferenceUtils.setPrefString(this, PreferenceConstants.ACCESSTOKEN, mOauthResp.getAccess_token());
				PreferenceUtils.setPrefInt(this, PreferenceConstants.EXPIRES, mOauthResp.getExpires());
				PreferenceUtils.setPrefInt(this, PreferenceConstants.EXPIRE_IN, mOauthResp.getExpires_in());
				PreferenceUtils.setPrefString(this, PreferenceConstants.REFRESHTOKEN, mOauthResp.getRefresh_token());
				mApiClient.setHeader(SharedConst.HTTP_AUTHORIZATION, mOauthResp.getAccess_token());
				save2Preferences();
				MainActivity_.intent(this).start();
				return;
			} catch (JsonParseException e) {
				L.e(e.toString());
			} catch (JsonMappingException e) {
				L.e(e.toString());
			} catch (IOException e) {
				L.e(e.toString());
			}
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
