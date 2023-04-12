package edu.northeastern.numad23sp_team26.pixel_pop;

import static android.content.Context.SENSOR_SERVICE;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.numad23sp_team26.R;
import edu.northeastern.numad23sp_team26.pixel_pop.models.PixelCell;

public class DrawView extends View implements SensorEventListener{

    private TextView xTextView, yTextView, zTextView;
    private SensorManager sensorManager;
    private Sensor accelerometerSensor;

    private boolean isAccelerometerSensorAvailable;
    private float currentX, currentY, currentZ, lastX, lastY, lastZ;

    /*************/

    private Paint strokeBrush = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint thickStrokeBrush = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint fillBrush = new Paint(Paint.ANTI_ALIAS_FLAG);
    private List<PixelCell> pixelCells;
    private final int NUM_LINES = 16;
    private int maxCoordinate;
    private float cellDim;
    private int fillBrushColor;

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);

        strokeBrush.setColor(Color.GRAY);
        strokeBrush.setStyle(Paint.Style.STROKE);
        strokeBrush.setStrokeJoin(Paint.Join.ROUND);
        strokeBrush.setStrokeWidth(1f);

        thickStrokeBrush.setColor(Color.BLACK);
        thickStrokeBrush.setStyle(Paint.Style.STROKE);
        thickStrokeBrush.setStrokeJoin(Paint.Join.ROUND);
        thickStrokeBrush.setStrokeWidth(1f);

        fillBrushColor = Color.BLACK;

        pixelCells = new ArrayList<>();
    }

    public void changeFillColor(int color) {
        fillBrushColor = color;
    }

    public void resetFills() {
        pixelCells.forEach(PixelCell::reset);
        postInvalidate();

        /*************/

        xTextView = findViewById(R.id.xTextView);
        yTextView = findViewById(R.id.yTextView);
        zTextView = findViewById(R.id.zTextView);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null){
            accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            isAccelerometerSensorAvailable = true;
        } else {
            xTextView.setText("Accelerometer sensor is not available");
            isAccelerometerSensorAvailable = false;
        }


        /*************/
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        maxCoordinate = getWidth();
        cellDim = (float)maxCoordinate / NUM_LINES;
        float center = (float)maxCoordinate / 2;

        if (pixelCells.isEmpty()) {
            fillPixelCells();
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
        return true;
    }

    private void fillPixelCells() {
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

    /*************/

    @Override
    public void onSensorChanged(SensorEvent event) {
        xTextView.setText((sensorEvent.values[0]+"m/s2"));
        yTextView.setText((sensorEvent.values[1]+"m/s2"));
        zTextView.setText((sensorEvent.values[2]+"m/s2"));

        currentX = sensorEvent.values[0];
        currentY = sensorEvent.values[1];
        currentZ = sensorEvent.values[2];

        lastX = currentX;
        lastY = currentY;
        lastZ = currentZ;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isAccelerometerSensorAvailable)
            sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(isAccelerometerSensorAvailable)
            sensorManager.unregisterListener(this);

    }

    /*************/

}
