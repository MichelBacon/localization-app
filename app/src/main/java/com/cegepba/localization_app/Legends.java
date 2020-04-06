package com.cegepba.localization_app;

public class Legends {
    private String Color;
    private String Name;

    public Legends(){};

    public Legends(String color, String name) {
        this.Color = color;
        this.Name = name;
    }

    public String getColor() {
        return Color;
    }

    public void setColor(String color) {
        this.Color = color;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }
}
