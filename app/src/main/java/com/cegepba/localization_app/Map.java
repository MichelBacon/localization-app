package com.cegepba.localization_app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import androidx.annotation.Nullable;

import com.google.android.gms.common.internal.service.Common;

//import com.hazem.coloringforkids.commom.Common;
//import com.hazem.coloringforkids.utils.FloodFill;


public class Map extends View {

    private static Bitmap bitmap;
    private float mPositionX,mPositionY;
    private float refX,refY;
    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.0f;

    private final static float mMinZoom = 0.6f;
    private final static float mMaxZoom = 1.5f;
    private float canvasWidth;
    private float canvasHeight;

    //private static String[] imageName = {"image1","image2","image3","image4","image5"};


    public Map(Context context) {
        super(context);
    }
    public Map(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image1);
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener{
        @Override
        public boolean onScale(ScaleGestureDetector detector){

            mScaleFactor *= detector.getScaleFactor();
            mScaleFactor = Math.max(mMinZoom, Math.min(mScaleFactor, mMaxZoom));
            invalidate();

            return true;
        }
    }
    private int brightness(int pixel) { return (pixel >> 16)& 0xff; }
    @Override

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBitmap(canvas);
        drawText(canvas);
    }

    public void drawBitmap(Canvas canvas){
        canvas.save();
        canvas.translate(mPositionX,mPositionY);
        canvas.scale(mScaleFactor, mScaleFactor);
        canvas.drawBitmap(bitmap, 0, 0, null);
        canvas.restore();

        canvasWidth = 2606 * mScaleFactor;
        canvasHeight = 1967 * mScaleFactor;
    }

    public void drawText(Canvas canvas) {
        Paint paint = new Paint();
        paint.setTextSize(100);
        canvas.drawText(Float.toString(mPositionX), 100,100, paint);
        canvas.drawText(Float.toString(mPositionY), 100,200, paint);
        canvas.drawText(Float.toString(mScaleFactor), 100,300, paint);
    }

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
                float nX = event.getX();
                float nY = event.getY();


                if (mPositionX + (nX - refX) <= 0 && mPositionX + (nX - refX) >= (canvasWidth * -1))
                    mPositionX += nX - refX;

                if (mPositionY + (nY - refY) <= 0 && mPositionY + (nY - refY) >= (canvasHeight * -1))
                    mPositionY += nY - refY;



                //mPositionX += nX - refX;
                //mPositionY += nY - refY;


                refX = nX;
                refY = nY;

                invalidate();

                break;
        }
        return true;
    }
    public void changeFloor(int floorNumber){
         //String name = "R.drawable." + imageName[floorNumber];
         // bitmap = BitmapFactory.decodeResource(getResources(), name);
         switch (floorNumber){
             case 1 :
                 bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image1);
                 break;
             case 2 :
                 bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image2);
                 break;
             case 3 :
                 bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image3);
                 break;
             case 4 :
                 bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image4);
                 break;
             case 5 :
                 bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image5);
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