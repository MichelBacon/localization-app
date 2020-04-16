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
import com.cegepba.localization_app.Model.Rooms;

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
    private RoomsManager roomsManager;
    private ArrayList<Rooms> rooms;
    private float clickPositionX;
    private float clickPositionY;
    private int brightness(int pixel) { return (pixel >> 16)& 0xff; }

    //private static String[] imageName = {"image1","image2","image3","image4","image5"};

    //endregion

    //region constructor

    public Map(Context context) {
        super(context);
    }
    public Map(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        floorLevel = getResources().getString(R.string.Floor1st);
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        bitmapMap = BitmapFactory.decodeResource(getResources(), R.drawable.floors1);
        bitmapUserPosition = BitmapFactory.decodeResource(getResources(), R.drawable.location);
        roomsManager = new RoomsManager();
        roomsManager.setRoomsArray();
        rooms = roomsManager.getRooms();
    }

    //endregion

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener{
        @Override
        public boolean onScale(ScaleGestureDetector detector){

            mScaleFactor *= detector.getScaleFactor();
            mScaleFactor = Math.max(mMinZoom, Math.min(mScaleFactor, mMaxZoom));
            invalidate();

            return true;
        }
    }

    //region Draw Items
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBitmap(canvas);
        drawUserPositionBitmap(canvas);
        drawText(canvas);

        canvasWidth = 3050 * mScaleFactor;
        canvasHeight = 3050 * mScaleFactor;
    }

    public void drawBitmap(Canvas canvas) {
        canvas.save();
        canvas.translate(mPositionX,mPositionY);
        canvas.scale(mScaleFactor, mScaleFactor);
        canvas.drawBitmap(bitmapMap, 0, 0, null);
        canvas.restore();
    }

    public void drawText(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.GRAY);
        paint.setTextSize(100);
        canvas.drawText(floorLevel, 100,100, paint);
        canvas.drawText(Float.toString(mPositionX), 100,200, paint);
        canvas.drawText(Float.toString(mPositionY), 100,300, paint);
        canvas.drawText(Float.toString(mScaleFactor), 100,400, paint);
    }

    public void drawUserPositionBitmap(Canvas canvas) {
        canvas.save();
        canvas.translate(mPositionX,mPositionY);
        canvas.scale(mScaleFactor/3, mScaleFactor/3);
        //TODO position bitmap = position user
        canvas.drawBitmap(bitmapUserPosition, 4000, 4000, null);
        canvas.restore();
    }

    //endregion

    final GestureDetector gestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
        public void onLongPress(MotionEvent e) {
            for (Rooms room : rooms) {
                if(clickPositionIsInAClass(clickPositionX, clickPositionY, room)) {
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
                float nX = event.getX();
                float nY = event.getY();

                if (mPositionX + (nX - refX) <= 0 && mPositionX + (nX - refX) >= (canvasWidth * -1))
                    mPositionX += nX - refX;

                if (mPositionY + (nY - refY) <= 0 && mPositionY + (nY - refY) >= (canvasHeight * -1))
                    mPositionY += nY - refY;

                refX = nX;
                refY = nY;

                invalidate();
                break;
        }
        return true;
    }

    private boolean clickPositionIsInAClass(float clickPositionX, float clickPositionY, Rooms room) {
        return clickPositionXIsBetweenFirebasePosition(clickPositionX, room) && clickPositionYIsBetweenFirebasePosition(clickPositionY, room);
    }

    private boolean clickPositionXIsBetweenFirebasePosition(float clickPositionX, Rooms room) {
        return (clickPositionX <= room.getPositionYTLeft()) && (clickPositionX >= room.getPositionXTRight());
    }

    private boolean clickPositionYIsBetweenFirebasePosition(float clickPositionY, Rooms room) {
        return (clickPositionY <= room.getPositionYTLeft()) && (clickPositionY >= room.getPositionYBLeft());
    }

    public void changeFloor(int floorNumber){
         //String name = "R.drawable." + imageName[floorNumber];
         // bitmap = BitmapFactory.decodeResource(getResources(), name);
         switch (floorNumber){
             case 1 :
                 floorLevel = getResources().getString(R.string.Floor1st);
                 bitmapMap = BitmapFactory.decodeResource(getResources(), R.drawable.floors1);
                 break;
             case 2 :
                 floorLevel = getResources().getString(R.string.Floor2nd);
                 bitmapMap = BitmapFactory.decodeResource(getResources(), R.drawable.floors2);
                 break;
             case 3 :
                 floorLevel = getResources().getString(R.string.Floor3rd);
                 bitmapMap = BitmapFactory.decodeResource(getResources(), R.drawable.floors3);
                 break;
             case 4 :
                 floorLevel = getResources().getString(R.string.Floor4th);
                 bitmapMap = BitmapFactory.decodeResource(getResources(), R.drawable.floors4);
                 break;
             case 5 :
                 floorLevel = getResources().getString(R.string.Floor5th);
                 bitmapMap = BitmapFactory.decodeResource(getResources(), R.drawable.floors5);
                 break;
             default:
         }
        invalidate();
     }

//    @Override
//    protected void onSizeChanged(int w, int h, int oldw, int oldh){
//        super.onSizeChanged(w,h,oldw,oldh);
//
//        if(bitmap == null){
//
//            Bitmap srcBitmap = BitmapFactory.decodeResource(getResources(), Common.PICTURE_SELECTED);
//            bitmap = Bitmap.createScaledBitmap(srcBitmap, w, h, false);
//
//            for(int i=0;i<bitmap.getWidth();i++){
//                for(int j=0;j<bitmap.getHeight();j++){
//
//                    int alpha = 255 - brightness(bitmap.getPixel(i,j));
//
//                    if(alpha<200){
//                        bitmap.setPixel(i,j, Color.WHITE);
//                    }else{
//                        bitmap.setPixel(i,j,Color.BLACK);
//                    }
//                }
//            }
//        }
//    }



}