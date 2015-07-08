package com.anutanetworks.ncxapp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aakash on 7/5/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Approval {
    private String id;
    private String processName;
    private String taskName;
    private long date;
    private String originator;
    private String approved;
    private String description;
    private String resName;
    private List<TaskApprover> taskApprovers;
    private List<SubUserTask> subTasks = new ArrayList<SubUserTask>();


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProcessName() {
        return processName;
    }

    public List<TaskApprover> getTaskApprovers() {
        return taskApprovers;
    }

    public void setTaskApprovers(List<TaskApprover> taskApprovers) {
        this.taskApprovers = taskApprovers;
    }
    public List<SubUserTask> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(List<SubUserTask> subTasks) {
        this.subTasks = subTasks;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getOriginator() {
        return originator;
    }

    public void setOriginator(String originator) {
        this.originator = originator;
    }

    public String getApproved() {
        return approved;
    }

    public void setApproved(String approved) {
        this.approved = approved;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }
}
