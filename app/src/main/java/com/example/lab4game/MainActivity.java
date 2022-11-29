package com.example.lab4game;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MotionEventCompat;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.lab4game.room.AppDatabase;
import com.example.lab4game.room.ResultDAO;
import com.example.lab4game.room.ResultEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    public final GameState gameState = new GameState();
    private final List<Snake> SnakeList = new ArrayList<>();
    private SurfaceView surfaceView;
    private TextView score;

    private static final int pointSize = 28;
    private static final int defaultTalePoints = 3;
    private static final int snakeColor = Color.RED;
    private static final int yColor = Color.YELLOW;
    private static final int snakeMovingSpeed = 800;

    private Timer timer;
    private Canvas canvas = null;
    private Paint pointColor = null;
    private Paint objectColor = null;
    private SurfaceHolder surfaceHolder;



    AppDatabase db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        surfaceView = findViewById(R.id.surfaceView);
        score = findViewById(R.id.score);
        surfaceView.getHolder().addCallback(this);
        this.db = Room.databaseBuilder(this, AppDatabase.class, "scores").build();

    }

    public boolean onTouchEvent(MotionEvent event){

        int action = MotionEventCompat.getActionMasked(event);
        switch(action) {

            case (MotionEvent.ACTION_DOWN) :
                this.gameState.setX1((int) event.getX());
                this.gameState.setY1((int) event.getY());
                Log.d("TEST", "X1 Y1 is " + this.gameState.getX1() + " " + this.gameState.getY1());
                break;
            case (MotionEvent.ACTION_UP) :
                int x2 = (int) event.getX();
                int y2 = (int) event.getY();
                if (Math.abs(gameState.getX1() -x2) > Math.abs(gameState.getY1() -y2)) {
                    if(gameState.getX1() > x2) {
                        gameState.setMovingPossition("left");
                    }
                    else{
                        gameState.setMovingPossition("right");
                }
                }
                else {
                    if(gameState.getY1() > y2) {
                        gameState.setMovingPossition("top");
                    }
                    else{
                        gameState.setMovingPossition("bottom");
                    }

                }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
        init();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }

    private void init(){
        SnakeList.clear();
        score.setText("0");
        gameState.setScores(0);
        gameState.setMovingPossition("right");
        int startPositionX = (pointSize)*defaultTalePoints;

        for (int i = 0;i < defaultTalePoints; i++){
            Snake snake = new Snake(startPositionX,pointSize);
            SnakeList.add(snake);

            startPositionX = startPositionX - (pointSize * 2);

        }
        addPoint();
        moveSnake();
        gameState.setFirst_time(System.currentTimeMillis());
    }

    private void addPoint(){
        int surfaceWidth = surfaceView.getWidth() - (pointSize*2);
        int surfaceHeight = surfaceView.getHeight() - (pointSize*2);

        int randomXPosition = new Random().nextInt(surfaceWidth / pointSize);
        int randomYPosition = new Random().nextInt(surfaceHeight / pointSize);

        if ((randomXPosition % 2) != 0){
            randomXPosition = randomXPosition + 1;

        }
        if ((randomYPosition % 2) != 0){
            randomYPosition = randomYPosition + 1;

        }
        gameState.setPositionX((pointSize * randomXPosition) + pointSize);
        gameState.setPositionY((pointSize * randomYPosition) + pointSize);
    }
    private void moveSnake(){
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                int headPositionX = SnakeList.get(0).getPositionX();
                int headPositionY = SnakeList.get(0).getPositionY();

                if(headPositionX == gameState.getPositionX() && gameState.getPositionY() == headPositionY){
                    growSnake();
                    addPoint();
                }
                switch (gameState.getMovingPossition()){
                    case "right":
                        SnakeList.get(0).setPositionX(headPositionX + (pointSize*2));
                        SnakeList.get(0).setPositionY(headPositionY);
                        break;
                    case "left":
                        SnakeList.get(0).setPositionX(headPositionX - (pointSize*2));
                        SnakeList.get(0).setPositionY(headPositionY);
                        break;
                    case "top":
                        SnakeList.get(0).setPositionX(headPositionX);
                        SnakeList.get(0).setPositionY(headPositionY - (pointSize*2));
                        break;
                    case "bottom":
                        SnakeList.get(0).setPositionX(headPositionX);
                        SnakeList.get(0).setPositionY(headPositionY + (pointSize*2));
                        break;
                }

                if (checkGameOver(headPositionX,headPositionY)){
                    timer.purge();
                    timer.cancel();

                    Context context;
                    ResultDAO resultDAO = db.resultDAO();
                    resultDAO.insert(new ResultEntity(gameState.getScores(), gameState.getFirst_time(),((System.currentTimeMillis() - gameState.getFirst_time())/1000)));
                    Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                    startActivity(intent);
                }
                else{
                    canvas = surfaceHolder.lockCanvas();
                    canvas.drawColor(Color.WHITE, PorterDuff.Mode.CLEAR);
                    canvas.drawCircle(SnakeList.get(0).getPositionX(),SnakeList.get(0).getPositionY(),pointSize,createPointColor());
                    canvas.drawCircle(gameState.getPositionX(), gameState.getPositionY(),pointSize,objectPaintColor());
                    for(int i = 1; i<SnakeList.size();i++){
                        int getTempPositionX = SnakeList.get(i).getPositionX();
                        int getTempPositionY = SnakeList.get(i).getPositionY();
                        SnakeList.get(i).setPositionX(headPositionX);
                        SnakeList.get(i).setPositionY(headPositionY);
                        canvas.drawCircle(SnakeList.get(i).getPositionX(),SnakeList.get(i).getPositionY(),pointSize,createPointColor());
                        headPositionX = getTempPositionX;
                        headPositionY = getTempPositionY;
                    }
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }

        },1000-snakeMovingSpeed,1000-snakeMovingSpeed);
    }
    private void growSnake(){
        Snake snake = new Snake(0,0);
        SnakeList.add(snake);
        gameState.setScores(gameState.getScores() + 1);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                score.setText(String.valueOf(gameState.getScores()));
            }
        });

    }
    private boolean checkGameOver(int headPositionX,int headPositionY){
        boolean gameOver = false;
        if(SnakeList.get(0).getPositionX() < 0 || SnakeList.get(0).getPositionY() <0 ||
                SnakeList.get(0).getPositionX() >=surfaceView.getWidth() ||
                SnakeList.get(0).getPositionY() >=surfaceView.getHeight()){
            gameOver = true;
        }
        else{
            for (int i = 1; i < SnakeList.size();i++){
                if (headPositionX == SnakeList.get(i).getPositionX() &&
                        headPositionY == SnakeList.get(i).getPositionY()) {
                    gameOver = true;
                    break;
                }
            }
        }
        return gameOver;
    }
    private Paint createPointColor(){
        if (pointColor == null){
            pointColor = new Paint();
            pointColor.setColor(snakeColor);
            pointColor.setStyle(Paint.Style.FILL);
            pointColor.setAntiAlias(true);
        }
        return pointColor;

    }
    private Paint objectPaintColor(){
        if (objectColor == null){
            objectColor = new Paint();
            objectColor.setColor(yColor);
            objectColor.setStyle(Paint.Style.FILL);
            objectColor.setAntiAlias(true);
        }
        return objectColor;

    }
}