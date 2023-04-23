package edu.northeastern.numad23sp_team26.pixel_pop;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.numad23sp_team26.pixel_pop.models.PixelCell;
import edu.northeastern.numad23sp_team26.pixel_pop.models.PixelCellDisplay;
import edu.northeastern.numad23sp_team26.pixel_pop.models.PixelMultiGame;

public class DrawView extends View {

    private final Paint strokeBrush = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint thickStrokeBrush = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint fillBrush = new Paint(Paint.ANTI_ALIAS_FLAG);
    private List<PixelCellDisplay> pixelCellsDisplay;
    private final List<PixelCell> pixelCells;
    private final int NUM_LINES = 16;
    private int fillBrushColor;
    private boolean isEditable = true;
    private String gameID;
    private String playMode;
    private String playerNum;
    private float center;
    private DatabaseReference databaseRef;

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);

        databaseRef = FirebaseDatabase.getInstance().getReference();

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

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public void setPlayMode(String playMode) {
        this.playMode = playMode;
    }

    public void setPlayerNum(String playerNum) {
        this.playerNum = playerNum;
    }

    public void changeFillColor(int color) {
        fillBrushColor = color;
    }

    public void resetAll() {
        pixelCells.forEach(PixelCell::reset);
        postInvalidate();
    }

    public void resetFills() {
        if (playMode != null && playMode.equalsIgnoreCase("MULTI")) {
            if (playerNum != null && playerNum.equalsIgnoreCase("p1")) {
                for (PixelCell c : pixelCells) {
                    if (c.getColNum() < 8) {
                        c.reset();
                    }
                }
            } else if (playerNum != null && playerNum.equalsIgnoreCase("p2")) {
                for (PixelCell c : pixelCells) {
                    if (c.getColNum() >= 8) {
                        c.reset();
                    }
                }
            }
        } else {
            pixelCells.forEach(PixelCell::reset);
        }
        saveMultiplayerGameDrawing();
        postInvalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int maxCoordinate = Math.min(getWidth(), getHeight());
        float cellDim = (float)maxCoordinate / NUM_LINES;
        center = (float)maxCoordinate / 2;

        if (pixelCells.isEmpty()) {
            addBlankPixelCells(cellDim);

            // Draw display pixels
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

        if (playMode != null && playMode.equalsIgnoreCase("MULTI")) {
            Paint multiPlayDividerBrush = new Paint(Paint.ANTI_ALIAS_FLAG);
            multiPlayDividerBrush.setColor(Color.MAGENTA);
            multiPlayDividerBrush.setStyle(Paint.Style.STROKE);
            multiPlayDividerBrush.setStrokeJoin(Paint.Join.ROUND);
            multiPlayDividerBrush.setStrokeWidth(3f);
            canvas.drawLine(center, 0, center, maxCoordinate, multiPlayDividerBrush);
        } else {
            canvas.drawLine(center, 0, center, maxCoordinate, thickStrokeBrush);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isEditable) {
            float x = event.getX();
            float y = event.getY();

            if (playMode != null && playMode.equalsIgnoreCase("MULTI")) {
                if (playerNum != null && playerNum.equalsIgnoreCase("p1") && x > center) {
                    return true;
                } else if (playerNum != null && playerNum.equalsIgnoreCase("p2") && x < center) {
                    return true;
                }
            }

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
                    saveMultiplayerGameDrawing();
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

    public List<PixelCellDisplay> getPixelCellsState() {
        List<PixelCellDisplay> currentPixelCells = new ArrayList<>();
        for (PixelCell c : pixelCells) {
            currentPixelCells.add(new PixelCellDisplay(c.getRowNum(), c.getColNum(), c.getColor()));
        }
        return currentPixelCells;
    }

    public void setIsEditable(boolean isEditable) {
        this.isEditable = isEditable;
    }

    public void listenMultiplayerGameDrawOnce() {
        if (gameID != null) {
            databaseRef.child("MultiplayerGames").child(gameID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    PixelMultiGame multiGame = snapshot.getValue(PixelMultiGame.class);
                    if (multiGame != null && multiGame.pixelCellsState != null) {
                        for (PixelCellDisplay status : multiGame.pixelCellsState) {
                            PixelCell pc = pixelCells.stream().filter(c -> c.getRowNum() == status.getRowNum() && c.getColNum() == status.getColNum()).findFirst().orElse(null);

                            if (pc != null) {
                                pc.draw(status.getColor());
                            }
                        }
                        postInvalidate();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public void listenMultiplayerGameDraw() {
        if (gameID != null) {
            databaseRef.child("MultiplayerGames").child(gameID).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if (snapshot.getKey() != null && !snapshot.getKey().equalsIgnoreCase("pixelCellsState")) {
                        return;
                    }
                    for (DataSnapshot statusSnapshot : snapshot.getChildren()) {
                        PixelCellDisplay status = statusSnapshot.getValue(PixelCellDisplay.class);
                        PixelCell pc = pixelCells.stream().filter(c -> c.getRowNum() == status.getRowNum() && c.getColNum() == status.getColNum()).findFirst().orElse(null);

                        if (pc != null) {
                            pc.draw(status.getColor());
                        }
                    }
                    postInvalidate();
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void saveMultiplayerGameDrawing() {
        if (gameID != null) {
            databaseRef.child("MultiplayerGames").child(gameID).runTransaction(new Transaction.Handler() {
                @NonNull
                @Override
                public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                    PixelMultiGame multiGame = currentData.getValue(PixelMultiGame.class);
                    if (multiGame == null) {
                        return Transaction.success(currentData);
                    }

                    multiGame.pixelCellsState = getPixelCellsState();
                    currentData.setValue(multiGame);

                    return Transaction.success(currentData);
                }

                @Override
                public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {

                }
            });
        }
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
