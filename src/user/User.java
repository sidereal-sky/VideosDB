package user;

import entertainment.Movie;
import fileio.UserInputData;

import java.util.ArrayList;
import java.util.HashMap;

public class User {
    private String username, subscription;
    private HashMap<String, Integer> history;
    private ArrayList<String> favourite;
    private ArrayList<Movie> ratedMovies;
    private HashMap<String, ArrayList<Integer>> ratedShows;
    private Integer numberOfRatings;


    public User(final UserInputData user) {
        this.username = user.getUsername();
        this.subscription = user.getSubscriptionType();
        this.history = (HashMap<String, Integer>) user.getHistory();
        this.favourite = user.getFavoriteMovies();
        ratedMovies = new ArrayList<>();
        ratedShows = new HashMap<>();
        numberOfRatings = 0;
    }

    /**
     * @return String: user's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return String: user's subscription type
     */
    public String getSubscription() {
        return subscription;
    }

    /**
     * @return HashMap: user's history (video title -> number of views)
     */
    public HashMap<String, Integer> getHistory() {
        return history;
    }

    /**
     * @return String list: user's favourite video titles
     */
    public ArrayList<String> getFavourite() {
        return favourite;
    }

    /**
     * @return Movie list: movies the user has rated
     */
    public ArrayList<Movie> getRatedMovies() {
        return ratedMovies;
    }

    /**
     * @return HashMap: shows the user has rated
     * (video title -> list of seasons)
     */
    public HashMap<String, ArrayList<Integer>> getRatedShows() {
        return ratedShows;
    }

    /**
     * @return Integer: number of ratings the user has made
     */
    public Integer getNumberOfRatings() {
        return numberOfRatings;
    }

    /**
     * @param numberOfRatings: new number of ratings
     */
    public void setNumberOfRatings(final Integer numberOfRatings) {
        this.numberOfRatings = numberOfRatings;
    }
}
