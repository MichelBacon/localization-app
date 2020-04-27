package com.cegepba.localization_app.Model;

import com.google.firebase.firestore.CollectionReference;

public class Nodes {
    private String name;
    private int xpos;
    private int ypos;

    public Nodes() {
    }

    public Nodes(String name, int xpos, int ypos) {
        this.name = name;
        this.xpos = xpos;
        this.ypos = ypos;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getXpos() {
        return xpos;
    }

    public void setXpos(int xpos) {
        this.xpos = xpos;
    }

    public int getYpos() {
        return ypos;
    }

    public void setYpos(int ypos) {
        this.ypos = ypos;
    }
}
