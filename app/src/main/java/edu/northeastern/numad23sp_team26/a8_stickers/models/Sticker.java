package edu.northeastern.numad23sp_team26.a8_stickers.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Sticker implements Parcelable {

    private String name;
    private String fileName;
    private int imageResource;

    public Sticker() {
        // Default constructor required for calls to DataSnapshot.getValue(StickerSent.class)
    }

    public Sticker(String name, String fileName, int imageResource) {
        this.name = name;
        this.fileName = fileName;
        this.imageResource = imageResource;
    }

    private Sticker(Parcel in) {
        name = in.readString();
        fileName = in.readString();
        imageResource = in.readInt();
    }

    public String getName() {
        return this.name;
    }

    public String getFileName() {
        return this.fileName;
    }

    public int getImageResource() {
        return this.imageResource;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(fileName);
        dest.writeInt(imageResource);
    }

    public static final Parcelable.Creator<Sticker> CREATOR = new Parcelable.Creator<Sticker>() {

        @Override
        public Sticker createFromParcel(Parcel source) {
            return new Sticker(source);
        }

        @Override
        public Sticker[] newArray(int size) {
            return new Sticker[size];
        }
    };
}
