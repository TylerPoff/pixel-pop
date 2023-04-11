package edu.northeastern.numad23sp_team26.pixel_pop.models;

import android.graphics.Color;

public class PixelCell implements Cloneable {

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

    @Override
    public PixelCell clone() {
        try {
            PixelCell clone = (PixelCell) super.clone();
            clone.rowNum = this.rowNum;
            clone.colNum = this.colNum;
            clone.left = this.left;
            clone.top = this.top;
            clone.right = this.right;
            clone.bottom = this.bottom;
            clone.color = this.color;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

}
