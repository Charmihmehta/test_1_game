package com.example.thenosetest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.util.Random;

public class GameEngine extends SurfaceView implements Runnable {

    // Android debug variables
    final static String TAG = "THE-NOSE-TEST";

    // screen size
    int screenHeight;
    int screenWidth;

    // game state
    boolean gameIsRunning;

    // threading
    Thread gameThread;


    // drawing variables
    SurfaceHolder holder;
    Canvas canvas;
    Paint paintbrush;
    int lives = 5;
    Bitmap bgImg;

//finger position
    boolean moveRight =true;
    boolean moveLeft = false;
    int fingerXPosition;
    int speed = 10;

    //hitbox
    boolean showHitbox = false;

    // -----------------------------------
    // GAME SPECIFIC VARIABLES
    // -----------------------------------

    // ----------------------------
    // ## SPRITES
    // ----------------------------
    Nose nose;
    Finger finger;

    // ----------------------------
    // ## GAME STATS
    // ----------------------------
    int score = 0;
    int miss = 0;
    String Msg = "";
   // String wiMsg;

    public GameEngine(Context context, int w, int h) {
        super(context);


        this.holder = this.getHolder();
        this.paintbrush = new Paint();

        this.screenWidth = w;
        this.screenHeight = h;


        this.printScreenInfo();

        // @TODO: Add your sprites
        this.spawnFinger();
        this.spawnNose();
        // @TODO: Any other game setup
//        bgImg = BitmapFactory.decodeResource(context.getResources(), R.drawable.background);
//        bgImg = Bitmap.createScaledBitmap(bgImg, this.screenWidth, this.screenHeight, false);

    }


    private void printScreenInfo() {

        Log.d(TAG, "Screen (w, h) = " + this.screenWidth + "," + this.screenHeight);
    }

    private void spawnFinger() {
        // put player in middle of screen --> you may have to adjust the Y position
        // depending on your device / emulator
        finger = new Finger(this.getContext(), (this.screenWidth/2) , (this.screenHeight ) / 2);

    }

    private void spawnNose() {
        Random random = new Random();

        //@TODO: Place the nose
        nose = new Nose(this.getContext(), (this.screenWidth/2) -300, 0);
    }

    // ------------------------------
    // GAME STATE FUNCTIONS (run, stop, start)
    // ------------------------------
    @Override
    public void run() {
        while (gameIsRunning == true) {
            this.updatePositions();
            this.redrawSprites();
            this.setFPS();
        }
    }


