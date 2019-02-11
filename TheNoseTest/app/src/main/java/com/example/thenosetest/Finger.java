package com.example.thenosetest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Log;

import static com.example.thenosetest.GameEngine.TAG;

public class Finger {

        int xPosition;
        int yPosition;
        int direction = -1;              // -1 = not moving, 0 = down, 1 = up
        Bitmap playerImage;

        int speed = 10;

        private Rect hitbox;


        public Finger(Context context, int x, int y) {
            this.playerImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.finger01);
            this.xPosition = x;
            this.yPosition = y;

            int imgWidth = this.playerImage.getWidth();
            int imgHeight = this.playerImage.getHeight();
            Log.d(TAG, "position (left, top) = " + this.xPosition + "," + this.yPosition);
            Log.d(TAG, "monkey (right, bottom) = " + imgWidth + "," + imgHeight);
            this.hitbox =new Rect(this.xPosition,
                    this.yPosition,
                    this.xPosition+ imgWidth, this.yPosition +imgHeight);

        }
        public Rect getHitbox()
        {
            return this.hitbox;
        }

        public void updateFingerPosition() {

           
       if(this.direction == 1){
           this.yPosition = this.yPosition - speed;
       }




            //move the hitbox
            this.hitbox.left = this.xPosition ;   //x1
            this.hitbox.top = this.yPosition;     //y1
            this.hitbox.right = (this.xPosition + this.playerImage.getWidth());   //x2
            this.hitbox.bottom = (this.yPosition + this.playerImage.getHeight());  //y2
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

        /**
         * Sets the direction of the player
         * @param i     0 = down, 1 = up
         */
        public void setDirection(int i) {
            this.direction = i;
        }
        public Bitmap getBitmap() {
            return this.playerImage;
        }


    }
