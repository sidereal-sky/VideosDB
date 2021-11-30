package action;

import database.Database;
import entertainment.Movie;
import entertainment.Show;
import fileio.ActionInputData;
import fileio.Writer;
import org.json.simple.JSONArray;
import user.User;

import java.util.ArrayList;

public class Recommendation extends Action{
	private String username;

	public Recommendation(ActionInputData action, Database database,
						  Writer output,
						  JSONArray arrayResult) {
		super(action, database, output, arrayResult);
		this.username = action.getUsername();
	}

	public String getUnseenVideo(User user) {
		for (Movie movie: getDatabase().getMyMovies()) {
			if (!user.getHistory().containsKey(movie.getTitle())) {
//				System.out.println(movie.getTitle() + " " + username);
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

	public String getBestUnseenVideo(User user) {
		ArrayList<Movie> sortedMovies = getDatabase().
				getMoviesByRating(new ArrayList<>(), "desc");

		for (Movie movie: sortedMovies) {
			if (!user.getHistory().containsKey(movie.getTitle())) {
				return movie.getTitle();
			}
		}

		ArrayList<Show> sortedShows = getDatabase().
				getShowsByRating(new ArrayList<>(), "desc");

		for (Show show: sortedShows) {
			if(!user.getHistory().containsKey(show.getTitle())) {
				return show.getTitle();
			}
		}
		return null;
	}

	@Override
	public void execute(String type) {
		User user = getDatabase().findUser(username);

		if (type.equals("standard")) {
			message = "StandardRecommendation ";
			if (getBestUnseenVideo(user) == null) {
				message += "cannot be applied!";
			} else {
				message += "result: " + getUnseenVideo(user);
			}
			writeOutput();
		} else if (type.equals("best_unseen")) {
			message = "BestRatedUnseenRecommendation ";
			if (getBestUnseenVideo(user) == null) {
				message += "cannot be applied!";
			} else {
				message += "result: " + getBestUnseenVideo(user);
			}
			writeOutput();
		}
	}
}
