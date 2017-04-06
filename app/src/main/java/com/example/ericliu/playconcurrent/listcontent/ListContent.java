package com.example.ericliu.playconcurrent.listcontent;

import com.example.ericliu.playconcurrent.semaphore.SemaphoreFragment;

import java.util.ArrayList;
import java.util.List;

public class ListContent {

    public static final List<ListItem> ITEMS = new ArrayList<ListItem>();


    static {
        ITEMS.add(new ListItem("Semaphore", SemaphoreFragment.class));
    }

}
