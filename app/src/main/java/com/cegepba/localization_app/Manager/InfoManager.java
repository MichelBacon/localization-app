package com.cegepba.localization_app.Manager;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.cegepba.localization_app.R;
import java.util.Objects;

public class InfoManager extends AppCompatActivity {

    TextView textViewDescription;
    int clickcount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        textViewDescription = findViewById(R.id.textView_descriptionInfo);

        textViewDescription.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                clickcount=clickcount+1;
                if(clickcount==6) {
                    showLoginAdminPage();
                }
            }
        });
    }

    private void showLoginAdminPage() {
        Intent myIntent = new Intent(this, LoginAdminManager.class);
        startActivity(myIntent);
    }
}