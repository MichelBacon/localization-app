package com.cegepba.localization_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.cegepba.localization_app.Manager.InfoManager;
import com.cegepba.localization_app.Manager.LegendManager;
import com.github.chrisbanes.photoview.PhotoView;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            PhotoView photoView = findViewById(R.id.photo_view);
            photoView.setImageResource(R.drawable.image);
        }
        catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(),Toast.LENGTH_LONG).show();
        }
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
                showActivity(InfoManager.class);
                return false;
            case R.id.nav_legend:
                showActivity(LegendManager.class);
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
