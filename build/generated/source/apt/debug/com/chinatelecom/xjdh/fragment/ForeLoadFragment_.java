//
// DO NOT EDIT THIS FILE.Generated using AndroidAnnotations 3.3.1.
//  You can create a larger work that contains this file and distribute that work under terms of your choice.
//


package com.chinatelecom.xjdh.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import com.chinatelecom.xjdh.R.layout;
import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.api.builder.FragmentBuilder;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class ForeLoadFragment_
    extends com.chinatelecom.xjdh.fragment.ForeLoadFragment
    implements HasViews, OnViewChangedListener
{

    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();
    private View contentView_;
    private Handler handler_ = new Handler(Looper.getMainLooper());

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
            contentView_ = inflater.inflate(layout.activity_smddevice_monitor, container, false);
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

    public static ForeLoadFragment_.FragmentBuilder_ builder() {
        return new ForeLoadFragment_.FragmentBuilder_();
    }

    @Override
    public void onViewChanged(HasViews hasViews) {
        btnRefresh = ((Button) hasViews.findViewById(com.chinatelecom.xjdh.R.id.btnRefresh));
        devList = ((ListView) hasViews.findViewById(com.chinatelecom.xjdh.R.id.devList));
        mSrlAlarm = ((SwipeRefreshLayout) hasViews.findViewById(com.chinatelecom.xjdh.R.id.srl_alarm));
        Show();
    }

    @Override
    public void ShowData() {
        handler_.post(new Runnable() {


            @Override
            public void run() {
                ForeLoadFragment_.super.ShowData();
            }

        }
        );
    }

    @Override
    public void ShowMessage(final String msg) {
        handler_.post(new Runnable() {


            @Override
            public void run() {
                ForeLoadFragment_.super.ShowMessage(msg);
            }

        }
        );
    }

    @Override
    public void ReadData() {
        BackgroundExecutor.execute(new BackgroundExecutor.Task("", 0, "") {


            @Override
            public void execute() {
                try {
                    ForeLoadFragment_.super.ReadData();
                } catch (Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }

        }
        );
    }

    public static class FragmentBuilder_
        extends FragmentBuilder<ForeLoadFragment_.FragmentBuilder_, com.chinatelecom.xjdh.fragment.ForeLoadFragment>
    {


        @Override
        public com.chinatelecom.xjdh.fragment.ForeLoadFragment build() {
            ForeLoadFragment_ fragment_ = new ForeLoadFragment_();
            fragment_.setArguments(args);
            return fragment_;
        }

    }

}