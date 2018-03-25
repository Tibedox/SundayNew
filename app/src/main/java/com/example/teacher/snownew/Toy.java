package com.example.teacher.snownew;

import android.app.Activity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class Toy {
    public static final int LIVE = 0, LOST = 1, CAUGHT = 2;
    double x, y, vy;
    int size;
    int status = LIVE;
    ImageView image;
    Toy (Activity activity, double x){
        this.x=x;
        y=120*V.kS;
        vy=5*V.kS + Math.random()*10*V.kS;
        size = (int)(100*V.kS);
        image = new ImageView(activity);
        image.setImageResource(R.drawable.toy+(int)(Math.random()*3)+1);
        activity.addContentView(image, new RelativeLayout.LayoutParams(size,size));
        outXY();
    }
    private void outXY(){
        image.setX((float)x-size/2);
        image.setY((float)y-size/2);
    }
    public void move(){
        y+=vy;
        if(y>V.scrHeight+size/2) {
            if(status==LIVE) lostToy();
        }
        outXY();
    }
    public void lostToy(){
        FrameLayout parent = (FrameLayout) image.getParent();
        parent.removeView(image);
        status = LOST;
    }
    public void catchToy(Snegurochka snegurochka){
        if(y>snegurochka.y-snegurochka.sizeY/2 && y<snegurochka.y &&
                x>snegurochka.x-snegurochka.sizeX/2 && x<snegurochka.x+snegurochka.sizeX/2) {
            FrameLayout parent = (FrameLayout) image.getParent();
            parent.removeView(image);
            status = CAUGHT;
        }
    }
}
