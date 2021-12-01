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


	public User(UserInputData user) {
		this.username = user.getUsername();
		this.subscription = user.getSubscriptionType();
		this.history = (HashMap<String, Integer>) user.getHistory();
		this.favourite = user.getFavoriteMovies();
		ratedMovies = new ArrayList<>();
		ratedShows = new HashMap<>();
		numberOfRatings = 0;
	}

	public String getUsername() {
		return username;
	}

	public String getSubscription() {
		return subscription;
	}

	public HashMap<String, Integer> getHistory() {
		return history;
	}

	public ArrayList<String> getFavourite() {
		return favourite;
	}

	public ArrayList<Movie> getRatedMovies() {
		return ratedMovies;
	}

	public HashMap<String, ArrayList<Integer>> getRatedShows() {
		return ratedShows;
	}

	public Integer getNumberOfRatings() {
		return numberOfRatings;
	}

	public void setNumberOfRatings(Integer numberOfRatings) {
		this.numberOfRatings = numberOfRatings;
	}
}
