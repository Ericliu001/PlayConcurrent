package com.example.ericliu.playconcurrent.semaphore;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.ericliu.playconcurrent.R;
import com.example.ericliu.playconcurrent.listcontent.BaseFragment;
import com.example.ericliu.playconcurrent.listcontent.ItemDetailsActivity;
import com.example.ericliu.playconcurrent.listcontent.ItemListActivity;
import com.example.ericliu.playconcurrent.listcontent.ListItem;

import java.lang.ref.WeakReference;
import java.util.concurrent.Semaphore;

import static com.example.ericliu.playconcurrent.listcontent.ItemDetailsActivity.ARG_ITEM;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailsActivity}
 * on handsets.
 */
public class SemaphoreFragment extends BaseFragment {

    private ProgressBar pb1, pb2, pb3, pb4, pb5;

    private static final int NUM_PROGRESSBARS = 5;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SemaphoreFragment() {
    }


    public static Fragment newInstance(final ListItem listItem) {
        final Fragment fragment = new SemaphoreFragment();
        final Bundle args = new Bundle();
        args.putParcelable(ARG_ITEM, listItem);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);
        pb1 = (ProgressBar) rootView.findViewById(R.id.progressBar1);
        pb2 = (ProgressBar) rootView.findViewById(R.id.progressBar2);
        pb3 = (ProgressBar) rootView.findViewById(R.id.progressBar3);
        pb4 = (ProgressBar) rootView.findViewById(R.id.progressBar4);
        pb5 = (ProgressBar) rootView.findViewById(R.id.progressBar5);

        return rootView;
    }


    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ProgressBarHandler progressBarHandler1 = new ProgressBarHandler(pb1);
        ProgressBarHandler progressBarHandler2 = new ProgressBarHandler(pb2);
        ProgressBarHandler progressBarHandler3 = new ProgressBarHandler(pb3);
        ProgressBarHandler progressBarHandler4 = new ProgressBarHandler(pb4);
        ProgressBarHandler progressBarHandler5 = new ProgressBarHandler(pb5);

        Semaphore[] semaphores = new Semaphore[NUM_PROGRESSBARS];
        semaphores[0] = new Semaphore(1);
        semaphores[1] = new Semaphore(0);
        semaphores[2] = new Semaphore(0);
        semaphores[3] = new Semaphore(0);
        semaphores[4] = new Semaphore(0);


        new WorkerThread(progressBarHandler1, semaphores, 0).start();
        new WorkerThread(progressBarHandler2, semaphores, 1).start();
        new WorkerThread(progressBarHandler3, semaphores, 2).start();
        new WorkerThread(progressBarHandler4, semaphores, 3).start();
        new WorkerThread(progressBarHandler5, semaphores, 4).start();
    }

    private static class ProgressBarHandler extends Handler {

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


    private static class WorkerThread extends Thread {

        private final ProgressBarHandler progressBarHandler;
        private final Semaphore[] semaphores;
        private final int index;
        private int progress;

        public WorkerThread(final ProgressBarHandler progressBarHandler, final Semaphore[] semaphores, final int index) {
            this.progressBarHandler = progressBarHandler;
            this.semaphores = semaphores;
            this.index = index;
        }


        @Override
        public void run() {
            final Semaphore currentSemaphore = semaphores[index];
            final Semaphore nextSemaphore = semaphores[(index + 1) % semaphores.length];

            try {

                while (progress <= 100) {

                    currentSemaphore.acquire();

                    sleep(1000);
                    Message message = progressBarHandler.obtainMessage();
                    message.arg1 = (progress += 10);
                    progressBarHandler.sendMessage(message);

                    nextSemaphore.release();

                }
                interrupt();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

