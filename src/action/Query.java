package action;

import actor.Actor;
import database.Database;
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

	public void sortUsers(final ArrayList<User> sortedUsers) {
		ArrayList<String> queriedUsers = new ArrayList<>();
		sortedUsers.removeIf(s -> s.getNumberOfRatings() == 0);

		number = min(number, sortedUsers.size());
		for (int i = 0; i < number; i++) {
			queriedUsers.add(sortedUsers.get(i).getUsername());

		}
		message = message + queriedUsers;
		writeOutput();
	}

	public void sortMovies(final ArrayList<Movie> sortedMovies,
						   final String criteria) {
		switch (criteria) {
			case "ratings" -> sortedMovies.removeIf(s -> s.getGrade() == 0);
			case "favorite" -> sortedMovies.removeIf(s -> s.getFavCount() == 0);
			case "most_viewed" -> sortedMovies.removeIf(
					s -> s.getViewCount() == 0);
		}

		number = min(number, sortedMovies.size());
		ArrayList<String> queriedMovies = new ArrayList<>();

		for (int i = 0; i < number; i++) {
			queriedMovies.add(sortedMovies.get(i).getTitle());
		}
		message = message + queriedMovies;
		writeOutput();
	}

	public void sortShows(ArrayList<Show> sortedShows, String criteria) {
		ArrayList<String> queriedShows = new ArrayList<>();

		if ("ratings".equals(criteria)) {
			sortedShows.removeIf(s -> s.getGrade() == 0);
		} else if ("favorite".equals(criteria)) {
			sortedShows.removeIf(s -> s.getFavCount() == 0);
		} else if ("most_viewed".equals(criteria)) {
			sortedShows.removeIf(s -> s.getViewCount() == 0);
		}

		number = min(number, sortedShows.size());

		for (int i = 0; i < number; i++) {
			queriedShows.add(sortedShows.get(i).getTitle());
		}
		message = message + queriedShows;
		writeOutput();
	}

	public void sortActors(ArrayList<Actor> sortedActors) {
		ArrayList<String> queriedActors = new ArrayList<>();
		number = min(number, sortedActors.size());
		for (int i = 0; i < number; i++) {
			queriedActors.add(sortedActors.get(i).getName());
		}
		message = message + queriedActors;
		writeOutput();
	}

	@Override
	public void execute(String type) {
		message = "Query result: ";
		switch (type) {
			case "users":
				sortUsers(getDatabase().getUsersByRating(sortType));
				break;
			case "movies":
				switch (criteria) {
					case "ratings" -> sortMovies(getDatabase().getMoviesByRating(filters, sortType),
							criteria);
					case "favorite" -> sortMovies(
							getDatabase().getFavouriteMovies(filters, sortType, ""), criteria);
					case "longest" -> sortMovies(
							getDatabase().getLongestMovies(filters, sortType), criteria);
					case "most_viewed" -> sortMovies(
							getDatabase().getMostViewedMovies(filters, sortType), criteria);
				}
				break;
			case "shows":
				switch (criteria) {
					case "ratings" -> sortShows(getDatabase().getShowsByRating(filters, sortType),
							criteria);
					case "favorite" -> sortShows(getDatabase().
									getFavouriteShows(filters, sortType, ""), criteria);
					case "longest" -> sortShows(getDatabase().getLongestShows(filters, sortType),
							criteria);
					case "most_viewed" -> sortShows(
							getDatabase().getMostViewedShows(filters, sortType), criteria);
				}
				break;
			case "actors":
				switch (criteria) {
					case "average" -> sortActors(getDatabase().getActorsByAvg(sortType));
					case "awards" -> sortActors(getDatabase().getActorsByAwards(filters, sortType));
					case "filter_description" -> sortActors(
							getDatabase().getActorsByKeywords(filters, sortType));
				}
				break;
		}
	}

	public int getNumber() {
		return number;
	}

	public String getSortType() {
		return sortType;
	}

	public String getCriteria() {
		return criteria;
	}

	public List<List<String>> getFilters() {
		return filters;
	}
}
