package edu.northeastern.numad23sp_team26.pixel_pop.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;

public class PixelPopUser implements Parcelable {

    public String email;
    public String profilePicture;
    public List<PixelScore> pixelScoreList;
    public List<PixelMultiScore> pixelMultiScoreList;

    public PixelPopUser() {
        // Default constructor required for calls to DataSnapshot.getValue(PixelPopUser.class)
    }

    public PixelPopUser(String email, String profilePicture, List<PixelScore> pixelScoreList, List<PixelMultiScore> pixelMultiScoreList) {
        this.email = email;
        this.profilePicture = profilePicture;
        this.pixelScoreList = pixelScoreList;
        this.pixelMultiScoreList = pixelMultiScoreList;
    }

    private PixelPopUser(Parcel in) {
        email = in.readString();
        profilePicture = in.readString();
        pixelScoreList = in.createTypedArrayList(PixelScore.CREATOR);
        pixelMultiScoreList = in.createTypedArrayList(PixelMultiScore.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(profilePicture);
        dest.writeTypedList(pixelScoreList);
        dest.writeTypedList(pixelMultiScoreList);
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
