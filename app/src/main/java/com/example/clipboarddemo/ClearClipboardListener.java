package com.example.clipboarddemo;

import android.content.ClipData;
import com.good.gd.content.ClipboardManager;
import android.content.Context;

public class ClearClipboardListener implements android.content.ClipboardManager.OnPrimaryClipChangedListener {
    private final ClipboardManager clipBoard;
    private boolean attached;

    public ClearClipboardListener(ClipboardManager clipBoard) {
        this.clipBoard = clipBoard;
        attach();
    }

    @Override
    public void onPrimaryClipChanged() {
        // Clear clipboard if it's disabled and current text is not empty.
        if (!AppPolicyManager.getInstance().isOutboundDlpEnabled()) {
            ClipData clip = clipBoard.getPrimaryClip();
            if (clip != null && clip.getItemCount() > 0) {
                ClipData.Item item = clip.getItemAt(0);
                if (item.getText().length() > 0) {
                    Context ctx = MainApplication.getAppContext();

                    clip = android.content.ClipData.newPlainText("", "");
                    clipBoard.setPrimaryClip(clip);
                }
            }
        }
    }

    /**
     * Attach this instance to the clipboard.
     */
    public synchronized void attach() {
        if (!attached) {
            attached = true;
            clipBoard.addPrimaryClipChangedListener(this);
            //GCLog.d(this, "Clipboard listener attached");
        }
    }

    /**
     * Detach this instance from the clipboard.
     */
    public synchronized void detach() {
        if (attached) {
            clipBoard.removePrimaryClipChangedListener(this);
            attached = false;
            //GCLog.d(this, "Clipboard listener detached");
        }
    }
}
