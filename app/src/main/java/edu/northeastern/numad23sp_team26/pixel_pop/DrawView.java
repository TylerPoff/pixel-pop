package edu.northeastern.numad23sp_team26.pixel_pop;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.numad23sp_team26.pixel_pop.models.PixelCell;
import edu.northeastern.numad23sp_team26.pixel_pop.models.PixelCellDisplay;

public class DrawView extends View {

    private final Paint strokeBrush = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint thickStrokeBrush = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint fillBrush = new Paint(Paint.ANTI_ALIAS_FLAG);
    private List<PixelCellDisplay> pixelCellsDisplay;
    private final List<PixelCell> pixelCells;
    private final int NUM_LINES = 16;
    private int fillBrushColor;
    private boolean isEditable = true;

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);

        strokeBrush.setColor(Color.GRAY);
        strokeBrush.setStyle(Paint.Style.STROKE);
        strokeBrush.setStrokeJoin(Paint.Join.ROUND);
        strokeBrush.setStrokeWidth(1.5f);

        thickStrokeBrush.setColor(Color.BLACK);
        thickStrokeBrush.setStyle(Paint.Style.STROKE);
        thickStrokeBrush.setStrokeJoin(Paint.Join.ROUND);
        thickStrokeBrush.setStrokeWidth(1.5f);

        fillBrushColor = Color.BLACK;

        pixelCellsDisplay = new ArrayList<>();
        pixelCells = new ArrayList<>();
    }

    public void changeFillColor(int color) {
        fillBrushColor = color;
    }

    public void resetFills() {
        pixelCells.forEach(PixelCell::reset);
        postInvalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int maxCoordinate = Math.min(getWidth(), getHeight());
        float cellDim = (float)maxCoordinate / NUM_LINES;
        float center = (float)maxCoordinate / 2;

        if (pixelCells.isEmpty()) {
            addBlankPixelCells(cellDim);

            // TODO: change this for the game
            if (!pixelCellsDisplay.isEmpty()) {
                drawPixelCellsDisplay();
            }
        }

        float top = 0;
        for (int row = 0; row < NUM_LINES; row++) {
            float left = 0;

            for (int col = 0; col < NUM_LINES; col++) {

                float right = left + cellDim;
                float bottom = top + cellDim;

                // draw grid
                canvas.drawRect(left, top, right, bottom, strokeBrush);

                // draw fill
                int index = row * NUM_LINES + col;
                fillBrush.setColor(pixelCells.get(index).getColor());
                fillBrush.setStyle(Paint.Style.FILL);
                canvas.drawRect(left, top, right, bottom, fillBrush);

                left = (left + cellDim);
            }
            top = top + cellDim;
        }

        canvas.drawLine(0, center, maxCoordinate, center, thickStrokeBrush);
        canvas.drawLine(center, 0, center, maxCoordinate, thickStrokeBrush);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isEditable) {
            float x = event.getX();
            float y = event.getY();

            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    for (PixelCell c : pixelCells) {
                        if (x > c.getLeft() && x < c.getRight() && y > c.getTop() && y < c.getBottom()) {
                            c.draw(fillBrushColor);
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    // do something
                    break;
                default:
                    return false;
            }
            postInvalidate();
        }
        return true;
    }

    public List<PixelCellDisplay> getPixelCellsDisplay() {
        if (!pixelCellsDisplay.isEmpty()) {
            return pixelCellsDisplay;
        }

        for (PixelCell c : pixelCells) {
            pixelCellsDisplay.add(new PixelCellDisplay(c.getRowNum(), c.getColNum(), c.getColor()));
        }
        return pixelCellsDisplay;
    }

    public void setPixelCellsDisplay(List<PixelCellDisplay> pcd) {
        pixelCellsDisplay = new ArrayList<>();
        for (PixelCellDisplay c : pcd) {
            pixelCellsDisplay.add(c.clone());
        }
    }

    public void setIsEditable(boolean isEditable) {
        this.isEditable = isEditable;
    }

    private void addBlankPixelCells(float cellDim) {
        float top = 0;
        for (int row = 0; row < NUM_LINES; row++) {
            float left = 0;

            for (int col = 0; col < NUM_LINES; col++) {
                pixelCells.add(new PixelCell(row, col, left, top, left + cellDim, top + cellDim));
                left = (left + cellDim);
            }
            top = top + cellDim;
        }
    }

    private void drawPixelCellsDisplay() {
        if (pixelCells.size() == pixelCellsDisplay.size()) {
            for (PixelCell c : pixelCells) {
                PixelCellDisplay currentCellDisplay = pixelCellsDisplay.stream().filter(item -> item.getRowNum() == c.getRowNum() && item.getColNum() == c.getColNum()).findFirst().orElse(null);
                if (currentCellDisplay != null) {
                    c.draw(currentCellDisplay.getColor());
                } else {
                    c.draw(Color.WHITE);
                }
            }
        }
    }

}
