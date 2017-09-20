//
// DO NOT EDIT THIS FILE.Generated using AndroidAnnotations 3.3.1.
//  You can create a larger work that contains this file and distribute that work under terms of your choice.
//


package com.chinatelecom.xjdh.ui;

import java.sql.SQLException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import com.chinatelecom.xjdh.R.id;
import com.chinatelecom.xjdh.R.layout;
import com.chinatelecom.xjdh.db.DatabaseHelper;
import com.chinatelecom.xjdh.model.GroupingName;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import org.androidannotations.api.builder.ActivityIntentBuilder;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class GroupingNameActivity_
    extends GroupingNameActivity
    implements HasViews, OnViewChangedListener
{

    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();
    private DatabaseHelper databaseHelper_;
    public final static String STATION_NAME_EXTRA = "stationName";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(onViewChangedNotifier_);
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
        setContentView(layout.activity_grouping_name);
    }

    private void init_(Bundle savedInstanceState) {
        OnViewChangedNotifier.registerOnViewChangedListener(this);
        databaseHelper_ = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        try {
            sDao = databaseHelper_.getDao(GroupingName.class);
        } catch (SQLException e) {
            Log.e("GroupingNameActivity_", "Could not create DAO sDao", e);
        }
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

    public static GroupingNameActivity_.IntentBuilder_ intent(Context context) {
        return new GroupingNameActivity_.IntentBuilder_(context);
    }

    public static GroupingNameActivity_.IntentBuilder_ intent(android.app.Fragment fragment) {
        return new GroupingNameActivity_.IntentBuilder_(fragment);
    }

    public static GroupingNameActivity_.IntentBuilder_ intent(android.support.v4.app.Fragment supportFragment) {
        return new GroupingNameActivity_.IntentBuilder_(supportFragment);
    }

    @Override
    public void onViewChanged(HasViews hasViews) {
        et_append_grouping_name = ((EditText) hasViews.findViewById(id.et_append_grouping_name));
        {
            View view = hasViews.findViewById(id.btn_save_grouping_name);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        GroupingNameActivity_.this.groupingName();
                    }

                }
                );
            }
        }
        initData();
    }

    @Override
    public void onDestroy() {
        OpenHelperManager.releaseHelper();
        databaseHelper_ = null;
        super.onDestroy();
    }

    private void injectExtras_() {
        Bundle extras_ = getIntent().getExtras();
        if (extras_!= null) {
            if (extras_.containsKey(STATION_NAME_EXTRA)) {
                stationName = extras_.getString(STATION_NAME_EXTRA);
            }
        }
    }

    @Override
    public void setIntent(Intent newIntent) {
        super.setIntent(newIntent);
        injectExtras_();
    }

    public static class IntentBuilder_
        extends ActivityIntentBuilder<GroupingNameActivity_.IntentBuilder_>
    {

        private android.app.Fragment fragment_;
        private android.support.v4.app.Fragment fragmentSupport_;

        public IntentBuilder_(Context context) {
            super(context, GroupingNameActivity_.class);
        }

        public IntentBuilder_(android.app.Fragment fragment) {
            super(fragment.getActivity(), GroupingNameActivity_.class);
            fragment_ = fragment;
        }

        public IntentBuilder_(android.support.v4.app.Fragment fragment) {
            super(fragment.getActivity(), GroupingNameActivity_.class);
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

        public GroupingNameActivity_.IntentBuilder_ stationName(String stationName) {
            return super.extra(STATION_NAME_EXTRA, stationName);
        }

    }

}
