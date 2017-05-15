package com.example.ericliu.playconcurrent.countdownlatch;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.ericliu.playconcurrent.R;
import com.example.ericliu.playconcurrent.listcontent.BaseFragment;
import com.example.ericliu.playconcurrent.listcontent.ListItem;

/**
 * A simple {@link Fragment} subclass.
 */
public class CountDownLatchFragment extends BaseFragment {
    // the 5 progressbars that take turns to increment.
    private ProgressBar  pb0, pb1, pb2, pb3, pb4;


    public static Fragment newInstance(final ListItem listItem) {
        final Fragment fragment = new CountDownLatchFragment();
        return setArguments(listItem, fragment);
    }

    public CountDownLatchFragment() {
        // Required empty public constructor
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
        return rootView;
    }

}
