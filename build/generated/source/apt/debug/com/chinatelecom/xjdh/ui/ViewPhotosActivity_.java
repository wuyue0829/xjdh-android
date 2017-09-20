//
// DO NOT EDIT THIS FILE.Generated using AndroidAnnotations 3.3.1.
//  You can create a larger work that contains this file and distribute that work under terms of your choice.
//


package com.chinatelecom.xjdh.ui;

import java.io.Serializable;
import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import com.chinatelecom.xjdh.R.id;
import com.chinatelecom.xjdh.R.layout;
import com.chinatelecom.xjdh.zoom.ViewPagerFixed;
import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.api.SdkVersionHelper;
import org.androidannotations.api.builder.ActivityIntentBuilder;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class ViewPhotosActivity_
    extends ViewPhotosActivity
    implements HasViews, OnViewChangedListener
{

    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();
    public final static String IMAGE_PATH_LIST_EXTRA = "imagePathList";
    public final static String IMAGE_LIST_EXTRA = "image_list";
    public final static String POSITION_EXTRA = "position";
    private Handler handler_ = new Handler(Looper.getMainLooper());

    @Override
    public void onCreate(Bundle savedInstanceState) {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(onViewChangedNotifier_);
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
        setContentView(layout.view_photos);
    }

    private void init_(Bundle savedInstanceState) {
        OnViewChangedNotifier.registerOnViewChangedListener(this);
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

    public static ViewPhotosActivity_.IntentBuilder_ intent(Context context) {
        return new ViewPhotosActivity_.IntentBuilder_(context);
    }

    public static ViewPhotosActivity_.IntentBuilder_ intent(android.app.Fragment fragment) {
        return new ViewPhotosActivity_.IntentBuilder_(fragment);
    }

    public static ViewPhotosActivity_.IntentBuilder_ intent(android.support.v4.app.Fragment supportFragment) {
        return new ViewPhotosActivity_.IntentBuilder_(supportFragment);
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
        vpGallery = ((ViewPagerFixed) hasViews.findViewById(id.vpGallery));
        {
            View view = hasViews.findViewById(id.btnDelete);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        ViewPhotosActivity_.this.OnBtnDeleteClicked();
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(id.btnDone);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        ViewPhotosActivity_.this.OnBtnDoneClicked();
                    }

                }
                );
            }
        }
        ShowView();
    }

    private void injectExtras_() {
        Bundle extras_ = getIntent().getExtras();
        if (extras_!= null) {
            if (extras_.containsKey(IMAGE_PATH_LIST_EXTRA)) {
                imagePathList = extras_.getStringArrayList(IMAGE_PATH_LIST_EXTRA);
            }
            if (extras_.containsKey(IMAGE_LIST_EXTRA)) {
                image_list = extras_.getStringArrayList(IMAGE_LIST_EXTRA);
            }
            if (extras_.containsKey(POSITION_EXTRA)) {
                position = extras_.getInt(POSITION_EXTRA);
            }
        }
    }

    @Override
    public void setIntent(Intent newIntent) {
        super.setIntent(newIntent);
        injectExtras_();
    }

    @Override
    public void Show() {
        handler_.post(new Runnable() {


            @Override
            public void run() {
                ViewPhotosActivity_.super.Show();
            }

        }
        );
    }

    @Override
    public void LoadData() {
        BackgroundExecutor.execute(new BackgroundExecutor.Task("", 0, "") {


            @Override
            public void execute() {
                try {
                    ViewPhotosActivity_.super.LoadData();
                } catch (Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }

        }
        );
    }

    public static class IntentBuilder_
        extends ActivityIntentBuilder<ViewPhotosActivity_.IntentBuilder_>
    {

        private android.app.Fragment fragment_;
        private android.support.v4.app.Fragment fragmentSupport_;

        public IntentBuilder_(Context context) {
            super(context, ViewPhotosActivity_.class);
        }

        public IntentBuilder_(android.app.Fragment fragment) {
            super(fragment.getActivity(), ViewPhotosActivity_.class);
            fragment_ = fragment;
        }

        public IntentBuilder_(android.support.v4.app.Fragment fragment) {
            super(fragment.getActivity(), ViewPhotosActivity_.class);
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

        public ViewPhotosActivity_.IntentBuilder_ imagePathList(ArrayList<String> imagePathList) {
            return super.extra(IMAGE_PATH_LIST_EXTRA, ((Serializable) imagePathList));
        }

        public ViewPhotosActivity_.IntentBuilder_ image_list(ArrayList<String> image_list) {
            return super.extra(IMAGE_LIST_EXTRA, ((Serializable) image_list));
        }

        public ViewPhotosActivity_.IntentBuilder_ position(int position) {
            return super.extra(POSITION_EXTRA, position);
        }

    }

}
