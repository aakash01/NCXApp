package com.anutanetworks.ncxapp.model;

import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Aakash on 7/5/2015.
 */
public class Alarm implements Serializable{
    private String id;
    private String message;
    private String description;
    private boolean acknowledged;
    private String alarmState;
    private String alarmSpecType;
    private String severity;
    private String component;
    private String componentId;
    private String componentType;
    private long timeStamp;
    private long lastActiveTimeStamp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isAcknowledged() {
        return acknowledged;
    }

    public void setAcknowledged(boolean acknowledged) {
        this.acknowledged = acknowledged;
    }

    public String getAlarmState() {
        return alarmState;
    }

    public void setAlarmState(String alarmState) {
        this.alarmState = alarmState;
    }

    public String getAlarmSpecType() {
        return alarmSpecType;
    }

    public void setAlarmSpecType(String alarmSpecType) {
        this.alarmSpecType = alarmSpecType;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public long getLastActiveTimeStamp() {
        return lastActiveTimeStamp;
    }

    public void setLastActiveTimeStamp(long lastActiveTimeStamp) {
        this.lastActiveTimeStamp = lastActiveTimeStamp;
    }
}
