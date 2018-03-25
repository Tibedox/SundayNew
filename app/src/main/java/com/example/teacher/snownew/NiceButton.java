package com.example.teacher.snownew;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class NiceButton {

    private String name;
    public ImageView image;
    NiceButton(Activity activity, float x, float y, int sx, int sy, String name){
        this.name = name;
        image = new ImageView(activity);
        image.setImageResource(chooseImage(name));
        outXY(x,y);
        activity.addContentView(image, new RelativeLayout.LayoutParams(sx, sy));
    }
    private void outXY(float x, float y){
        image.setX(x);
        image.setY(y);
    }
    private int chooseImage(String s){
        switch (s) {
            case "New Game": return R.drawable.button_new_game;
            case "New Game Down": return R.drawable.button_new_game_down;
            case "Load Game": return R.drawable.button_load_game;
            case "Load Game Down": return R.drawable.button_load_game_down;
            case "Exit": return R.drawable.button_exit;
            case "Exit Down": return R.drawable.button_exit_down;
            default: return R.mipmap.ic_launcher;
        }
    }
    public void buttonDown(){
        image.setImageResource(chooseImage(name+" Down"));
    }
    public void buttonUp(){
        image.setImageResource(chooseImage(name));
    }
}
