package com.example.ericliu.playconcurrent.listcontent;

import android.app.Activity;
import android.app.Fragment;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * A list item representing a element in the list.
 */
public class ListItem implements Parcelable {
    public final String content;
    public final Class<? extends Fragment> fragment;

    public ListItem(String content, Class<? extends Fragment> fragment) {
        this.content = content;
        this.fragment = fragment;
    }

    @Override
    public String toString() {
        return content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.content);
        dest.writeSerializable(this.fragment);
    }

    protected ListItem(Parcel in) {
        this.content = in.readString();
        this.fragment = (Class<? extends Fragment>) in.readSerializable();
    }

    public static final Parcelable.Creator<ListItem> CREATOR = new Parcelable.Creator<ListItem>() {
        @Override
        public ListItem createFromParcel(Parcel source) {
            return new ListItem(source);
        }

        @Override
        public ListItem[] newArray(int size) {
            return new ListItem[size];
        }
    };
}
