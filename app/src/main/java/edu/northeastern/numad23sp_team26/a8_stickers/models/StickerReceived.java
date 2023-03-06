package edu.northeastern.numad23sp_team26.a8_stickers.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class StickerReceived implements Parcelable {

    private Sticker sticker;
    private User from;
    private String timeStamp;
    public int isNotified;

    public StickerReceived() {
        // Default constructor required for calls to DataSnapshot.getValue(StickerSent.class)
    }

    public StickerReceived(Sticker sticker, User from, String timeStamp, int isNotified) {
        this.sticker = sticker;
        this.from = from;
        this.timeStamp = timeStamp;
        this.isNotified = isNotified;
    }

    private StickerReceived(Parcel in) {
        sticker = in.readParcelable(Sticker.class.getClassLoader());
        from = in.readParcelable(User.class.getClassLoader());
        timeStamp = in.readString();
        isNotified = in.readInt();
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
    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof StickerReceived)) {
            return false;
        }

        StickerReceived other = (StickerReceived) obj;
        return sticker.getFileName().equalsIgnoreCase(other.getSticker().getFileName())
                && from.username.equalsIgnoreCase(other.from.username)
                && timeStamp.equalsIgnoreCase(other.timeStamp);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(sticker) + Objects.hashCode(from) + Objects.hashCode(timeStamp);
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
        dest.writeInt(isNotified);
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
