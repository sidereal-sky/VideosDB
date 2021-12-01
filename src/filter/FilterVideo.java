package filter;

import database.Database;
import entertainment.Movie;
import entertainment.Show;

import java.util.ArrayList;
import java.util.List;

public abstract class FilterVideo extends Filter {
    private ArrayList<Movie> filteredMovies;
    private ArrayList<Show> filteredShows;

    public FilterVideo(final Database database) {
        super(database);
        filteredMovies = new ArrayList<>();
        filteredShows = new ArrayList<>();
    }

    /**
     * @return list of filtered and/or sorted movies
     */
    public ArrayList<Movie> getFilteredMovies() {
        return filteredMovies;
    }

    /**
     * @param filteredMovies: new list of filtered and/or sorted movies
     */
    public void setFilteredMovies(final ArrayList<Movie> filteredMovies) {
        this.filteredMovies = filteredMovies;
    }

    /**
     * @return list of filtered and/or sorted shows
     */
    public ArrayList<Show> getFilteredShows() {
        return filteredShows;
    }

    /**
     * @param filteredShows: new list of filtered and/or sorted shows
     */
    public void setFilteredShows(final ArrayList<Show> filteredShows) {
        this.filteredShows = filteredShows;
    }

    /**
     * sort videos by rating
     */
    public void getVideoByRating(final List<List<String>> filters,
                                 final String sortType) {

    }

    /**
     * sort videos by number of times they appear in users' favourite list
     */
    public void getFavouriteVideo(final List<List<String>> filters,
                                  final String sortType,
                                  final String type) {

    }

    /**
     * sort videos by their duration
     */
    public void getLongestVideo(final List<List<String>> filters,
                                final String sortType) {

    }

    /**
     * sort videos by the number of times they have been viewed
     */
    public void getMostViewedVideo(final List<List<String>> filters,
                                   final String sortType) {

    }
}
