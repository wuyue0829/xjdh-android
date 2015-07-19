package com.chinatelecom.xjdh.ui;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.api.rest.MediaType;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.adapter.UserDetailListAdapter;
import com.chinatelecom.xjdh.bean.ApiResponse;
import com.chinatelecom.xjdh.bean.UserDetailListItem;
import com.chinatelecom.xjdh.bean.UserInfo;
import com.chinatelecom.xjdh.rest.client.ApiRestClientInterface;
import com.chinatelecom.xjdh.utils.CryptoUtils;
import com.chinatelecom.xjdh.utils.FileUtils;
import com.chinatelecom.xjdh.utils.L;
import com.chinatelecom.xjdh.utils.PreferenceConstants;
import com.chinatelecom.xjdh.utils.PreferenceUtils;
import com.chinatelecom.xjdh.utils.SharedConst;
import com.chinatelecom.xjdh.utils.StringUtils;
import com.chinatelecom.xjdh.utils.T;

@EActivity(R.layout.activity_user_detail)
public class UserDetailActivity extends BaseActivity {
	@ViewById(R.id.lv_user_detail)
	ListView mLvUser;
	Dialog mImgChangeDialog;
	@RestService
	ApiRestClientInterface mApiClient;
	Uri pendingImageUri;
	UserInfo mUserInfo;
	List<UserDetailListItem> listItems = new ArrayList<UserDetailListItem>();
	UserDetailListAdapter mUserDetailAdapter;
	ProgressDialog pDialog, uploadProgressDialog;
	public static final int CAPTURE_PICTURE = 1;// 调用相机拍照
	public static final int PICK_PICTURE = 2; // 从相册中选择图片
	public static final int RESULT_PICTURE = 3;// 剪切返回结果

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mApiClient.setHeader(SharedConst.HTTP_AUTHORIZATION, PreferenceUtils.getPrefString(this, PreferenceConstants.ACCESSTOKEN, ""));
		setTitle("用户中心");
		pDialog = new ProgressDialog(this);
		pDialog.setMessage(getResources().getString(R.string.progress_loading_msg));
		uploadProgressDialog = new ProgressDialog(this);
		uploadProgressDialog.setMessage("正在上传...");
	}

	@AfterViews
	void bindData() {
		mUserDetailAdapter = new UserDetailListAdapter(this, listItems);
		mLvUser.setAdapter(mUserDetailAdapter);
		pDialog.show();
		getUserInfo();
	}

	@Background
	void getUserInfo() {
		try {
			ApiResponse mApiResp = mApiClient.getUserInfo();
			if (mApiResp.getRet() == 0) {
				ObjectMapper mapper = new ObjectMapper();
				mUserInfo = mapper.readValue(mApiResp.getData(), UserInfo.class);
				updateView(true);
				return;
			}
		} catch (Exception e) {
			L.e(e.toString());
		}
		updateView(false);
	}

	@UiThread
	void updateView(boolean isSuccess) {
		pDialog.dismiss();
		if (isSuccess) {
			listItems.clear();
			((LinearLayout) findViewById(R.id.ly_user_detail)).setGravity(Gravity.TOP);
			mLvUser.setVisibility(View.VISIBLE);
			findViewById(R.id.btn_reload).setVisibility(View.GONE);
			listItems.add(new UserDetailListItem(1, 2, "头像", "user_image", mUserInfo.getUser_image()));
			listItems.add(new UserDetailListItem(2, 1, "姓名", "full_name", mUserInfo.getFull_name()));
			listItems.add(new UserDetailListItem(3, 1, "性别", "gender", mUserInfo.getGender().equalsIgnoreCase("male") ? "男" : "女"));
			listItems.add(new UserDetailListItem(4, 1, "联系电话", "mobile", mUserInfo.getMobile()));
			listItems.add(new UserDetailListItem(5, 1, "邮箱", "email", mUserInfo.getEmail()));
			listItems.add(new UserDetailListItem(6, 1, "其他信息", "info", mUserInfo.getInfo()));
			listItems.add(new UserDetailListItem(7, 1, "修改密码", "password", ""));
			mUserDetailAdapter.notifyDataSetChanged();
		} else {
			findViewById(R.id.btn_reload).setVisibility(View.VISIBLE);
			((LinearLayout) findViewById(R.id.ly_user_detail)).setGravity(Gravity.CENTER_VERTICAL);
			mLvUser.setVisibility(View.GONE);
		}
	}

	@ItemClick(R.id.lv_user_detail)
	void onListItemClicked(int position) {
		userDetailListItem = listItems.get(position);
		// image
		if (userDetailListItem.getType() == 2) {
			mImgChangeDialog = new Dialog(this);
			mImgChangeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			mImgChangeDialog.setContentView(getLayoutInflater().inflate(R.layout.photo_menu_dialog, null));
			Window window = mImgChangeDialog.getWindow();
			WindowManager.LayoutParams lp = window.getAttributes();
			DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
			int screenW = dm.widthPixels;
			lp.width = screenW;
			window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置

			mImgChangeDialog.findViewById(R.id.btn_photo).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mImgChangeDialog.dismiss();
					pendingImageUri = FileUtils.getTakePhotoUri();
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					intent.putExtra(MediaStore.EXTRA_OUTPUT, pendingImageUri);
					startActivityForResult(intent, CAPTURE_PICTURE);
				}
			});
			mImgChangeDialog.findViewById(R.id.btn_choose_img).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mImgChangeDialog.dismiss();
					Intent intent = new Intent(Intent.ACTION_PICK, null);
					intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
					startActivityForResult(intent, PICK_PICTURE);
				}
			});
			mImgChangeDialog.findViewById(R.id.btnCancel).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mImgChangeDialog.dismiss();
				}
			});
			mImgChangeDialog.setCanceledOnTouchOutside(true);
			mImgChangeDialog.show();
		} else if (userDetailListItem.getId() == 3) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("请选择性别");
			final String[] sex = { "男", "女" };
			// 设置一个单项选择下拉框
			builder.setSingleChoiceItems(sex, mUserInfo.getGender().equalsIgnoreCase("male") ? 0 : 1, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					userDetailListItem.setColumnVal(sex[which]);
				}
			});
			builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					uploadData();
					dialog.dismiss();
				}
			});
			builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {

				}
			});
			builder.show();
		}
		// other
		else {
			showModifyDialog();
		}
	}

	private UserDetailListItem userDetailListItem;

	void showModifyDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("修改" + (userDetailListItem.getId() == 7 ? "密码" : userDetailListItem.getColumnText()));
		View dialogView = getLayoutInflater().inflate(R.layout.dialog_modify_user_info, null);
		TextView tvColumn = (TextView) dialogView.findViewById(R.id.tv_column);
		tvColumn.setText(userDetailListItem.getColumnText());
		final EditText etColumnVal = (EditText) dialogView.findViewById(R.id.et_column_val);
		etColumnVal.setText(userDetailListItem.getColumnVal());
		etColumnVal.setSingleLine(true);
		if (userDetailListItem.getId() == 4) {
			etColumnVal.setInputType(InputType.TYPE_CLASS_PHONE);
		} else if (userDetailListItem.getId() == 5) {
			etColumnVal.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
		} else if (userDetailListItem.getId() == 6) {
			etColumnVal.setSingleLine(false);
			etColumnVal.setMinLines(3);
		} else if (userDetailListItem.getId() == 7) {
			etColumnVal.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
			etColumnVal.setMinEms(6);
		}
		builder.setView(dialogView);
		builder.setNegativeButton("取消", null);
		builder.setPositiveButton("确定", null);
		final AlertDialog dialog = builder.create();
		dialog.show();
		dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (userDetailListItem.getId()) {
				case 2:
					if (etColumnVal.getText().toString().length() < 2) {
						T.showLong(getApplicationContext(), "姓名至少2个字符");
						return;
					}
					break;
				case 5:
					if (!StringUtils.isEmail(etColumnVal.getText().toString())) {
						T.showLong(getApplicationContext(), "请输入合法的邮箱地址");
						return;
					}
				case 7:
					if (etColumnVal.getText().toString().length() < 6) {
						T.showLong(getApplicationContext(), "密码最少6个字符");
						return;
					}
					break;
				case 4:
					if (!StringUtils.isMobile(etColumnVal.getText().toString())) {
						T.showLong(getApplicationContext(), "请输入合法的手机号码");
						return;
					}
					break;
				default:
					break;
				}

				userDetailListItem.setColumnVal(etColumnVal.getText().toString());
				uploadProgressDialog.show();
				uploadData();
				dialog.dismiss();
			}
		});
	}

	@Background
	void uploadData() {
		LinkedHashMap<String, String> items = new LinkedHashMap<String, String>(0);
		if (userDetailListItem.getId() == 3)
			items.put(userDetailListItem.getColumnName(), userDetailListItem.getColumnVal().equalsIgnoreCase("男") ? "male" : "female");
		else
			items.put(userDetailListItem.getColumnName(), userDetailListItem.getColumnVal());
		boolean isSuccess = false;
		try {
			ApiResponse resp = mApiClient.modifyUserInfo(items);
			if (resp.getRet() == 0) {
				isSuccess = true;
			} else {
				isSuccess = false;
			}
		} catch (Exception e) {
			L.e(e.toString());
		}
		onUserInfoChanged(isSuccess);
	}

	@UiThread
	void onUserInfoChanged(boolean isSuccess) {
		uploadProgressDialog.dismiss();
		if (isSuccess) {
			if (userDetailListItem.getId() == 7) {
				PreferenceUtils.setPrefString(this, PreferenceConstants.PASSWORD, CryptoUtils.MD5(userDetailListItem.getColumnVal(), true));
				userDetailListItem.setColumnVal("");
			}
			listItems.set(userDetailListItem.getId() - 1, userDetailListItem);
			mUserDetailAdapter.notifyDataSetChanged();
			try {
				ObjectMapper mapper = new ObjectMapper();
				PreferenceUtils.setPrefString(this, PreferenceConstants.USER_INFO, mapper.writeValueAsString(mUserInfo));
			} catch (Exception e) {
				L.e(e.toString());
			}
			T.showShort(this, "修改" + (userDetailListItem.getId() == 7 ? "密码" : userDetailListItem.getColumnText()) + "成功");
		} else {
			T.showShort(this, "修改" + (userDetailListItem.getId() == 7 ? "密码" : userDetailListItem.getColumnText()) + "失败");
		}
	}

	@Click(R.id.btn_reload)
	void reload() {
		bindData();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK)
			return;
		switch (requestCode) {
		case CAPTURE_PICTURE:
			startPhotoZoom(pendingImageUri, RESULT_PICTURE);
			break;
		case PICK_PICTURE:
			if (data != null) {
				startPhotoZoom(data.getData(), RESULT_PICTURE);
			}
			break;
		case RESULT_PICTURE:
			if (data != null) {
				Bundle extras = data.getExtras();
				if (extras != null) {
					Bitmap photo = extras.getParcelable("data");
					final ByteArrayOutputStream stream = new ByteArrayOutputStream();
					photo.compress(CompressFormat.JPEG, 100, stream);
					// ByteArrayInputStream is = new ByteArrayInputStream(stream.toByteArray());
					ByteArrayResource image = new ByteArrayResource(stream.toByteArray(), "aaadasd") {
						@Override
						public String getFilename() throws IllegalStateException {
							return SharedConst.USER_IMAGE + ".jpg";
						}

						@Override
						public String getDescription() {
							return super.getDescription();
						}
					};
					MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<>();

					multiValueMap.add(SharedConst.USER_IMAGE, image);
					uploadProgressDialog.show();
					modifyUserImage(multiValueMap);
				}
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Background
	void modifyUserImage(MultiValueMap<String, Object> multiValueMap) {
		mApiClient.setHeader("Content-Type", MediaType.MULTIPART_FORM_DATA);
		try {
			ApiResponse mApiResp = mApiClient.modifyuserimage(multiValueMap);
			if (mApiResp.getRet() == 0) {
				userDetailListItem.setColumnVal(mApiResp.getData());
				onUserInfoChanged(true);
				return;
			}
		} catch (Exception e) {
			L.e(e.toString());
		}
		onUserInfoChanged(false);
	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri, int requestCode) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");

		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 300);
		intent.putExtra("outputY", 300);
		intent.putExtra("return-data", true);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		startActivityForResult(intent, requestCode);
	}
}
