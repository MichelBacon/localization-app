package com.cegepba.localization_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.github.chrisbanes.photoview.PhotoView;

public class MainActivity extends AppCompatActivity {

    private Button buttonFloors1;
    private Button buttonFloors2;
    private Button buttonFloors3;
    private Button buttonFloors4;
    private Button buttonFloors5;

    PhotoView photoView;

    //TEMPO
    EditText coox;
    EditText cooy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      
        photoView = findViewById(R.id.photo_view);
        photoView.setImageResource(R.drawable.image1);
        photoView.setMaximumScale(10f);
        setFloorScale();

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

        //TEMPO
         coox = findViewById(R.id.coox);
         cooy = findViewById(R.id.cooy);
         photoView.setOnClickListener(new PhotoView.OnClickListener() {
             @Override
             public void onClick(View v) {
                 int x = photoView.getLeft();
                 int y = photoView.getTop();

                 coox.setText(Integer.toString(x));
                 cooy.setText(Integer.toString(y));

             }
         });
         //end tempo
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

    //region change floor
    private void changeFloor(int floorLevel){
        switch (floorLevel){
            case 1 :
                setFloorScale();
                photoView.setImageResource(R.drawable.image1);
                createMessage(R.string.Floor1st);
                break;
            case 2 :
                setFloorScale();
                photoView.setImageResource(R.drawable.image2);
                createMessage(R.string.Floor2nd);
                break;

            case 3 :
                setFloorScale();
                photoView.setImageResource(R.drawable.image3);
                createMessage(R.string.Floor3rd);
                break;

            case 4 :
                photoView.setImageResource(R.drawable.image4);
                createMessage(R.string.Floor4th);
                setFloorScale();
                break;
            case 5 :
                photoView.setImageResource(R.drawable.image5);
                createMessage(R.string.Floor5th);
                setFloorScale();
                break;
            default:

        }
    }

    private void setListener1(){

        buttonFloors1.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                changeFloor(1);
            }
        });
    }
    private void setListener2(){
        buttonFloors2.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                changeFloor(2);
            }
        });
    }
    private void setListener3(){
        buttonFloors3.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                changeFloor(3);
            }
        });
    }
    private void setListener4(){
        buttonFloors4.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                changeFloor(4);
            }
        });
    }
    private void setListener5(){
        buttonFloors5.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                changeFloor(5);
            }
        });
    }
    //endregion chan chan

    private void createMessage(int msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    private void setFloorScale(){
        photoView.post(new Runnable() {
            @Override
            public void run() {
                photoView.setScale(5f,false);
            }
        });
    }
}
