package com.example.recipeproject.model;

import java.util.ArrayList;

public class User {
    private int id;
    private String email;
    private String name;
    private String image_link;
    private ArrayList<Integer> favorites;
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage_link() {
        return image_link;
    }

    public void setImage_link(String image_link) {
        this.image_link = image_link;
    }

    public ArrayList<Integer> getFavorites() {
        return favorites;
    }

    public void setFavorites(ArrayList<Integer> favorites) {
        this.favorites = favorites;
    }




}
