package edu.northeastern.numad23sp_team26;

public class StickerUserModel {

    String name, sent;
    int image;

    public StickerUserModel(String name, String sent, int image) {
        this.name = name;
        this.sent = sent;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSent() {
        return sent;
    }

    public void setSent(String sent) {
        this.sent = sent;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
