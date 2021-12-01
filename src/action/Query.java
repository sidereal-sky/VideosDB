package action;

import actor.Actor;
import database.Database;
import filter.FilterActors;
import filter.FilterMovie;
import filter.FilterShow;
import filter.FilterUsers;
import entertainment.Movie;
import entertainment.Show;
import fileio.ActionInputData;
import fileio.Writer;
import org.json.simple.JSONArray;
import user.User;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.min;

public class Query extends Action {
    private int number;
    private String sortType;
    private String criteria;
    private List<List<String>> filters;

    public Query(final ActionInputData action, final Database database,
                 final Writer output, final JSONArray arrayResult) {
        super(action, database, output, arrayResult);
        setType(action.getObjectType());
        number = action.getNumber();
        sortType = action.getSortType();
        criteria = action.getCriteria();
        filters = action.getFilters();
    }

    /**
     * @param sortedUsers: list of filtered and/or sorted users
     */
    public void getUsers(final ArrayList<User> sortedUsers) {
        ArrayList<String> queriedUsers = new ArrayList<>();
        sortedUsers.removeIf(s -> s.getNumberOfRatings() == 0);

        number = min(number, sortedUsers.size());
        for (int i = 0; i < number; i++) {
            queriedUsers.add(sortedUsers.get(i).getUsername());

        }
        setMessage(getMessage() + queriedUsers);
        writeOutput();
    }

    /**
     * @param sortedMovies: list of filtered and/or sorted movies
     */
    public void getMovies(final ArrayList<Movie> sortedMovies) {
        ArrayList<String> queriedMovies = new ArrayList<>();
        number = min(number, sortedMovies.size());

        for (int i = 0; i < number; i++) {
            queriedMovies.add(sortedMovies.get(i).getTitle());
        }
        setMessage(getMessage() + queriedMovies);
        writeOutput();
    }

    /**
     * @param sortedShows: list of filtered and/or sorted shows
     */
    public void getShows(final ArrayList<Show> sortedShows) {
        ArrayList<String> queriedShows = new ArrayList<>();
        number = min(number, sortedShows.size());

        for (int i = 0; i < number; i++) {
            queriedShows.add(sortedShows.get(i).getTitle());
        }
        setMessage(getMessage() + queriedShows);
        writeOutput();
    }

    /**
     * @param sortedActors: list of filtered and/or sorted actors
     */
    public void getActors(final ArrayList<Actor> sortedActors) {
        ArrayList<String> queriedActors = new ArrayList<>();
        number = min(number, sortedActors.size());

        for (int i = 0; i < number; i++) {
            queriedActors.add(sortedActors.get(i).getName());
        }
        setMessage(getMessage() + queriedActors);
        writeOutput();
    }

    /**
     * perform queries on: users, actors or videos
     */
    @Override
    public void execute() {
        setMessage("Query result: ");

        if (getType().equals("users")) {
            FilterUsers filterUsers = new FilterUsers(getDatabase());
            getUsers(filterUsers.getUsersByRating(sortType));
        } else if (getType().equals("actors")) {
            FilterActors filterActors = new FilterActors(getDatabase());
            switch (criteria) {
                case "average" -> getActors(
                        filterActors.getActorsByAvg(sortType));
                case "awards" -> getActors(
                        filterActors.getActorsByAwards(filters, sortType));
                case "filter_description" -> getActors(
                        filterActors.getActorsByKeywords(filters, sortType));
                default -> System.out.println("Wrong input");
            }
        } else {
            FilterMovie filterMovie = new FilterMovie(getDatabase());
            FilterShow filterShow = new FilterShow(getDatabase());
            switch (criteria) {
                case "ratings" -> {
                    filterMovie.getVideoByRating(filters, sortType);
                    filterMovie.getFilteredMovies().
                            removeIf(s -> s.getGrade() == 0);
                    filterShow.getVideoByRating(filters, sortType);
                    filterShow.getFilteredShows().
                            removeIf(s -> s.getGrade() == 0);
                }
                case "favorite" -> {
                    filterMovie.getFavouriteVideo(filters, sortType, "");
                    filterMovie.getFilteredMovies().
                            removeIf(s -> s.getFavCount() == 0);
                    filterShow.getFavouriteVideo(filters, sortType, "");
                    filterShow.getFilteredShows().
                            removeIf(s -> s.getFavCount() == 0);
                }
                case "longest" -> {
                    filterMovie.getLongestVideo(filters, sortType);
                    filterShow.getLongestVideo(filters, sortType);
                }
                default -> {
                    filterMovie.getMostViewedVideo(filters, sortType);
                    filterMovie.getFilteredMovies().
                            removeIf(s -> s.getViewCount() == 0);
                    filterShow.getMostViewedVideo(filters, sortType);
                    filterShow.getFilteredShows().
                            removeIf(s -> s.getViewCount() == 0);
                }
            }

            if (getType().equals("movies")) {
                getMovies(filterMovie.getFilteredMovies());
            } else {
                getShows(filterShow.getFilteredShows());
            }
        }
    }
}
