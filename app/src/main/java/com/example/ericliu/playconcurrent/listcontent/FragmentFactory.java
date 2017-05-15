package com.example.ericliu.playconcurrent.listcontent;


import android.app.Fragment;

import com.example.ericliu.playconcurrent.countdownlatch.CountDownLatchFragment;
import com.example.ericliu.playconcurrent.semaphore.SemaphoreFragment;

/**
 * Created by ericliu on 6/4/17.
 */

public final class FragmentFactory {


    public Fragment createFragment(ListItem listItem) {
        if (listItem.fragment.equals(SemaphoreFragment.class)) {

            return SemaphoreFragment.newInstance(listItem);

        } else if (listItem.fragment.equals(CountDownLatchFragment.class)) {

            return CountDownLatchFragment.newInstance(listItem);
            
        } else {
            throw new IllegalArgumentException("The Fragment type: " + listItem + " is not supported.");
        }
    }
}
