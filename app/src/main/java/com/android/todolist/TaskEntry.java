package com.android.todolist;


import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class TaskEntry {
//alanlar
    private String id;
    private int priority;
    private String description;
    private Date updated;


    TaskEntry(String id, int priority, String description, Date updated) {
        this.id = id;
        this.priority = priority;
        this.description = description;
        this.updated = updated;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getUpdatedAt() {
        return updated;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updated = updated;
    }

//zaman formatı
    @NonNull
    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd/MM/yyyy hh:mm:ss", Locale.getDefault());

        return id + "\t" + description + "\t" + priority + "\t" + dateFormat.format(updated);
    }

}
