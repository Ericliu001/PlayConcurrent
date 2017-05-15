package com.example.ericliu.playconcurrent.countdownlatch;

import android.os.Message;

import com.example.ericliu.playconcurrent.listcontent.ProgressBarHandler;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * Created by ericliu on 15/5/17.
 */

public class LatchWorkerThread extends Thread {
    private ProgressBarHandler progressBarHandler;
    private final CountDownLatch beforeLatch;
    private final CountDownLatch afterLatch;
    private int progress;

    public LatchWorkerThread(final CountDownLatch beforeLatch, final CountDownLatch afterLatch) {
        this.beforeLatch = beforeLatch;
        this.afterLatch = afterLatch;
    }

    public void setProgressBarHandler(final ProgressBarHandler progressBarHandler) {
        this.progressBarHandler = progressBarHandler;
    }

    @Override
    public void run() {
        try {
            beforeLatch.await();

            while (progress < 100) {
                sleep(500); // we use a sleep call to mock some lengthy work.
                if (progressBarHandler != null) {
                    Random random = new Random();
                    int i = random.nextInt(10);
                    Message message = progressBarHandler.obtainMessage();
                    message.arg1 = (progress += 5 * i);
                    progressBarHandler.sendMessage(message);
                }
            }

            afterLatch.countDown();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
