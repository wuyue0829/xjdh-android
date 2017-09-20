//
// DO NOT EDIT THIS FILE.Generated using AndroidAnnotations 3.3.1.
//  You can create a larger work that contains this file and distribute that work under terms of your choice.
//


package com.chinatelecom.xjdh.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import com.chinatelecom.xjdh.R.layout;
import com.chinatelecom.xjdh.bean.ApiResponse;
import com.chinatelecom.xjdh.rest.client.ApiRestClientInterface_;
import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.api.SdkVersionHelper;
import org.androidannotations.api.builder.ActivityIntentBuilder;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class ComputerRoomActivity_
    extends ComputerRoomActivity
    implements HasViews, OnViewChangedListener
{

    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();
    public final static String STATION_CODE_EXTRA = "station_code";
    public final static String ROOM_ID_EXTRA = "roomID";
    private Handler handler_ = new Handler(Looper.getMainLooper());

    @Override
    public void onCreate(Bundle savedInstanceState) {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(onViewChangedNotifier_);
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
        setContentView(layout.activity_download_station);
    }

    private void init_(Bundle savedInstanceState) {
        OnViewChangedNotifier.registerOnViewChangedListener(this);
        mApiClient = new ApiRestClientInterface_(this);
        injectExtras_();
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

    public static ComputerRoomActivity_.IntentBuilder_ intent(Context context) {
        return new ComputerRoomActivity_.IntentBuilder_(context);
    }

    public static ComputerRoomActivity_.IntentBuilder_ intent(android.app.Fragment fragment) {
        return new ComputerRoomActivity_.IntentBuilder_(fragment);
    }

    public static ComputerRoomActivity_.IntentBuilder_ intent(android.support.v4.app.Fragment supportFragment) {
        return new ComputerRoomActivity_.IntentBuilder_(supportFragment);
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
        img_btn_reback = ((ImageButton) hasViews.findViewById(com.chinatelecom.xjdh.R.id.img_btn_reback));
        mSrlAlarm = ((SwipeRefreshLayout) hasViews.findViewById(com.chinatelecom.xjdh.R.id.srl_alarm));
        lv_stationName_grouping = ((ListView) hasViews.findViewById(com.chinatelecom.xjdh.R.id.lv_stationName_grouping));
        if (lv_stationName_grouping!= null) {
            lv_stationName_grouping.setOnItemClickListener(new OnItemClickListener() {


                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ComputerRoomActivity_.this.groupingClicked(position);
                }

            }
            );
        }
        initData();
    }

    private void injectExtras_() {
        Bundle extras_ = getIntent().getExtras();
        if (extras_!= null) {
            if (extras_.containsKey(STATION_CODE_EXTRA)) {
                station_code = extras_.getInt(STATION_CODE_EXTRA);
            }
            if (extras_.containsKey(ROOM_ID_EXTRA)) {
                roomID = extras_.getInt(ROOM_ID_EXTRA);
            }
        }
    }

    @Override
    public void setIntent(Intent newIntent) {
        super.setIntent(newIntent);
        injectExtras_();
    }

    @Override
    public void ShowResponse(final ApiResponse apiResp) {
        handler_.post(new Runnable() {


            @Override
            public void run() {
                ComputerRoomActivity_.super.ShowResponse(apiResp);
            }

        }
        );
    }

    @Override
    public void getData(final int type, final int code) {
        BackgroundExecutor.execute(new BackgroundExecutor.Task("", 0, "") {


            @Override
            public void execute() {
                try {
                    ComputerRoomActivity_.super.getData(type, code);
                } catch (Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }

        }
        );
    }

    public static class IntentBuilder_
        extends ActivityIntentBuilder<ComputerRoomActivity_.IntentBuilder_>
    {

        private android.app.Fragment fragment_;
        private android.support.v4.app.Fragment fragmentSupport_;

        public IntentBuilder_(Context context) {
            super(context, ComputerRoomActivity_.class);
        }

        public IntentBuilder_(android.app.Fragment fragment) {
            super(fragment.getActivity(), ComputerRoomActivity_.class);
            fragment_ = fragment;
        }

        public IntentBuilder_(android.support.v4.app.Fragment fragment) {
            super(fragment.getActivity(), ComputerRoomActivity_.class);
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

        public ComputerRoomActivity_.IntentBuilder_ station_code(int station_code) {
            return super.extra(STATION_CODE_EXTRA, station_code);
        }

        public ComputerRoomActivity_.IntentBuilder_ roomID(int roomID) {
            return super.extra(ROOM_ID_EXTRA, roomID);
        }

    }

}