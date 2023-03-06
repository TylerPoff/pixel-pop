package edu.northeastern.numad23sp_team26.a8_stickers.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;

public class User implements Parcelable {
    public String username;
    public String firstName;
    public String lastName;
    public List<StickerSent> stickersSent;
    public List<StickerReceived> stickersReceived;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String firstName, String lastName,
                List<StickerSent> stickersSent, List<StickerReceived> stickersReceived) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.stickersSent = stickersSent;
        this.stickersReceived = stickersReceived;
    }

    private User(Parcel in) {
        username = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        stickersSent = in.createTypedArrayList(StickerSent.CREATOR);
        stickersReceived = in.createTypedArrayList(StickerReceived.CREATOR);
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(firstName);
        dest.writeString(lastName);
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {

        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
