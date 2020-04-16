package com.cegepba.localization_app.Model;

public class Floors {

    private int floorNum;
    private int drawable;

    public Floors(){}

    public Floors(int floorNum, int drawable) {
        this.floorNum = floorNum;
        this.drawable = drawable;
    }

    public int getFloorNum() {
        return floorNum;
    }

    public void setFloorNum(int floorNum) {
        this.floorNum = floorNum;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }
}
