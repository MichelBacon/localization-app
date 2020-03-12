package com.cegepba.localization_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import com.github.chrisbanes.photoview.PhotoView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(MainActivity.this, "Firebase connection Sucess", Toast.LENGTH_LONG).show();

        PhotoView photoView = findViewById(R.id.photo_view);
        photoView.setImageResource(R.drawable.image);
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
                showWindow("info");
                return false;
            case R.id.nav_legend:
                showWindow("legend");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showWindow(String windowName) {
        Intent myIntent = new Intent(MainActivity.this, Info.class);
        startActivity(myIntent);
    }
}
