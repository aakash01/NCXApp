package com.anutanetworks.ncxapp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Aakash on 7/6/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubUserTask {
    private String id;
    private String description;
    private String commands;
    private String resName, resDisplayName;
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCommands() {
        return commands;
    }

    public void setCommands(String commands) {
        this.commands = commands;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public String getResDisplayName() {
        return resDisplayName;
    }

    public void setResDisplayName(String resDisplayName) {
        this.resDisplayName = resDisplayName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
