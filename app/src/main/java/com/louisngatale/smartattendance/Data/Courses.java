package com.louisngatale.smartattendance.Data;

import java.util.ArrayList;

public class Courses {
    private String id;
    private String course;
    private String image;

    public Courses() {
    }

    public Courses(String id, String course, String image) {
        this.id = id;
        this.course = course;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
