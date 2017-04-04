package com.example.ericliu.playconcurrent.dummy;

import com.example.ericliu.playconcurrent.ItemDetailActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class ListContent {

    public static final List<ListItem> ITEMS = new ArrayList<ListItem>();


    static {
        ITEMS.add(new ListItem("Semaphore", ItemDetailActivity.class));
    }

}
