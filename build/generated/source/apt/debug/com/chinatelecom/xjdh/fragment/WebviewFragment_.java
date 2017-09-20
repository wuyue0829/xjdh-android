//
// DO NOT EDIT THIS FILE.Generated using AndroidAnnotations 3.3.1.
//  You can create a larger work that contains this file and distribute that work under terms of your choice.
//


package com.chinatelecom.xjdh.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;
import com.chinatelecom.xjdh.R.layout;
import org.androidannotations.api.builder.FragmentBuilder;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class WebviewFragment_
    extends com.chinatelecom.xjdh.fragment.WebviewFragment
    implements HasViews, OnViewChangedListener
{

    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();
    private View contentView_;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(onViewChangedNotifier_);
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
    }

    @Override
    public View findViewById(int id) {
        if (contentView_ == null) {
            return null;
        }
        return contentView_.findViewById(id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView_ = super.onCreateView(inflater, container, savedInstanceState);
        if (contentView_ == null) {
            contentView_ = inflater.inflate(layout.webview, container, false);
        }
        return contentView_;
    }

    @Override
    public void onDestroyView() {
        contentView_ = null;
        super.onDestroyView();
    }

    private void init_(Bundle savedInstanceState) {
        OnViewChangedNotifier.registerOnViewChangedListener(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    public static WebviewFragment_.FragmentBuilder_ builder() {
        return new WebviewFragment_.FragmentBuilder_();
    }

    @Override
    public void onViewChanged(HasViews hasViews) {
        webview = ((WebView) hasViews.findViewById(com.chinatelecom.xjdh.R.id.webview_main));
        mTvWebviewReload = ((TextView) hasViews.findViewById(com.chinatelecom.xjdh.R.id.tv_webview_reload));
        if (mTvWebviewReload!= null) {
            mTvWebviewReload.setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    WebviewFragment_.this.loadWebviewContent();
                }

            }
            );
        }
        bindData();
    }

    public static class FragmentBuilder_
        extends FragmentBuilder<WebviewFragment_.FragmentBuilder_, com.chinatelecom.xjdh.fragment.WebviewFragment>
    {


        @Override
        public com.chinatelecom.xjdh.fragment.WebviewFragment build() {
            WebviewFragment_ fragment_ = new WebviewFragment_();
            fragment_.setArguments(args);
            return fragment_;
        }

    }

}