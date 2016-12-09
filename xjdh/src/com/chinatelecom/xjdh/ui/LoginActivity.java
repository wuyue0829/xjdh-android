package com.chinatelecom.xjdh.ui;

import java.util.LinkedHashMap;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.api.rest.MediaType;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.bean.ApiResponse;
import com.chinatelecom.xjdh.bean.ClaimOpenId;
import com.chinatelecom.xjdh.bean.ClaimResoure;
import com.chinatelecom.xjdh.bean.ClaimToken;
import com.chinatelecom.xjdh.bean.IdentificationPlatform;
import com.chinatelecom.xjdh.bean.LoginResponse;
import com.chinatelecom.xjdh.bean.OauthParam;
import com.chinatelecom.xjdh.bean.OauthRespose;
import com.chinatelecom.xjdh.rest.client.ApiRestClientInterface;
import com.chinatelecom.xjdh.rest.client.ClaimTokenRestClientInterface;
import com.chinatelecom.xjdh.rest.client.OauthRestClientInterface;
import com.chinatelecom.xjdh.utils.CryptoUtils;
import com.chinatelecom.xjdh.utils.L;
import com.chinatelecom.xjdh.utils.PreferenceConstants;
import com.chinatelecom.xjdh.utils.PreferenceUtils;
import com.chinatelecom.xjdh.utils.SharedConst;
import com.chinatelecom.xjdh.utils.T;
import com.ultrapower.auth.AuthWbLoginActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author peter
 * 
 */
@EActivity(R.layout.activity_login)
public class LoginActivity extends BaseActivity {
	@ViewById(R.id.et_login_password)
	EditText mPasswordEt;
	@ViewById(R.id.et_login_user_name)
	EditText mAccountEt;
	@ViewById(R.id.cb_is_remeber)
	CheckBox mAutoSavePasswordCK;
	@ViewById(R.id.btn_authorization)
	Button btn_authorization;
	@RestService
	OauthRestClientInterface mOauthClient;
	@RestService
	OauthRestClientInterface apiRestClientInterface2;
	@RestService
	ApiRestClientInterface apiRestClientInterface;
	@RestService
	ClaimTokenRestClientInterface mApiClient;
	ProgressDialog pDialog;
	private String password;
	private String mobile;
	String mAccount;
	String mPassword;
	@SuppressWarnings("unused")
	private String meToken = "";
	public static final int REQUESTCODE = 6;

