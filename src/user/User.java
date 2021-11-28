package user;

import entertainment.Movie;
import entertainment.Season;
import entertainment.Show;
import fileio.UserInputData;

import java.util.ArrayList;
import java.util.HashMap;

public class User {
    private String username, subscription;
    private HashMap<String, Integer> history;
    private ArrayList<String> favourite;
    private ArrayList<Movie> ratedMovies;
    private HashMap<String, ArrayList<Integer>> ratedShows;


    public User(UserInputData user) {
        this.username = user.getUsername();
        this.subscription = user.getSubscriptionType();
        this.history = (HashMap<String, Integer>) user.getHistory();
        this.favourite = user.getFavoriteMovies();
        ratedMovies = new ArrayList<>();
        ratedShows = new HashMap<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSubscription() {
        return subscription;
    }

    public void setSubscription(String subscription) {
        this.subscription = subscription;
    }

    public HashMap<String, Integer> getHistory() {
        return history;
    }

    public void setHistory(HashMap<String, Integer> history) {
        this.history = history;
    }

    public ArrayList<String> getFavourite() {
        return favourite;
    }

    public void setFavourite(ArrayList<String> favourite) {
        this.favourite = favourite;
    }

    public ArrayList<Movie> getRatedMovies() {
        return ratedMovies;
    }

    public HashMap<String, ArrayList<Integer>> getRatedShows() {
        return ratedShows;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", subscription='" + subscription + '\'' +
                ", history=" + history +
                ", favourite=" + favourite +
                '}';
    }
}
