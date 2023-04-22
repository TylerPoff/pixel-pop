package edu.northeastern.numad23sp_team26.pixel_pop.models;

import android.os.Parcel;
import android.os.Parcelable;

public class PixelPopUserActions implements Parcelable {

    public boolean paused;
    public boolean skipCountdown;
    public boolean reset;
    public boolean done;

    public PixelPopUserActions() {
        paused = false;
        skipCountdown = false;
        reset = false;
        done = false;
    }

    private PixelPopUserActions(Parcel in) {
        paused = in.readInt() == 1;
        skipCountdown = in.readInt() == 1;
        reset = in.readInt() == 1;
        done = in.readInt() == 1;
    }

    public void setPaused(boolean isPaused) {
        paused = isPaused;
    }

    public void setSkipCountdown(boolean isSkipCountdown) {
        skipCountdown = isSkipCountdown;
    }

    public void setReset(boolean isReset) {
        reset = isReset;
    }

    public void setDone(boolean isDone) {
        done = isDone;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(paused ? 1 : 0);
        dest.writeInt(skipCountdown ? 1 : 0);
        dest.writeInt(reset ? 1 : 0);
        dest.writeInt(done ? 1 : 0);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PixelPopUserActions> CREATOR = new Creator<PixelPopUserActions>() {

        @Override
        public PixelPopUserActions createFromParcel(Parcel in) {
            return new PixelPopUserActions(in);
        }

        @Override
        public PixelPopUserActions[] newArray(int size) {
            return new PixelPopUserActions[size];
        }
    };
}
