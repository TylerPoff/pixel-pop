package edu.northeastern.numad23sp_team26.pixel_pop.models;

public class PixelCellDisplay implements Cloneable {

    private int rowNum;
    private int colNum;
    private int color;

    public PixelCellDisplay(int rowNum, int colNum, int color) {
        this.rowNum = rowNum;
        this.colNum = colNum;
        this.color = color;
    }

    public int getRowNum() {
        return rowNum;
    }

    public int getColNum() {
        return colNum;
    }

    public int getColor() {
        return color;
    }

    @Override
    public PixelCellDisplay clone() {
        try {
            PixelCellDisplay clone = (PixelCellDisplay) super.clone();
            clone.rowNum = this.rowNum;
            clone.colNum = this.colNum;
            clone.color = this.color;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

}
