package com.cegepba.localization_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import com.cegepba.localization_app.Manager.InfoManager;
import com.cegepba.localization_app.Manager.LegendManager;
import com.cegepba.localization_app.Model.Rooms;
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.Requirement;
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.RequirementsWizardFactory;
import com.estimote.proximity_sdk.api.EstimoteCloudCredentials;
import com.estimote.proximity_sdk.api.ProximityObserver;
import com.estimote.proximity_sdk.api.ProximityObserverBuilder;
import com.estimote.proximity_sdk.api.ProximityZone;
import com.estimote.proximity_sdk.api.ProximityZoneBuilder;
import com.estimote.proximity_sdk.api.ProximityZoneContext;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;

public class MainActivity extends AppCompatActivity {
    //region private variable
    private Button buttonFloors1;
    private Button buttonFloors2;
    private Button buttonFloors3;
    private Button buttonFloors4;
    private Button buttonFloors5;
    private FirebaseFirestore db;
    private ProximityObserver proximityObserver;
    ProximityObserver.Handler observationHandler;
    Map map;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        map = findViewById(R.id.map);
        db = FirebaseFirestore.getInstance();

        buttonFloors1 = findViewById(R.id.btnFloor1);
        buttonFloors2 = findViewById(R.id.btnFloor2);
        buttonFloors3 = findViewById(R.id.btnFloor3);
        buttonFloors4 = findViewById(R.id.btnFloor4);
        buttonFloors5 = findViewById(R.id.btnFloor5);
        setListener1();
        setListener2();
        setListener3();
        setListener4();
        setListener5();

        EstimoteCloudCredentials cloudCredentials =
                new EstimoteCloudCredentials("localization-app-o5p", "a38e9ecd8297e4ee81e372564fe23434");

        this.proximityObserver =
                new ProximityObserverBuilder(getApplicationContext(), cloudCredentials)
                        .onError(new Function1<Throwable, Unit>() {
                            @Override
                            public Unit invoke(Throwable throwable) {
                                Log.e("app", "proximity observer error: " + throwable);
                                return null;
                            }
                        })
                        .withLowLatencyPowerMode()
                        .build();

        ProximityZone zone = new ProximityZoneBuilder()
                .forTag("desks")
                .inFarRange()
                .onEnter(new Function1<ProximityZoneContext, Unit>() {
                    @Override
                    public Unit invoke(ProximityZoneContext context) {
                        String deskOwner = context.getAttachments().get("desk-owner");
                        Log.d("app", "Welcome to " + deskOwner + "'s desk");
                        return null;
                    }
                })
                .onExit(new Function1<ProximityZoneContext, Unit>() {
                    @Override
                    public Unit invoke(ProximityZoneContext context) {
                        Log.d("app", "Bye bye, come again!");
                        return null;
                    }
                })
                .onContextChange(new Function1<Set<? extends ProximityZoneContext>, Unit>() {
                    @Override
                    public Unit invoke(Set<? extends ProximityZoneContext> contexts) {
                        List<String> deskOwners = new ArrayList<>();
                        for (ProximityZoneContext context : contexts) {
                            deskOwners.add(context.getAttachments().get("desk-owner"));
                        }
                        Log.d("app", "In range of desks: " + deskOwners);
                        return null;
                    }
                })
                .build();

        /*RequirementsWizardFactory
                .createEstimoteRequirementsWizard()
                .fulfillRequirements(this,
                        // onRequirementsFulfilled
                        new Function0<Unit>() {
                            @Override public Unit invoke() {
                                Log.d("app", "requirements fulfilled");
                                proximityObserver.startObserving(zone);
                                return null;
                            }
                        },
                        // onRequirementsMissing
                        new Function1<List<? extends Requirement>, Unit>() {
                            @Override public Unit invoke(List<? extends Requirement> requirements) {
                                Log.e("app", "requirements missing: " + requirements);
                                return null;
                            }
                        },
                        // onError
                        new Function1<Throwable, Unit>() {
                            @Override public Unit invoke(Throwable throwable) {
                                Log.e("app", "requirements error: " + throwable);
                                return null;
                            }
                        });*/

        observationHandler =
                proximityObserver
                        .startObserving(zone);
    }

    @Override
    protected void onDestroy() {
        observationHandler.stop();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.drawer_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchData(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_info:
                showActivity(InfoManager.class);
                return false;
            case R.id.nav_legend:
                showActivity(LegendManager.class);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void searchData(String query) {
        db.collection("Rooms").whereEqualTo("name", query).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.getResult() != null){
                    if(task.getResult().isEmpty())
                    {
                        createMessage(R.string.not_found);
                    }

                    for(DocumentSnapshot doc : task.getResult()) {
                        Rooms room = doc.toObject(Rooms.class);
                        Toast.makeText(getApplicationContext(),room.getDescription(),Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private void showActivity(Class className) {
        Intent myIntent = new Intent(MainActivity.this, className);
        startActivity(myIntent);
    }
    //region change floor

    private void setListener1(){
        buttonFloors1.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                map.changeFloor(1);
                createMessage(R.string.Floor1st);
            }
        });
    }
    private void setListener2(){
        buttonFloors2.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {

                map.changeFloor(2);

                createMessage(R.string.Floor2nd);
            }
        });
    }
    private void setListener3(){
        buttonFloors3.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                map.changeFloor(3);
                createMessage(R.string.Floor3rd);
            }
        });
    }
    private void setListener4(){
        buttonFloors4.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                map.changeFloor(4);
                createMessage(R.string.Floor4th);
            }
        });
    }
    private void setListener5(){
        buttonFloors5.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                map.changeFloor(5);
                createMessage(R.string.Floor5th);
            }
        });
    }


    private void createMessage(int msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    //endregion chan chan
}
