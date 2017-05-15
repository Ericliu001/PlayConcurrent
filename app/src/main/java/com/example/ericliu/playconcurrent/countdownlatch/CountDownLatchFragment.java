package com.example.ericliu.playconcurrent.countdownlatch;


import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.ericliu.playconcurrent.R;
import com.example.ericliu.playconcurrent.listcontent.BaseFragment;
import com.example.ericliu.playconcurrent.listcontent.ListItem;
import com.example.ericliu.playconcurrent.listcontent.ProgressBarHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * A simple {@link Fragment} subclass.
 */
public class CountDownLatchFragment extends BaseFragment {
    private static final int NUM_PROGRESSBARS = 5;
    // the 5 progressbars that take turns to increment.
    private ProgressBar pb0, pb1, pb2, pb3, pb4;
    private Button btnStart;
    private ViewGroup initializingView;
    private final List<LatchWorkerThread> threads = new ArrayList<>(NUM_PROGRESSBARS);
    private CountDownLatch beforeLatch;
    private CountDownLatch afterLatch;
    private boolean hasWorkStarted;


    public static Fragment newInstance(final ListItem listItem) {
        final Fragment fragment = new CountDownLatchFragment();
        return setArguments(listItem, fragment);
    }

    public CountDownLatchFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        beforeLatch = new CountDownLatch(1);
        afterLatch = new CountDownLatch(NUM_PROGRESSBARS);

        threads.add(new LatchWorkerThread(beforeLatch, afterLatch));
        threads.add(new LatchWorkerThread(beforeLatch, afterLatch));
        threads.add(new LatchWorkerThread(beforeLatch, afterLatch));
        threads.add(new LatchWorkerThread(beforeLatch, afterLatch));
        threads.add(new LatchWorkerThread(beforeLatch, afterLatch));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_count_down_latch, container, false);
        pb0 = (ProgressBar) rootView.findViewById(R.id.progressBar0);
        pb1 = (ProgressBar) rootView.findViewById(R.id.progressBar1);
        pb2 = (ProgressBar) rootView.findViewById(R.id.progressBar2);
        pb3 = (ProgressBar) rootView.findViewById(R.id.progressBar3);
        pb4 = (ProgressBar) rootView.findViewById(R.id.progressBar4);
        btnStart = (Button) rootView.findViewById(R.id.btnStart);
        initializingView = (ViewGroup) rootView.findViewById(R.id.initializingView);
        return rootView;
    }


    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ProgressBarHandler progressBarHandler0 = new ProgressBarHandler(pb0);
        ProgressBarHandler progressBarHandler1 = new ProgressBarHandler(pb1);
        ProgressBarHandler progressBarHandler2 = new ProgressBarHandler(pb2);
        ProgressBarHandler progressBarHandler3 = new ProgressBarHandler(pb3);
        ProgressBarHandler progressBarHandler4 = new ProgressBarHandler(pb4);

        threads.get(0).setProgressBarHandler(progressBarHandler0);
        threads.get(1).setProgressBarHandler(progressBarHandler1);
        threads.get(2).setProgressBarHandler(progressBarHandler2);
        threads.get(3).setProgressBarHandler(progressBarHandler3);
        threads.get(4).setProgressBarHandler(progressBarHandler4);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new InitializingTask(beforeLatch).execute();
                toggleWork();
            }
        });
    }

    private void toggleWork() {
        if (!hasWorkStarted) {
            hasWorkStarted = true;
            for (final LatchWorkerThread thread : threads) {
                thread.start();
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        for (final LatchWorkerThread thread : threads) {
            thread.interrupt();
        }
        threads.clear();
    }

    private class InitializingTask extends AsyncTask<Void, Void, Void> {

        private CountDownLatch beforeLatch;

        public InitializingTask(final CountDownLatch beforeLatch) {
            this.beforeLatch = beforeLatch;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            initializingView.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(final Void... params) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(final Void aVoid) {
            super.onPostExecute(aVoid);
            beforeLatch.countDown();
            beforeLatch = null;
            initializingView.setVisibility(View.GONE);
        }
    }

}
