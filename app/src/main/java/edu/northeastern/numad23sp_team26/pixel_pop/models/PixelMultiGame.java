package edu.northeastern.numad23sp_team26.pixel_pop.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;

public class PixelMultiGame implements Parcelable {

    public String gameID;
    public String playerOneID;
    public String playerTwoID;
    public String adventure;
    public int levelNum;
    public PixelPopUserActions playerOneActions;
    public PixelPopUserActions playTwoActions;
    public List<PixelCellDisplay> pixelCellsState;

    public PixelMultiGame() {
        // Default constructor required for calls to DataSnapshot.getValue(PixelMultiGame.class)
    }

    public PixelMultiGame(String gameID, String playerOneID) {
        this.gameID = gameID;
        this.playerOneID = playerOneID;
        this.playerTwoID = "";
        this.adventure = "";
        this.levelNum = 0;
        this.playerOneActions = new PixelPopUserActions();
        this.playTwoActions = new PixelPopUserActions();
    }

    private PixelMultiGame(Parcel in) {
        gameID = in.readString();
        playerOneID = in.readString();
        playerTwoID = in.readString();
        adventure = in.readString();
        levelNum = in.readInt();
        playerOneActions = in.readParcelable(PixelPopUserActions.class.getClassLoader());
        playTwoActions = in.readParcelable(PixelPopUserActions.class.getClassLoader());
    }

    public void setAdventure(String adventure) {
        this.adventure = adventure;
    }

    public void setLevelNum(int levelNum) {
        this.levelNum = levelNum;
    }

    public void addPlayerTwo(String playerTwoID) {
        this.playerTwoID = playerTwoID;
    }

    public void setPixelCellsState(List<PixelCellDisplay> pixelCellsState) {
        // TODO: make this parcelable
        this.pixelCellsState = pixelCellsState;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(gameID);
        dest.writeString(playerOneID);
        dest.writeString(playerTwoID);
    }

    public static final Parcelable.Creator<PixelMultiGame> CREATOR = new Parcelable.Creator<PixelMultiGame>() {

        @Override
        public PixelMultiGame createFromParcel(Parcel source) {
            return new PixelMultiGame(source);
        }

        @Override
        public PixelMultiGame[] newArray(int size) {
            return new PixelMultiGame[size];
        }
    };
}
