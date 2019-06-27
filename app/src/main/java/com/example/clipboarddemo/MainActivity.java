package com.example.clipboarddemo;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;

import com.good.gd.GDAndroid;
import com.good.gd.GDStateListener;
import com.good.gd.content.ClipboardManager;

import java.util.Map;

public class MainActivity extends AppCompatActivity implements GDStateListener {

    private WebView webView = null;

    public enum GDStatus {
        GD_AUTHORIZED,
        GD_NOT_AUTHORIZED_TEMPORARY,
        GD_CONFIG_CHANGED,
        GD_POLICY_CHANGED,
        GD_SERVICES_CHANGED,
        GD_STATUS_UNKNOWN,
        GD_NOT_AUTHORIZED_PERMANENT;
    }

    private static final String TAG = MainActivity.class.getSimpleName();
    private Intent uiIntent = null;
    private Context context = null;
    private boolean uiLaunched = true;
    // notification constants

    private GDStatus gdStatus = GDStatus.GD_STATUS_UNKNOWN;

    // Get instance of clipboard manager
    public ClipboardManager clipboardManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GDAndroid.getInstance().activityInit(this);
        webView = findViewById(R.id.mainWebView);
        setupWebView();
    }
    
    private void setupWebView() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl("https://getbevel.com/login");
    }

    @Override
    public void onAuthorized() {
        Log.d(TAG, "onAuthorized");

        gdStatus = GDStatus.GD_AUTHORIZED;

        AppPolicyManager.getInstance().init();

        if (!uiLaunched && uiIntent != null && context != null)
        {
            uiLaunched = true;
        }

        // assign clipboard listener
        clipboardManager = ClipboardManager.getInstance(MainApplication.getAppContext());
        clipboardManager.addPrimaryClipChangedListener(new android.content.ClipboardManager.OnPrimaryClipChangedListener() {

            @Override
            public void onPrimaryClipChanged() {
                if (!AppPolicyManager.getInstance().isOutboundDlpEnabled()) {
                    ClipData clip = clipboardManager.getPrimaryClip();
                    if (clip != null && clip.getItemCount() > 0) {
                        ClipData.Item item = clip.getItemAt(0);
                        if (item.getText().length() > 0) {
                            Context ctx = MainApplication.getAppContext();

                            clip = android.content.ClipData.newPlainText("", "");
                            clipboardManager.setPrimaryClip(clip);
                        }
                    }
                }
            }
        });


//        if (AppPolicyManager.getInstance().isOutboundDlpEnabled() || AppPolicyManager.getInstance().isInboundDlpEnabled()) {
//            webView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    Toast.makeText(MainActivity.this, "Copy and Paste Disabled", Toast.LENGTH_LONG).show();
//                    return true;
//                }
//            });
//            webView.setLongClickable(false);
//            webView.setHapticFeedbackEnabled(false);
//        }
    }

    @Override
    public void onLocked() {

    }

    @Override
    public void onWiped() {

    }

    @Override
    public void onUpdateConfig(Map<String, Object> map) {

    }

    @Override
    public void onUpdatePolicy(Map<String, Object> map) {

    }

    @Override
    public void onUpdateServices() {

    }

    @Override
    public void onUpdateEntitlements() {

    }
}
