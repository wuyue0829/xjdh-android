//
// DO NOT EDIT THIS FILE.Generated using AndroidAnnotations 3.3.1.
//  You can create a larger work that contains this file and distribute that work under terms of your choice.
//


package com.chinatelecom.xjdh.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.chinatelecom.xjdh.R.layout;
import com.chinatelecom.xjdh.bean.ApiResponseUpLoad;
import com.chinatelecom.xjdh.rest.client.ApiRestClientInterface_;
import com.chinatelecom.xjdh.view.MyGridView;
import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.api.SdkVersionHelper;
import org.androidannotations.api.builder.ActivityIntentBuilder;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class WorkRealismActivity_
    extends WorkRealismActivity
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
        setContentView(layout.realistic_fragment);
    }

    private void init_(Bundle savedInstanceState) {
        OnViewChangedNotifier.registerOnViewChangedListener(this);
        mApiClient = new ApiRestClientInterface_(this);
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

    public static WorkRealismActivity_.IntentBuilder_ intent(Context context) {
        return new WorkRealismActivity_.IntentBuilder_(context);
    }

    public static WorkRealismActivity_.IntentBuilder_ intent(android.app.Fragment fragment) {
        return new WorkRealismActivity_.IntentBuilder_(fragment);
    }

    public static WorkRealismActivity_.IntentBuilder_ intent(android.support.v4.app.Fragment supportFragment) {
        return new WorkRealismActivity_.IntentBuilder_(supportFragment);
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
        mSpRoom = ((Spinner) hasViews.findViewById(com.chinatelecom.xjdh.R.id.sp_room));
        etMemo = ((EditText) hasViews.findViewById(com.chinatelecom.xjdh.R.id.etMemo));
        gggridview = ((MyGridView) hasViews.findViewById(com.chinatelecom.xjdh.R.id.gggridview));
        etPerson = ((EditText) hasViews.findViewById(com.chinatelecom.xjdh.R.id.etPerson));
        spCategory = ((EditText) hasViews.findViewById(com.chinatelecom.xjdh.R.id.spCategory));
        mSpCounty = ((Spinner) hasViews.findViewById(com.chinatelecom.xjdh.R.id.sp_county));
        mSpCity = ((Spinner) hasViews.findViewById(com.chinatelecom.xjdh.R.id.sp_city));
        mSpSubstation = ((Spinner) hasViews.findViewById(com.chinatelecom.xjdh.R.id.sp_substation));
        tvDate = ((TextView) hasViews.findViewById(com.chinatelecom.xjdh.R.id.tvDate));
        {
            View view = hasViews.findViewById(com.chinatelecom.xjdh.R.id.saveMassage);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        WorkRealismActivity_.this.submitClick();
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(com.chinatelecom.xjdh.R.id.etDate);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        WorkRealismActivity_.this.onDateClick();
                    }

                }
                );
                view.setOnFocusChangeListener(new OnFocusChangeListener() {


                    @Override
                    public void onFocusChange(View view, boolean hasFocus) {
                        WorkRealismActivity_.this.onDateClick(view, hasFocus);
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(com.chinatelecom.xjdh.R.id.quxiao);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        WorkRealismActivity_.this.Canncel();
                    }

                }
                );
            }
        }
        if (gggridview!= null) {
            gggridview.setOnItemClickListener(new OnItemClickListener() {


                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    WorkRealismActivity_.this.OnGridItemClick(position);
                }

            }
            );
        }
        bindData();
    }

    @Override
    public void ShowResponsess(final ApiResponseUpLoad resp) {
        handler_.post(new Runnable() {


            @Override
            public void run() {
                WorkRealismActivity_.super.ShowResponsess(resp);
            }

        }
        );
    }

    @Override
    public void DoWorkerUpload(final String citycode, final String workCategoryId, final String countycode, final String person, final String memo, final String date, final String substationId, final String roomId) {
        BackgroundExecutor.execute(new BackgroundExecutor.Task("", 0, "") {


            @Override
            public void execute() {
                try {
                    WorkRealismActivity_.super.DoWorkerUpload(citycode, workCategoryId, countycode, person, memo, date, substationId, roomId);
                } catch (Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }

        }
        );
    }

    public static class IntentBuilder_
        extends ActivityIntentBuilder<WorkRealismActivity_.IntentBuilder_>
    {

        private android.app.Fragment fragment_;
        private android.support.v4.app.Fragment fragmentSupport_;

        public IntentBuilder_(Context context) {
            super(context, WorkRealismActivity_.class);
        }

        public IntentBuilder_(android.app.Fragment fragment) {
            super(fragment.getActivity(), WorkRealismActivity_.class);
            fragment_ = fragment;
        }

        public IntentBuilder_(android.support.v4.app.Fragment fragment) {
            super(fragment.getActivity(), WorkRealismActivity_.class);
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