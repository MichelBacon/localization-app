package com.cegepba.localization_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.OnViewDragListener;
import com.github.chrisbanes.photoview.PhotoView;

import java.lang.reflect.Array;

public class MainActivity extends AppCompatActivity {


    private PhotoView photoView;
    private ImageView location_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        location_user = findViewById(R.id.location_pointer);
        photoView = findViewById(R.id.photo_view);
        photoView.setImageResource(R.drawable.image);

        photoView.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                return false;
            }
        });

        photoView.setOnViewDragListener(new OnViewDragListener() {
            @Override
            public void onDrag(float dx, float dy) {
                Log.e("DRAAAAAGGGG!!!!!", "onDrag: dy= " + photoView.getX());
                location_user.setX(location_user.getX() + dx);
                location_user.setY(location_user.getY() + dy);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.drawer_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_info:
                showActivity(Info.class);
                return false;
            case R.id.nav_legend:
                showActivity(Legend.class);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showActivity(Class className) {
        Intent myIntent = new Intent(MainActivity.this, className);
        startActivity(myIntent);
    }
}
