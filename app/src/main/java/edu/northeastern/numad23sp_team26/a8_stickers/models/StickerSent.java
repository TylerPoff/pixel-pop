package edu.northeastern.numad23sp_team26.a8_stickers.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class StickerSent implements Parcelable {

    private Sticker sticker;
    private int totalCount;

    public StickerSent() {
        // Default constructor required for calls to DataSnapshot.getValue(StickerSent.class)
    }

    public StickerSent(Sticker sticker, int totalCount) {
        this.sticker = sticker;
        this.totalCount = totalCount;
    }

    private StickerSent(Parcel in) {
        sticker = in.readParcelable(Sticker.class.getClassLoader());
        totalCount = in.readInt();
    }

    public Sticker getSticker() {
        return this.sticker;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void sendOne() {
        totalCount++;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeParcelable(sticker, flags);
        dest.writeInt(totalCount);
    }

    public static final Parcelable.Creator<StickerSent> CREATOR = new Parcelable.Creator<StickerSent>() {

        @Override
        public StickerSent createFromParcel(Parcel source) {
            return new StickerSent(source);
        }

        @Override
        public StickerSent[] newArray(int size) {
            return new StickerSent[size];
        }
    };
}
