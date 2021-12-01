package action;

import database.Database;
import filter.FilterMovie;
import filter.FilterShow;
import entertainment.Movie;
import entertainment.Show;
import entertainment.Video;
import fileio.ActionInputData;
import fileio.Writer;
import org.json.simple.JSONArray;
import user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Recommendation extends Action {
    private String username;
    private String searchGenre;
    private FilterMovie filterMovie;
    private FilterShow filterShow;

    public Recommendation(final ActionInputData action, final Database database,
                          final Writer output, final JSONArray arrayResult) {
        super(action, database, output, arrayResult);
        this.username = action.getUsername();
        this.searchGenre = action.getGenre();
        filterMovie = new FilterMovie(database);
        filterShow = new FilterShow(database);
    }

    /**
     * @param user: user in need of a recommendation
     * @return their first unseen video from the database
     */
    public String getUnseenVideo(final User user) {
        for (Movie movie: getDatabase().getMyMovies()) {
            if (!user.getHistory().containsKey(movie.getTitle())) {
                return movie.getTitle();
            }
        }
        for (Show show: getDatabase().getMyShows()) {
            if (!user.getHistory().containsKey(show.getTitle())) {
                return show.getTitle();
            }
        }
        return null;
    }

    /**
     * @param user: user in need of a recom
     * @return the best rated unseen video from the db
     *         or
     *         the first unseen users' favorite video from the db
     *
     */
    public String getBestUnseenVideo(final User user) {
        ArrayList<Video> bestVideos = new ArrayList<>();
        // first elem will be a movie, and the second a show
        bestVideos.add(null);
        bestVideos.add(null);

        if (getType().equals("best_unseen")) {
            filterMovie.getVideoByRating(new ArrayList<>(), "desc");
        } else if (getType().equals("favorite")) {
            filterMovie.getFavouriteVideo(new ArrayList<>(),
                    "desc", "recom");
        }

        ArrayList<Movie> sortedMovies = filterMovie.getFilteredMovies();
        for (Movie movie: sortedMovies) {
            if (!user.getHistory().containsKey(movie.getTitle())) {
                bestVideos.set(0, movie);
                break;
            }
        }

        if (getType().equals("best_unseen")) {
            filterShow.getVideoByRating(new ArrayList<>(), "desc");
        } else if (getType().equals("favorite")) {
            filterShow.getFavouriteVideo(new ArrayList<>(), "desc", "recom");
        }

        ArrayList<Show> sortedShows = filterShow.getFilteredShows();
        for (Show show: sortedShows) {
            if (!user.getHistory().containsKey(show.getTitle())) {
                bestVideos.set(1, show);
                break;
            }
        }

        if (!bestVideos.contains(null)) {
            // got both a movie and a show in there
            if (getType().equals("best_unseen")) {
                if (bestVideos.get(0).getGrade()
                        >= bestVideos.get(1).getGrade()) {
                    return bestVideos.get(0).getTitle();
                } else {
                    return bestVideos.get(1).getTitle();
                }
            } else if (getType().equals("favorite")) {
                if (bestVideos.get(0).getFavCount()
                        >= bestVideos.get(1).getFavCount()) {
                    return bestVideos.get(0).getTitle();
                } else {
                    return bestVideos.get(1).getTitle();
                }
            }
        } else if (bestVideos.get(0) == null && bestVideos.get(1) != null) {
            return bestVideos.get(1).getTitle();
        } else if (bestVideos.get(1) == null && bestVideos.get(0) != null) {
            return bestVideos.get(0).getTitle();
        }

        return null;
    }

    /**
     * @param user: user in need of a recom
     * @return video title from a given genre that is the most popular
     */
    public String getPopularVideo(final User user) {
        HashMap<String, Integer> sortedGenres = getDatabase().getPopularGenres();

        for (Map.Entry<String, Integer> entry: sortedGenres.entrySet()) {
            for (Movie movie: getDatabase().getMyMovies()) {
                if (!user.getHistory().containsKey(movie.getTitle())
                        && movie.formattedGenres().contains(entry.getKey())) {
                    return movie.getTitle();
                }
            }

            for (Show show: getDatabase().getMyShows()) {
                if (!user.getHistory().containsKey(show.getTitle())
                        && show.formattedGenres().contains(entry.getKey())) {
                    return show.getTitle();
                }
            }
        }
        return null;
    }

    /**
     * @param user: user in need of a recom
     * @return list of video titles from a given genre
     */
    public ArrayList<String> searchVideos(final User user) {
        // simulate the filters list from the query action
        List<List<String>> filters = new ArrayList<>();
        List<String> year = new ArrayList<>();
        year.add(null);
        filters.add(year);
        List<String> genre = new ArrayList<>();
        genre.add(searchGenre);
        filters.add(genre);

        filterMovie.getVideoByRating(filters, "asc");
        ArrayList<Movie> movies = filterMovie.getFilteredMovies();

        filterShow.getVideoByRating(filters, "asc");
        ArrayList<Show> shows = filterShow.getFilteredShows();

        ArrayList<String> titles = new ArrayList<>();
        for (Movie movie: movies) {
            if (!user.getHistory().containsKey(movie.getTitle())) {
                titles.add(movie.getTitle());
            }
        }
        for (Show show: shows) {
            if (!user.getHistory().containsKey(show.getTitle())) {
                titles.add(show.getTitle());
            }
        }

        titles.sort(String::compareTo);
        return titles;
    }

    /**
     * give recommendations to the user
     */
    @Override
    public void execute() {
        User user = getDatabase().findUser(username);
        setMessage(getType().substring(0, 1).toUpperCase()
                + getType().substring(1) + "Recommendation");
        if (getType().equals("best_unseen")) {
            setMessage("BestRatedUnseenRecommendation");
        }

        if (getType().equals("standard")) {
            if (getUnseenVideo(user) == null) {
                setMessage(getMessage() + " cannot be applied!");
            } else {
                setMessage(getMessage() + " result: " + getUnseenVideo(user));
            }
        } else if (getType().equals("best_unseen")) {
            if (getBestUnseenVideo(user) == null) {
                setMessage(getMessage() + " cannot be applied!");
            } else {
                setMessage(getMessage() + " result: "
                        + getBestUnseenVideo(user));
            }
        } else if (user.getSubscription().equals("PREMIUM")) {
            switch (getType()) {
                case "popular":
                    if (getPopularVideo(user) == null) {
                        setMessage(getMessage() + " cannot be applied!");
                    } else {
                        setMessage(getMessage() + " result: "
                                + getPopularVideo(user));
                    }
                    break;
                case "favorite":
                    if (getBestUnseenVideo(user) == null) {
                        setMessage(getMessage() + " cannot be applied!");
                    } else {
                        setMessage(getMessage() + " result: "
                                + getBestUnseenVideo(user));
                    }
                    break;
                case "search":
                    if (searchVideos(user).isEmpty()) {
                        setMessage(getMessage() + " cannot be applied!");
                    } else {
                        setMessage(getMessage() + " result: "
                                + searchVideos(user));
                    }
                    break;
                default:
                    System.out.println("Wrong input");
            }
        } else {
            setMessage(getMessage() + " cannot be applied!");
        }
        writeOutput();
    }
}
