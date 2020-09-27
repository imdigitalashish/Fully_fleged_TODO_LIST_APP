package com.imdigitalashish.vacoder.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "task_table")
public class Task {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;

    private boolean dueDate;
    private boolean done_or_note;

    private int date;
    private int month;
    private int year;

    private int hour;
    private int minute;

    public Task(String title, boolean dueDate, boolean done_or_note, int date, int month, int year, int hour, int minute) {
        this.title = title;
        this.dueDate = dueDate;
        this.done_or_note = done_or_note;
        this.date = date;
        this.month = month;
        this.year = year;
        this.hour = hour;
        this.minute = minute;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isDueDate() {
        return dueDate;
    }

    public void setDueDate(boolean dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isDone_or_note() {
        return done_or_note;
    }

    public void setDone_or_note(boolean done_or_note) {
        this.done_or_note = done_or_note;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }
}
