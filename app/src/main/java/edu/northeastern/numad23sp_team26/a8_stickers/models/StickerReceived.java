package edu.northeastern.numad23sp_team26.a8_stickers.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class StickerReceived implements Parcelable {

    private Sticker sticker;
    private User from;
    private String timeStamp;

    public StickerReceived() {
        // Default constructor required for calls to DataSnapshot.getValue(StickerSent.class)
    }

    public StickerReceived(Sticker sticker, User from, String timeStamp) {
        this.sticker = sticker;
        this.from = from;
        this.timeStamp = timeStamp;
    }

    private StickerReceived(Parcel in) {
        sticker = in.readParcelable(Sticker.class.getClassLoader());
        from = in.readParcelable(User.class.getClassLoader());
        timeStamp = in.readString();
    }

    public Sticker getSticker() {
        return this.sticker;
    }

    public User getFrom() {
        return this.from;
    }

    public String getTimeStamp() {
        return this.timeStamp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeParcelable(sticker, flags);
        dest.writeParcelable(from, flags);
        dest.writeSerializable(timeStamp);
    }

    public static final Parcelable.Creator<StickerReceived> CREATOR = new Parcelable.Creator<StickerReceived>() {

        @Override
        public StickerReceived createFromParcel(Parcel source) {
            return new StickerReceived(source);
        }

        @Override
        public StickerReceived[] newArray(int size) {
            return new StickerReceived[size];
        }
    };
}
