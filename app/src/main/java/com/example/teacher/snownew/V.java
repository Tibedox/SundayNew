package com.example.teacher.snownew;

public class V {
    public static int scrWidth, scrHeight;
    public static final double KOEFF_BUTTON_INTRO = 3.96;
    public static double kS = 1;
    public static int rateOfDropNewToy = 300; // чем больше число, тем реже выпадают игрушки
    public static boolean canToLoadGame = false;
    public static String PREFERENCES = "CurrentGame";
    public static int touchScreenX;

    public static void calculateCoefficientScreen(){
        kS = (scrWidth>scrHeight?scrWidth:scrHeight)/2560.;
    }
}
