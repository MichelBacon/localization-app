package com.cegepba.localization_app.Model;

import com.google.firebase.firestore.DocumentReference;

public class Rooms {
    private String name;
    //private Floors floor;
    private String description;
    private String beaconId;
    private float positionXBLeft;
    private float positionXBRight;
    private float positionXTLeft;
    private float positionXTRight;
    private float positionYTLeft;
    private float positionYTRight;
    private float positionYBLeft;
    private float positionYBRight;

    public Rooms(){}

    public Rooms(String name, String description, String beaconId, float positionXBLeft,
                 float positionXBRight, float positionXTLeft, float positionXTRight, float positionYTLeft,
                 float positionYTRight, float positionYBLeft, float positionYBRight) {
        this.name = name;
        this.description = description;
        this.beaconId = beaconId;
        this.positionXBLeft = positionXBLeft;
        this.positionXBRight = positionXBRight;
        this.positionXTLeft = positionXTLeft;
        this.positionXTRight = positionXTRight;
        this.positionYTLeft = positionYTLeft;
        this.positionYTRight = positionYTRight;
        this.positionYBLeft = positionYBLeft;
        this.positionYBRight = positionYBRight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /*public Floors getFloor() {
        return floor;
    }
    public void setFloor(Floors floor) {
        this.floor = floor;
    }*/

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBeaconId() {
        return beaconId;
    }

    public float getPositionXBLeft() {
        return positionXBLeft;
    }

    public float getPositionXBRight() {
        return positionXBRight;
    }

    public float getPositionXTLeft() {
        return positionXTLeft;
    }

    public float getPositionXTRight() {
        return positionXTRight;
    }


    public float getPositionYTLeft() {
        return positionYTLeft;
    }

    public float getPositionYTRight() {
        return positionYTRight;
    }

    public float getPositionYBLeft() {
        return positionYBLeft;
    }

    public float getPositionYBRight() {
        return positionYBRight;
    }
}