package com.anutanetworks.ncxapp.model;

/**
 * Created by Aakash on 7/9/2015.
 */
public class AlarmDetailItem {
    String  name;
    String value;

    public AlarmDetailItem(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
