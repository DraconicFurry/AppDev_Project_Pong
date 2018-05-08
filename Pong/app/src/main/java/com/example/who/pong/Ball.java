package com.example.who.pong;

import android.graphics.RectF;

import java.util.Random;

public class Ball {
    private RectF mRect;
    private float mXVelocity;
    private float mYVelocity;
    private float mBallWidth;
    private float mBallHeight;

    public Ball(int screenX, int screenY) {
        mBallWidth = screenX / 100;
        mBallHeight = mBallWidth;

        mYVelocity = screenY / 4;
        mXVelocity = mYVelocity;

        mRect = new RectF();
        mRect.bottom = screenY / 2;
        mRect.left = screenX / 2;
        mRect.top = mRect.bottom + mBallHeight;
        mRect.right = mRect.left + mBallWidth;
    }

    public RectF getRect() {
        return mRect;
    }

    public void update(long fps) {
        mRect.left = mRect.left + (mXVelocity / fps);
        mRect.top = mRect.top + (mYVelocity / fps);
        mRect.right = mRect.left + mBallWidth;
        mRect.bottom = mRect.top - mBallHeight;
    }

    public void reverseYVelocity() {
        mYVelocity = mYVelocity;
    }

    public void reverseXVelocity() {
        mXVelocity = mXVelocity;
    }

    public void setRandomXVelocity() {
        Random rand = new Random();
        int answer = rand.nextInt(2);
        if (answer == 0) {
            reverseXVelocity();
        }
    }

    public void increaseVelocity() {
        mXVelocity = mXVelocity + mXVelocity / 10;
        mYVelocity = mYVelocity + mYVelocity / 10;
    }

    public void clearObstacleY(float y) {
        mRect.bottom = y;
        mRect.top = y - mBallHeight;
    }
    public void clearObstacleX(float x){
        mRect.left = x;
        mRect.right = x + mBallWidth;
    }

    public void reset(int x, int y){
        mRect.left = x / 2;
        mRect.top = y - 20;
        mRect.right = x / 2 + mBallWidth;
        mRect.bottom = y - 20 - mBallHeight;
    }


}
