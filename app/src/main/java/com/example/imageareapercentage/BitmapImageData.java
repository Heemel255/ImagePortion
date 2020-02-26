package com.example.imageareapercentage;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class BitmapImageData {

    private Bitmap originalImg;
    private Bitmap img;

    private int boxSize;

    private Canvas canvas;
    private Paint bmpPaint;

    private Rect mRect;
    private Paint mPaint;

    private Paint sPaint;


    public BitmapImageData(Bitmap b) {

        this.originalImg = b;

        this.initCanvas();
    }

    private void initCanvas() {

        this.img = Bitmap.createBitmap(originalImg.getWidth(), originalImg.getHeight(), Bitmap.Config.RGB_565);
        this.canvas = new Canvas(img);

        this.bmpPaint = new Paint();
        this.bmpPaint.setAntiAlias(true);
        this.bmpPaint.setFilterBitmap(true);
        this.bmpPaint.setDither(true);

        canvas.drawBitmap(originalImg,0,0, bmpPaint);

        //paint settings for rectangle
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setColor(Color.RED);
        this.mPaint.setAlpha(100);

        //paint settings for text
        this.sPaint = new Paint();
        this.sPaint.setAntiAlias(true);
        this.sPaint.setColor(Color.WHITE);
    }



    public void drawRectToImg(int x1, int y1, int x2, int y2) {

        this.resetCanvas();

        //these conditions check if the user started touching the screen from the right and/or bottom side and dragged opposite
        int left = x1 > x2 ? x2 : x1;
        int right =  left == x2 ? x1: x2;
        int top = y1 > y2 ? y2 : y1;
        int bottom =  top == y2 ? y1: y2;

        //calculate the percentage
        this.calcBoxSize( left, right, top, bottom);
        String percText = this.getBoxSizePercent() != 0 ? String.valueOf(this.getBoxSizePercent()) + "%" : "<1%";

        //draw rectangle
        mRect = new Rect(left, top, right, bottom);
        canvas.drawRect(mRect, mPaint);

        //draw percentage text
        int textWidth = (int)sPaint.measureText(percText);
        int xPerc = left + ((((right - left) / 2)) - (textWidth / 2));
        int yPerc = (int)((top + ((bottom - top) /2)) - ((sPaint.descent() + sPaint.ascent()) /2));
        canvas.drawText(percText, (float)xPerc, (float)yPerc, sPaint);
    }

    private void calcBoxSize(int left, int right, int top, int bottom){

        int boxWidth = right - left;
        int boxHeight = bottom - top;
        this.boxSize = boxHeight * boxWidth;
    }

    private int getBoxSizePercent() {

        double imgSize = (double)(this.originalImg.getWidth() * this.originalImg.getHeight());
        double finalSizePercentage = (this.boxSize / imgSize) * 100;
        return (int)finalSizePercentage;
    }

    private void resetCanvas() {

        canvas.drawBitmap(originalImg,0,0, bmpPaint);
    }

    public Bitmap getBmp() {

        if(this.img != null) {
            return this.img;
        }
        else {
            return  null;
        }
    }
}
