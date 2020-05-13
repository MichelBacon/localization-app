package com.cegepba.localization_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import com.cegepba.localization_app.EstimoteBeacon.BeaconManager;
import com.cegepba.localization_app.EstimoteBeacon.EstimoteCredentials;
import com.cegepba.localization_app.Manager.InfoManager;
import com.cegepba.localization_app.Manager.LegendManager;
import com.cegepba.localization_app.Model.Room;
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.Requirement;
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.RequirementsWizardFactory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.List;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;

public class MainActivity extends AppCompatActivity {
    //region private variable
    private Button buttonFloors1, buttonFloors2, buttonFloors3, buttonFloors4, buttonFloors5;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private BeaconManager beaconManager;
    private SearchView searchView;
    Map map;
    private String startNode, destinationNode;
    private ProgressBar progressBar;
    private MenuItem cancel, updatePosition;
    RouteFinder rf;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        map = findViewById(R.id.map);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        auth.signOut();

        rf = new RouteFinder();

        buttonFloors1 = findViewById(R.id.btnFloor1);
        buttonFloors2 = findViewById(R.id.btnFloor2);
        buttonFloors3 = findViewById(R.id.btnFloor3);
        buttonFloors4 = findViewById(R.id.btnFloor4);
        buttonFloors5 = findViewById(R.id.btnFloor5);
        progressBar = findViewById(R.id.progressBar);
        setListener1();
        setListener2();
        setListener3();
        setListener4();
        setListener5();
        RequirementsWizardFactory
                .createEstimoteRequirementsWizard()
                .fulfillRequirements(this,
                        new Function0<Unit>() {
                            @Override
                            public Unit invoke() {
                                Log.d("app", "requirements fulfilled");
                                startProximityContentManager();
                                return null;
                            }
                        },
                        new Function1<List<? extends Requirement>, Unit>() {
                            @Override
                            public Unit invoke(List<? extends Requirement> requirements) {
                                Log.e("app", "requirements missing: " + requirements);
                                return null;
                            }
                        },
                        new Function1<Throwable, Unit>() {
                            @Override
                            public Unit invoke(Throwable throwable) {
                                Log.e("app", "requirements error: " + throwable);
                                return null;
                            }
                        });

        RouteFinder rf = new RouteFinder();

        AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(MainActivity.this, R.style.myDialog));
        alert.setTitle(getResources().getString(R.string.enteryourposition));
        alert.setNeutralButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = alert.create();
        alert11.show();
    }

    private void startProximityContentManager() {
        beaconManager = new BeaconManager(this, ((EstimoteCredentials) getApplication()).cloudCredentials);
        beaconManager.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (beaconManager != null)
            beaconManager.stop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.drawer_menu, menu);
        cancel = menu.findItem(R.id.nav_cancel_traject);
        cancel.setVisible(false);
        updatePosition = menu.findItem(R.id.nav_update_position);
        updatePosition.setVisible(false);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setQueryHint(getResources().getString(R.string.position));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                progressBar.setVisibility(View.VISIBLE);
                if(searchView.getQueryHint() != getResources().getString(R.string.destination)) {
                    searchDataWithPositionOrNot(true, query);
                } else {
                    searchDataWithPositionOrNot(false, query);
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    private void searchDataWithPositionOrNot(boolean isPosition, String query) {
        searchData(query, isPosition, new OnResultCallback() {
            @Override
            public void onSuccess(int listLength) {
                updateSearchView();
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(int numberOfTime) {
                createMessage(R.string.not_found);
                progressBar.setVisibility(View.INVISIBLE);
            }
        }, false);
    }

    private void updateSearchView() {
        if (searchView.getQueryHint() != getResources().getString(R.string.destination)) {
            changeSearchBarForDestination();
        } else {
            exitSearchBar();
        }
    }

    private void exitSearchBar() {
        searchView.clearFocus();
        searchView.setIconified(true);
        searchView.onActionViewCollapsed();
        searchView.setQueryHint(getResources().getString(R.string.position));
    }

    private void changeSearchBarForDestination() {
        searchView.setQuery("", false);
        searchView.setQueryHint(getResources().getString(R.string.destination));
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.enteryourdestination), Toast.LENGTH_LONG).show();
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
            case R.id.nav_cancel_traject:
                map.cancelTraject();
                cancel.setVisible(false);
                createMessage(R.string.msg_trajet_annule);
                return true;
            case R.id.nav_update_position:
                createAlertMessage();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void createAlertMessage() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(this);
        alert.setTitle("Mettre à jour la position");
        alert.setMessage("Veuillez entrer votre position");
        alert.setView(edittext);

        alert.setPositiveButton("Mettre à jour", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                progressBar.setVisibility(View.VISIBLE);
                String localName = edittext.getText().toString();
                if(localName.equals("")){
                    createMessage(R.string.msg_no_position_entered);
                    progressBar.setVisibility(View.INVISIBLE);
                } else {
                    searchData(localName, true, new OnResultCallback() {
                        @Override
                        public void onSuccess(int listLength) {
                            createMessage(R.string.msg_update_position);
                            if(listLength == 1) {
                                createAlertBoxForArrived();
                                cancel.setVisible(false);
                            }
                            progressBar.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onFailure(int numberOfTime) {
                            if(numberOfTime == 1) {
                                createAlertBoxForRedirection();
                            } else if(numberOfTime > 1) {
                                createAlertBoxForAnotherRedirectionToCancel();
                            } else {
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    }, true);
                }
            }
        });

        alert.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {dialog.cancel();}
        });

        alert.show();
    }

    private void createAlertBoxForRedirection() {
        AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(MainActivity.this, R.style.myDialog));
        alert.setTitle(getResources().getString(R.string.msg_not_good_path));
        alert.setMessage("Vous êtes dans la mauvaise direction");
        alert.setNeutralButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = alert.create();
        alert11.show();
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void createAlertBoxForArrived() {
        AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(MainActivity.this, R.style.myDialog));
        alert.setTitle("Attention");
        alert.setMessage("Vous êtes arrivé");
        alert.setNeutralButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = alert.create();
        alert11.show();
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void createAlertBoxForAnotherRedirectionToCancel() {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(MainActivity.this, R.style.myDialog));

        builder.setTitle(getResources().getString(R.string.msg_not_good_path));
        builder.setMessage("Voulez-vous annuler votre trajet?");

        builder.setPositiveButton("OUI", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                map.cancelTraject();
                cancel.setVisible(false);
            }
        });

        builder.setNegativeButton("NON", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void searchData(String query, final Boolean isPosition, final OnResultCallback onResultCallback, final Boolean keepDestination) {
        db.collection("rooms").whereEqualTo("name", query.toLowerCase()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.getResult() != null){
                    if(task.getResult().isEmpty())
                    {
                        onResultCallback.onFailure(0);
                    } else {
                        DocumentSnapshot doc = task.getResult().getDocuments().get(0);
                        Room room = doc.toObject(Room.class);
                        DocumentReference docRefNode = room.getNodeRef();
                        if(docRefNode != null) {
                            docRefNode.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    DocumentSnapshot doc = task.getResult();

                                    if(keepDestination) {
                                        getRoadWhenKeepingDestination(doc, onResultCallback);
                                    } else {
                                        if(isPosition) {
                                            startNode = doc.getId();
                                            onResultCallback.onSuccess(-1);
                                        } else {
                                            getRoadWhenHavingNewDestination(doc, onResultCallback);
                                        }
                                    }
                                }
                            });
                        } else {
                            onResultCallback.onFailure(0);
                        }
                    }
                }
            }
        });
    }

    private void getRoadWhenKeepingDestination(DocumentSnapshot doc, final OnResultCallback onResultCallback) {
        startNode = doc.getId();
        rf.getRoad(startNode, destinationNode, true, new RouteFinder.FirebaseCallback() {
            @Override
            public void onCallback(int[][] list) {
                map.setPositionList(list);
                setButtonVisibile();
                onResultCallback.onSuccess(list.length);
            }

            @Override
            public void onCallback(boolean isNotOnGoodPath, int[][] list, int notOnGoodPathNumber) {
                map.setPositionList(list);
                setButtonVisibile();
                onResultCallback.onFailure(notOnGoodPathNumber);
            }
        });
    }

    private void getRoadWhenHavingNewDestination(DocumentSnapshot doc, final OnResultCallback onResultCallback) {
        destinationNode = doc.getId();
        if(startNode != null && destinationNode != null){
            rf = new RouteFinder();
            rf.getRoad(startNode, destinationNode, false, new RouteFinder.FirebaseCallback() {
                @Override
                public void onCallback(int[][] list) {
                    map.setPositionList(list);
                    setButtonVisibile();
                    onResultCallback.onSuccess(list.length);
                }

                @Override
                public void onCallback(boolean isNotOnGoodPath, int[][] list, int notOnGoodPathNumber) {}
            });
        }
    }

    private void setButtonVisibile() {
        updatePosition.setVisible(true);
        cancel.setVisible(true);
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

    public interface OnResultCallback{
        void onSuccess(int listSize);
        void onFailure(int numberOfTime);
    }

    private void createMessage(int msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    private void createMessage(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
    //endregion chan chan
}
