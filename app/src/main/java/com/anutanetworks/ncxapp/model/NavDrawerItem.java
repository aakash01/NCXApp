package com.anutanetworks.ncxapp.model;

/**
 * Created by Aakash on 7/2/2015.
 */

public class NavDrawerItem {
    private boolean showNotify;
    private String title;
    private String icon;


    public NavDrawerItem() {

    }

    public NavDrawerItem(boolean showNotify, String title, String icon) {
        this.showNotify = showNotify;
        this.title = title;
        this.icon = icon;
    }

    public boolean isShowNotify() {
        return showNotify;
    }

    public void setShowNotify(boolean showNotify) {
        this.showNotify = showNotify;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
