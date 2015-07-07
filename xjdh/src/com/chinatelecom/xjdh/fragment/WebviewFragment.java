package com.chinatelecom.xjdh.fragment;

import java.io.IOException;
import java.io.InputStream;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.utils.DialogUtils;

@EFragment(R.layout.webview)
public class WebviewFragment extends Fragment {
	@ViewById(R.id.webview_main)
	WebView webview;
	Dialog pDialog;
	static String originalUrl;

	public static WebviewFragment newInstance(String originalUrl) {
		WebviewFragment fragment = new WebviewFragment_();
		WebviewFragment.originalUrl = originalUrl;
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		pDialog = DialogUtils.createLoadingDialog(getActivity(), null);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@SuppressLint("SetJavaScriptEnabled")
	@AfterViews
	void bindData() {
		webview.getSettings().setJavaScriptEnabled(true);
		webview.getSettings().setUseWideViewPort(true);
		webview.setWebViewClient(new WebViewClient() {
			@SuppressLint("NewApi")
			@Override
			public void onPageFinished(WebView view, String url) {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
					if (0 != (getActivity().getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE)) {
						WebView.setWebContentsDebuggingEnabled(true);
					}
				}
				pDialog.dismiss();
				super.onPageFinished(view, url);
			}

			@Override
			public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
				WebResourceResponse response = null;
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					if (url.contains("bootstrap.css")) {
						return getWebResourceResponseFromAsset("text/css", "css/bootstrap.css");
					} else if (url.contains("bootstrap-responsive.css")) {
						return getWebResourceResponseFromAsset("text/css", "css/bootstrap-responsive.css");
					} else if (url.contains("jquery.js")) {
						return getWebResourceResponseFromAsset("application/x-javascript", "js/jquery.js");
					} else {
						return super.shouldInterceptRequest(view, url);
					}

				}
				return response;
			}

			private WebResourceResponse getWebResourceResponseFromAsset(String mimeType, String fileName) {
				try {
					return getUtf8EncodedWebResourceResponse(mimeType, getActivity().getAssets().open(fileName));
				} catch (IOException e) {
					return null;
				}
			}

			private WebResourceResponse getUtf8EncodedWebResourceResponse(String mimeType, InputStream data) {
				return new WebResourceResponse(mimeType, "UTF-8", data);
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (Uri.parse(url).getHost().equals(Uri.parse(originalUrl).getHost())) {
					return false;
				}
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
				startActivity(intent);
				return true;
			}
		});
		webview.loadUrl(originalUrl);
		pDialog.show();
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (webview != null)
			webview.loadUrl(originalUrl);
	}

}
