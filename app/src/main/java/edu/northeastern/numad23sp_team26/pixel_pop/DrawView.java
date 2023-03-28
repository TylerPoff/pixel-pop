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

import edu.northeastern.numad23sp_team26.pixel_pop.models.Coordinate;

public class DrawView extends View {

    private Paint strokeBrush = new Paint();
    private Paint fillBrush = new Paint();
    private List<Coordinate> touchedCoordinates;

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);

        strokeBrush.setAntiAlias(true);
        strokeBrush.setColor(Color.BLACK);
        strokeBrush.setStyle(Paint.Style.STROKE);
        strokeBrush.setStrokeJoin(Paint.Join.ROUND);
        strokeBrush.setStrokeWidth(3f);

        fillBrush.setAntiAlias(true);
        fillBrush.setColor(Color.BLACK);
        fillBrush.setStyle(Paint.Style.FILL);

        touchedCoordinates = new ArrayList<>();
    }

    // TODO: allow multiple colors
    public void changeFillColor(int color) {
        fillBrush.setColor(color);
    }

    public void resetFills() {
        touchedCoordinates.clear();
        postInvalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float numLines = 16f;
        int maxCoordinate = getWidth();
        float cellDim = maxCoordinate / numLines;

        float top = 0;
        for (int row = 0; row < numLines; row++) {
            float left = 0;
            for (int col = 0; col < numLines; col++) {
                // draw grid
                canvas.drawRect(left, top, left + cellDim, top + cellDim, strokeBrush);

                // draw fill
                // TODO: optimize this algorithm
                for (Coordinate c : touchedCoordinates) {
                    if (c.getX() > left && c.getX() < left + cellDim
                            && c.getY() > top && c.getY() < top + cellDim) {
                        canvas.drawRect(left, top, left + cellDim, top + cellDim, fillBrush);
                    }
                }

                left = (left + cellDim);
            }
            top = top + cellDim;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                touchedCoordinates.add(new Coordinate(x, y));
                break;
            case MotionEvent.ACTION_UP:
                // do something
                break;
            default:
                return false;
        }
        postInvalidate();
        return true;
    }
}
