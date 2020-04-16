package com.cegepba.localization_app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import androidx.annotation.Nullable;

import com.cegepba.localization_app.R;

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
        drawUserPositionBitmap(canvas);
        drawText(canvas);
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
        canvas.translate(mPositionX, mPositionY);
        canvas.scale(mScaleFactor/3, mScaleFactor/3);
        //TODO position bitmap = position user
        canvas.drawBitmap(bitmapUserPosition, 3200, 6900, null);
        canvas.restore();
    }

    //endregion

    @Override
    public boolean onTouchEvent(MotionEvent event){
        mScaleDetector.onTouchEvent(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                refX = event.getX();
                refY = event.getY();
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