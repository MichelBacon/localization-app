package com.cegepba.localization_app;

import com.google.firebase.firestore.DocumentReference;

public class Rooms {
    private String name;
    private DocumentReference legendId;
    //private Floors floor;
    private String description;
    private String beaconId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DocumentReference getLegendId() {
        return legendId;
    }

    public void setLegendId(DocumentReference legendId) {
        this.legendId = legendId;
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

    public void setBeaconId(String beaconId) {
        this.beaconId = beaconId;
    }

    public Rooms(){};

    public Rooms(String name, DocumentReference legendId, /*Floors floor,*/ String description, String beaconId) {
        this.name = name;
        this.legendId = legendId;
        //this.floor = floor;
        this.description = description;
        this.beaconId = beaconId;
    }
}
