package com.cegepba.localization_app.Model;

import com.google.firebase.firestore.DocumentReference;

public class Connection {
    private DocumentReference connectionRef;
    private int distance;

    public Connection() {}

    public Connection(DocumentReference connectionRef, int distance) {
        this.connectionRef = connectionRef;
        this.distance = distance;
    }

    public DocumentReference getConnectionRef() {
        return connectionRef;
    }

    public void setConnectionRef(DocumentReference connectionRef) {
        this.connectionRef = connectionRef;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
