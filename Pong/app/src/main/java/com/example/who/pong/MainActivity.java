package com.example.who.pong;

import android.graphics.Point;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.Display;

public class MainActivity extends AppCompatActivity {
    PongView pongView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        pongView = new PongView(this, size.x, size.y);
        pongView.run();
        setContentView(pongView);

    }

    @Override
    protected void onResume() {
        super.onResume();
        pongView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pongView.pause();
    }
}