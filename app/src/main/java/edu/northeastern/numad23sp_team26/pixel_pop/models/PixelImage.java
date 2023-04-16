package edu.northeastern.numad23sp_team26.pixel_pop.models;

import java.util.List;

public class PixelImage {

    private String adventure;
    private int levelNum;
    private int displaySecondsTimer;
    private int drawSecondsTimer;
    private List<PixelCellDisplay> pixelCellsDisplay;

    public PixelImage(String adventure, int levelNum, int displaySecondsTimer, int drawSecondsTimer, List<PixelCellDisplay> pixelCellsDisplay) {
        this.adventure = adventure;
        this.levelNum = levelNum;
        this.displaySecondsTimer = displaySecondsTimer;
        this.drawSecondsTimer = drawSecondsTimer;
        this.pixelCellsDisplay = pixelCellsDisplay;
    }

    public String getAdventure() {
        return adventure;
    }

    public int getLevelNum() {
        return levelNum;
    }

    public int getDisplaySecondsTimer() {
        return displaySecondsTimer;
    }

    public List<PixelCellDisplay> getPixelCellsDisplay() {
        return pixelCellsDisplay;
    }
}
