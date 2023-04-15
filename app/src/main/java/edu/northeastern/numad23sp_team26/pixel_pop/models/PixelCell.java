package edu.northeastern.numad23sp_team26.pixel_pop.models;

import android.graphics.Color;

public class PixelCell {

    private int rowNum;
    private int colNum;
    private float left;
    private float top;
    private float right;
    private float bottom;
    private int color;

    public PixelCell(int rowNum, int colNum, float left, float top, float right, float bottom) {
        this.rowNum = rowNum;
        this.colNum = colNum;
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.color = Color.WHITE;
    }

    public void draw(int color) {
        this.color = color;
    }

    public void reset() {
        this.color = Color.WHITE;
    }

    public int getRowNum() {
        return rowNum;
    }

    public int getColNum() {
        return colNum;
    }

    public float getLeft() {
        return left;
    }

    public float getTop() {
        return top;
    }

    public float getRight() {
        return right;
    }

    public float getBottom() {
        return bottom;
    }

    public int getColor() {
        return color;
    }

}
