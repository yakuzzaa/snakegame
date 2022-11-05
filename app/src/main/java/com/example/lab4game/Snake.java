package com.example.lab4game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import java.util.List;

public class Snake  {
    private int positionX,positionY;

    public Snake(int positionX,int positionY){
        this.positionX = positionX;
        this.positionY = positionY;

    }

    public int getPositionX(){
        return positionX;
    }
    public void setPositionX(int positionX){
        this.positionX = positionX;
    }
    public int getPositionY(){
        return positionY;
    }
    public void setPositionY(int positionY){
        this.positionY = positionY;
    }


}


