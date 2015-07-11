package com.anutanetworks.ncxapp.model;

/**
 * Created by Aakash on 7/11/2015.
 */
public class ApprovalDetailItem {

    private boolean detailData;
    private boolean header;
    private boolean device;
    private boolean commandData;

    private String value;

    public boolean isDetailData() {
        return detailData;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isCommandData() {
        return commandData;
    }

    public void setCommandData(boolean commandData) {
        this.commandData = commandData;
    }

    public boolean isHeader() {
        return header;
    }

    public void setHeader(boolean header) {
        this.header = header;
    }

    public void setDetailData(boolean detailData) {
        this.detailData = detailData;
    }

    public boolean isDevice() {
        return device;
    }

    public void setDevice(boolean device) {
        this.device = device;
    }
}
