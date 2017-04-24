package com.example.ericliu.playconcurrent.listcontent;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.view.View;

import com.example.ericliu.playconcurrent.R;

import static com.example.ericliu.playconcurrent.listcontent.ItemDetailsActivity.ARG_ITEM;

/**
 * Created by ericliu on 11/4/17.
 */

public class BaseFragment extends Fragment {
    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Bundle arguments = getArguments();
        final ListItem listItem = arguments.getParcelable(ARG_ITEM);

        if (getArguments().containsKey(ARG_ITEM)) {

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(listItem.content);
            }
        }
    }
}
