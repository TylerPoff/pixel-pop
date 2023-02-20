package edu.northeastern.numad23sp_team26;

public class Movie {
    private final String genre;
    private final String posterURL;
    private final String name;
    private final String year;
    private final String type;

    public Movie(String name, String year, String type, String posterURL, String genre) {
        this.genre = genre;
        this.posterURL = posterURL;
        this.name = name;
        this.type = type;
        this.year = year;
    }

    public String getGenre() {
        return this.genre;
    }
    public String getPosterURL() { return this.posterURL; }
    public String getName() {
        return this.name;
    }
    public String getYear() {
        return this.year;
    }
    public String getType() { return this.type; }
}
