package com.project_core.imageareapercentage;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class BitmapImageData {

    private Bitmap originalImg;
    private Bitmap img;

    private int boxSize;

    private Canvas canvas;
    private Paint bmpPaint;

    private Rect mRect;
    private Paint mPaint;

    private Paint sPaint;


    public BitmapImageData(Bitmap bmp) {

        originalImg = bmp;

        initCanvas();
    }

    private void initCanvas() {

        img = Bitmap.createBitmap(originalImg.getWidth(), originalImg.getHeight(), Bitmap.Config.RGB_565);
        canvas = new Canvas(img);

        bmpPaint = new Paint();
        bmpPaint.setAntiAlias(true);
        bmpPaint.setFilterBitmap(true);
        bmpPaint.setDither(true);

        canvas.drawBitmap(originalImg,0,0, bmpPaint);

        //paint settings for rectangle
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
        mPaint.setAlpha(100);

        //paint settings for text
        sPaint = new Paint();
        sPaint.setAntiAlias(true);
        sPaint.setColor(Color.WHITE);
    }

    //x1/y1 and x2/y2 are the initial click and final release coordinates
    public void drawRectToImg(int x1, int y1, int x2, int y2) {

        resetCanvas();

        //these conditions check if the user started touching the screen from the right and/or bottom side and dragged opposite
        int left = x1 > x2 ? x2 : x1;
        int right =  left == x2 ? x1: x2;
        int top = y1 > y2 ? y2 : y1;
        int bottom =  top == y2 ? y1: y2;

        //calculate the percentage, boxSize: width * height of user selected rectangle
        boxSize = (bottom - top) * (right - left);
        int p = getBoxSizePercent();
        String percText = p != 0 ? String.valueOf(p) + "%" : "<1%";

        //draw rectangle
        mRect = new Rect(left, top, right, bottom);
        canvas.drawRect(mRect, mPaint);

        //scale percentage text and draw
        int textWidth = (int)sPaint.measureText(percText);
        int xPerc = left + ((((right - left) / 2)) - (textWidth / 2));
        int yPerc = (int)((top + ((bottom - top) /2)) - ((sPaint.descent() + sPaint.ascent()) /2));

        float tSize = (float)(Math.sqrt(boxSize) * 0.5);
        sPaint.setTextSize(tSize);
        canvas.drawText(percText, (float)xPerc, (float)yPerc, sPaint);
    }

    private int getBoxSizePercent() {

        double imgSize = (double)(originalImg.getWidth() * originalImg.getHeight());
        double finalSizePercentage = (boxSize / imgSize) * 100;
        return (int)finalSizePercentage;
    }

    private void resetCanvas() {

        canvas.drawBitmap(originalImg,0,0, bmpPaint);
    }

    public Bitmap getBmp() {

        if(img != null) {
            return img;
        }
        else {
            return  null;
        }
    }
}
