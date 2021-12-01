package filter;

import database.Database;
import entertainment.Movie;
import entertainment.Show;

import java.util.ArrayList;
import java.util.List;

public abstract class FilterVideo extends Filter {
	private ArrayList<Movie> filteredMovies;
	private ArrayList<Show> filteredShows;

	public FilterVideo(Database database) {
		super(database);
		filteredMovies = new ArrayList<>();
	}

	public ArrayList<Movie> getFilteredMovies() {
		return filteredMovies;
	}

	public void setFilteredMovies(
			ArrayList<Movie> filteredMovies) {
		this.filteredMovies = filteredMovies;
	}

	public ArrayList<Show> getFilteredShows() {
		return filteredShows;
	}

	public void setFilteredShows(ArrayList<Show> filteredShows) {
		this.filteredShows = filteredShows;
	}

	public void getVideoByRating(List<List<String>> filters,
											 String sortType) {

	}

	public void getFavouriteVideo(List<List<String>> filters,
								  String sortType, String type) {

	}

	public void getLongestVideo(List<List<String>> filters,
								String sortType) {

	}

	public void getMostViewedVideo(List<List<String>> filters,
								   String sortType) {

	}

	// SORT SHIT
}
