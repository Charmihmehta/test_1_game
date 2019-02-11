package com.example.thenosetest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Log;

import static com.example.thenosetest.GameEngine.TAG;

public class Nose {
    int xPosition;
    int yPosition;
    int direction;
    Bitmap image;
    private Rect hitbox1;
    private Rect hitbox2;


    public Nose(Context context, int x, int y) {
        this.image = BitmapFactory.decodeResource(context.getResources(), R.drawable.nose01);
        this.xPosition = x;
        this.yPosition = y;
        int imgWidth = this.image.getWidth();
        int imgHeight = this.image.getHeight();

        Log.d(TAG, "posit (left, top) = " + this.xPosition + "," + this.yPosition);
        Log.d(TAG, "mon (right, bottom) = " + imgWidth + "," + imgHeight);
        this.hitbox1 =new Rect(this.xPosition +100,
                this.yPosition +350,
                this.xPosition+ (imgWidth/2) - 35, this.yPosition +imgHeight);

        this.hitbox2 =new Rect(this.xPosition + (imgWidth/2) + 30,
                this.yPosition +350,
                this.xPosition+ (imgWidth) -80 , this.yPosition +imgHeight);
    }
    public Rect getHitbox1()
    {
        return this.hitbox1;
    }
    public Rect getHitbox2()
    {
        return this.hitbox2;
    }
    public void updateNosePosition() {


    }


    public void setXPosition(int x) {
        this.xPosition = x;
    }
    public void setYPosition(int y) {
        this.yPosition = y;
    }
    public int getXPosition() {
        return this.xPosition;
    }
    public int getYPosition() {
        return this.yPosition;
    }

    public Bitmap getBitmap() {
        return this.image;
    }

}
