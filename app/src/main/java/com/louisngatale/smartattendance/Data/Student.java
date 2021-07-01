package com.louisngatale.smartattendance.Data;

public class Student {
    String id;
    Integer percentage;

    public Student() {
    }

    public Student(String id, Integer percentage) {
        this.id = id;
        this.percentage = percentage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getPercentage() {
        return percentage;
    }

    public void setPercentage(Integer percentage) {
        this.percentage = percentage;
    }
}
