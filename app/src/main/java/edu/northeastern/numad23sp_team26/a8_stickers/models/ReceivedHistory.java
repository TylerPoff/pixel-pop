package edu.northeastern.numad23sp_team26.a8_stickers.models;

public class ReceivedHistory {

    private final String stickerIV;
    private final String stickerName;
    private final String stickerFrom;
    private final String stickerTimestamp;


    public ReceivedHistory(String stickerIV, String stickerName, String stickerFrom, String stickerTimestamp) {
        this.stickerIV = stickerIV;
        this.stickerName = stickerName;
        this.stickerFrom = stickerFrom;
        this.stickerTimestamp = stickerTimestamp;
    }

    public String getStickerIV() {
        return this.stickerIV;
    }
    public String getStickerName() { return this.stickerName; }
    public String getStickerFrom() {
        return this.stickerFrom;
    }
    public String getStickerTimestamp() {
        return this.stickerTimestamp;
    }
}
