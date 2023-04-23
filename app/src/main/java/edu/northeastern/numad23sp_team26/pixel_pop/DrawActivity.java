package edu.northeastern.numad23sp_team26.pixel_pop;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.squareup.seismic.ShakeDetector;

import edu.northeastern.numad23sp_team26.R;
import edu.northeastern.numad23sp_team26.pixel_pop.models.PixelImage;
import edu.northeastern.numad23sp_team26.pixel_pop.models.PixelMultiGame;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DrawActivity extends MultiPlayCommonActivity implements ShakeDetector.Listener {

    private static final String TAG = "pixel_pop.DrawActivity";
    private DrawView drawView;
    private Gson gson;
    private ArrayList<Integer> colorList;
    private TextView displayTimer;
    private TextView memorizeTV;
    private ConstraintLayout drawPalette;
    private Handler handler = new Handler();
    private String adventure;
    private int levelNum;
    private int maxLevels;
    private SwitchMaterial shakeToEraseSwitch;
    private SensorManager sensorManager;
    private boolean shouldShake = false;
    private ShakeDetector shakeDetector;
    private int noCount = 0;
    private Button skipBtn;
    private DisplayTimerThread displayTimerThread;
    private boolean paused = false;
    private MediaPlayer player;
    private String playMode;
    private String playerNum;
    private String gameID;
    private String otherPlayerID;
    private boolean isDone = false;
    private boolean isOpenPauseMenu;
    private AlertDialog pauseDialog;
    private ChildEventListener multiPlayGamesDoneChildListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            PixelMultiGame multiGame = snapshot.getValue(PixelMultiGame.class);
            if (multiGame != null && multiGame.gameID.equalsIgnoreCase(multiPlayGameID)) {
                if (multiGame.isPlayerOneDone == 1) {
                    if (playerNum.equalsIgnoreCase("p2")) {
                        handleDone();
                    }
                } else if (multiGame.isPlayerTwoDone == 1) {
                    if (playerNum.equalsIgnoreCase("p1")) {
                        Button doneBtn = findViewById(R.id.doneBtn);
                        doneBtn.setEnabled(true);
                    }
                }
            }
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            PixelMultiGame multiGame = snapshot.getValue(PixelMultiGame.class);
            if (multiGame != null && multiGame.gameID.equalsIgnoreCase(multiPlayGameID)) {
                createDisconnectedAlertDialog();
            }
        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);

        musicPlay();

        drawView = findViewById(R.id.drawView);
        displayTimer = findViewById(R.id.displayTimer);
        memorizeTV = findViewById(R.id.memorizeTV);
        drawPalette = findViewById(R.id.drawPalette);

        skipBtn = findViewById(R.id.skipBtn);
        skipBtn.setOnClickListener(v -> {
            if (displayTimerThread != null) {
                displayTimerThread.interrupt();
            }
        });

        shakeToEraseSwitch = findViewById(R.id.switchShakeErase);
        shakeToEraseSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            shouldShake = isChecked;
            if (shouldShake) {
                Toast.makeText(this, "Shake to reset enabled", Toast.LENGTH_SHORT).show();
                shakeDetector = new ShakeDetector(this);
                sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
                shakeDetector.start(sensorManager, SensorManager.SENSOR_DELAY_GAME);
            } else {
                Toast.makeText(this, "Shake to reset disabled", Toast.LENGTH_SHORT).show();
                if (shakeDetector != null) {
                    shakeDetector.stop();
                }
            }
        });

        Button quitBtn = findViewById(R.id.quitBtn);
        quitBtn.setOnClickListener(v -> createQuitAlertDialog());

        Button pauseBtn = findViewById(R.id.pauseBtn);
        pauseBtn.setOnClickListener(v -> createPauseAlertDialog());

        Button whiteColorBtn = findViewById(R.id.whiteColorBtn);
        whiteColorBtn.setOnClickListener(v -> drawView.changeFillColor(getColor(R.color.white)));

        ImageButton eraserBtn = findViewById(R.id.eraserBtn);
        eraserBtn.setOnClickListener(v -> drawView.changeFillColor(getColor(R.color.white)));

        Button resetBtn = findViewById(R.id.resetBtn);
        resetBtn.setOnClickListener(v -> createResetAlertDialog());

        TextView waitingTV = findViewById(R.id.waitingTV);

        Button doneBtn = findViewById(R.id.doneBtn);
        doneBtn.setOnClickListener(v -> {
            if (playMode.equalsIgnoreCase("MULTI")) {
                if (playerNum.equalsIgnoreCase("p1")) {
                    notifyDone(1);
                } else {
                    notifyDone(2);
                    shakeToEraseSwitch.setVisibility(View.INVISIBLE);
                    shakeToEraseSwitch.setEnabled(false);
                    skipBtn.setVisibility(View.INVISIBLE);
                    skipBtn.setEnabled(false);
                    memorizeTV.setVisibility(View.INVISIBLE);
                    drawPalette.setVisibility(View.INVISIBLE);
                    drawView.setIsEditable(false);
                    resetBtn.setEnabled(false);
                    doneBtn.setEnabled(false);
                    waitingTV.setVisibility(View.VISIBLE);
                }
            } else {
                handleDone();
            }
        });

        TextView p1TV = findViewById(R.id.p1TV);
        TextView p2TV = findViewById(R.id.p2TV);

        gson = new Gson();

        if (getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            adventure = extras.getString("adventure");
            levelNum = extras.getInt("levelNum");
            maxLevels = extras.getInt("maxLevels");
            colorList = extras.getIntegerArrayList("colorList");
            playMode = extras.getString("playMode");
            playerNum = extras.getString("playerNum");
            gameID = extras.getString("gameID");

            drawView.setPlayMode(playMode);

            if (playMode.equalsIgnoreCase("MULTI")) {
                drawView.setGameID(gameID);
                pauseBtn.setVisibility(View.INVISIBLE);
                pauseBtn.setEnabled(false);
                if (playerNum.equalsIgnoreCase("p1")) {
                    p2TV.setBackgroundResource(android.R.color.transparent);
                    doneBtn.setEnabled(false);
                } else {
                    p1TV.setBackgroundResource(android.R.color.transparent);
                }
                drawView.setPlayerNum(playerNum);
            } else {
                quitBtn.setVisibility(View.INVISIBLE);
                quitBtn.setEnabled(false);
                p1TV.setVisibility(View.GONE);
                p2TV.setVisibility(View.GONE);
            }

            setPixelImageProperties();

            /*
            Button logBtn = findViewById(R.id.logBtn);
            logBtn.setOnClickListener(v -> {
                PixelImage currentImage = new PixelImage(adventure, levelNum, 30, 600, drawView.getPixelCellsDisplay());
                String jsonToLog =  gson.toJson(currentImage);
                int chunkCount = jsonToLog.length() / 4000;
                for (int i = 0; i <= chunkCount; i++) {
                    int max = 4000 * (i + 1);
                    if (max >= jsonToLog.length()) {
                        Log.v("", jsonToLog.substring(4000 * i));
                    } else {
                        Log.v("", jsonToLog.substring(4000 * i, max));
                    }
                }
            });
            */
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isOpenPauseMenu = true;
        musicPlay();
        if (shouldShake) {
            shakeDetector = new ShakeDetector(this);
            sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
            shakeDetector.start(sensorManager, SensorManager.SENSOR_DELAY_GAME);
        }
    }

    @Override
    protected void onPause() {
        musicStop();
        databaseRef.child("MultiplayerGames").removeEventListener(multiPlayGamesDoneChildListener);

        if (shakeDetector != null) {
            shakeDetector.stop();
        }
        if (playMode.equalsIgnoreCase("SINGLE")) {
            if (displayTimerThread != null) {
                displayTimerThread.pauseTimer();
            }

            if (isOpenPauseMenu) {
                if (pauseDialog == null || !pauseDialog.isShowing()) {
                    createPauseAlertDialog();
                }
            }
        }
        isPlaying = isDone;
        super.onPause();
    }

    @Override
    public void hearShake() {
        if (shakeDetector != null) {
            shakeDetector.stop();
        }
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this)
                .setOnCancelListener(d -> {
                    shakeDetector = new ShakeDetector(this);
                    sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
                    shakeDetector.start(sensorManager, SensorManager.SENSOR_DELAY_GAME);
                });
        final View ShakeResetPopupView = getLayoutInflater().inflate(R.layout.reset_popup, null);
        Button noBtn = ShakeResetPopupView.findViewById(R.id.noBtn);
        Button yesBtn = ShakeResetPopupView.findViewById(R.id.yesBtn);
        dialogBuilder.setView(ShakeResetPopupView);
        AlertDialog dialog = dialogBuilder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        noBtn.setOnClickListener(v -> {
            dialog.dismiss();
            // Add to the no count
            noCount++;
            if (noCount == 3) {
                // Reset noCount
                noCount = 0;
                shouldShake = false;
                shakeToEraseSwitch.setChecked(false);
                // If the shake detector is active, stop it
                if (shakeDetector != null) {
                    shakeDetector.stop();
                }

                showSimpleDialog();
            } else {
                shakeDetector = new ShakeDetector(this);
                sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
                shakeDetector.start(sensorManager, SensorManager.SENSOR_DELAY_GAME);
            }
        });
        yesBtn.setOnClickListener(v -> {
            dialog.dismiss();
            drawView.resetFills();
            shakeDetector = new ShakeDetector(this);
            sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
            shakeDetector.start(sensorManager, SensorManager.SENSOR_DELAY_GAME);

            // A yes should reset the no count
            noCount = 0;
        });
    }

    private void  showSimpleDialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final View ShakeDisabledPopupView = getLayoutInflater().inflate(R.layout.shake_disabled_popup, null);
        Button okBtn = ShakeDisabledPopupView.findViewById(R.id.okBtn);
        dialogBuilder.setView(ShakeDisabledPopupView);
        AlertDialog dialog = dialogBuilder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        okBtn.setOnClickListener(v -> dialog.dismiss());
    }

    @Override
    public void onBackPressed() {
        createQuitAlertDialog();
    }

    @Override
    protected void multiPlayGamesListener() {
        databaseRef.child("MultiplayerGames").addChildEventListener(multiPlayGamesDoneChildListener);
    }

    private void setPixelImageProperties() {
        Button colorBtn1 = findViewById(R.id.colorBtn1);
        Button colorBtn2 = findViewById(R.id.colorBtn2);
        Button colorBtn3 = findViewById(R.id.colorBtn3);
        Button colorBtn4 = findViewById(R.id.colorBtn4);
        Button colorBtn5 = findViewById(R.id.colorBtn5);
        Button colorBtn6 = findViewById(R.id.colorBtn6);
        Button colorBtn7 = findViewById(R.id.colorBtn7);
        Button colorBtn8 = findViewById(R.id.colorBtn8);

        colorBtn1.getBackground().setTint(colorList.get(0));
        colorBtn2.getBackground().setTint(colorList.get(1));
        colorBtn3.getBackground().setTint(colorList.get(2));
        colorBtn4.getBackground().setTint(colorList.get(3));
        colorBtn5.getBackground().setTint(colorList.get(4));
        colorBtn6.getBackground().setTint(colorList.get(5));
        colorBtn7.getBackground().setTint(colorList.get(6));
        colorBtn8.getBackground().setTint(colorList.get(7));

        colorBtn1.setOnClickListener(v -> drawView.changeFillColor(colorList.get(0)));
        colorBtn2.setOnClickListener(v -> drawView.changeFillColor(colorList.get(1)));
        colorBtn3.setOnClickListener(v -> drawView.changeFillColor(colorList.get(2)));
        colorBtn4.setOnClickListener(v -> drawView.changeFillColor(colorList.get(3)));
        colorBtn5.setOnClickListener(v -> drawView.changeFillColor(colorList.get(4)));
        colorBtn6.setOnClickListener(v -> drawView.changeFillColor(colorList.get(5)));
        colorBtn7.setOnClickListener(v -> drawView.changeFillColor(colorList.get(6)));
        colorBtn8.setOnClickListener(v -> drawView.changeFillColor(colorList.get(7)));

        List<PixelImage> pixelImages = loadPixelImagesFromFile("pixelImages.json");
        PixelImage imageToDisplay = pixelImages.stream().filter(pixelImage -> pixelImage.getAdventure().equalsIgnoreCase(adventure) && pixelImage.getLevelNum() == levelNum).findFirst().orElse(null);
        if (imageToDisplay != null) {
            shakeToEraseSwitch.setVisibility(View.INVISIBLE);
            shakeToEraseSwitch.setEnabled(false);
            skipBtn.setVisibility(View.VISIBLE);
            skipBtn.setEnabled(true);
            memorizeTV.setVisibility(View.VISIBLE);
            drawPalette.setVisibility(View.INVISIBLE);
            drawView.setIsEditable(false);
            drawView.setPixelCellsDisplay(imageToDisplay.getPixelCellsDisplay());
            displayTimer.setText("" + imageToDisplay.getDisplaySecondsTimer());
            displayTimerThread = new DisplayTimerThread(imageToDisplay.getDisplaySecondsTimer());
            displayTimerThread.start();
        }
    }

    private void createResetAlertDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final View ResetPopupView = getLayoutInflater().inflate(R.layout.reset_popup, null);
        Button noBtn = ResetPopupView.findViewById(R.id.noBtn);
        Button yesBtn = ResetPopupView.findViewById(R.id.yesBtn);
        dialogBuilder.setView(ResetPopupView);
        AlertDialog dialog = dialogBuilder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        noBtn.setOnClickListener(v -> dialog.dismiss());
        yesBtn.setOnClickListener(v -> {
            dialog.dismiss();
            drawView.resetFills();
        });
    }

    private void handleDone() {
        isDone = true;
        Intent intent = new Intent (getApplicationContext(), ResultsActivity.class);

        Bundle extras = new Bundle();
        extras.putString("adventure", adventure);
        extras.putInt("levelNum", levelNum);
        extras.putInt("maxLevels", maxLevels);
        extras.putIntegerArrayList("colorList", colorList);
        extras.putParcelableArrayList("originalPixels", new ArrayList<>(drawView.getPixelCellsDisplay()));
        extras.putParcelableArrayList("drawnPixels", new ArrayList<>(drawView.getPixelCellsState()));
        extras.putString("playMode", playMode);
        extras.putString("gameID", multiPlayGameID);
        extras.putString("otherPlayerID", otherPlayerID);
        intent.putExtras(extras);

        isOpenPauseMenu = false;
        startActivity(intent);
        finish();
    }

    private void notifyDone(int playerNum) {
        databaseRef.child("MultiplayerGames").child(gameID).runTransaction(new Transaction.Handler() {

            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                PixelMultiGame multiGame = currentData.getValue(PixelMultiGame.class);
                if (multiGame == null) {
                    return Transaction.success(currentData);
                }

                if (playerNum == 1) {
                    multiGame.isPlayerOneDone = 1;
                    currentData.setValue(multiGame);
                    otherPlayerID = multiGame.playerTwoID;
                    handleDone();
                } else if (playerNum == 2) {
                    multiGame.isPlayerTwoDone = 1;
                    currentData.setValue(multiGame);
                    otherPlayerID = multiGame.playerOneID;
                }

                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {

            }
        });
    }

    private List<PixelImage> loadPixelImagesFromFile(String fileName) {
        try {
            InputStreamReader isr = new InputStreamReader(getAssets().open(fileName));
            BufferedReader br = new BufferedReader(isr);

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            String json = sb.toString();
            List<PixelImage> pixelImages = gson.fromJson(json, new TypeToken<List<PixelImage>>(){}.getType());

            br.close();
            isr.close();

            return pixelImages;
        } catch (IOException e) {
            String msg = "Error loading pixel images from file";
            Log.e("DrawActivity", msg, e);
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            return new ArrayList<>();
        }
    }

    private void createQuitAlertDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final View QuitPopupView = getLayoutInflater().inflate(R.layout.quit_level_popup, null);
        Button cancelBtn = QuitPopupView.findViewById(R.id.cancelBtn);
        Button quitBtn = QuitPopupView.findViewById(R.id.quitBtn);
        dialogBuilder.setView(QuitPopupView);
        AlertDialog dialog = dialogBuilder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        cancelBtn.setOnClickListener(v -> {
            dialog.dismiss();
        });
        quitBtn.setOnClickListener(v -> {
            dialog.dismiss();
            isPlaying = false;
            isOpenPauseMenu = false;
            finish();
        });
    }

    private void createPauseAlertDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final View PausePopupView = getLayoutInflater().inflate(R.layout.pause_menu_popup, null);
        Button resumeBtn = PausePopupView.findViewById(R.id.pause_menu_resume_btn);
        Button quitBtn = PausePopupView.findViewById(R.id.pause_menu_quit_btn);
        dialogBuilder.setView(PausePopupView);
        pauseDialog = dialogBuilder.create();
        pauseDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        pauseDialog.setOnDismissListener(dialogInterface -> {
            if (paused) {
                displayTimerThread.resumeTimer();
            }
        });
        pauseDialog.show();
        displayTimerThread.pauseTimer();
        resumeBtn.setOnClickListener(v -> {
            displayTimerThread.resumeTimer();
            pauseDialog.dismiss();
        });
        quitBtn.setOnClickListener(v -> {
            pauseDialog.dismiss();
            isOpenPauseMenu = false;
            finish();
        });
    }

    private class DisplayTimerThread extends Thread {

        private int displaySecondsTimer;
        private final Object lock = new Object();

        public DisplayTimerThread(int displaySecondsTimer) {
            this.displaySecondsTimer = displaySecondsTimer;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < displaySecondsTimer; i++) {
                    Thread.sleep(1000);
                    if (paused) {
                        synchronized (lock) {
                            lock.wait();
                        }
                    }
                    int currentTime = displaySecondsTimer - i - 1;
                    handler.post(() -> {
                        if (currentTime <= 10) {
                            displayTimer.setTextColor(Color.RED);
                        }
                        displayTimer.setText("" + currentTime);
                    });

                    // Change to draw mode
                    if (currentTime == 0) {
                        drawView.resetAll();
                        drawView.setIsEditable(true);
                        drawView.listenMultiplayerGameDrawOnce();
                        drawView.listenMultiplayerGameDraw();
                        handler.post(() -> {
                            displayTimer.setTextColor(Color.BLACK);
                            displayTimer.setText("Draw");
                            skipBtn.setVisibility(View.INVISIBLE);
                            skipBtn.setEnabled(false);
                            memorizeTV.setVisibility(View.INVISIBLE);
                            drawPalette.setVisibility(View.VISIBLE);
                            shakeToEraseSwitch.setVisibility(View.VISIBLE);
                            shakeToEraseSwitch.setEnabled(true);
                        });
                    }
                }
            } catch (InterruptedException e) {
                // Interrupted
            }
        }

        public void pauseTimer() {
            paused = true;
        }

        public void resumeTimer() {
            synchronized (lock) {
                paused = false;
                lock.notify();
            }
        }

        @Override
        public void interrupt() {
            try {
                // Change to draw mode
                drawView.resetAll();
                drawView.setIsEditable(true);
                drawView.listenMultiplayerGameDrawOnce();
                drawView.listenMultiplayerGameDraw();
                handler.post(() -> {
                    displayTimer.setTextColor(Color.BLACK);
                    displayTimer.setText("Draw");
                    skipBtn.setVisibility(View.INVISIBLE);
                    skipBtn.setEnabled(false);
                    memorizeTV.setVisibility(View.INVISIBLE);
                    drawPalette.setVisibility(View.VISIBLE);
                    shakeToEraseSwitch.setVisibility(View.VISIBLE);
                    shakeToEraseSwitch.setEnabled(true);
                });
            }
            finally {
                super.interrupt();
            }
        }
    }

    public void musicPlay() {
        if (player == null) {
            player = MediaPlayer.create(this, R.raw.puzzler);
        }
        player.start();
        player.setLooping(true);
    }
    public void musicStop() {
        if (player != null) {
            player.release();
            player = null;
            Toast.makeText(this, "Music off", Toast.LENGTH_SHORT);
        }
    }
}
