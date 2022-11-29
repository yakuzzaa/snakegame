package com.example.lab4game;

public class GameState {
    private int positionX;

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    private int positionY;

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    private float x1 = 0.0F;

    public float getX1() {
        return x1;
    }

    public void setX1(float x1) {
        this.x1 = x1;
    }

    private float y1 = 0.0f;

    public float getY1() {
        return y1;
    }

    public void setY1(float y1) {
        this.y1 = y1;
    }

    public long first_time;
    private String movingPossition = "right";

    public String getMovingPossition() {
        return movingPossition;
    }

    public void setMovingPossition(String movingPossition) {
        this.movingPossition = movingPossition;
    }

    private int scores = 0;

    public int getScores() {
        return scores;
    }

    public void setScores(int scores) {
        this.scores = scores;
    }

    public GameState() {
    }

    public void setFirst_time(long currentTimeMillis) {
        this.first_time = currentTimeMillis;
    }

    public long getFirst_time() {
        return this.first_time;
    }
}