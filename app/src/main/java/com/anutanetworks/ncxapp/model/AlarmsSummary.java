package com.anutanetworks.ncxapp.model;

/**
 * Created by Aakash on 7/8/2015.
 */
public class AlarmsSummary {
    private int critical;
    private int major;
    private int minor;
    private int info;
    private int warning;

    public int getCritical() {
        return critical;
    }

    public void setCritical(int critical) {
        this.critical = critical;
    }

    public int getMajor() {
        return major;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public int getMinor() {
        return minor;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    public int getInfo() {
        return info;
    }

    public void setInfo(int info) {
        this.info = info;
    }

    public int getWarning() {
        return warning;
    }

    public void setWarning(int warning) {
        this.warning = warning;
    }
}
