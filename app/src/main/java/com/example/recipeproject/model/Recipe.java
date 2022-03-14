package com.example.recipeproject.model;

import java.util.ArrayList;
import java.util.Date;

public class Recipe {
    public String getStrdate() {
        return strdate;
    }

    public void setStrdate(String strdate) {
        this.strdate = strdate;
    }

    private  String strdate;
    private String description;
    private String duration;
    private int id;
    private String name;
    private String portion;
    private ArrayList<Step> steps;
    private ArrayList<String> ingredients;
    private String thumbnail;
    private String userId;
    private Date date;
    private User user;
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Recipe() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPortion() {
        return portion;
    }

    public void setPortion(String portion) {
        this.portion = portion;
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<Step> steps) {
        this.steps = steps;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getUserID() {
        return userId;
    }

    public void setUserID(String userID) {
        this.userId = userID;
    }


    public void cloneRecipe(Recipe r){
        setId(r.getId());
        setDescription(r.getDescription());
        setDate(r.getDate());
        setDuration(r.getDuration());
        setName(r.getName());
        setIngredients(r.getIngredients());
        setPortion(r.getPortion());
        setSteps(r.getSteps());
        setThumbnail(r.getThumbnail());
        setUserID(r.getUserID());
    }
}
