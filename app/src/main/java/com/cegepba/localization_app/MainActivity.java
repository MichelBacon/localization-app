package com.cegepba.localization_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.util.Log;
import android.view.DragEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.github.chrisbanes.photoview.OnViewDragListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;

public class MainActivity extends AppCompatActivity {

    private Button buttonFloors1;
    private Button buttonFloors2;
    private Button buttonFloors3;
    private Button buttonFloors4;
    private Button buttonFloors5;
    private float XmapPosition;
    private float YmapPosition;
    private float CurrentScale;
    PhotoView photoView;
//TEMPO
    private TextView CorX;
    private TextView CorY;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        photoView = findViewById(R.id.photo_view);
        photoView.setImageResource(R.drawable.image1);
        photoView.setMaximumScale(10f);
        setFloorScale(5f);

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
        CorX = findViewById(R.id.textView);
        CorY = findViewById(R.id.textView2);


        photoView.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {

                return false;
            }
        });
        photoView.setOnViewDragListener(new OnViewDragListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDrag(float dx, float dy) {
                Log.e("DRAAAAAGGGG!!!!!", "onDrag: dy= " + dy);
                //XmapPosition = dx;
                //YmapPosition = dy;

                int[] xY = {10,10};
                photoView.getLocationOnScreen(xY);
                XmapPosition = xY[0];
                YmapPosition = xY[1];

                //YmapPosition = photoView.getX();
                CorX.setText(Float.toString(XmapPosition));
                CorY.setText(Float.toString(YmapPosition));
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
//TODO set res string for 1ère étage
    //region change floor
    private void changeFloor(int floorLevel){
        CurrentScale = (float) photoView.getScale();
        switch (floorLevel){
            case 1 :
                photoView.setImageResource(R.drawable.image1);
                createMessage(R.string.Floor1st);
                setMapPosition();
                setFloorScale(CurrentScale);
                break;
            case 2 :
                photoView.setImageResource(R.drawable.image2);
                createMessage(R.string.Floor2nd);
                setMapPosition();
                setFloorScale(CurrentScale);
                break;

            case 3 :
                photoView.setImageResource(R.drawable.image3);
                createMessage(R.string.Floor3rd);
                setMapPosition();
                setFloorScale(CurrentScale);
                break;

            case 4 :
                photoView.setImageResource(R.drawable.image4);
                createMessage(R.string.Floor4th);
                setMapPosition();
                setFloorScale(CurrentScale);
                break;
            case 5 :
                photoView.setImageResource(R.drawable.image5);
                createMessage(R.string.Floor5th);
                setMapPosition();
                setFloorScale(CurrentScale);
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
        setMapPosition();
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
    private void setFloorScale(final float CurrentScale){
        photoView.post(new Runnable() {
            @Override
            public void run() {
                photoView.setScale(CurrentScale, XmapPosition, YmapPosition, false);
            }
        });
    }
    private void setMapPosition(){
    }
}
