package com.cegepba.localization_app.Manager;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.cegepba.localization_app.MainActivity;
import com.cegepba.localization_app.Model.Rooms;
import com.cegepba.localization_app.R;
import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.service.BeaconManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.UUID;


//TODO https://developer.estimote.com/android/tutorial/part-2-background-monitoring/

public class BeaconMonitoring extends Application {

    private BeaconManager beaconManager;
    private FirebaseFirestore db;

    @Override
    public void onCreate() {
        super.onCreate();

        beaconManager = new BeaconManager(getApplicationContext());
        db = FirebaseFirestore.getInstance();

        //TODO change scanperiod for the battery draining
        beaconManager.setBackgroundScanPeriod(1000, 0);

        beaconManager.setMonitoringListener(new BeaconManager.BeaconMonitoringListener() {
            @Override
            public void onEnteredRegion(BeaconRegion region, List<Beacon> beacons) {
                db.collection("Rooms").whereEqualTo("beaconId", beacons.get(0).getProximityUUID().toString().toUpperCase()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.getResult() != null){
                            for(DocumentSnapshot doc : task.getResult()) {
                                Rooms room = doc.toObject(Rooms.class);
                                Toast.makeText(getApplicationContext(),room.getDescription(),Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
            }
            @Override
            public void onExitedRegion(BeaconRegion region) {
                Log.e("EXIT", "EXCDOIIIIITT");
            }
        });

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startMonitoring(new BeaconRegion(
                        "monitored region",
                        null,
                        null, null));
            }
        });
    }
}
