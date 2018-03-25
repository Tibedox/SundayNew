package com.example.teacher.snownew;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class Snegurochka {
    double x, y, vx;
    int sizeX, sizeY;
    int direction = -1; // 1 - вправо, -1 - влево
    boolean go = false;
    ImageView image;
    Snegurochka(Activity activity){
        sizeX = (int)(300*V.kS); //524x600
        sizeY = (int)(sizeX/(524/600.));
        x=V.scrWidth/2;
        y=V.scrHeight-sizeY/2;
        vx=15*V.kS;
        image = new ImageView(activity);
        image.setImageResource(R.drawable.snegurochka);
        activity.addContentView(image, new RelativeLayout.LayoutParams(sizeX,sizeY));
        outXY();
    }
    private void outXY(){
        image.setX((float)x-sizeX/2);
        image.setY((float)y-sizeY/2);
    }
    public void move(){
        if(go) x+=vx*direction;
        if(x >= V.touchScreenX-vx/2 && x <= V.touchScreenX+vx/2) go=false;
        if(x<sizeX/2 || x>V.scrWidth-sizeX/2) go=false;
        outXY();
    }
    public void reverse(){
        image.setScaleX(-direction);
    }
}