    public void pauseGame() {
        gameIsRunning = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            // Error
        }
    }

    public void startGame() {
        gameIsRunning = true;
        gameThread = new Thread(this);
        gameThread.start();
    }


    // ------------------------------
    // GAME ENGINE FUNCTIONS
    // - update, draw, setFPS
    // ------------------------------
 public void updateFinger()
    {
        fingerXPosition = this.finger.xPosition;
        if(moveRight == true) {
            fingerXPosition = this.finger.xPosition + speed;
            if(fingerXPosition >=screenWidth*0.85){
                this.moveLeft = true;
                this.moveRight = false;
            }

        }
         if(this.moveLeft == true){
            fingerXPosition = this.finger.xPosition - speed;
            if(fingerXPosition <= screenWidth*0.15){
                this.moveRight =true;
                this.moveLeft = false;
            }
        }

        finger.setXPosition(fingerXPosition);
}
    public void updatePositions() {
        // @TODO: Update position of finger
         updateFinger();
        finger.updateFingerPosition();


//        fingerXPosition = this.finger.xPosition;
//        if(moveRight == true) {
//            fingerXPosition = this.finger.xPosition + 10;
//            if(fingerXPosition >=screenWidth){
//                this.moveLeft = true;
//                this.moveRight = false;
//            }
//
//        }
//        if(this.moveLeft == true){
//            fingerXPosition = this.finger.xPosition - 10;
//            if(fingerXPosition <= 0){
//                this.moveRight =true;
//                this.moveLeft = false;
//            }
//        }

        // @TODO: Update position of nose
        //nose.updateEnemyPosition();

        // @TODO: Collision detection between finger and wall
        if (finger.getYPosition()<=0) {

                miss++;

                Msg = "You loss";

            finger.direction = -1;
            finger.setXPosition((this.screenWidth/2) - 700);
            finger.setYPosition((this.screenHeight ) / 2);

            Log.d(TAG,"collide to wall");
            //finger.setXPosition(this.screenWidth);
        }


        // @TODO: Collision detection between nose and finger

        if(finger.getHitbox().intersect(nose.getHitbox1())){
            score++;

            Msg = "you win";
            //reset finger
            startGame();
            finger.direction = -1;
            finger.setXPosition((this.screenWidth/2) );
            finger.setYPosition((this.screenHeight ) / 2);
        }
        else if(finger.getHitbox().intersect(nose.getHitbox2())){
            score++;

            //reset finger
            finger.direction = -1;
            finger.setXPosition((this.screenWidth/2) );
            finger.setYPosition((this.screenHeight ) / 2);
        }

    }

    public void redrawSprites() {
        if (this.holder.getSurface().isValid()) {
            this.canvas = this.holder.lockCanvas();

            //----------------

            // configure the drawing tools
            this.canvas.drawColor(Color.argb(255, 255, 255, 255));
            paintbrush.setColor(Color.WHITE);


//            //@TODO Draw the background
//
//            canvas.drawBitmap(bgImg, 0, 0, paintbrush);
            paintbrush.setColor(Color.BLUE);
            paintbrush.setStyle(Paint.Style.STROKE);
            paintbrush.setStrokeWidth(5);
          //  canvas.drawRect(0,100,200,3000,paintbrush);
          //  this.canvas.drawRect((float) (this.screenWidth * 0.15), 0,(float) (this.screenWidth * 0.15),this.screenHeight,paintbrush);
            //@TODO: Draw the finger
          //  Log.d(TAG, "player(w, h) = " + this.player.getXPosition() + "," + this.player.getYPosition());
            canvas.drawBitmap(this.finger.getBitmap(), this.finger.getXPosition(), this.finger.getYPosition(), paintbrush);


            //@TODO: Draw the nose
          //  Log.d(TAG, "enemy(w, h) = " + this.enemy.getXPosition() + "," + this.enemy.getYPosition());
            canvas.drawBitmap(this.nose.getBitmap(), this.nose.getXPosition(), this.nose.getYPosition(), paintbrush);


            if(this.showHitbox == true){
                //@TODO: hit box for finger
                paintbrush.setColor(Color.BLUE);
                paintbrush.setStyle(Paint.Style.STROKE);
                paintbrush.setStrokeWidth(5);
                canvas.drawRect(finger.getHitbox(), paintbrush);

                //@TODO: hit box for nose
                paintbrush.setColor(Color.RED);
                paintbrush.setStyle(Paint.Style.STROKE);
                paintbrush.setStrokeWidth(5);
                canvas.drawRect(nose.getHitbox1(), paintbrush);
                paintbrush.setColor(Color.GREEN);
                paintbrush.setStyle(Paint.Style.STROKE);
                paintbrush.setStrokeWidth(5);
                canvas.drawRect(nose.getHitbox2(), paintbrush);
                 //@TODO: boundry
                canvas.drawLine((float) (this.screenWidth * 0.15),0, (float) (this.screenWidth * 0.15),this.screenHeight,paintbrush);
                canvas.drawLine((float) (this.screenWidth * 0.85),0, (float) (this.screenWidth * 0.85),this.screenHeight,paintbrush);
            }
            if(this.showHitbox == false)
            {
                paintbrush.setColor(getResources().getColor(android.R.color.transparent));
                paintbrush.setStyle(Paint.Style.STROKE);
                paintbrush.setStrokeWidth(5);
                canvas.drawRect(finger.getHitbox(), paintbrush);

                paintbrush.setColor(getResources().getColor(android.R.color.transparent));
                paintbrush.setStyle(Paint.Style.STROKE);
                paintbrush.setStrokeWidth(5);
                canvas.drawRect(nose.getHitbox1(), paintbrush);

                paintbrush.setColor(getResources().getColor(android.R.color.transparent));
                paintbrush.setStyle(Paint.Style.STROKE);
                paintbrush.setStrokeWidth(5);
                canvas.drawRect(nose.getHitbox2(), paintbrush);
                canvas.drawLine((float) (this.screenWidth * 0.15),0, (float) (this.screenWidth * 0.15),this.screenHeight,paintbrush);
                canvas.drawLine((float) (this.screenWidth * 0.85),0, (float) (this.screenWidth * 0.85),this.screenHeight,paintbrush);
            }

            //----------------


            //@TODO: Draw text on screen
            paintbrush.setColor(Color.BLUE);
            paintbrush.setStyle(Paint.Style.STROKE);
            paintbrush.setTextSize(50);
            canvas.drawText("Nose picked:" + this.score, 50, 600, paintbrush);

            canvas.drawText("Nose missed:" +this.miss, 50, 400, paintbrush);

            paintbrush.setTextSize(50);
            canvas.drawText(""+ Msg, screenWidth/2, screenHeight/2, paintbrush);
            this.holder.unlockCanvasAndPost(canvas);
        }
    }

    public void setFPS() {
        try {
            gameThread.sleep(50);
        } catch (Exception e) {

        }
    }

    // ------------------------------
    // USER INPUT FUNCTIONS
    // ------------------------------


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int userAction = event.getActionMasked();
        //@TODO: What should happen when person touches the screen?
        if (userAction == MotionEvent.ACTION_DOWN) {
            // move player up
            Toast.makeText(getContext(), "tap detected", Toast.LENGTH_SHORT).show();
            this.finger.setDirection(1);
        } else if (userAction == MotionEvent.ACTION_UP) {
            // move player down
//            this.player.setDirection(0);
        }

        return true;
    }
}
