package action;

import database.Database;
import entertainment.Movie;
import entertainment.Show;
import fileio.ActionInputData;
import fileio.Writer;
import org.json.simple.JSONArray;
import user.User;

import java.util.ArrayList;

public class Command extends Action {
    private String username;
    private String title;
    private double grade;
    private int seasonNumber;

    public Command(final ActionInputData action, final Database database,
                   final Writer output, final JSONArray arrayResult) {
        super(action, database, output, arrayResult);
        this.username = action.getUsername();
        this.title = action.getTitle();
        this.grade = action.getGrade();
        this.seasonNumber = action.getSeasonNumber();
    }

    /**
     * @param user: the user that executes the favorite operation
     */
    public void addFavourite(final User user) {
        // the user has viewed the video
        if (user.getHistory().containsKey(title)) {
            if (!user.getFavourite().contains(title)) {
                user.getFavourite().add(title);
                setMessage("success -> " + title + " was added as favourite");
            } else {
                setMessage("error -> " + title + " is already in favourite list");
            }
        } else {
            setMessage("error -> " + title + " is not seen");
        }
        writeOutput();
    }

    /**
     * @param user: the user that executes the favorite operation
     */
    public void viewVideo(final User user) {
        if (user.getHistory().containsKey(title)) {
            user.getHistory().put(title, user.getHistory().get(title) + 1);
        } else {
            user.getHistory().put(title, 1);
        }
        Integer views = user.getHistory().get(title);
        setMessage("success -> " + title + " was viewed with total views of "
                + views);
        writeOutput();
    }

    /**
     * @param user: the user that executes the rate operation
     * @param movie: the movie they wish to rate
     */
    public void rateMovie(final User user, final Movie movie) {
        // the user viewed the movie and hasn't rated it yet
        if (user.getHistory().containsKey(title)) {
            if (!user.getRatedMovies().contains(movie)) {
                user.getRatedMovies().add(movie);
                user.setNumberOfRatings(user.getNumberOfRatings() + 1);
                movie.getRatings().add(grade);
                setMessage("success -> " + title + " was rated with "
                        + grade + " by " + username);
            } else {
                setMessage("error -> " + title + " has been already rated");
            }
        } else {
            setMessage("error -> " + title + " is not seen");
        }
        writeOutput();
    }

    /**
     * @param user: the user that executes the rate operation
     * @param show: the show they wish to rate
     */
    public void rateShow(final User user, final Show show) {
        // the user viewed the show and hasn't rated that particular season yet
        if (user.getHistory().containsKey(title)) {
            ArrayList<Integer> seasonsRated = user.getRatedShows().get(title);
            if (seasonsRated == null) {
                seasonsRated = new ArrayList<>();
            } else if (seasonsRated.contains(seasonNumber)) {
                setMessage("error -> " + title + " has been already rated");
                writeOutput();
                return;
            }
            seasonsRated.add(seasonNumber);
            user.getRatedShows().put(title, seasonsRated);
            user.setNumberOfRatings(user.getNumberOfRatings() + 1);
            // add rating to the season's list of ratings
            show.getSeasons().get(seasonNumber - 1).getRatings().add(grade);
            setMessage("success -> " + title + " was rated with "
                    + grade + " by " + username);
        } else {
            setMessage("error -> " + title + " is not seen");
        }
        writeOutput();
    }

    /**
     * perform command: favorite, view, rating
     */
    @Override
    public void execute() {
        User user = getDatabase().findUser(username);
        if (user == null) {
            return;
        }
        Movie movie = getDatabase().findMovie(title);
        Show show = getDatabase().findShow(title);
        switch (getType()) {
            case "favorite":
                addFavourite(user);
                break;
            case "view":
                viewVideo(user);
                break;
            case "rating":
                if (movie != null || show != null) {
                    if (seasonNumber > 0) {
                        rateShow(user, show);
                    } else {
                        rateMovie(user, movie);
                    }
                }
                break;
            default:
                System.out.println("Wrong input");
        }
    }
}
