package com.example.c3;

import java.util.ArrayList;
import java.util.List;

public class Article {

    //public static List<Article> articleArrayList = new ArrayList<>();
    private int id;
    private String title;
    private String description;

    public Article(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
