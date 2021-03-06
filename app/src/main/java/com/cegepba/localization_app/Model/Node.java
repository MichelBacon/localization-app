package com.cegepba.localization_app.Model;

import java.util.ArrayList;

public class Node {
    private String beaconId;
    private ArrayList<Connection> connections;
    private String name;
    private int xpos;
    private int ypos;
    private int floorNum;
    public Node() {
        connections = new ArrayList<>();
    }

    public Node(String name, int xpos, int ypos, int floorNum, String beaconId) {
        connections = new ArrayList<>();
        this.name = name;
        this.xpos = xpos;
        this.ypos = ypos;
        this.floorNum = floorNum;
        this.beaconId = beaconId;
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
    public int getFloorNum() {
        return floorNum;
    }
    public void setFloor(int inValue){
        this.floorNum = inValue;
    }
    public void setYpos(int ypos) {
        this.ypos = ypos;
    }

    public void addConnection(Connection connection) {
        connections.add(connection);
    }

    public String toString() {
        String nodeString;
        nodeString = this.name;
        for (Connection connection: connections) {
            nodeString += "\n\t" + connection.getConnectionRef().getId() + " : " + connection.getDistance();
        }
        return nodeString;
    }

    public ArrayList<Connection> getConnections() {
        return connections;
    }

    public String getBeaconId() {
        return beaconId;
    }
}
