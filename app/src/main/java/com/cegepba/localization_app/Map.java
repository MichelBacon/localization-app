package com.cegepba.localization_app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import androidx.annotation.Nullable;

import com.cegepba.localization_app.Manager.PopManager;
import com.cegepba.localization_app.Manager.RoomsManager;
import com.cegepba.localization_app.Model.Floor;
import com.cegepba.localization_app.Model.Room;

import java.util.ArrayList;

//import com.hazem.coloringforkids.commom.Common;
//import com.hazem.coloringforkids.utils.FloodFill;


public class Map extends View {

    //region private variable
    private Bitmap bitmapMap;
    private Bitmap bitmapUserPosition;
    private float mPositionX,mPositionY;
    private float refX,refY;
    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.0f;
    private final static float mMinZoom = 0.6f;
    private final static float mMaxZoom = 1.5f;
    private float canvasWidth;
    private float canvasHeight;
    private String floorLevel;
    //TODO change init currentFloor with the current user floor
    private int currentFloor = 1;
    private ArrayList<Floor> listFloors;
    private ArrayList<Room> rooms;
    private float clickPositionX;
    private float clickPositionY;
    private int brightness(int pixel) { return (pixel >> 16)& 0xff; }
    Paint paint = new Paint();
    RouteFinder routeFinder = new RouteFinder();

    //private static String[] imageName = {"image1","image2","image3","image4","image5"};

    //endregion

    //region constructor

    public Map(Context context) {
        super(context);
    }
    public Map(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        bitmapMap = BitmapFactory.decodeResource(getResources(), R.drawable.floors1);
        bitmapUserPosition = BitmapFactory.decodeResource(getResources(), R.drawable.location);
        RoomsManager roomsManager = new RoomsManager();
        roomsManager.setRoomsArray();
        rooms = roomsManager.getRooms();
        listFloors = new ArrayList<>();

        initFloorList();
        floorLevel = "Étage : " + listFloors.get(0).getFloorNum();
    }

    //endregion

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener{
        @Override
        public boolean onScale(ScaleGestureDetector detector){

            final float scale = detector.getScaleFactor();
            mScaleFactor = Math.max(mMinZoom, Math.min(mScaleFactor * scale, mMaxZoom));

                // 1 Grabbing
                final float centerX = detector.getFocusX();
                final float centerY = detector.getFocusY();
                // 2 Calculating difference
                float diffX = centerX - mPositionX;
                float diffY = centerY - mPositionY;
                // 3 Scaling difference
                diffX = diffX * scale - diffX;
                diffY = diffY * scale - diffY;
                // 4 Updating image origin
                if (mScaleFactor < mMaxZoom && mScaleFactor > mMinZoom) {
                    if (mPositionX > -canvasWidth && mPositionX < 0) {
                        mPositionX -= diffX;
                    }
                    if (mPositionY > -canvasHeight && mPositionY < 0) {
                        mPositionY -= diffY;
                    }
                }
                invalidate();
            return true;
        }
    }

