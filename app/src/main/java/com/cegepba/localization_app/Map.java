package com.cegepba.localization_app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
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

public class Map extends View {

    //region private variable
    private Bitmap bitmapMap;
    private Bitmap bitmapUserPosition;
    private Bitmap destinationPosition;
    private float mPositionX,mPositionY;
    private float refX,refY;
    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.0f;
    private final static float mMinZoom = 0.6f;
    private final static float mMaxZoom = 1.5f;
    private float canvasWidth;
    private float canvasHeight;
    private String floorLevel;
    private int currentFloor = 1;
    private int ActiveFloor;
    private ArrayList<Floor> listFloors;
    private ArrayList<Room> rooms;
    private float clickPositionX;
    private float clickPositionY;
    int[][] nodesToDraw;

    //endregion

    //region constructor

    public Map(Context context) {
        super(context);
    }

    public Map(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        bitmapMap = BitmapFactory.decodeResource(getResources(), R.drawable.floors1);
        bitmapUserPosition = BitmapFactory.decodeResource(getResources(), R.drawable.you);
        destinationPosition = BitmapFactory.decodeResource(getResources(), R.drawable.splash_logo);
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

                final float centerX = detector.getFocusX();
                final float centerY = detector.getFocusY();

                float diffX = centerX - mPositionX;
                float diffY = centerY - mPositionY;

                diffX = diffX * scale - diffX;
                diffY = diffY * scale - diffY;

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
        canvas.save();
        canvas.translate(mPositionX,mPositionY);
        canvas.scale(mScaleFactor, mScaleFactor);
        canvasWidth = 3050 * mScaleFactor;
        canvasHeight = 2700 * mScaleFactor;
        drawObject(canvas);
        canvas.restore();
        drawText(canvas);
        invalidate();
    }

    private void drawObject(Canvas canvas) {
        drawBitmap(canvas);
        drawTraject(canvas);
        if(currentFloor == ActiveFloor) {
            drawUserPositionBitmap(canvas);
        }
    }

    private void drawBitmap(Canvas canvas) {
        canvas.drawBitmap(bitmapMap, 0, 0, null);
    }

    private void drawTraject(Canvas canvas){
        int yPos;
        int finalX = 0;
        int buffer = 100;
        if(nodesToDraw != null) {
            for(int xPos = 0; xPos<nodesToDraw.length; xPos++) {
                if(xPos+1 != nodesToDraw.length) {
                    finalX = xPos+1;
                    yPos =0;
                    Paint paint = new Paint();
                    paint.setStrokeWidth(35);
                    paint.setMaskFilter(new BlurMaskFilter(10, BlurMaskFilter.Blur.NORMAL));
                    if(ActiveFloor == nodesToDraw[xPos][2]){
                        paint.setColor(Color.BLUE);
                    }else{
                        paint.setColor(Color.GRAY);
                    }

                    canvas.drawLine(nodesToDraw[xPos][yPos], nodesToDraw[xPos][yPos+1], nodesToDraw[xPos+1][yPos], nodesToDraw[xPos+1][yPos+1], paint);
                }
            }
            if(currentFloor == ActiveFloor)
                canvas.drawBitmap(destinationPosition, null, new RectF(nodesToDraw[finalX][0]-buffer, nodesToDraw[finalX][1]-buffer, nodesToDraw[finalX][0]+buffer, nodesToDraw[finalX][1]+buffer), null);
        }
    }

    public void setPositionList(int[][] positions) {
        nodesToDraw = positions;
    }

    private void drawText(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.blue));
        paint.setTextSize(100);
        canvas.drawText(floorLevel, 100,100, paint);
        //canvas.drawText(Float.toString(mPositionX), 100,200, paint);
        //canvas.drawText(Float.toString(mPositionY), 100,300, paint);
        //canvas.drawText(Float.toString(mScaleFactor), 100,400, paint);
    }

    private void drawUserPositionBitmap(Canvas canvas) {
        int buffer = 100;
        if(nodesToDraw != null) {
            canvas.drawBitmap(bitmapUserPosition, null, new RectF(nodesToDraw[0][0]-buffer, nodesToDraw[0][1]-buffer, nodesToDraw[0][0]+buffer, nodesToDraw[0][1]+buffer), null);
        }
    }
    //endregion

    final GestureDetector gestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
        public void onLongPress(MotionEvent e) {
            for (Room room : rooms) {
                if(clickPositionIsInAClass(clickPositionX, clickPositionY, room) && room.getFloor() == currentFloor) {
                    Intent myIntent = new Intent(getContext(), PopManager.class);
                    if(room.getNodeRef() != null) {
                        String docRefNode = room.getNodeRef().getPath();
                        room.setNodeRef(null);
                        myIntent.putExtra("path", docRefNode);
                        myIntent.putExtra("room", room);
                        getContext().startActivity(myIntent);
                    }
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
    public void cancelTraject() {
        int[] oldUserPosition = new int[2];
        oldUserPosition[0] = nodesToDraw[0][0];
        oldUserPosition[1] = nodesToDraw[0][1];
        nodesToDraw = new int[2][3];
        nodesToDraw[0][0] = oldUserPosition[0];
        nodesToDraw[0][1] = oldUserPosition[1];
        nodesToDraw[0][2] = 1;
        nodesToDraw[1][0] = oldUserPosition[0];
        nodesToDraw[1][1] = oldUserPosition[1];
        nodesToDraw[1][2] = 1;
    }

    private boolean clickPositionIsInAClass(float clickPositionX, float clickPositionY, Room room) {
        return clickPositionXIsBetweenFirebasePosition(clickPositionX, room) && clickPositionYIsBetweenFirebasePosition(clickPositionY, room);
    }

    private boolean clickPositionXIsBetweenFirebasePosition(float clickPositionX, Room room) {
        return (clickPositionX <= room.getPositionXTLeft()) && (clickPositionX >= room.getPositionXTRight());
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
        ActiveFloor = floorNumber;
        addElementFromFloor(floorNumber-1);
        invalidate();
    }

    private void addElementFromFloor(int floorNumber) {
        floorLevel = "Étage : " + listFloors.get(floorNumber).getFloorNum();
        currentFloor = listFloors.get(floorNumber).getFloorNum();
        bitmapMap = BitmapFactory.decodeResource(getResources(), listFloors.get(floorNumber).getDrawable());
    }
}