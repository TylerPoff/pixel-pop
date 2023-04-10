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

import android.util.Log;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import edu.northeastern.numad23sp_team26.pixel_pop.models.PixelCell;

public class DrawView extends View {

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

    // -----------------------------------------------------------------------------------------------------------------------------------------------


    public String pixelCellsToString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < pixelCells.size(); i++) {
            PixelCell cell = pixelCells.get(i);
            sb.append(String.format("%d = {PixelCell} ", i));
            sb.append("bottom = ").append(cell.getBottom()).append(", ");
            sb.append("colNum = ").append(cell.getColNum()).append(", ");
            sb.append("color = ").append(cell.getColor()).append(", ");
            sb.append("left = ").append(cell.getLeft()).append(", ");
            sb.append("right = ").append(cell.getRight()).append(", ");
            sb.append("rowNum = ").append(cell.getRowNum()).append(", ");
            sb.append("top = ").append(cell.getTop()).append("\n");
        }
        return sb.toString();
    }

    public String pixelCellsToStringJSON() {
        JSONArray jsonArray = new JSONArray();

        for (int i = 0; i < pixelCells.size(); i++) {
            PixelCell cell = pixelCells.get(i);
            JSONObject cellJson = new JSONObject();
            try {
                cellJson.put("index", i);
                cellJson.put("bottom", cell.getBottom());
                cellJson.put("colNum", cell.getColNum());
                cellJson.put("color", cell.getColor());
                cellJson.put("left", cell.getLeft());
                cellJson.put("right", cell.getRight());
                cellJson.put("rowNum", cell.getRowNum());
                cellJson.put("top", cell.getTop());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(cellJson);
        }
        return jsonArray.toString();
    }

    public void writeJSONToFile(String fileName, String json) {
        try {
            FileOutputStream fos = getContext().openFileOutput(fileName, Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            osw.write(json);
            osw.close();
            fos.close();
        } catch (IOException e) {
            Log.e("PixelDraw", "Error writing JSON to file", e);
        }
    }

    public int[][] getPixelGrid() {
        int[][] pixelGrid = new int[NUM_LINES][NUM_LINES];
        int i = 0;
        for (int row = 0; row < NUM_LINES; row++) {
            for (int col = 0; col < NUM_LINES; col++) {
                PixelCell cell = pixelCells.get(i);
                pixelGrid[row][col] = cell.getColor();
                i++;
            }
        }
        return pixelGrid;
    }

    public void setPixelGrid(int[][] pixelGrid) {
        int i = 0;
        for (int row = 0; row < NUM_LINES; row++) {
            for (int col = 0; col < NUM_LINES; col++) {
                PixelCell cell = pixelCells.get(i);
                cell.setColor(pixelGrid[row][col]);
                i++;
            }
        }
        postInvalidate();
    }


    public void savePixelGridToFile(String fileName) {
        int[][] pixelGrid = getPixelGrid();
        PixelGridState state = new PixelGridState(pixelGrid);
        Gson gson = new Gson();
        String json = gson.toJson(state);

        try {
            FileOutputStream fos = getContext().openFileOutput(fileName, Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            osw.write(json);
            osw.close();
            fos.close();
        } catch (IOException e) {
            Log.e("PixelDraw", "Error saving pixel grid to file", e);
        }
    }

    public void loadPixelGridFromFile(String fileName) {
        try {
            FileInputStream fis = getContext().openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            String json = sb.toString();
            Gson gson = new Gson();
            PixelGridState state = gson.fromJson(json, PixelGridState.class);
            setPixelGrid(state.getPixelGrid());

            br.close();
            isr.close();
            fis.close();
        } catch (IOException e) {
            Log.e("PixelDraw", "Error loading pixel grid from file", e);
        }
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------------
}
