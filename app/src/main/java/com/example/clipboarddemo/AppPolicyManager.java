package com.example.clipboarddemo;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.good.gd.GDAndroid;
import com.good.gd.content.ClipboardManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Thread.setDefaultUncaughtExceptionHandler;

/**
 * Created by davidfekke on 1/23/17.
 * TODO: Add policy settings for Download and Upload.
 */

public class AppPolicyManager {

    private static AppPolicyManager _instance = new AppPolicyManager();

    private ClearClipboardListener clearClipboardListener;

    public static AppPolicyManager getInstance() {
        if (_instance == null) {
            _instance = new AppPolicyManager();
        }
        return _instance;
    }

    private AppPolicyManager() {

    }

    public void init() {
        boolean inbound = getInstance().isInboundDlpEnabled();
        boolean outbound = getInstance().isOutboundDlpEnabled();
    }

    /**
     * @return "Prevent copy from non-GD apps into GD apps" policy flag on GC
     */
    public boolean isInboundDlpEnabled(){
        final Map<String, Object> appConfig = GDAndroid.getInstance().getApplicationConfig();
        return (Boolean) appConfig.get(GDAndroid.GDAppConfigKeyPreventDataLeakageIn);
    }

    /**
     * @return "Prevent copy from GD apps into non-GD apps" policy flag on GC
     */
    boolean isOutboundDlpEnabled(){
        final Map<String, Object> appConfig = GDAndroid.getInstance().getApplicationConfig();
        return (Boolean) appConfig.get(GDAndroid.GDAppConfigKeyPreventDataLeakageOut);
    }

    /**
     * @return "Prevent Android Dictation" policy flag on GC
     */
    public boolean isDictationPreventionEnabled(){
        final Map<String, Object> appConfig = GDAndroid.getInstance().getApplicationConfig();
        return (Boolean) appConfig.get(GDAndroid.GDAppConfigKeyPreventAndroidDictation);
    }
}
