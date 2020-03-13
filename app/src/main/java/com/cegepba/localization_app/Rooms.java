package com.cegepba.localization_app;

public class Rooms {
    private String name;
    private Legends legendId;
    private Floor floor;
    private String description;
    private String beaconId;
    private Byte augmentedReality;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Legends getLegendId() {
        return legendId;
    }

    public void setLegendId(Legends legendId) {
        this.legendId = legendId;
    }

    public Floor getFloor() {
        return floor;
    }

    public void setFloor(Floor floor) {
        this.floor = floor;
    }

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

    public Byte getAugmentedReality() {
        return augmentedReality;
    }

    public void setAugmentedReality(Byte augmentedReality) {
        this.augmentedReality = augmentedReality;
    }

    public Rooms(String name, Legends legendId, Floor floor, String description, String beaconId, Byte augmentedReality) {
        this.name = name;
        this.legendId = legendId;
        this.floor = floor;
        this.description = description;
        this.beaconId = beaconId;
        this.augmentedReality = augmentedReality;
    }
}
