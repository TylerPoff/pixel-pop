package edu.northeastern.numad23sp_team26.a8_stickers.models;

public class StickerUser {

    String name, sent;
    int image;

    public StickerUser(String name, String sent, int image) {
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
