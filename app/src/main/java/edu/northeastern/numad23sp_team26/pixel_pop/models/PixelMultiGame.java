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
    public int isPlayerOneDone;
    public int isPlayerTwoDone;
    public List<PixelCellDisplay> playerOnePixelCellsState;
    public List<PixelCellDisplay> playerTwoPixelCellsState;

    public PixelMultiGame() {
        // Default constructor required for calls to DataSnapshot.getValue(PixelMultiGame.class)
    }

    public PixelMultiGame(String gameID, String playerOneID) {
        this.gameID = gameID;
        this.playerOneID = playerOneID;
        this.playerTwoID = "";
        this.adventure = "";
        this.levelNum = 0;
        this.isPlayerOneDone = 0;
        this.isPlayerTwoDone = 0;
    }

    private PixelMultiGame(Parcel in) {
        gameID = in.readString();
        playerOneID = in.readString();
        playerTwoID = in.readString();
        adventure = in.readString();
        levelNum = in.readInt();
        isPlayerOneDone = in.readInt();
        isPlayerTwoDone = in.readInt();
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
        dest.writeString(adventure);
        dest.writeInt(levelNum);
        dest.writeInt(isPlayerOneDone);
        dest.writeInt(isPlayerTwoDone);
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
