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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.chinatelecom.xjdh.R.layout;
import com.chinatelecom.xjdh.bean.ApiResponse;
import com.chinatelecom.xjdh.rest.client.ApiRestClientInterface_;
import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.api.builder.FragmentBuilder;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class DoneFragment_
    extends com.chinatelecom.xjdh.fragment.DoneFragment
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
            contentView_ = inflater.inflate(layout.activity_download_station, container, false);
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
        mApiClient = new ApiRestClientInterface_(getActivity());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    public static DoneFragment_.FragmentBuilder_ builder() {
        return new DoneFragment_.FragmentBuilder_();
    }

    @Override
    public void onViewChanged(HasViews hasViews) {
        lv_stationName_grouping = ((ListView) hasViews.findViewById(com.chinatelecom.xjdh.R.id.lv_stationName_grouping));
        mSrlAlarm = ((SwipeRefreshLayout) hasViews.findViewById(com.chinatelecom.xjdh.R.id.srl_alarm));
        if (lv_stationName_grouping!= null) {
            lv_stationName_grouping.setOnItemClickListener(new OnItemClickListener() {


                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    DoneFragment_.this.groupingClicked(position);
                }

            }
            );
        }
        initData();
    }

    @Override
    public void ShowResponse(final ApiResponse apiResp) {
        handler_.post(new Runnable() {


            @Override
            public void run() {
                DoneFragment_.super.ShowResponse(apiResp);
            }

        }
        );
    }

    @Override
    public void getData(final int code, final int type, final int locationType) {
        BackgroundExecutor.execute(new BackgroundExecutor.Task("", 0, "") {


            @Override
            public void execute() {
                try {
                    DoneFragment_.super.getData(code, type, locationType);
                } catch (Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }

        }
        );
    }

    public static class FragmentBuilder_
        extends FragmentBuilder<DoneFragment_.FragmentBuilder_, com.chinatelecom.xjdh.fragment.DoneFragment>
    {


        @Override
        public com.chinatelecom.xjdh.fragment.DoneFragment build() {
            DoneFragment_ fragment_ = new DoneFragment_();
            fragment_.setArguments(args);
            return fragment_;
        }

    }

}
