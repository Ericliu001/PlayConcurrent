package com.example.ericliu.playconcurrent.countdownlatch;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ericliu.playconcurrent.R;
import com.example.ericliu.playconcurrent.listcontent.BaseFragment;
import com.example.ericliu.playconcurrent.listcontent.ListItem;

/**
 * A simple {@link Fragment} subclass.
 */
public class CountDownLatchFragment extends BaseFragment {


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
        return inflater.inflate(R.layout.fragment_count_down_latch, container, false);
    }

}
