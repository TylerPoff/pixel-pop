package edu.northeastern.numad23sp_team26.pixel_pop.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class PixelScore implements Parcelable {

    public int accuracyPercent;
    public String timeStamp;

    public PixelScore() {
        // Default constructor required for calls to DataSnapshot.getValue(PixelScore.class)
    }

    public PixelScore(int accuracyPercent, String timeStamp) {
        this.accuracyPercent = accuracyPercent;
        this.timeStamp = timeStamp;
    }

    private PixelScore(Parcel in) {
        accuracyPercent = in.readInt();
        timeStamp = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(accuracyPercent);
        dest.writeString(timeStamp);
    }

    public static final Parcelable.Creator<PixelScore> CREATOR = new Parcelable.Creator<PixelScore>() {

        @Override
        public PixelScore createFromParcel(Parcel source) {
            return new PixelScore(source);
        }

        @Override
        public PixelScore[] newArray(int size) {
            return new PixelScore[size];
        }
    };
}
