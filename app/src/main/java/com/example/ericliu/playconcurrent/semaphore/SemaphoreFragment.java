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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import static com.example.ericliu.playconcurrent.listcontent.ItemDetailsActivity.ARG_ITEM;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailsActivity}
 * on handsets.
 */
public class SemaphoreFragment extends BaseFragment {

    // the 5 progressbars that take turns to increment.
    private ProgressBar pb1, pb2, pb3, pb4, pb5;

    private static final int NUM_PROGRESSBARS = 5;

    private final List<WorkerThread> threads = new ArrayList<>(NUM_PROGRESSBARS);

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
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        Semaphore[] semaphores = new Semaphore[NUM_PROGRESSBARS];

        // notice the first Semaphore is 'unlocked' while the rest are 'locked'
        semaphores[0] = new Semaphore(1);
        semaphores[1] = new Semaphore(0);
        semaphores[2] = new Semaphore(0);
        semaphores[3] = new Semaphore(0);
        semaphores[4] = new Semaphore(0);


        threads.add(new WorkerThread(semaphores, 0));
        threads.add(new WorkerThread(semaphores, 1));
        threads.add(new WorkerThread(semaphores, 2));
        threads.add(new WorkerThread(semaphores, 3));
        threads.add(new WorkerThread(semaphores, 4));

        for (final WorkerThread thread : threads) {
            thread.start();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
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

        threads.get(0).setProgressBarHandler(progressBarHandler1);
        threads.get(1).setProgressBarHandler(progressBarHandler2);
        threads.get(2).setProgressBarHandler(progressBarHandler3);
        threads.get(3).setProgressBarHandler(progressBarHandler4);
        threads.get(4).setProgressBarHandler(progressBarHandler5);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (final WorkerThread thread : threads) {
            thread.interrupt();
        }
        threads.clear();
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

        private ProgressBarHandler progressBarHandler;
        private final Semaphore[] semaphores;
        private final int index;
        private int progress;

        public WorkerThread(final Semaphore[] semaphores, final int index) {
            this.semaphores = semaphores;
            this.index = index;
        }


        public void setProgressBarHandler(final ProgressBarHandler progressBarHandler) {
            this.progressBarHandler = progressBarHandler;
        }

        /**
         * We use Semaphores here to coordinate the threads because the Semaphore in java is not 'fully-bracketed',
         * which means the thread to release a permit does not have to be the one that has acquired
         * the permit in the first place.
         * We can utilise this feature of Semaphore to let one thread to release a permit for the next thread.
         */
        @Override
        public void run() {
            final Semaphore currentSemaphore = semaphores[index];
            final Semaphore nextSemaphore = semaphores[(index + 1) % semaphores.length];

            try {
                while (true) {
                    currentSemaphore.acquire();

                    sleep(1000); // we use a sleep call to mock some lengthy work.
                    if (progressBarHandler != null) {
                        Message message = progressBarHandler.obtainMessage();
                        message.arg1 = (progress += 10);
                        progressBarHandler.sendMessage(message);
                    }

                    nextSemaphore.release();

                    if (progress == 100) {
                        progress = 0;
                    }
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