    //region Draw Items
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvasWidth = 3050 * mScaleFactor;
        canvasHeight = 2700 * mScaleFactor;
        drawBitmap(canvas);
        if(currentFloor == 5) {
            drawUserPositionBitmap(canvas);
        }
        drawText(canvas);
    }

    private void drawBitmap(Canvas canvas) {
        canvas.save();
        canvas.translate(mPositionX,mPositionY);
        canvas.scale(mScaleFactor, mScaleFactor);
        canvas.drawBitmap(bitmapMap, 0, 0, null);
        drawTraject(canvas, 1280,2472,1280,3200);
        canvas.restore();
    }
    private void drawTraject(Canvas canvas, int Xstart, int Ystart, int Xend, int Yend){
        Paint paint = new Paint();

        if(currentFloor == 5) {
            paint.setColor(Color.BLUE);
        } else {
            paint.setColor(Color.GRAY);
        }

        //routeFinder.getPositionForRoad(routeFinder.getRoad());

        paint.setStrokeWidth(35);
        canvas.drawLine(Xstart, Ystart, Xend, Yend, paint);
        //canvas.drawLine(Xend, 3200, 2800, 3200, paint); // test
    }

    private void drawText(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.GRAY);
        paint.setTextSize(100);
        canvas.drawText(floorLevel, 100,100, paint);
        canvas.drawText(Float.toString(mPositionX), 100,200, paint);
        canvas.drawText(Float.toString(mPositionY), 100,300, paint);
        canvas.drawText(Float.toString(mScaleFactor), 100,400, paint);
    }

    private void drawUserPositionBitmap(Canvas canvas) {
        canvas.save();
        canvas.translate(mPositionX, mPositionY);
        canvas.scale(mScaleFactor/3, mScaleFactor/3);
        //TODO position bitmap = position user
        canvas.drawBitmap(bitmapUserPosition, 3200, 6900, null);
        canvas.restore();
    }

    //endregion

    final GestureDetector gestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
        public void onLongPress(MotionEvent e) {
            for (Room room : rooms) {
                if(clickPositionIsInAClass(clickPositionX, clickPositionY, room) && room.getFloor() == currentFloor) {
                    Intent myIntent = new Intent(getContext(), PopManager.class);
                    myIntent.putExtra("room", room);
                    getContext().startActivity(myIntent);
                }
            }
        }
    });

    @Override
    public boolean onTouchEvent(MotionEvent event){
        gestureDetector.onTouchEvent(event);
        mScaleDetector.onTouchEvent(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                refX = event.getX();
                refY = event.getY();

                float tempX = mPositionX / mScaleFactor;
                float tempY = mPositionY / mScaleFactor;

                clickPositionX = tempX - refX;
                clickPositionY = tempY - refY;

                //paint((int)((refX - mPositionX)/mScaleFactor),(int)((refY - mPositionY/mScaleFactor));
                break;
            case MotionEvent.ACTION_MOVE:
                if (!mScaleDetector.isInProgress()){
                    float nX = event.getX();
                    float nY = event.getY();

                    float deltaX = nX - refX;
                    float deltaY = nY - refY;

                    mPositionX = movement(mPositionX, deltaX, canvasWidth);
                    mPositionY = movement(mPositionY, deltaY, canvasHeight);

                    refX = nX;
                    refY = nY;

                    invalidate();
                }
                break;
        }
        return true;
    }
    private float movement(float position, float delta, float canvas){
        if (position + delta >= 0 && delta > 0){
            position = 0;
        }else if (position + delta < -canvas){
            position = -canvas;
        }else{
            position += delta;
        }
        return position;
    }

    private boolean clickPositionIsInAClass(float clickPositionX, float clickPositionY, Room room) {
        return clickPositionXIsBetweenFirebasePosition(clickPositionX, room) && clickPositionYIsBetweenFirebasePosition(clickPositionY, room);
    }

    private boolean clickPositionXIsBetweenFirebasePosition(float clickPositionX, Room room) {
        return (clickPositionX <= room.getPositionYTLeft()) && (clickPositionX >= room.getPositionXTRight());
    }

    private boolean clickPositionYIsBetweenFirebasePosition(float clickPositionY, Room room) {
        return (clickPositionY <= room.getPositionYTLeft()) && (clickPositionY >= room.getPositionYBLeft());
    }

    private void initFloorList() {
        for (int i=0; i<5; i++){
            int j = i + 1;
            Floor floors = new Floor();
            floors.setDrawable(getResources().getIdentifier("floors"+j,"drawable", getContext().getPackageName()));
            floors.setFloorNum(j);
            listFloors.add(floors);
        }
    }

    public void changeFloor(int floorNumber){
        addElementFromFloor(floorNumber-1);
        invalidate();
    }

    private void addElementFromFloor(int floorNumber) {
        floorLevel = "Étage : " + listFloors.get(floorNumber).getFloorNum();
        currentFloor = listFloors.get(floorNumber).getFloorNum();
        bitmapMap = BitmapFactory.decodeResource(getResources(), listFloors.get(floorNumber).getDrawable());
    }
}