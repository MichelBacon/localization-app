package com.cegepba.localization_app.Model;

public class Floor {

    private int floorNum;
    private int drawable;

    public Floor(){}

    public Floor(int floorNum, int drawable) {
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
