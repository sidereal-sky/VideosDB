package database;

import actor.Actor;
import entertainment.Movie;
import entertainment.Show;
import user.User;

import java.util.ArrayList;

public class Database {
    private ArrayList<User> myUsers;
    private ArrayList<Actor> myActors;
    private ArrayList<Movie> myMovies;
    private ArrayList<Show> myShows;

    public Database(ArrayList<User> myUsers, ArrayList<Actor> myActors,
                    ArrayList<Movie> myMovies, ArrayList<Show> myShows) {
        this.myUsers = myUsers;
        this.myActors = myActors;
        this.myMovies = myMovies;
        this.myShows = myShows;
    }

    public ArrayList<User> getMyUsers() {
        return myUsers;
    }

    public User findUser(String username) {
        for (User user: getMyUsers()) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public ArrayList<Actor> getMyActors() {
        return myActors;
    }

    public ArrayList<Movie> getMyMovies() {
        return myMovies;
    }

    public Movie findMovie(String videoTitle) {
        for (Movie movie: getMyMovies()) {
            if (movie.getTitle().equals(videoTitle)) {
                return movie;
            }
        }
        return null;
    }

    public ArrayList<Show> getMyShows() {
        return myShows;
    }

    public Show findShow(String videoTitle) {
        for (Show show: getMyShows()) {
            if (show.getTitle().equals(videoTitle)) {
                return show;
            }
        }
        return null;
    }
}
