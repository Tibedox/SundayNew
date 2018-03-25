package com.example.teacher.snownew;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class DedMoroz {
    double x, y, vx;
    int sizeX, sizeY;
    int direction = -1; // 1 - вправо, -1 - влево
    ImageView image;
    DedMoroz(Activity activity){
        x=V.scrWidth/2;
        y=180*V.kS;
        vx=5*V.kS;
        sizeX = (int)(400*V.kS); //600x459
        sizeY = (int)(sizeX/(600/459.));
        image = new ImageView(activity);
        image.setImageResource(R.drawable.dedmoroz);
        activity.addContentView(image, new RelativeLayout.LayoutParams(sizeX,sizeY));
        outXY();
    }
    private void outXY(){
        image.setX((float)x-sizeX/2);
        image.setY((float)y-sizeY/2);
    }
    public void move(){
        x+=vx*direction;
        if(x<sizeX/2 || x>V.scrWidth-sizeX/2) {
            image.setScaleX(direction);
            direction=-direction;
        }
        outXY();
    }
}
