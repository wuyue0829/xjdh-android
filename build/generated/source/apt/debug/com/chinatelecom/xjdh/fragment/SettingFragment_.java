//
// DO NOT EDIT THIS FILE.Generated using AndroidAnnotations 3.3.1.
//  You can create a larger work that contains this file and distribute that work under terms of your choice.
//


package com.chinatelecom.xjdh.fragment;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.SwitchPreference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.chinatelecom.xjdh.R.string;
import com.chinatelecom.xjdh.R.xml;
import org.androidannotations.api.builder.FragmentBuilder;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class SettingFragment_
    extends com.chinatelecom.xjdh.fragment.SettingFragment
    implements HasViews
{

    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();
    private View contentView_;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(onViewChangedNotifier_);
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(xml.preferences);
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
        return contentView_;
    }

    @Override
    public void onDestroyView() {
        contentView_ = null;
        super.onDestroyView();
    }

    private void init_(Bundle savedInstanceState) {
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    public static SettingFragment_.FragmentBuilder_ builder() {
        return new SettingFragment_.FragmentBuilder_();
    }

    @Override
    public void addPreferencesFromResource(int preferencesResId) {
        super.addPreferencesFromResource(preferencesResId);
        spfBackground = ((SwitchPreference) this.findPreference(this.getString(string.new_message_background)));
        {
            Preference preference = this.findPreference(this.getString(string.logout));
            if (preference!= null) {
                preference.setOnPreferenceClickListener(new OnPreferenceClickListener() {


                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        SettingFragment_.this.onPreferenceLogoutClicked();
                        return true;
                    }

                }
                );
            }
        }
        {
            Preference preference = this.findPreference(this.getString(string.app_exit));
            if (preference!= null) {
                preference.setOnPreferenceClickListener(new OnPreferenceClickListener() {


                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        SettingFragment_.this.onPreferenceExitClicked();
                        return true;
                    }

                }
                );
            }
        }
        {
            Preference preference = this.findPreference(this.getString(string.app_about));
            if (preference!= null) {
                preference.setOnPreferenceClickListener(new OnPreferenceClickListener() {


                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        SettingFragment_.this.onPreferenceAboutClicked();
                        return true;
                    }

                }
                );
            }
        }
    }

    public static class FragmentBuilder_
        extends FragmentBuilder<SettingFragment_.FragmentBuilder_, com.chinatelecom.xjdh.fragment.SettingFragment>
    {


        @Override
        public com.chinatelecom.xjdh.fragment.SettingFragment build() {
            SettingFragment_ fragment_ = new SettingFragment_();
            fragment_.setArguments(args);
            return fragment_;
        }

    }

}
