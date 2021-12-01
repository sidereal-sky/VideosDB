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

    public Command(ActionInputData action, Database database, Writer output,
				   JSONArray arrayResult) {
        super(action, database, output, arrayResult);
        this.username = action.getUsername();
        this.title = action.getTitle();
        this.grade = action.getGrade();
        this.seasonNumber = action.getSeasonNumber();
    }

    public void addFavourite(User user, Movie movie, Show show) {
        // the user has viewed the video
        if (user.getHistory().containsKey(title)) {
            if (!user.getFavourite().contains(title)) {
                user.getFavourite().add(title);
				if (movie != null) {
					movie.setFavCount(movie.getFavCount() + 1);
				} else if (show != null) {
					show.setFavCount(show.getFavCount() + 1);
				}
                message = "success -> " + title + " was added as favourite";
            } else {
                message = "error -> " + title + " is already in favourite list";
            }
        } else {
            message = "error -> " + title + " is not seen";
        }
        writeOutput();
    }

    public void viewVideo(User user, Movie movie, Show show) {
        if (user.getHistory().containsKey(title)) {
            user.getHistory().put(title, user.getHistory().get(title) + 1);
        } else {
            user.getHistory().put(title, 1);
        }
//		if (movie != null) {
//			movie.setViewCount(movie.getViewCount() + 1);
//		} else if (show != null) {
//			show.setViewCount(show.getViewCount() + 1);
//		}
        Integer views = user.getHistory().get(title);
        message = "success -> " + title + " was viewed with total views of "
				+ views;
        writeOutput();
    }

    public void rateMovie(User user, Movie movie) {
        // the user viewed the movie and hasn't rated it yet
        if (user.getHistory().containsKey(title)) {
			if (!user.getRatedMovies().contains(movie)) {
				user.getRatedMovies().add(movie);
				user.setNumberOfRatings(user.getNumberOfRatings() + 1);
				movie.getRatings().add(grade);
				message = "success -> " + title + " was rated with " +
						grade + " by " + username;
			} else {
				message = "error -> " + title + " has been already rated";
			}
        } else {
			message = "error -> " + title + " is not seen";
		}
		writeOutput();
    }

    public void rateShow(User user, Show show) {
		// the user viewed the show and hasn't rated that particular season yet
		if (user.getHistory().containsKey(title)) {
			ArrayList<Integer> seasonsRated = user.getRatedShows().get(title);
			if (seasonsRated == null) {
				seasonsRated = new ArrayList<>();
			} else if (seasonsRated.contains(seasonNumber)) {
				message = "error -> " + title + " has been already rated";
				writeOutput();
				return;
			}
			seasonsRated.add(seasonNumber);
			user.getRatedShows().put(title, seasonsRated);
			user.setNumberOfRatings(user.getNumberOfRatings() + 1);
			// add rating to the season's list of ratings
			show.getSeasons().get(seasonNumber - 1).getRatings().add(grade);
			message = "success -> " + title + " was rated with " +
					grade + " by " + username;
		} else {
			message = "error -> " + title + " is not seen";
		}
		writeOutput();
    }

    @Override
    public void execute(String type) {
        User user = getDatabase().findUser(username);
        if (user == null) {
            return;
        }

		Movie movie = getDatabase().findMovie(title);
		Show show = getDatabase().findShow(title);

        switch (type) {
            case "favorite":
                addFavourite(user, movie, show);
                break;
            case "view":
                viewVideo(user, movie, show);
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
        }
    }
}
