package com.cegepba.localization_app.Model;

public class Legend {
    private String Color;
    private String Name;

    public Legend(){}

    public Legend(String color, String name) {
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
