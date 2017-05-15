package com.example.ericliu.playconcurrent.listcontent;

import android.os.Handler;
import android.os.Message;
import android.widget.ProgressBar;

import java.lang.ref.WeakReference;

/**
 * Created by ericliu on 15/5/17.
 */
public class ProgressBarHandler extends Handler {

    private final WeakReference<ProgressBar> progressBarWeakReference;

    public ProgressBarHandler(ProgressBar progressBar) {
        progressBarWeakReference = new WeakReference<>(progressBar);
    }

    @Override
    public void handleMessage(final Message msg) {
        if (progressBarWeakReference.get() != null) {
            final int progress = msg.arg1;
            final ProgressBar progressBar = progressBarWeakReference.get();
            progressBar.setProgress(progress);
        }
    }
}
