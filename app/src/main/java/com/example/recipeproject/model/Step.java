package com.example.recipeproject.model;

public class Step {
    private String image;
    private String text;

    public Step() {
    }

    public Step(String image, String text) {
        this.image = image;
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Step{" +
                "image='" + image + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
