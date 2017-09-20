//
// DO NOT EDIT THIS FILE.Generated using AndroidAnnotations 3.3.1.
//  You can create a larger work that contains this file and distribute that work under terms of your choice.
//


package com.chinatelecom.xjdh.receiver;

import android.content.Context;
import android.content.Intent;

public final class AppBroadcastReceiver_
    extends AppBroadcastReceiver
{

    public final static String ACTIONS_ON_RECEIVE_NETWORK_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";
    public final static String ACTIONS_ON_RECEIVE_BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        init_(context);
        super.onReceive(context, intent);
        String action = intent.getAction();
        if (ACTIONS_ON_RECEIVE_NETWORK_CHANGE.equals(action)) {
            AppBroadcastReceiver_.this.onReceiveNetworkChange(context);
            return ;
        }
        if (ACTIONS_ON_RECEIVE_BOOT_COMPLETED.equals(action)) {
            AppBroadcastReceiver_.this.onReceiveBootCompleted(context);
            return ;
        }
    }

    private void init_(Context context) {
    }

}