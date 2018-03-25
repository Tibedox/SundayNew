package com.example.teacher.snownew;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Highscore extends AppCompatActivity {
    TextView text;
    EditText name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);
        setFullScreenLandscape();
        createTextFields();
        text.setText(name.getText());
    }
    private void setFullScreenLandscape(){
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }
    private void createTextFields(){
        text = new TextView(this);
        this.addContentView(text, new RelativeLayout.LayoutParams(V.scrWidth/2, V.scrHeight/2));
        text.setX((float)(V.scrWidth/2));
        text.setY((float)(20*V.kS));
        text.setTextSize((float)(40*V.kS));
        text.setTextColor(Color.RED);
        text.setText("Enter your name: ");

        name = new EditText(this);
        this.addContentView(name, new RelativeLayout.LayoutParams(V.scrWidth/2, V.scrHeight/2));
        name.setX((float)(V.scrWidth/2));
        name.setY((float)(120*V.kS));
        name.setTextSize((float)(40*V.kS));
        name.setTextColor(Color.RED);
        name.setHint("Noname");
    }
}
