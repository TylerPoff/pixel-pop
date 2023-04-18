package edu.northeastern.numad23sp_team26.pixel_pop.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class PixelCellDisplay implements Cloneable, Parcelable {

    private int rowNum;
    private int colNum;
    private int color;

    public PixelCellDisplay() {
        // Default constructor required for calls to DataSnapshot.getValue(PixelCellDisplay.class)
    }

    public PixelCellDisplay(int rowNum, int colNum, int color) {
        this.rowNum = rowNum;
        this.colNum = colNum;
        this.color = color;
    }

    private PixelCellDisplay(Parcel in) {
        rowNum = in.readInt();
        colNum = in.readInt();
        color = in.readInt();
    }

    public int getRowNum() {
        return rowNum;
    }

    public int getColNum() {
        return colNum;
    }

    public int getColor() {
        return color;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof PixelCellDisplay)) {
            return false;
        }

        PixelCellDisplay c = (PixelCellDisplay) obj;

        return this.rowNum == c.rowNum && this.colNum == c.colNum && this.color == c.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rowNum, colNum, color);
    }

    @Override
    public PixelCellDisplay clone() {
        try {
            PixelCellDisplay clone = (PixelCellDisplay) super.clone();
            clone.rowNum = this.rowNum;
            clone.colNum = this.colNum;
            clone.color = this.color;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(rowNum);
        dest.writeInt(colNum);
        dest.writeInt(color);
    }

    public static final Parcelable.Creator<PixelCellDisplay> CREATOR = new Parcelable.Creator<PixelCellDisplay>() {

        @Override
        public PixelCellDisplay createFromParcel(Parcel source) {
            return new PixelCellDisplay(source);
        }

        @Override
        public PixelCellDisplay[] newArray(int size) {
            return new PixelCellDisplay[size];
        }
    };
}
