package edu.northeastern.numad23sp_team26.pixel_pop.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class PixelMultiScore implements Parcelable {

    public String adventure;
    public int levelNum;
    public int accuracyPercent;
    public String timeStamp;
    public PixelPopUser otherPlayer;

    public PixelMultiScore() {
        // Default constructor required for calls to DataSnapshot.getValue(PixelMultiScore.class)
    }

    public PixelMultiScore(String adventure, int levelNum, int accuracyPercent, String timeStamp, PixelPopUser otherPlayer) {
        this.adventure = adventure;
        this.levelNum = levelNum;
        this.accuracyPercent = accuracyPercent;
        this.timeStamp = timeStamp;
        this.otherPlayer = otherPlayer;
    }

    private PixelMultiScore(Parcel in) {
        adventure = in.readString();
        levelNum = in.readInt();
        accuracyPercent = in.readInt();
        timeStamp = in.readString();
        otherPlayer = in.readParcelable(PixelPopUser.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(adventure);
        dest.writeInt(levelNum);
        dest.writeInt(accuracyPercent);
        dest.writeString(timeStamp);
        dest.writeParcelable(otherPlayer, flags);
    }

    public static final Parcelable.Creator<PixelMultiScore> CREATOR = new Parcelable.Creator<PixelMultiScore>() {

        @Override
        public PixelMultiScore createFromParcel(Parcel source) {
            return new PixelMultiScore(source);
        }

        @Override
        public PixelMultiScore[] newArray(int size) {
            return new PixelMultiScore[size];
        }
    };
}
