package com.example.teacher.snownew;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class MainActivity extends AppCompatActivity {
    RelativeLayout screen;
    DedMoroz dedMoroz;
    Snegurochka snegurochka;
    ArrayList<Toy> toy;
    TextView textScore, textTime;
    long timeFromStartGame = 0;
    boolean readyToDropNextToy = true;
    boolean pause = false;
    private int score = 0;
    private SoundPool soundPool;
    private Map<Integer, Integer> soundMap;
    public static final int BROKEN_GLASS = 0;
    public static final int SCREAM_SNEG[] = {1,2,3,4,5};
    private float volume = 1, rate = 1;
    private int loop = 0, priority = 1;
    private int brokenToys = 0;
    public Gamer gamers[] = new Gamer[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setFullScreenLandscape();
        getDimensionsOfScreen();
        initializeSound(this);
        createTextFields();
        screen = (RelativeLayout)findViewById(R.id.screen);
        screen.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                onTouchScreen(event);
                return true;
            }
        });
        dedMoroz = new DedMoroz(this);
        snegurochka = new Snegurochka(this);
        for(int i=0; i<10; i++) gamers[i] = new Gamer();
        toy = new ArrayList<>();
        if(V.canToLoadGame) loadGame();
        MyTimer timer = new MyTimer(Long.MAX_VALUE, 5);
        timer.start();
    }
    @Override
    protected void onStart() {
        super.onStart();
        pause = false;
    }
    @Override
    protected void onPause() {
        super.onPause();
        pause = true;
    }
    @Override
    protected void onStop() {
        super.onStop();
        saveGame();
    }
    private void onTouchScreen(MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            V.touchScreenX = (int)event.getX();
            snegurochka.go = true;
            if(snegurochka.x < event.getX()) {
                if(snegurochka.direction != 1) {
                    snegurochka.direction = 1;
                    snegurochka.reverse();
                }
            }
            if(snegurochka.x > event.getX()){
                if(snegurochka.direction != -1) {
                    snegurochka.direction = -1;
                    snegurochka.reverse();
                }
            }
        }
    }
    private void initializeSound(Context context){
        int MAX_STREAMS = 4;
        int SOUND_QUALITY = 100;
        int priority = 1;
        soundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, SOUND_QUALITY);
        soundMap = new HashMap<>();
        soundMap.put(BROKEN_GLASS, soundPool.load(context, R.raw.glass, priority));
        for(int i=0; i<5; i++)
            soundMap.put(SCREAM_SNEG[i], soundPool.load(context, R.raw.snegurochka01+i, priority));
    }
    private void setFullScreenLandscape(){
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }
    private void getDimensionsOfScreen(){
        V.scrWidth = getResources().getDisplayMetrics().widthPixels;
        V.scrHeight = getResources().getDisplayMetrics().heightPixels;
    }
    private void createTextFields(){
        textScore = new TextView(this);
        this.addContentView(textScore, new RelativeLayout.LayoutParams(V.scrWidth/2, V.scrHeight/2));
        textScore.setX((float)(20*V.kS));
        textScore.setY((float)(20*V.kS));
        textScore.setTextSize((float)(40*V.kS));
        textScore.setTextColor(Color.RED);
        textScore.setText("Score: ");

        textTime = new TextView(this);
        this.addContentView(textTime, new RelativeLayout.LayoutParams(V.scrWidth/2, V.scrHeight/2));
        textTime.setX((float)(20*V.kS));
        textTime.setY((float)(120*V.kS));
        textTime.setTextSize((float)(40*V.kS));
        textTime.setTextColor(Color.RED);
        textTime.setText("Time: ");
    }
    private void update(){
        dedMoroz.move();
        snegurochka.move();
        if(timeFromStartGame%5==0 && readyToDropNextToy) {
            toy.add(new Toy(this, dedMoroz.x));
            readyToDropNextToy = false;
        }
        if(timeFromStartGame %5==1 && !readyToDropNextToy) readyToDropNextToy = true;

        for(int i=0; i<toy.size(); i++)
            if(toy.get(i).status==Toy.LOST) {
                soundPool.play(soundMap.get(BROKEN_GLASS),volume,volume,priority,loop,rate);
                toy.remove(i);
                score--;
                brokenToys++;
            } else {
                toy.get(i).move();
                toy.get(i).catchToy(snegurochka);
                if(toy.get(i).status==Toy.CAUGHT){
                    soundPool.play(soundMap.get(SCREAM_SNEG[new Random().nextInt(5)]),volume,volume,priority,loop,3f);
                    toy.remove(i);
                    score+=10;
                }
            }
        textScore.setText("Score: "+score);
        if(brokenToys>=10) {
            showAlert();
            pause = true;
        }
    }

    private void saveGame(){
        SharedPreferences sp = getSharedPreferences(V.PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat("DM.x",(float)dedMoroz.x);
        editor.putFloat("DM.y",(float)dedMoroz.y);
        editor.putFloat("DM.vx",(float)dedMoroz.vx);
        editor.putInt("DM.direction",dedMoroz.direction);
        editor.putFloat("snegurochka.x",(float)snegurochka.x);
        editor.putFloat("snegurochka.y",(float)snegurochka.y);
        editor.putFloat("snegurochka.vx",(float)snegurochka.vx);
        editor.putInt("snegurochka.direction",snegurochka.direction);
        editor.putInt("score",score);
        editor.putInt("BrokenToys",brokenToys);
        editor.putInt("Toy.size",toy.size());
        for(int i=0; i<toy.size(); i++){
            editor.putFloat("Toy.x"+i,(float)toy.get(i).x);
            editor.putFloat("Toy.y"+i,(float)toy.get(i).y);
            editor.putFloat("Toy.vy"+i,(float)toy.get(i).vy);
        }
        editor.apply();
    }
    private void loadGame(){
        SharedPreferences sp = getSharedPreferences(V.PREFERENCES, Context.MODE_PRIVATE);
        dedMoroz.x = sp.getFloat("DM.x",0);
        dedMoroz.y = sp.getFloat("DM.y",0);
        dedMoroz.vx = sp.getFloat("DM.vx",0);
        dedMoroz.direction = sp.getInt("snegurochka.direction",0);
        snegurochka.x = sp.getFloat("snegurochka.x",0);
        snegurochka.y = sp.getFloat("snegurochka.y",0);
        snegurochka.vx = sp.getFloat("snegurochka.vx",0);
        snegurochka.direction = sp.getInt("snegurochka.direction",0);
        score = sp.getInt("score",0);
        brokenToys = sp.getInt("BrokenToys",0);
        int n = sp.getInt("Toy.size",0);
        for(int i=0; i<n; i++){
            toy.add(new Toy(this, 0));
            toy.get(i).x=sp.getFloat("Toy.x"+i,0);
            toy.get(i).y=sp.getFloat("Toy.y"+i,0);
            toy.get(i).vy=sp.getFloat("Toy.vy"+i,0);
        }
    }

    class MyTimer extends CountDownTimer {
        public MyTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if(!pause) {
                update();
                timeFromStartGame = (Long.MAX_VALUE - millisUntilFinished) / 300;
                int min = (int)(Long.MAX_VALUE - millisUntilFinished)/1000/60;
                int sec = (int)(Long.MAX_VALUE - millisUntilFinished)/1000 - min*60;
                textTime.setText("Time: "+min+":"+sec);
            }
        }

        @Override
        public void onFinish() {

        }
    }
    private void showAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Game Over"); //Заголовок диалога
        builder.setMessage("Вы играли, играли, и проиграли. Что дальше?"); //Текст диалога
        builder.setCancelable(false); //Запрет отмены диалога при касании в любую точку экрана
//Создание отрицательной кнопки
        builder.setNegativeButton("Выйти из игры", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                startActivity(new Intent(MainActivity.this, Highscore.class));
                finish();
            }
        });
//Создание положительной кнопки
        builder.setPositiveButton("Сыграть ещё", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                pause = false;
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                finish();
            }
        });

        AlertDialog alert = builder.create();
        alert.show(); //Показать диалог
    }
    class Gamer{
        String name = "Noname";
        int score = 0;
    }
}
