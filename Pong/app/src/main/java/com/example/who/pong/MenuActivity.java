package com.example.who.pong;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity {

    private TextView mTitleText;
    private TextView mHighscoreText;
    private Button mPongButton;
    private Button mBreakoutButton;
    private Button mEditNameButton;
    String mName;
    int mHighscore;
    final int RESULT_CODE_PONG = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        mTitleText = (TextView)findViewById(R.id.text_title);

        mHighscoreText = (TextView)findViewById(R.id.text_highscore);
        refreshText();

        mPongButton = (Button)findViewById(R.id.button_pong);
        mPongButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(MenuActivity.this, R.string.coming_soon, Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivityForResult(new Intent(MenuActivity.this,MainActivity.class), RESULT_CODE_PONG);
               // startActivity(intent);
            }
        });

        mBreakoutButton = (Button)findViewById(R.id.button_breakout);
        mBreakoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MenuActivity.this, R.string.coming_soon, Toast.LENGTH_SHORT).show();
            }
        });

        mEditNameButton = (Button)findViewById(R.id.button_edit_name);
        mEditNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditNameDialog end = new EditNameDialog();
                end.show(getSupportFragmentManager(), "edit_name_dialog");
                refreshText();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        refreshText();
    }

    public void refreshText() {
        mName = HighscorePreferences.getName(this);
        mHighscore = HighscorePreferences.getScore(this);
        mHighscoreText.setText(mName + ": " + mHighscore);
    }
}
