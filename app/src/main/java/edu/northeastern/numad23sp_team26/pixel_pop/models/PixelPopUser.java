package edu.northeastern.numad23sp_team26.pixel_pop.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class PixelPopUser implements Parcelable {

    public String email;
    public String profilePicture;

    public PixelPopUser() {
        // Default constructor required for calls to DataSnapshot.getValue(PixelPopUser.class)
    }

    public PixelPopUser(String email, String profilePicture) {
        this.email = email;
        this.profilePicture = profilePicture;
    }

    private PixelPopUser(Parcel in) {
        email = in.readString();
        profilePicture = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(profilePicture);
    }

    public static final Parcelable.Creator<PixelPopUser> CREATOR = new Parcelable.Creator<PixelPopUser>() {

        @Override
        public PixelPopUser createFromParcel(Parcel source) {
            return new PixelPopUser(source);
        }

        @Override
        public PixelPopUser[] newArray(int size) {
            return new PixelPopUser[size];
        }
    };
}
