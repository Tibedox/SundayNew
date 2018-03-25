package com.example.teacher.snownew;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class Intro extends AppCompatActivity {
    NiceButton buttonNewGame, buttonLoadGame, buttonExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        V.scrWidth = getResources().getDisplayMetrics().widthPixels;
        V.scrHeight = getResources().getDisplayMetrics().heightPixels;
        V.calculateCoefficientScreen();
        createButtons();
        setClickers();
    }

    private void createButtons(){
        float x, y;
        int widthButton, heightButton;
        widthButton = (V.scrWidth>V.scrHeight?V.scrWidth:V.scrHeight)/3;
        heightButton = (int)(widthButton/V.KOEFF_BUTTON_INTRO);
        x = V.scrWidth/2-widthButton/2;
        y = V.scrHeight/2-heightButton/2;
        buttonNewGame = new NiceButton(this, x, y, widthButton, heightButton, "New Game");
        y = V.scrHeight/2-heightButton*2;
        buttonLoadGame = new NiceButton(this, x, y, widthButton, heightButton, "Load Game");
        y = V.scrHeight/2+heightButton;
        buttonExit = new NiceButton(this, x, y, widthButton, heightButton, "Exit");
    }

    private void setClickers(){
        buttonNewGame.image.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()== MotionEvent.ACTION_DOWN){
                    buttonNewGame.buttonDown();
                }
                if(event.getAction()== MotionEvent.ACTION_UP) {
                    buttonNewGame.buttonUp();
                    startActivity(new Intent(Intro.this, MainActivity.class));
                    //finish();
                }
                return true;
            }
        });

        buttonLoadGame.image.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()== MotionEvent.ACTION_DOWN){
                    buttonLoadGame.buttonDown();
                }
                if(event.getAction()== MotionEvent.ACTION_UP) {
                    buttonLoadGame.buttonUp();
                    SharedPreferences sp = getSharedPreferences(V.PREFERENCES, Context.MODE_PRIVATE);
                    if(sp.contains("DM.x")) {
                        V.canToLoadGame = true;
                        startActivity(new Intent(Intro.this, MainActivity.class));
                    }
                }
                return true;
            }
        });

        buttonExit.image.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()== MotionEvent.ACTION_DOWN){
                    buttonExit.buttonDown();
                }
                if(event.getAction()== MotionEvent.ACTION_UP) {
                    buttonExit.buttonUp();
                    System.exit(0);
                }
                return true;
            }
        });
    }
}
