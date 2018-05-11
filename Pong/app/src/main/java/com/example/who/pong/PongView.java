package com.example.who.pong;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Point;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.io.IOException;
import android.app.Activity;

import javax.xml.transform.Result;

class PongView extends SurfaceView implements Runnable {

    Thread mGameThread = null;
    SurfaceHolder mSurfaceHolder;
    volatile boolean mPlaying;
    boolean mPaused = true;
    Paint mPaint;
    Canvas mCanvas;
    long mFPS;
    int mScreenX;
    int mScreenY;
    Ball mBall;
    Bat mBat;
    SoundPool mSoundPool;
    int beep1;
    int beep2;
    int beep3;
    int loseLife;
    int explode;
    int mScore;
    int mLives;
    int mHighscore;
    Intent intent;

    public PongView(Context context, Intent i, int x, int y) {
        super(context);
        mScreenX = x;
        mScreenY = y;
        mSurfaceHolder = getHolder();
        mPaint = new Paint();
        mBat = new Bat(x, y);
        mBall = new Ball(x, y);
        mScore = 0;
        mLives = 3;
        intent = i;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            mSoundPool = new SoundPool.Builder()
                    .setMaxStreams(5)
                    .setAudioAttributes(audioAttributes)
                    .build();

        } else {
            mSoundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }

        try {
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor;

            descriptor = assetManager.openFd("beep1.ogg");
            beep1 = mSoundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("beep2.ogg");
            beep2 = mSoundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("beep3.ogg");
            beep3 = mSoundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("loseLife.ogg");
            loseLife = mSoundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("explode.ogg");
            explode = mSoundPool.load(descriptor, 0);

        } catch(IOException e){
            // Print an error message to the console
            Log.e("error", "failed to load sound files");
        }

        setupAndRestart();
    }

    @Override
    public void run() {
        while (mPlaying) {
            long startFrameTime = System.currentTimeMillis();

            if(!mPaused){
                update();
            }

            draw();

            long timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                mFPS = 1000 / timeThisFrame;
            }
        }
    }

    public void resume() {
        mPlaying = true;
        mGameThread = new Thread(this);
        mGameThread.start();
    }

    public void pause() {
        mPlaying = false;
        try {
            mGameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }
    }

    public void update() {
        mBat.update(mFPS);
        mBall.update(mFPS);
        if(RectF.intersects(mBat.getRect(), mBall.getRect())) {
            mBall.setRandomXVelocity();
            mBall.reverseYVelocity();
            mBall.clearObstacleY(mBat.getRect().top - 2);

            mScore++;
            if (mScore > mHighscore) {mHighscore = mScore;}
            mBall.increaseVelocity();

            mSoundPool.play(beep1, 1, 1, 0, 0, 1);
        }
        if(mBall.getRect().bottom > mScreenY){
            mBall.reverseYVelocity();
            mBall.clearObstacleY(mScreenY - 2);
            mLives--;
            mSoundPool.play(loseLife, 1, 1, 0, 0, 1);

            if(mLives == 0){
                mPaused = true;
                setupAndRestart();
            }
        }
        if(mBall.getRect().top < 0){
            mBall.reverseYVelocity();
            mBall.clearObstacleY(12);

            mSoundPool.play(beep2, 1, 1, 0, 0, 1);
        }
        if(mBall.getRect().left < 0){
            mBall.reverseXVelocity();
            mBall.clearObstacleX(2);

            mSoundPool.play(beep3, 1, 1, 0, 0, 1);
        }
        if(mBall.getRect().right > mScreenX){
            mBall.reverseXVelocity();
            mBall.clearObstacleX(mScreenX - 22);

            mSoundPool.play(beep3, 1, 1, 0, 0, 1);
        }
    }

    public void setupAndRestart(){
        mBall.reset(mScreenX, mScreenY);
        if(mLives == 0) {
            mScore = 0;
            mLives = 3;
        }
        intent.putExtra("EXTRA_HIGHSCORE", mHighscore);
        Activity ac = (Activity) getContext();
        ac.setResult(Activity.RESULT_OK, intent);
    }

    public void draw() {
        if (mSurfaceHolder.getSurface().isValid()) {
            mCanvas = mSurfaceHolder.lockCanvas();
            mCanvas.drawColor(Color.argb(255, 0, 0, 0));
            mPaint.setColor(Color.argb(255, 255, 255, 255));
            mCanvas.drawRect(mBat.getRect(), mPaint);
            mCanvas.drawRect(mBall.getRect(), mPaint);
            //mPaint.setColor(Color.argb(255, 255, 255, 255));
            mPaint.setTextSize(40);
            mCanvas.drawText("Score: " + mScore + "   Lives: " + mLives, 10, 50, mPaint);

            mSurfaceHolder.unlockCanvasAndPost(mCanvas);
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:

                mPaused = false;
                if(motionEvent.getX() > mScreenX / 2){
                    mBat.setMovementState(mBat.RIGHT);
                }
                else{
                    mBat.setMovementState(mBat.LEFT);
                }

                break;

            case MotionEvent.ACTION_UP:

                mBat.setMovementState(mBat.STOPPED);
                break;
        }
        return true;
    }
}
