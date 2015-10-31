package model;


import java.io.Serializable;
import java.util.Date;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

public class Task {
    private String name;
    private String description;
    private Date date;
    private String contacts;

    public Task(String name, String description, Date date, String contacts) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.contacts = contacts;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Date getDate() {
        return date;
    }

    public String getContacts() {
        return contacts;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