	private String err;
	private String state;
	private String code;// 通过授权码申请服务申请到的授权码
	private String metoken;// 该参数只适用于集团移动应用框架
	private String access_token;// 授权令牌
	private String client_id = "650228RING";// 分配给第三方应用的appid
	private String resource_key = "9283ae2516d74e97afd58483d4dec222";// 分配给第三方应用的资源key
	private String token_secret = "605223a326274a2a8701b0a381744318";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 获取私密令牌的方式
		// meToken = getIntent().getStringExtra("metoken");
		String token = PreferenceUtils.getPrefString(this, PreferenceConstants.ACCESSTOKEN, "");
		apiRestClientInterface2.setHeader(SharedConst.HTTP_AUTHORIZATION, token);
	}

	@AfterViews
	void initData() {
		mAccountEt.setText(PreferenceUtils.getPrefString(this, PreferenceConstants.ACCOUNT, ""));
	}

	@Click(R.id.btn_login)
	void onLoginClicked() {
		// submittData();
		pDialog = new ProgressDialog(this);
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
		pDialog.setMessage("正在登录，请稍后...");
		pDialog.show();
		doLogin();
	}

	@Background
	void doLogin() {
		try {
			String pwdStr = CryptoUtils.decrypt(SharedConst.PASSWORD_CRYPRO_SEED, SharedConst.CLIENT_PASSWORD);
			OauthParam param = new OauthParam(SharedConst.CLIENT_ID, pwdStr, SharedConst.CLIENT_REDIRECT_URL, mAccount,
					CryptoUtils.MD5(mPassword, true));
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
				L.d("WWWWWWWWWWWWWWWWWWWWWWWWWw", mapper.writeValueAsString(resp.getResponse().toString()));
				OauthRespose mOauthResp = mapper.readValue(resp.getResponse(), OauthRespose.class);
				PreferenceUtils.setPrefString(this, PreferenceConstants.USER_INFO, mapper.writeValueAsString(resp));
				PreferenceUtils.setPrefString(this, PreferenceConstants.ACCESSTOKEN, mOauthResp.getAccess_token());
				PreferenceUtils.setPrefInt(this, PreferenceConstants.EXPIRES, mOauthResp.getExpires());
				PreferenceUtils.setPrefInt(this, PreferenceConstants.EXPIRE_IN, mOauthResp.getExpires_in());
				PreferenceUtils.setPrefString(this, PreferenceConstants.REFRESHTOKEN, mOauthResp.getRefresh_token());
				apiRestClientInterface.setHeader(SharedConst.HTTP_AUTHORIZATION, mOauthResp.getAccess_token());
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
		Toast.makeText(this, "" + resp.getRet(), 0).show();

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

	@Click(R.id.btn_authorization)
	void AuthorizationClicked() {

		Intent intent = new Intent(LoginActivity.this, AuthWbLoginActivity.class);
		intent.putExtra(AuthWbLoginActivity.STATE, "123456");// 原样返回判断状态值
		intent.putExtra(AuthWbLoginActivity.SCOPE, "s");// 授权范围 用户信息
		LoginActivity.this.startActivityForResult(intent, 1);
	}

	ObjectMapper mapper = new ObjectMapper();

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == AuthWbLoginActivity.RESULT_CODE) {
			String jsonData = data.getStringExtra(AuthWbLoginActivity.RESULT_STR);
			L.e("jsonData  :" + jsonData);
			try {
				IdentificationPlatform platform = mapper.readValue(jsonData, IdentificationPlatform.class);
				err = platform.getError();
				state = platform.getState();
				code = platform.getCode();
				metoken = platform.getMetoken();
				L.e("解析JSON  ：" + err + "" + state + "" + code + "" + metoken);
			} catch (Exception e) {
			}
		}
		claimToken();

	}

	@Background
	public void claimToken() {
		/**
		 * 申请令牌 使用FormHttpMessageConverter做信息转换--> 做POST请求
		 * 必须使用MultiValueMap才能转化为HTTP请求 APPLICATION_FORM_URLENCODED:采用的是FORM表单提交
		 */
		mApiClient.setHeader("Content-Type", MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, Object> items = new LinkedMultiValueMap<String, Object>();
		items.add("grant_type", "authorization_code");
		items.add("client_id", "650228RING");
		items.add("token_secret", "605223a326274a2a8701b0a381744318");
		items.add("device", "android");
		items.add("metoken", metoken);
		items.add("code", code);
		// items.add("metoken", "cd791fabe8f64f92ba09c35b332bb68f");
		// items.add("code", "c9fe3267ce5694ffdee00ae22e58cdb4");
		ClaimToken claimToken = mApiClient.accessToken(items);
		L.e("err:" + claimToken.getError() + "  Expires_in :" + claimToken.getExpires_in() + "  Access_token  :"
				+ claimToken.getAccess_token());
		access_token = claimToken.getAccess_token();
		cliamOpenId();
	}

	public void cliamOpenId() {
		/**
		 * 申请openId
		 */
		ClaimOpenId claimOpenId = mApiClient.me(access_token, client_id);
		L.e(" Error ：" + claimOpenId.getError() + "  Client_id ：" + claimOpenId.getClient_id() + "  Open_id :"
				+ claimOpenId.getOpen_id());
		claimResource();
	}

	public void claimResource() {
		/**
		 * 申请资源
		 */
		mApiClient.setHeader("Content-Type", MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, Object> resource = new LinkedMultiValueMap<String, Object>();
		resource.add("access_token", access_token);
		resource.add("resource_key", resource_key);
		resource.add("client_id", client_id);
		ClaimResoure claimResoure = mApiClient.get_basicUserInfo(resource);
		L.e("申请资源 :" + "Error :" + claimResoure.getError() + " Account：" + claimResoure.getAccount() + " getUsername :"
				+ claimResoure.getUsername() + " getDeptname :" + claimResoure.getDeptname() + " getEmail :"
				+ claimResoure.getEmail() + " getTelephone :" + claimResoure.getTelephone() + " getDeptid："
				+ claimResoure.getDeptid() + " getMobile ：" + claimResoure.getMobile());
		submittData(claimResoure);
	}

	public void submittData(ClaimResoure claimResoure) {
		LinkedHashMap<String, String> items = new LinkedHashMap<String, String>(0);
		// items.put("username", claimResoure.getUsername());
		// items.put("deptname", claimResoure.getDeptname());
		// items.put("email", claimResoure.getEmail());
		// items.put("telephone", claimResoure.getTelephone());
		// items.put("deptid", claimResoure.getDeptid());
		// items.put("mobile", claimResoure.getMobile());
		items.put("mobile", claimResoure.getMobile());
		ApiResponse resp = apiRestClientInterface2.creationUser(items);
		password = resp.getResponse();
		mobile = resp.getMobile();
		L.e("resp :" + resp.getResponse());
		if (resp.getRet() == 0) {
			L.e("data :" + resp.getData() + "ret :" + resp.getRet());
		} else {
			L.e("data :" + resp.getData() + "ret :" + resp.getRet());
		}
		ShowAccountPassword();
	}

	@UiThread
	void ShowAccountPassword() {
		mAccountEt.setText(mobile);
		mPasswordEt.setText(password);
	}
}
