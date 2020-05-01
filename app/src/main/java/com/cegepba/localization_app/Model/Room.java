package com.cegepba.localization_app.Model;

import com.google.firebase.firestore.DocumentReference;

import java.io.Serializable;

public class Room implements Serializable {
    private DocumentReference nodeRef;
    private String name;
    private int floor;
    private String description;
    private int positionXBLeft;
    private int positionXBRight;
    private int positionXTLeft;
    private int positionXTRight;
    private int positionYTLeft;
    private int positionYTRight;
    private int positionYBLeft;
    private int positionYBRight;

    public Room(){}

    public Room(DocumentReference nodeRef, String name, int floor, String description, int positionXBLeft,
                int positionXBRight, int positionXTLeft, int positionXTRight, int positionYTLeft,
                int positionYTRight, int positionYBLeft, int positionYBRight) {
        this.nodeRef = nodeRef;
        this.name = name;
        this.description = description;
        this.positionXBLeft = positionXBLeft;
        this.positionXBRight = positionXBRight;
        this.positionXTLeft = positionXTLeft;
        this.positionXTRight = positionXTRight;
        this.positionYTLeft = positionYTLeft;
        this.positionYTRight = positionYTRight;
        this.positionYBLeft = positionYBLeft;
        this.positionYBRight = positionYBRight;
        this.floor = floor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFloor() {
        return floor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public DocumentReference getNodeRef() {
        return nodeRef;
    }

    public void setNodeRef(DocumentReference nodeRef) {
        this.nodeRef = nodeRef;
    }
}