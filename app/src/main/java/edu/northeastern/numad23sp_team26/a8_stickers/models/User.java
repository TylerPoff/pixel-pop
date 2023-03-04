package edu.northeastern.numad23sp_team26.a8_stickers.models;

// username, first and last name
public class User {
    public String username;
    public String firstName;
    public String lastName;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
        /***/
    }

    public User(String username, String firstName, String lastName) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
    }

}
