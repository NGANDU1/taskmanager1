package com.example.taskmanger;

public class Task {
    private int id;
    private String title;
    private String description;
    private String date;
    private String time;
    private long reminderTime;  // Add reminderTime as a long to store the time in milliseconds

    // Empty constructor
    public Task() {
    }

    // Constructor with fields (including reminderTime)
    public Task(int id, String title, String description, String date, String time, long reminderTime) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.reminderTime = reminderTime;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public long getReminderTime() {
        return reminderTime;  // Get the reminder time
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setReminderTime(long reminderTime) {
        this.reminderTime = reminderTime;  // Set the reminder time
    }
}
