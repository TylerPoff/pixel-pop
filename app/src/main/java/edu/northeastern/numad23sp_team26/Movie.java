package edu.northeastern.numad23sp_team26;

public class Movie {
    private final String genre;
    private final String posterURL;
    private final String name;
    private final String year;
    private final String imdbID;
    private final String type;


    public Movie(String name, String genre, String year, String imdbID
                 ,String type, String posterURL) {
        this.genre = genre;
        this.posterURL = posterURL;
        this.name = name;
        this.type = type;
        this.year =year;
        this.imdbID = imdbID;
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
    public String getImdbID() {
        return this.imdbID;
    }
}
