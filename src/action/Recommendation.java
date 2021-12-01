package action;

import database.Database;
import entertainment.Movie;
import entertainment.Show;
import entertainment.Video;
import fileio.ActionInputData;
import fileio.Writer;
import org.checkerframework.checker.units.qual.C;
import org.json.simple.JSONArray;
import user.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Recommendation extends Action{
	private String username;
	private String searchGenre;

	public Recommendation(ActionInputData action, Database database,
						  Writer output,
						  JSONArray arrayResult) {
		super(action, database, output, arrayResult);
		this.username = action.getUsername();
		this.searchGenre = action.getGenre();
	}

	public String getUnseenVideo(User user) {
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

	public String getBestUnseenVideo(User user, String type) {
		ArrayList<Video> bestVideos = new ArrayList<>();
		bestVideos.add(null);
		bestVideos.add(null);

		ArrayList<Movie> sortedMovies = new ArrayList<>();

		if(type.equals("best_unseen")) {
			sortedMovies = getDatabase().getMoviesByRating(new ArrayList<>(), "desc");
		} else if (type.equals("favorite")) {
			sortedMovies = getDatabase().getFavouriteMovies(new ArrayList<>(), "desc", "recom");
		}

		for (Movie movie: sortedMovies) {
			if (!user.getHistory().containsKey(movie.getTitle())) {
				bestVideos.set(0, movie);
				break;
			}
		}

		ArrayList<Show> sortedShows = new ArrayList<>();

		if (type.equals("best_unseen")) {
			sortedShows = getDatabase().getShowsByRating(new ArrayList<>(), "desc");
		} else if (type.equals("favorite")) {
			sortedShows = getDatabase().getFavouriteShows(new ArrayList<>(), "desc", "recom");
		}

		for (Show show: sortedShows) {
			if(!user.getHistory().containsKey(show.getTitle())) {
				bestVideos.set(1, show);
				break;
			}
		}

		if (!bestVideos.contains(null)) {
			// got both a movie and a show in there
			if (type.equals("best_unseen")) {
				if (bestVideos.get(0).getGrade() >= bestVideos.get(1).getGrade()) {
					return bestVideos.get(0).getTitle();
				} else {
					return bestVideos.get(1).getTitle();
				}
			} else if (type.equals("favorite")) {
				if (bestVideos.get(0).getFavCount() >= bestVideos.get(1).getFavCount()) {
					return bestVideos.get(0).getTitle();
				} else {
					return bestVideos.get(1).getTitle();
				}
			}
		} else if (bestVideos.get(0) == null && bestVideos.get(1) != null) {
			return bestVideos.get(1).getTitle();
		} else if (bestVideos.get(1) == null && bestVideos.get(0) != null){
			return bestVideos.get(0).getTitle();
		}

		return null;
	}

	public String getPopularVideo (User user) {
		HashMap<String, Integer> sortedGenres = getDatabase().getPopularGenres();

		for (Map.Entry<String, Integer> entry: sortedGenres.entrySet()) {
			for (Movie movie: getDatabase().getMyMovies()) {
				if(!user.getHistory().containsKey(movie.getTitle()) &&
						movie.formattedGenres().contains(entry.getKey())) {
					return movie.getTitle();
				}
			}

			for (Show show: getDatabase().getMyShows()) {
				if(!user.getHistory().containsKey(show.getTitle()) &&
						show.formattedGenres().contains(entry.getKey())) {
					return show.getTitle();
				}
			}
		}
		return null;
	}

	public ArrayList<String> searchVideos(User user) {
		List<List<String>> filters = new ArrayList<>();
		List<String> year = new ArrayList<>();
		year.add(null);
		filters.add(year);
		List<String> genre = new ArrayList<>();
		genre.add(searchGenre);
		filters.add(genre);

		ArrayList<String> titles = new ArrayList<>();
		ArrayList<Movie> movies = getDatabase().getMoviesByRating(filters, "asc");
		ArrayList<Show> shows = getDatabase().getShowsByRating(filters, "asc");

		for (Movie movie: movies) {
			if(!user.getHistory().containsKey(movie.getTitle())) {
				titles.add(movie.getTitle());
			}
		}

		for (Show show: shows) {
			if(!user.getHistory().containsKey(show.getTitle())) {
				titles.add(show.getTitle());
			}
		}

		titles.sort(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
		});
		return titles;
	}

	@Override
	public void execute(String type) {
		User user = getDatabase().findUser(username);
		message = type.substring(0, 1).toUpperCase() + type.substring(1) + "Recommendation";
		if (type.equals("best_unseen")) {
			message = "BestRatedUnseenRecommendation ";
		}

		if (type.equals("standard")) {
			if (getUnseenVideo(user) == null) {
				message += " cannot be applied!";
			} else {
				message += " result: " + getUnseenVideo(user);
			}
//			writeOutput();
		} else if (type.equals("best_unseen")) {
			if (getBestUnseenVideo(user, type) == null) {
				message += "cannot be applied!";
			} else {
				message += "result: " + getBestUnseenVideo(user, type);
			}
//			writeOutput();
		} else if (user.getSubscription().equals("PREMIUM")) {
			if (type.equals("popular")) {
				if (getPopularVideo(user) == null) {
					message += " cannot be applied!";
				} else {
					message += " result: " + getPopularVideo(user);
				}
			} else if (type.equals("favorite")) {
				if (getBestUnseenVideo(user, type) == null) {
					message += " cannot be applied!";
				} else {
					message += " result: " + getBestUnseenVideo(user, type);
				}
			} else if (type.equals("search")) {
				if (searchVideos(user).isEmpty()) {
					message += " cannot be applied!";
				} else {
					message += " result: " + searchVideos(user);
				}
			}
		} else {
			message += " cannot be applied!";
		}
		writeOutput();
	}
}
