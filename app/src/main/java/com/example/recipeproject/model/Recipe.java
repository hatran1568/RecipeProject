package com.example.recipeproject.model;

import java.util.ArrayList;
import java.util.Date;

public class Recipe {
    private String description;
    private String duration;
    private int id;
    private String name;
    private String portion;
    private ArrayList<Step> steps;
    private ArrayList<String> ingredients;
    private String thumbnail;
    private String userID;
    private Date date;
    private User user;

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
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "description='" + description + '\'' +
                ", duration='" + duration + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", portion='" + portion + '\'' +
                ", steps=" + steps +
                ", ingredients=" + ingredients +
                ", thumbnail='" + thumbnail + '\'' +
                ", userID=" + userID +
                "date=" + getDate().toString()+
                '}';
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
