//
// DO NOT EDIT THIS FILE.Generated using AndroidAnnotations 3.3.1.
//  You can create a larger work that contains this file and distribute that work under terms of your choice.
//


package com.chinatelecom.xjdh.ui;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import com.chinatelecom.xjdh.R.array;
import com.chinatelecom.xjdh.R.id;
import com.chinatelecom.xjdh.R.layout;
import com.chinatelecom.xjdh.rest.client.ApiRestClientInterface_;
import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.api.SdkVersionHelper;
import org.androidannotations.api.builder.ActivityIntentBuilder;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class SPDevTestActivity_
    extends SPDevTestActivity
    implements HasViews, OnViewChangedListener
{

    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();
    private Handler handler_ = new Handler(Looper.getMainLooper());

    @Override
    public void onCreate(Bundle savedInstanceState) {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(onViewChangedNotifier_);
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
        setContentView(layout.activity_spdev_test);
    }

    private void init_(Bundle savedInstanceState) {
        OnViewChangedNotifier.registerOnViewChangedListener(this);
        Resources resources_ = this.getResources();
        baud_rate = resources_.getStringArray(array.spdev_rate_array);
        apiRestClient = new ApiRestClientInterface_(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
        super.setContentView(view, params);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    public static SPDevTestActivity_.IntentBuilder_ intent(Context context) {
        return new SPDevTestActivity_.IntentBuilder_(context);
    }

    public static SPDevTestActivity_.IntentBuilder_ intent(android.app.Fragment fragment) {
        return new SPDevTestActivity_.IntentBuilder_(fragment);
    }

    public static SPDevTestActivity_.IntentBuilder_ intent(android.support.v4.app.Fragment supportFragment) {
        return new SPDevTestActivity_.IntentBuilder_(supportFragment);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (((SdkVersionHelper.getSdkInt()< 5)&&(keyCode == KeyEvent.KEYCODE_BACK))&&(event.getRepeatCount() == 0)) {
            onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onViewChanged(HasViews hasViews) {
        tvSendData = ((TextView) hasViews.findViewById(id.tvSendData));
        spIndex = ((Spinner) hasViews.findViewById(id.spIndex));
        spModel = ((Spinner) hasViews.findViewById(id.spModel));
        spRate = ((Spinner) hasViews.findViewById(id.spRate));
        tvResult = ((TextView) hasViews.findViewById(id.tvResult));
        btnTest = ((Button) hasViews.findViewById(id.btnTest));
        tvBanner = ((TextView) hasViews.findViewById(id.tvBanner));
        tvRecvData = ((TextView) hasViews.findViewById(id.tvRecvData));
        if (btnTest!= null) {
            btnTest.setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    SPDevTestActivity_.this.onBtnTestClick();
                }

            }
            );
        }
        {
            View view = hasViews.findViewById(id.btnUpdate);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        SPDevTestActivity_.this.onBtnUpdateClick();
                    }

                }
                );
            }
        }
        Show();
    }

    @Override
    public void ShowResult(final String result) {
        handler_.post(new Runnable() {


            @Override
            public void run() {
                SPDevTestActivity_.super.ShowResult(result);
            }

        }
        );
    }

    @Override
    public void showData() {
        handler_.post(new Runnable() {


            @Override
            public void run() {
                SPDevTestActivity_.super.showData();
            }

        }
        );
    }

    @Override
    public void getData() {
        BackgroundExecutor.execute(new BackgroundExecutor.Task("", 0, "") {


            @Override
            public void execute() {
                try {
                    SPDevTestActivity_.super.getData();
                } catch (Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }

        }
        );
    }

    @Override
    public void ReadResult() {
        BackgroundExecutor.execute(new BackgroundExecutor.Task("", 0, "") {


            @Override
            public void execute() {
                try {
                    SPDevTestActivity_.super.ReadResult();
                } catch (Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }

        }
        );
    }

    public static class IntentBuilder_
        extends ActivityIntentBuilder<SPDevTestActivity_.IntentBuilder_>
    {

        private android.app.Fragment fragment_;
        private android.support.v4.app.Fragment fragmentSupport_;

        public IntentBuilder_(Context context) {
            super(context, SPDevTestActivity_.class);
        }

        public IntentBuilder_(android.app.Fragment fragment) {
            super(fragment.getActivity(), SPDevTestActivity_.class);
            fragment_ = fragment;
        }

        public IntentBuilder_(android.support.v4.app.Fragment fragment) {
            super(fragment.getActivity(), SPDevTestActivity_.class);
            fragmentSupport_ = fragment;
        }

        @Override
        public void startForResult(int requestCode) {
            if (fragmentSupport_!= null) {
                fragmentSupport_.startActivityForResult(intent, requestCode);
            } else {
                if (fragment_!= null) {
                    fragment_.startActivityForResult(intent, requestCode, lastOptions);
                } else {
                    if (context instanceof Activity) {
                        Activity activity = ((Activity) context);
                        activity.startActivityForResult(intent, requestCode, lastOptions);
                    } else {
                        context.startActivity(intent, lastOptions);
                    }
                }
            }
        }

    }

}