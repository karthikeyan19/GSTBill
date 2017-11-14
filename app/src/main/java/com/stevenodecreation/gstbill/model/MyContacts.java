package com.stevenodecreation.gstbill.model;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by lenovo on 29-09-2017.
 */

public class MyContacts implements Comparable<MyContacts> {

    public String id;
    public String displayName;
    public List<String> phoneNoList;
    public List<String> emailIdList;

    @Override
    public int compareTo(@NonNull MyContacts myContacts) {
        return displayName.toLowerCase().compareTo(myContacts.displayName.toLowerCase());
    }
}
